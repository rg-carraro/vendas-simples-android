# Fase 7E — Consolidação Git e preparação da release V12

**Data:** 16/10/2026
**Branch:** `fix/v12-seguranca-conformidade`
**Versão de trabalho:** `com.vendassimples.app`, `versionCode 12`, `versionName 1.0.12`
**Referência V11:** `v1.0.11-rc.1` → `8aa34881755f222f9b090691aaa6127b31b1a363`

## 1. Resumo executivo

A chave local efetivamente selecionada pelo `app/build.gradle` foi lida sem revelar credenciais. Os fingerprints locais SHA-256 e SHA-1 coincidem exatamente com os valores que o usuário copiou da seção **Certificado da chave de upload** da Google Play Console. A validação local foi concluída; a tela da Console continua sendo evidência fornecida pelo usuário, não acessada diretamente nesta execução.

`git fetch origin` foi concluído com sucesso. `origin/master` agora aponta para `fb59cf96392b80c63a42974c80c1b91be1657e4c` e inclui a revisão de política `5da7f4af0f5954b14b44d8bf1715eb35304c924e`. A branch local continua no commit V11 `8aa34881755f222f9b090691aaa6127b31b1a363`; o histórico diverge em 9 commits locais e 2 remotos a partir de `99f41e70984d1aa25cc9eca89cc5c6067f55f47b`. O conteúdo da política local pública e o conteúdo principal do asset offline correspondem ao arquivo do `master` remoto.

O diretório de trabalho continua contendo alterações locais anteriores, sem staging. Não houve commit, push, merge, rebase, reset, tag, build ou AAB. A tag V11 não mudou.

**Classificação: APTO COM RESSALVAS** para continuar a consolidação controlada. O plano de commits está preparado, mas os arquivos e hunks ainda precisam de revisão final antes de staging; essa fase não consolida commits nem autoriza gerar AAB.

## 2. Certificado de upload

O `app/build.gradle` aponta para `signingConfigs.release`, que lê `VENDAS_KEYSTORE_PROPERTIES` ou, se a variável não estiver definida, `user.home/keystore.properties`. Neste processo, o fallback foi o efetivo. Os quatro campos exigidos estavam presentes e o arquivo de keystore configurado existia. `keytool -list -v` retornou sucesso e os fingerprints abaixo; nenhum caminho, alias, senha ou chave privada foi registrado.

| Fingerprint | Certificado local | Valor copiado pelo usuário da Play Console | Resultado |
|---|---|---|---|
| SHA-256 | `CD:90:9D:AE:E8:16:B2:7F:4E:32:13:52:73:62:25:80:BE:F0:73:F1:8A:8D:EB:56:04:F5:24:90:9C:63:C6:9E` | Igual | Coincide |
| SHA-1 | `FE:B9:79:CD:C1:3F:B4:EE:FA:80:35:8D:70:6F:9F:35:40:0F:34:AE` | Igual | Coincide |

**Resultado: CHAVE DE UPLOAD VALIDADA** para o alias/certificado público da configuração release local inspecionada. Os valores da Play Console foram copiados pelo usuário; não houve login nem consulta direta à Console. Nenhum keystore foi modificado. A futura execução Gradle deverá continuar usando a configuração local correta; não transportar o arquivo de propriedades ou segredos para o repositório.

## 3. Estado inicial do Git

- Branch ativa: `fix/v12-seguranca-conformidade`.
- `HEAD`: `8aa34881755f222f9b090691aaa6127b31b1a363`.
- Tag `v1.0.11-rc.1` e branch `release/v11-personalizacao` continuam no mesmo commit de `HEAD`.
- `master` e `origin/master` antes do fetch: `99f41e70984d1aa25cc9eca89cc5c6067f55f47b`.
- Árvore de trabalho: 20 caminhos rastreados modificados, 26 entradas não rastreadas (incluindo as duas pastas auxiliares `.codex/` e `.worktrees/`), nenhum arquivo staged.
- `git diff --stat`: 379 inserções e 72 remoções em 20 caminhos rastreados.
- `git diff --check`: sem erro de whitespace; somente avisos de normalização LF/CRLF em arquivos preexistentes.
- `git ls-files` não identifica keystores, propriedades de assinatura, APKs ou AABs rastreados. Não foram encontrados nomes de arquivo com padrões usuais de segredo/keystore dentro de `.codex/` ou `.worktrees/`; isso não substitui revisão do conteúdo antes de qualquer staging.

O estado sujo já existia antes da fase. Nada dele foi descartado ou preparado para commit.

## 4. Resultado do fetch

A primeira tentativa no sandbox foi impedida de escrever `.git/FETCH_HEAD`; o comando autorizado foi então executado com acesso Git e concluiu sem integração automática:

```text
99f41e7..fb59cf9  master -> origin/master
```

Somente referências remotas foram atualizadas. Não houve pull, merge ou rebase. O branch de trabalho e os arquivos do checkout permaneceram intactos.

## 5. Diferenças entre branches

- Base comum de `HEAD` e `origin/master`: `99f41e70984d1aa25cc9eca89cc5c6067f55f47b`.
- Branch V12 local: 9 commits à frente da base, sem os dois commits remotos.
- `origin/master`: 2 commits à frente da base, sem os nove commits V11 locais.
- Commits remotos: revisão da política `5da7f4a` e merge `fb59cf9`.
- A tag V11 continua preservada no commit original `8aa3488`.

O usuário informou que a lista da Play Console contém os códigos `3, 4, 5, 6, 7, 8, 10, 11` e não contém `12`. Portanto, o código 12 foi reportado como disponível. Essa lista é evidência fornecida pelo usuário e deve ser conferida novamente no momento do envio; o acesso à Console não foi realizado pelo Codex.

## 6. Política pública incorporada ao master

O commit `5da7f4a` está na ancestralidade de `origin/master`. A única mudança entre a base local e o `master` atualizado é `site/privacy/index.html`. A versão remota tem data de atualização 15/10/2026.

Comparações somente leitura confirmaram:

- o arquivo local `site/privacy/index.html` corresponde ao arquivo no `origin/master`, normalizando finais de linha;
- o conteúdo entre `<main>` e `</main>` corresponde exatamente ao asset offline `app/src/main/assets/privacy_policy.html` após normalização de newline e espaços externos.

Assim, a integração não deve copiar a política novamente nem recriar conteúdo. Após commit do estado local e merge autorizado, comparar o resultado final desses dois caminhos e conferir a cópia offline. Isso confirma o conteúdo versionado, não a implantação HTTP atual do GitHub Pages; a disponibilidade online ainda depende de verificação independente.

## 7. Inventário das alterações locais V12

### Caminhos rastreados modificados (20)

| Área | Arquivos |
|---|---|
| Versão e toolchain | `app/build.gradle`, `build.gradle`, `gradle.properties`, `gradle/wrapper/gradle-wrapper.jar`, `gradle/wrapper/gradle-wrapper.properties`, `gradlew`, `gradlew.bat` |
| Backup, Activity e interface Android | `app/src/main/java/com/example/controlevendas/BackupManager.kt`, `app/src/main/java/com/example/controlevendas/MainActivity.kt`, `app/src/main/AndroidManifest.xml`, `app/src/main/res/values/styles.xml` |
| Testes instrumentados e ferramentas | `app/src/androidTest/java/com/example/controlevendas/BackupInstrumentedTests.kt`, `app/src/androidTest/java/com/example/controlevendas/FinalUiInstrumentedTests.kt`, `tools/validar_ajustes_finais_v11.py`, `tools/validar_backup_v11.py` |
| Documentação e política | `README.md`, `docs/DECISOES.md`, `docs/SEGURANCA_PRIVACIDADE.md`, `docs/WEB_GITHUB_PAGES.md`, `site/privacy/index.html` |

### Arquivos não rastreados de código/teste/documentação

- Código e recursos novos: `app/src/main/assets/privacy_policy.html`, `app/src/main/java/com/example/controlevendas/BackupTransfer.kt`, `BackupValidator.kt`, `PrivacyPolicy.kt`, `app/src/main/res/values-v27/styles.xml`.
- Testes novos: `app/src/test/java/com/example/controlevendas/BackupTransferTest.kt` e `PrivacyPolicyTest.kt`.
- Relatórios anteriores não rastreados: `docs/FASE2_SEGURANCA_CONFORMIDADE.md`, `FASE3_REVISAO_HOMOLOGACAO.md`, `FASE4_CORRECOES_TECNICAS.md`, `FASE5_HOMOLOGACAO_CONFORMIDADE.md`, `FASE6A_PUBLICACAO_POLITICA.md`, `FASE6B1_HOMOLOGACAO_FISICA_V11.md`, `FASE6B_HOMOLOGACAO_ANDROID.md`, `FASE6C_SEGURANCA_DADOS_PLAY.md`, `FASE7A_CONSOLIDACAO_V12.md`, `FASE7B1_COMPATIBILIDADE_KOTLIN.md`, `FASE7B2_DIAGNOSTICO_BUILD_DEBUG.md`, `FASE7B3_VALIDACAO_BUILD_POWERSHELL.md`, `FASE7B_PREPARACAO_RC_V12.md`, `FASE7C_PREPARACAO_V12.md`, `FASE7D1_VALIDACAO_UPLOAD_KEY.md`, `FASE7D2_CHAVE_UPLOAD_E_RECONCILIACAO_GIT.md`, `FASE7D_AUDITORIA_PRE_RELEASE_V12.md`, `docs/SEGURANCA_DADOS_PLAY.md`.
- Este relatório (`docs/FASE7E_CONSOLIDACAO_GIT_V12.md`) é a única nova criação desta fase.
- `.codex/` e `.worktrees/` são diretórios auxiliares locais; não devem ser adicionados recursivamente a commits.

