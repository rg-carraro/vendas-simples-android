# Fase 7C — Versionamento V12 e preparação controlada

**Data:** 16/10/2026
**Repositório:** `rg-carraro/vendas-simples-android`
**Branch:** `fix/v12-seguranca-conformidade`
**Referência V11:** tag `v1.0.11-rc.1` → commit `8aa34881755f222f9b090691aaa6127b31b1a363`
**Versão configurada após esta fase:** `versionCode 12`, `versionName 1.0.12`

## 1. Resumo executivo

Atualizei somente os campos de versão em `app/build.gradle`. O identificador `com.vendassimples.app`, `minSdk 24`, `compileSdk 36`, `targetSdk 36`, Billing Library 9.1.0 e o produto `full_access` permanecem inalterados. A tag V11 e seu commit estão preservados.

A suíte unitária foi reexecutada forçando tarefas: 43/43 testes passaram em debug e release. `lintDebug` e `lintRelease` passaram com zero erros e 67 avisos por variante. A compilação Kotlin de AndroidTest passou; os testes instrumentados não foram executados no dispositivo. O build `:app:assembleDebug` da V12 passou no Codex e seu APK foi conferido por `aapt` e `apksigner`.

**Classificação: APTO COM RESSALVAS.** A preparação técnica/versionamento V12 avançou, mas ainda não há base para gerar o AAB ou declarar a V12 pronta para distribuição. O certificado local de upload ainda precisa ser comparado ao certificado da Play Console; disponibilidade do versionCode 12, Data safety/política pública, Billing real e homologação de runtime permanecem pendentes. Esta fase não gerou AAB nem fez commit, push, merge, tag ou publicação.

## 2. Estado inicial do Git

- Branch ativa: `fix/v12-seguranca-conformidade`.
- `HEAD` inicial: `8aa34881755f222f9b090691aaa6127b31b1a363`, igual a `v1.0.11-rc.1^{commit}`.
- `master` e `origin/master` locais: `99f41e70984d1aa25cc9eca89cc5c6067f55f47b`; a branch V12 está 9 commits à frente, 0 atrás (sem fetch).
- Não havia arquivos staged. A árvore já tinha alterações locais conhecidas das Fases 2–6C e relatórios 7A–7B. Foram preservadas integralmente. O versionamento desta fase altera `app/build.gradle`; este documento é novo.
- `.codex/worktrees/privacy-policy-2026-10` e `.worktrees/privacy-policy-2026-10` são worktrees locais não rastreadas; não devem ser incluídas em staging ou commits.
- Saídas `app/build/` e `.gradle-user/` são ignoradas; APKs, keystores debug, caches e logs não são material fonte nem devem ser versionados.
- `git diff --check` não encontrou erro de whitespace. Git reportou somente avisos de normalização LF/CRLF em arquivos modificados antes desta fase.

### Política pública e refs locais

O pedido assume que o commit de política `5da7f4af0f5954b14b44d8bf1715eb35304c924e` já entrou em `master`. Os refs locais inspecionados mostram o commit na branch `docs/privacy-policy-2026-10-review` e fora da ancestralidade do `master` local (`99f41e7`). **Não fiz fetch**, portanto não afirmo o estado remoto atual. O conteúdo de `site/privacy/index.html` na árvore de trabalho foi comparado com o desse commit e está idêntico. Não fiz merge nem copiei novamente o commit. Antes de integrar/publicar, reconciliar o estado remoto e confirmar a política servida em produção; o relatório da Fase 6C registra pendências de confirmação independente e Data safety.

## 3. Inventário das alterações locais

O `git diff HEAD` registra trabalho preexistente destas áreas, não criado nesta Fase 7C:

| Área | Arquivos/categorias observados | Situação |
|---|---|---|
| Segurança e transferência de backup | `BackupManager.kt`, novos `BackupTransfer.kt` e `BackupValidator.kt`, testes unitários e instrumentados, validadores Python | Mudanças locais anteriores; revisar por arquivo antes de eventual staging |
| Privacidade | `PrivacyPolicy.kt`, asset HTML offline, `MainActivity.kt`, teste de paridade, `site/privacy/index.html` | Conteúdo local de site igual ao commit 5da7f4a; commit não é ancestral dos refs locais de master |
| Android/permissões | Manifesto com `POST_NOTIFICATIONS`, `MainActivity.kt`, temas `values`/`values-v27` | Mudanças locais anteriores; runtime ainda pendente |
| Toolchain | AGP 8.9.1, KGP 2.2.20, wrapper 8.11.1, scripts/JAR do Wrapper, `gradle.properties` | Revisado parcialmente em Fases 7A–7B; alterações anteriores preservadas |
| Documentação | README, decisões, privacidade, Pages, relatórios Fase 2–7B e segurança Play | Untracked/modified preexistente; esta fase adiciona o relatório 7C |
| Worktrees e artefatos | `.codex/`, `.worktrees/`, `.gradle-user/`, `app/build/` | Auxiliares/gerados; excluir de commits |
| Versionamento | `app/build.gradle` | Nesta fase: somente 11/1.0.11 → 12/1.0.12 |

