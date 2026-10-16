package com.example.controlevendas

import org.junit.Assert.*
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class BackupTransferTest {
    private fun input(size: Int = 256) = ByteArray(size).apply {
        "SQLite format 3\u0000".toByteArray(Charsets.US_ASCII).copyInto(this)
    }
    @Test fun copiesBytesExactlyWithBoundedBuffer() {
        val bytes = input(150_000)
        val out = ByteArrayOutputStream()
        BackupTransfer().copy(ByteArrayInputStream(bytes), out) { Long.MAX_VALUE }
        assertArrayEquals(bytes, out.toByteArray())
    }
    @Test fun acceptsExactlyTheLimitAndRejectsOneByteMore() {
        val policy = BackupTransfer(256, 0)
        policy.requireSize(256)
        assertThrows(BackupRejected::class.java) { policy.requireSize(257) }
        policy.copy(ByteArrayInputStream(input()), ByteArrayOutputStream()) { 1000 }
        assertThrows(BackupRejected::class.java) { policy.copy(ByteArrayInputStream(input(257)), ByteArrayOutputStream()) { 1000 } }
    }
    @Test fun rejectsInvalidAndTruncatedHeaders() {
        for (bytes in listOf(byteArrayOf(1, 2), ByteArray(256), input(20))) {
            assertThrows(BackupRejected::class.java) { BackupTransfer().copy(ByteArrayInputStream(bytes), ByteArrayOutputStream()) { Long.MAX_VALUE } }
        }
    }
    @Test fun requiresReserveIncludingRollbackSpaceWithoutOverflow() {
        val policy = BackupTransfer(256, 16)
        policy.requireSpace(116, 100)
        assertThrows(BackupRejected::class.java) { policy.requireSpace(115, 100) }
        assertThrows(BackupRejected::class.java) { policy.requireSpace(Long.MAX_VALUE, Long.MAX_VALUE) }
        assertThrows(BackupRejected::class.java) { policy.copy(ByteArrayInputStream(input()), ByteArrayOutputStream()) { 15 } }
    }
    @Test fun propagatesReadFailureAndDoesNotLoopOnZeroBulkReads() {
        val bad = object : InputStream() { override fun read(): Int = throw IOException("synthetic read failure") }
        assertThrows(IOException::class.java) { BackupTransfer().copy(bad, ByteArrayOutputStream()) { Long.MAX_VALUE } }
        val bytes = input()
        val odd = object : ByteArrayInputStream(bytes) { override fun read(b: ByteArray, off: Int, len: Int) = 0 }
        val out = ByteArrayOutputStream()
        BackupTransfer().copy(odd, out) { Long.MAX_VALUE }
        assertArrayEquals(bytes, out.toByteArray())
    }
}
