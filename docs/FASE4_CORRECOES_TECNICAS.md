# Fase 4 — lint, compatibilidade e regressão

Início: 15/10/2026; conclusão e retomada: 16/10/2026 (America/Sao_Paulo). Branch `fix/v12-seguranca-conformidade`. Trabalho autorizado: corrigir os três erros de lint, atualizar somente as ferramentas necessárias à API 36 e repetir a regressão. Sem commit, push, tag, alteração de versão, AAB ou publicação. A política pública e a Play Console não foram modificadas.

## 1. Estado inicial e preservação

HEAD e tag `v1.0.11-rc.1`: `8aa34881755f222f9b090691aaa6127b31b1a363`. A árvore já continha as alterações não commitadas das Fases 2/3 e a confirmação da URL oficial. Foram preservadas, sem reset, checkout, stash ou limpeza. Snapshot de hashes e estado inicial em `.gradle-user/fase4/initial-files.json` e `initial-status.txt`. Os relatórios anteriores continuam como evidência histórica, sem reescrever seus resultados.

Leituras: AGENTS, README, decisões, documentação de segurança/monetização/regras financeiras/validação, relatórios integrais das Fases 2/3 e investigação de dados do Billing. Aplicadas as skills locais de regressão, segurança, interface, backup, validação e release.

Versão preservada: **11 / 1.0.11**, applicationId **com.vendassimples.app**, minSdk **24**, compileSdk/targetSdk **36**, Billing **9.1.0**, produto INAPP **full_access**. Não houve mudança de schema SQLite, regras financeiras, FREE 30 ou FULL_ACCESS.

## 2. Causas e correções de lint

### NewApi

O tema base usava `android:windowLightNavigationBar`, disponível somente na API 27. Foi criado `AppThemeBase` com todos os atributos comuns, `AppTheme` base herdando esse estilo e variante `values-v27/styles.xml` acrescentando somente o atributo API 27+. A herança explícita preserva cores, fontes, tema dos diálogos e todos os atributos comuns; não se criou uma variante parcial que perdesse o tema base.

APIs 24–26 usam o tema comum, sem o atributo indisponível. APIs 27+ mantêm a barra clara com ícones escuros. Não foi aumentado o minSdk nem aplicada supressão. Execução física/API 24 segue separada da análise estática e da compilação.

### NotificationPermission

O app realmente utiliza notificações locais de vendas vencidas/vencendo hoje, emitidas ao carregar/atualizar os dados enquanto está em uso. O código já criava canal em API 26+, mas chamava notify sem declarar/verificar POST_NOTIFICATIONS em API 33+. Além do bloqueio dos avisos, uma exceção nessa chamada podia impedir o callback de atualização da tela e ser apresentada como erro de banco.

Correção:

- Declarada somente a permissão de notificações necessária ao recurso existente.
- Verificação explícita de versão/permissão imediatamente antes da emissão. API 24–32 mantém o fluxo anterior; em API 33+ a emissão aguarda autorização.
- Solicitação runtime apenas por ação explícita **Ativar avisos de vencimento**, no menu existente quando API 33+ está sem permissão. Não há solicitação automática ao abrir o app, ler relatório, restaurar backup ou consultar a política. A ação está disponível independentemente da licença.
- Se concedida, os avisos atuais são atualizados; se negada, uma mensagem informa o estado sem bloquear dados ou insistir automaticamente. As preferências de canal/sistema continuam sob controle do Android.
- `atualizarAvisosSemInterromperTela` isola falhas de emissão; o callback do relatório continua após a leitura bem-sucedida. O log usa mensagem fixa, sem SQL, dados pessoais ou conteúdo da exceção.

Não foram adicionados agendamento, serviço em segundo plano, alarmes exatos, backend ou promessa de notificação confiável com o app fechado. A permissão é pertinente porque o recurso existe; não foram solicitadas câmera, contatos, localização ou armazenamento amplo.

### SuspiciousIndentation

`setTextColor/when` em etiquetaStatus estava desalinhado após `typeface`. Ambos já eram incondicionais dentro de apply. Somente a indentação foi corrigida: expressões, classificadores, rótulos, cores e regras de quitação/vencimento/pagamento parcial foram preservados.

## 3. Ferramentas e compatibilidade

| Item | Antes | Depois / motivo |
| --- | --- | --- |
| AGP | 8.5.2 | **8.9.1**, mínimo indicado na tabela oficial para API 36 |
| Gradle | 8.7 | **8.11.1**, mínimo exigido pela linha AGP 8.9 |
| Wrapper | Arquivos da versão anterior | JAR e scripts regenerados pela tarefa oficial wrapper; distribuição com SHA-256 oficial fixado |
| JDK usado | Android Studio JBR 21.0.10 | Mantido; Gradle 8.11.1 suporta execução com Java 21, AGP exige ao menos 17 |
| Kotlin/KGP | 1.9.24 | Mantido, sem migração de linguagem/compilador principal |
| SDKs e Billing | 24 / 36 / 36; Billing 9.1.0 | Mantidos |
| Ocultação de aviso SDK | android.suppressUnsupportedCompileSdk=36 | Removida; a nova configuração deve ser verificada sem ocultar o aviso |

