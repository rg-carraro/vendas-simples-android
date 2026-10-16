# Fase 6B — Homologação Android e regressão

Data: 16/10/2026. Repositório `rg-carraro/vendas-simples-android`. Aplicativo `com.vendassimples.app`. Versão de trabalho `versionCode 11` / `versionName 1.0.11`.

## 1. Resumo executivo

| Área | Resultado |
|---|---|
| Testes unitários | **APROVADO:** 43/43 em debug e 43/43 em release, sem falhas, erros ou testes ignorados. Reexecutados nesta fase. |
| Build e testes Android | **APROVADO:** `assembleDebug` e compilação de `assembleDebugAndroidTest`; os testes instrumentados não foram executados em dispositivo. |
| Lint | **APROVADO:** zero erros e 66 avisos por variante (debug/release). A contagem coincide com a Fase 5; não foi feita comparação item a item dos 66 avisos nesta fase. |
| APK | Debug atualizado, assinatura Android Debug verificada. Não é o AAB nem uma atualização compatível com a instalação Play. |
| Ambiente Android | **BLOQUEADO para runtime:** nenhum AVD válido foi listado; a pasta `Pixel_9.avd` existente não tem `config.ini` nem um `.ini` de AVD. O comando ADB falhou antes de listar dispositivos. |
| Política pública | **APROVADO por HTTP:** status 200, UTF-8; data pública 15/10/2026 e texto visível da seção principal igual ao local. O fluxo de abertura pelo aplicativo em dispositivo não foi testado. |
| Integridade de release | A tag continua apontando para `8aa34881755f222f9b090691aaa6127b31b1a363`; os dois AABs mantêm SHA-256 `d751f51dc69ac2da2784d841374288e4456bd666e2a5237c2e91c1ee9d47aaba`. |

**Recomendação: APTO COM RESSALVAS para continuar a preparação técnica da V12.** Não considerar homologação de dispositivo concluída. Antes de distribuir uma V12, executar o roteiro físico/API 24, teste real de upgrade pela faixa Play e validação de compra/restauração em conta de teste autorizada. Não houve mudança de código de produção, versão, tag, AAB, Console ou publicação.

## 2. Estado Git e preservação

No início, a branch ativa era `fix/v12-seguranca-conformidade`, `HEAD` era `8aa34881755f222f9b090691aaa6127b31b1a363`, e a tag `v1.0.11-rc.1` resolvia para esse mesmo commit. A contagem inicial de `git status --short` era 34 entradas: 19 arquivos modificados e 15 arquivos/diretórios não rastreados, incluindo trabalho prévio das Fases 2–5 e as worktrees auxiliares da Fase 6A. Nenhuma dessas alterações foi descartada.

Nesta fase, não houve edição Kotlin/Java, Gradle de produto, manifesto ou configuração comercial. O único arquivo novo de documentação é este relatório; ele permanece **não commitado**. Um perfil Android temporário criado para a tentativa de inventário do ADB gerou um `debug.keystore`; após confirmar que o diretório continha somente o perfil/cache gerado nesta fase, o diretório foi removido para não deixar uma chave local no workspace. O processo ADB observado já existia desde 11/10 e não foi encerrado. A pasta de build/caches é local. Não houve `git add`, commit, push, tag ou operação de release. O APK debug foi gerado em `app/build/outputs/apk/debug/app-debug.apk`; os AABs históricos não foram reconstruídos nem sobrescritos.

## 3. Inventário de ambientes

