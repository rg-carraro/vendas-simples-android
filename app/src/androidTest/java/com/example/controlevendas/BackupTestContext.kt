package com.example.controlevendas

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import java.io.File

/** Namespace de teste separado: nenhum banco/preferência operacional é aberto ou apagado. */
class BackupTestContext(base: Context) : ContextWrapper(base) {
    init { check(base.packageName == "com.vendassimples.app" || base.packageName == "com.vendassimples.app.fase16") }
    override fun getPackageName() = "com.vendassimples.app.backup_tests.test"
    override fun getFilesDir() = File(baseContext.cacheDir, "backup-test-files").apply { mkdirs() }
    override fun getCacheDir() = File(baseContext.cacheDir, "backup-test-cache").apply { mkdirs() }
    override fun getDatabasePath(name: String) = File(
        File(baseContext.cacheDir, "backup-test-databases").apply { mkdirs() }, name
    )
    override fun openOrCreateDatabase(name: String, mode: Int, factory: SQLiteDatabase.CursorFactory?): SQLiteDatabase =
        SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory)
    override fun openOrCreateDatabase(name: String, mode: Int, factory: SQLiteDatabase.CursorFactory?, errorHandler: DatabaseErrorHandler?): SQLiteDatabase =
        SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name).path, factory, errorHandler)
    override fun deleteDatabase(name: String) = SQLiteDatabase.deleteDatabase(getDatabasePath(name))
    override fun getSharedPreferences(name: String, mode: Int): SharedPreferences =
        baseContext.getSharedPreferences("backup_test_only_$name", mode)
}
