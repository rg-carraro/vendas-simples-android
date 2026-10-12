# Release versão 11 — personalização

Data: 11/10/2026. Solicitação: pedido anexado pelo usuário. Base: marco V10 beta da Store, branch `master`, árvore limpa no início. Sem push, merge ou upload no Play Console.

## Melhorias implementadas

- Preferência `businessName` local, normalizada, até 120 caracteres e acentos preservados. Primeiro acesso à funcionalidade oferece configuração uma vez, inclusive em atualização V10; Agora não, Voltar ou fechamento não repetem o convite. Personalizar negócio no menu de três pontos permite editar/remover o nome também no FREE.
- Cabeçalho mantém a marca Vendas Simples, nome opcional em linha secundária e slogan. Nome secundário longo usa reticências/descrição acessível completa; configuração, assinatura e PDF mantêm o nome. Persistência em SharedPreferences independente do SQLite e da licença.
- Cobrança WhatsApp mantém valores, parcelas e saldos existentes; agradecimento usa uma única assinatura com o nome. Resumo em imagem recebe texto personalizado, incluindo o fallback de compartilhamento. PDF/CSV compartilhados recebem identificação no texto do Intent; backup continua no fluxo anterior.
- Os três documentos (card, resumo financeiro/mensal e extrato de cliente) usam o gerador PDF comum com nome no cabeçalho, quebrado por largura medida inclusive sem espaços. Valores e cálculos preservados.
- Datas completas apresentadas em `dd/MM/yyyy` em cards, detalhes, histórico, filtros, formulários, cobrança, PDF e CSV. Conversão de volta a ISO ao aplicar filtros ou salvar venda/pagamento/correção. Sem alteração dos registros, schema, ordenação e consultas. Referências mensais e nomes técnicos de arquivos preservados.
- Miniatura à esquerda e Remover foto contextual à direita, com 16 dp e índice acessível; reorganização vertical quando largura/fonte exigem. Toque para ampliar, galeria, câmera e remoção individual preservados.

## Arquivos modificados

`app/build.gradle`, `MainActivity.kt`, novos `BusinessPreferences.kt` e `BusinessIdentity.kt`, novos testes `BusinessIdentityTest.kt` e `ReleaseCompatibilityTest.kt`; `README.md`, `docs/PRODUTO.md`, `docs/DECISOES.md`, `docs/RELEASE_PLAY_STORE.md`, `docs/VALIDACAO.md`, `docs/ARQUITETURA.md` e este relatório.

## Identidade e regras preservadas

Application ID `com.vendassimples.app`, versionCode **11**, versionName **1.0.11**. minSdk 24, compileSdk/targetSdk 36, Billing 9.1.0, produto `full_access`, opção `full-access`. FREE 30, clientes ilimitados, CSV no FREE, backup/restauração no FULL_ACCESS, compra única com preço do Google Play. Sem novas dependências, permissões, backend ou login.

## Verificações da primeira etapa de personalização

`clean test assembleDebug bundleRelease --offline --no-daemon`: BUILD SUCCESSFUL em 5m09s. Após limitar o título da UI a duas linhas, `test assembleDebug bundleRelease` executado novamente: BUILD SUCCESSFUL em 2m50s. JDK do Android Studio, Gradle 8.7 e SDK local; sem novas dependências. Avisos de depreciação de Locale/ajuste de teclado e parâmetro não utilizado, sem erros.

16 testes em debug e 16 em release, zero falhas/erros: 8 anteriores (dinheiro e formato de backup), 5 de nome/textos/datas e 3 de compatibilidade FREE/FULL_ACCESS/estados Billing. `git diff --check` e links locais dos documentos afetados aprovados. Conferência do manifesto diretamente no AAB por bundletool 1.16.0 confirmou applicationId/versões/minSdk/targetSdk; `bundletool validate` terminou com código 0.

Não havia aparelho/emulador conectado (`adb devices` vazio). Testes funcionais manuais, persistência da preferência pela UI, geração visual de PDFs e envio WhatsApp não executados. Testes de domínio não comprovam transação real no Google Play nem homologam os fluxos completos de cadastro, pagamentos, fotos ou backup.

A primeira tentativa de build falhou ao acessar `C:\.android`. A execução foi ajustada para o JDK do Android Studio, SDK local, cache Gradle do repositório e diretório Java correto. Nenhuma credencial foi copiada ao repositório ou exibida.

## Artefato e assinatura

Entrega atual, após o refinamento dos rótulos do gráfico de 12/10/2026: `app/build/outputs/bundle/release/vendas-simples-v11.aab`, cópia idêntica de `app-release.aab`. Tamanho: **5.319.844 bytes**. SHA-256: `b0d0dffcce2c40497639a0c212c3068cf69419e761c49e394b1fa11a45b4d8fe`.

Assinado pela configuração release local existente. `jarsigner -verify -verbose -certs` retornou código 0 e `jar verified.`; certificado válido até 15/02/2054. A ferramenta avisa sobre certificado autoassinado/cadeia não reconhecida pelo truststore Java, ausência de timestamp e diferença de leitura JarFile/JarInputStream. O manifesto ZIP está na posição 931, após o conteúdo; uma verificação independente conferiu os hashes SHA-256 dos **929 arquivos de conteúdo**, todos cobertos pelo manifesto assinado. A assinatura criptográfica passou; a correspondência do certificado com a chave de upload cadastrada na Play ainda exige conferência no Console.

APK debug: `app/build/outputs/apk/debug/app-debug.apk`. Evidências locais ignoradas pelo Git: `.gradle-user/v11-backup-build.log`, `v11-backup-tests/`, `v11-bundle-validation.log`, `v11-signature.log`, `v11-manifest.xml` e `v11-artifact.json`. Artefatos e credenciais não integram o commit.

## Git

Commit local de implementação, testes e documentação; branch e hash informados na entrega. Sem alterações preexistentes no início e sem push, merge ou publicação. O bundle foi gerado antes do commit final; sua metadata de procedência Git pode apontar ao HEAD anterior, com as alterações compiladas da árvore local.

## Limites e pendências

O nome integra o backup SQLite formato 2; backups antigos/formato 1 preservam o nome local, ou a apresentação genérica em instalação nova. Cabeçalho PDF permite nomes longos, mas a validação visual em PDF real e aparelho continua pendente. Rotação/process death e rascunhos mantêm os limites existentes. Compatibilidade de atualização com certificado da versão publicada e disponibilidade do código 11 precisam ser conferidas no Play Console pelo responsável.

