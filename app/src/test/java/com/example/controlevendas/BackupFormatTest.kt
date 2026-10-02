package com.example.controlevendas

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupFormatTest {
    @Test fun formatIsVersionedAndDoesNotContainEntitlement() {
        assertEquals(1, BackupManager.FORMAT_VERSION)
        assertEquals(3, BackupManager.SCHEMA_VERSION)
        assertEquals(false, BackupManager.METADATA_TABLE.contains("ENTITLEMENT", true))
    }
    @Test fun backupNameUsesVendasSimplesPrefix() {
        assertEquals(true, "VendasSimples_Backup_20261002_120145.db".startsWith(BackupManager.FILE_PREFIX))
    }
}
