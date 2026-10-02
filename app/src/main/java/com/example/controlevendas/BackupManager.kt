package com.example.controlevendas

import android.content.Context
import android.net.Uri
import android.database.sqlite.SQLiteDatabase
import java.io.File
import java.io.FileOutputStream

/** Backup local versionado por schema; não contém nem determina entitlement. */
class BackupManager(private val context: Context) {
    companion object {
        const val FORMAT_VERSION = 1
        const val FILE_PREFIX = "VendasSimples_Backup_"
        const val METADATA_TABLE = "VENDAS_SIMPLES_BACKUP_METADATA"
        const val SCHEMA_VERSION = 3
    }
    fun createBackup(destination: Uri, database: LocalDatabase): Result<Unit> = runCatching {
        val db = database.writableDatabase
        db.rawQuery("PRAGMA wal_checkpoint(FULL)", null).use { it.moveToFirst() }
        db.execSQL("PRAGMA foreign_keys=ON")
        val source = context.getDatabasePath(LocalDatabase.DB_NAME)
        val staged = File.createTempFile("vendas_backup_", ".db", context.cacheDir)
        try {
            source.copyTo(staged, true)
            SQLiteDatabase.openDatabase(staged.path, null, SQLiteDatabase.OPEN_READWRITE).use { copy ->
                copy.execSQL("CREATE TABLE IF NOT EXISTS $METADATA_TABLE (format_version INTEGER NOT NULL, schema_version INTEGER NOT NULL)")
                copy.execSQL("DELETE FROM $METADATA_TABLE")
                copy.execSQL("INSERT INTO $METADATA_TABLE VALUES (?, ?)", arrayOf(FORMAT_VERSION, SCHEMA_VERSION))
                copy.rawQuery("PRAGMA integrity_check", null).use { it.moveToFirst() }
            }
            context.contentResolver.openOutputStream(destination)?.use { out -> staged.inputStream().use { it.copyTo(out) } }
                ?: error("Não foi possível abrir o destino")
        } finally { staged.delete() }
    }
    fun validate(source: Uri): Result<Unit> = runCatching {
        val temp = File.createTempFile("vendas_restore_", ".db", context.cacheDir)
        try {
            context.contentResolver.openInputStream(source)?.use { input -> FileOutputStream(temp).use { input.copyTo(it) } } ?: error("Arquivo inacessível")
            SQLiteDatabase.openDatabase(temp.path, null, SQLiteDatabase.OPEN_READONLY).use { db ->
                val required = setOf("CLIENTES", "VENDAS", "PAGAMENTOS", "FOTOS_VENDA")
                val found = mutableSetOf<String>()
                db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null).use { c -> while (c.moveToNext()) found += c.getString(0) }
                require(found.containsAll(required) && METADATA_TABLE in found) { "Backup Vendas Simples incompatível" }
                db.rawQuery("SELECT format_version, schema_version FROM $METADATA_TABLE LIMIT 1", null).use { c ->
                    require(c.moveToFirst() && c.getInt(0) == FORMAT_VERSION && c.getInt(1) == SCHEMA_VERSION) { "Versão de backup incompatível" }
                }
                db.rawQuery("PRAGMA integrity_check", null).use { c -> require(c.moveToFirst() && c.getString(0) == "ok") { "Integridade SQLite inválida" } }
            }
        } finally { temp.delete() }
    }
    fun restore(source: Uri, database: LocalDatabase): Result<Unit> = runCatching {
        validate(source).getOrThrow()
        val current = context.getDatabasePath(LocalDatabase.DB_NAME)
        val rollback = File.createTempFile("vendas_rollback_", ".db", context.cacheDir)
        val incoming = File.createTempFile("vendas_incoming_", ".db", context.cacheDir)
        try {
            database.close()
            current.copyTo(rollback, true)
            File(current.path + "-wal").delete()
            File(current.path + "-shm").delete()
            context.contentResolver.openInputStream(source)?.use { input -> FileOutputStream(incoming).use { input.copyTo(it) } } ?: error("Arquivo inacessível")
            if (!current.delete()) error("Não foi possível preparar a substituição do banco")
            if (!incoming.renameTo(current)) error("Não foi possível substituir o banco")
        } catch (e: Exception) { if (rollback.exists()) rollback.copyTo(current, true); throw e }
        finally { rollback.delete(); incoming.delete() }
    }
}
