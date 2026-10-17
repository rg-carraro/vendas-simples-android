# Fase 7F — Commits locais V12 e preparação da integração Git

**Data:** 16/10/2026
**Repositório:** `rg-carraro/vendas-simples-android`
**Branch:** `fix/v12-seguranca-conformidade`
**Versão:** `com.vendassimples.app`, `versionCode 12`, `versionName 1.0.12`
**Referência V11:** `v1.0.11-rc.1` → `8aa34881755f222f9b090691aaa6127b31b1a363`

## 1. Resumo executivo

Foram criados cinco commits locais, com staging explícito por arquivo, para a validação do backup, política offline e controles de aviso, toolchain, metadados V12 e documentação selecionada. A chave local de upload havia sido validada na Fase 7E. Não houve push, merge, rebase, tag, AAB ou publicação.

Após os commits, foram executados testes unitários debug/release, lint debug/release e compilação Kotlin dos testes Android. Os 43 testes de cada variante passaram, a compilação AndroidTest passou e o lint terminou com zero erros e 67 avisos por variante. Testes instrumentados em dispositivo não foram executados.

**Classificação: APTO COM RESSALVAS.** Os commits estão rastreáveis e a V11 permanece preservada. A árvore ainda contém alterações locais e documentos que ficaram fora do staging; a integração com `origin/master` não foi executada e exige autorização própria.

## 2. Estado Git inicial

- Branch: `fix/v12-seguranca-conformidade`.
- `HEAD` inicial: `8aa34881755f222f9b090691aaa6127b31b1a363`.
- `v1.0.11-rc.1` apontava para esse mesmo commit.
- `origin/master`: `fb59cf96392b80c63a42974c80c1b91be1657e4c` (atualizado na Fase 7E).
- Base comum: `99f41e70984d1aa25cc9eca89cc5c6067f55f47b`; antes dos commits havia 9 commits locais e 2 remotos exclusivos.
- O worktree já tinha modificações e arquivos não rastreados de fases anteriores, sem staging. Foram preservados.
- A chave local estava validada pelos fingerprints SHA-256 e SHA-1 registrados na Fase 7E; não reli nem alterei o keystore nesta fase.

## 3. Inventário e classificação das alterações

### A — Incluídas em commits locais V12

**Segurança de backup**

- `BackupManager.kt`, novas `BackupTransfer.kt` e `BackupValidator.kt`.
- Testes unitários `BackupTransferTest.kt`, testes instrumentados de backup e ferramenta `validar_backup_v11.py`.

**Política local e avisos**

- `PrivacyPolicy.kt`, asset offline `privacy_policy.html`, integração correspondente em `MainActivity.kt` e testes unitário/instrumentado.
- Manifesto, recursos de tema `values` e `values-v27`, ferramenta `validar_ajustes_finais_v11.py`.
- `MainActivity.kt` foi incluído por inteiro porque seus hunks relacionados a backup, política, permissão/avisos e correção de indentação estavam intercalados; o arquivo foi revisado no diff.

**Toolchain e versão**

- `build.gradle`, `gradle.properties`, Gradle Wrapper (`jar`, propriedades, `gradlew`, `gradlew.bat`).
- `app/build.gradle`, somente `versionCode 12` e `versionName 1.0.12`.

**Documentação selecionada**

- `docs/DECISOES.md`, `docs/SEGURANCA_PRIVACIDADE.md`, `docs/SEGURANCA_DADOS_PLAY.md` e relatórios das Fases 2, 3, 4, 6A, 6C, 7A, 7B.1, 7B, 7C, 7D.1, 7D.2, 7D e 7E.
- Os relatórios são históricos e registram o estado observado em suas respectivas fases; divergências posteriores são resolvidas por relatórios cronologicamente posteriores.

### B — Preservadas fora dos commits; exigem revisão ou decisão posterior

