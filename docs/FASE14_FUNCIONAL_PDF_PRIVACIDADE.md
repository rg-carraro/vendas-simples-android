# Fase 14 — identidade visual dos PDFs e acesso à política

**Data:** 19/10/2026
**Branch:** `feature/v14-pdf-identidade-privacidade`
**Worktree:** `.worktrees/v14-pdf-identidade-privacidade`
**Base verificada:** master `88c9bb1f9b5455a4b03230932d4b2de488ab780a` (merge PR #5, inclui a consolidação documental da Fase 14)
**Versão nesta branch:** `versionCode 14`, `versionName 1.0.14`
**Estado:** implementação local validada para preparação do release; AAB assinado gerado. Commit, push e PR são registrados no estado final desta fase; sem merge, upload ou distribuição.

## Resultado

O menu de três pontos não apresenta mais uma entrada duplicada para Política de Privacidade. “Sobre o aplicativo” permanece no menu e ainda oferece a leitura local/offline, bem como a ação para abrir o endereço HTTPS oficial. As demais opções existentes não foram alteradas. A seta e o retorno Android de Sobre continuam configurados para voltar ao menu.

O inventário encontrou três ações de exportação PDF: PDF do card, relatório financeiro por mês/período e extrato por cliente. Todas delegam a um único gerador paginado, `gerarPdfGenerico`, em `MainActivity.kt`; não há geradores PDF independentes. A identidade reutiliza o ícone launcher oficial `R.mipmap.ic_launcher` (com recursos compatíveis com API 24), o nome atual de `BusinessPreferences.displayName` — que preserva o fallback “Vendas Simples” — e a identificação sutil “Gerado pelo Vendas Simples”. Esse cabeçalho é desenhado somente na primeira página. Quando o gerador cria uma página seguinte, continua iniciando o conteúdo em `y = 42`, sem reservar espaço ou repetir ícone/cabeçalho.

Títulos de relatório, ordenação, campos, moedas, datas, totais, inclusão de fotos, filtros e rotina de compartilhamento não foram alterados. CSV continua separado e sem mudanças. Como o cabeçalho ocupa espaço na página inicial, alguns documentos podem paginar um registro mais cedo; a paginação prossegue pelo fluxo existente e precisa de inspeção visual final.

## Testes e builds

Comando executado na worktree funcional:

```powershell
$env:JAVA_HOME = '<JBR do Android Studio>'
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
$env:GRADLE_USER_HOME = '<cache Gradle local já disponível>'
.\gradlew.bat :app:testDebugUnitTest :app:testReleaseUnitTest :app:compileDebugAndroidTestKotlin :app:lintDebug :app:lintRelease :app:assembleDebug
```

O `JAVA_HOME`, `PATH` e `GRADLE_USER_HOME` foram definidos apenas para o processo PowerShell daquele comando. Resultado: `BUILD SUCCESSFUL` em 4m15s; 84 tarefas. Testes unitários: 46/46 em debug e 46/46 em release. AndroidTest compilado. `lintDebug` e `lintRelease` passaram sem erros, com 67 avisos por variante. O histórico da V13 também registra 67 avisos; não foi feita comparação item a item para atribuir a origem de cada aviso.

O proprietário informou que homologou manualmente a V14 e confirmou o funcionamento das mudanças aprovadas (RELATADO PELO PROPRIETÁRIO; não repetido pelo Codex). Foi acrescentado AndroidTest que renderiza PDF multipágina com dados sintéticos, com nome configurado e com fallback vazio, e verifica pixels na região do ícone para confirmar presença na primeira página e ausência na segunda. O teste foi compilado, mas **não executado em runtime**. ADB falhou ao tentar criar `\.android` por permissão do sandbox e `emulator -list-avds` não listou AVD. Nenhum dispositivo recebeu instalação e API 24 não foi testada.

Build debug foi produzido em `.worktrees/v14-pdf-identidade-privacidade/app/build/outputs/apk/debug/app-debug.apk`: 7.323.463 bytes, SHA-256 `8F958CF2FCBF256E9CAD375AF6D7BC2FA71D206B2A7E43605E4DE38532FD6022`. O `aapt dump badging` confirmou `com.vendassimples.app`, versionCode 14, versionName 1.0.14, minSdk 24, targetSdk 36 e compileSdk 36.

## Fechamento do release V14 (19/10/2026)

O comando `:app:testDebugUnitTest :app:testReleaseUnitTest :app:compileDebugAndroidTestKotlin :app:lintDebug :app:lintRelease :app:bundleRelease` terminou com `BUILD SUCCESSFUL` (96 tarefas; 30 executadas, 66 atualizadas). Os testes unitários debug/release e a compilação do AndroidTest passaram; lint debug/release passou com 0 erros e 67 avisos por variante. Nenhum warning foi suprimido ou corrigido nesta etapa. O build não executou AndroidTest em dispositivo.

Bundle gerado: `app/build/outputs/bundle/release/app-release.aab` nesta worktree; tamanho 5.364.816 bytes; SHA-256 `158DA7AF0CA10F1E8355E5AE4CC6CC99BDDEE733B6090C6144E7AB7CD274B8FC`. Bundletool 1.17.2 validou o AAB e leu o manifesto: package `com.vendassimples.app`, versionCode 14, versionName `1.0.14`, minSdk 24, targetSdk 36 e compileSdk 36. `jarsigner -verify` retornou `jar verified`. O certificado público tem SHA-256 `CD:90:9D:AE:E8:16:B2:7F:4E:32:13:52:73:62:25:80:BE:F0:73:F1:8A:8D:EB:56:04:F5:24:90:9C:63:C6:9E` e SHA-1 `FE:B9:79:CD:C1:3F:B4:EE:FA:80:35:8D:70:6F:9F:35:40:0F:34:AE`. Ambos coincidem com os certificados extraídos do AAB V13 já existente em seu worktree, e o SHA-256 coincide com a impressão digital da chave de upload registrada anteriormente. A transcrição anterior do SHA-1 continha `...C1:35:B4...`, divergindo do certificado extraído; registrar/corrigir esse valor na documentação de referência em revisão futura. O certificado é autoassinado, como esperado para o certificado de upload local; isso não representa o certificado de assinatura do Google Play.

Nenhuma mudança funcional além das duas melhorias V14 aprovadas foi feita. O proprietário relatou a homologação manual descrita acima; runtime de AndroidTest, API 24, atualização in-place pela Play e Billing real permanecem não executados pelo Codex. A V13/AAB V13 em seu worktree próprio não foi alterada. O AAB V14 não foi carregado à Console.

## Arquivos alterados

- `app/build.gradle` — `versionCode 14` / `versionName 1.0.14` nesta branch.
- `app/src/main/java/com/example/controlevendas/MainActivity.kt` — remoção do atalho duplicado e cabeçalho na primeira página do gerador PDF comum.
- `app/src/androidTest/java/com/example/controlevendas/FinalUiInstrumentedTests.kt` — ausência do menu duplicado, acesso a Política em Sobre e teste instrumentado multipágina do PDF.
- `AGENTS.md`, `README.md`, `docs/DECISOES.md`, `docs/PENDENCIAS_ATUAIS.md`, `docs/ROADMAP_PRODUTO.md`, `docs/HISTORICO_TESTES.md`, `docs/HISTORICO_VERSOES.md`, `docs/INDEX.md` e `docs/FASE14_CONSOLIDACAO_CONHECIMENTO.md` — continuidade e estado da V14.
- `docs/FASE14_FUNCIONAL_PDF_PRIVACIDADE.md` — este relatório.

## Preservação e pendências

Não foram alterados banco SQLite, schema/migrações, cálculos, clientes, vendas, parcelas, pagamentos, fotos armazenadas, Billing, produto `full_access`, limite FREE 30, backup/restauração, política local/online, CSV ou aplicação V13. A versão V13 e seu AAB previamente validado (SHA-256 registrado no histórico: `33D0906198E439304887A59AB683AF723636DEEE6ACAF51025BD073C42ED6642`) permanecem em seu worktree próprio; nenhum artefato V13 foi escrito.

Verificações automatizadas complementares ainda pendentes:

- Executar o AndroidTest em AVD dedicado e revisar a renderização do PDF, incluindo nome longo/acentuado, fallback, ícone, título, margens, fotos, valores/totais e paginação de cada rota.
- Revalidar navegação do menu e leitura offline/online da política em runtime, inclusive retorno do Android; testar em API 24 se houver emulador disponível.
- Conferir o delta com a master atualizada quando for preparar integração; atualizar o estado das pendências com evidência da runtime.

O teste manual acima é um relato do proprietário, não execução observada pelo Codex. Na etapa funcional inicial, não houve commit, push, PR, merge, tag, AAB, upload ou publicação. As pendências técnicas e o fechamento do release são registrados após a validação final desta etapa.
