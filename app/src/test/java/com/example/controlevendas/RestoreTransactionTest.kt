package com.example.controlevendas

import java.io.File
import java.nio.file.Files
import org.junit.Assert.*
import org.junit.Test

class RestoreTransactionTest {
    private class Store : BusinessPreferenceStore {
        var state = BusinessPreferenceState("Anterior", false)
        var failure: Throwable? = null
        override fun snapshot() = state
        override fun restoreState(state: BusinessPreferenceState) {
            this.state = state
            failure?.let { failure = null; throw it }
        }
    }
    private fun scenario(block: (File, File, File, Store) -> Unit) {
        val directory = Files.createTempDirectory("synthetic-restore-").toFile()
        try {
            val current = File(directory, "current.db").apply { writeText("old financial snapshot") }
            val incoming = File(directory, "incoming.db").apply { writeText("new financial snapshot") }
            block(File(directory, "journal"), current, incoming, Store())
        } finally { directory.deleteRecursively() }
    }
    @Test fun successReplacesBothAndClearsJournal() = scenario { journal, current, incoming, store ->
        RestoreTransaction(journal, current, store).replace(incoming, "João & Jóias")
        assertEquals("new financial snapshot", current.readText())
        assertEquals(BusinessPreferenceState("João & Jóias", true), store.state)
        assertFalse(File(journal, "pending").exists())
        assertFalse(File(journal, "previous.db").exists())
    }
    @Test fun preferenceWriteFailureRollsBackDatabaseAndPreference() = scenario { journal, current, incoming, store ->
        store.failure = IllegalStateException("synthetic disk failure")
        try { RestoreTransaction(journal, current, store).replace(incoming, "Novo"); fail() }
        catch (_: IllegalStateException) { }
        assertEquals("old financial snapshot", current.readText())
        assertEquals(BusinessPreferenceState("Anterior", false), store.state)
        assertFalse(File(journal, "pending").exists())
    }
    @Test fun interruptedProcessRecoversBothBeforeOpeningDatabase() = scenario { journal, current, incoming, store ->
        store.failure = AssertionError("simulate process termination")
        try { RestoreTransaction(journal, current, store).replace(incoming, "Novo"); fail() }
        catch (_: AssertionError) { }
        assertTrue(File(journal, "pending").exists())
        assertEquals("new financial snapshot", current.readText())
        RestoreTransaction(journal, current, store).recover()
        assertEquals("old financial snapshot", current.readText())
        assertEquals(BusinessPreferenceState("Anterior", false), store.state)
        RestoreTransaction(journal, current, store).recover()
        assertEquals("old financial snapshot", current.readText())
    }
    @Test fun legacyNullAndEmptyNamesPreservePreferenceState() {
        listOf(null, "", "  ").forEach { name ->
            scenario { journal, current, incoming, store ->
                RestoreTransaction(journal, current, store).replace(incoming, name)
                assertEquals("new financial snapshot", current.readText())
                assertEquals(BusinessPreferenceState("Anterior", false), store.state)
            }
        }
    }
    @Test fun missingIncomingFileRollsBackWithoutChangingName() = scenario { journal, current, incoming, store ->
        incoming.delete()
        try { RestoreTransaction(journal, current, store).replace(incoming, "Novo"); fail() }
        catch (_: IllegalStateException) { }
        assertEquals("old financial snapshot", current.readText())
        assertEquals(BusinessPreferenceState("Anterior", false), store.state)
    }
}