O diff de `MainActivity.kt` mistura tratamento de erros de backup, política offline e autorização/notificações; separação em commits puramente por função requer staging por hunks e revisão específica. Nenhum staging foi feito. Não vi mudança em cálculos financeiros, schema SQLite, limite FREE, implementação do Billing, preço, SKU, CSV/PDF/WhatsApp ou personalização do negócio no diff de compatibilidade desta consolidação. A auditoria não substitui validação em runtime.

## 4. Versionamento realizado

Somente `app/build.gradle` foi alterado nesta fase:

```text
versionCode 12
versionName '1.0.12'
```

Mantidos `applicationId com.vendassimples.app`, min/compile/target SDKs 24/36/36. A referência V11 não foi alterada. A disponibilidade de `versionCode 12` na Play Console não foi verificada porque a Console não foi acessada.

## 5. Testes após o versionamento

Primeira matriz solicitada:

```text
gradlew.bat --no-daemon --max-workers=1 --console=plain :app:testDebugUnitTest :app:testReleaseUnitTest :app:lintDebug :app:lintRelease :app:compileDebugAndroidTestKotlin
```

Resultado: **BUILD SUCCESSFUL**, 3m49s, 68 tarefas (24 executadas, 44 `UP-TO-DATE`). O Gradle encontrou testes unitários up-to-date, então forcei sua execução efetiva e a recompilação das fontes AndroidTest:

```text
gradlew.bat --no-daemon --max-workers=1 --console=plain --rerun-tasks :app:testDebugUnitTest :app:testReleaseUnitTest :app:compileDebugAndroidTestKotlin
```

Resultado: **BUILD SUCCESSFUL**, 4m52s, 53 tarefas executadas.

| Verificação | Resultado observado após `versionCode 12` |
|---|---|
| Testes unitários debug | 43 testes; 0 falhas, 0 erros, 0 ignorados |
| Testes unitários release | 43 testes; 0 falhas, 0 erros, 0 ignorados |
| `lintDebug` | 0 erros; 67 avisos |
| `lintRelease` | 0 erros; 67 avisos |
| `:app:compileDebugAndroidTestKotlin` (forçado) | Passou; compilação somente, sem execução instrumentada |
| `git diff --check` | Sem erro de whitespace; avisos LF/CRLF preexistentes |
| Testes instrumentados em aparelho/emulador | Não executados |

A contagem de lint permanece 67 por variante. O relatório da Fase 7B registrava 66 e 39 `SetTextI18n`; os XML atuais registram 67 e 40, respectivamente. A diferença não foi causada por alteração de fonte nesta fase e não pode ser atribuída ao KGP com as evidências disponíveis. Avisos Kotlin de depreciação para `Locale(String,String)` e `SOFT_INPUT_ADJUST_RESIZE` reapareceram; não foram suprimidos nem corrigidos aqui.

Durante a execução forçada, o Kotlin daemon não conseguiu criar marcador em `%LOCALAPPDATA%\kotlin\daemon` (`AccessDeniedException`). Gradle usou a estratégia fallback sem daemon Kotlin e completou compilação e testes; as depreciações acima foram reportadas. Isso é limitação/aviso do ambiente, não falha final da suíte.

## 6. Build debug da V12

Comando executado:

```text
gradlew.bat --no-daemon --max-workers=1 --console=plain :app:assembleDebug
```

Resultado: **BUILD SUCCESSFUL** em 59 segundos, 32 tarefas (2 executadas, 30 `UP-TO-DATE`). Foi gerado apenas APK debug, não APK release nem AAB.

