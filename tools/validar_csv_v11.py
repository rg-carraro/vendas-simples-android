"""Valida o CSV produzido pelo teste Kotlin com o parser padrão do Python."""
import csv
from datetime import datetime
from decimal import Decimal
from pathlib import Path
import re

path = Path(__file__).resolve().parents[1] / "app/build/test-artifacts/sales-v11.csv"
assert path.read_bytes().startswith(b"Cliente;")
assert not path.read_bytes().startswith(b"\xef\xbb\xbf")
excel = path.with_name("sales-v11-excel.csv")
assert excel.read_bytes() == b"\xef\xbb\xbf" + path.read_bytes()
with excel.open(encoding="utf-8-sig", newline="") as source:
    excel_rows = list(csv.reader(source, delimiter=";", strict=True))
with path.open(encoding="utf-8", newline="") as source:
    rows = list(csv.reader(source, delimiter=";", strict=True))
assert rows == excel_rows
assert rows[0] == ["Cliente", "Descrição", "Data Compra", "Vencimento", "Valor", "Pago", "Faltante", "Parcela"]
assert len(rows) == 8 and all(len(row) == 8 for row in rows)
assert rows[1][:2] == ["tutu; João — 漢字 😀", 'brinco, "pérola"\r\nsegunda\nlinha\rfim']
expected = [(12345, 2345, 10000), (3333, 1000, 2333), (3333, 0, 3333),
            (3334, 3334, 0), (0, 0, 0), (1, 0, 1), (9223372036854775807, 0, 9223372036854775807)]
for row, cents in zip(rows[1:], expected):
    assert row[2:4] == ["11/10/2026", "05/11/2026"]
    for date in row[2:4]:
        datetime.strptime(date, "%d/%m/%Y")
    for value, expected_cents in zip(row[4:7], cents):
        assert re.fullmatch(r"-?\d+,\d{2}", value)
        assert Decimal(value.replace(",", ".")) * 100 == expected_cents
assert [row[7] for row in rows[1:]] == ["1/1", "1/3", "2/3", "3/3", "1/1", "1/1", "1/1"]
assert sum(Decimal(row[4].replace(",", ".")) for row in rows[2:5]) == Decimal("100.00")
print("CSV aprovado: UTF-8 com/sem BOM, 7 registros, 8 colunas, textos intactos, datas e valores exatos.")