## Checklist manual no celular antes do upload

- [ ] Atualizar sobre V10 com dados sintéticos e confirmar preservação de clientes, vendas, pagamentos, fotos e licença.
- [ ] Primeiro acesso sem nome: convite opcional, apresentação genérica, Agora não/Voltar e reinício sem repetição.
- [ ] Salvar AleJoias, fechar/reabrir; editar para nome com acentos e nome longo de 120 caracteres; apagar o nome e verificar fallback.
- [ ] Tela inicial e Configurações no FREE e FULL_ACCESS; retrato/paisagem, tela estreita, fonte ampliada e teclado sem ações inacessíveis.
- [ ] Cobrar venda à vista e parcelada com pagamento parcial; conferir dados, vencimento brasileiro e única assinatura. Quitada não deve cobrar. Testar com/sem nome e cancelar o envio.
- [ ] Compartilhar imagem do resumo, PDF e CSV; conferir identificação e fallback sem WhatsApp instalado.
- [ ] Gerar os três PDFs com/sem nome, acentos, nome longo com/sem espaços, múltiplas páginas/fotos; conferir cabeçalhos, valores, totais, saldos e datas sem cortes.
- [ ] Criar/editar cliente e venda, datas no calendário e filtros; reiniciar e conferir persistência e ordenação.
- [ ] R$ 100,00 em três parcelas e pagamentos de R$ 30,00 e R$ 20,00; soma das parcelas R$ 100,00, pago R$ 50,00 e saldo R$ 50,00 nos fluxos correspondentes.
- [ ] Adicionar, ampliar e remover fotos; cancelar galeria/câmera/formulário; verificar relação por compra após reabrir.
- [ ] FREE: criar até 30 vendas, bloquear a 31ª; manter consultas, pagamentos existentes e CSV.
- [ ] FULL_ACCESS: venda acima do limite, backup e restauração com fotos e nome do negócio após reinstalação; licença deve ser restaurada separadamente pela Play.
- [ ] Pela Play com conta de teste: preço, compra aprovada/pendente/cancelada/recusada, indisponibilidade, reconhecimento e restauração da licença anterior.
- [ ] Testar offline, Android 24 e versão recente; conferir versão e certificado antes do upload manual.

## Notas sugeridas para Google Play

Personalize o aplicativo com o nome do seu negócio e use essa identidade nas mensagens e PDFs. Datas no padrão brasileiro e melhor espaçamento ao remover fotos de vendas.

## Complemento — backup do nome do negócio

O backup completo continua exclusivo de FULL_ACCESS, com personalização no FREE. O arquivo `.db` existente passa ao formato 2 somente em `VENDAS_SIMPLES_BACKUP_METADATA`, incluindo `business_name`. Schema financeiro 3, IDs, relações, fotos e valores preservados; não há segundo mecanismo de backup do usuário. A criação SAF e o fluxo interno de compartilhamento usam o mesmo `BackupManager`.

A restauração lê/valida uma única cópia, reconhece formatos 1 e 2, grava o nome com commit síncrono e atualiza o título da Activity. WhatsApp e PDFs leem a mesma preferência. Nome ausente, vazio ou nulo preserva o nome atual; instalação nova com backup antigo mantém fallback e convite opcional. Nome não vazio restaurado marca o convite como já oferecido.

`RestoreTransaction` grava banco anterior e estado da identidade em journal privado com sincronização de arquivos antes da troca. Falha reverte os dois; interrupção pendente é recuperada antes de abrir SQLite no próximo início. Falha de recuperação impede abrir a base e preserva os arquivos para nova tentativa. Journal é recuperação interna, não outro backup exportado. Não contém nem altera estado de licença. Energia/armazenamento permanentemente indisponíveis não são garantidos por testes de interrupção de processo.

Arquivos deste complemento: `BackupManager.kt`, `BusinessPreferences.kt`, `MainActivity.kt`, novo `RestoreTransaction.kt`, `BackupFormatTest.kt`, novo `RestoreTransactionTest.kt`, runner/contexto/cenários em `app/src/androidTest/`, `app/build.gradle` (runner somente de testes), `tools/validar_backup_v11.py`, README, arquitetura, produto, decisões, segurança e este relatório.

### Resultados do complemento

- `test assembleDebug assembleDebugAndroidTest bundleRelease --no-daemon`: BUILD SUCCESSFUL em 1m39s. **21 testes unitários por variante** (debug e release), zero falhas/erros; 5 novos testes cobrem sucesso conjunto, falha de persistência, interrupção simulada, nome legado/vazio e falha de substituição. Sem dependências novas de produção ou de testes; runner instrumentado usa APIs Android nativas.
- AVD isolado **Build10, Android 15/API 35**: `tools/validar_backup_v11.py --adb <adb-local> --serial emulator-5556` terminou com código 0. **11 testes instrumentados + 1 após encerrar e reiniciar o processo**, todos aprovados. SQLite/SharedPreferences reais em namespace sintético separado, sem abrir/apagar a base operacional. Hashes do banco e preferência operacionais do AVD preservados.
- Cenários instrumentados: nome no backup e igualdade de registros exportados; restauração em instalação vazia; acentos/caracteres especiais; parcelas/pagamentos parciais/fotos e sentinelas de preferências alheias à identidade preservados; formato 1 com/sem nome atual; nomes vazios/nulos; arquivo corrompido; versão de metadados inválida; bloqueio de exportação/restauração em todos os estados diferentes de FULL_ACCESS; novo backup depois de restaurar formato 1; persistência do nome e pagamentos em novo processo.
- Bundletool aprovou o AAB final, manifesto confirmou `com.vendassimples.app`, `11 / 1.0.11`, minSdk 24 e targetSdk 36. Jarsigner confirmou a assinatura; os 929 hashes de conteúdo cobertos pelo manifesto assinado foram verificados. Artefato atual e SHA-256 estão na seção de assinatura acima.
- `git diff --check`, links documentais e sintaxe do script aprovados. SQLite operacional, cálculos, Billing e regras comerciais não foram alterados; não há novas permissões.

