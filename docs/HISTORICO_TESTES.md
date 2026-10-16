# Histórico de testes e qualidade

Este documento registra resultados documentados no momento de cada fase. Não transforma teste antigo em aprovação do código atual, nem compilação AndroidTest em execução instrumentada.

| Marco | Cobertura/resultado documentado | Limite |
|---|---|---|
| 29/09 — `4e08155` | Primeira suíte `MoneyRulesTest`; a documentação a associa a validação pré-Billing. | A contagem exata de teste executado nesta primeira ocorrência não está reunida neste arquivo. |
| 02/10 — `d8e6fcf` | `BackupFormatTest` adicionado para formato/compatibilidade. | Não prova recuperação física em todos dispositivos. |
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

Use os registros `PASSOU`, `FALHOU`, `NÃO EXECUTADO` com commit/versão, ambiente, aparelho/API e evidência. A lista de regressão reutilizável está em [VALIDACAO](VALIDACAO.md) e [regressao-release skill](../.agents/skills/regressao-release/SKILL.md).
