# Fase 7B — Consolidação controlada e preparação da RC V12

**Data:** 16/10/2026
**Aplicativo:** Vendas Simples — `com.vendassimples.app`
**Branch de trabalho:** `fix/v12-seguranca-conformidade`
**HEAD/tag V11:** `8aa34881755f222f9b090691aaa6127b31b1a363` / `v1.0.11-rc.1`
**Versão que permanece na árvore:** `versionCode 11`, `versionName 1.0.11`

## 1. Resumo executivo

Esta fase revisou o estado real do Git, as alterações locais das Fases 2–6C, os caminhos de segurança/atualização e a matriz do toolchain. Reexecutei testes unitários e lint; compilei fontes de testes instrumentados. Não houve modificação de código, versão, commit, tag, merge, push, AAB, Play Console ou site publicado. O único arquivo criado nesta fase é este relatório.

**Classificação: NÃO APTO PARA GERAR AAB V12 neste momento.** O Kotlin Gradle Plugin 1.9.24 está fora da faixa oficialmente plenamente compatível com AGP 8.9.1/Gradle 8.11.1. A preparação de artefato deve aguardar aprovação da menor correção proposta abaixo e sua regressão. Além disso, o empacotamento debug falhou no ambiente por acesso negado ao lock do keystore debug; a compilação AndroidTest passou, mas o APK instrumentado não foi reempacotado nesta execução. A existência da configuração de assinatura release local não comprova compatibilidade com a upload key do Play.

A revisão técnica/documental pode continuar. As 43 unidades de teste por variante e os resultados de lint foram preservados em relação à Fase 7A; isso não homologa o runtime da V12. Compras reais, API 24, upgrade pela Alpha e declaração Data safety permanecem pendentes.

## 2. Estado inicial do Git

- Branch ativa `fix/v12-seguranca-conformidade`, `HEAD=8aa3488…`, igual à tag `v1.0.11-rc.1` e à branch `release/v11-personalizacao`.
- `master`/`origin/master` conhecidos apontam a `99f41e7`. A branch de trabalho contém **9 commits à frente e nenhum atrás** no snapshot local; não fiz merge nem fetch.
- As alterações locais não commitadas já existiam. O inventário inicial desta execução mostrou 38 entradas: 19 rastreadas modificadas e 19 não rastreadas. Foram preservadas. Após criar este relatório, o estado esperado soma 39 entradas, sem staging.
- `git diff --check` não encontrou erros de whitespace; reportou avisos de normalização LF/CRLF em arquivos do Wrapper/configuração.
- Worktrees listadas: `.codex/worktrees/privacy-policy-2026-10` em `99f41e7` e `.worktrees/privacy-policy-2026-10` na branch `docs/privacy-policy-2026-10-review`, commit `5da7f4a`. São árvores separadas e ficam fora da consolidação da aplicação.
- O commit de revisão da política `5da7f4af0f5954b14b44d8bf1715eb35304c924e` está em branch separada e não está integrado ao `master` local conhecido. Não concluo a partir disso se o conteúdo remoto do Pages está ou não atualizado: não fiz fetch, e o usuário registrou confirmação visual na Fase 6C.
- A URL oficial foi aberta pela ferramenta web nesta fase, mas a ferramenta retornou “URL is not accessible”. Isso não informa HTTP status. Portanto, acessibilidade/paridade pública atual ficam **não verificadas independentemente**; a confirmação visual anterior é evidência fornecida pelo usuário.

### Inventário das alterações preexistentes

**Fonte/configuração Android modificados:** `app/src/androidTest/java/com/example/controlevendas/BackupInstrumentedTests.kt`, `FinalUiInstrumentedTests.kt`, `app/src/main/AndroidManifest.xml`, `BackupManager.kt`, `MainActivity.kt`, `app/src/main/res/values/styles.xml`, `build.gradle`, `gradle.properties`, `gradle/wrapper/gradle-wrapper.jar`, `gradle-wrapper.properties`, `gradlew` e `gradlew.bat`.

**Fonte Android/testes novos:** `BackupTransfer.kt`, `BackupValidator.kt`, `PrivacyPolicy.kt`, `app/src/main/assets/privacy_policy.html`, `app/src/main/res/values-v27/styles.xml`, `BackupTransferTest.kt` e `PrivacyPolicyTest.kt`.