| Item | Observação desta sessão |
|---|---|
| Android SDK | `<ANDROID_SDK>`. |
| Plataformas | `android-35`, `android-36`, `android-36.1`; API 24 não está instalada. |
| Imagens de sistema | API 35 (`google_apis_playstore_ps16k`, x86_64) e API 37.0. Não há imagem API 24 ou API 36. |
| Emulador | `emulator.exe` presente, mas `emulator -list-avds` não listou AVD. `<ANDROID_USER_HOME>\avd\Pixel_9.avd` contém somente `version_num.cache`; faltam a configuração do AVD e o arquivo `.ini`. Não foi inicializado nem alterado. |
| ADB | `adb.exe` presente. `adb devices -l` encerrou com `Cannot mkdir '\.android': Permission denied` antes de exibir a lista. O processo ADB observado era anterior a esta fase e foi preservado. Portanto o inventário de dispositivos físicos não é conclusivo e nenhum dispositivo foi considerado testado. |
| Java | JDK 21 do Android Studio em `C:\Program Files\Android\Android Studio\jbr`; `JAVA_HOME` foi definido somente nos comandos desta execução. |
| Ambiente A — API 24 | Não disponível; plataforma e imagem de sistema ausentes, sem instalação adicional nesta fase. |
| Ambiente B — Android recente | Imagem API 35 instalada, mas sem AVD válido. Não houve execução em runtime. |
| Ambiente C — telefone físico | Nenhum aparelho foi confirmado pelo ADB; testes físicos pendentes. |

### Instruções para criar ambientes

**AVD API 24:** em Android Studio, abrir *Tools → Device Manager → Create Device*, escolher um perfil de telefone, selecionar a imagem Android Nougat/API 24 compatível (x86_64, se oferecida), instalar a imagem pelo SDK Manager e concluir com o nome `Fase6B_API24`. Essa imagem não foi instalada nesta sessão.

**AVD API 35:** no mesmo Device Manager, criar um perfil de telefone com a imagem API 35 já instalada (`Google Play x86_64, 16 KB page size`, se compatível com o Android Studio local). Criar com nome `Fase6B_API35`; não reutilizar nem limpar um AVD com dados existentes.

**Telefone físico:** habilitar opções do desenvolvedor e depuração USB em aparelho de teste, conferir `adb devices -l`, confirmar origem/assinatura do app e não instalar o APK debug sobre uma instalação Play com dados importantes. Usar cliente e vendas sintéticos.

## 4. APK de homologação

Build: `:app:assembleDebug` com Gradle Wrapper 8.11.1 e JDK 21.

| Campo | Valor verificado |
|---|---|
| Arquivo | `app/build/outputs/apk/debug/app-debug.apk` |
| Tamanho | 7.256.135 bytes |
| SHA-256 | `e4a0e11b97ba45349566ce2c0f0141214c3021a4915c59e32e1ae1c8537e7513` |
| Gerado | 16/10/2026, 12:07 (horário local do ambiente) |
| Assinatura | `apksigner verify` aprovado; certificado **Android Debug**, esquema APK v2. Não é chave Play. |
| Identidade/versão | `com.vendassimples.app`, `versionCode 11`, `versionName 1.0.11` |
| SDK | `minSdk 24`, `compileSdk 36`, `targetSdk 36` extraídos do APK. |
| Billing | Manifesto declara versão da Billing Library `9.1.0`; código mantém produto `full_access` e opção `full-access`. |
| Backup e provider | `allowBackup=false`; FileProvider `com.vendassimples.app.provider`, não exportado, com concessão temporária de URI. |
| Permissões mescladas observadas | `POST_NOTIFICATIONS`, `com.android.vending.BILLING`, permissão interna de receiver, `ACCESS_NETWORK_STATE` e `INTERNET`. |
| Política offline | `assets/privacy_policy.html` está presente no APK. |

Este APK é somente para homologação isolada. Não corresponde ao AAB histórico da V11 publicado/testado pela Play e não comprova atualização de assinatura Play, compra ou restauração de compra.

## 5. Testes automatizados e comandos

O Wrapper exigiu baixar Gradle 8.11.1; a primeira tentativa sem rede foi bloqueada. Após obter a distribuição oficial, duas execuções paralelas falharam ao mover transformações temporárias no cache. Sem limpar o cache, a configuração serial (`--max-workers=1`) passou. A falha observada foi do ambiente/cache Gradle, não um teste de produto reprovado.

Comandos relevantes (o `JAVA_HOME` apontou para o JDK embutido; `GRADLE_USER_HOME` isolado em `.gradle-user/fase6b`):

