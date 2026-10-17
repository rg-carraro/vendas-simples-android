package com.example.controlevendas

import android.app.Activity
import android.app.Instrumentation
import android.os.Bundle

/** Runner Android nativo, sem dependências de produção ou bibliotecas adicionais. */
class BackupInstrumentationRunner : Instrumentation() {
    private var restartPhase = false
    private var finalUiPhase = false
    override fun onCreate(arguments: Bundle?) {
        super.onCreate(arguments)
        restartPhase = arguments?.getString("phase") == "restart"
        finalUiPhase = arguments?.getString("phase") == "final-ui"
        start()
    }
    override fun onStart() {
        if (finalUiPhase) {
            val tests = FinalUiInstrumentedTests(this)
            var failures = 0
            val methods = tests.javaClass.declaredMethods.filter { it.name.startsWith("test") && it.parameterCount == 0 }.sortedBy { it.name }
            methods.forEach { method ->
                try {
                    tests.prepare()
                    method.invoke(tests)
                    sendStatus(0, Bundle().apply { putString("stream", "${method.name}: PASSOU\n") })
                } catch (failure: Throwable) {
                    failures++
                    sendStatus(-2, Bundle().apply { putString("stream", "${method.name}: ${(failure.cause ?: failure).stackTraceToString()}\n") })
                } finally { tests.close() }
            }
            finish(Activity.RESULT_OK, Bundle().apply {
                putString("stream", if (failures == 0) "\nOK (${methods.size} tests)\n" else "\nFAILURES: $failures\n")
            })
            return
        }
        val tests = BackupInstrumentedTests(BackupTestContext(targetContext))
        val methods = tests.javaClass.declaredMethods.filter {
            it.name.startsWith("test") && it.parameterCount == 0 &&
                (if (restartPhase) it.name == "testRestartPersistence" else it.name != "testRestartPersistence")
        }.sortedBy { it.name }
        var failures = 0
        methods.forEachIndexed { index, method ->
            val status = Bundle().apply {
                putString("class", tests.javaClass.name)
                putString("test", method.name)
                putInt("numtests", methods.size)
                putInt("current", index + 1)
            }
            sendStatus(1, status)
            try {
                if (!restartPhase) tests.prepare()
                method.invoke(tests)
                status.putString("stream", ".")
                sendStatus(0, status)
            } catch (failure: Throwable) {
                failures++
                status.putString("stack", (failure.cause ?: failure).stackTraceToString())
                status.putString("stream", "\n${method.name}: ${failure.cause ?: failure}\n")
                sendStatus(-2, status)
            } finally { tests.close() }
        }
        finish(Activity.RESULT_OK, Bundle().apply {
            putString("stream", if (failures == 0) "\nOK (${methods.size} tests)\n" else "\nFAILURES!!! Tests: ${methods.size}, Failures: $failures\n")
        })
    }
}
