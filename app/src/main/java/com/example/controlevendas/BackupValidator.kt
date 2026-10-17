package com.example.controlevendas

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Locale

/** Lê somente cópia privada, aberta read-only. Nunca executa DDL vindo do arquivo. */
internal class BackupValidator(private val context: Context) {
    private data class ObjectDefinition(val type: String, val table: String, val sql: String?)

    private fun objects(db: SQLiteDatabase, allowedNames: Set<String>? = null): Map<String, ObjectDefinition> {
        val result = mutableMapOf<String, ObjectDefinition>()
        db.rawQuery("SELECT type, name, tbl_name, sql FROM sqlite_master", null).use { c ->
            while (c.moveToNext()) {
                val name = c.getString(1)
                // Interromper na primeira entrada desconhecida, sem acumular schema arbitrário em memória.
                if (allowedNames != null && name !in allowedNames) reject()
                require(result.put(name, ObjectDefinition(c.getString(0), c.getString(2), c.getString(3))) == null)
            }
        }
        return result
    }

    private fun normalize(sql: String) = sql.replace(Regex("[\\s`\"\\[\\]]"), "").uppercase(Locale.ROOT)

    private fun signature(definition: ObjectDefinition): String? {
        val sql = definition.sql ?: return null
        if (sql.length > 8192) reject() // DDL próprio é pequeno; limitar o parser, não os dados comerciais.
        if (definition.type != "table") return normalize(sql)
        // ALTER TABLE v2 -> v3 adicionou centavos ao final. A ordem das colunas não é requisito.
        val match = Regex("(?is)^\\s*CREATE\\s+TABLE\\s+([`\"\\[\\]\\w]+)\\s*\\((.*)\\)\\s*;?\\s*$").matchEntire(sql)
            ?: throw BackupRejected("A estrutura do backup é incompatível.")
        return normalize(match.groupValues[1]) + "(" + match.groupValues[2].split(',').map(::normalize).sorted().joinToString(",") + ")"
    }

    fun validate(db: SQLiteDatabase): String? {
        db.execSQL("PRAGMA query_only=ON")
        // Desconhecido/no-op nas versões SQLite antigas; a whitelist abaixo é obrigatória em todas.
        db.execSQL("PRAGMA trusted_schema=OFF")
        val metadata = BackupManager.METADATA_TABLE
        val expected = SQLiteDatabase.create(null).use { reference ->
            LocalDatabase(context).onCreate(reference)
            objects(reference)
        }.toMutableMap()
        expected.remove("android_metadata")
        val requiredNames = expected.keys + metadata
        val actual = objects(db, requiredNames + "android_metadata")
        if (actual.keys - "android_metadata" != requiredNames) reject()
        for ((name, definition) in actual) {
            if (name == metadata) {
                if (definition.type != "table" || definition.table != metadata) reject()
                val permitted = listOf(
                    "CREATE TABLE $metadata (format_version INTEGER NOT NULL, schema_version INTEGER NOT NULL)",
                    "CREATE TABLE $metadata (format_version INTEGER NOT NULL, schema_version INTEGER NOT NULL, business_name TEXT)"
                ).map { signature(ObjectDefinition("table", metadata, it)) }
                if (signature(definition) !in permitted) reject()
            } else if (name == "android_metadata") {
                if (definition.type != "table" || definition.table != name || signature(definition) !=
                    signature(ObjectDefinition("table", name, "CREATE TABLE android_metadata (locale TEXT)"))) reject()
            } else {
                val trusted = expected[name] ?: reject()
                if (definition.type != trusted.type || definition.table != trusted.table || signature(definition) != signature(trusted)) reject()
            }
        }
        // Somente após rejeitar views/triggers/CHECK/defaults/índices desconhecidos, consultar registros/integridade.
        if (db.version != BackupManager.SCHEMA_VERSION) reject()
        db.rawQuery("PRAGMA integrity_check", null).use { c ->
            if (c.count != 1 || !c.moveToFirst() || c.getString(0) != "ok") reject()
        }
        var version: Int
        db.rawQuery("SELECT format_version, schema_version FROM $metadata", null).use { c ->
            if (c.count != 1 || !c.moveToFirst() || c.getType(0) != Cursor.FIELD_TYPE_INTEGER || c.getType(1) != Cursor.FIELD_TYPE_INTEGER) reject()
            val value = c.getLong(0)
            if (value !in 1L..BackupManager.FORMAT_VERSION.toLong() || c.getLong(1) != BackupManager.SCHEMA_VERSION.toLong()) reject()
            version = value.toInt()
        }
        validateRows(db)
        if (version == 1) return null
        db.rawQuery("SELECT business_name FROM $metadata", null).use { c ->
            if (!c.moveToFirst()) reject()
            if (c.isNull(0)) return null
            if (c.getType(0) != Cursor.FIELD_TYPE_STRING) reject()
            val name = c.getString(0)
            if (name.length > BusinessIdentity.MAX_NAME_LENGTH) reject()
            return BusinessIdentity.normalize(name).takeIf { it.isNotEmpty() }
        }
    }