```text
.\gradlew.bat --no-daemon --max-workers=1 --console=plain :app:assembleDebug
.\gradlew.bat --no-daemon --max-workers=1 --console=plain :app:testDebugUnitTest :app:testReleaseUnitTest :app:lintDebug :app:lintRelease
.\gradlew.bat --no-daemon --max-workers=1 --console=plain --rerun-tasks :app:testDebugUnitTest :app:testReleaseUnitTest :app:assembleDebugAndroidTest
```

| Tarefa | Resultado | Cobertura/limite |
|---|---|---|
| `testDebugUnitTest` | **43 aprovados, 0 falhas/erros/ignorados** | Executado novamente nesta fase. Inclui CSV, backup, restauração transacional, política, dinheiro, identidade, compatibilidade e percentuais. |
| `testReleaseUnitTest` | **43 aprovados, 0 falhas/erros/ignorados** | Executado novamente nesta fase. |
| `assembleDebug` | **BUILD SUCCESSFUL** | APK debug gerado; sem bundle release. |
| `assembleDebugAndroidTest` | **BUILD SUCCESSFUL** | Compilou/empacotou testes instrumentados; não os executou em dispositivo. |
| `lintDebug` | **0 erros, 66 avisos** | IDs `NewApi`, `NotificationPermission` e `SuspiciousIndentation` não aparecem nos relatórios atuais. |
| `lintRelease` | **0 erros, 66 avisos** | Mesma contagem observada na Fase 5; comparação aviso a aviso com os XML históricos não foi refeita. |
| ADB/instrumentação | **BLOQUEADO** | ADB não chegou a listar dispositivos; sem AVD válido. |

Avisos Kotlin de depreciação/uso não aplicado apareceram durante compilação (`Locale`, `SOFT_INPUT_ADJUST_RESIZE`, parâmetro não usado), sem erro. Não foram usadas supressões nem alterado código para fazer os checks passarem.

### Cobertura unitária observada

Os 43 testes cobrem: CSV em UTF-8, escaping, acentos e parser; centavos, pagamentos parciais e saldos; backup versionado sem entitlement; cópia limitada e arquivos truncados; recuperação transacional e preferência do nome; política offline/URL; compatibilidade FREE/FULL_ACCESS e estados indisponível/pendente do Billing; percentuais do gráfico incluindo zero e extremos; nome/data do negócio.

Os testes Android de backup/FULL_ACCESS, registros financeiros/fotos, instalação/reinício e interface foram **compilados**, não executados. O teste unitário não substitui teste SQLite/SAF completo na UI nem compra de Billing pela Play.

## 6. Matriz de cenários por dispositivo

Para os cenários abaixo, o ambiente pretendido é A (API 24), B (API 35 recente) ou C (telefone físico). Nenhum estava utilizável para instalação. `BLOQUEADO` significa que o cenário exige Android runtime e não pôde ser executado. Testes unitários correlatos são anotados, mas não mudam o status do teste por dispositivo.