| Campo do APK V12 debug | Resultado |
|---|---|
| Caminho | `app/build/outputs/apk/debug/app-debug.apk` |
| Tamanho | 7.318.615 bytes |
| SHA-256 APK | `615928EDFDBB8E9CE17CE92FB6F8F11E2750FDB25713C9FD737CC9D123A2CE43` |
| Application ID | `com.vendassimples.app` |
| VersionCode / VersionName | `12 / 1.0.12` |
| Compile SDK lido do manifesto | 36 |
| Assinatura | `apksigner verify`: `Verifies`; APK Signature Scheme v2 válida |
| DN do certificado debug | `CN=Android Debug, O=Android, C=US` |
| SHA-256 do certificado debug | `B9579026295CCF44ADF72483CDFB63EF36522C004FD947FC1AB4EED56EC55115` |

O APK é saída local ignorada pelo Git. Não instalar sobre a V11 da Play; a assinatura é debug e distinta da assinatura de distribuição. Os testes foram unitários; não se afirma teste instrumentado ou físico V12.

O teste de sintaxe `bash -n ./gradlew` foi tentado, mas o host negou a execução do Bash (`Acesso negado`), portanto essa verificação não foi realizada nesta fase. O wrapper Windows `.bat` utilizado nos comandos Gradle executou normalmente. Uma execução anterior de sintaxe consta no relatório Fase 7A; a tentativa atual não a invalida nem substitui.

## 7. Assinatura de upload

A configuração externa indicada por `VENDAS_KEYSTORE_PROPERTIES` existe; o arquivo de propriedades contém os campos requeridos, e o caminho configurado para o keystore existe. Os valores, alias e caminhos internos foram mantidos redigidos. `keytool -list -v`, recebendo a senha do keystore por variável de ambiente temporária não exibida, leu o alias/certificado e produziu a impressão pública:

```text
SHA-256 do certificado local de upload: CD:90:9D:AE:E8:16:B2:7F:4E:32:13:52:73:62:25:80:BE:F0:73:F1:8A:8D:EB:56:04:F5:24:90:9C:63:C6:9E
```

Isso confirma que o keystore e certificado público local são legíveis. Não foi executada assinatura de release nem uma operação de assinatura privada; não foi acessada a Play Console. **Correspondência da upload key: pendente.** O SHA-256 acima deve ser comparado localmente com “Upload key certificate” em Play Console → Integridade do app/Assinatura do app. Se divergir, não gerar ou enviar bundle com essa chave até esclarecer. Não compartilhar keystore, arquivo de propriedades, senhas ou chaves privadas. O certificado debug da seção 6 não participa dessa comparação.

## 8. Notas de versão propostas

Texto curto, pronto para revisão futura, sem afirmar homologação:

> Reforçamos as verificações e a recuperação durante a restauração de backups. A Política de Privacidade pode ser consultada dentro do aplicativo, inclusive offline. Atualizamos a compatibilidade do ambiente Android e corrigimos pontos de estabilidade.

Essas notas não foram enviadas à Play Console e não significam que a V12 foi homologada.

## 9. Plano de integração Git (proposta; não executado)

1. **Backup e restauração:** `BackupManager.kt`, `BackupTransfer.kt`, `BackupValidator.kt`, testes unitários/instrumentados e validadores Python relacionados.
2. **Política offline:** `PrivacyPolicy.kt`, asset offline, teste de paridade e hunks correspondentes de `MainActivity.kt`. O HTML público deve ser tratado separadamente até reconciliar o commit `5da7f4a` com `master` remoto; o conteúdo local já é igual, portanto evitar duplicá-lo.
3. **Compatibilidade Android e notificações:** manifesto, tema base/`values-v27`, e hunks de permissão/avisos em `MainActivity.kt`.
4. **Toolchain Kotlin/Android:** KGP/AGP em `build.gradle` raiz, `gradle.properties` e conjunto consistente do Gradle Wrapper. Revisar scripts/jar do wrapper como unidade e não incluir worktrees.
5. **Testes:** instrumentados, unitários, ferramentas de validação e fixtures sintéticas; separar teste de produção.
6. **Documentação:** README, decisões, segurança/privacidade, Pages e relatórios 2–7C; verificar cada item não rastreado antes de staging.
7. **Versionamento:** `app/build.gradle` isolado por último, após gates e confirmação de disponibilidade do code na Console.

As divisões acima são uma proposta: `MainActivity.kt` contém hunk de mais de uma área e requer staging parcial/revisão para evitar mistura. Não foram usados `git add`, commit, merge, push ou tag. `.codex/`, `.worktrees/`, `.gradle-user/`, `.gradle/`, `app/build/`, certificados e arquivos de credenciais ficam excluídos.

