# Fase 17D — master consolidada e build Debug local

**Data:** 26/10/2026

**Repositório:** `rg-carraro/vendas-simples-android`

**Código master usado na build:** `21e5f824914339944efd6110ad91a0082178aad7`

**Versão Release preservada:** `versionCode 14`, `versionName 1.0.14`, `applicationId com.vendassimples.app`

## Integração

A Fase 16 já estava integrada pela PR #7 no merge `a742c9139950c6098b4a4960c181e97a45b48fb7`, que contém os commits `d00acef` (Light/Dark), `a813542` (limpeza de temporários SQLite) e `7db6fb3` (documentação/homologação). A Fase 17B foi registrada pelo commit `21e5f824914339944efd6110ad91a0082178aad7`, filho direto daquele merge, e integrada em `origin/master` por fast-forward, sem force push.

A abertura de PR foi tentada, mas o conector GitHub retornou HTTP 403 (`Resource not accessible by integration`) e o `gh` CLI não estava instalado. Por isso foi usada a integração direta, prevista como alternativa na solicitação, após confirmar que `origin/master` era ancestral do commit e que `git push --dry-run` só faria avanço fast-forward. Não há PR da Fase 17B.

As alterações documentais locais das Fases 16F/16G foram encontradas na worktree preexistente `.worktrees/master-post-merge`: nove arquivos rastreados modificados e dois relatórios não rastreados. Foram copiados para esta worktree limpa baseada em `origin/master`, sem editar ou remover os originais. Os 11 arquivos foram conferidos um a um por SHA-256 após a cópia. Nos novos arquivos 16F/16G, apenas espaços finais de quebra Markdown foram normalizados em linhas vazias; os originais permaneceram intactos. A consolidação preserva os relatos da PR #7, o hash do merge, o histórico de versões, resultados de testes e os relatórios 16F/16G. As cópias e atualizações de 17B/17D estão incluídas nesta consolidação documental.

## Mudanças de UI consolidadas

- Nova Venda: Galeria e Abrir câmera lado a lado, pesos iguais, texto centralizado e quebra responsiva de até duas linhas.
- Remover foto: ícone decorativo removido; rótulo, descrição acessível, espaçamento e callback preservados.
- Lista de Vendas: ícones dos filtros Data e A receber removidos, textos centralizados e callbacks/estado de filtro mantidos.
- Nenhuma mudança funcional em vendas, pagamentos, SQLite, cálculos, Billing, backup, fotos, PDFs, CSV ou WhatsApp. A navegação da janela Nova Venda foi preservada.

## Verificações

Executadas em worktree limpa no commit `21e5f824914339944efd6110ad91a0082178aad7`, com Gradle offline e cache local que contém Kotlin 2.2.20:

| Verificação | Resultado |
|---|---|
| `:app:testDebugUnitTest` | **46/46 passou** |
| `:app:testReleaseUnitTest` | **46/46 passou** |
| `:app:compileDebugAndroidTestKotlin` | **Passou** |
| `:app:assembleDebugAndroidTest` | **Passou** |
| `:app:lintDebug` e `:app:lintRelease` | **Passaram, 0 erros; 65 avisos Debug e 67 Release** |
| `:app:assembleDebug` | **Passou** |
| Galaxy S22, backup sintético | **38/38 passou** |
| Galaxy S22, persistência após restart | **1/1 passou** |
| Galaxy S22, UI `final-ui` | **20/20 passou** |
| `git diff --check` | **Passou para as alterações rastreadas de código e documentação.** |

O aparelho foi identificado como Galaxy S22 SM-S906E, Android 16/API 36. Ambos os APKs de teste foram atualizados com `adb install -r`; o runner executado foi o recém-gerado do mesmo código master. As suítes de backup usam `BackupTestContext`, banco/preferences sob namespace sintético `.test`; os testes de UI guardam e restauram as preferências do pacote paralelo. Nenhum teste de venda foi salvo no pacote oficial.

O proprietário informa que aprovou visualmente os ajustes nos modos claro e escuro no Galaxy S22. Esta é homologação relatada pelo proprietário; o Codex não capturou screenshots nem alternou o tema ou fonte nesta execução. Os testes exercitaram layout com 28 sp/280 dp e recursos Light/Dark. Intents reais de câmera/galeria e compra/restauração real de Billing não foram executados. A variante usa identificador `.fase16`, portanto não valida a configuração Billing do pacote oficial.

## APK local

- **Arquivo:** `C:\Users\rgcar\git\vendas-simples-android\dist\debug\VendasSimples_Master_Consolidada_Debug.apk`
- **Tamanho:** 7.331.743 bytes
- **SHA-256:** `CDFCC296A86EB15D602E20D50C25B74103FB88A069A002FF0365B965575EED9F`
- **Application ID efetivo:** `com.vendassimples.app.fase16`
- **Rótulo Debug:** `Vendas Simples — Consolidado`
- **Metadados:** `versionCode 14`, `versionName 1.0.14`
- **Assinatura:** APK Signature Scheme v2 válida; certificado Debug SHA-256 `413dc97b411355b68d9a6bab11c2bfda5a5a4f77c2ac82cd20b1cc3fae2f4563`.
- **Commit de código usado:** `21e5f824914339944efd6110ad91a0082178aad7`.

O APK foi produzido em uma worktree destacada da master e o manifesto Debug recebeu localmente o rótulo “Vendas Simples — Consolidado”. Esse ajuste de rótulo não foi commitado; Release e application ID oficial não foram alterados.

## Preservação e próximos passos

A V14 oficial `com.vendassimples.app` permaneceu em `14 / 1.0.14`. Não houve AAB, build de Release assinada, upload, publicação ou alteração na Google Play. Billing, identificador oficial e assinatura Release foram preservados. A futura V15 (`15 / 1.0.15`) depende de autorização específica e de seu próprio checklist de release.

Após a instalação dos APKs paralelos, ADB confirmou a V14 oficial ainda em `versionCode 14`, `versionName 1.0.14` e `lastUpdateTime=2026-10-21 12:53:45`. O APK de teste foi instalado com `-r`; o app paralelo ficou em primeiro plano após a validação. O modo do aparelho não foi alterado.

A worktree principal e as demais worktrees foram preservadas. Antes de sincronizar a worktree `master-post-merge`, os nove arquivos rastreados modificados e dois relatórios não rastreados foram copiados para `C:\Users\rgcar\git\vendas-simples-android\.worktrees\fase17d-preservacao-master-20261026\copias\`, preservando datas e conferindo SHA-256 individualmente. O manifesto tem SHA-256 `B3D87CB3B704208039E25AAE041CEE41D8D893B15A5F7845A7096C48B462C4AD`; os dois documentos que colidiam foram movidos para `originais-movidos-da-colisao\docs\`, sem exclusão definitiva. Após as cópias serem verificadas e os conteúdos documentais correspondentes estarem no commit remoto, a branch local `master` foi avançada por fast-forward de `a742c913` para `6a0eff6516900ba8559a8a4c16aa336cae406874`; a worktree `master-post-merge` ficou limpa e igual a `origin/master` naquele SHA. O commit de código usado na build permaneceu `21e5f824`; o avanço subsequente foi documental.