| ID | Passo e esperado | Obtido / evidência | Status |
|---|---|---|---|
| A01 | Instalar limpo e abrir sem erro | Sem AVD válido; ADB falhou antes de listar dispositivos. | BLOQUEADO |
| A02 | Abrir aplicativo e navegar na tela inicial | Sem ambiente Android executável. | BLOQUEADO |
| A03 | Fechar e reabrir; manter dados locais | Sem ambiente Android executável. | BLOQUEADO |
| A04 | Reiniciar dispositivo/AVD e abrir | Sem ambiente Android executável. | BLOQUEADO |
| A05 | Confirmar persistência de clientes/vendas | Sem banco de teste em dispositivo. | BLOQUEADO |
| A06 | Confirmar fluxo offline | Unit tests da política passaram; operação offline da UI não foi exercitada nesta fase. | BLOQUEADO |
| B01 | Cadastrar cliente sintético | Sem ambiente Android executável. | BLOQUEADO |
| B02 | Editar cliente e conferir persistência | Sem ambiente Android executável. | BLOQUEADO |
| B03 | Registrar venda à vista | Sem ambiente Android executável. | BLOQUEADO |
| B04 | Registrar venda parcelada | Regras unitárias de centavos passaram; fluxo visual não executado. | BLOQUEADO |
| B05 | Registrar pagamento parcial | Regras unitárias de pagamento parcial passaram; UI não executada. | BLOQUEADO |
| B06 | Conferir pago e saldo a receber | Regras unitárias passaram; relatório real no app não conferido. | BLOQUEADO |
| B07 | Editar venda de teste | Sem ambiente Android executável. | BLOQUEADO |
| B08 | Excluir venda sintética e dependências | Sem ambiente Android executável. | BLOQUEADO |
| B09 | Conferir histórico após edição/exclusão | Sem ambiente Android executável. | BLOQUEADO |
| B10 | Conferir filtros e relatórios | Sem ambiente Android executável. | BLOQUEADO |
| C01 | Selecionar foto da galeria | Sem galeria/ambiente Android. | BLOQUEADO |
| C02 | Capturar foto pela câmera | Sem câmera/telefone físico. | BLOQUEADO |
| C03 | Visualizar foto vinculada | Sem ambiente Android executável. | BLOQUEADO |
| C04 | Remover foto mantendo original conforme fluxo | Sem ambiente Android executável. | BLOQUEADO |
| C05 | Salvar venda com foto | Sem ambiente Android executável. | BLOQUEADO |
| C06 | Reiniciar e confirmar foto persistida | Sem ambiente Android executável. | BLOQUEADO |
| C07 | Negar permissão e conferir comportamento | Sem diálogo de permissão em runtime. | BLOQUEADO |
| D01 | Exportar CSV | 9 testes unitários do exportador passaram; exportação real pela UI não foi feita. | BLOQUEADO |
| D02 | Conferir conteúdo e valores CSV | Parser/escaping/Unicode unitários passaram; arquivo de UI não gerado. | BLOQUEADO |
| D03 | Gerar PDF | Sem ambiente Android executável. | BLOQUEADO |
| D04 | Abrir PDF em aplicativo externo | Sem visualizador externo/dispositivo. | BLOQUEADO |
| D05 | Compartilhar documento pelo Android | Sem chooser Android. | BLOQUEADO |
| D06 | Abrir cobrança pelo WhatsApp | Sem WhatsApp/dispositivo de teste. | BLOQUEADO |
| D07 | Conferir valores, datas e nome na mensagem | Identidade unitária passou; fluxo de mensagem não testado. | BLOQUEADO |
| D08 | Testar ausência de WhatsApp | Sem ambiente Android. | BLOQUEADO |
| E01 | Configurar nome do negócio | Identidade/preferência unitária passou; UI não testada. | BLOQUEADO |
| E02 | Conferir nome na tela inicial | Sem ambiente Android. | BLOQUEADO |
| E03 | Conferir nome no PDF | Sem ambiente Android. | BLOQUEADO |
| E04 | Conferir nome no WhatsApp | Sem ambiente Android. | BLOQUEADO |
| E05 | Abrir política offline dentro do app | Asset presente; teste local de equivalência e política passou, mas UI sem rede não foi executada em dispositivo. | BLOQUEADO |
| E06 | Abrir política pública a partir do app | URL acessível por GET no host (ver seção 8); abertura `ACTION_VIEW` não testada em Android. | BLOQUEADO |
| E07 | Desligar rede e reabrir política local | Sem dispositivo para alternar rede; somente testes locais do asset. | BLOQUEADO |
| E08 | Testar fallback sem navegador | Teste unitário simulou navegador ausente/erro e passou; ausência real de browser no Android não testada. | BLOQUEADO |
| F01 | Criar backup legítimo com FULL_ACCESS de teste | Sem licença Play de teste/dispositivo. | BLOQUEADO |
| F02 | Validar arquivo criado pela UI | Testes unitários do formato e transferência passaram; UI não gerou arquivo nesta fase. | BLOQUEADO |
| F03 | Restaurar backup legítimo | Testes unitários de transação passaram; SAF/UI não executados. | BLOQUEADO |
| F04 | Confirmar clientes e vendas restaurados | Sem restauração em dispositivo. | BLOQUEADO |
| F05 | Confirmar parcelas e pagamentos restaurados | Fixture/invariantes automatizados passam; restauração na UI não executada. | BLOQUEADO |
| F06 | Confirmar fotos/BLOBs restaurados | Testes instrumentados compilados; sem execução em dispositivo. | BLOQUEADO |
| F07 | Restaurar arquivo inválido/corrompido | Unit tests cobrem headers/truncamento/transação; tentativa na UI não executada. | BLOQUEADO |
| F08 | Conferir rejeição sem alteração da base | Testes unitários de rollback passaram; verificação byte a byte de base em runtime não feita. | BLOQUEADO |
| F09 | Confirmar dados e nome anteriores preservados | RestoreTransaction unitários passaram; não houve base de aparelho. | BLOQUEADO |
| F10 | Testar arquivo grande/malformado em ambiente isolado | Testes unitários cobrem limite de stream e falha de leitura; teste UI/runtime não executado. | BLOQUEADO |
| G01 | Criar até 30 vendas em FREE | Teste unitário de limite 30 passou; fluxo Play/UI não testado. | BLOQUEADO |
| G02 | Confirmar bloqueio da 31ª venda | Teste unitário de bloqueio passou; diálogo UI não testado. | BLOQUEADO |
| G03 | Confirmar clientes ilimitados em FREE | Sem execução funcional em dispositivo nesta fase. | BLOQUEADO |
| G04 | Confirmar CSV liberado em FREE | Sem execução funcional em dispositivo nesta fase. | BLOQUEADO |
| G05 | Confirmar backup/restauração exigem FULL_ACCESS | Testes instrumentados com este cenário foram compilados, mas não executados; sem Billing/device. | BLOQUEADO |
| G06 | Billing indisponível sem perda de dados | Teste unitário de estado indisponível/pendente passou; desconexão real da Play não foi testada. | BLOQUEADO |
| G07 | Compra cancelada permanece FREE e preserva dados | Sem Billing Play de teste. | BLOQUEADO |
| G08 | Restaurar compra FULL_ACCESS existente | Exige build Play e conta autorizada; não executado. | BLOQUEADO |
| G09 | Licença persiste após reinício | Exige licença real e dispositivo; não executado. | BLOQUEADO |

