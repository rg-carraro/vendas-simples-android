# Histórico de testes e qualidade

Este documento registra resultados documentados no momento de cada fase. Não transforma teste antigo em aprovação do código atual, nem compilação AndroidTest em execução instrumentada.

| Marco | Cobertura/resultado documentado | Limite |
|---|---|---|
| 29/09 — `4e08155` | Primeira suíte `MoneyRulesTest`; a documentação a associa a validação pré-Billing. | A contagem exata de teste executado nesta primeira ocorrência não está reunida neste arquivo. |
| 02/10 — `d8e6fcf` | `BackupFormatTest` adicionado para formato/compatibilidade. | Não prova recuperação física em todos dispositivos. |
| V10 beta — `99f41e7` | O relatório registra 8 testes unitários (6 `MoneyRulesTest`, 2 `BackupFormatTest`), `testDebugUnitTest assembleDebug bundleRelease` aprovados, e 8 checks de navegação/filtros mais 13 checks de UI em AVD Android 15/API 35 em rodadas documentadas. | A pesquisa com teclado teve digitação incompleta; gravação financeira e regressão completa não foram validadas. Rodada final com fonte a 200% encontrou ANR/árvore nula e não foi aprovada; os checks anteriores não aprovam o APK final. Ver [ATUALIZACAO_ALPHA_UI_BILLING](ATUALIZACAO_ALPHA_UI_BILLING.md). |
| V11 — `eae7711`–`8aa3488` | Testes de identidade, compatibilidade, backup/restore, CSV, gráfico percentual e instrumentação de UI. O histórico de release registra progressão de 16→27→35 testes por variante e casos nativos; no tag RC: 35 por variante, 44 execuções nativas de layout, 10 checks de UI e 12 testes de backup conforme mensagem da tag/documentação. | Resultados e número variam por etapa/escopo; consultar [RELEASE_V11](RELEASE_V11.md). Não comprova Play/produção. |
| V11 em aparelho — Fase 6B.1 | Relatório registra 13/13 verificações manuais aprovadas pelo usuário em Samsung Galaxy S22+ / Android 16. | Evidência relatada pelo usuário, não executada/observada diretamente pelo Codex; é somente V11 e não aprova V12. |
| Fase 2–4 | Suíte de backup endurecida, testes de transferência/validação; política, lint e instrumentação local. Fase 4 registra `BUILD SUCCESSFUL`, zero erros de lint e 66 avisos por variante no código V11. | Relatórios registram erros transitórios e execuções separadas. Não homogenizar contagens de diferentes commits. |
| Fase 6B | 43/43 unit tests debug e release, build debug e compilação AndroidTest; 0 erros lint/66 avisos por variante. | ADB/device indisponível naquele ambiente; AndroidTest não foi executado em runtime. |
| Fase 7H — V12 integrada | 43/43 debug e 43/43 release; `lintDebug`/`lintRelease` 0 erros e 67 avisos por variante; `compileDebugAndroidTestKotlin`, `assembleDebug` e diff-check passaram. | AndroidTest compilado, não executado. Sem teste físico, API 24 ou Play Billing real. Consulte [relatório 7H](FASE7H_INTEGRACAO_REGRESSAO_V12.md). |
| Fase 7I — AAB V12 | `bundletool validate`, leitura de manifesto, `jarsigner`, certificados, hash e inspeção direcionada de conteúdo passaram conforme registro. | AAB não comprova upload ou aceitação pela Console. A varredura de conteúdo é heurística. Consulte [relatório 7I](FASE7I_AAB_V12.md). |

## Evolução da cobertura

Testes foram ampliados de regras monetárias para formato de backup, transação de restauração, identidade, compatibilidade, CSV (parser estrito, valores, caracteres e colunas), percentuais, política e UI/backup instrumentados. O histórico Git aponta adições/modificações nos commits `4e08155`, `d8e6fcf`, `eae7711`, `bc70079`, `2b88ccd`, `8fed0b2`, `0a85fe0`, `8aa3488`, `b2e65a0` e `5467c40`.

## Gaps de homologação V12