Ocorrências do ambiente: duas tentativas offline do APK instrumentado falharam por dependência transitiva Kotlin `kotlin-stdlib-jdk8:1.8.0` ausente; o Gradle mascarou a resolução com erro interno de caminho. A resolução pela rede concluiu o build. O primeiro APK debug tinha chave diferente da instalação existente no AVD; apenas os APKs debug/teste foram regenerados com a chave debug local anterior (`assembleDebug assembleDebugAndroidTest --offline`: sucesso em 1m10s), sem apagar dados do AVD. A assinatura release não foi alterada e suas credenciais não foram expostas. O emulador foi temporariamente encerrado durante a compilação para liberar memória.

Limites: testes instrumentados usam arquivos locais e namespace sintético, não o seletor SAF real, provedores remotos ou transações Google Play. Reinício automatizado comprova persistência Android da preferência e banco em outro processo; não homologa visualmente a tela inicial/WhatsApp/PDFs nem substitui teste manual no celular. API 24, perda abrupta de energia e falha permanente de armazenamento não foram executadas. Nenhum teste manual no celular foi declarado realizado. Os resultados da primeira etapa acima são históricos; o AAB desta entrega contém o complemento.

### Homologação adicional no celular

- [ ] FULL_ACCESS: salvar nome com acentos, gerar backup pelo seletor de destino e conferir nome após restaurar em instalação nova; fechar/reabrir e verificar início, WhatsApp e os três PDFs.
- [ ] Restaurar formato 1 com/sem nome no aparelho; conferir que nome válido não foi apagado e dados financeiros/fotos permanecem íntegros.
- [ ] Formato 2 com nome vazio/nulo: preservar nome existente; novo backup após restauração deve conter o nome atualmente configurado.
- [ ] Comparar clientes, IDs, parcelas, pagamentos parciais, saldos e fotos antes/depois; testar o fluxo interno de compartilhamento quando acessível.
- [ ] Backup corrompido, versão incompatível, cancelamento do seletor/confirmação, provedor indisponível e espaço insuficiente: nenhum sucesso indevido nem perda de banco/nome.
- [ ] Interromper em ambiente sintético e reabrir; verificar recuperação conjunta e ausência de convite repetido após nome restaurado.
- [ ] FREE: personalização funciona, backup/restauração bloqueados; FULL_ACCESS e restauração de compras devem permanecer independentes do arquivo restaurado.

## Complemento — correções da exportação CSV

Os campos financeiros de `VendaRelatorio` são `Long` em centavos, confirmados pela leitura de `valor_total_centavos` e soma de `valor_pago_centavos` em `LocalDatabase.getRelatorio`. O exportador anterior usava `Long.toString()`, expondo centavos sem escala. Agora `BigDecimal.valueOf(cents, 2)` produz reais com exatamente duas casas e vírgula decimal, sem `R$`, ponto flutuante ou arredondamento. Valor, pago e saldo são os mesmos recebidos do relatório; nenhum cálculo ou dado persistido foi alterado.

O exportador anterior concatenava campos com `;` e registros com quebra de linha sem escaping. Isso reproduz deslocamento de colunas com ponto e vírgula e divisão do registro com quebras de linha no texto. O registro original “tutu — brinco” não foi disponibilizado para inspeção: a falha estrutural foi identificada no código e reproduzida com dados sintéticos, sem atribuir conteúdo específico ao registro real. Agora campos contendo `;`, vírgula, aspas, CR ou LF são delimitados por aspas; aspas internas são duplicadas e todo o texto original é preservado.

Formato escolhido: **UTF-8 com BOM**, separador **ponto e vírgula**, término de registros **CRLF**, oito colunas originais na mesma ordem. Cada parcela da lista recebida ocupa um registro lógico, mesmo quando uma célula contém várias linhas físicas. Não há linha extra `sep=;`. O arquivo anterior já era UTF-8, mas não tinha BOM: detecção incorreta da codificação por aplicativos é uma causa provável dos acentos corrompidos, não confirmada com o arquivo da homologação. Datas ISO válidas continuam apresentadas em `dd/MM/yyyy` pelo conversor existente; campos ausentes ficam vazios e textos legados não reconhecidos são preservados, sem inventar datas.

Arquivos deste complemento: `MainActivity.kt` (delegação da escrita), novo `SalesCsvExporter.kt`, novo `SalesCsvExporterTest.kt`, novo `tools/validar_csv_v11.py` e este relatório. As três chamadas de exportação mantêm suas listas, filtros, critérios, ordem e compartilhamento. Nenhuma alteração em SQLite, cálculos financeiros, backup/restauração, Billing, FREE/FULL_ACCESS ou versão; sem novas dependências.

### Homologação do CSV em planilhas

- [ ] No Google Planilhas, importar o arquivo com separador `;` e localidade Brasil; conferir as oito colunas, acentos e valores numéricos sem `R$`.
- [ ] No Excel, usar Dados → De Texto/CSV, codificação UTF-8 e delimitador `;`; conferir números com localidade portuguesa/Brasil e datas `dd/MM/yyyy`.
- [ ] Exportar cliente e descrição com acentos, `;`, vírgulas, aspas e múltiplas linhas, incluindo o caso “tutu — brinco”; conferir que uma parcela continua em uma linha lógica e as quebras ficam dentro da célula.
- [ ] Conferir centavos, zero, pagamento parcial, quitação e compra de R$ 100,00 em três parcelas; comparar com o relatório e os filtros selecionados.

Limites: CSV não incorpora estilo monetário nem tipos de célula; a inferência de números/datas depende da localidade e das opções de importação. Abrir diretamente pode selecionar outro delimitador conforme configuração do aplicativo. Um leitor que divide simplesmente por linhas não suporta textos multilinha; deve usar parser CSV com aspas. Testes manuais no Google Planilhas, Excel e celular não foram realizados nesta etapa.


### Resultados das correções de CSV

