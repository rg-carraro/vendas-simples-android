# Fase 16 — implementação de Light Mode e Dark Mode

**Data:** 23/10/2026
**Branch/worktree:** `feature/fase16-light-dark-mode`, `.worktrees/fase16-light-dark-mode`
**Commit base:** `dfb87e6ddb294834bde8f6ae044b4a1d58ed7525` (confirmado por `git ls-remote` como master remota atual em 23/10/2026)
**Versão do código-base:** `versionCode 14`, `versionName 1.0.14`, `applicationId com.vendassimples.app`. O proprietário relata que a V14 está na Play Store; Console não consultada.
**Estado:** alterações locais não commitadas, em branch isolada. Sem PR, merge, push, AAB ou publicação.

## Requisitos e decisões

- Requisito do proprietário: implementar temas claro e escuro completos, detectar a preferência de tema do Android automaticamente, manter mudanças de UI restritas à apresentação e preservar a identidade e os fluxos do app.
- Preferência automática: `Theme.MaterialComponents.DayNight.NoActionBar` seleciona recursos `values` ou `values-night` conforme o sistema. A Activity não fixa um modo próprio. Mudanças de `uiMode` recriam a Activity pelo comportamento padrão do manifesto.
- A V14 distribuída é preservada: esta implementação está isolada em worktree/branch baseada no último `master` do Git; `versionCode` e `versionName` não foram alterados.
- A Fase 15A foi copiada para esta branch para preservar a evidência anterior e manter o link do relatório. Ela continua descrevendo o estado auditado, não uma auditoria visual desta implementação.

## Alterações

- `app/src/main/res/values/colors.xml` agora define tokens semânticos usados pela interface: fundo/superfície, texto primário/secundário/desativado, ações, bordas, erro e estados de venda.
- `app/src/main/res/values-night/colors.xml` fornece a paleta noturna correspondente. A paleta foi escolhida para atingir pelo menos 4,5:1 em texto e 3:1 em contornos de componentes nos pares definidos; os testes acrescentados verificam esses pares em runtime.
- `app/src/main/res/color/` contém seletores para texto e fundo de botões habilitados/desabilitados, texto de ações dos diálogos, texto de campos e contorno de campos. No estado desabilitado, o fundo muda para a superfície variante e o texto mantém contraste AA.
- `values/styles.xml` configura o tema Material DayNight e o overlay dos diálogos; `values-v27/styles.xml` acompanha a luminosidade dos ícones de status e navegação ao tema.
- `MainActivity.kt` resolve as cores dinâmicas por recursos, atualiza estados de vendas, campos e controles e usa `MaterialAlertDialogBuilder` para diálogos, respeitando o overlay Material claro/escuro.
- Rótulos da opção de licença e placeholder do nome do negócio foram simplificados para evitar truncamento observado com escala de fonte ampliada; os listeners e ações existentes foram preservados.
- Ícones de voltar, calendário e dinheiro passaram a usar o token primário.
- A paleta usada na geração dos PDFs foi isolada como valores fixos históricos para preservar o conteúdo visual dos documentos, conforme a restrição da fase.
- `FinalUiInstrumentedTests.kt` ganhou verificações de contraste semântico nos dois conjuntos de recursos, estados de campos e cores/tipografia de diálogos; o teste existente de estados de venda agora compara com tokens semânticos.
- O tema Material dos diálogos define explicitamente o texto de corpo com o token secundário opaco; o estilo Material padrão aplicava 60% de opacidade e o contraste composto medido ficava em 4,08:1.
- O build `debug` usa o sufixo de pacote `.fase16`, mantendo instalação e runner instrumentado separados do `applicationId` da Play. O sufixo não se aplica ao tipo `release`.
- Não houve alteração em schema, formato de backup, Billing, regras financeiras, exportação PDF ou versão. A Fase 16C acrescentou apenas a remoção do arquivo temporário `.corrupt` e o rótulo exclusivo do APK debug.

## Telas e componentes abrangidos pelo código

A Activity monta telas programaticamente. A revisão de cobertura identificou estes conjuntos: menu principal e cabeçalho; lista/cards de vendas, filtros e estado de pagamento; detalhes e opções do card; criação/edição de venda, campos e fotos; registro de pagamento; resumo de clientes; dashboard financeiro e seleção de mês; tela Sobre, contato e política; alertas de confirmação/erro, date picker, popups e menus; barras de status/navegação. Os diálogos usam o overlay DayNight quando construídos pelo Material. `DatePickerDialog` permanece do sistema e herda o tema da Activity.

PDF e seu layout interno foram mantidos com cores fixas deliberadas. Ícone de launcher também mantém a identidade fixa. O menu popup foi inspecionado no dispositivo, incluindo fonte ampliada; a opção de licença foi encurtada para caber nessa escala.

## Contraste calculado para os tokens

Razões calculadas pela fórmula WCAG de luminância relativa sobre os valores hexadecimais da paleta:

| Par | Claro | Escuro | Referência |
|---|---:|---:|---:|
| Texto principal / superfície | ≥ 14:1 | ≥ 13:1 | 4,5:1 |
| Texto secundário / superfície | ≥ 7:1 | ≥ 9:1 | 4,5:1 |
| Texto primário / fundo | ≥ 12:1 | ≥ 15:1 | 4,5:1 |
| Texto sobre primária / botão primário | 5,17:1 | 7,36:1 | 4,5:1 |
| Azul primário / fundo da tela | 4,82:1 | 7,36:1 | 4,5:1 |
| Texto de botão desabilitado / fundo desabilitado | 6,15:1 | 6,97:1 | 4,5:1 |
| Texto em botão secundário / superfície | 7,58:1 | 9,85:1 | 4,5:1 |
| Contorno de campo / superfície | ≥ 4,6:1 | ≥ 5,7:1 | 3:1 |
| Pares de texto/fundo dos estados de venda | ≥ 5,5:1 | ≥ 5,2:1 | 4,5:1 |

Razões são propriedades da combinação de tokens; não incluem elevação, transparência composta, imagem de fundo, antialiasing, tamanho real do texto ou estados que não estejam entre os pares explicitamente verificados. A avaliação WCAG completa ainda depende de inspeção visual e de todos os componentes em execução.

## Validação

- **PASSOU:** revisão estática de `versionCode`/`versionName`; permanecem 14/1.0.14.
- **PASSOU:** inspeção das referências de cores da Activity, dos três vetores ajustados e dos recursos diurnos/noturnos; `Color.rgb` em produção fica restrito à paleta preservada do PDF.
- **PASSOU:** verificação manual das razões acima para pares representativos de texto, botão, campo e estados de venda.
- **PASSOU:** `:app:testDebugUnitTest` — 46/46; `:app:testReleaseUnitTest` — 46/46.
- **PASSOU:** `:app:compileDebugAndroidTestKotlin` e `:app:assembleDebugAndroidTest` — testes instrumentados compilados e empacotados. O manifesto androidTest aponta `targetPackage=com.vendassimples.app.fase16`; os cenários rodam no contexto de teste isolado e usam somente dados sintéticos.
- **PASSOU:** `:app:lintDebug` e `:app:lintRelease`, zero erros; 63 avisos debug e 65 release na validação Fase 16C.
- **PASSOU:** `:app:assembleDebug` e `:app:assembleDebugAndroidTest` — APKs de debug construídos localmente. O app debug recebe o sufixo fixo `.fase16`, rótulo “Vendas Simples — Teste Light/Dark” e foi instalado em paralelo; o pacote oficial release permanece `com.vendassimples.app`.
- **PASSOU:** `git diff --check` e parse XML de todos os recursos em `app/src/main/res`.
- **Diagnóstico do bloqueio anterior:** a primeira tentativa apontou plugin Kotlin 2.2.20 ausente porque usava `.gradle-user` isolado, que só continha Kotlin 1.9.24. O plugin e as dependências 2.2.20 estavam no cache global `C:\Users\rgcar\.gradle`; repetir offline com esse `GRADLE_USER_HOME` resolveu a configuração e permitiu compilar/testar. Nenhuma versão ou dependência foi alterada.
- **PASSOU — dispositivo real:** Samsung Galaxy S22 SM-S906E, Android 16/API 36, 1080×2340, densidade 450 dpi. V14 original `com.vendassimples.app`, versionCode 14/versionName 1.0.14, lastUpdateTime `2026-10-21 12:53:45`; pacote oficial não foi alvo de instalação ou testes.
- **PASSOU — instrumentação de interface:** 19 métodos de `FinalUiInstrumentedTests` aprovados nas quatro combinações claro/escuro e fonte 1,0/1,3: 76/76 execuções. Testam tokens/contraste, diálogos, estados e filtros de venda, totais/gráfico, preferências, fotos e PDF sintéticos; não substituem o percurso visual integral dos formulários comerciais.
- **PASSOU — correção do resíduo:** a base V14 e o código pré-Fase16 tinham o mesmo cleanup sem tratar o nome `.corrupt` criado pelo SQLite ao rejeitar um arquivo temporário. `removeTemporary` agora remove também esse sibling. O caso específico passou e a suíte completa passou 38/38, incluindo rejeições, interrupções e preservação da base sintética.
- **PASSOU — pacote paralelo para avaliação:** `com.vendassimples.app.fase16`, versionCode 14/versionName 1.0.14 e rótulo “Vendas Simples — Teste Light/Dark” foi instalado ao lado da V14 e permanece instalado. O pacote temporário do runner `.fase16.test` foi removido após os testes.
- **PASSOU — checks locais Fase 16C:** `:app:testDebugUnitTest` e `:app:testReleaseUnitTest` — 46/46 cada; compilação instrumentada; lint debug/release sem erros (63/65 avisos); `:app:assembleDebug` e `:app:assembleDebugAndroidTest` offline. Nenhum AAB foi gerado.
- **PASSOU — settings restauradas:** ADB confirmou modo claro (`Night mode: no`) e fonte 1,0 depois da matriz de testes.
- **PASSOU — inspeção visual:** tela inicial e diálogo “Nome do negócio” em modo claro; tela inicial, menu principal e diálogo “Nome do negócio” em modo escuro; tema automático acompanhou a configuração do Android. Fonte foi ampliada temporariamente de 1,0 para 1,3, e restaurada para 1,0. Tema foi restaurado para claro. Os valores finais foram consultados por ADB.
- **PASSOU — correção visual confirmada:** a primeira inspeção com fonte 1,3 encontrou truncamento da opção de licença no menu e do placeholder. Os textos foram reduzidos sem mudança de ações; nova inspeção no S22 confirmou ambos inteiros. O título, texto explicativo, campo e ações Cancelar/Salvar do diálogo também permaneceram visíveis no escuro com fonte ampliada.
- **NÃO EXECUTADO:** percurso visual de criação/edição de cliente e venda, diálogo Detalhes da Venda e suas ações, formulários Registrar Pagamento, PDF do Card, interface SAF de backup/restauração, licença/Billing real, orientação paisagem e outros tamanhos. Intents de compartilhamento não foram enviados. As rotinas instrumentadas de dados/PDF/fotos são sintéticas e não cobrem todos esses percursos.
- Casos automatizados de backup, cliente, venda, pagamento, fotos e PDF usaram fixtures sintéticas no namespace do runner `.test`; nenhum banco da V14 foi aberto ou alterado. Nenhuma compra real foi executada.

