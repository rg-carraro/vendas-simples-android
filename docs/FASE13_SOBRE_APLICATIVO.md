# V13 — Tela Sobre e contato com o desenvolvedor

**Estado:** implementação local na branch `feature/v13-sobre-aplicativo`; não publicada.

**Base:** merge oficial `11214de6aac8bf1072c1767344c725939d3b1802` (`master`).

**Metadados preparados:** applicationId `com.vendassimples.app`, versionCode `13`, versionName `1.0.13`, minSdk 24, targetSdk 36. A mudança de versão existe somente nesta branch.

## Requisitos e decisões

A pedido do proprietário, o menu ⋮ mantém suas opções e recebe “Sobre o aplicativo”. A tela mostra o ícone oficial, “Vendas Simples”, “Controle de Vendas”, a versão obtida dos metadados instalados, a descrição institucional, “Desenvolvedor: RGCarraro” e `rgcarraro@gmail.com`. Existe uma ação de contato, “Falar com o desenvolvedor”, e acesso à política de privacidade.

O contato abre `ACTION_SENDTO` com `mailto:`, destinatário e assunto definidos e corpo editável contendo somente o texto sugerido, o nome do app e a versão real. Não há envio automático: o usuário revisa e envia no cliente escolhido. Nenhum dado comercial, anexo, token ou log é incluído. Se não houver cliente compatível ou a abertura falhar, a tela apresenta uma mensagem e permite copiar o e-mail.

A política abre pelo fluxo já existente, que mostra o texto empacotado localmente/offline e oferece o link HTTPS oficial. Não houve alteração da política, do site, do Billing, das permissões, do banco ou dos fluxos de dados comerciais. O endereço de privacidade que já consta da política permanece intacto; o novo endereço é o canal geral para falar com o desenvolvedor nesta tela. A tela Sobre funciona offline; iniciar/enviar um e-mail depende de aplicativo e conectividade escolhidos pelo usuário.

## Impacto em privacidade e Data Safety

Esta funcionalidade não adiciona SDK, backend, permissão, telemetria ou transmissão automática. Ao invocar o e-mail, o sistema entrega ao aplicativo de e-mail o destinatário, assunto, versão e texto inicial. O conteúdo que o usuário escrever só será enviado se ele decidir fazê-lo; o provedor de e-mail e o destinatário passam a tratar a mensagem segundo seus próprios serviços. O usuário deve evitar inserir dados de clientes desnecessários.

Isso documenta o novo fluxo iniciado pelo usuário; não substitui a auditoria geral de SDKs/Billing nem valida, por si só, uma declaração Data Safety. A orientação e as lacunas do levantamento oficial de Data Safety permanecem descritas nos relatórios próprios do projeto.

## Arquivos alterados

- `app/build.gradle` — versionCode/versionName 13/1.0.13 somente na branch de feature.
- `app/src/main/java/com/example/controlevendas/DeveloperContact.kt` — identidade pública, textos, version label e URI mailto.
- `app/src/main/java/com/example/controlevendas/MainActivity.kt` — item do menu, tela, retorno, abertura segura de e-mail e fallback de cópia.
- `app/src/test/java/com/example/controlevendas/DeveloperContactTest.kt` — conteúdo, versão, URI, corpo editável e ausência de dados privados no modelo.
- `app/src/androidTest/java/com/example/controlevendas/FinalUiInstrumentedTests.kt` — menu, tela, identidade, ícone, versão, retorno, Intent e fallback.
- `docs/DECISOES.md`, `docs/HISTORICO_VERSOES.md`, `docs/INDEX.md` e `README.md` — decisão e estado do candidato.
- `docs/FASE13_SOBRE_APLICATIVO.md` — este registro.

## Validação

Executado com JBR 21.0.10 e Gradle Wrapper 8.11.1, usando `GRADLE_USER_HOME` definido apenas para o processo porque o valor padrão do ambiente apontava para `C:\.gradle` sem permissão.

