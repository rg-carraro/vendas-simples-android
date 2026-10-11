"""Smoke de Financeiro/Clientes no AVD Build10, sem gravar dados financeiros.

Requer APK debug e a fixture sintética UI do build 10 já instalada.
Uso: python tools/validar_financeiro_v11.py --serial emulator-5556
"""
import argparse
import hashlib
import json
from pathlib import Path
import re
import sqlite3
import validar_ui_build10 as ui


class Device(ui.Device):
    def snapshot(self, name):
        # /sdcard can be temporarily unavailable while Android applies wm overrides.
        ui.time.sleep(1)
        remote = f"/data/local/tmp/finance-v11-{ui.time.monotonic_ns()}.xml"
        for attempt in range(3):
            self.shell("wm", "dismiss-keyguard")
            self.shell("uiautomator", "dump", remote)
            xml = self.adb("exec-out", "cat", remote)
            try:
                self.nodes = list(ui.ET.fromstring(xml).iter("node"))
                break
            except ui.ET.ParseError:
                ui.time.sleep(2)
        else:
            raise RuntimeError("Captura de UI inválida: " + xml.decode(errors="replace"))
        (ui.OUT / (name + ".xml")).write_bytes(xml)
        (ui.OUT / (name + ".png")).write_bytes(self.adb("exec-out", "screencap", "-p"))
        self.shell("rm", remote)
        return self.nodes


def bounds(node):
    return tuple(map(int, re.findall(r"\d+", node.get("bounds"))))