## Evidências e limites

- Os recursos `values` e `values-night`, estilos, seletores, código da Activity e teste Android instrumentado são evidências locais rastreáveis nesta worktree.
- O relatório [Fase 15A — auditoria de contraste](FASE15A_AUDITORIA_CONTRASTE.md) registra os achados prévios; não é resultado de homologação desta mudança.
- As capturas temporárias das telas do pacote de teste foram inspecionadas localmente e removidas; não foram adicionadas ao repositório. Uma captura intermediária chegou a mostrar uma chamada recebida durante a inspeção; ela não foi atendida e o arquivo foi descartado.
- Não foi consultada a Play Console. Distribuição da V14 é relato do proprietário.

## Problemas confirmados e hipóteses

- **Confirmado no código:** a V14 usava tema DayNight nominal, mas faltava paleta noturna semântica para a interface criada programaticamente; cores fixas na Activity impediam acompanhamento integral do modo noturno. Fonte: inspeção da base `dfb87e6` e relatório da Fase 15A.
- **Confirmado nesta implementação:** chamadas de diálogo Material agora recebem tema DayNight e as principais cores de tela/estados usam tokens qualificados.
- **Hipótese a validar:** componentes de biblioteca, dropdowns, ripple, date picker e popup podem ter atributos internos ou cores nativas que não são totalmente cobertos pelo overlay. Build e AVD são necessários para detectar incompatibilidade de estilo e diferenças de renderização entre versões Android.
- **Confirmado e corrigido no aparelho:** fonte 1,3 truncava a opção de licença e o placeholder; versão revisada foi conferida no S22 e ambos cabem sem truncamento.
- **Hipótese a validar:** contraste visual aparente pode variar com sobreposição do sistema, fabricante, brilho, transparência e escala de fonte mesmo quando os tokens calculados satisfazem as razões definidas.

## Riscos de regressão

- O proprietário informou que concluiu os testes manuais no Galaxy S22 e que todos os cenários executados passaram. O relato não enumerou resultados por tela; os testes automatizados cobrem componentes/invariantes sintéticos e não foram apresentados como navegação manual observada pelo Codex.
- A correção elimina o resíduo temporário reproduzido; os testes automatizados confirmam o resultado. Não houve alteração do formato ou dos dados dos backups.
- O teste sintético de PDF passou. A execução de compartilhamento/WhatsApp não foi realizada pelo Codex; não se registrou envio externo.

## Recomendações de correção mínima e aceite de eventual V15

1. Homologação manual no S22: **aprovada conforme relato do proprietário** para os cenários executados; inventário por tela não foi fornecido.
2. Aceite técnico: pares de texto normal ≥4,5:1, texto grande ≥3:1 e limites/indicadores essenciais ≥3:1; unit tests, AndroidTest, lint e `assembleDebug` sem erro; nenhum dado comercial, Billing, formato de backup ou PDF alterado funcionalmente.
3. A integração Git da Fase 16 foi autorizada; decidir versionamento e preparação de release separadamente. Não fazer AAB ou publicação nesta fase.

## Estado Git e continuidade

Alterações permanecem na branch `feature/fase16-light-dark-mode`, baseada na `master` remota V14. A integração Fase 16D está autorizada; registrar PR e SHA do merge neste histórico após a conclusão. Não houve geração de AAB ou publicação. A V14 e sua instalação foram preservadas.