## 7. Compatibilidade por API

| API | Evidência | Limite |
|---|---|---|
| API 24 | APK declara `minSdk 24`; compilação e lint passaram sem erro de NewApi. | Sem plataforma/imagem/AVD API 24 e sem execução. Compatibilidade runtime continua pendente. |
| API 35 | Imagem x86_64 de Play Store disponível no SDK. | Sem AVD válido; não executado nesta fase. A regressão de Fase 5 em API 35 é evidência histórica, não teste desta execução. |
| API 36 | Plataforma instalada; APK declara compile/target 36; lint/build aprovados. | Sem imagem API 36/AVD; compilação não equivale a teste runtime. |
| API 37 | Imagem existe no SDK, mas não é API alvo desta versão. | Sem AVD e sem teste; não substitui API 35/36. |

## 8. Política offline e pública

O APK contém `assets/privacy_policy.html`; `PrivacyPolicyTest` aprovou a URL HTTPS esperada, regras de URL e sincronismo local do texto principal. Nesta fase, um GET somente de leitura à URL oficial respondeu **HTTP 200**, `Content-Type: text/html; charset=utf-8`; o corpo público informa atualização de **15/10/2026**. Os 31 blocos de título/parágrafo da seção `<main>` têm o mesmo texto visível da política local após remover tags, decodificar entidades HTML e normalizar espaços. O HTML público não é byte a byte igual ao arquivo local (formatação/tamanho diferem), sem divergência textual visível na seção principal.

Teste via navegador embutido, `ACTION_VIEW`, funcionamento offline no aparelho e fallback sem browser permanecem pendentes de runtime.

## 9. Problemas e riscos

