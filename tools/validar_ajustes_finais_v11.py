"""Validação dos ajustes finais no AVD Build10/debug; não salva vendas.

Executa views Android reais e smoke de menu/configuração, restaurando identidade
e configurações do AVD. Requer os APKs debug e androidTest instalados.
"""
import argparse
import hashlib
import json
import re
import subprocess
from pathlib import Path
import validar_ui_build10 as ui
from validar_financeiro_v11 import Device, bounds, texts


def native_tests(device, label):
    output = subprocess.check_output(device.prefix + ["shell", "am", "instrument", "-w", "-e", "phase", "final-ui",
        "com.vendassimples.app.test/com.example.controlevendas.BackupInstrumentationRunner"], timeout=180).decode("utf-8", errors="replace")
    (ui.OUT / (label + ".log")).write_text(output, encoding="utf-8")
    device.check(label, "OK (8 tests)" in output and "FAILURES" not in output)


def edit_name(device, value):
    field = next(n for n in device.nodes if n.get("class") == "android.widget.EditText")
    x1, y1, x2, y2 = bounds(field)
    device.shell("input", "tap", str((x1+x2)//2), str((y1+y2)//2))
    device.shell("input", "keyevent", "KEYCODE_MOVE_END")
    device.shell("input", "keyevent", *(["KEYCODE_DEL"] * 120))
    device.shell("input", "text", value.replace(" ", "%s"))
    device.back()  # Fecha teclado para alcançar as ações do diálogo.
    device.snapshot("configuracao-editada")


def run(device):
    native_tests(device, "views-normal")
    device.start()
    device.snapshot("menu")
    if "System UI isn't responding" in texts(device):
        device.click("Wait")
        device.snapshot("menu-estavel")
    device.check("Marca fixa e slogan", "Vendas Simples" in texts(device) and "Seu controle de vendas" in texts(device))
    device.check("Sem botão redundante de configuração", not any("Configurações" in t for t in texts(device)))
    device.click("Mais opções", "content-desc")
    device.snapshot("menu-opcoes")
    device.check("Personalização no menu e exportação preservada", "Personalizar negócio" in texts(device) and "Exportar CSV" in texts(device))
    device.click("Personalizar negócio")
    device.snapshot("configuracao")
    device.check("Configuração existente com salvar/cancelar", all(v in [t.casefold() for t in texts(device)] for v in ["salvar", "cancelar"]))
    edit_name(device, "Acessorio E Joias")
    device.click("Salvar")
    device.snapshot("nome-salvo")
    device.check("Cabeçalho atualiza ao salvar", "Vendas Simples" in texts(device) and "Acessorio E Joias" in texts(device))
    device.start()
    device.snapshot("nome-apos-reinicio")
    device.check("Nome persiste após reiniciar processo", "Acessorio E Joias" in texts(device) and "Vendas Simples" in texts(device))
    device.click("Mais opções", "content-desc")
    device.snapshot("opcoes-cancelamento")
    device.click("Personalizar negócio")
    device.snapshot("configuracao-cancelamento")
    edit_name(device, "Alteracao Cancelada")
    device.click("Cancelar")
    device.snapshot("nome-preservado")
    device.check("Cancelar preserva nome", "Acessorio E Joias" in texts(device) and "Alteracao Cancelada" not in texts(device))
    device.click("Nova")
    device.snapshot("nova-venda")
    device.check("Salvar/Cancelar do formulário disponíveis", all(v in [t.casefold() for t in texts(device)] for v in ["salvar", "cancelar"]))
    device.click("Cancelar")

    device.shell("wm", "size", "640x1280")
    device.shell("wm", "density", "320")
    device.shell("settings", "put", "system", "font_scale", "2.0")
    native_tests(device, "views-320dp-fonte200")
    device.start()
    device.snapshot("cabecalho-320dp-fonte200")
    device.check("Marca/nome/slogan em 320 dp e fonte 200%", all(v in texts(device) for v in ["Vendas Simples", "Acessorio E Joias", "Seu controle de vendas"]))
    device.click("Mais opções", "content-desc")
    device.snapshot("opcoes-320dp-fonte200")
    device.click("Personalizar negócio")
    device.snapshot("configuracao-320dp-fonte200")
    device.click("Cancelar")
    device.snapshot("retorno-320dp-fonte200")
    device.click("Nova")
    device.snapshot("venda-320dp-fonte200")
    device.check("Rodapé do formulário em tela pequena", all(v in [t.casefold() for t in texts(device)] for v in ["salvar", "cancelar"]))
    device.click("Cancelar")
    device.shell("wm", "size", "1280x640")
    native_tests(device, "views-paisagem-fonte200")
    device.start()
    device.snapshot("cabecalho-paisagem-fonte200")
    device.click("Nova")
    device.snapshot("venda-paisagem-fonte200")
    device.check("Rodapé do formulário em paisagem", all(v in [t.casefold() for t in texts(device)] for v in ["salvar", "cancelar"]))
    device.click("Cancelar")


def run_discard(device):
    device.start()
    device.snapshot("menu")
    device.click("Nova")
    device.snapshot("formulario")
    device.click("Nome Do Cliente")
    device.shell("input", "text", "Rascunho")
    device.back()
    device.snapshot("rascunho")
    device.click("Cancelar")
    device.snapshot("confirmacao-cancelar")
    device.check("Cancelar solicita confirmação de descarte", "Descartar alterações?" in texts(device))
    device.click("Continuar editando")
    device.snapshot("continuar-editando")
    device.check("Continuar editando preserva rascunho", "Rascunho" in texts(device))
    device.back()
    device.snapshot("confirmacao-android")
    device.check("Voltar Android solicita confirmação", "Descartar alterações?" in texts(device))
    device.click("Descartar")
    device.snapshot("retorno-menu")
    device.check("Descartar retorna ao menu", "Menu Principal" in texts(device))


def run_views(device):
    native_tests(device, "views-normal")
    device.shell("wm", "size", "640x1280")
    device.shell("wm", "density", "320")
    device.shell("settings", "put", "system", "font_scale", "2.0")
    native_tests(device, "views-320dp-fonte200")
    device.shell("wm", "size", "1280x640")
    native_tests(device, "views-paisagem-fonte200")


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--serial", required=True)
    parser.add_argument("--discard-only", action="store_true", help="Verifica rascunho/confirmar descarte sem gravar venda")
    parser.add_argument("--views-only", action="store_true", help="Repete apenas os testes nativos nas três configurações")
    args = parser.parse_args()
    ui.OUT = Path(".gradle-user/discard-v11" if args.discard_only else ".gradle-user/final-values-v11" if args.views_only else ".gradle-user/final-ui-v11")
    ui.OUT.mkdir(parents=True, exist_ok=True)
    device = Device(args.serial)
    device.shell("am", "force-stop", ui.PACKAGE)
    baseline = device.adb("exec-out", "run-as", ui.PACKAGE, "cat", "databases/vendas_simples.db")
    identity = device.adb("exec-out", "run-as", ui.PACKAGE, "cat", "shared_prefs/business_identity.xml")
    identity_file = ui.OUT / "original-identity.xml"
    identity_file.write_bytes(identity)
    original_font = device.shell("settings", "get", "system", "font_scale").strip()
    original_size = device.shell("wm", "size")
    original_density = device.shell("wm", "density")
    try:
        device.shell("wm", "size", "reset")
        device.shell("wm", "density", "reset")
        device.shell("settings", "put", "system", "font_scale", "1.0")
        ui.time.sleep(5)
        if args.discard_only:
            run_discard(device)
        elif args.views_only:
            run_views(device)
        else:
            run(device)
    except Exception as error:
        device.results.append({"cenario": "Execução completa", "passou": False, "erro": str(error)})
        raise
    finally:
        device.shell("am", "force-stop", ui.PACKAGE)
        remote = "/data/local/tmp/v11-original-identity.xml"
        device.adb("push", str(identity_file), remote)
        device.shell("run-as", ui.PACKAGE, "cp", remote, "shared_prefs/business_identity.xml")
        device.shell("rm", remote)
        device.shell("settings", "put", "system", "font_scale", original_font if original_font != "null" else "1.0")
        for setting, original_config in [("size", original_size), ("density", original_density)]:
            override = re.search(r"Override.*?:\s*(\S+)", original_config)
            device.shell("wm", setting, override.group(1) if override else "reset")
        after = device.adb("exec-out", "run-as", ui.PACKAGE, "cat", "databases/vendas_simples.db")
        device.check("Banco preservado byte a byte", hashlib.sha256(baseline).digest() == hashlib.sha256(after).digest())
        device.check("Identidade original restaurada", identity == device.adb("exec-out", "run-as", ui.PACKAGE, "cat", "shared_prefs/business_identity.xml"))
        (ui.OUT / "results.json").write_text(json.dumps(device.results, ensure_ascii=False, indent=2), encoding="utf-8")
    print(json.dumps(device.results, ensure_ascii=False, indent=2))