## 10. Checklist de homologação V12

Executar somente depois de gerar/distribuir um artefato autorizado. Usar dispositivo/conta de teste e dados sintéticos; não instalar APK debug sobre a V11 da Play.

### Atualização V11 → V12 via Google Play Alpha

- [ ] Registrar aparelho, versão Android, conta/faixa, bundle/code e evidência sanitizada.
- [ ] Confirmar abertura e funcionamento offline após atualização.
- [ ] Comparar antes/depois: clientes, vendas, parcelas, pagamentos parciais, valores/saldos, fotos e nome do negócio.
- [ ] Conferir relatórios, PDFs, CSV e WhatsApp.
- [ ] Confirmar FULL_ACCESS permanece consultado pelo Play Billing; backup não restaura nem modifica licença.
- [ ] Reiniciar aplicativo/aparelho e confirmar persistência.

### Segurança, licença e compatibilidade

- [ ] Executar backup/restore válido e backup antigo sem nome em ambiente sintético.
- [ ] Rejeitar arquivo corrompido/incompatível sem alterar banco, fotos, preferência ou licença.
- [ ] Testar FREE no limite de 30 vendas e bloqueio da 31ª; testar FULL_ACCESS, compra/restauração e estados pendente/cancelado usando contas de teste Play autorizadas.
- [ ] Executar instrumentados no runtime e homologar API 24 e Android 16/API 36 (incluindo permissão de notificação quando aplicável).
- [ ] Validar política pública servida e conteúdo offline correspondente.

Nenhum teste da lista acima foi declarado como executado nesta fase, salvo os testes unitários e checks de build das seções 5–6.

## 11. Pendências da Play Console

- Confirmar que `versionCode 12` ainda está disponível e verificar track/status do app; refs locais não mostram estado da Console.
- Comparar o SHA-256 do certificado upload local com o “Upload key certificate” da Console.
- Conferir respostas atuais de Data safety em todas as versões/tracks distribuídos e resolver inventário Billing 9.1.0 conforme Fase 6C.
- Confirmar publicação/paridade atual da política; o commit `5da7f4a` não é ancestral do `master` local e a URL servida não foi aberta de forma independente nesta fase.
- Confirmar produto INAPP `full_access` e fluxo de teste de compra/restauração sem alterar SKU/preço nesta fase.
- Upload, análise e distribuição Alpha continuam sem autorização nesta etapa.

## 12. Bloqueadores antes do AAB

1. Comparação do certificado upload local com Console e confirmação de `versionCode 12` disponível.
2. Conclusão das pendências técnicas de conformidade/Data safety e confirmação da política pública.
3. Definir e executar a regressão instrumentada/API 24 e Android recente; o APK debug e testes unitários não cobrem runtime nem upgrade.
4. Executar validações finais no código V12 e somente então pedir autorização específica para gerar AAB assinado.

Nenhum AAB foi gerado nesta fase. A assinatura debug não é a assinatura do AAB.

## 13. Bloqueadores antes de produção

- Homologação Alpha do AAB e atualização V11→V12 pela própria Google Play sem perda de dados/licença.
- Testes reais de compra, restauração, pendente/cancelada e Billing indisponível com contas autorizadas.
- Data safety validada para todas as versões/tracks e política pública atual/paritária.
- Compatibilidade em aparelho/API mínima e recente, backup/rollback e operações financeiras.
- Aprovação explícita de upload/publicação e revisão final do artefato exato/hash/assinatura.

Esta V12 não está declarada pronta para produção.

## 14. Estado final do Git

- Branch `fix/v12-seguranca-conformidade`; `HEAD=8aa3488…`; tag V11 continua no mesmo commit.
- Working tree permanece intencionalmente suja com o trabalho anterior. Arquivos das fases anteriores e worktrees foram preservados.
- Única alteração de código desta fase: `app/build.gradle`; novo documento: `docs/FASE7C_PREPARACAO_V12.md`.
- Nenhum staging, commit, push, merge, tag, build release/AAB, instalação APK ou alteração de Play Console ocorreu.

## 15. Recomendação

**APTO COM RESSALVAS para preparar V12; NÃO APTO para gerar AAB ou produção neste momento.** A próxima fase deve fechar upload certificate/Console, Data safety e política, e preparar homologação instrumentada e V11→V12 pela Alpha. Manter `versionCode 12`/`versionName 1.0.12` no branch de trabalho, sem gerar o AAB, até autorização explícita após revisão desses gates.
