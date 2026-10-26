# Fase 16F — sincronização da master e consolidação documental

**Data:** 23/10/2026

**Worktree:** `.worktrees/master-post-merge`

**Branch sincronizada:** `master`

**SHA anterior:** `11214de6aac8bf1072c1767344c725939d3b1802`

**SHA local/remoto após fast-forward:** `a742c9139950c6098b4a4960c181e97a45b48fb7`

## Integração da Fase 16

A `origin/master` contém a PR [#7](https://github.com/rg-carraro/vendas-simples-android/pull/7), mergeada em 23/10/2026. O merge tem como primeiro pai a base V14 `dfb87e6ddb294834bde8f6ae044b4a1d58ed7525` e como segundo pai o tip da feature `7db6fb3784bb8544cfbfa5f840d53fb40b558eb4`. Os três commits da fase estão presentes: `d00acef` (Light/Dark Mode), `a813542` (limpeza dos temporários SQLite) e `7db6fb3` (documentação/homologação).

O fast-forward local foi feito somente depois da cópia verificada dos documentos não rastreados que ocupavam caminhos criados pela master remota. O SHA local ficou igual a `origin/master`; nenhum merge commit novo foi criado.

## Documentos preservados e comparação

As versões locais estavam como arquivos não rastreados na worktree `master-post-merge`. Todas foram copiadas integralmente, com timestamps de criação/modificação preservados, e conferidas por SHA-256. Os originais foram movidos para a pasta de preservação antes do fast-forward; portanto, nenhum conteúdo foi descartado.

Manifesto de preservação: `%TEMP%\vendas-simples-fase16f-preservacao-20261023\manifest.json`

Cópias espelhadas: `%TEMP%\vendas-simples-fase16f-preservacao-20261023\docs\`

Originais movidos: `%TEMP%\vendas-simples-fase16f-preservacao-20261023\originais-removidos-da-colisao\docs\`

SHA-256 do manifesto final (inclui os caminhos dos originais movidos): `FC5BF70AEFE808EB0C9FF45E30E5B1D85DDE22BD742D5A233F5B0964EF530EC9`.

| Documento | SHA-256 da cópia local | Relação com `origin/master` | Diferença documental observada |
|---|---|---|---|
| `FASE9A_POS_MERGE_PREPARACAO_ALPHA.md` | `61BDF2D47A41486CA61E3EA567F34745E256BBC4ADF24391C4A90C63AD279511` | Idêntico | Nenhuma diferença de conteúdo. |
| `FASE9B_CONFORMIDADE_ALPHA_V12.md` | `013C055FC59A9954D3CEF008EBDA58E72379D5D33302EB2A68A5FB0288E29AD7` | Diferente | A cópia local contém o caminho absoluto do AAB; a versão remota substitui esse caminho por uma indicação sanitizada. Métricas e hash do artefato são preservados na versão remota. Diferenças adicionais são somente espaços de quebra Markdown. |
| `FASE9C_AUDITORIA_DATA_SAFETY_BILLING.md` | `2946E0097D7D8D58D858EF58CF1E2E0571FBA243C58C254E18D5FE89AF3674E1` | Diferente | A comparação encontrou apenas espaços finais usados como quebras Markdown no cabeçalho; sem diferença substantiva identificada. |
| `FASE9D_RESOLUCAO_DATA_SAFETY.md` | `243B2560056BB7472F48A6282C1D1201DDF2D6822B6709264F004A91034FF099` | Idêntico | Nenhuma diferença de conteúdo. |
| `FASE9E_RESPOSTA_GOOGLE_DATA_SAFETY.md` | `5A5712AEF258326F3B88B0DA10BA51704EB82663FBBD1DA48B637D29C9694F88` | Idêntico | Nenhuma diferença de conteúdo. |

Os arquivos rastreados após o fast-forward são os da versão remota. A cópia local de 9B, incluindo o caminho original, continua guardada fora do repositório para referência; não foi incorporado o caminho absoluto do AAB ao histórico rastreado.

## Consolidação de homologação e versões

- A Fase 15A continua sendo o diagnóstico estático anterior. Seu adendo aponta os relatórios posteriores, sem reescrever as observações históricas como se fossem estado atual.
- Os relatórios 16, 16B e 16C registram paletas DayNight, correção de contraste, correção do arquivo temporário `.corrupt` e resultados automatizados. A suíte no S22 passou 38/38 testes de backup e 76/76 execuções de UI nas quatro combinações de tema e fonte; unit tests passaram 46 por variante, lint sem erros e `assembleDebug` concluído.
- O proprietário informou que todos os cenários manuais executados no Galaxy S22 passaram. Essa aprovação permanece identificada como relato do proprietário; o inventário por fluxo não foi fornecido.
- A master declara `versionCode 14` / `versionName 1.0.14`. O proprietário informa que a V14 permanece em teste fechado; a Play Console não foi consultada nesta consolidação.
- A implementação Light/Dark e a limpeza de temporários de backup estão integradas. Não houve alteração de Billing, regras comerciais, schema/formato de backup ou funcionalidades comerciais nesta Fase 16.
- Preparar V15 (`15 / 1.0.15`) é pendência que exige autorização específica. Não houve build de release, AAB, publicação ou ação na Play.

## Alterações locais e limites

A sincronização alterou somente o HEAD e os arquivos rastreados da worktree `master-post-merge` por fast-forward. Foram atualizados localmente `README.md`, os relatórios 15A/16/16B/16C, `HISTORICO_VERSOES.md`, `HISTORICO_TESTES.md`, `PENDENCIAS_ATUAIS.md` e `INDEX.md`; este relatório documenta a consolidação. As mudanças documentais permanecem sem commit e sem push, prontas para revisão.

As outras worktrees e branches foram preservadas. A worktree principal continua na branch `fix/v12-seguranca-conformidade` com seus arquivos preexistentes; nenhum código de app, versão, APK/AAB ou estado da Google Play foi alterado nesta tarefa.
