package com.example.controlevendas

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.database.sqlite.SQLiteDatabase
import java.io.File
import java.io.FileOutputStream

/** SQLite financeiro preservado; formato 2 adiciona apenas identidade nos metadados. */
class BackupManager(private val context: Context) {
    companion object {
        const val FORMAT_VERSION = 2
        const val FILE_PREFIX = "VendasSimples_Backup_"
        const val METADATA_TABLE = "VENDAS_SIMPLES_BACKUP_METADATA"
        const val SCHEMA_VERSION = 3
    }
    private fun transaction() = RestoreTransaction(
        File(context.filesDir, "restore_transaction"),
        context.getDatabasePath(LocalDatabase.DB_NAME), BusinessPreferences(context)
    )
    fun recoverPendingRestore(): Result<Unit> = runCatching { transaction().recover() }

    fun createBackup(destination: Uri, database: LocalDatabase, entitlement: EntitlementState): Result<Unit> = runCatching {
        requireFullAccess(entitlement)
        checkpoint(database.writableDatabase)
        val staged = File.createTempFile("vendas_backup_", ".db", context.cacheDir)
        try {
            context.getDatabasePath(LocalDatabase.DB_NAME).copyTo(staged, true)
            SQLiteDatabase.openDatabase(staged.path, null, SQLiteDatabase.OPEN_READWRITE).use { copy ->
                copy.beginTransaction()
                try {
                    copy.execSQL("DROP TABLE IF EXISTS $METADATA_TABLE")
                    copy.execSQL("CREATE TABLE $METADATA_TABLE (format_version INTEGER NOT NULL, schema_version INTEGER NOT NULL, business_name TEXT)")
                    val values = ContentValues().apply {
                        put("format_version", FORMAT_VERSION)
                        put("schema_version", SCHEMA_VERSION)
                        put("business_name", BusinessPreferences(context).businessName)
                    }
                    copy.insertOrThrow(METADATA_TABLE, null, values)
                    copy.setTransactionSuccessful()
                } finally { copy.endTransaction() }
                integrityCheck(copy)
                checkpoint(copy)
            }
            validateFile(staged)
            context.contentResolver.openOutputStream(destination, "wt")?.use { out ->
                staged.inputStream().use { it.copyTo(out) }
            } ?: error("Não foi possível abrir o destino")
        } finally { removeTemporary(staged) }
    }

    fun validate(source: Uri): Result<Unit> = runCatching {
        val temp = File.createTempFile("vendas_validate_", ".db", context.cacheDir)
        try { copySource(source, temp); validateFile(temp); Unit }
        finally { removeTemporary(temp) }
    }

    private fun validateFile(file: File): String? {
        SQLiteDatabase.openDatabase(file.path, null, SQLiteDatabase.OPEN_READONLY).use { db ->
            integrityCheck(db)
            require(db.version == SCHEMA_VERSION) { "Schema SQLite incompatível" }
            val required = mapOf(
                "CLIENTES" to setOf("id_cliente", "nome"),
                "VENDAS" to setOf("id_venda", "id_cliente", "descricao", "data_venda", "data_vencimento", "valor_total", "valor_total_centavos", "parcela_atual", "parcelas", "id_venda_pai"),
                "PAGAMENTOS" to setOf("id_pagamento", "id_venda", "data_pagamento", "valor_pago", "valor_pago_centavos"),
                "FOTOS_VENDA" to setOf("id_foto", "id_venda_pai", "imagem"),
                "SYNC_DIRTY" to setOf("entity_type", "entity_id"),
                "SYNC_DELETIONS" to setOf("entity_type", "entity_id"),
                METADATA_TABLE to setOf("format_version", "schema_version")
            )
            for ((table, columns) in required) {
                val found = mutableSetOf<String>()
                db.rawQuery("PRAGMA table_info($table)", null).use { c ->
                    while (c.moveToNext()) found += c.getString(1)
                }
                require(found.containsAll(columns)) { "Backup Vendas Simples incompatível" }
            }
            var version = 0
            db.rawQuery("SELECT format_version, schema_version FROM $METADATA_TABLE", null).use { c ->
                require(c.count == 1 && c.moveToFirst()) { "Metadados de backup inválidos" }
                version = c.getInt(0)
                require(version in 1..FORMAT_VERSION && c.getInt(1) == SCHEMA_VERSION) { "Versão de backup incompatível" }
            }
            if (version == 1) return null
            db.rawQuery("SELECT business_name FROM $METADATA_TABLE", null).use { c ->
                check(c.moveToFirst())
                if (c.isNull(0)) return null
                require(c.getType(0) == android.database.Cursor.FIELD_TYPE_STRING) { "Nome do negócio inválido" }
                val name = c.getString(0)
                require(name.length <= BusinessIdentity.MAX_NAME_LENGTH) { "Nome do negócio inválido" }
                return BusinessIdentity.normalize(name).takeIf { it.isNotEmpty() }
            }
        }
    }

    fun restore(source: Uri, database: LocalDatabase, entitlement: EntitlementState): Result<Unit> = runCatching {
        requireFullAccess(entitlement)
        val current = context.getDatabasePath(LocalDatabase.DB_NAME)
        database.writableDatabase
        val incoming = File.createTempFile("vendas_incoming_", ".db", current.parentFile)
        try {
            copySource(source, incoming)
            val restoredName = validateFile(incoming)
            checkpoint(database.writableDatabase)
            database.close()
            File(current.path + "-wal").delete()
            File(current.path + "-shm").delete()
            transaction().replace(incoming, restoredName)
        } finally { removeTemporary(incoming) }
    }

    private fun requireFullAccess(state: EntitlementState) {
        require(state == EntitlementState.FULL_ACCESS) { "Backup e restauração exigem Licença Completa" }
    }
    private fun checkpoint(db: SQLiteDatabase) {
        db.rawQuery("PRAGMA wal_checkpoint(FULL)", null).use { c ->
            require(c.moveToFirst() && c.getInt(0) == 0) { "Banco ocupado; tente novamente" }
        }
    }
    private fun integrityCheck(db: SQLiteDatabase) {
        db.rawQuery("PRAGMA integrity_check", null).use { c ->
            require(c.count == 1 && c.moveToFirst() && c.getString(0) == "ok") { "Integridade SQLite inválida" }
        }
    }
    private fun copySource(source: Uri, target: File) {
        context.contentResolver.openInputStream(source)?.use { input ->
            FileOutputStream(target).use { output -> input.copyTo(output); output.fd.sync() }
        } ?: error("Arquivo inacessível")
    }
    private fun removeTemporary(file: File) {
        file.delete()
        File(file.path + "-wal").delete()
        File(file.path + "-shm").delete()
        File(file.path + "-journal").delete()
    }
}