- [ ] Executar instrumentação em aparelho/emulador; compilar não conta como executar.
- [ ] Validar runtime na API mínima 24 e Android 16/API 36.
- [ ] Atualizar V11→V12 pela Google Play Alpha com dados controlados.
- [ ] Executar teste real de Billing (aprovada, cancelada/recusada, pendente, reconhecimento, restauração) em conta de licença de teste.
- [ ] Confirmar política pública e declarações atuais da Console.
- [ ] Validar exportações/compartilhamento e backup/restauração em instalação descartável com dados sintéticos.

## V13 — estado posterior ao relatório da Fase 13

O relatório da Fase 13 registra 46 testes unitários aprovados, compilação de AndroidTest, build debug e lint sem erros/67 avisos. O proprietário relatou validação visual/fluxos no S22+ e restore V12→V13 Debug. O AAB local release foi validado depois com SHA-256 `33D0906198E439304887A59AB683AF723636DEEE6ACAF51025BD073C42ED6642`. Esses registros não comprovam runtime instrumentado, atualização direta pela Play, execução em API24 ou compra/restauração Play real. Status Alpha envio 6 “Em análise” em 19/10/2026 é relato do proprietário, não teste observado. Consulte [Pendências atuais](PENDENCIAS_ATUAIS.md).

## V14 — validação funcional local (19/10/2026)

Na branch `feature/v14-pdf-identidade-privacidade`, 46/46 testes unitários passaram em debug e 46/46 em release. `compileDebugAndroidTestKotlin`, `lintDebug`, `lintRelease` e `assembleDebug` passaram; lint reportou 0 erros e 67 avisos por variante. ADB não iniciou porque não pôde criar `\.android` no sandbox, e não havia AVD listado; AndroidTest runtime, inspeção visual do PDF, aparelho real e API24 não foram executados. Consulte [relatório funcional da Fase 14](FASE14_FUNCIONAL_PDF_PRIVACIDADE.md).

O proprietário posteriormente informou ter homologado manualmente as mudanças funcionais V14 e confirmado funcionamento correto. Esta evidência é **RELATADA PELO PROPRIETÁRIO**; não substitui o AndroidTest runtime nem foi observada pelo Codex.

No fechamento do release V14 (19/10/2026), `testDebugUnitTest`, `testReleaseUnitTest`, `compileDebugAndroidTestKotlin`, `lintDebug`, `lintRelease` e `bundleRelease` passaram juntos. Os testes unitários e a compilação AndroidTest estavam atualizados; lint teve 0 erros e 67 avisos por variante. Bundletool validou o AAB e seu manifesto, e `jarsigner -verify` confirmou a assinatura. AndroidTest runtime, API24, atualização via Play e Billing real continuam pendentes. Hash e metadados constam em [Fase 14](FASE14_FUNCIONAL_PDF_PRIVACIDADE.md).

## Fase 16 — Light Mode e Dark Mode (23/10/2026)

Na worktree isolada `feature/fase16-light-dark-mode`, baseada no código V14 `dfb87e6`, `:app:testDebugUnitTest` e `:app:testReleaseUnitTest` passaram com 46 testes por variante. Na Fase 16C, instrumentação executada no Galaxy S22 SM-S906E / Android 16/API 36 passou 38/38 testes de backup e 19 testes de UI em cada uma das quatro combinações claro/escuro e fonte 1,0/1,3 (76/76 execuções). A causa do `.db.corrupt` foi corrigida no cleanup do arquivo temporário; backup format/schema não mudaram. `compileDebugAndroidTestKotlin`, lint debug/release (0 erros, 63 avisos debug e 65 release) e `assembleDebug` passaram. APK paralelo code14/name1.0.14, pacote `.fase16`, entregue em `dist/debug`; o runner `.fase16.test` foi removido após os testes. O proprietário informa que concluiu os testes manuais no S22 e que todos os cenários executados passaram (aprovação relatada, sem inventário individual). Veja [relatório Fase 16](FASE16_LIGHT_DARK_MODE.md) e [homologação final 16C](FASE16C_HOMOLOGACAO_FINAL.md).

Use os registros `PASSOU`, `FALHOU`, `NÃO EXECUTADO` com commit/versão, ambiente, aparelho/API e evidência. A lista de regressão reutilizável está em [VALIDACAO](VALIDACAO.md) e [regressao-release skill](../.agents/skills/regressao-release/SKILL.md).