def scroll_to(device, text):
    for index in range(18):
        found = [n for n in device.nodes if n.get("text", "").casefold() == text.casefold()]
        if found and bounds(found[0])[3] > bounds(found[0])[1]:
            return found[0]
        scroll = next(n for n in device.nodes if n.get("class") == "android.widget.ScrollView")
        x1, y1, x2, y2 = bounds(scroll)
        device.shell("input", "swipe", str((x1+x2)//2), str(y2-12), str((x1+x2)//2), str(y1+12), "250")
        device.snapshot("rolagem")
    raise AssertionError("Não encontrado após rolar: " + text)


def currency(cents):
    integer, fraction = divmod(cents, 100)
    return f"R$ {integer:,}".replace(",", ".") + f",{fraction:02}"


def texts(device):
    return [n.get("text", "").replace("\xa0", " ") for n in device.nodes]


def run(device, db):
    month = "2026-10"
    values = db.execute("""SELECT v.valor_total_centavos, COALESCE(SUM(p.valor_pago_centavos),0)
        FROM VENDAS v LEFT JOIN PAGAMENTOS p ON p.id_venda=v.id_venda
        WHERE v.data_venda LIKE ? GROUP BY v.id_venda""", (month + "%",)).fetchall()
    expected = [sum(v[0] for v in values), sum(v[1] for v in values), sum(v[0]-v[1] for v in values)]
    device.start()
    device.snapshot("menu")
    if "System UI isn't responding" in texts(device):
        device.click("Wait")
        device.snapshot("menu-apos-system-ui")
    if "Como se chama seu negócio?" in texts(device):
        device.back()
        device.snapshot("menu-apos-convite")
    device.click("Financeiro")
    device.snapshot("financeiro-inicial")
    selector = "Selecionar mês do financeiro: " + month
    device.click(selector, "content-desc")
    device.snapshot("meses")
    device.click(month)
    device.snapshot("financeiro-mes")
    device.check("Indicadores iguais às somas independentes SQLite", all(currency(v) in texts(device) for v in expected))
    device.check("Sem card redundante de mês", "Mês Selecionado" not in texts(device))
    sold = device.find("Total vendido")
    received = device.find("Total recebido")
    device.check("Grade de duas colunas em tela comum", bounds(sold)[0] < bounds(received)[0] and abs(bounds(sold)[1]-bounds(received)[1]) < 8)
    device.click("Total vendido")
    device.snapshot("detalhe")
    device.back()
    device.snapshot("detalhe-retorno")
    device.check("Voltar Android preserva mês", device.find(selector, "content-desc") is not None)
    scroll_to(device, "Relatório por período")
    device.click("Relatório por período")
    device.snapshot("periodo-dialogo")
    device.click("Cancelar")
    device.snapshot("periodo-cancelado")
    device.click("Voltar à tela anterior", "content-desc")
    device.snapshot("menu-retorno")
    device.click("Clientes")
    device.snapshot("clientes")
    device.click("Pesquisar cliente", "content-desc")
    device.shell("input", "text", "ui%sfotos")
    device.snapshot("clientes-teclado")
    device.check("Teclado não oculta cabeçalho", device.find("Voltar à tela anterior", "content-desc") is not None)
    device.back()
    device.snapshot("clientes-sem-teclado")
    device.click("Aplicar")
    device.snapshot("clientes-filtrados")
    device.check("Filtro ignora caixa e exclui outros clientes", "UI Fotos" in texts(device) and "UI Parcela" not in texts(device))
    client = db.execute("""SELECT v.valor_total_centavos, COALESCE(SUM(p.valor_pago_centavos),0)
        FROM VENDAS v JOIN CLIENTES c ON c.id_cliente=v.id_cliente
        LEFT JOIN PAGAMENTOS p ON p.id_venda=v.id_venda WHERE c.nome='UI Fotos' GROUP BY v.id_venda""").fetchall()
    client_values = [sum(v[0] for v in client), sum(v[1] for v in client), sum(v[0]-v[1] for v in client)]
    device.check("Totais do cliente preservados", all(currency(v) in texts(device) for v in client_values))
    # The case-insensitive helper would match the search input before the card.
    client_name = next(n for n in device.nodes if n.get("text") == "UI Fotos" and n.get("class") == "android.widget.TextView")
    x1, y1, x2, y2 = bounds(client_name)
    device.shell("input", "tap", str((x1+x2)//2), str((y1+y2)//2))
    device.snapshot("historico")
    device.check("Toque no card abre histórico", "Histórico Do Cliente" in texts(device))
    device.click("Voltar à tela anterior", "content-desc")
    device.snapshot("historico-retorno")
    device.check("Seta preserva pesquisa do cliente", "Limpar Pesquisa" in texts(device) and "UI Fotos" in texts(device))
    device.click("Limpar Pesquisa")
    device.snapshot("clientes-limpos")
    device.check("Limpar pesquisa restaura população", "Limpar Pesquisa" not in texts(device) and any(t.startswith("UI Lista") for t in texts(device)))
    device.back()
    device.snapshot("menu-final")
    device.check("Voltar Android de clientes retorna ao financeiro", "Financeiro e Relatórios" in texts(device))
    device.click("Mais opções", "content-desc")
    device.snapshot("opcoes")
    device.check("Menu de três pontos preservado", "Exportar CSV" in texts(device))
    device.back()

    # 320 dp e fonte 150%; mudanças apenas nas configurações do AVD isolado.
    device.shell("wm", "size", "640x1280")
    device.shell("wm", "density", "320")
    device.shell("settings", "put", "system", "font_scale", "1.5")
    device.start()
    device.snapshot("pequena-menu")
    device.click("Financeiro")
    device.snapshot("pequena-financeiro")
    device.check("Fonte ampliada empilha indicadores", bounds(device.find("Total vendido"))[0] == bounds(device.find("Total recebido"))[0])
    scroll_to(device, "Gerar PDF financeiro")
    device.check("PDF acessível após rolagem em tela pequena", device.find("Gerar PDF financeiro") is not None)
    device.click("Voltar à tela anterior", "content-desc")
    device.snapshot("pequena-retorno")
    device.click("Clientes")
    device.snapshot("pequena-clientes")
    device.click("Pesquisar cliente", "content-desc")
    device.snapshot("pequena-teclado")
    device.check("Pesquisa e retorno acessíveis com teclado/fonte ampliada", device.find("Pesquisar cliente", "content-desc") is not None and device.find("Voltar à tela anterior", "content-desc") is not None)
    device.back()


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--serial", required=True)
    args = parser.parse_args()
    ui.OUT = Path(".gradle-user/finance-v11")
    ui.OUT.mkdir(parents=True, exist_ok=True)
    device = Device(args.serial)
    device.shell("am", "force-stop", ui.PACKAGE)
    original = device.adb("exec-out", "run-as", ui.PACKAGE, "cat", "databases/vendas_simples.db")
    fixture = ui.OUT / "baseline.db"
    fixture.write_bytes(original)
    original_font = device.shell("settings", "get", "system", "font_scale").strip()
    original_size = device.shell("wm", "size")
    original_density = device.shell("wm", "density")
    try:
        with sqlite3.connect(fixture) as db:
            assert db.execute("SELECT COUNT(*) FROM CLIENTES WHERE nome='UI Fotos'").fetchone()[0] == 1, "Requer fixture sintética Build10 existente"
            device.shell("wm", "size", "reset")
            device.shell("wm", "density", "reset")
            device.shell("settings", "put", "system", "font_scale", "1.0")
            ui.time.sleep(5)
            run(device, db)
    except Exception as error:
        device.results.append({"cenario": "Execução completa", "passou": False, "erro": str(error)})
        raise
    finally:
        device.shell("settings", "put", "system", "font_scale", original_font if original_font != "null" else "1.0")
        for setting, original_config in [("size", original_size), ("density", original_density)]:
            override = re.search(r"Override.*?:\s*(\S+)", original_config)
            device.shell("wm", setting, override.group(1) if override else "reset")
        device.shell("am", "force-stop", ui.PACKAGE)
        after = device.adb("exec-out", "run-as", ui.PACKAGE, "cat", "databases/vendas_simples.db")
        device.check("Banco preservado byte a byte", hashlib.sha256(original).digest() == hashlib.sha256(after).digest())
        (ui.OUT / "results.json").write_text(json.dumps(device.results, ensure_ascii=False, indent=2), encoding="utf-8")
    print(json.dumps(device.results, ensure_ascii=False, indent=2))