- `README.md` continua descrevendo a fase atual como V11, embora contenha links adicionados a relatórios das Fases 2–4. Ficou fora do commit até atualização coerente do status e dos links V12.
- `docs/WEB_GITHUB_PAGES.md` inclui referência a diretório de evidências local `.gradle-user/...`; requer revisão antes de versionamento.
- `site/privacy/index.html` continua modificado no worktree, mas seu conteúdo coincide com o já incorporado a `origin/master` pelo commit `5da7f4a`. Não foi duplicado em commit local; confirmar a igualdade na integração futura.
- `docs/FASE5_HOMOLOGACAO_CONFORMIDADE.md`, `FASE6B1_HOMOLOGACAO_FISICA_V11.md`, `FASE6B_HOMOLOGACAO_ANDROID.md`, `FASE7B2_DIAGNOSTICO_BUILD_DEBUG.md` e `FASE7B3_VALIDACAO_BUILD_POWERSHELL.md` ficaram fora: incluem caminho pessoal, identificador de dispositivo ou referência de configuração que requer sanitização/revisão antes de publicar no histórico Git.
- Este relatório, `docs/FASE7F_COMMITS_V12.md`, foi criado após o commit de documentação e permanece não commitado para evitar criar um ciclo de commits apenas para registrar o próprio hash.

### C — Não versionadas

- `.codex/`, `.worktrees/`, caches `.gradle-user/` e `.gradle/`, saídas `app/build/`, arquivos temporários, logs, APKs e AABs.
- Não foram encontrados no índice arquivos `.aab`, `.apk`, `.jks`, `.keystore`, `keystore.properties`, tokens ou chaves privadas. Varredura heurística do diff staged não encontrou atribuições óbvias de senhas/tokens ou blocos de chave privada; isso não substitui revisão de conteúdo.
- `.gitignore` não foi alterado. Nenhum conteúdo de usuário/cliente foi adicionado intencionalmente.

## 4. Revisão de segredos e artefatos

Cada grupo foi staged com caminhos explícitos; não foi usado `git add .` ou `git add -A`. Os diffs staged foram revisados, a busca heurística não retornou marcadores de segredo e `git diff --cached --check` foi executado antes dos commits. Em alguns relatórios Markdown, o check registrou espaços finais usados como quebra de linha deliberada; não houve erro de conteúdo/código. Arquivos binários e configurações de assinatura não foram staged.

O HTML público da política ficou fora dos commits porque a versão local já corresponde ao conteúdo de `origin/master`. O asset local da política foi incluído no commit do aplicativo.

## 5. Commits locais criados

| Ordem | Commit | Assunto | Conteúdo principal |
|---:|---|---|---|
| 1 | `b2e65a02868646b0e9122eb41f46523ba526bb9b` | `fix: harden sqlite backup validation and transfer` | Validação/limites de backup, cópia com integridade, testes e ferramenta de validação |
| 2 | `5467c40edd57f8b5b1271e8ead874404e50b426c` | `feat: add offline privacy policy and notification controls` | Política offline, permissão explícita de avisos, mensagens seguras e testes |
| 3 | `25ff1611c7879863eba939eb8d4d180696723ec1` | `build: update Android Gradle and Kotlin toolchain` | AGP 8.9.1, Kotlin 2.2.20, Gradle 8.11.1 e Wrapper |
| 4 | `eb717b7820fba77f56e7316fa415b5b43cde8ff3` | `build: set V12 version metadata` | `12 / 1.0.12` |
| 5 | `3d770dfc7e5eeeb9bd096a4e9a6347d6dc78a8e3` | `docs: consolidate V12 security and release evidence` | Decisões, segurança e relatórios selecionados das Fases 2–7E |

Não houve commit vazio, amend ou alteração de commits anteriores.

Arquivos por commit, em ordem:

1. **Backup:** `app/src/androidTest/java/com/example/controlevendas/BackupInstrumentedTests.kt`, `app/src/main/java/com/example/controlevendas/BackupManager.kt`, `app/src/main/java/com/example/controlevendas/BackupTransfer.kt`, `app/src/main/java/com/example/controlevendas/BackupValidator.kt`, `app/src/test/java/com/example/controlevendas/BackupTransferTest.kt`, `tools/validar_backup_v11.py`.
2. **Política e avisos:** `app/src/androidTest/java/com/example/controlevendas/FinalUiInstrumentedTests.kt`, `app/src/main/AndroidManifest.xml`, `app/src/main/assets/privacy_policy.html`, `app/src/main/java/com/example/controlevendas/MainActivity.kt`, `app/src/main/java/com/example/controlevendas/PrivacyPolicy.kt`, `app/src/main/res/values-v27/styles.xml`, `app/src/main/res/values/styles.xml`, `app/src/test/java/com/example/controlevendas/PrivacyPolicyTest.kt`, `tools/validar_ajustes_finais_v11.py`.
3. **Toolchain:** `build.gradle`, `gradle.properties`, `gradle/wrapper/gradle-wrapper.jar`, `gradle/wrapper/gradle-wrapper.properties`, `gradlew`, `gradlew.bat`.
4. **Versão:** `app/build.gradle`.
5. **Documentação:** `docs/DECISOES.md`, `docs/FASE2_SEGURANCA_CONFORMIDADE.md`, `docs/FASE3_REVISAO_HOMOLOGACAO.md`, `docs/FASE4_CORRECOES_TECNICAS.md`, `docs/FASE6A_PUBLICACAO_POLITICA.md`, `docs/FASE6C_SEGURANCA_DADOS_PLAY.md`, `docs/FASE7A_CONSOLIDACAO_V12.md`, `docs/FASE7B1_COMPATIBILIDADE_KOTLIN.md`, `docs/FASE7B_PREPARACAO_RC_V12.md`, `docs/FASE7C_PREPARACAO_V12.md`, `docs/FASE7D1_VALIDACAO_UPLOAD_KEY.md`, `docs/FASE7D2_CHAVE_UPLOAD_E_RECONCILIACAO_GIT.md`, `docs/FASE7D_AUDITORIA_PRE_RELEASE_V12.md`, `docs/FASE7E_CONSOLIDACAO_GIT_V12.md`, `docs/SEGURANCA_DADOS_PLAY.md`, `docs/SEGURANCA_PRIVACIDADE.md`.

## 6. Arquivos excluídos do staging

Além dos itens B e C acima, ficaram fora o `README.md`, `docs/WEB_GITHUB_PAGES.md`, `site/privacy/index.html`, relatórios com dados do ambiente a sanitizar, diretórios auxiliares, arquivos ignorados de build e artefatos históricos. Nenhum arquivo não revisado foi adicionado automaticamente.

## 7. Testes após consolidação

### Execução aprovada

Comando executado com `--rerun-tasks`:

```text
gradlew.bat --no-daemon --max-workers=1 --console=plain --rerun-tasks :app:testDebugUnitTest :app:testReleaseUnitTest :app:lintDebug :app:lintRelease :app:compileDebugAndroidTestKotlin
```

Resultado: **BUILD SUCCESSFUL**, 68 tarefas executadas em 6m20s.

| Verificação | Resultado |
|---|---|
| Testes unitários debug | 43 testes; 0 falhas, 0 erros, 0 ignorados |
| Testes unitários release | 43 testes; 0 falhas, 0 erros, 0 ignorados |
| `lintDebug` | 0 erros; 67 avisos |
| `lintRelease` | 0 erros; 67 avisos |
| `compileDebugAndroidTestKotlin` | Passou; compilação somente |
| Bash `-n ./gradlew` | Passou |
| `./gradlew --version` via Git Bash, Gradle user home do workspace | Passou; Gradle 8.11.1, JBR 21.0.10 |
| `git diff --check` | Sem erros; avisos CRLF em arquivos modificados preexistentes |

Os avisos Kotlin de `Locale(String,String)` e `SOFT_INPUT_ADJUST_RESIZE` continuam. Os 67 avisos lint são os listados nos relatórios gerados; não foram suprimidos nesta fase.

### Falha de ambiente e limites

A primeira tentativa com um Gradle user home isolado não encontrou o marcador do plugin Kotlin 2.2.20. Repeti usando o cache Gradle já existente, sem modificar a configuração do projeto, e a execução acima passou. Um primeiro `gradlew --version` sem `JAVA_HOME` falhou; a verificação final com JBR 21 e cache dentro do workspace passou.

Testes Android instrumentados não foram executados no dispositivo/emulador; apenas compilados. Não executei `assembleDebug`, build release, `bundleRelease` ou qualquer geração de AAB nesta fase.

## 8. Situação da tag V11 e versão

- `versionCode = 12`, `versionName = 1.0.12`, `applicationId = com.vendassimples.app`.
- A tag `v1.0.11-rc.1` continua apontando para `8aa34881755f222f9b090691aaa6127b31b1a363`.
- O commit V11 não foi alterado e não recebeu novos commits por amend/rebase.

## 9. AABs anteriores

Os AABs permaneceram ignorados pelo Git e foram conferidos após os commits:

