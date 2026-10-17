# Exportação CSV brasileira — V11

O exportador único `SalesCsvExporter` recebe a lista já filtrada de parcelas do relatório. Não consulta nem altera o banco. Valores são `Long` em centavos na origem; `BigDecimal.valueOf(cents, 2)` apresenta duas casas decimais com vírgula, sem R$ ou ponto flutuante. CSV continua disponível no FREE.

## Formato

- UTF-8 explícito, CRLF entre registros e delimitador `;`.
- Colunas: Cliente; Descrição; Data Compra; Vencimento; Valor; Pago; Faltante; Parcela.
- Datas ISO completas apresentadas em `dd/MM/yyyy`, preservando textos legados não reconhecidos.
- Aspas delimitam campos com `;`, vírgulas, aspas ou CR/LF; aspas internas duplicadas. Quebras originais permanecem dentro da célula. Um registro lógico por parcela, mesmo com várias linhas físicas.
- CSV padrão: sem BOM, começando diretamente pelos bytes ASCII de Cliente.
- CSV para Excel: os mesmos bytes, precedidos por `EF BB BF` (BOM UTF-8).

A escolha aparece ao exportar e não muda filtros, cálculos, quantidade de colunas ou população. Não há conversão para Windows-1252/ANSI, que perderia caracteres como emoji ou ideogramas. Não há substituição de textos suspeitos do usuário.

## Causa dos caracteres incorretos

`ï»¿Cliente` é a leitura dos bytes `EF BB BF` como caracteres Latin-1/Windows-1252; `DescriÃ§Ã£o` é a leitura dos bytes UTF-8 de Descrição da mesma forma. O teste automatizado reproduz os exemplos usando o decodificador incorreto e recupera o cabeçalho correto usando UTF-8. Isso explica o mecanismo; o arquivo original e a versão do leitor do celular não foram inspecionados.

O usuário identificou Google Planilhas Android abrindo o arquivo compartilhado. Remover o BOM no formato padrão impede que esse prefixo esteja no arquivo; **não força o leitor a interpretar os demais bytes como UTF-8**. Não há codificação CSV universal que obrigue todo leitor a reconhecer Unicode. Uma importação incorreta pode afetar qualquer texto acentuado, não apenas o cabeçalho.

## Importação recomendada

No Google Planilhas Android, selecionar CSV padrão e conferir os cabeçalhos e textos acentuados antes de utilizar os dados. A [ajuda oficial do Android](https://support.google.com/docs/answer/6000292?co=GENIE.Platform%3DAndroid&hl=pt-BR) confirma suporte a CSV, mas não documenta garantia de detecção de codificação nessa abertura direta.

Se o problema persistir, importar **o arquivo original**, sem copiar o texto já corrompido, pelo Google Planilhas em um navegador de computador: Arquivo → Importar → Fazer upload, escolher separador personalizado `;`, verificar os textos e configurar localidade Brasil. Após confirmar a leitura correta, abrir a planilha convertida no Android. Esse é um procedimento recomendado para homologação, não uma compatibilidade executada nesta sessão. Ver [procedimento oficial de importação](https://support.google.com/docs/answer/40608?hl=pt-BR).

No Excel, a opção com BOM favorece a abertura direta conforme a [orientação oficial da Microsoft](https://support.microsoft.com/en-us/excel/opening-csv-utf-8-files-correctly-in-excel). Se falhar, no Excel de computador usar Dados → De Texto/CSV, selecionar UTF-8/65001, delimitador `;` e localidade brasileira. CSV não contém estilos nem tipos de célula: datas e números dependem das opções de importação/localidade.

## Evidências e limites

Testes Kotlin conferem bytes, BOM opcional, Unicode, dinheiro, datas e escaping. `tools/validar_csv_v11.py` lê ambos os arquivos realmente gerados pelo Kotlin com o parser padrão do Python e verifica igualdade de registros/colunas/valores. Isso não comprova abertura em Google Planilhas Android ou Excel reais. Resultados atuais, pendências e checklist em [RELEASE_V11.md](RELEASE_V11.md).