- `test assembleDebug assembleRelease bundleRelease --no-daemon`: **BUILD SUCCESSFUL em 2m30s**, incluindo lint vital release. **27 testes por variante**, debug e release, zero falhas/erros/skips; os 21 anteriores e 6 novos testes de CSV. Cobertura: centavos e pagamentos parciais, zero, saldo negativo, precisão de Long, acentos/BOM, datas, textos com separadores/aspas/CR/LF, ausência de campos, relatório vazio e uma/várias parcelas.
- `python tools/validar_csv_v11.py`: aprovado. Parser padrão `csv.reader` em modo estrito leu o arquivo efetivamente produzido pelo exportador Kotlin: cabeçalho e 7 registros, todos com 8 colunas. Textos especiais preservados integralmente, datas válidas e igualdade de valores em centavos conferida com `Decimal`, incluindo soma de três parcelas igual a R$ 100,00.
- A primeira execução offline passou nos testes e produziu APK debug, mas falhou no lint do release por `lint-gradle:31.5.2` ausente do cache. A execução com resolução pela rede concluiu todos os builds e testes; nenhuma checagem foi desabilitada. Uma tentativa do parser antes de a fixture existir foi repetida após os testes e aprovada.
- Bundletool validou o AAB; manifesto confirmou `com.vendassimples.app`, `11 / 1.0.11`, minSdk 24 e targetSdk 36. Jarsigner confirmou a assinatura e a verificação independente aprovou os 929 hashes de conteúdo. Caminho, tamanho e SHA-256 atuais na seção de assinatura. Configuração e credenciais de assinatura preservadas.
- `git diff --check` aprovado. Logs locais ignorados: `.gradle-user/v11-csv-build.log`, `v11-csv-build-online.log`, além das evidências de assinatura; fixture sintética em `app/build/test-artifacts/sales-v11.csv`. Não houve push nem publicação. Os resultados de backup anteriores são históricos, e nenhum fluxo de backup foi alterado nesta correção.

## Complemento — Financeiro e Relatório por Cliente compactos

Financeiro e Relatórios mantém a tela única com mês, vendido, recebido, a receber, clientes com saldo, cards/parcelas do mês, vencidas, gráfico, relatório por período e PDF financeiro. O mês aparece uma vez em seletor compacto no início do conteúdo, próximo ao título, com toque mínimo de 48 dp. A escolha continua reconstruindo todos os indicadores e ações com a lista do mês selecionado. “Clientes com saldo” explicita a contagem anterior de clientes com débito, sem mudar a população.

Indicadores usam duas colunas quando largura e fonte permitem; fonte ampliada, tela estreita ou valores largos empilham os componentes. A largura mínima considera a medida real dos textos, e a altura passa de fixa a conteúdo com mínimo de 64 dp. Gráfico tem margens menores e rótulos sem repetição visual dos totais; descrição acessível mantém cada valor. PDF e relatório por período compartilham uma faixa responsiva com alvos de pelo menos 48 dp.

Relatório por Cliente remove o rótulo duplicado da pesquisa e organiza pesquisa/Aplicar em faixa responsiva. Cards têm margens e padding menores, nome completo com altura livre, quantidade de cards/vendas, vendido/recebido em colunas responsivas e a receber/faltante destacado por rótulo, negrito e posição, além da cor. Sem ellipsize ou limite de linhas para nomes e valores. Aplicar, ação do teclado, limpar pesquisa, ordenação, agrupamento e toque para histórico mantêm os comportamentos anteriores.

Cabeçalho fixo, seta, callback do Voltar Android, menu de três pontos e ScrollView existentes preservados. Nenhum botão redundante de retorno foi acrescentado. Filtros de relatório, somas, vencimentos, pagamentos, SQL/schema, PDFs, CSV, backup, personalização e Billing não foram modificados. `versionCode 11`, `versionName 1.0.11` mantidos.

Arquivos deste complemento: `MainActivity.kt`, novo `tools/validar_financeiro_v11.py` e este relatório. O script exige APK debug no AVD Build10 e fixture sintética UI existente; não cria nem modifica registros. Compara totais financeiros com consultas SQLite independentes, testa pesquisa/navegação e restaura configurações de tela/fonte ao terminar. Evidências locais em `.gradle-user/finance-v11/`, fora do Git.

### Homologação adicional das telas

- [ ] No celular, comparar todos os indicadores e detalhamentos com a mesma população do mês; trocar entre dois meses com valores diferentes e conferir gráfico, retorno e PDF.
- [ ] Confirmar que Clientes com saldo conserva a contagem de clientes com débito; conferir cards/parcelas e vencidas sem confundir compras com parcelas.
- [ ] Testar pesquisa vazia, parcial, sem resultados, caixa diferente, Aplicar, ação do teclado e limpar; entrar no histórico e retornar pela seta e pelo Android.
- [ ] Usar nomes longos, valores altos, fonte ampliada, tela pequena e paisagem; conferir texto completo, ausência de sobreposição e acesso aos controles com teclado aberto.
- [ ] Conferir PDF financeiro e relatório por período, menu de três pontos e demais funcionalidades com dados sintéticos antes da distribuição.

Limites preservados: rolagem, rascunhos e seleção em memória não ganham garantia após rotação/process death. Resultados automatizados não equivalem à homologação manual no celular nem à atualização pela Play.


### Resultados do refinamento financeiro

- `test assembleDebug assembleRelease bundleRelease --offline --no-daemon`: **BUILD SUCCESSFUL em 3m38s**, incluindo lint vital. **27 testes por variante**, debug e release, zero falhas/erros/skips. Sem novas dependências ou alteração da configuração comercial/assinatura.
- Bundletool validou o AAB e confirmou applicationId, versão 11 / 1.0.11 e SDKs. Jarsigner confirmou a assinatura e a verificação independente aprovou os 929 hashes de conteúdo. Artefato atual e hash na seção de assinatura; logs em `.gradle-user/v11-finance-build.log` e evidências locais de assinatura.

- `python tools/validar_financeiro_v11.py --serial emulator-5556`: **16 checks aprovados** no AVD Build10, Android 15/API 35, APK debug atualizado sem apagar dados. Cenários: totais financeiros e do cliente iguais às somas independentes SQLite; grade em duas colunas; remoção do mês duplicado; seleção do mês e retorno do detalhe; filtro sem distinção de caixa; teclado; abertura do histórico; seta preservando pesquisa; limpar pesquisa; Voltar Android para Financeiro; menu de três pontos; indicadores empilhados em 320 dp/fonte 150%; acesso ao PDF após rolagem e pesquisa/retorno com teclado. Banco operacional sintético preservado byte a byte por SHA-256 antes/depois.
- Capturas automatizadas de Financeiro e cliente na configuração normal e Financeiro/pesquisa com fonte 150% foram inspecionadas. Valores visíveis completos e sem sobreposição nos cenários capturados; em tela pequena o conteúdo usa rolagem vertical. Nenhum teste manual no celular foi realizado. O smoke confirmou a seleção do único mês da fixture (2026-10), não a troca entre dois meses distintos, nomes longos, valores extremos, paisagem ou API 24; esses cenários permanecem no checklist.
- Ocorrências do ambiente/teste: convite inicial de personalização, diálogo de lentidão do System UI e acesso temporariamente negado a `/sdcard` interromperam tentativas iniciais. O smoke trata o convite/aviso do sistema e captura em `/data/local/tmp` com validação/repetição da árvore XML. Uma seleção inicial encontrou a pesquisa em vez do card por texto semelhante; o alvo do card foi corrigido e a abertura do histórico passou a ser explicitamente conferida. Configurações originais de tamanho/densidade/fonte restauradas ao terminar. Nenhuma dessas tentativas alterou registros financeiros.
- `git diff --check` e sintaxe Python aprovados. Sem push, publicação ou alteração de funcionalidades fora das telas afetadas. Resultados de CSV/backup anteriores permanecem históricos; o AAB atual inclui todos os complementos V11.

