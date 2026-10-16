package com.example.controlevendas

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/** Mensagens controladas, sem SQL, caminhos ou conteúdo do usuário. */
class BackupRejected(message: String) : IOException(message)

internal class BackupTransfer(
    val maximumBytes: Long = 512L * 1024 * 1024,
    val reserveBytes: Long = 16L * 1024 * 1024
) {
    init { require(maximumBytes >= 100 && reserveBytes >= 0) }

    fun requireSize(size: Long) {
        if (size < 0 || size > maximumBytes) {
            throw BackupRejected("Backup maior que o limite de ${maximumBytes / (1024 * 1024)} MB.")
        }
    }

    fun requireSpace(available: Long, additional: Long = 0) {
        if (additional < 0 || available < reserveBytes || additional > available - reserveBytes) {
            throw BackupRejected("Espaço insuficiente. Libere espaço no aparelho e tente novamente.")
        }
    }

    /** Limite aplicado ao fluxo real, inclusive quando o provedor omite/mente o tamanho. */
    fun copy(input: InputStream, output: OutputStream, available: () -> Long) {
        val header = ByteArray(16)
        for (i in header.indices) {
            val byte = input.read()
            if (byte < 0) throw BackupRejected("O arquivo não é um backup SQLite válido.")
            header[i] = byte.toByte()
        }
        if (!header.contentEquals("SQLite format 3\u0000".toByteArray(Charsets.US_ASCII))) {
            throw BackupRejected("O arquivo não é um backup SQLite válido.")
        }
        requireSpace(available(), header.size.toLong())
        output.write(header)
        var total = header.size.toLong()
        val buffer = ByteArray(64 * 1024)
        while (true) {
            // read() assegura progresso mesmo com um InputStream que retorna zero no read(buffer).
            val first = input.read()
            if (first < 0) break
            buffer[0] = first.toByte()
            val count = input.read(buffer, 1, buffer.size - 1).coerceAtLeast(0) + 1
            if (count.toLong() > maximumBytes - total) requireSize(maximumBytes + 1)
            requireSpace(available(), count.toLong())
            output.write(buffer, 0, count)
            total += count
        }
        if (total < 100) throw BackupRejected("O backup está incompleto.")
    }
}
