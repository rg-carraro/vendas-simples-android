# Fase 17B — Interface Clean

**Data:** 26/10/2026  
**Branch/worktree:** `feature/fase17b-interface-clean` / `.worktrees/fase17b-interface-clean`  
**Base:** master integrada, commit `a742c9139950c6098b4a4960c181e97a45b48fb7` (PR #7)  
**Versão:** `versionCode 14`, `versionName 1.0.14`; sem alteração de metadados.

## Alterações

- Nova Venda: “Galeria” e “Abrir câmera” ficam lado a lado com pesos iguais, espaçamento de 8 dp, estilo azul e texto centralizado. Os botões permitem até duas linhas e não têm largura mínima fixa, acomodando telas estreitas e fontes ampliadas.
- Remover foto: retirado o ícone de lixeira. O texto e a descrição acessível “Remover foto N de total” permanecem, junto à margem responsiva de 16 dp entre a miniatura e a ação.
- Lista de Vendas: removidos os drawables de calendário e financeiro dos filtros. “Data” e “A receber” permanecem como rótulos, centralizados. O estado selecionado continua expresso pela cor, e os callbacks e critérios de filtro foram preservados.
- Nenhum outro ícone, cabeçalho, botão de janela modal ou navegação foi alterado.

## Arquivos

- `app/src/main/java/com/example/controlevendas/MainActivity.kt`
- `app/src/debug/AndroidManifest.xml` (rótulo exclusivo Debug para a Fase 17C)
- `app/src/androidTest/java/com/example/controlevendas/FinalUiInstrumentedTests.kt`
- `docs/FASE17B_INTERFACE_CLEAN.md`
- `docs/HISTORICO_TESTES.md`
- `docs/HISTORICO_VERSOES.md`
- `docs/INDEX.md`

## Validação

Executado no worktree da Fase 17B, sem modificações na worktree principal:

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:testReleaseUnitTest :app:compileDebugAndroidTestKotlin :app:lintDebug :app:lintRelease :app:assembleDebug --offline
```

- **PASSOU:** 46/46 testes unitários Debug e 46/46 Release.
- **PASSOU:** `compileDebugAndroidTestKotlin`.
- **PASSOU:** `lintDebug` e `lintRelease`, 0 erros; 65 avisos Debug e 67 Release.
- **PASSOU:** `assembleDebug`; APK local gerado em `app/build/outputs/apk/debug/app-debug.apk` dentro desta worktree. Não foi copiado para `dist`.
- **PASSOU:** `git diff --check`.
- **NÃO EXECUTADO:** runtime dos testes instrumentados e inspeção visual/funções no aparelho. `adb devices -l` listou apenas o cabeçalho, sem dispositivos, em 26/10/2026. O serial `RQCTB02SZWL` retornou “device not found”.

Portanto, câmera e galeria tiveram seus callbacks preservados no diff, e a inclusão/remoção sintética existente continua sendo compilada, mas não houve execução física de captura, seletor de galeria ou remoção nesta rodada. Também ficaram pendentes a confirmação visual dos quatro ajustes, Light/Dark Mode e fonte ampliada no Galaxy S22. As novas verificações instrumentadas de filtros e drawables, e do layout dos botões de foto a 28 sp/280 dp, compilaram, mas não foram executadas em runtime.

## Preservação e pendências no encerramento original da Fase 17B

O `applicationId` Release permanece `com.vendassimples.app`; a variante Debug mantém o sufixo isolado existente `.fase16`. No encerramento original da Fase 17B, nenhuma variante havia sido instalada no aparelho. Nenhum dado comercial, SQLite, cálculo financeiro, Billing, backup, PDF ou funcionalidade comercial foi alterado. Não foi gerado AAB e não houve commit, push, merge ou publicação. **O aditivo da Fase 17C abaixo atualiza o estado de instalação e os testes no dispositivo.**

Próximo passo registrado ao encerrar a Fase 17B: com o S22 reconectado, instalar/atualizar somente o APK Debug paralelo e executar os AndroidTests `final-ui`, conferir os botões de foto e os filtros em modo claro/escuro e fonte normal/ampliada, e testar seletor/câmera e inclusão/remoção com imagens sintéticas no namespace paralelo. O aditivo da Fase 17C registra a execução efetiva possível; a instalação oficial `com.vendassimples.app` permaneceu intocada.

## Aditivo Fase 17C — build e estado do Galaxy S22 (26/10/2026)

- O S22 SM-S906E apareceu autorizado no ADB, Android 16/API 36, 1080×2340 px, 450 dpi. Configuração original observada antes/depois: modo claro, fonte 1,0; nenhuma configuração do aparelho foi alterada.
- Antes da tentativa, `com.vendassimples.app` estava em `14 / 1.0.14`, instalado/atualizado em `2026-10-21 12:53:45`. O pacote paralelo `com.vendassimples.app.fase16` estava em `14 / 1.0.14`, instalado em 23/10/2026. Certificados SHA-256 do APK paralelo instalado e do APK Fase17B coincidiram (`413dc97b411355b68d9a6bab11c2bfda5a5a4f77c2ac82cd20b1cc3fae2f4563`).
- O manifesto Debug foi rotulado “Vendas Simples — Teste Interface Clean”. `assembleDebug` e `compileDebugAndroidTestKotlin` passaram. `connectedDebugAndroidTest` concluiu, mas reportou **0 testes**; não foi contado como validação. Uma invocação direta inicial também falhou porque o pacote de runner ainda não estava instalado. Após instalar ambos os APKs de Debug e de instrumentação no perfil principal, a chamada direta `am instrument -w --user 0 -e phase final-ui com.vendassimples.app.fase16.test/com.example.controlevendas.BackupInstrumentationRunner` executou **20/20 testes instrumentados, todos PASSOU**.
- A suíte no S22 cobriu: filtros e ordenação; descrição sem drawables e centralização/área mínima dos filtros; remoção sintética de primeira/última/única foto; pesos iguais, callbacks presentes e texto de Galeria/Câmera sem corte em 28 sp/280 dp; contrastes Light/Dark e cores dos diálogos por recursos/contextos. Não executou intents reais do seletor de galeria ou câmera, nem fluxo manual de salvar venda com/sem foto. Não houve screenshot ou inspeção visual humana nesta sessão.
- Estado de instalação: antes da tarefa conectada, `.fase16` estava instalado em 23/10. Depois dela, deixou de aparecer no perfil principal; ADB não consegue consultar o perfil Secure Folder (`user 150`). Não foi executado `adb uninstall` nem `pm clear` pelo Codex. O proprietário autorizou então uma instalação limpa em `user 0`; a nova instalação foi feita com `adb install --user 0 -r`, código 0, em `2026-10-26 19:35:58`. O pacote de instrumentação `.fase16.test` também foi instalado para executar os testes e permanece instalado; nenhuma desinstalação foi feita. Não é possível determinar se os dados da instalação paralela de 23/10 foram removidos pela tarefa conectada ou se ainda existem no Secure Folder.
- Inicialização: `monkey` abriu `com.vendassimples.app.fase16`; `dumpsys activity` confirmou `MainActivity` em foreground. Rótulo, package, versionCode 14/name1.0.14 e assinatura V2 conferem.
- A V14 oficial `com.vendassimples.app` permaneceu instalada em `14 / 1.0.14`; `lastUpdateTime` antes/depois `2026-10-21 12:53:45`. A configuração do aparelho permaneceu modo claro, fonte 1,0. O Galaxy S22 reportou Android 16/API36, 1080×2340, 450 dpi. O proprietário pode agora fazer a inspeção visual do app paralelo aberto. Câmera/galeria reais e a troca física do tema/fonte permanecem pendentes.
- APK entregue localmente: `C:\Users\rgcar\git\vendas-simples-android\.worktrees\fase17b-interface-clean\dist\debug\VendasSimples_Fase17B_InterfaceClean_Debug.apk`; 7.334.356 bytes; SHA-256 `3F198F49A124E500A918326F0EA20D334203580765ED69D701FA85E141DA5A87`. `applicationId` `com.vendassimples.app.fase16`, rótulo “Vendas Simples — Teste Interface Clean”, code14/name1.0.14 e assinatura V2 válida.

## Aditivo Fase 17D — revalidação e homologação informada (26/10/2026)

- O proprietário confirmou nesta solicitação que aprovou a aparência dos ajustes da Interface Clean no Galaxy S22, nos modos claro e escuro. Esse é um relato de homologação do proprietário; não houve captura ou inspeção visual humana pelo Codex nesta execução. A configuração do aparelho foi preservada.
- Reexecutado no S22 SM-S906E/Android 16/API 36: os 20 testes `final-ui` passaram via `am instrument` (**20/20**), incluindo testes de layout com fonte ampliada, filtros e inclusão/remoção sintética de fotos. Os APKs Debug e AndroidTest foram atualizados com `adb install -r`; os dados do pacote paralelo foram preservados. A execução Gradle foi feita pela suíte local abaixo; `connectedDebugAndroidTest` não foi executado nesta rodada.
- Reexecutado com Gradle e cache `C:\Users\rgcar\.gradle`: `testDebugUnitTest`, `testReleaseUnitTest`, `compileDebugAndroidTestKotlin`, `lintDebug`, `lintRelease` e `assembleDebug` passaram; build terminou `BUILD SUCCESSFUL`. A primeira tentativa com cache `.gradle-user` falhou por ausência do marker Kotlin 2.2.20, e a tentativa com rede também não resolveu; o cache padrão já continha Kotlin 2.2.20. Nenhuma dependência ou versão foi alterada.
- Navegação e callbacks de galeria/câmera foram mantidos no diff; nenhum teste real de compra ou Billing foi realizado. Por usar `applicationId` paralelo `.fase16`, a build não valida a configuração de Billing do identificador oficial. Não houve AAB, publicação, alteração de versão, commit ou push ao registrar este aditivo.