## Ajustes finais após homologação informada pelo usuário

O usuário informa homologação manual da V11 no celular e solicita exclusivamente três ajustes. Essa informação não representa execução manual pelo Codex nem aprovação do APK com os ajustes finais.

- Cabeçalho compartilhado mantém **Vendas Simples** como marca fixa. Nome opcional aparece abaixo, em linha secundária, seguido de **Seu controle de vendas**, inclusive em paisagem. A linha do negócio é ocultada sem nome; nome longo tem reticências para conter altura e descrição acessível completa, além da edição existente. Título/fonte menores e largura ponderada preservam as áreas de 48 dp de Voltar e três pontos. Atualização ao salvar e restaurar backup usa o mesmo helper, sem alterar nome exportado, PDFs ou WhatsApp.
- Removido o botão Configurações — Nome do negócio do menu principal. **Personalizar negócio** foi acrescentado ao menu de três pontos antes das opções existentes, fora da condição FULL_ACCESS. Reutiliza diálogo, preferência, salvar/cancelar e convite inicial opcional; persistência e regras comerciais preservadas.
- Fotos do rascunho usam miniatura à esquerda e botão contextual à direita, com 16 dp, ícone de lixeira, texto e identificação acessível por índice. Área mínima de 48 dp, miniatura de 72 dp, sem botão de largura total. Tela menor que 360 dp ou fonte acima de 130% empilha os elementos. Após remoção, índices são reconstruídos; visualização, compressão, câmera/galeria, salvamento e Cancelar continuam nos componentes existentes.

Arquivos: `MainActivity.kt`; novo `FinalUiInstrumentedTests.kt` e fase optativa `final-ui` do runner de testes existente; novo `tools/validar_ajustes_finais_v11.py`; README, este relatório e skills `ui-ux-android`/`vendas-simples-interface`. O runner padrão de backup permanece com sua seleção anterior. Sem alteração em SQLite, cálculos, filtros, relatórios, CSV, PDFs, WhatsApp, formato de backup, Billing, preço, permissões ou versão 11 / 1.0.11.

As skills registram marca separada do negócio, configurações secundárias no menu e ações compactas por miniatura como candidatos ao AleJoias Vendas. O repositório AleJoias não foi modificado e eventual portabilidade exige adaptação à identidade/funcionalidades próprias e homologação separada.

### Checklist final no celular

- [ ] Atualizar com o APK final e conferir banco, licença e dados já homologados; validar também instalação limpa e convite opcional.
- [ ] Sem nome: marca e slogan. Com nome: marca, nome secundário e slogan em todas as telas. Usar acentos, 120 caracteres, fonte ampliada, retrato/paisagem e conferir seta/menu sem sobreposição.
- [ ] Personalizar negócio em FREE/FULL_ACCESS; editar, salvar, cancelar e reabrir o app. Ausência do botão antigo e todas as opções anteriores do menu preservadas.
- [ ] Anexar uma e várias fotos por câmera/galeria; ampliar e remover primeira, última e única; conferir índices, somente a foto selecionada removida e nenhum registro existente afetado ao cancelar.
- [ ] Nova Venda/edição em 320 dp e fonte ampliada, teclado e paisagem; acessar Salvar/Cancelar e ações de foto por rolagem quando necessário.
- [ ] Reconfirmar WhatsApp, PDFs, CSV, backup/restauração do nome e compras pela Play com contas de teste, sem inferir aprovação desses fluxos a partir dos testes visuais.

Limites: linha secundária longa usa reticências na UI, mantendo nome completo acessível/configurável e nas saídas existentes. Homologação final no celular e envio Alpha continuam separados; não houve push nem upload.


### Resultados dos ajustes finais

- Testes unitários: **27 por variante**, debug e release, zero falhas/erros/skips. `test assembleDebug assembleDebugAndroidTest assembleRelease bundleRelease --offline --no-daemon`: **BUILD SUCCESSFUL em 1m33s** na confirmação separada. APKs e AAB gerados com versão 11 / 1.0.11.
- `lint` completo foi executado e **falhou com 3 erros e 68 avisos** em debug: `NewApi` em `styles.xml` (`windowLightNavigationBar`, API 27 versus minSdk 24), `NotificationPermission` em `manager.notify` e `SuspiciousIndentation` em `etiquetaStatus`. Os três trechos foram conferidos em `git show HEAD` anterior aos ajustes e são preexistentes. Não houve supressão, baseline ou mudança fora dos três ajustes autorizados. Logs: `.gradle-user/v11-final-build.log`, `v11-final-build-confirm.log`; relatório `app/build/reports/lint-results-debug.html`. Lint completo permanece pendência explícita, portanto não declarar todos os checks de release aprovados.
- Bundletool validou o AAB e confirmou app/versão/SDKs. Jarsigner aprovou assinatura; verificação independente conferiu os 929 hashes de conteúdo. Configuração de assinatura local preservada sem exposição de credenciais; caminho/tamanho/SHA-256 na seção de assinatura.

