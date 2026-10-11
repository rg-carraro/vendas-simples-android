package com.example.controlevendas

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.util.Base64
import java.io.File

/** Opera no namespace sintético BackupTestContext; nunca abre banco/preferências operacionais. */
class BackupInstrumentedTests(private val context: Context) {
    private lateinit var database: LocalDatabase
    private val manager get() = BackupManager(context)
    private val preferences get() = BusinessPreferences(context)
    private val full = EntitlementState.FULL_ACCESS
    private val name = "João & Jóias — Ação #1"
    private val backup get() = File(context.filesDir, "synthetic_backup.db")
    private val identity get() = context.getSharedPreferences("business_identity", Context.MODE_PRIVATE)

    fun prepare() {
        check(context.packageName.endsWith(".test")) { "Recusa armazenamento de produção" }
        context.deleteDatabase(LocalDatabase.DB_NAME)
        check(identity.edit().clear().commit())
        File(context.filesDir, "restore_transaction").listFiles()?.forEach { check(it.delete()) }
        backup.delete()
        database = LocalDatabase(context)
        database.writableDatabase
    }
    fun close() { if (::database.isInitialized) database.close() }

    private fun seed() {
        database.novaVenda(NovaVendaRequest("Cliente sintético", "Três parcelas", "2026-10-11", 10_000L, 3), listOf(byteArrayOf(1, 2, 3, 4)))
        val first = database.getRelatorio().first()
        database.novoPagamento(NovoPagamentoRequest(first.id_venda.toLong().toString(), "2026-10-12", 1_000L))
        database.novoPagamento(NovoPagamentoRequest(first.id_venda.toLong().toString(), "2026-10-13", 500L))
    }
    private fun create() {
        preferences.restoreState(BusinessPreferenceState(name, true))
        manager.createBackup(Uri.fromFile(backup), database, full).getOrThrow()
    }
    private fun restore() {
        manager.restore(Uri.fromFile(backup), database, full).getOrThrow()
        database = LocalDatabase(context)
    }
    private fun oldFormat() {
        SQLiteDatabase.openDatabase(backup.path, null, SQLiteDatabase.OPEN_READWRITE).use {
            it.execSQL("DROP TABLE ${BackupManager.METADATA_TABLE}")
            it.execSQL("CREATE TABLE ${BackupManager.METADATA_TABLE} (format_version INTEGER NOT NULL, schema_version INTEGER NOT NULL)")
            it.execSQL("INSERT INTO ${BackupManager.METADATA_TABLE} VALUES (1, 3)")
        }
    }
    private fun financialSnapshot(db: SQLiteDatabase): List<String> =
        listOf("CLIENTES", "VENDAS", "PAGAMENTOS", "FOTOS_VENDA", "SYNC_DIRTY", "SYNC_DELETIONS").flatMap { table ->
            val rows = mutableListOf<String>()
            db.rawQuery("SELECT * FROM $table ORDER BY rowid", null).use { c ->
                while (c.moveToNext()) rows += table + (0 until c.columnCount).joinToString("|") { column ->
                    when (c.getType(column)) {
                        Cursor.FIELD_TYPE_NULL -> "<null>"
                        Cursor.FIELD_TYPE_BLOB -> Base64.encodeToString(c.getBlob(column), Base64.NO_WRAP)
                        else -> c.getString(column)
                    }
                }
            }
            rows
        }