| Arquivo | Tamanho | SHA-256 |
|---|---:|---|
| `app/build/outputs/bundle/release/app-release.aab` | 5.320.483 bytes | `D751F51DC69AC2DA2784D841374288E4456BD666E2A5237C2E91C1EE9D47AABA` |
| `app/build/outputs/bundle/release/vendas-simples-v11.aab` | 5.320.483 bytes | `D751F51DC69AC2DA2784D841374288E4456BD666E2A5237C2E91C1EE9D47AABA` |

Os hashes/tamanhos coincidem com a Fase 7E; nenhum artefato foi movido ou alterado. Conforme Fase 7D, o manifesto interno não foi extraído nesta execução, portanto o nome do arquivo não comprova sozinho sua versão. `bundleRelease` futuro pode sobrescrever `app-release.aab`; gerar eventual V12 em worktree isolado após autorização.

## 10. Divergência com `origin/master`

`origin/master` continua em `fb59cf96392b80c63a42974c80c1b91be1657e4c` e inclui os commits da política pública. Após os cinco commits desta fase, `HEAD` é `3d770dfc7e5eeeb9bd096a4e9a6347d6dc78a8e3`. A contagem `git rev-list --left-right --count HEAD...origin/master` é **14 / 2**: 14 commits locais exclusivos (9 commits V11 da linha atual e 5 commits locais V12 desta fase) e 2 remotos exclusivos, com base comum `99f41e70984d1aa25cc9eca89cc5c6067f55f47b`.

Nenhum merge/rebase/push foi executado. A política pública remota já contém o mesmo HTML que a modificação local não commitada; confirmar igualdade após integração para evitar duplicação.

## 11. Plano de integração controlada

1. Revisar as alterações locais restantes (`README.md`, `docs/WEB_GITHUB_PAGES.md`, `site/privacy/index.html`) e os relatórios excluídos; corrigir status V11/V12 e sanitizar evidências se forem necessários.
2. Preservar o checkout atual e qualquer conteúdo não commitado. Para integrar, preferir um worktree novo e limpo a partir de `fix/v12-seguranca-conformidade`, após confirmar que o destino não existe.
3. Com autorização específica para merge, integrar `origin/master` por merge explícito, sem rebase/reset. Rever o histórico e os conflitos antes de resolver.
4. Conferir que o merge preserva `5da7f4a`/`fb59cf9`, que `site/privacy/index.html` termina igual ao HTML remoto e que o asset offline corresponde ao corpo público.
5. Reexecutar testes unitários, lint e compilação AndroidTest no commit integrado; depois tratar homologação em runtime/API 24 e Alpha V11→V12.

O plano não executa nem autoriza merge, push, AAB ou publicação.

## 12. Pendências técnicas e de conformidade

- Executar instrumentados em aparelho/emulador e homologar API 24 e Android 16/API 36.
- Validar atualização V11→V12 via Google Play Alpha e preservar registros, fotos, preferências e licença.
- Testar compra/restauração `FULL_ACCESS`, FREE de 30 vendas e estados Billing com conta de teste.
- Confirmar novamente `versionCode 12` na Play Console antes do upload; disponibilidade foi informada pelo usuário.
- Confirmar Data Safety e inventário de telemetria Billing/DataTransport/CCT, bem como o conteúdo efetivamente servido pelo GitHub Pages.
- Validar metadados, assinatura, certificado, estrutura e SHA-256 do AAB V12 quando a geração tiver autorização própria.

## 13. Estado Git final

- Branch: `fix/v12-seguranca-conformidade`; `HEAD=3d770dfc7e5eeeb9bd096a4e9a6347d6dc78a8e3`.
- Nenhum arquivo staged após o quinto commit.
- Alterações rastreadas restantes: `README.md`, `docs/WEB_GITHUB_PAGES.md`, `site/privacy/index.html`.
- Não rastreados restantes: `.codex/`, `.worktrees/` e cinco relatórios excluídos para revisão/sanitização.
- Este relatório é novo e permanece não commitado; não foi criado ciclo adicional de documentação.
- Sem merge, rebase, push, tag, AAB ou publicação.

## 14. Próxima ação recomendada

Revisar o status documental V12 e decidir o destino dos arquivos preservados fora dos commits. Depois, com autorização específica, integrar `origin/master` em worktree limpa, reexecutar a regressão no histórico integrado e concluir homologação técnica/Play Console. A branch está organizada em commits locais, mas não está integrada nem pronta para envio Alpha. **Classificação final: APTO COM RESSALVAS.**