### Leitura técnica do diff

As mudanças locais abrangem validação e transferência segura do backup SQLite, cópia offline da política, integração de notificações/permissão, ajustes de tema por API, atualização do AGP/Kotlin/Wrapper, testes e validadores, documentação e mudança de versão para 12/1.0.12. `MainActivity.kt` mistura alterações de backup/erro, política e avisos; staging por arquivo inteiro pode juntar assuntos diferentes. Os testes instrumentados adicionados não foram executados em aparelho nesta fase. O diff não representa, por si só, validação de comportamento em runtime ou conformidade na Play Console.

## 8. Riscos de integração e itens a excluir

- O histórico está divergente; integrar antes de revisar e registrar o conteúdo local pode dificultar recuperação e revisão.
- `MainActivity.kt` requer revisão por hunk para commits separados.
- `site/privacy/index.html` foi alterado nos dois lados desde a base; o arquivo final local e remoto coincide agora, mas a integração deve confirmar igualdade e evitar duplicação.
- A pasta de helpers contém worktrees e conteúdo local não destinado ao produto. Não usar `git add -A`.
- Não incluir `.codex/`, `.worktrees/`, caches (`.gradle-user/`, `.gradle/`), `app/build/`, APK/AAB, keystores, arquivos de propriedades de assinatura, logs ou dados de clientes.
- O README e `docs/RELEASE_PLAY_STORE.md` ainda descrevem a fase corrente como V11; atualizar a documentação de status numa etapa documental aprovada antes de declarar a preparação V12 consolidada.
- `git diff --check` não examina conteúdo não rastreado; revisar também esses arquivos antes de staging.

## 9. Plano de commits rastreáveis (proposta; não executado)

Após revisão de todo o conteúdo e autorização específica para commits:

1. **Toolchain compatível:** `build.gradle`, `gradle.properties`, Wrapper completo e justificativa da matriz AGP 8.9.1 / Kotlin 2.2.20 / Gradle 8.11.1.
2. **Validação/transferência de backup:** `BackupManager.kt`, `BackupTransfer.kt`, `BackupValidator.kt`, testes unitários e instrumentados relacionados e validadores, observando que os testes instrumentados precisam de execução runtime.
3. **Política local e avisos/Activity:** asset, `PrivacyPolicy.kt`, teste de privacidade e hunks revisados de `MainActivity.kt`; separar os hunks de notificação/manifesto conforme a finalidade.
4. **Compatibilidade de permissão/tema:** `AndroidManifest.xml`, `values/styles.xml`, `values-v27/styles.xml` e hunks de Activity correspondentes, junto a testes instrumentados pertinentes.
5. **Documentação de segurança e homologação:** relatórios das Fases 2–7E, decisões, privacidade, Pages e README; revisar referências cruzadas e status antes de adicionar.
6. **Versão V12:** `app/build.gradle` (`12` / `1.0.12`) em alteração rastreável própria após os gates técnicos/comerciais.

Os grupos são uma proposta, não uma autorização para `git add` ou commit. Revisar cada arquivo e selecionar hunks conscientemente. Não criar commits nesta fase. Após commits aprovados e com árvore limpa, integrar `origin/master` por merge explícito (sem rebase/reset), comparar o HTML público e o asset offline, resolver qualquer conflito sem sobrescrita automática e repetir as verificações. A integração também exige autorização própria.

## 10. Preservação dos AABs V11 existentes

Os arquivos abaixo continuam presentes, ignorados pelo Git, com tamanho e hash conferidos nesta execução:

| Arquivo | Tamanho | SHA-256 |
|---|---:|---|
| `app/build/outputs/bundle/release/app-release.aab` | 5.320.483 bytes | `D751F51DC69AC2DA2784D841374288E4456BD666E2A5237C2E91C1EE9D47AABA` |
| `app/build/outputs/bundle/release/vendas-simples-v11.aab` | 5.320.483 bytes | `D751F51DC69AC2DA2784D841374288E4456BD666E2A5237C2E91C1EE9D47AABA` |

O relatório da Fase 7D diz que o manifesto interno desses AABs não foi extraído; o nome do segundo sugere V11, mas não é prova de versão. Ambos têm mesmo hash e antecedem a V12. Não foram movidos ou modificados. `bundleRelease` pode sobrescrever `app-release.aab`; quando houver autorização futura, construir em worktree isolado para que `app/build/` seja separado e comparar hash/tamanho dos artefatos históricos antes e depois. Não os renomear nem mover nesta fase.