SHA-256 da distribuição Gradle 8.11.1 bin: `f397b287023acdba1e9f6fc5ea72d22dd63669d59ed4a289a29b1a76eee151c6`, consultado em services.gradle.org e configurado em distributionSha256Sum. Nenhuma biblioteca funcional foi atualizada indiscriminadamente, nem introduzida arquitetura nova.

**Ressalva Kotlin:** a tabela do fornecedor lista KGP 1.9.20–1.9.25 como plenamente testado até Gradle 8.1.1/AGP 8.1.0 e permite usar versões posteriores com ressalvas. Essa limitação já existia no conjunto anterior. Não se afirma suporte integral da matriz Kotlin apenas porque compila. Foi preservado o compilador atual para evitar migração ampla não necessária às correções; os resultados locais abaixo documentam a combinação exercitada. Uma atualização principal de Kotlin exigiria proposta e regressão próprias, não foi feita silenciosamente.

Fontes oficiais consultadas: [mínimos AGP/API](https://developer.android.com/build/releases/about-agp), [AGP 8.9 e requisitos](https://developer.android.com/build/releases/agp-8-9-0-release-notes), [Gradle 8.11.1/JDK](https://docs.gradle.org/8.11.1/userguide/compatibility.html), [Kotlin/Gradle/AGP](https://kotlinlang.org/docs/gradle-configure-project.html) e [permissão de notificações](https://developer.android.com/develop/ui/compose/notifications/notification-permission). A tabela geral do Android indica 8.9.1 para API 36; não confundir esse patch mínimo com 8.9.0.

## 4. Arquivos alterados nesta fase

- `MainActivity.kt`: permissão/ação explícita de avisos, guard de emissão, isolamento de falha e indentação.
- `app/src/main/AndroidManifest.xml`: POST_NOTIFICATIONS.
- `app/src/main/res/values/styles.xml` e novo `values-v27/styles.xml`: tema compatível com API 24.
- `build.gradle`, `gradle.properties`, `gradle/wrapper/gradle-wrapper.properties`, `gradle-wrapper.jar`, `gradlew` e `gradlew.bat`: atualização mínima e remoção da ocultação de compileSdk.
- `FinalUiInstrumentedTests.kt`: cenário real de callback de relatório e emissão/ausência de notificação conforme permissão; parcela quitada não emite aviso. Dados sintéticos somente em memória, sem gravar vendas.
- Este relatório e link no README.

Os novos arquivos e alterações das Fases 2/3 permanecem na árvore. A lista acima descreve apenas o incremento da Fase 4, não atribui a esta fase o validador de backup ou a política local já existentes.

## 5. Comandos e ocorrências

Ambiente: JDK/SDK locais, GRADLE_USER_HOME no repositório, assinatura configurada externamente sem leitura/exposição de credenciais. Execução limitada a dois workers e heap de 1536 MiB; emulador separado da compilação no host de 8 GB.

```text
gradlew.bat wrapper --gradle-version 8.11.1 --distribution-type bin
  --gradle-distribution-sha256-sum f397b287023acdba1e9f6fc5ea72d22dd63669d59ed4a289a29b1a76eee151c6
gradlew.bat test assembleDebug assembleDebugAndroidTest lintDebug lintRelease
  --continue --no-daemon --max-workers=2 -Pkotlin.compiler.execution.strategy=in-process
```

Na primeira atualização, a tarefa Gradle wrapper concluiu BUILD SUCCESSFUL em 2m47s e regenerou os arquivos. O batch Windows em execução foi reescrito e terminou depois com comando truncado/código 9009. Esse retorno não foi contado como aprovação integral do comando. A execução posterior usa os arquivos regenerados e verifica o wrapper pelo build real. Log: wrapper-update.log.

Houve uma interrupção externa por limite de uso da revisão automática de aprovação, que impediu uma consulta diagnóstica jcmd ao processo Gradle. A consulta não foi executada e não era necessária à implementação. O usuário solicitou retomada; o estado foi conferido e os testes prosseguiram, sem contornar a aprovação.

A primeira execução completa com AGP novo terminou em 10m40s com falha somente em compileDebugUnitTestKotlin: Gradle não conseguiu mover um workspace temporário de transformação para sua localização imutável no cache Windows. Não houve erro de sintaxe/tipo Kotlin; produção, APKs e lint avançaram. O retorno foi registrado como falha, não como aprovação. Foi feita repetição offline com --max-workers=1, sem apagar caches/arquivos, para conferir o resultado final (build-confirm.log).

A repetição do comando completo terminou **BUILD SUCCESSFUL em 4m08s**, com 13 tarefas executadas e 89 atualizadas. Foram executados/conferidos os testes das duas variantes e os APKs/lints solicitados, sem falhas remanescentes. Resultados release já executados na primeira rodada foram reutilizados como up-to-date; não se contabilizam como uma segunda execução física dos mesmos testes. A falha do workspace não se repetiu.

Não foram executadas tarefas assembleRelease/bundleRelease, scripts que copiam/substituem o AAB histórico, alteração da Play Console, commit ou publicação.

## 6. Resultados e comparação de lint

Resultados finais desta fase. Evidências locais ignoradas em `.gradle-user/fase4/`; as contagens abaixo não reutilizam resultados da Fase 3 como se fossem testes novos.

| Verificação | Antes | Fase 4 |
| --- | --- | --- |
| Lint debug | 3 erros, 56 avisos | **0 erros, 66 avisos** |
| Lint release | 3 erros, 56 avisos | **0 erros, 66 avisos** |
| Unitários debug/release | 43 por variante na Fase 3 | **43 por variante, zero falhas/erros/skips** |
| APK debug / instrumentado | Fase 3 disponível | **Build aprovado**, versão 11/1.0.11 |
| Backup, segurança e reinício | 38 + 1 na Fase 3 | **38 + 1 aprovados**, 148,7 s; banco/preferência originais preservados |
| Interface nativa em quatro configurações | 48 execuções na Fase 3 | **52 execuções aprovadas**, 13 testes por configuração, 204,3 s |
| Privacidade pelo menu | Evidência histórica | **7 verificações aprovadas**, 54,2 s, offline |
| Vendas, filtros, detalhes, fotos e pagamento cancelado | Evidência histórica | **10 verificações aprovadas**, 103,7 s |
| Avisos negados/concedidos | Sem cobertura runtime específica anterior | **13 testes nativos negados + 13 concedidos**, além de 10 verificações do fluxo/restauração |
| CSV por parser | Evidência histórica | **Aprovado: 7 registros, 8 colunas, UTF-8 com/sem BOM, textos/datas/valores exatos** |

A comparação de avisos deve considerar ID/mensagem/arquivo, sem confundir deslocamento de linha com aviso novo. Separar avisos de código novo, avisos antigos e diagnósticos novos do analisador AGP atualizado. Não considerar lint aprovado se existir qualquer erro remanescente; não foi criada baseline nem supressão genérica.

Regressão Android exclusivamente no AVD Build10, API 35/Android 15, namespace sintético de backup e fixture operacional sintética preservada. Interface repetida em configuração normal, 320 dp/fonte 150%, 320 dp/fonte 200% e paisagem/fonte 200%; o teste novo eleva a suíte de 12 para 13 métodos. Logs separados em fase4/backup e fase4/views, sem sobrescrever evidências anteriores. Comandos: `python tools/validar_backup_v11.py --adb <SDK>/platform-tools/adb.exe --serial emulator-5556 --output-dir .gradle-user/fase4/backup` e `python tools/validar_ajustes_finais_v11.py --serial emulator-5556 --views-only --output-dir .gradle-user/fase4/views`; demais modos --privacy-only e --sales-only usam diretórios próprios. CSV: `python tools/validar_csv_v11.py`.

O wrapper de interface aprovou seis verificações, incluindo preservação; política aprovou sete e vendas aprovou dez. A suíte nativa foi executada em seis rodadas: quatro configurações de tela e duas rodadas de permissão explícita, totalizando 78 execuções dos 13 métodos (não são 78 métodos distintos).

Automação adicional `python .gradle-user/fase4/test_notifications.py`: confirmou ausência de solicitação automática ao abrir; acionou Ativar avisos pelo menu, negou no diálogo real do Android, conferiu acesso à tela e executou 13 testes com permissão negada. Depois repetiu a ação, concedeu no diálogo real, conferiu que a ação de solicitar some e executou 13 testes com permissão concedida. Verificados callback, notificação ativa para parcela parcial vencida e ausência para quitada. Logs notifications/notifications-denied.log e notifications-granted.log, capturas XML/PNG e dez verificações em notifications/results.json. É automação no emulador, não homologação manual física.

Banco e identidade coincidiram byte a byte com os baselines antes/depois da suíte principal e do teste adicional. As decisões anteriores de permissão (concedida/negada, user-set/user-fixed), Wi-Fi e dados móveis foram restauradas. A conferência final aprovou cinco condições em runtime-final-preservation.json, e o AVD isolado foi encerrado. Nenhuma falha de aplicativo ou teste permaneceu nessa regressão.

Comparação XML por variante: os 56 avisos anteriores permanecem semanticamente; a mensagem de um ObsoleteSdkInt ganhou apenas formatação com crases. O analisador atualizado acrescentou **dez UseKtx** em trechos existentes: três SharedPreferences.edit em BusinessPreferences, quatro SQLiteDatabase.transaction em LocalDatabase, dois String.toUri e um createBitmap em MainActivity. Nenhum desses trechos foi criado/modificado pelas correções da Fase 4. São recomendações de conveniência de APIs, não justificativa para refatorar persistência nesta tarefa. Não foram identificados avisos nos novos guards, solicitação explícita ou tema qualificado. Delta bruto com mensagens/arquivos em lint-debug-delta.json e lint-release-delta.json; XMLs anteriores/posteriores preservados.

## 7. Compatibilidade e regressões

API 24: tema comum sem atributo API 27; código de permissão 33+ protegido por versão, fluxo legado mantido. API 36: compilação/target preservados, AGP no mínimo oficial, notificações dependentes de autorização. Não confundir compatibilidade estática com execução real em cada API: os dispositivos/imagens efetivamente utilizados serão registrados nos resultados.

Execução runtime realizada **somente em API 35** nesta fase. API 24 e API 36 foram verificadas por configuração, recursos/guards e lint/compilação aplicáveis; não houve execução dessas duas APIs, instalação de novas imagens ou homologação física. Esses testes continuam no checklist manual da Fase 3.

Banco, cálculos, parcelamento, pagamentos, fotos, PDFs, CSV, WhatsApp, personalização, Billing, FREE 30, segurança de backups e texto local da política não receberam mudanças de implementação nesta fase. As alterações da Activity estão limitadas aos avisos e à indentação. A verificação final compara hashes contra o snapshot e o AAB contra o SHA original.

Comparação auxiliar de 22 métodos de financeiro/filtros/vendas/pagamentos/fotos/PDF/CSV/WhatsApp/personalização contra 8aa3488, desconsiderando espaços/indentação, não apontou alterações (protected-methods.json). Foi combinada à leitura do diff; não é prova de execução de todos esses fluxos em aplicativos externos. A suíte de backup repetiu compatibilidade 1/2, fotos/pagamentos/IDs/nome, bloqueio FREE e rejeições adversariais, com checks de preservação.

Não serão declarados testes físicos, compras reais, SAF remoto, PDFs/WhatsApp em aplicativos externos ou atualização assinada pela Play sem execução comprovada. O checklist da Fase 3 continua aplicável para esses fluxos.

## 8. Riscos e pendências

Permanecem, independentemente do lint: reconciliação da política pública versus local (divergência registrada na verificação de 15/10), confirmações de Segurança dos dados/Billing e formulário, rastreabilidade do upload Alpha, homologação API 24/API 36/celular/compra e disponibilidade de restauração síncrona/arquivos volumosos. Não foram modificadas a política pública nem suas declarações. KGP permanece fora da faixa plenamente testada documentada; a combinação local passou, sem alegar garantia universal. Os 66 avisos por variante estão visíveis e não foram suprimidos. A falha inicial de workspace Windows deve ser observada em futuras execuções; repetir com um worker resolveu esta ocorrência, sem promessa de eliminar toda falha de cache.

## 9. Estado final e artefatos

Os dois AABs V11 originais permanecem com SHA-256 `d751f51dc69ac2da2784d841374288e4456bd666e2a5237c2e91c1ee9d47aaba`; não incluem as correções desta fase. Nenhum novo AAB ou APK release foi gerado. APK debug para homologação: `app/build/outputs/apk/debug/app-debug.apk`, assinatura debug e versão 11/1.0.11. Não substitui uma atualização Play com assinatura compatível.

Comparação final contra initial-files.json: somente os 11 arquivos existentes enumerados na seção 4 foram alterados, e foram acrescentados este relatório e values-v27/styles.xml. O incremento contém 13 arquivos. Segurança de backups, preferências, dados/monetização, política local/site, relatórios anteriores e demais alterações locais foram preservados. HEAD/tag continuam em 8aa3488 e o índice Git permanece sem alterações staged. Evidência em final-preservation.json; links locais e git diff --check conferidos. Sem commit, push, tag ou publicação.

## 10. Recomendação

**APTO PARA HOMOLOGAÇÃO CONTROLADA, COM RESSALVAS.** Os três erros foram corrigidos, o comando completo de testes/build debug/lint passou e a regressão automatizada não apresentou falhas remanescentes. **Não é aprovação para produção nem autorização para gerar candidata V12.** Concluir as pendências externas e o checklist físico/API 24/API 36/Play; revisar as alterações e autorizar explicitamente qualquer próximo commit, incremento, AAB, tag ou publicação.