1. **Sem runtime Android disponível:** é o principal bloqueio de homologação. A listagem ADB falha ao tentar criar `\.android`; não se presume que não exista aparelho, apenas que não foi possível enumerá-lo. O diretório AVD incompleto não foi iniciado.
2. **API 24 e API 36 sem imagens runtime:** minSdk/target/compile foram inspecionados; a execução nessas versões ainda não foi validada.
3. **Billing:** estados de domínio passam unit tests, mas compra, cancelamento, restauração e persistência de entitlement exigem conta de teste da Play e build distribuído pela loja.
4. **APK debug:** assinatura distinta da Play; não usar para atualizar instalação Play nem dados importantes.
5. **Cache Gradle Windows:** tentativas paralelas falharam ao mover diretórios temporários; serialização do worker passou. Não houve limpeza do cache.
6. **Build warnings:** 66 avisos lint por variante e avisos de compilação Kotlin permanecem. Lint não tem erros.
7. **AAB Alpha:** hashes locais e tag preservados, mas não foi comparado o AAB com o artefato efetivamente enviado à Play Console.

Nenhuma falha funcional foi observada nos testes executados. Os testes bloqueados não são considerados aprovados.

## 10. Plano de atualização V11 → V12

Não executar nesta fase. Quando houver AAB V12 autorizado:

1. Em aparelho de teste/conta autorizada, instalar V11 pela faixa Play; registrar modelo/API, versão e estado inicial.
2. Criar apenas dados fictícios: cliente, venda à vista, parcelada, pagamentos parciais, foto e nome do negócio; registrar quantidades e saldos.
3. Conferir origem e assinatura da instalação V11; confirmar que o entitlement FULL_ACCESS veio da Play.
4. Atualizar pela mesma faixa/teste Play, sem desinstalar e sem usar APK debug.
5. Após atualizar, conferir clientes, IDs/relações, parcelas, pagamentos, fotos, nome, saldos, relatórios e licença.
6. Testar PDF/CSV/WhatsApp, política, trabalho offline, Billing indisponível e restauração de compra em conta controlada.
7. Registrar evidências sem dados pessoais; em falha, parar e preservar o aparelho/base para diagnóstico.

## 11. Checklist para homologação manual

- [ ] Criar AVD API 24 pelo Device Manager com imagem autorizada e instalação limpa.
- [ ] Criar AVD API 35 (imagem já presente) ou confirmar telefone de teste conectado por ADB.
- [ ] Instalar o APK debug **somente em ambiente limpo de teste**, confirmar pacote/versão; guardar APK SHA acima.
- [ ] Executar grupos A–E da matriz, incluindo reinício, offline, câmera/galeria, CSV/PDF, nome personalizado e compartilhamento sem enviar cobrança real.
- [ ] Executar F com fixture sintética e licença FULL_ACCESS Play autorizada; nunca restaurar sobre dados reais.
- [ ] Executar G com FREE/FULL_ACCESS na faixa Play e conta de teste; registrar cancelamento, indisponibilidade e restauração.
- [ ] Em telefone físico, validar câmera, Photo Picker, WhatsApp, visualizador PDF, SAF e URL pública.
- [ ] Registrar modelo, API, versão do APK, data, IDs, passos, resultado e evidência sanitizada.
- [ ] Não atualizar V11 → V12 até existir artefato V12 com autorização específica.

## 12. Arquivos e estado final

Novo arquivo nesta fase: `docs/FASE6B_HOMOLOGACAO_ANDROID.md`. Os outputs locais incluem o APK debug acima e relatórios Gradle/lint; nenhum deles foi commitado. Não houve alteração do código de produção.

Ao fim, `HEAD` e `v1.0.11-rc.1` continuam em `8aa34881755f222f9b090691aaa6127b31b1a363`; `versionCode 11`, `versionName 1.0.11` e aplicação permanecem iguais. Os dois AABs preservam o hash descrito na seção 1. Não houve commit, push, tag, AAB release, mudança no Console, merge, deploy nem publicação Android.

## 13. Próxima ação

Executar a matriz em AVD API 24 e em AVD/telefone recente, começando por instalação limpa e mantendo dados de teste isolados. A etapa exige que o usuário crie/autorize os ambientes ou conecte telefone de teste; os comandos ADB só devem ser repetidos após corrigir o diretório Android do perfil com permissão apropriada. Após fechar as pendências de API mínima, dispositivo e Billing Play, reavaliar a candidata V12. **Classificação atual: APTO COM RESSALVAS**, sem homologação completa e sem autorização para release.