    private fun reject(): Nothing = throw BackupRejected("O backup contém estrutura ou registros inválidos ou incompatíveis.")

    private fun validateRows(db: SQLiteDatabase) {
        fun invalid(table: String, condition: String) {
            db.rawQuery("SELECT 1 FROM $table WHERE $condition LIMIT 1", null).use { if (it.moveToFirst()) reject() }
        }
        fun text(column: String) = "(typeof($column) != 'text' OR length(trim($column)) = 0)"
        fun optionalText(column: String) = "typeof($column) NOT IN ('text','null')"
        invalid("CLIENTES", "${text("id_cliente")} OR ${text("nome")}")
        invalid("VENDAS", "${text("id_venda")} OR ${text("id_cliente")} OR ${optionalText("descricao")} OR " +
            "typeof(valor_total_centavos) != 'integer' OR valor_total_centavos < 0 OR " +
            "typeof(parcela_atual) != 'integer' OR parcela_atual < 1 OR parcela_atual > 2147483647 OR typeof(parcelas) != 'integer' OR parcelas < 1 OR parcelas > 2147483647 OR " +
            "(id_venda_pai IS NOT NULL AND ${text("id_venda_pai")}) OR typeof(valor_total) NOT IN ('real','integer','null') OR " +
            "NOT EXISTS (SELECT 1 FROM CLIENTES c WHERE c.id_cliente=VENDAS.id_cliente)")
        invalid("PAGAMENTOS", "${text("id_pagamento")} OR ${text("id_venda")} OR " +
            "typeof(valor_pago_centavos) != 'integer' OR valor_pago_centavos < 0 OR typeof(valor_pago) NOT IN ('real','integer','null') OR " +
            "NOT EXISTS (SELECT 1 FROM VENDAS v WHERE v.id_venda=PAGAMENTOS.id_venda)")
        invalid("FOTOS_VENDA", "${text("id_foto")} OR ${text("id_venda_pai")} OR typeof(imagem) != 'blob' OR length(imagem)=0 OR " +
            "NOT EXISTS (SELECT 1 FROM VENDAS v WHERE v.id_venda_pai=FOTOS_VENDA.id_venda_pai)")
        for (table in listOf("SYNC_DIRTY", "SYNC_DELETIONS")) invalid(table, "${text("entity_type")} OR ${text("entity_id")}")
        // Nomes/descrições/clientes e metadados de exclusão não têm IDs numéricos; vendas/pagamentos sim.
        for ((table, id) in listOf("VENDAS" to "id_venda", "PAGAMENTOS" to "id_pagamento")) {
            db.rawQuery("SELECT $id FROM $table", null).use { c ->
                while (c.moveToNext()) {
                    val number = c.getString(0).toLongOrNull() ?: reject()
                    if (number <= 0 || number > 9_007_199_254_740_991L || number.toString() != c.getString(0)) reject()
                }
            }
        }
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
            isLenient = false
            timeZone = java.util.TimeZone.getTimeZone("UTC") // Data civil não depende de lacunas de horário de verão.
        }
        for ((table, columns) in listOf("VENDAS" to listOf("data_venda", "data_vencimento"), "PAGAMENTOS" to listOf("data_pagamento"))) {
            db.rawQuery("SELECT ${columns.joinToString(",")} FROM $table", null).use { c ->
                while (c.moveToNext()) for (column in columns.indices) {
                    if (c.isNull(column)) continue // Ausência histórica preservada; data não vazia deve ser ISO real.
                    if (c.getType(column) != Cursor.FIELD_TYPE_STRING) reject()
                    val value = c.getString(column)
                    if (value.isEmpty()) continue
                    val position = ParsePosition(0)
                    if (!Regex("\\d{4}-\\d{2}-\\d{2}").matches(value) || dateFormat.parse(value, position) == null || position.index != value.length) reject()
                }
            }
        }
        // Parcelas podem ter sido apagadas/editadas individualmente. id_venda_pai é agrupador, não FK para uma linha-pai.
        db.rawQuery("SELECT 1 FROM VENDAS WHERE id_venda_pai IS NOT NULL GROUP BY id_venda_pai, parcela_atual HAVING COUNT(*) > 1 LIMIT 1", null)
            .use { if (it.moveToFirst()) reject() }
        // SUM inteiro lança erro em overflow. Não limitar saldo a zero: pagamentos excedentes históricos são preservados.
        for ((table, value) in listOf("VENDAS" to "valor_total_centavos", "PAGAMENTOS" to "valor_pago_centavos")) {
            db.rawQuery("SELECT SUM($value) FROM $table", null).use { c ->
                if (!c.moveToFirst() || c.getType(0) !in listOf(Cursor.FIELD_TYPE_NULL, Cursor.FIELD_TYPE_INTEGER)) reject()
            }
        }
    }
}