- AVD Build10, Android 15/API 35: `tools/validar_ajustes_finais_v11.py --serial emulator-5556` terminou com código 0, **16 checks aprovados**, incluindo **5 testes nativos em cada uma de 3 configurações** (normal, 320 dp/fonte 200%, paisagem/fonte 200%): marca e slogan sem nome; nome longo com acentos/caracteres especiais e descrição acessível; preferência em nova Activity; ausência do botão antigo; adição de três imagens sintéticas por URI/compressão e remoção primeira/última/única, preservando bytes e índices das demais e espaçamento do componente.
- Smoke pela UI real: acesso pelo menu, ações Salvar/Cancelar, atualização imediata, persistência após encerramento/reinício do processo, cancelamento sem alterar nome, Nova Venda com ações de rodapé acessíveis em retrato/paisagem e fonte 200%. Banco comparado por SHA-256 antes/depois, byte a byte preservado; arquivo original da identidade e configurações de tamanho/densidade/fonte restaurados. Evidências em `.gradle-user/final-ui-v11/` e `v11-final-ui.log`, ignoradas pelo Git. Primeira tentativa excedeu timeout de 45 s para os cinco ciclos de Activity; tempo específico da instrumentação ampliado para 180 s, nova execução aprovada. Aviso de lentidão do System UI tratado pelo smoke.
- Capturas do cabeçalho normal/com nome e 320 dp/fonte 200%, além do rodapé de Nova Venda em paisagem/fonte 200%, foram inspecionadas. Isso é inspeção de capturas automatizadas, não homologação manual no celular. Testes de fotos exercitam compressão e views do componente com dados sintéticos; não homologam os seletores reais de câmera/galeria nem a disposição visual de fotos no formulário completo. Esses fluxos, pré-visualização, API 24, teclado nos formulários finais e transações Google Play permanecem no checklist manual.
- Parser CSV padrão executado novamente: aprovado, 7 registros com 8 colunas, textos preservados e valores/datas exatos. Sintaxe Python e `git diff --check` aprovados. Skills preservam frontmatter e conteúdo anterior, com novos padrões documentados em português; nenhuma alteração no AleJoias.

- Regressão instrumentada existente: `tools/validar_backup_v11.py --adb <adb-local> --serial emulator-5556` aprovada novamente: **11 cenários + 1 em novo processo**, banco e preferência operacionais preservados por hash. A fase padrão do runner manteve a seleção anterior. Log `.gradle-user/v11-final-backup.log`.

## Revisão integrada — 12/10/2026

Pedido: compactação final por cliente, investigação do CSV no Google Planilhas Android, regressão e preparação Alpha. Estado inicial: branch `release/v11-personalizacao`, árvore limpa. Preservados `11 / 1.0.11`, applicationId e assinatura existentes. Não houve push, upload, alteração de preço/produto no Console ou modificação do AleJoias.

### Cards por cliente

Cabeçalho único com nome completo e quantidade de vendas, mantendo a contagem anterior de registros/cards (cada parcela é um registro). As somas e a população não foram alteradas. `ClientReportValues` recebe os valores já calculados/formatados e organiza Vendido, Recebido e Faltante em três colunas quando couberem; largura real e fonte efetiva determinam duas/uma coluna. Rótulos menores, fonte normal em Vendido/Recebido, somente o valor Faltante em negrito. Sem altura fixa, reticências ou limite de linhas para nomes/valores; em uma coluna, valores extremos podem quebrar linha. Padding e margens menores.

### CSV: causa e tratamento

O arquivo anterior era UTF-8 com BOM. Os exemplos informados pelo usuário são reproduzidos byte a byte ao decodificar UTF-8 como Latin-1: `EF BB BF` vira `ï»¿`, e os bytes de Descrição viram `DescriÃ§Ã£o`. O mecanismo está comprovado em teste; não houve inspeção do arquivo original nem do decodificador interno do Google Planilhas da homologação.

O exportador único agora oferece **CSV padrão — UTF-8**, sem BOM, e **CSV para Excel — com BOM**. Mesmos registros/colunas, escaping, delimitador `;`, CRLF, datas brasileiras e decimais exatos; somente o prefixo difere. Unicode completo preservado, incluindo emoji/ideogramas; nenhum texto do usuário é substituído e nenhuma conversão ANSI com perda de caracteres foi introduzida. A escolha de formato é acrescentada às ações existentes, preservando filtros e compartilhamento.

Remover o BOM elimina o prefixo no arquivo padrão, mas **não obriga um leitor que escolhe outra codificação a reconhecer acentos**. Compatibilidade de abertura direta no Google Planilhas Android/Excel reais não foi executada. Procedimento de importação, diferenças entre perfis e fontes oficiais em [EXPORTACAO_CSV.md](EXPORTACAO_CSV.md). Não declarar “compatibilidade definitiva no celular” apenas porque o parser passou; conferir o novo arquivo na última homologação. Se o Android continuar interpretando incorretamente, importar o original pelo Planilhas no computador, verificar cabeçalhos/separador/localidade e abrir a planilha convertida no Android.

### Integração e preservação

Inspeção comparou 12 funções protegidas da Activity com o HEAD anterior: configuração/cabeçalho/fotos, formulário, Financeiro/detalhes/período, histórico, WhatsApp, PDF, backup e pagamento permanecem iguais. Evidência `.gradle-user/v11-integrated-audit.json` é inspeção de código, não teste funcional. SQLite, cálculos, IDs, fotos persistidas, Billing 9.1.0, produto `full_access`, preço dinâmico e limite FREE 30 não foram alterados.

Arquivos: `MainActivity.kt`, novo `ClientReportValues.kt`, `SalesCsvExporter.kt`, `SalesCsvExporterTest.kt`, `FinalUiInstrumentedTests.kt`, ferramentas `validar_csv_v11.py`, `validar_financeiro_v11.py`, `validar_ajustes_finais_v11.py`; este relatório, novo `EXPORTACAO_CSV.md`, monetização, README e skills `ui-ux-android`, `regressao-release`, `vendas-simples-release`. A skill de regressão ganhou frontmatter consistente, checks de bytes/Unicode, formatos CSV e distinção de evidências. Padrões de cards, preservação e release ficam candidatos ao AleJoias, com identidade e dados próprios preservados.

### Preparação Alpha — execução manual futura