- `:app:testDebugUnitTest` — **46 testes aprovados, 0 falhas** (inclui os 3 testes novos de contato).
- Na primeira execução, um dos testes novos falhou ao ler `URI.rawQuery`; `mailto:` é opaca no parser Java. Corrigida a leitura do teste usando a parte específica da URI, sem mudança no contrato de produção, e repetida a suíte com sucesso.
- `:app:compileDebugAndroidTestKotlin` — **aprovado**; testes instrumentados compilam.
- `:app:assembleDebug` — **aprovado**. APK local: `app/build/outputs/apk/debug/app-debug.apk`, 7.472.773 bytes, SHA-256 `D00FFBC29592F3320FAF3353EE81641DF023D4BC14A5D36C2729F8C7AA0D3C0C`.
- `aapt dump badging` — confirmou applicationId `com.vendassimples.app`, versionCode `13` e versionName `1.0.13` no APK gerado.
- O mesmo APK informa minSdk 24, targetSdk 36 e compileSdk 36.
- `:app:lintDebug` — **aprovado, 0 erros e 67 avisos**. A Fase 7H registrou 67 avisos na base V12; a contagem é igual. Não foi feita comparação automatizada aviso por aviso.
- `git diff --check` — aprovado.
- Testes instrumentados em runtime e inspeção visual em 320 dp/fonte ampliada — **não executados**. O ADB não conseguiu criar `\.android` por permissão negada. Não contornei essa restrição e nenhum dispositivo foi instalado ou alterado.
- Teste real de seleção de aplicativo de e-mail/envio — **não executado**; o teste automatizado cobre a Intent e a falha simulada de cliente ausente, sem enviar e-mail.

## Homologação relatada pelo proprietário

Em 19/10/2026, o proprietário informou que:

- validou visualmente a tela Sobre no Samsung Galaxy S22+;
- confirmou a abertura do Gmail com destinatário, assunto e corpo corretos;
- confirmou o acesso à política de privacidade local e online;
- criou um backup na V12 e o restaurou na V13 Debug;
- após a restauração, encontrou clientes, vendas, parcelas, miniaturas de fotografias e relatórios;
- conferiu que o resumo financeiro apresentava valores matematicamente consistentes.

Esses resultados ficam classificados como **RELATADOS PELO PROPRIETÁRIO**; não foram repetidos nem inspecionados diretamente pelo Codex nesta execução. A restauração de backup entre versões não substitui o teste de atualização direta V12→V13 por instalação da Google Play, que continua pendente. Não há alegação de envio Alpha, de aprovação da Play ou de teste de compra real nesta evidência.

## Artefatos, distribuição e preservação

O APK acima é debug para validação local. **Nenhum AAB V13 foi gerado.** O AAB V12 na worktree `integration/v12-master-validation` foi conferido sem reconstrução: 5.361.969 bytes, SHA-256 `4CF6052542FA842EC0796D16006A2912BC825039366C20AE7EB504A608CFA75D`.

A branch da V12, a master, o AAB V12 e a instalação real no Samsung S22+ não foram modificados. Não houve commit, push, merge, PR, tag, upload ou publicação.

## Pendências e recomendação

Antes de homologar a V13, executar os testes instrumentados e a revisão visual em emulador/dispositivo separado, incluindo tela estreita e fonte ampliada; verificar abertura/cancelamento em cliente de e-mail e o fallback sem cliente; conferir a política local e os textos no dispositivo. Manter a V12 Alpha intacta até o término da homologação pedida para ela. Esta implementação está **localmente pronta para revisão de código**, mas a homologação visual/física permanece pendente.

Checklist de homologação isolada:

- Instalar apenas em emulador/dispositivo de teste separado; validar API 24 e uma versão Android recente.
- Abrir ⋮ → “Sobre o aplicativo”; confirmar que as opções anteriores do menu continuam disponíveis.
- Conferir ícone, nome, subtítulo, grafia `RGCarraro`, endereço e versão exibida igual aos metadados do APK.
- Conferir layout em 320 dp e fonte ampliada, rolagem e retorno pela seta e pelo botão Android.
- Abrir o contato, verificar destinatário/assunto/corpo, editar e cancelar sem enviar; confirmar que nenhum dado comercial ou anexo é incluído automaticamente.
- Simular ausência de cliente de e-mail e validar mensagem/cópia do endereço.
- Abrir a política sem rede para conferir o texto offline; se houver rede, validar o link HTTPS oficial.
- Confirmar que a instalação, os dados e a faixa Alpha V12 do S22+ não foram envolvidos.
