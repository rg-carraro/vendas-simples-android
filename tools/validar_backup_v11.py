"""Testes instrumentados exclusivamente no AVD Build10/debug, com namespace sintético.

Uso: python tools/validar_backup_v11.py --adb CAMINHO_ADB --serial emulator-5556
Instala APK debug e APK de testes; não compra licença nem altera dados operacionais.
"""
import argparse
from pathlib import Path
import subprocess


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--adb", default="adb")
    parser.add_argument("--serial", required=True)
    args = parser.parse_args()
    if not args.serial.startswith("emulator-"):
        raise RuntimeError("Recusa aparelho real")
    prefix = [args.adb, "-s", args.serial]

    def adb(*commands, timeout=60):
        try:
            return subprocess.check_output(prefix + list(commands), stderr=subprocess.STDOUT, timeout=timeout).decode("utf-8", errors="replace")
        except subprocess.CalledProcessError as error:
            raise RuntimeError(error.output.decode("utf-8", errors="replace")) from error

    if adb("shell", "getprop", "ro.boot.qemu.avd_name").strip() != "Build10":
        raise RuntimeError("Recusa AVD não isolado")
    for apk in ["app/build/outputs/apk/debug/app-debug.apk", "app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk"]:
        if "Success" not in adb("install", "-r", "-t", apk):
            raise RuntimeError("Falha ao instalar " + apk)
    package = "com.vendassimples.app"
    adb("shell", "run-as", package, "id")
    out = Path(".gradle-user/v11-backup-tests")
    out.mkdir(parents=True, exist_ok=True)

    def operational_hash():
        return adb("shell", "run-as", package, "sh", "-c",
                   "'for f in databases/vendas_simples.db shared_prefs/business_identity.xml; do if [ -f $f ]; then sha256sum $f; fi; done'")

    before = operational_hash()
    component = package + ".test/com.example.controlevendas.BackupInstrumentationRunner"
    for phase in ["main", "restart"]:
        if phase == "restart":
            # Outra execução em novo processo: SharedPreferences e SQLite devem sobreviver.
            adb("shell", "am", "force-stop", package)
        arguments = ["shell", "am", "instrument", "-w", "-r"]
        if phase == "restart":
            arguments += ["-e", "phase", "restart"]
        result = adb(*arguments, component, timeout=240)
        (out / (phase + ".txt")).write_text(result, encoding="utf-8")
        expected = "OK (11 tests)" if phase == "main" else "OK (1 tests)"
        if expected not in result or "FAILURES!!!" in result or "INSTRUMENTATION_FAILED" in result:
            raise RuntimeError(result)
        print(phase + ": " + expected)
    after = operational_hash()
    if before != after:
        raise RuntimeError("Dados operacionais do AVD foram modificados")
    print("Hashes do banco e preferência operacionais preservados")


if __name__ == "__main__":
    main()
