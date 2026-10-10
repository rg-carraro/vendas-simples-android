# Atualização Alpha — UI/UX e Billing

Data: 07/10/2026. Base Git: `5b496bc`; alterações não commitadas. Os 19 cenários anteriores são informação do usuário, não execução desta sessão.

## Diagnóstico e soluções

- Navegação: `onBackPressed` antigo mantinha uma tabela diferente dos botões da UI, inclusive nos históricos de clientes. Agora `OnBackPressedDispatcher` invoca a mesma ação configurada para a seta do cabeçalho. No menu o callback fica desativado, permitindo saída padrão. Filtros e meses continuam nas propriedades existentes; o conteúdo das telas ainda é renderizado pelas funções atuais ao retornar.
- Formulário: Cancelar e Voltar solicitam confirmação se campos ou fotos tiverem alterações. Android 33+ usa callback da janela para gestos; versões anteriores usam evento Voltar. Toque fora não descarta o formulário.
- Nova Venda: margens menores, valor e parcelas na mesma linha quando largura/fonte permitem, descrição com duas linhas mínimas e até três visíveis. Em largura menor que 360 dp ou fonte acima de 130%, campos ficam empilhados. Todos os campos e ações de fotos são mantidos. Salvar/Cancelar são os botões fixos do diálogo, fora do ScrollView; altura limitada a 85% da tela, com ajuste para teclado.
- Billing: não foram encontrados preços comerciais fixos no código de produção nem no site. O preço já vinha de ProductDetails. Agora produto/preço ausentes, consulta falha ou serviço desconectado impedem compra; menu e desbloqueio não oferecem preço fictício. A chamada de compra, confirmação, acknowledge e restauração continuam na integração existente. Biblioteca 9.1.0, produto `full_access` e opção `full-access` preservados.

## Arquivos desta alteração

`MainActivity.kt`, `PlayBillingGateway.kt`, `app/build.gradle`, `README.md`, `docs/DECISOES.md`, `docs/MONETIZACAO.md`, `docs/RELEASE_PLAY_STORE.md`, `docs/VALIDACAO.md` e este relatório. Ícones, alteração prévia de AndroidManifest e versionCode 9 já estavam pendentes antes do trabalho; foram preservados. Sem alterações no banco, migrations, cálculos, FREE 30 ou exportações.

## Versão e artefato

Preparado `versionCode = 10`, `versionName = 1.0.0`: HEAD tinha 8, árvore de trabalho tinha 9. Não houve acesso ao Console para confirmar códigos já usados; verificar antes de upload. Sem commit, push ou publicação.

Artefato gerado e assinado: `app/build/outputs/bundle/release/app-release.aab`. Build final concluído; tamanho e SHA-256 estão na seção de artefatos ao final. AAB inclui os ícones previamente pendentes.

## Validação e limites

Busca `49[,.]90|19[,.]90|R\$|4990|1990` em `app/src/main` e `site`: apenas o formatador genérico contém R$. Os valores de testes monetários são fixtures, não preços exibidos. `git diff --check` executado.

Na primeira rodada não havia dispositivo/emulador conectado (`adb devices` vazio). Testes manuais de navegação, layout, teclado, compra, restauração, persistência e limite FREE não executados. Android mínimo 24, Android recente, paisagem, fonte ampliada e abertura/fechamento do teclado exigem homologação. A posição de rolagem não é armazenada ao reconstruir telas; filtros e mês são mantidos em memória. Rotação/process death seguem o comportamento anterior e não oferecem recuperação de rascunho.

## Checklist manual pela Play — ainda pendente

