package com.example.controlevendas

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.database.sqlite.SQLiteDatabase
import java.io.File
import java.io.FileOutputStream

/** SQLite financeiro preservado; formato 2 adiciona apenas identidade nos metadados. */
class BackupManager internal constructor(
    private val context: Context,
    private val transfer: BackupTransfer,
    private val readSource: (Uri) -> java.io.InputStream?,
    private val freeBytes: (File) -> Long
) {
    constructor(context: Context) : this(context, BackupTransfer(),
        { context.contentResolver.openInputStream(it) }, { it.usableSpace })
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
        val source = context.getDatabasePath(LocalDatabase.DB_NAME)
        transfer.requireSize(source.length())
        transfer.requireSpace(freeBytes(context.cacheDir), source.length())
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
            transfer.requireSize(staged.length())
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
        return SQLiteDatabase.openDatabase(file.path, null, SQLiteDatabase.OPEN_READONLY).use {
            BackupValidator(context).validate(it)
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
            transfer.requireSpace(freeBytes(current.parentFile!!), Math.multiplyExact(current.length(), 2))
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
        readSource(source)?.use { input ->
            FileOutputStream(target).use { output ->
                transfer.copy(input, output) { freeBytes(target.parentFile!!) }
                output.fd.sync()
            }
        } ?: error("Arquivo inacessível")
    }
    private fun removeTemporary(file: File) {
        file.delete()
        File(file.path + "-wal").delete()
        File(file.path + "-shm").delete()
        File(file.path + "-journal").delete()
    }
}