    fun testBackupContainsNameAndAllFinancialRows() {
        seed()
        val before = financialSnapshot(database.readableDatabase)
        create()
        SQLiteDatabase.openDatabase(backup.path, null, SQLiteDatabase.OPEN_READONLY).use {
            check(before == financialSnapshot(it))
            it.rawQuery("SELECT format_version, business_name FROM ${BackupManager.METADATA_TABLE}", null).use { c ->
                check(c.moveToFirst() && c.getInt(0) == 2 && c.getString(1) == name)
            }
        }
    }
    fun testRestoreNewInstallationAndAccents() {
        seed(); create()
        close(); context.deleteDatabase(LocalDatabase.DB_NAME)
        check(identity.edit().clear().commit())
        database = LocalDatabase(context)
        restore()
        check(preferences.businessName == name && preferences.initialSetupOffered)
        check(BusinessIdentity.thanks(preferences.businessName).endsWith(name))
        check(preferences.displayName == name)
        check(database.getRelatorio().size == 3)
    }
    fun testRestorePreservesInstallmentsPaymentsPhotosAndLicense() {
        seed()
        val before = financialSnapshot(database.readableDatabase)
        create()
        preferences.businessName = "Outro negócio"
        check(identity.edit().putString("unrelated_license_sentinel", "untouched").commit())
        val license = context.getSharedPreferences("license_test_sentinel", Context.MODE_PRIVATE)
        check(license.edit().putString("state", "FULL_ACCESS_SENTINEL").commit())
        restore()
        check(before == financialSnapshot(database.readableDatabase))
        val rows = database.getRelatorio()
        check(rows.sumOf { it.valor_total } == 10_000L)
        check(rows.sumOf { it.total_pago } == 1_500L)
        check(rows.sumOf { it.saldo } == 8_500L)
        check(database.getFotosVenda(rows.first().id_venda_pai).single().contentEquals(byteArrayOf(1, 2, 3, 4)))
        check(identity.getString("unrelated_license_sentinel", "") == "untouched")
        check(license.getString("state", "") == "FULL_ACCESS_SENTINEL")
        check(preferences.businessName == name)
    }
    fun testOldBackupPreservesCurrentNameAndFinancialData() {
        seed(); create(); oldFormat()
        val before = financialSnapshot(database.readableDatabase)
        preferences.restoreState(BusinessPreferenceState("Nome atual", true))
        restore()
        check(preferences.businessName == "Nome atual")
        check(before == financialSnapshot(database.readableDatabase))
    }
    fun testOldBackupOnNewInstallationUsesGenericName() {
        seed(); create(); oldFormat()
        close(); context.deleteDatabase(LocalDatabase.DB_NAME)
        check(identity.edit().clear().commit())
        database = LocalDatabase(context)
        restore()
        check(preferences.businessName.isEmpty())
        check(preferences.displayName == "Vendas Simples")
        check(database.getRelatorio().size == 3)
    }
    fun testBlankAndNullNamesNeverEraseCurrentName() {
        create()
        for (value in listOf("   ", null)) {
            SQLiteDatabase.openDatabase(backup.path, null, SQLiteDatabase.OPEN_READWRITE).use {
                it.execSQL("UPDATE ${BackupManager.METADATA_TABLE} SET business_name=?", arrayOf(value))
            }
            preferences.restoreState(BusinessPreferenceState("Preservado", true))
            restore()
            check(preferences.businessName == "Preservado")
        }
    }
    fun testCorruptBackupPreservesDatabaseAndName() {
        seed()
        val before = financialSnapshot(database.readableDatabase)
        preferences.restoreState(BusinessPreferenceState("Preservado", true))
        backup.writeBytes(byteArrayOf(0, 1, 2))
        check(manager.restore(Uri.fromFile(backup), database, full).isFailure)
        check(preferences.businessName == "Preservado")
        check(before == financialSnapshot(database.readableDatabase))
    }
    fun testInvalidMetadataAndSchemaAreRejected() {
        seed(); create()
        val before = financialSnapshot(database.readableDatabase)
        preferences.restoreState(BusinessPreferenceState("Preservado", true))
        SQLiteDatabase.openDatabase(backup.path, null, SQLiteDatabase.OPEN_READWRITE).use {
            it.execSQL("UPDATE ${BackupManager.METADATA_TABLE} SET format_version=99")
        }
        check(manager.restore(Uri.fromFile(backup), database, full).isFailure)
        check(before == financialSnapshot(database.readableDatabase))
        check(preferences.businessName == "Preservado")
    }
    fun testFreeAndUnavailableCannotBackupOrRestore() {
        seed(); create()
        val before = financialSnapshot(database.readableDatabase)
        val blocked = File(context.filesDir, "must_not_exist.db").apply { delete() }
        EntitlementState.values().filter { it != full }.forEach {
            check(manager.createBackup(Uri.fromFile(blocked), database, it).isFailure)
            check(manager.restore(Uri.fromFile(backup), database, it).isFailure)
        }
        check(!blocked.exists())
        check(before == financialSnapshot(database.readableDatabase))
        preferences.businessName = "Personalização FREE"
        check(preferences.businessName == "Personalização FREE")
    }
    fun testZPrepareRestartPersistence() {
        seed(); create()
        preferences.businessName = "Antes"
        restore()
        check(preferences.businessName == name)
    }
    fun testNewBackupAfterOldRestoreExportsCurrentName() {
        create(); oldFormat()
        preferences.restoreState(BusinessPreferenceState("Nome preservado", true))
        restore()
        manager.createBackup(Uri.fromFile(backup), database, full).getOrThrow()
        SQLiteDatabase.openDatabase(backup.path, null, SQLiteDatabase.OPEN_READONLY).use {
            it.rawQuery("SELECT format_version, business_name FROM ${BackupManager.METADATA_TABLE}", null).use { c ->
                check(c.moveToFirst() && c.getInt(0) == 2 && c.getString(1) == "Nome preservado")
            }
        }
    }
    fun testRestartPersistence() {
        check(context.packageName.endsWith(".test"))
        database = LocalDatabase(context)
        check(preferences.businessName == name && preferences.initialSetupOffered)
        check(database.getRelatorio().size == 3)
        check(database.getRelatorio().sumOf { it.total_pago } == 1_500L)
    }
}