## 11. Verificações técnicas e evidências anteriores

| Item | Evidência | Estado nesta fase |
|---|---|---|
| `applicationId` / versão | `com.vendassimples.app`, `12 / 1.0.12`, fonte em `app/build.gradle` | Conferidos por leitura |
| SDK | `minSdk 24`, `compileSdk 36`, `targetSdk 36` | Conferidos por leitura |
| AGP / Kotlin / Gradle | `8.9.1` / `2.2.20` / `8.11.1` | Conferidos por leitura; matriz documentada na Fase 7B.1 |
| Billing | `9.1.0`, produto permanece `full_access` na configuração/código | Versão conferida; testes Play reais pendentes |
| Testes unitários | Relatório Fase 7C: 43 passaram em debug e 43 em release | Evidência anterior, não reexecutada |
| Lint | Relatório Fase 7C: 0 erros e 67 avisos por variante | Evidência anterior, não reexecutada |
| AndroidTest | Compilação Kotlin passou; testes instrumentados não executados | Runtime pendente |
| APK debug V12 | Relatório 7C registra applicationId/versão e validação por `aapt`/`apksigner`; arquivo atual com 7.318.615 bytes e SHA-256 `615928EDFDBB8E9CE17CE92FB6F8F11E2750FDB25713C9FD737CC9D123A2CE43` conferido diretamente | Build/metadata são evidência anterior; hash/tamanho do arquivo foram conferidos nesta fase. APK debug não é upload key nem AAB release |
| Certificado release | `keytool` sobre o alias efetivo, SHA-256/SHA-1 correspondentes ao informado da Console | Conferido nesta fase |
| `git diff --check` | Sem erro; avisos preexistentes CRLF | Executado nesta fase |

Não executei testes, lint, build debug/release ou AAB nesta fase. A disponibilidade de `versionCode 12` foi reportada pelo usuário e requer confirmação final no envio.

## 12. Pendências técnicas e de conformidade

- Executar novamente testes unitários, lint e builds na árvore/commit consolidado, pois os resultados da Fase 7C antecedem a consolidação.
- Executar os testes instrumentados em dispositivo/emulador; testar API 24 e Android 16/API 36.
- Homologar update V11 → V12 via Google Play Alpha com dados sintéticos: clientes, vendas, parcelas, pagamentos, fotos, preferências e licença.
- Exercitar compra/restauração `FULL_ACCESS`, FREE 30 vendas e Billing 9.1.0 com contas de teste autorizadas.
- Conferir a disponibilidade do código 12 na Console novamente antes de upload.
- Confirmar as declarações de Segurança dos dados, comportamento de telemetria do Billing/DataTransport/CCT e a página pública efetivamente servida. A Console não foi consultada; o acesso HTTP à página não foi validado nesta fase.
- O backup SQLite não tem criptografia própria; a licença não é restaurada do backup. Manter esta limitação refletida no produto e na declaração pública.
- Confirmar no artefato release final os metadados, assinatura, certificado, esquema de assinatura e SHA-256. Não usar `apksigner` como validador de AAB.

## 13. Estado Git final

Após o fetch:

- `HEAD`, branch, tag V11 e arquivos locais continuam inalterados.
- `origin/master` foi atualizado para `fb59cf96392b80c63a42974c80c1b91be1657e4c`.
- A árvore permanece com as 20 modificações rastreadas e 26 entradas não rastreadas descritas acima; nenhum staging foi feito.
- Este relatório é o único arquivo criado nesta fase.
- Não houve commit, push, merge, rebase, reset, tag, build, AAB, upload ou publicação.

## 14. Próxima ação recomendada

Revisar o inventário de arquivos não rastreados e os hunks mistos de `MainActivity.kt`; corrigir o status documental V11/V12 onde for apropriado; então, com autorização específica, preparar commits locais pequenos e revisáveis. Depois de commits e árvore limpa, integrar `origin/master` por merge controlado e reexecutar regressão no resultado integrado. Preservar os AABs históricos e fazer qualquer AAB V12 futuro em worktree separada. A chave de upload local está validada pelos dois fingerprints informados, mas upload, Alpha, produção e qualquer operação de publicação continuam dependentes de autorização própria.

**Classificação: APTO COM RESSALVAS.** A chave está validada e as referências remotas atualizadas; a revisão/staging integral, a integração, a regressão do estado consolidado e os gates de conformidade/homologação continuam pendentes. Aguardar autorização antes de commits ou integração Git. Nenhum AAB foi gerado.
