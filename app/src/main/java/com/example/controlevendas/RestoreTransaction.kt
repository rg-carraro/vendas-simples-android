package com.example.controlevendas

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream

data class BusinessPreferenceState(val name: String, val setupOffered: Boolean)

interface BusinessPreferenceStore {
    fun snapshot(): BusinessPreferenceState
    fun restoreState(state: BusinessPreferenceState)
}

/** Journal privado: enquanto pending existir, reinício reverte banco e identidade juntos. */
class RestoreTransaction(
    private val directory: File,
    private val current: File,
    private val preferences: BusinessPreferenceStore
) {
    private val pending = File(directory, "pending")
    private val previous = File(directory, "previous.db")

    fun recover() {
        if (!pending.exists()) return
        val state = DataInputStream(pending.inputStream()).use {
            require(it.readInt() == 1) { "Journal de restauração incompatível" }
            BusinessPreferenceState(it.readUTF(), it.readBoolean())
        }
        require(previous.isFile) { "Cópia de recuperação ausente" }
        replaceWithCopy(previous)
        preferences.restoreState(state)
        check(pending.delete()) { "Não foi possível concluir a recuperação" }
        previous.delete()
    }

    fun replace(incoming: File, restoredName: String?) {
        check(!pending.exists()) { "Recuperação pendente" }
        check(directory.isDirectory || directory.mkdirs())
        val state = preferences.snapshot()
        copySynced(current, previous)
        val marker = File(directory, "pending.tmp")
        FileOutputStream(marker).use { stream ->
            val data = DataOutputStream(stream)
            data.writeInt(1)
            data.writeUTF(state.name)
            data.writeBoolean(state.setupOffered)
            data.flush()
            stream.fd.sync()
        }
        check(marker.renameTo(pending)) { "Não foi possível preparar a recuperação" }
        try {
            check(incoming.renameTo(current)) { "Não foi possível substituir o banco" }
            val name = restoredName?.let(BusinessIdentity::normalize)?.takeIf { it.isNotEmpty() }
            if (name != null) preferences.restoreState(BusinessPreferenceState(name, true))
            check(pending.delete()) { "Não foi possível concluir a restauração" }
            previous.delete()
        } catch (failure: Exception) {
            try { recover() } catch (recovery: Exception) { failure.addSuppressed(recovery) }
            throw failure
        }
    }

    private fun replaceWithCopy(source: File) {
        val temporary = File.createTempFile("restore_recovery_", ".db", current.parentFile)
        try {
            copySynced(source, temporary)
            File(current.path + "-wal").delete()
            File(current.path + "-shm").delete()
            check(temporary.renameTo(current)) { "Não foi possível recuperar o banco anterior" }
        } finally { temporary.delete() }
    }

    private fun copySynced(source: File, target: File) {
        FileOutputStream(target).use { output ->
            source.inputStream().use { it.copyTo(output) }
            output.fd.sync()
        }
    }
}