- [ ] Homologar APK final no celular sobre a versão anterior, comparando clientes, vendas/parcelas, pagamentos, saldos, fotos e licença antes/depois.
- [ ] Cliente/quantidade no cabeçalho e três valores em colunas; somente Faltante em negrito. Testar valores altos, nomes longos, fonte 200%, 320 dp, paisagem, teclado, pesquisa e retorno.
- [ ] No Google Planilhas Android, abrir CSV padrão recém-exportado e registrar versão do app, cabeçalhos, acentos no conteúdo, datas, números e colunas. Se falhar, usar a importação documentada; não avaliar arquivo antigo/regravado com mojibake.
- [ ] No Excel, testar opção com BOM e importação UTF-8/`;`/Brasil. Conferir aspas, delimitadores, quebra dentro da célula, pagamento parcial e várias parcelas.
- [ ] Conferir marca/nome/slogan, menu, convite opcional, salvar/cancelar/reiniciar; nome em WhatsApp/PDF e backup/restauração em aparelho novo/arquivo legado.
- [ ] Câmera/galeria, várias fotos, remoção primeira/última/única, ampliação, persistência e PDF; Salvar/Cancelar, confirmação de descarte e navegação.
- [ ] Mês/período no Financeiro, totais do mesmo conjunto e PDF; testar offline e Android 24.
- [ ] Pela Play com contas de teste: FREE bloqueia 31ª tentativa, FULL_ACCESS mantém acesso, preço vem da Play, compra/restauração/pendente/cancelamento e atualização preservam licença/dados. Testes de domínio não comprovam transação real.
- [ ] Resolver ou avaliar formalmente erros de lint ainda pendentes antes do envio. Conferir código 11 disponível, chave de upload correta, política/ficha de dados/classificação e notas V11.
- [ ] Após aprovação explícita, responsável prepara release no teste fechado Alpha com o AAB de hash informado e registra notas/testadores. Upload/envio para análise/distribuição não foram executados nem autorizados nesta tarefa.

Notas candidatas para Alpha: cards por cliente mais compactos, personalização gratuita com marca preservada, nome no backup completo e opções de CSV em português brasileiro. Sem promessa de sincronização ou nova regra comercial.


### Evidências desta revisão integrada

- `test assembleDebug assembleDebugAndroidTest assembleRelease bundleRelease --offline --no-daemon`: **BUILD SUCCESSFUL em 4m52s**. **30 testes por variante**, zero falhas/erros/skips; 3 novos casos CSV verificam diferença exclusiva do BOM, reprodução do decodificador incorreto e preservação integral de Unicode/textos do usuário. `tools/validar_csv_v11.py`: aprovado em ambos os arquivos realmente gerados pelo Kotlin, 7 registros e 8 colunas, datas/centavos/parcelas e textos especiais intactos.
- Lint completo executado em debug e release separadamente: **3 erros e 70 avisos em cada variante**. Os erros permanecem nos trechos preexistentes `NewApi` (styles.xml/API 27), `NotificationPermission` (notify) e `SuspiciousIndentation` (etiquetaStatus). `lint --continue` falhou em 2m16s; `lintRelease` falhou em 4m11s. Lint vital/build release passaram. Não houve supressão/baseline ou alteração dessas funcionalidades fora do escopo. Relatórios em `app/build/reports/lint-results-debug.html` e `lint-results-release.html`; logs `.gradle-user/v11-integrated-lint*.log`. Essa pendência continua impedindo declarar todas as checagens de release aprovadas.
- `tools/validar_ajustes_finais_v11.py`: **16 checks aprovados**, incluindo **21 execuções nativas** (7 em normal, 7 em 320 dp/fonte 200%, 7 em paisagem/fonte 200%). Novos casos medem os valores pequenos/extremos em larguras distintas, verificam fonte normal/negrito somente em Faltante, ausência de reticências, nome longo completo e totais de dois meses distintos com cache sintético. Os casos anteriores de marca/nome, menu, preferência, fotos e rodapé continuam aprovados. Banco byte a byte preservado e identidade/configurações originais restauradas. Uma tentativa parou por diálogo de lentidão do Pixel Launcher; helper passou a tratar somente avisos de System UI/Pixel Launcher com repetição limitada e captura de evidência, sem ocultar falhas do app. Repetição terminou com código 0.
- Bundletool validou AAB e manifesto `com.vendassimples.app`, `11 / 1.0.11`, minSdk 24, targetSdk 36. Jarsigner aprovou assinatura e 929 hashes de conteúdo foram conferidos independentemente. Artefato, tamanho e SHA-256 atuais na seção de assinatura. O artefato foi regenerado novamente após o ajuste de singular/plural descrito abaixo.
- Google Planilhas (`com.google.android.apps.docs.editors.sheets`) e Excel (`com.microsoft.office.excel`) não instalados no AVD. Não houve teste real nesses leitores nem teste manual no celular. Valores altos/nome longo são testes de views e cache sintético; dados financeiros operacionais não foram substituídos. A seleção UI da fixture usa seu único mês, enquanto dois meses com totais diferentes são verificados no teste nativo de estado/renderização.

