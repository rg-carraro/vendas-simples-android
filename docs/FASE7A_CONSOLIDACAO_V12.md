# Fase 7A — Consolidação técnica e preparação da V12

**Data:** 16/10/2026
**Branch:** `fix/v12-seguranca-conformidade`
**Versão preservada:** `versionCode 11`, `versionName 1.0.11`
**Referência:** tag `v1.0.11-rc.1`, commit `8aa34881755f222f9b090691aaa6127b31b1a363`

## 1. Escopo e conclusão

Esta fase consolida a auditoria das alterações locais e a regressão automatizada antes de qualquer preparação de distribuição V12. Não foram alterados código de produção nem números de versão. Não foram criados commits/tags, feitos push, gerados AABs ou acessadas/mudadas declarações no Play Console.

**Recomendação: apto com ressalvas para prosseguir à Fase 7B (preparação e homologação controlada), não apto para produção ainda.** A suíte unitária e lint estão verdes; permanecem verificações em runtime/dispositivo, Play Billing, formulário Data safety e confirmação independente do estado publicado da política. A combinação de versões Kotlin/AGP/Gradle também está fora da matriz oficialmente indicada pelo Kotlin Gradle Plugin 1.9.24 e deve ser resolvida ou formalmente aceita antes de uma release.

## 2. Estado Git e referência

- `HEAD` e `v1.0.11-rc.1` apontam para `8aa34881755f222f9b090691aaa6127b31b1a363`; a branch atual é `fix/v12-seguranca-conformidade`.
- A referência V11 e seu identificador não foram modificados.
- Há alterações locais preexistentes (arquivos modificados e não rastreados) relativas às Fases 2–6C. Elas foram preservadas. Não houve `git add`, reset, checkout, clean ou descarte.
- `git diff --check` não encontrou whitespace problemático; Git reporta apenas avisos de conversão LF/CRLF em alguns arquivos.
- Dois worktrees locais aparecem sob `.codex/` e `.worktrees/`; os diretórios de trabalho são auxiliares locais e não fazem parte do artefato Android. Eles não foram limpos nem removidos.
- Este relatório é a única alteração feita nesta Fase 7A.

## 3. Inventário e classificação

O inventário abaixo é o estado observado antes de criar este relatório. “Modificado” e “não rastreado” descrevem Git; nenhum item foi adicionado ao índice.

### Código/configuração Android e ferramenta de testes

**Modificados:** `app/src/main/AndroidManifest.xml`, `app/src/main/java/com/example/controlevendas/BackupManager.kt`, `app/src/main/java/com/example/controlevendas/MainActivity.kt`, `app/src/main/res/values/styles.xml`, `build.gradle`, `gradle.properties`, arquivos do Gradle Wrapper (`gradle/wrapper/gradle-wrapper.jar`, `gradle-wrapper.properties`, `gradlew`, `gradlew.bat`), dois testes instrumentados e dois validadores Python.

**Novos:** `BackupTransfer.kt`, `BackupValidator.kt`, `PrivacyPolicy.kt`, `app/src/main/assets/privacy_policy.html`, `app/src/main/res/values-v27/styles.xml`, e testes unitários `BackupTransferTest.kt` e `PrivacyPolicyTest.kt`.

Esses arquivos são candidatos a revisão/inclusão no fonte V12, sujeitos à revisão de diff, assinatura/build e regressão completa na Fase 7B. Os arquivos `androidTest` e `test` são fonte de teste; não integram o APK de produção. Caches, perfis Android/Java, APKs e saídas em `app/build/` e `.gradle-user/` são artefatos locais e não devem ser incluídos em commit/release.

### Política e documentação de produto

`site/privacy/index.html`, `README.md`, `docs/DECISOES.md`, `docs/SEGURANCA_PRIVACIDADE.md` e `docs/WEB_GITHUB_PAGES.md` foram modificados. Foram adicionados relatórios das Fases 2, 3, 4, 5, 6A, 6B, 6B.1 e 6C, além de `docs/SEGURANCA_DADOS_PLAY.md`. São documentação/evidência e conteúdo do site; não são código executável Android. A política embarcada para leitura offline está em `app/src/main/assets/` e, ao contrário do HTML hospedado, entra no APK.

Os diretórios `.codex/` e `.worktrees/` contêm áreas de trabalho Git locais. Devem ser excluídos de qualquer pacote de fonte/release.

### Itens explicitamente fora do escopo desta fase

Não houve alteração em banco SQLite/modelos financeiros, regras de parcelas/pagamentos, filtros financeiros, CSV/PDF/WhatsApp, personalização do nome, código do Billing, preço/SKU/offer, limites FREE/FULL, URL/configurações de Play Console nem política pública remota. Não foi feito merge de branches.

## 4. Revisão de comportamento, dados e compatibilidade

A análise do diff local contra `8aa3488` identifica extração de validação/cópia de backup para classes dedicadas, política local/offline, alterações no manifesto para notificações, ajustes de tema por versão e atualização do toolchain. Os metadados/formato de backup existente permanecem versionados; as versões anteriores descritas pelos validadores continuam aceitas. Os testes cobrem validação/transferência e restauração, mas a execução de instrumentados em aparelho/SQLite/SAF ainda não foi feita nesta fase.

