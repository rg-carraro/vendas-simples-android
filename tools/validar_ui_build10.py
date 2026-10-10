"""Smoke tests de UI por ADB, exclusivamente no AVD isolado Build10.

Uso: python tools/validar_ui_build10.py --serial emulator-5556 --seed
Requer APK debug instalado. --seed popula uma base vazia com dados sintéticos,
sem criar/alterar schema. Evidências ficam em .gradle-user/ui-refinement/.
Não executa compra, envio, publicação ou operações em aparelho real.
"""
import argparse
import json
import os
from pathlib import Path
import re
import sqlite3
import struct
import subprocess
import time
import xml.etree.ElementTree as ET
import zlib

PACKAGE = "com.vendassimples.app"
ACTIVITY = PACKAGE + "/com.example.controlevendas.MainActivity"
OUT = Path(".gradle-user/ui-refinement")


class Device:
    def __init__(self, serial):
        self.prefix = ["adb", "-s", serial]
        self.results = []
        self.nodes = []
        if not serial.startswith("emulator-"):
            raise RuntimeError("Somente emulador de testes")
        if self.shell("getprop", "ro.boot.qemu.avd_name").strip() != "Build10":
            raise RuntimeError("AVD não isolado")
        self.shell("run-as", PACKAGE, "id")  # Refuse a non-debug installation.

    def adb(self, *args):
        return subprocess.check_output(self.prefix + list(args), timeout=45)

    def shell(self, *args):
        return self.adb("shell", *args).decode("utf-8", errors="replace")

    def snapshot(self, name):
        time.sleep(1)
        remote = f"/sdcard/build10-ui-{time.monotonic_ns()}.xml"
        for attempt in range(3):
            self.shell("wm", "dismiss-keyguard")
            output = self.shell("uiautomator", "dump", remote)
            if "dumped to" in output:
                break
            time.sleep(2)
        else:
            raise RuntimeError("Não foi possível obter uma árvore de UI atual: " + output)
        xml = self.adb("exec-out", "cat", remote)
        (OUT / (name + ".xml")).write_bytes(xml)
        self.nodes = list(ET.fromstring(xml).iter("node"))
        (OUT / (name + ".png")).write_bytes(self.adb("exec-out", "screencap", "-p"))
        return self.nodes

    def find(self, text, attribute="text"):
        matches = [n for n in self.nodes if n.get(attribute, "").casefold() == text.casefold()]
        assert matches, f"Controle ausente: {text}"
        return matches[0]

    def click(self, text, attribute="text"):
        node = self.find(text, attribute)
        x1, y1, x2, y2 = map(int, re.findall(r"\d+", node.get("bounds")))
        assert x2 > x1 and y2 > y1, f"Controle sem área visível: {text}"
        self.shell("input", "tap", str((x1 + x2) // 2), str((y1 + y2) // 2))

    def check(self, label, condition):
        self.results.append({"cenario": label, "passou": bool(condition)})
        assert condition, label

    def back(self):
        self.shell("input", "keyevent", "KEYCODE_BACK")

    def start(self):
        self.shell("am", "force-stop", PACKAGE)
        self.shell("am", "start", "-n", ACTIVITY)
        time.sleep(3)
        self.shell("wm", "dismiss-keyguard")


def png(rgb):
    def chunk(kind, data):
        return struct.pack("!I", len(data)) + kind + data + struct.pack("!I", zlib.crc32(kind + data))
    data = b"".join(b"\0" + bytes(rgb) * 80 for _ in range(80))
    return b"\x89PNG\r\n\x1a\n" + chunk(b"IHDR", struct.pack("!2I5B", 80, 80, 8, 2, 0, 0, 0)) + chunk(b"IDAT", zlib.compress(data)) + chunk(b"IEND", b"")


def seed(device):
    device.shell("am", "force-stop", PACKAGE)
    raw = device.adb("exec-out", "run-as", PACKAGE, "cat", "databases/vendas_simples.db")
    fixture = OUT / "synthetic.db"
    fixture.write_bytes(raw)
    with sqlite3.connect(fixture) as db:
        if db.execute("SELECT COUNT(*) FROM VENDAS").fetchone()[0] != 0:
            raise RuntimeError("Fixture requer base vazia")
        original_schema = db.execute("SELECT sql FROM sqlite_master ORDER BY name").fetchall()
        for i in range(26):
            name = ["UI Fotos", "UI Parcela", "UI Quitado", "UI Sem Foto"][i] if i < 4 else f"UI Lista {i:02}"
            db.execute("INSERT INTO CLIENTES(id_cliente,nome) VALUES (?,?)", (str(8000 + i), name))
            db.execute("INSERT INTO VENDAS(id_venda,id_cliente,descricao,data_venda,data_vencimento,valor_total,valor_total_centavos,parcela_atual,parcelas,id_venda_pai) VALUES (?,?,?,?,?,?,?,?,?,?)", (str(9000 + i), str(8000 + i), "Descrição sintética longa para verificar quebra de texto. " * 4, "2026-10-07", f"2026-11-{28-i:02}", 123.45, 12345, 2 if i == 1 else 1, 3 if i == 1 else 1, str(9000 + i)))
        for ident, numero, vencimento in [(9501, 1, "2026-10-27"), (9502, 3, "2026-12-27")]:
            db.execute("INSERT INTO VENDAS(id_venda,id_cliente,descricao,data_venda,data_vencimento,valor_total,valor_total_centavos,parcela_atual,parcelas,id_venda_pai) VALUES (?,?,?,?,?,?,?,?,?,?)", (str(ident), "8001", "Parcela sintética", "2026-10-07", vencimento, 123.45, 12345, numero, 3, "9001"))
        for i, cents in [(0, 3000), (2, 12345)]:
            db.execute("INSERT INTO PAGAMENTOS VALUES (?,?,?,?,?)", (str(10000 + i), str(9000 + i), "2026-10-07", cents / 100, cents))
        for i, rgb in enumerate([(37, 99, 235), (71, 85, 105), (226, 244, 235)]):
            db.execute("INSERT INTO FOTOS_VENDA VALUES (?,?,?)", (str(11000 + i), "9000", png(rgb)))
        assert db.execute("SELECT sql FROM sqlite_master ORDER BY name").fetchall() == original_schema
    device.adb("push", str(fixture), "/data/local/tmp/build10-synthetic.db")
    device.shell("run-as", PACKAGE, "cp", "/data/local/tmp/build10-synthetic.db", "databases/vendas_simples.db")
    device.start()


def smoke(device):
    device.start()
    device.snapshot("menu")
    device.find("Menu Principal")
    device.check("Seta oculta no menu", not any(n.get("content-desc") == "Voltar à tela anterior" for n in device.nodes))
    device.click("Vendas")
    device.snapshot("lista")
    device.check("Seta presente na lista", device.find("Voltar à tela anterior", "content-desc") is not None)
    device.click("Voltar à tela anterior", "content-desc")
    device.snapshot("retorno-seta")
    device.check("Seta retorna ao menu", device.find("Menu Principal") is not None)
    device.click("Vendas")
    device.snapshot("lista-android")
    device.back()
    device.snapshot("retorno-android")
    device.check("Android retorna ao menu", device.find("Menu Principal") is not None)
    device.click("Financeiro")
    device.snapshot("financeiro")
    device.click("Voltar à tela anterior", "content-desc")
    device.snapshot("financeiro-retorno")
    device.check("Financeiro retorna ao menu", device.find("Menu Principal") is not None)
    device.click("Mais opções", "content-desc")
    device.snapshot("menu-opcoes")
    device.check("Menu de três pontos preservado", device.find("Exportar CSV") is not None)
    device.back()
    device.snapshot("menu-apos-opcoes")
    device.click("Vendas")
    device.snapshot("lista-filtro")
    device.click("A receber")
    device.snapshot("filtro-ativo")
    device.check("Filtro A Receber ativo", device.find("Todos").get("selected") == "true")
    device.click("Limpar Filtros")
    device.snapshot("filtro-limpo")
    device.check("Limpar filtro", device.find("A receber").get("selected") == "false")


def details(device):
    for attempt in range(4):
        if any(n.get("text") == "UI Fotos" for n in device.nodes):
            break
        scroll = next(n for n in device.nodes if n.get("class") == "android.widget.ScrollView")
        x1, y1, x2, y2 = map(int, re.findall(r"\d+", scroll.get("bounds")))
        device.shell("input", "swipe", str((x1+x2)//2), str(y2-8), str((x1+x2)//2), str(y1+8), "400")
        device.snapshot("lista-fotos")
    device.click("UI Fotos")
    device.snapshot("opcoes-card")
    device.click("Ver detalhes")
    device.snapshot("detalhes-topo")
    actions = ["Registrar Pagamento", "PDF do Card", "Fechar"]
    positions = {t: device.find(t).get("bounds") for t in actions + ["Detalhes da Venda"]}
    device.check("Três ações disponíveis sem rolar", len(positions) == 4)
    scroll = next(n for n in device.nodes if n.get("class") == "android.widget.ScrollView")
    x1, y1, x2, y2 = map(int, re.findall(r"\d+", scroll.get("bounds")))
    device.shell("input", "swipe", str((x1+x2)//2), str(y2-8), str((x1+x2)//2), str(y1+8), "400")
    device.snapshot("detalhes-rolado")
    for attempt in range(16):
        if any(n.get("content-desc") == "Foto 1 de 3; toque para ampliar" for n in device.nodes):
            break
        device.shell("input", "swipe", str((x1+x2)//2), str(y2-8), str((x1+x2)//2), str(y1+8), "400")
        device.snapshot("detalhes-rolado")
    device.check("Título e rodapé fixos após rolar", all(device.find(t).get("bounds") == b for t, b in positions.items()))
    device.check("Fotos preservadas", device.find("Foto 1 de 3; toque para ampliar", "content-desc") is not None)
    device.click("Foto 1 de 3; toque para ampliar", "content-desc")
    device.snapshot("foto-ampliada")
    device.check("Ampliação de foto", any(n.get("class") == "android.widget.ImageView" for n in device.nodes))
    device.click("Fechar")
    device.snapshot("detalhes-apos-foto")
    device.click("Registrar Pagamento")
    device.snapshot("dialogo-pagamento")
    device.check("Ação abre pagamento", device.find("Valor Pago") is not None)
    device.click("Cancelar")
    device.snapshot("lista-apos-cancelar-pagamento")


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--serial", required=True)
    parser.add_argument("--seed", action="store_true")
    parser.add_argument("--details", action="store_true", help="Verifica detalhes da fixture sintética UI Fotos")
    parser.add_argument("--details-only", action="store_true", help="Executa apenas a verificação dos detalhes sintéticos")
    args = parser.parse_args()
    os.environ.setdefault("ANDROID_SDK_HOME", str(Path.home()))
    OUT.mkdir(parents=True, exist_ok=True)
    device = Device(args.serial)
    try:
        if args.seed:
            seed(device)
        if args.details_only:
            device.start()
            device.snapshot("menu-detalhes")
            device.click("Vendas")
            device.snapshot("lista-detalhes")
            details(device)
        else:
            smoke(device)
            if args.details:
                details(device)
    except Exception as error:
        device.results.append({"cenario": "Execução completa", "passou": False, "erro": str(error)})
        raise
    finally:
        (OUT / "smoke-results.json").write_text(json.dumps(device.results, ensure_ascii=False, indent=2), encoding="utf-8")
    print(json.dumps(device.results, ensure_ascii=False, indent=2))