**Site/documentação/scripts modificados:** `README.md`, `docs/DECISOES.md`, `docs/SEGURANCA_PRIVACIDADE.md`, `docs/WEB_GITHUB_PAGES.md`, `site/privacy/index.html`, `tools/validar_ajustes_finais_v11.py`, `tools/validar_backup_v11.py`.

**Relatórios/documentos não rastreados:** relatórios Fases 2, 3, 4, 5, 6A, 6B, 6B.1, 6C, 7A, `docs/SEGURANCA_DADOS_PLAY.md` e este relatório 7B. A classificação completa por commit está proposta na seção 11.

**Exclusões de fonte/release:** `.codex/`, `.worktrees/`, `.gradle-user/`, `.gradle/`, `app/build/`, perfis, logs, APKs e AABs locais. `.gradle-user/` e `**/build/` são ignorados pelo Git; worktrees aparecem como não rastreadas nesta árvore. A propriedade de assinatura está fora do repositório e não foi incluída nem exibida.

## 3. Alterações técnicas revisadas e limites de inclusão

O diff local contra o commit V11 contém ajustes do projeto nas seguintes áreas: atualização do AGP/Wrapper, recurso de notificação com permissão explícita em Android recente, tema qualificado por API, validação e transferência de backup, política embutida/offline, testes e relatórios. A página em `site/privacy/` é conteúdo para o Pages; seu deploy é separado da distribuição Android. A política em `assets/` entra no APK.

Os testes automatizados em `src/test` e `src/androidTest` são fonte de validação, não funcionalidades incluídas no APK de produção. Worktrees, logs, certificados, credenciais, bancos/fotos reais e artefatos de build não entram em commits. A aplicação não recebeu mudanças nesta Fase 7B; nada foi promovido ou consolidado em commit.

O diff não mostra alteração a `LocalDatabase.kt`, `RestoreTransaction.kt`, modelos financeiros, exportadores, PDF, WhatsApp ou gateway de Billing. O manifesto inclui `POST_NOTIFICATIONS`, necessário ao fluxo local de avisos existente; isso não prova coleta/transmissão de dados. A permissão e seu pedido contextual precisam continuar cobertos por teste Android e revisão do manifesto mesclado.

## 4. Compatibilidade AGP / Gradle / Kotlin / JDK / API

| Componente observado | Valor |
|---|---|
| Android Gradle Plugin | `8.9.1` |
| Gradle Wrapper | `8.11.1` (distribuição bin com SHA-256 configurado) |
| Kotlin Gradle Plugin | `1.9.24` |
| JDK da execução | Android Studio JBR 21 |
| `minSdk` | `24` |
| `compileSdk` / `targetSdk` | `36` / `36` |

A tabela oficial do Android indica AGP mínimo **8.9.1** para API 36 e Gradle mínimo **8.11.1** para AGP 8.9. AGP 8.x requer JDK 17 ou superior; JDK 21 atende a esse mínimo. Essa parte está alinhada.

A tabela oficial atual do KGP informa que Kotlin 1.9.20–1.9.25 é plenamente compatível somente até Gradle 8.1.1 e AGP 8.1.0. O conjunto usado (Gradle 8.11.1/AGP 8.9.1) está fora dessa matriz. O build local anterior passar não altera esse status de suporte.

**Menor correção recomendada, não aplicada:** avaliar pin de KGP para `2.2.20`, cuja matriz oficial inclui Gradle 8.11.1 e AGP 8.9.1, mantendo AGP/Gradle/API já requeridos por compileSdk 36. Depois, executar testes, lint, builds e revisão de diferenças do compilador/stdlib. Não atualizei Kotlin automaticamente, conforme critério de interrupção desta fase. Essa alteração pode expor avisos/mudanças de compilador e requer aprovação antes de tocar em configuração.

Fontes oficiais:

- [Matriz Android Gradle Plugin e API](https://developer.android.com/build/releases/about-agp)
- [Matriz Kotlin Gradle Plugin, Gradle e AGP](https://kotlinlang.org/docs/gradle-configure-project.html)
- [JDK em builds Android](https://developer.android.com/build/jdks)

**Efeito:** A preparação do AAB fica interrompida até que a incompatibilidade documentada seja resolvida ou uma decisão explícita aceite a combinação fora da matriz. Não avancei `versionCode` nem `versionName`.

## 5. Revisão de segurança de backup

A implementação revisada faz a verificação em cópia privada antes da substituição. `BackupValidator` abre a base somente para leitura, ativa `query_only` e tenta desativar `trusted_schema`; independentemente da disponibilidade desse PRAGMA em versões antigas, a whitelist de tabelas/índices continua necessária. Compara o schema com uma base em memória criada pelo `LocalDatabase.onCreate`, limita objetos e SQL, rejeita estruturas extras/views/triggers e não executa DDL recebido do backup. Verifica `integrity_check`, versões e tipos dos metadados, IDs/referências, datas, BLOBs, posições de parcelas e overflow de somas.

`BackupTransfer` limita o fluxo real lido, cabeçalho SQLite, tamanho e espaço disponível; limitações do provedor são tratadas explicitamente. `RestoreTransaction` mantém cópia anterior e journal para recuperar banco e preferência do negócio em falha/reinício. Mensagens controladas evitam devolver SQL/caminhos do backup à UI.

Compatibilidade preservada pelo código: schema financeiro **3**; metadados de backup formato **1 e 2**; formato 1 sem nome e formato 2 com nome. Nome ausente/vazio não deve apagar a preferência atual. A licença não está nos metadados nem no journal de preferência. As unidades testadas não substituem teste de SAF/SQLite real instrumentado desta execução.

Limites registrados nas fases anteriores continuam: formatos não têm autenticação criptográfica de origem; uma alteração coerente de registros pode passar validações estruturais/semânticas. Bases legítimas acima de 512 MiB podem ser recusadas. A validação da foto cobre tipo, tamanho não vazio e vínculo, não todo codec/semântica da imagem. O relatório da Fase 2 contém os pormenores e limites; não declarar “backup inviolável” ou “segurança absoluta”.

## 6. Revisão de banco e upgrade V11 → V12

`LocalDatabase.DB_VERSION` permanece 3. `onUpgrade` contém somente migrações legadas `<2` (tabela de fotos) e `<3` (colunas de centavos); não há nova migração nas alterações atuais. `BusinessPreferences` segue em `SharedPreferences` e é independente do banco. `RestoreTransaction` preserva o snapshot anterior da preferência e não trata entitlement. Nenhum arquivo de base/schema foi alterado.

Assim, no código estático, a V12 planejada mantém o mesmo `applicationId`, banco/schema e local de preferência; não há requisito de desinstalação, limpar dados ou recadastrar clientes/vendas. Uma atualização Android normal deve preservar IDs, relações, parcelas, pagamentos, fotos e valores. Para atualizar sobre a instalação Play, ainda é necessário `versionCode` maior e assinatura/upload corretos. Isto é análise estática, **não prova de upgrade instalado pela Google Play**. O teste de atualização só pode ser concluído quando a versão 12 estiver distribuída em Alpha e atualizada sobre uma instalação de teste V11.

A licença FULL_ACCESS é consultada pelo Billing e não é importada com backup. A compra, restauração, reconhecimento e entitlement reais da Google Play não foram exercitados nesta fase.

## 7. Funcionalidades comerciais e regressão de escopo

O diff revisado mantém FREE até 30 vendas, clientes ilimitados e CSV liberado; FULL_ACCESS é compra única no produto `full_access`, sem assinatura, com vendas ilimitadas e backup/restauração completos. Billing Library permanece 9.1.0; não há alteração de SKU/preço/consulta pelo usuário. Não foram encontrados no diff novo cadastro/login obrigatório, backend de vendas ou sincronização automática.

Clientes, vendas, parcelas, pagamentos parciais, saldos, relatórios, fotos, PDFs, CSV, WhatsApp e nome personalizado permanecem sem alteração de regra nesta consolidação. Falhas de licença/rede não podem apagar o banco. Esses itens têm cobertura unitária/instrumentada anterior conforme relatórios; esta fase não os reexecutou em aparelho.

## 8. Testes e builds desta execução

Comparação com a Fase 7A: unitários mantêm 43/43 por variante e lint mantém 0 erros/66 avisos em cada variante. Os XMLs atuais têm 0 falhas, 0 erros e 0 ignorados. Lint atual mantém 39 `SetTextI18n`, 10 `UseKtx`, 5 `IconLauncherShape`, 3 `ViewConstructor`, 2 `MonochromeLauncherIcon`, 2 `UseCompatTextViewDrawableApis` e um de cada `DataExtractionRules`, `DrawAllocation`, `ExifInterface`, `ObsoleteSdkInt` e `UsableSpace` por variante. A análise da Fase 4 explica a origem de 10 `UseKtx` adicionados pelo analisador então atualizado e 56 avisos anteriores; a contagem continua sem erros nesta execução. Não foi aplicada baseline/supressão.

| Verificação | Resultado Fase 7B |
|---|---|
| `:app:testDebugUnitTest` | Passou: 43 testes, 0 falhas/erros/ignorados |
| `:app:testReleaseUnitTest` | Passou: 43 testes, 0 falhas/erros/ignorados |
| `:app:lintDebug` | Passou: 0 erros, 66 avisos |
| `:app:lintRelease` | Passou: 0 erros, 66 avisos |
| `:app:compileDebugAndroidTestKotlin` | **BUILD SUCCESSFUL**; confirmou compilação das fontes instrumentadas. Não executou testes no runtime. |
| `:app:assembleDebug` | **Falhou** em `validateSigningDebug` por `AccessDeniedException` ao abrir `debug.keystore.lock` no Android user home dentro do workspace. A repetição com diretório de home Java/Android alternativo teve o mesmo bloqueio. Não é resultado de teste funcional. |
| `:app:assembleDebugAndroidTest` | Não concluiu empacotamento nesta execução; a tentativa agregada parou na falha de `validateSigningDebug`. Fontes compilam conforme a tarefa direcionada acima. |
| Testes instrumentados em emulador/aparelho | Não executados. |
| `bundleRelease` / AAB V12 | Não executados por regra da fase e bloqueio de compatibilidade. |
| `git diff --check` | Sem erro; avisos LF/CRLF mencionados na seção 2. |

O primeiro comando de teste foi interpretado incorretamente pelo wrapper batch ao passar `-Pkotlin.compiler.execution.strategy` e terminou antes de executar tarefas. A execução corrigida sem essa propriedade executou unitários/lint com sucesso e mais tarde falhou no lock de assinatura debug. Kotlin tentou escrever marcador em `%LOCALAPPDATA%\kotlin\daemon`, recebeu `AccessDeniedException` e usou fallback; tarefas unitárias completaram. O wrapper Gradle também emitiu aviso de métricas em `C:\.android` sem permissão. Isso restringe o ambiente e não foi contornado por mudanças de ACL.

Há saídas locais historicamente existentes em `app/build/`: dois AABs V11 de 5.320.483 bytes, ambos SHA-256 `d751f51dc69ac2da2784d841374288e4456bd666e2a5237c2e91c1ee9d47aaba`, datados de 12/10/2026. Não executei `bundleRelease` nem os sobrescrevi. APK debug/instrumentado presente no diretório de build é saída anterior; a falha acima significa que não afirmo um APK debug novo/concluído nesta fase.

## 9. Versionamento

**Não incrementado.** `app/build.gradle` continua `versionCode 11` e `versionName '1.0.11'`; `applicationId com.vendassimples.app`, min 24, compile/target 36. A versão candidata solicitada (12/1.0.12) fica para depois das pré-verificações. A tag e o commit V11 continuam disponíveis e intactos.

## 10. Revisão da assinatura

`app/build.gradle` carrega `VENDAS_KEYSTORE_PROPERTIES` ou `keystore.properties` fora do repositório. O arquivo de propriedades local, os campos necessários e o arquivo de keystore referenciado estão presentes; não foram impressos paths internos adicionais, aliases, senhas ou bytes da chave. Não acessei nem mostrei credenciais. Não gerei AAB e não extraí/publicizei fingerprint nesta fase.

Isso **não confirma** que a chave local seja a upload key aceita na Play Console. Com Play App Signing, o AAB enviado usa a upload key, enquanto Google assina os APKs distribuídos com a app signing key. A comparação exige evidência/certificados públicos da página App integrity/App signing da Console e verificação criptográfica do artefato final. A Console não foi acessada. Referência oficial: [Assinar seu app e Play App Signing](https://developer.android.com/studio/publish/app-signing).

Estado de assinatura: **configuração local presente; assinatura de release V12 não executada; correspondência Play desconhecida/bloqueada por falta de evidência**. APK de debug usa outra chave e não prova atualizabilidade da instalação V11 da Play.

## 11. Plano de commits — proposta, não executada

1. **Segurança e restauração:** `BackupManager.kt`, novos `BackupTransfer.kt`/`BackupValidator.kt`, `PrivacyPolicy.kt` e assets offline se mantidos. Rever cada arquivo e o limite de escopo antes de stage.
2. **Compatibilidade Android/toolchain:** manifesto, temas `values`/`values-v27`, `build.gradle`, `gradle.properties` e arquivos Wrapper. Esse commit depende de aprovar/resolver o KGP, reexecutar regressão e passar empacotamento debug.
3. **Testes e regressão:** testes unitários/instrumentados e scripts `tools/validar_*.py`; não incluir logs, fixtures reais ou cache.
4. **Documentação e política:** relatórios Fases 2–7B, segurança, README, decisões, segurança/privacidade e guia Pages. A página pública é fluxo separado; não misturar integração à branch Android com deploy do site sem revisar o PR/branch de política.
5. **Versionamento V12:** alteração isolada de `app/build.gradle` para 12/1.0.12, após gates técnicos e validação de disponibilidade do code no Console; nova regressão e artefato sob autorização.

Não usar commit único “tudo” sem revisão. Manter política/segredos/backups/apks/AABs, `.codex/`, `.worktrees/`, `.gradle-user/`, `.gradle/`, `app/build/` e arquivos `keystore.properties` fora de staging. Não fiz `git add` ou commit.

## 12. Notas propostas para Google Play Alpha

Rascunho baseado no código local revisado, não submetido:

> Melhoramos as verificações durante a restauração de backups e a recuperação em caso de falha. A Política de Privacidade também pode ser consultada dentro do aplicativo sem conexão. Fizemos ajustes de compatibilidade para notificações em versões recentes do Android.

Não usar afirmações como “backup inviolável”, “segurança absoluta”, “nenhuma coleta de dados” ou “testado em todos os dispositivos”. As notas só podem acompanhar uma versão após a correção do toolchain e validação do comportamento/artefato final.

## 13. Pendências de conformidade

1. **Google Play Data safety:** confirmar Console, todas as versões/faixas distribuídas e respostas vigentes. Não inventar ou aplicar respostas de uma tabela candidata.
2. **Billing 9.1.0/CCT:** fonte oficial específica ou evidência técnica controlada para payload/categorias/finalidades/destinos/retenção/opcionalidade. Nenhuma captura de rede nesta fase.
3. **Política pública:** status visual informado pelo usuário na Fase 6C, mas não confirmado via fetch/HTTP neste ambiente. O commit da política local continua em branch separada no snapshot; confirmar merge/deploy e comparar o texto servido com asset/site local antes de declarar paridade.
4. **API 24 e V12 runtime:** API 24 não está instalada conforme Fase 6B; sem execução nesta fase.
5. **FULL_ACCESS real:** compra, cancelamento, pendente, restauração de compra, disponibilidade do Billing e retenção do entitlement não testados pela Play nesta fase.
6. **Upgrade V11→V12:** teste via Google Play Alpha ainda não executado.
7. **Rastreabilidade do AAB Alpha:** confirmar na Console qual bundle/certificado está distribuído; os AABs locais de 12/10 não provam o conteúdo enviado.
8. **Homologação física V12:** os 13/13 da V11 foram informados pelo usuário no Samsung Android 16; não cobrem V12, backup, Billing ou upgrade.

## 14. Checklist antes de gerar AAB

- [ ] Aprovar a correção de compatibilidade KGP e manter AGP 8.9.1, Gradle 8.11.1, JDK adequado e compileSdk/targetSdk 36.
- [ ] Executar novamente testes debug/release, lint debug/release e revisar todos os avisos, sem baseline.
- [ ] Fazer `assembleDebug` e compilação/empacotamento de testes instrumentados passarem em ambiente estável; executar instrumentação em API 24 e Android recente.
- [ ] Rever migrations, backups 1/2, bancos com dados sintéticos, rollback, preferência e isolamento do entitlement.
- [ ] Fechar a revisão Data safety/Billing e confirmar conteúdo/link público da política.
- [ ] Confirmar com o responsável a disponibilidade de versionCode 12 na Console.
- [ ] Incrementar somente então para `12 / 1.0.12` em alteração rastreável e repetir as validações afetadas.
- [ ] Confirmar configuração de upload signing sem imprimir secrets; comparar certificado público SHA-256 com upload certificate no Console. Distinguir upload key da app signing key Google.
- [ ] Gerar AAB somente após autorização final. Registrar tamanho, SHA-256, `bundletool validate`, identidade, versionamento, manifest/SDK, certificado e proveniência.

## 15. Checklist antes do upload Alpha

- [ ] Responsável confirma o AAB local e SHA-256, `applicationId`, code/name, SDKs e certificado de upload.
- [ ] Revisar versão atualmente ativa, política/Data safety e status da Console; corrigir respostas sob responsabilidade do desenvolvedor.
- [ ] Confirmar que Play Console reconhece produto INAPP `full_access`/oferta `full-access`, sem introduzir assinatura.
- [ ] Conferir release notes, formulário de conteúdo, permissões, política e regiões/faixa.
- [ ] Não substituir instalação V11 com APK debug; subir o AAB correto à Alpha apenas com autorização separada.
- [ ] Guardar evidência de processamento/track; upload não equivale a aprovação/disponibilidade.

Nenhum item foi realizado por esta fase e nenhum acesso/modificação no Console ocorreu.

## 16. Plano de teste V11 → V12

Executar somente depois da publicação Alpha aprovada, em aparelho/conta de teste sem dados reais:

1. Confirmar V11 e origem Google Play; inventariar contagens sintéticas, relações cliente/venda/parcela, pagamentos e totais, fotos, nome e estado FULL_ACCESS sem coletar dados pessoais.
2. Fazer backup de recuperação se autorizado e registrar que backup não inclui licença. Usar dados sintéticos, não a instalação operacional do Samsung.
3. Atualizar pela própria Google Play para V12 com `versionCode` maior e assinatura correta; não desinstalar, limpar dados nem instalar APK debug.
4. Conferir que o banco permanece schema 3, IDs/parcelas/pagamentos/fotos/totais/nome persistem e a consulta de compra conserva entitlement. Não restaurar um backup sobre os dados da validação de upgrade.
5. Reabrir app após reinício; testar política offline, notificações conforme permissão, CSV/PDF/WhatsApp e operação offline.
6. Separadamente testar backup/restore FULL_ACCESS em instalação sintética, formats 1/2, banco corrompido e rollback; verificar que FREE não obtém backup ou licença por restauração.
7. Registrar PASSOU/FALHOU/BLOQUEADO/NÃO EXECUTADO, aparelho/API, bundle/code e evidência sanitizada. Parar em qualquer divergência e preservar o estado para investigação.

Este plano não afirma que V11→V12 já foi testado.

## 17. Estado final do Git

Branch, `HEAD`, tag V11 e referências locais não mudaram. Não houve stage, commit, push, merge, nova tag ou limpeza. Nenhum código de produção foi alterado nesta Fase 7B. A árvore permanece deliberadamente suja com o trabalho prévio; novo arquivo desta fase: `docs/FASE7B_PREPARACAO_RC_V12.md`. Caches/build e os dois AABs antigos são locais ignorados; não foram criados AABs novos.

## 18. Recomendação final e ponto de aprovação

**NÃO APTO PARA GERAR AAB V12 ainda.** A evidência oficial não dá suporte pleno ao Kotlin 1.9.24 com as versões AGP/Gradle que compileSdk 36 exige. A correção mínima proposta é atualizar e fixar KGP `2.2.20` (após decisão do responsável), então repetir testes/builds e confirmar a assinatura de upload contra a Console. O `assembleDebug` também precisa passar em ambiente que permita o lock do keystore debug. Nenhum desses bloqueios foi mascarado.

Após resolver compatibilidade e empacotamento, o status poderá ser reavaliado para **APTO COM RESSALVAS** à geração de AAB, ainda condicionado à autorização explícita do AAB. A V12 não está homologada em aparelho físico e não está pronta para produção. Próximo passo recomendado: aprovar ou rejeitar a atualização do KGP proposta; até essa decisão, permanecem congelados versionCode 11 e a preparação de artefato.