Preparação de faixa fechada Alpha segue o [guia oficial de releases da Play](https://support.google.com/googleplay/android-developer/answer/9859348?hl=pt-BR), após aprovação explícita e avaliação das pendências; não houve acesso/mutação no Console. O preço R$ 19,90 é informação comercial confirmada pelo usuário, não conferência da configuração atual na Play.

- `tools/validar_financeiro_v11.py`: **21 checks aprovados**, com Wi-Fi e dados móveis desativados e restaurados ao finalizar. Conferência independente das somas SQLite, pesquisa/retorno/teclado, grade, mês, gráfico e acesso às ações. Os dois CSVs foram gerados pela Activity, lidos diretamente do cache Android e comparados: padrão sem BOM/cabeçalho UTF-8, Excel igual ao padrão mais EF BB BF, 29 registros lógicos (cabeçalho + 28 parcelas) com 8 colunas. Choosers foram cancelados sem selecionar destinatário. Banco permaneceu idêntico por hash. Evidências em `.gradle-user/finance-v11/` e `v11-integrated-finance-ui.log`.
- `--discard-only`: **6 checks aprovados**; Cancelar e Voltar Android solicitam confirmação com alterações, Continuar editando mantém rascunho e Descartar retorna ao menu sem gravação. Banco e identidade preservados. Evidências `.gradle-user/discard-v11/`, `v11-integrated-discard.log`.
- Regressão de backup: **11 testes + 1 em outro processo**, todos aprovados; parcelas/pagamentos/fotos e identidade operacional preservados. Log `.gradle-user/v11-integrated-backup.log`. Os testes unitários de compatibilidade mantêm bloqueio da 31ª tentativa FREE e domínio FULL_ACCESS/pendente/indisponível; compra/restauração real da licença pela Play permanece manual.
- Captura de cliente filtrado inspecionada: três valores completos na mesma faixa e somente Faltante em negrito. A conferência revelou o texto “1 vendas”; ajuste final usa “1 venda” e “2 vendas” etc., com teste nativo dos dois ramos. As suítes de views são repetidas após essa alteração e o AAB é regenerado para refletir o código final.

Para homologar atualização, usar APK/instalação com assinatura compatível; o APK debug é destinado ao ambiente debug. Upgrade de instalação obtida na Store e preservação da licença devem ser verificados pela faixa Play autorizada, com o certificado correto.

- Build final após singular/plural: **BUILD SUCCESSFUL em 4m12s**, 30 testes unitários por variante novamente aprovados; debug, androidTest, release e bundle regenerados. Parser CSV novamente aprovado. --views-only repetiu **21 execuções nativas** do código final nas três configurações, incluindo cabeçalho singular/plural e layout/valores altos; 3 checks de grupo e 2 de preservação aprovados. Evidências .gradle-user/final-values-v11/, v11-integrated-final-values.log, v11-integrated-final-build.log.
- AAB final copiado do bundle recém-gerado e verificado após essa correção: **5.319.121 bytes**, SHA-256 `3132674d924bdafa29267c1fffe3ceb284be4a7bfc5dcc2ff5d19ffaba24ea58`; assinatura/integridade e manifesto novamente aprovados. Nenhum código de produção foi alterado após o build final. O lint completo citado acima antecede apenas a correção de singular/plural; os três trechos com erros permanecem inalterados.


## Gráfico financeiro: valores e percentuais — 12/10/2026

Refinamento restrito ao texto das três barras existentes. Nome à esquerda, moeda e percentual à direita, fonte 13 sp; largura real dos textos determina linha única ou empilhamento, com altura livre e sem reticências. Cores, barras, margens e filtros preservados. Ao selecionar outro mês, o dashboard reconstrói os rótulos com os mesmos totais já calculados.

Percentuais usam o total vendido como denominador, uma casa decimal e HALF_UP, sem conversão para ponto flutuante. Vendido positivo mostra 100,0%; denominador zero (ou negativo defensivamente) mostra 0,0%. Recebido e a receber são independentes: nenhum ajuste, limitação a 100% ou residual artificial. Arredondamento independente pode produzir diferença de 0,1 ponto em empates; investigar divergências nos totais originais antes de corrigir dados. A população do mês e as somas permanecem iguais.

O formatador monetário anterior passa por Double. Exclusivamente os novos textos do gráfico usam BigDecimal.valueOf(centavos, 2) e NumberFormat pt-BR, preservando centavos inclusive em Long.MAX_VALUE; nenhum outro formatador ou saída foi modificado. SQLite, relatórios/PDFs, CSV, backup, Billing e personalização permanecem intactos. Versão 11 / 1.0.11; sem push ou publicação.

Arquivos: MainActivity.kt; novos FinancialChartLabel.kt, FinancialChartPercentage.kt e FinancialChartPercentageTest.kt; FinalUiInstrumentedTests.kt; tools/validar_ajustes_finais_v11.py; este relatório e skills ui-ux-android/regressao-release.

Checklist de homologação no celular: conferir exemplo 3.050,00 / 238,00 / 2.812,00 (100,0% / 7,8% / 92,2%), mês vazio, troca de mês, valores altos, 320 dp, paisagem, fonte ampliada, rótulos acessíveis e ausência de cortes. Homologação anterior não comprova os novos textos.


### Verificações do refinamento do gráfico

- Build final `test assembleDebug assembleDebugAndroidTest assembleRelease bundleRelease --offline --no-daemon`: BUILD SUCCESSFUL em 4m36s; **35 testes unitários por variante**, zero falhas/erros/skips. Cinco novos casos cobrem exemplo de homologação, denominador zero, extremos sem overflow, valores inconsistentes/arredondamento independente e preservação de todos os centavos. Log `.gradle-user/v11-chart-final-build.log`.
- Inspeção de LocalDatabase.getRelatorio confirma valor_total_centavos, soma de pagamentos e saldo = valor - pago; o dashboard agrega esses três campos sobre o mesmo filtro. Sem inconsistência de regra encontrada; dados reais de homologação não foram inspecionados.
- Bundletool e jarsigner aprovaram o AAB final, manifesto 11 / 1.0.11 confirmado e 929 hashes de conteúdo assinado verificados. Artefato atual na seção de assinatura.
- Lint vital release passou no build. Lint completo não foi repetido neste refinamento; os 3 erros e 70 avisos registrados na revisão anterior permanecem pendência, sem supressão ou alteração fora do escopo.

- Confirmação final após limitar atualizações de LayoutParams à mudança horizontal/vertical: BUILD SUCCESSFUL em **3m08s**, novamente 35 testes por variante sem falhas. Todos os APKs/AAB regenerados; assinatura/manifesto/929 hashes novamente aprovados. Log `.gradle-user/v11-chart-confirm-build.log`.
- Primeira execução de views passou normal e 320 dp/fonte 200%; paisagem foi interrompida deliberadamente para incorporar a revisão de medição. O wrapper confirmou banco byte a byte e identidade restaurados. Não contabilizar essa execução interrompida como aprovação completa. Evidências `.gradle-user/chart-initial-v11/` e `v11-chart-views.log`.
- Sintaxe da ferramenta Python, links locais das skills e git diff --check aprovados. Validador quick_validate.py não executou por PyYAML ausente; os frontmatters foram comparados com HEAD e permanecem idênticos, sem novos placeholders. Conteúdo revisado conforme skill-creator, sem novas dependências ou alteração da política de invocação.

- Repetição final no AVD Build10/API 35: **24 execuções nativas aprovadas** (8 normal, 8 em 320 dp/fonte 200%, 8 paisagem/fonte 200%). Verificados moeda/percentual completos, largura das linhas, ausência de reticências/sobreposição e disposição horizontal quando os textos cabem; valores normais, zero e extremos. Estado/renderização de dois meses distintos e mês vazio conferem atualização dos rótulos. Seletor preservado por inspeção; não houve homologação manual de seleção desses meses no celular.
- Wrapper terminou com 5 checks aprovados: três grupos, banco byte a byte preservado e identidade original restaurada; tamanho/densidade/fonte do AVD restaurados. Evidências `.gradle-user/final-values-v11/` e `v11-chart-confirm-views.log`. Nenhum teste visual manual no celular ou API 24 foi realizado neste refinamento. Pendências: checklist dos novos textos no aparelho e lint completo já registrado.