O manifesto fonte solicita `POST_NOTIFICATIONS`; essa permissão dá suporte ao fluxo existente de notificações locais em Android recente e não prova coleta de dados. A decisão de autorização deve permanecer contextual e apenas para o recurso, sem tornar outras funções dependentes dela. A permissão é uma mudança relevante para a revisão do pacote e deve ser conferida no manifesto mesclado e no fluxo real durante a Fase 7B.

A configuração inspecionada preserva `applicationId com.vendassimples.app`, `minSdk 24`, `compileSdk 36`, `targetSdk 36`, `versionCode 11`, `versionName 1.0.11`, Billing Library `9.1.0`, produto `full_access` e oferta `full-access`. A configuração de backup do Android continua com `allowBackup=false`. O banco, IDs e relações não foram modificados no diff observado. Testes de domínio não substituem inspeção/execução de restauração em dispositivo nem confirmação de atualização instalada por Play.

O backup não deve transportar a licença nem alterar estado de entitlement. Os fluxos de FULL_ACCESS dependem do Billing Google e da conectividade para consultar compras; isso é separado dos dados locais offline. Não há evidência nesta fase de alteração do gateway/identificador de compra.

### Toolchain

- Android Gradle Plugin: `8.9.1`.
- Gradle Wrapper: `8.11.1`.
- JDK da execução: Android Studio JBR/JDK `21`.
- Kotlin Gradle Plugin: `1.9.24`.
- API: compile/target `36`, min `24`.

A matriz oficial do Android informa AGP mínimo `8.9.1` para API 36 e Gradle mínimo `8.11.1` para AGP 8.9; JDK 17 ou superior é compatível com AGP 8.x. Assim, AGP/Gradle/JDK usados estão alinhados ao requisito da API 36. A tabela oficial do Kotlin Gradle Plugin 1.9.20–1.9.25 indica suporte pleno até Gradle 8.1.1 e AGP 8.1.0. O build funcionou com a combinação atual, mas Gradle 8.11.1/AGP 8.9.1 está fora dessa faixa publicada para Kotlin 1.9.24. Não atualizar Kotlin nesta consolidação sem avaliar impacto; registrar como pendência técnica antes da candidata de distribuição.

Referências oficiais consultadas:

- [Android Gradle Plugin — versões e compatibilidade](https://developer.android.com/build/releases/about-agp)
- [Kotlin — configurar projetos Gradle e matriz de compatibilidade](https://kotlinlang.org/docs/gradle-configure-project.html)
- [JDK em builds Android](https://developer.android.com/build/jdks)

## 5. Verificações executadas

Gradle foi executado offline, sem daemon, com um worker e compilação Kotlin in-process, usando diretórios Android/Java locais do workspace para contornar a falha de criação de `C:\.android` observada nesta integração. A tentativa de `assembleDebugAndroidTest` em perfil anterior falhou ao criar/acessar `debug.keystore.lock`; repetida com perfil Java/Android local dedicado, os artefatos APK debug e APK androidTest foram produzidos. APK androidTest gerado significa compilação, não execução de testes instrumentados.

| Verificação | Resultado observado |
|---|---|
| `:app:testDebugUnitTest` | 43 testes, 0 falhas, 0 erros, 0 ignorados |
| `:app:testReleaseUnitTest` | 43 testes, 0 falhas, 0 erros, 0 ignorados |
| Cobertura por domínio | Suíte inclui dinheiro/pagamentos, CSV, backup/compatibilidade/transação, política, identidade/licença e percentuais; ver resultados XML/testes de origem |
| `:app:lintDebug` | 0 erros, 66 avisos |
| `:app:lintRelease` | 0 erros, 66 avisos |
| `:app:assembleDebug` | APK debug produzido em `app/build/outputs/apk/debug/app-debug.apk` |
| `:app:assembleDebugAndroidTest` | APK de testes produzido em `app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk`; testes não executados em runtime |
| `git diff --check` | Sem erro; avisos de normalização LF/CRLF do Git |
| `bash -n gradlew` | Sintaxe shell válida (verificação anterior registrada durante o diagnóstico do Wrapper) |

A contagem dos avisos de lint coincide com a Fase 6B; a comparação item a item com os avisos históricos não foi realizada, logo não se afirma que todos sejam preexistentes. A compilação emitiu avisos Kotlin de depreciações e parâmetro sem uso. Os erros de lint conhecidos `NewApi`, `NotificationPermission` e `SuspiciousIndentation` não aparecem nos relatórios atuais.

Não foi executada a suíte instrumentada no emulador ou telefone, nem teste físico/manual nesta Fase 7A. Não foi possível usar ADB dentro deste ambiente por limitações de acesso ao diretório Android/perfil e ao dispositivo; não foi tentado contornar políticas/permissões. O usuário relatou 13/13 verificações manuais na homologação anterior; isso permanece evidência reportada pelo usuário para V11, não execução independente nesta fase nem validação de V12.

Build release de APK/AAB não foi solicitado e não foi executado como artefato de entrega; nenhum AAB foi gerado nesta fase.

## 6. Google Play Segurança dos dados e privacidade

A revisão estática Fase 6C concluiu que o aplicativo opera principalmente com dados comerciais em armazenamento local e não encontrou conta/back-end próprio ou analytics próprio no código auditado. Isso não equivale a “nenhum dado coletado”: a consulta automática de Billing inicia no fluxo de inicialização e a análise anterior não estabeleceu todos os campos/eventos/payloads, finalidades, destinatários e retenção da Billing Library/serviço CCT. A permissão `INTERNET`/dependência não prova por si só quais dados são transmitidos.

**Bloqueadores para enviar/atualizar a declaração Data safety:**

1. Confirmar comportamento/categorias efetivas, finalidade, destino e obrigatoriedade/opcionalidade de Billing 9.1.0 e dependências Google, com fonte oficial ou evidência técnica controlada.
2. Revisar respostas existentes na Play Console considerando todas as versões e faixas distribuídas, não somente o código local V12. Não inventar nem copiar respostas de outra versão.
3. Validar no Console o link e a versão pública atual da política e sua paridade com o conteúdo local. O usuário reportou a política publicada e visualmente conferida nas Fases 6A/6C; abertura independente pelo agente não foi confirmada. Os refs locais conhecidos apontam para branch de política ainda não integrada ao `master` local e não foram atualizados via fetch, portanto esse estado remoto continua pendente de confirmação, não uma conclusão de que a publicação falhou.

A declaração é atualização do próprio desenvolvedor no Console e continua sob responsabilidade dele. Nenhuma resposta foi alterada nesta fase.

## 7. Classificação de prontidão

| Marco | Estado | Motivo |
|---|---|---|
| Continuidade da revisão técnica/Fase 7B | **APTO COM RESSALVAS** | Testes unitários e lint sem erros; builds debug e de instrumentação compilados. |
| Homologação runtime V12 | **PENDENTE** | API 24 e runtime Android recente/dispositivo, upgrade V11→V12 pela Play, e instrumentados não executados nesta fase. |
| Fluxos de licença | **PENDENTE** | Compra, cancelamento, restauração e consulta real em faixa/conta autorizada da Play não foram exercitados. |
| Envio/atualização de Data safety | **BLOQUEADO até evidência** | Billing/dependências, práticas de todas as versões/faixas e respostas atuais do Console ainda precisam confirmação. |
| Release de produção | **NÃO APTO NESTA FASE** | Pendências runtime, Data safety, confirmação pública independente e compatibilidade Kotlin; requer nova decisão e aprovação de release. |

## 8. Checklist para Fase 7B

- [ ] Revisar este inventário e decidir quais arquivos das Fases 2–6C entram em commits lógicos; excluir worktrees, caches e saídas de build.
- [ ] Resolver ou documentar formalmente a compatibilidade suportada entre Kotlin, AGP e Gradle antes de preparar artefato distribuível.
- [ ] Executar testes instrumentados em ambiente autorizado e testar instalação/upgrade, backup/restauração, política offline, notificações, dados financeiros e nome do negócio.
- [ ] Testar API 24 e uma versão recente (idealmente API 36), além de aparelho físico se disponível; não declarar teste manual sem evidência.
- [ ] Validar V11→V12 por faixa Play e fluxos FULL_ACCESS com conta de teste autorizada; confirmar que a licença não é sobrescrita pelo backup.
- [ ] Conferir manifesto mesclado, identidade do pacote, versionamento, certificado/upload key e compatibilidade do artefato com o app distribuído. Não derivar upload key/certificado de credenciais expostas.
- [ ] Obter confirmação de Billing/Data safety e revisar todas as versões/faixas ativas no Console.
- [ ] Verificar independentemente URL/corpo servido da política e consistência do conteúdo offline.
- [ ] Reexecutar suíte completa e lint após qualquer mudança, registrar hashes e resultados e só então pedir decisão sobre candidata.
- [ ] Manter publicação, push, tag e submissão ao Play condicionados a autorização explícita.

## 9. Limitações e riscos residuais

- Testes unitários cobrem lógica, não cenários completos de Android runtime, SAF/SQLite, permissões e lifecycle.
- Não há confirmação independente de dispositivo ADB nesta execução; a homologação física anterior pertence à V11 e foi reportada como realizada pelo usuário.
- Compatibilidade API 24 foi inspecionada por configuração/lint, não exercitada em runtime.
- 66 avisos por variante permanecem; severidades são Warning, mas a lista não foi comparada item a item a um baseline versionado nesta fase.
- Matriz KGP declarada não cobre oficialmente o Gradle/AGP atuais, apesar de o build passar.
- Data safety e conteúdo remoto da política exigem acesso/decisão do desenvolvedor e prova atual na Console/site.
- Os APKs gerados são saídas locais de debug/teste; não são AAB de release nem evidência de assinatura adequada para Play.
