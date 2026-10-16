# Fase 7H — Integração isolada e regressão V12

**Data:** 16/10/2026

**Branch isolada:** `integration/v12-master-validation`

**Worktree:** `.worktrees/integration-v12-master-validation` dentro do repositório

**Versão:** `com.vendassimples.app`, versionCode 12, versionName 1.0.12

**Classificação:** **INTEGRAÇÃO CONCLUÍDA COM RESSALVAS**.

## Estado inicial e isolamento

A branch original era `fix/v12-seguranca-conformidade` no commit `628b9f43ffa5bd0a7e478935729e0e45b3473228`. Havia zero arquivos rastreados modificados/staged; relatórios locais, originais privados e os diretórios de worktrees auxiliares apareciam como não rastreados e foram preservados. `origin/master` apontava para `fb59cf96392b80c63a42974c80c1b91be1657e4c`. A tag `v1.0.11-rc.1` apontava para `8aa34881755f222f9b090691aaa6127b31b1a363`. Os dois worktrees preexistentes estavam limpos.

O novo worktree foi criado em caminho livre, na branch `integration/v12-master-validation`, a partir do commit inicial `628b9f43ffa5bd0a7e478935729e0e45b3473228`. Nenhum arquivo rastreado da branch original foi alterado.

## Merge e revisão

Merge local executado no worktree isolado: `git merge --no-ff origin/master`.

- Resultado: merge concluído sem conflitos Git; nenhum arquivo ficou unmerged.
- Merge commit: `64344855e377dadf5f6b3044cadde6b41e728459`.
- Pais: V12 `628b9f43ffa5bd0a7e478935729e0e45b3473228` e `origin/master` `fb59cf96392b80c63a42974c80c1b91be1657e4c`.
- Os dois commits exclusivos do master alteravam a página `site/privacy/index.html`. A árvore integrada conserva a versão local mais recente de 15/10; o asset offline continua textualmente igual ao conteúdo de `<main>` dessa página. Não foi necessário escolher integralmente um lado nem editar código para resolver conflito.
- `git diff --check HEAD^1 HEAD` passou. `origin/master` agora é ancestral do HEAD integrado; a divergência final é 17 commits à frente / 0 atrás.

## Regressão automatizada

Ambiente: JBR 21 do Android Studio, Gradle Wrapper 8.11.1, cache Gradle existente, modo offline. Variáveis `JAVA_HOME`, `PATH` e `GRADLE_USER_HOME` foram definidas somente para o processo do comando. Execução com `--no-daemon --max-workers=1 --console=plain --rerun-tasks`:

```text
:app:testDebugUnitTest
:app:testReleaseUnitTest
:app:lintDebug
:app:lintRelease
:app:compileDebugAndroidTestKotlin
:app:assembleDebug
```

Resultado final: **BUILD SUCCESSFUL**, 84 tarefas executadas em 5m36s.

| Verificação | Resultado |
|---|---|
| Testes unitários debug | 43; 0 falhas, 0 erros |
| Testes unitários release | 43; 0 falhas, 0 erros |
| Lint debug | 0 erros, 67 avisos |
| Lint release | 0 erros, 67 avisos |
| `compileDebugAndroidTestKotlin` | Passou; compilação somente, sem execução em dispositivo |
| `assembleDebug` | Passou |
| `git diff --check` do merge | Passou |

Contagens de testes e lint correspondem à linha de base informada na Fase 7F (43 testes e 67 avisos em cada variante). Os avisos Kotlin observados incluem construtor `Locale(String,String)` e `SOFT_INPUT_ADJUST_RESIZE` obsoletos. Não foi identificada falha de teste ou erro de lint. Não foram executados testes instrumentados em aparelho/emulador.

## APK debug

Gerado apenas no worktree isolado:

- Caminho: `app/build/outputs/apk/debug/app-debug.apk` dentro do worktree acima.
- Tamanho: 7.318.615 bytes.
- SHA-256: `615928EDFDBB8E9CE17CE92FB6F8F11E2750FDB25713C9FD737CC9D123A2CE43`.
- `aapt dump badging`: applicationId `com.vendassimples.app`, versionCode `12`, versionName `1.0.12`, minSdk 24, targetSdk 36.

Não foi gerado AAB nem build release instalável.

## Verificações estáticas de produto

O código integrado contém as tabelas e fluxos locais para clientes, vendas, pagamentos e fotos; limite FREE `30`; compra única `full_access`/`one_time` com Billing Library `9.1.0`; identidade do negócio em preferências; exportação CSV, PDFs e compartilhamento WhatsApp. Backup e restauração passam pelo `BackupManager`, validam o arquivo e verificam entitlement `FULL_ACCESS`; o backup não importa licença. A política offline permanece e corresponde ao HTML local de 15/10.

Essas são inspeções estáticas e testes automatizados; não representam homologação manual, compra Play real, atualização V11→V12 ou verificação da página pública/Data Safety.

## Preservação e estado final

- Branch original continua em `628b9f43ffa5bd0a7e478935729e0e45b3473228`, sem alterações rastreadas/staged.
- Tag V11 permanece em `8aa34881755f222f9b090691aaa6127b31b1a363`.
- Os dois AABs V11 permanecem com 5.320.483 bytes cada e SHA-256 `D751F51DC69AC2DA2784D841374288E4456BD666E2A5237C2E91C1EE9D47AABA`.
- Os dois worktrees auxiliares e `.private-originals-fase7g2/` foram preservados.
- Worktree temporário permanece em `integration/v12-master-validation`, no merge commit `64344855e377dadf5f6b3044cadde6b41e728459`. Está limpo antes da criação deste relatório; este arquivo é não rastreado e não foi commitado.
- Não houve push, rebase, reset, clean, tag, alteração de keystore, AAB, upload ou publicação.

## Pendências antes de AAB/Alpha

1. Homologar em aparelho os fluxos de atualização V11→V12, vendas, fotos, preferências, FREE/FULL_ACCESS, backup/restauração e compra/retorno de licença.
2. Completar a validação externa de Billing/Data Safety, declaração Play Console e página pública da política. A Fase 7G.3 documenta que a ferramenta web não confirmou o conteúdo publicado.
3. Antes de gerar AAB, revisar a configuração de assinatura/chave e verificar versão, assinatura e hash do artefato; a regressão desta fase não produziu nem valida AAB.
4. Revisar o commit de merge e este worktree antes de qualquer integração de volta. Push, upload Alpha e publicação continuam fora desta autorização.

**Conclusão:** integração local isolada concluída, sem conflitos e com a regressão automatizada aprovada. Ressalvas de homologação física, confirmação de Data Safety/Billing e validação final de release permanecem; portanto, não classificar como apto a publicar ou enviar Alpha.
