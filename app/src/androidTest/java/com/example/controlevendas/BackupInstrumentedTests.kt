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
        context.cacheDir.listFiles().orEmpty().filter { it.name.startsWith("vendas_validate_") }.forEach { check(it.delete()) }
        context.getDatabasePath(LocalDatabase.DB_NAME).parentFile!!.listFiles().orEmpty()
            .filter { it.name.startsWith("vendas_incoming_") }.forEach { check(it.delete()) }
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

    private fun rejectedImport(change: (SQLiteDatabase) -> Unit) {
        seed(); create()
        SQLiteDatabase.openDatabase(backup.path, null, SQLiteDatabase.OPEN_READWRITE).use(change)
        assertRejected(manager)
    }
    private fun assertRejected(candidate: BackupManager) {
        val before = financialSnapshot(database.readableDatabase)
        val bytes = context.getDatabasePath(LocalDatabase.DB_NAME).readBytes()
        preferences.restoreState(BusinessPreferenceState("Preservado", true))
        check(candidate.validate(Uri.fromFile(backup)).isFailure)
        check(candidate.restore(Uri.fromFile(backup), database, full).isFailure)
        check(before == financialSnapshot(database.readableDatabase))
        check(bytes.contentEquals(context.getDatabasePath(LocalDatabase.DB_NAME).readBytes()))
        check(preferences.businessName == "Preservado" && preferences.initialSetupOffered)
        check(context.cacheDir.listFiles().orEmpty().none { it.name.startsWith("vendas_validate_") }) {
            "Arquivos de validação temporários restantes: ${context.cacheDir.listFiles().orEmpty().filter { it.name.startsWith("vendas_validate_") }.map { it.name }}"
        }
        check(context.getDatabasePath(LocalDatabase.DB_NAME).parentFile!!.listFiles().orEmpty().none { it.name.startsWith("vendas_incoming_") }) {
            "Arquivos de restauração temporários restantes: ${context.getDatabasePath(LocalDatabase.DB_NAME).parentFile!!.listFiles().orEmpty().filter { it.name.startsWith("vendas_incoming_") }.map { it.name }}"
        }
    }
    fun testRejectsUnexpectedTrigger() = rejectedImport {
        it.execSQL("CREATE TRIGGER injected AFTER INSERT ON PAGAMENTOS BEGIN UPDATE VENDAS SET valor_total_centavos=1; END")
    }
    fun testRejectsUnexpectedView() = rejectedImport { it.execSQL("CREATE VIEW injected AS SELECT * FROM CLIENTES") }
    fun testRejectsViewReplacingExpectedTable() = rejectedImport {
        it.execSQL("DROP TABLE CLIENTES")
        it.execSQL("CREATE VIEW CLIENTES AS SELECT 'synthetic' AS id_cliente, 'Synthetic' AS nome")
    }
    fun testRejectsCorruptSQLiteWithValidHeader() {
        seed(); create()
        java.io.RandomAccessFile(backup, "rw").use { it.seek(100); it.writeByte(0) }
        assertRejected(manager)
    }
    fun testRejectsExcessiveSchemaStatement() = rejectedImport { db ->
        db.execSQL("ALTER TABLE CLIENTES RENAME TO clients_before")
        db.execSQL("CREATE TABLE CLIENTES (id_cliente TEXT PRIMARY KEY, nome TEXT " + " ".repeat(9000) + "NOT NULL)")
        db.execSQL("INSERT INTO CLIENTES SELECT * FROM clients_before")
        db.execSQL("DROP TABLE clients_before")
    }
    fun testRejectsUnknownTable() = rejectedImport { it.execSQL("CREATE TABLE injected (secret TEXT)") }
    fun testRejectsUnknownIndex() = rejectedImport { it.execSQL("CREATE INDEX injected ON CLIENTES(nome)") }
    fun testRejectsAlteredSchema() = rejectedImport { it.execSQL("ALTER TABLE CLIENTES ADD COLUMN injected TEXT DEFAULT 'unsafe'") }
    fun testRejectsOrphanPayment() = rejectedImport { it.execSQL("UPDATE PAGAMENTOS SET id_venda='missing'") }
    fun testRejectsInstallmentWithoutClient() = rejectedImport { it.execSQL("UPDATE VENDAS SET id_cliente='missing'") }
    fun testRejectsPhotoWithoutSaleGroup() = rejectedImport { it.execSQL("UPDATE FOTOS_VENDA SET id_venda_pai='missing'") }
    fun testRejectsIncompatibleType() = rejectedImport { it.execSQL("UPDATE VENDAS SET valor_total_centavos='invalid'") }
    fun testRejectsNegativeStoredMoney() = rejectedImport { it.execSQL("UPDATE PAGAMENTOS SET valor_pago_centavos=-1") }
    fun testRejectsMoneyOverflow() = rejectedImport { it.execSQL("UPDATE VENDAS SET valor_total_centavos=9223372036854775807") }
    fun testRejectsInvalidDate() = rejectedImport { it.execSQL("UPDATE VENDAS SET data_vencimento='2026-02-30'") }
    fun testRejectsInvalidId() = rejectedImport { it.execSQL("UPDATE PAGAMENTOS SET id_pagamento=id_pagamento || '.5'") }
    fun testRejectsInstallmentCountOutsideModelIntegerRange() = rejectedImport { it.execSQL("UPDATE VENDAS SET parcelas=2147483648") }
    fun testRejectsDuplicateInstallmentPosition() = rejectedImport { it.execSQL("UPDATE VENDAS SET parcela_atual=1") }
    fun testRejectsNonSQLiteFile() { seed(); backup.writeText("synthetic non-SQLite"); assertRejected(manager) }
    fun testRejectsUnsupportedSchema() = rejectedImport { it.execSQL("PRAGMA user_version=99") }
    fun testRejectsLargeStreamBeforeReplacement() {
        seed(); create()
        assertRejected(BackupManager(context, BackupTransfer(256, 0), { backup.inputStream() }, { Long.MAX_VALUE }))
    }
    fun testRejectsReadFailureBeforeReplacement() {
        seed(); create()
        assertRejected(BackupManager(context, BackupTransfer(), { object : java.io.InputStream() {
            override fun read(): Int = throw java.io.IOException("synthetic unavailable provider")
        } }, { Long.MAX_VALUE }))
    }
    fun testRejectsInsufficientSpaceBeforeReplacement() {
        seed(); create()
        assertRejected(BackupManager(context, BackupTransfer(), { backup.inputStream() }, { 0L }))
    }
    fun testInsufficientRollbackSpacePreservesOriginal() {
        seed(); create()
        val before = financialSnapshot(database.readableDatabase)
        val bytes = context.getDatabasePath(LocalDatabase.DB_NAME).readBytes()
        preferences.restoreState(BusinessPreferenceState("Preservado", true))
        val stagedSpaceOnly = BackupManager(context, BackupTransfer(reserveBytes = 0), { backup.inputStream() }, { 65_536L })
        check(stagedSpaceOnly.restore(Uri.fromFile(backup), database, full).isFailure)
        check(before == financialSnapshot(database.readableDatabase))
        check(bytes.contentEquals(context.getDatabasePath(LocalDatabase.DB_NAME).readBytes()))
        check(preferences.businessName == "Preservado")
    }
    fun testLegitimatePartialGroupAndOverpaymentRemainCompatible() {
        seed()
        val first = database.getRelatorio().first()
        database.deletarVenda(first.id_venda.toLong().toString())
        val remaining = database.getRelatorio().first()
        database.novoPagamento(NovoPagamentoRequest(remaining.id_venda.toLong().toString(), "2026-10-13", remaining.valor_total + 1))
        // Edição individual permite alterar a quantidade sem reescrever as outras parcelas.
        database.writableDatabase.execSQL("UPDATE VENDAS SET parcelas=1 WHERE id_venda=?", arrayOf(remaining.id_venda.toLong().toString()))
        create()
        val before = financialSnapshot(database.readableDatabase)
        restore()
        check(before == financialSnapshot(database.readableDatabase))
        check(database.getRelatorio().any { it.saldo < 0 })
    }
    fun testHistoricalCivilDateAtDaylightSavingTransition() {
        val previous = java.util.TimeZone.getDefault()
        try {
            java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone("America/Sao_Paulo"))
            seed(); create()
            SQLiteDatabase.openDatabase(backup.path, null, SQLiteDatabase.OPEN_READWRITE).use {
                it.execSQL("UPDATE VENDAS SET data_venda='2018-11-04', data_vencimento='2018-11-04'")
            }
            restore()
            check(database.getRelatorio().all { it.data_venda == "2018-11-04" && it.data_vencimento == "2018-11-04" })
        } finally { java.util.TimeZone.setDefault(previous) }
    }
    fun testLegacyMissingDatesAndMigratedColumnOrder() {
        seed(); create(); oldFormat()
        SQLiteDatabase.openDatabase(backup.path, null, SQLiteDatabase.OPEN_READWRITE).use { db ->
            // Schema v2 legítimo antes do ALTER TABLE da versão 3; os centavos ficam ao final.
            db.execSQL("DROP TABLE VENDAS")
            db.execSQL("CREATE TABLE VENDAS (id_venda TEXT PRIMARY KEY, id_cliente TEXT NOT NULL, descricao TEXT, data_venda TEXT, data_vencimento TEXT, valor_total REAL, parcela_atual INTEGER NOT NULL DEFAULT 1, parcelas INTEGER NOT NULL DEFAULT 1, id_venda_pai TEXT)")
            db.execSQL("ALTER TABLE VENDAS ADD COLUMN valor_total_centavos INTEGER NOT NULL DEFAULT 0")
            db.execSQL("CREATE INDEX idx_vendas_cliente ON VENDAS(id_cliente)")
            db.execSQL("DELETE FROM PAGAMENTOS")
            db.execSQL("DELETE FROM FOTOS_VENDA")
            db.execSQL("INSERT INTO VENDAS(id_venda,id_cliente,valor_total_centavos,data_venda,data_vencimento) SELECT '1',id_cliente,0,NULL,'' FROM CLIENTES LIMIT 1")
        }
        restore()
        check(database.getRelatorio().single().valor_total == 0L)
    }

}