- [ ] Conferir versionCode disponível e instalar por atualização Alpha sobre a versão publicada com dados sintéticos.
- [ ] Em cada tela/lista, comparar Voltar da UI e gesto/botão Android; histórico deve voltar para clientes, resumos para financeiro e detalhes para a origem.
- [ ] Confirmar saída padrão no menu e persistência dos dados após reiniciar.
- [ ] Verificar filtros e mês ao retornar; testar listas longas.
- [ ] Nova Venda em aparelho pequeno/grande, retrato/paisagem e fonte ampliada; conferir cliente, valor, parcelas, data, descrição e fotos.
- [ ] Abrir teclado e testar acesso a todos os campos e Salvar/Cancelar; primeiro Voltar com teclado deve fechá-lo conforme comportamento Android.
- [ ] Cancelar/Voltar com e sem alterações; continuar editando e descartar; testar galeria/câmera canceladas.
- [ ] Preço da licença deve coincidir com o Play na moeda/região da conta; sem rede/produto válido não permitir compra.
- [ ] Compra aprovada, pendente, cancelada, reconhecimento e restauração com conta de teste; verificar acesso de comprador anterior.
- [ ] FREE: 30 vendas permitidas, 31ª bloqueada; FULL_ACCESS libera nova venda sem modificar registros existentes.
- [ ] Reexecutar os 19 cenários de regressão anteriores, incluindo pagamentos, relatórios, fotos, CSV/PDF e backup/restauração.

Referências técnicas: [Voltar moderno](https://developer.android.com/guide/navigation/custom-back/predictive-back-gesture) e [integração Billing/ProductDetails](https://developer.android.com/google/play/billing/integrate).

## Resultado automatizado

`testDebugUnitTest assembleDebug bundleRelease`: BUILD SUCCESSFUL. Oito testes (6 MoneyRulesTest, 2 BackupFormatTest), zero falhas/erros. APK debug e AAB release gerados. A primeira tentativa offline falhou por dependência Billing ausente no cache; a execução com resolução de dependências foi concluída. Avisos de depreciação de Locale e ajuste do teclado e parâmetro não usado; sem erro de compilação.

## Refinamento adicional de UI/UX — Build 10

Solicitação adicional de 07/10/2026. Mantidos versionCode 10, versionName 1.0.0 e applicationId. Comparação incremental baseada na árvore local anterior, preservando as mudanças de Nova Venda, confirmação de descarte, Billing/preço dinâmico e ícones.

### Diagnóstico e implementação

- Detalhes: espaço vertical excessivo e dimensão do diálogo sem limite explícito. Agora cabeçalho com título fixo, status fixo na configuração normal e rolável quando fonte/altura exigirem; cliente no conteúdo rolável, conteúdo com ScrollView e peso 1, e rodapé separado com Registrar Pagamento, PDF do Card e Fechar. Datas e valores relacionados ficam lado a lado quando há espaço; margens reduzidas e valores financeiros destacados. Fotos continuam em faixa horizontal, com toque para ampliar. O diálogo anterior oferecia Registrar Pagamento mesmo sem saldo, mas o formulário já rejeitava qualquer valor nessa situação. O rodapé agora exibe essa ação somente quando há saldo positivo; usa o critério já existente, sem alterar pagamento ou cálculo. PDF e Fechar permanecem disponíveis.
- Lista: pesquisa deixa de ter um card externo com rótulo duplicado. Filtros Data e A Receber usam botões com ícones, área mínima de toque de 48 dp, cores e descrição acessível de estado ativo. Layout empilha os filtros com largura menor que 360 dp ou fonte acima de 130%. Aplicar, limpeza e combinação de filtros mantêm suas funções; nenhum SQL mudou.
- Cards: margens/padding menores, miniatura de 80 dp, agrupamento textual de parcela/compra e valor/pago. Status, vencimento, data de pagamento, valores, fotos e ações preservados. Texto pode quebrar linha; sem ellipsis ou limite de linhas.
- Cabeçalho: seta com área de 48 dp, título em coluna flexível e menu existente preservado. A seta usa `onBackPressedDispatcher`; `configurarRetorno` configura a mesma ação para ambos, e oculta a seta/desativa o callback no menu. Não há nova tabela de destinos ou reconstrução do cabeçalho a cada navegação. Diálogos e confirmações de descarte continuam nas janelas existentes.

Arquivos deste refinamento: `app/src/main/java/com/example/controlevendas/MainActivity.kt`, três vetores locais (`ic_back.xml`, `ic_calendar.xml`, `ic_money.xml`), `tools/validar_ui_build10.py` e este relatório. Os vetores foram criados no destino. Nenhuma mudança adicional em Gradle, Manifest, Billing, SQLite, migrations, cálculos, regras de licença, pagamentos, exportações, backup ou WhatsApp.

### Método de validação

Os resultados finais estão registrados abaixo. As evidências de UI de rodadas intermediárias não equivalem à homologação integral do APK final. O script de smoke tests exige AVD `Build10`, instalação debug e recusa aparelho real. A opção `--seed` exige base vazia e adiciona apenas dados sintéticos (26 compras/28 cards, estados aberto/parcial/quitado, três parcelas vinculadas e três fotos) ao schema criado pelo próprio app; não altera estrutura ou migração.

APK debug: `app/build/outputs/apk/debug/app-debug.apk`.
AAB release: `app/build/outputs/bundle/release/app-release.aab`.

### Checklist adicional de homologação

- [ ] Seta e Voltar físico/gestual equivalentes em todas as telas; seta ausente no menu.
- [ ] Voltar de histórico de cliente, resumos e detalhamento financeiro preserva o destino anterior.
- [ ] Menu de três pontos continua acessível, inclusive com fonte ampliada.
- [ ] Nova Venda com campos/fotos alterados: Cancelar/Voltar mantém confirmação de descarte; teclado aberto não esconde ações.
- [ ] Lista vazia/extensa, pesquisa, Aplicar, data, A Receber, filtros combinados e Limpar Filtros.
- [ ] Detalhes sem/com múltiplas fotos, parcelados, abertos, parciais e quitados; título e ações permanecem visíveis durante rolagem.
- [ ] Ampliar fotos, Registrar Pagamento, PDF do Card e Fechar mantêm seus fluxos anteriores.
- [ ] Tela pequena/grande, paisagem, fontes ampliadas e gestos sem cortes/sobreposição.
- [ ] Regressão de cadastro, parcelas, pagamentos, relatórios, CSV/PDF e persistência em atualização Alpha.

Referências de apresentação: [ScrollView](https://developer.android.com/reference/android/widget/ScrollView) e [áreas de toque acessíveis](https://support.google.com/accessibility/android/answer/7101858?hl=en).

Validação de navegação antes da remoção: oito checks de UI automatizados passaram no AVD isolado Android 15 (API 35), em 480 × 800 px / 240 dpi (320 dp de largura): seta oculta/presente, seta e Android retornando ao menu, retorno do financeiro, menu de três pontos, ativação/limpeza de A Receber. Após essa rodada, removidos os oito botões Voltar de rodapé. Reduzido também o padding inferior duplicado da área de listas. Fechar/Cancelar/Salvar dos diálogos preservados.

Ocorrências do ambiente: primeiro boot levou aproximadamente 11 minutos; houve ANR de Process system/System UI, encerramento da automação com código 137, foco nulo por bloqueio de tela e dumps sem raiz. O teste não reutiliza XML antigo: dumps têm nomes exclusivos e erros impedem aprovação. Após liberar memória do daemon Gradle e recuperar o foco, os oito checks acima passaram. A digitação via ADB foi incompleta na tentativa de pesquisa; esse cenário permanece pendente de verificação com teclado real/virtual estável. Não houve alteração do filtro para contornar a automação.

Inspeção visual dos detalhes em tela pequena: cabeçalho e três ações visíveis antes/depois da rolagem; três fotos sintéticas e ampliação preservadas. Treze checks de UI passaram na rodada com fixture (oito de navegação/filtros e cinco de detalhes/foto/abertura de pagamento). A abertura do formulário de pagamento foi testada e cancelada; isso não comprova gravação financeira, que permanece no checklist de regressão.

Ajustes finais de apresentação: valores financeiros movidos acima da descrição longa, espaço de 4 dp entre a ação de pagamento e a linha de PDF/Fechar, ícone de três pontos dimensionado em dp para não ser cortado por escala de fonte. Em altura inferior a 400 dp, o slogan decorativo do cabeçalho é ocultado para reservar área ao conteúdo; nome, seta e menu continuam presentes. Todos os campos/ações funcionais permanecem disponíveis.

Riscos/limitações: posição de rolagem, restauração de rascunho após rotação/process death e atualização periódica de telas continuam com os limites anteriores. Alterações no layout devem ser homologadas também em aparelhos reais, Android 24 e versão recente, fonte ampliada, paisagem e atualização via Play. Compra/restauração de licença, gravação de pagamentos, geração/compartilhamento PDF/CSV, filtros combinados e regressão completa não foram aprovados por esta rodada de smoke tests.

### Build final do refinamento

`testDebugUnitTest assembleDebug bundleRelease --offline`: BUILD SUCCESSFUL em 3m40s. Oito testes, zero falhas/erros; APK debug e AAB release assinado regenerados no estado final da interface. Metadata do APK confirma `com.vendassimples.app`, versionCode 10 e versionName 1.0.0.

- `app/build/outputs/apk/debug/app-debug.apk`: 7327520 bytes; SHA-256 `e6acb7da1505b864c792d9fca02cbfee2d283fbcb4193f6a337c0e514ede8758`.
- `app/build/outputs/bundle/release/app-release.aab`: 5307635 bytes; SHA-256 `0ac43af0fab778976430752e5a08e4192ea2552a619dec1b93b631bcb25c31b5`.

### git diff --stat final

Inclui mudanças anteriores; vetores, ferramenta e relatório ainda não rastreados não entram no stat padrão.

```text
 README.md                                          |   4 +-
 app/build.gradle                                   |   2 +-
 app/src/main/AndroidManifest.xml                   |   4 +-
 .../com/example/controlevendas/MainActivity.kt     | 223 +++++++++++++++------
 .../example/controlevendas/PlayBillingGateway.kt   |   6 +-
 app/src/main/res/drawable/ic_vendas_simples.xml    |  10 -
 docs/DECISOES.md                                   |   6 +-
 docs/MONETIZACAO.md                                |   4 +-
 docs/RELEASE_PLAY_STORE.md                         |   6 +-
 docs/VALIDACAO.md                                  |   4 +-
 10 files changed, 183 insertions(+), 86 deletions(-)
```

A rodada com fonte a 200% encontrou pouco espaço central, apesar das ações fixas. O layout final agrupa PDF/Fechar lado a lado, com quebra nativa dos textos e áreas de toque mantidas. Em fonte ampliada/tela baixa, status fica no conteúdo; título continua fixo e usa base 16 sp em largura inferior a 360 dp, escalada pelo Android (campos e valores não foram reduzidos). Registrar Pagamento só é apresentado com saldo positivo, refletindo o limite já aplicado pelo formulário existente. Sem mudança de regra financeira.


### Situação da homologação final

O APK final foi instalado por atualização no AVD Build10, preservando a fixture. A repetição com fonte a 200% não foi aprovada: o System UI apresentou ANR e a automação encontrou janela do sistema/árvore nula. A rodada anterior com 13 checks aprovados está preservada em `.gradle-user/ui-refinement/small-final/`; ocorreu antes dos ajustes finais de título/status, rodapé em duas linhas e ocultação de pagamento sem saldo. Esses ajustes finais compilaram, mas exigem nova homologação visual e funcional em ambiente estável.

Comparação estática com a árvore anterior ao refinamento confirmou identidade das funções `vendasFiltradas`, `abrirDialogVenda`, `abrirDialogPagamento`, `abrirDialogCorrigirPagamento`, `gerarPdfCard`, `exportarCsvResumo`, `abrirNovaVendaOuDesbloqueio` e `cobrarViaWhatsApp`. Isso comprova preservação dos seus códigos; não substitui teste funcional. `git diff --check` passou. A ferramenta de UI usa apenas fixture sintética e exige AVD Build10/debug; `--seed` recusa banco não vazio. Não executar contra dados reais.

Nenhum commit, push, upload, deploy ou publicação foi realizado. Mantidos `versionCode = 10`, `versionName = 1.0.0` e `applicationId = com.vendassimples.app`; disponibilidade do código 10 no Console não foi verificada.
