# Fase 8B.1 — Histórico sanitizado para publicação

**Data:** 16/10/2026
**Branch original:** `integration/v12-master-validation`
**HEAD original preservado:** `aa255f10eb7429c42fe7ed5be964f180941b15e2`
**Branch alternativa:** `release/v12-historico-sanitizado`
**Base:** `origin/master` em `fb59cf96392b80c63a42974c80c1b91be1657e4c`
**Escopo:** reconstrução dos commits locais, sanitização documental e validação; sem push, PR, merge em `master`, tag, alteração funcional ou rebuild de artefato.

## 1. Motivo e resultado

A auditoria da Fase 8B encontrou um caminho absoluto do perfil Windows em `docs/RELEASE_V11.md`, além de espaços finais e linhas vazias excedentes em cinco relatórios. O caminho foi convertido para `app/build/outputs/bundle/release/vendas-simples-v11.aab` durante a reconstrução do primeiro commit que introduziu o relatório; os commits seguintes que atualizam esse arquivo foram reaplicados com a mesma sanitização. Os cinco relatórios foram corrigidos no commit que os introduziu.

**Classificação: HISTÓRICO SANITIZADO COM RESSALVAS.** Antes de incluir este relatório, a comparação com a branch original encontrou seis arquivos documentais diferentes; o relatório é o sétimo arquivo exclusivamente documental. A árvore de código, Gradle, manifesto, testes e ferramentas é equivalente. O merge V12, a versão 12 e o AAB original foram preservados na branch/worktree original. A publicação ainda requer autorização em fase separada; os metadados originais de autoria foram preservados e podem expor o e-mail do autor no GitHub.

## 2. Base, branches e worktrees

- Base observada após `git fetch origin`: `origin/master` em `fb59cf96392b80c63a42974c80c1b91be1657e4c`; a comparação tinha zero commits remotos exclusivos e 22 commits na candidata original.
- Branch original permaneceu em `aa255f10eb7429c42fe7ed5be964f180941b15e2`, limpa e sem alteração de ref.
- O merge original `64344855e377dadf5f6b3044cadde6b41e728459` foi mantido intacto. Na reconstrução, sua árvore é reproduzida a partir da base remota e dos commits reaplicados, com diferenças apenas nas seis sanitizações documentais listadas abaixo.
- Worktree novo: `.worktrees/release-v12-historico-sanitizado`. Os demais worktrees, branches, tags e arquivos preexistentes foram preservados.
- A base remota já continha exatamente o blob da página pública alterado em `628b9f43`; o hash do blob `site/privacy/index.html` era idêntico. Esse commit não foi duplicado na branch nova.

O nó de merge `64344855` não foi recriado: seu segundo pai é `origin/master`, que já é ancestral direto da nova branch por decisão de base. Recriá-lo exigiria um merge redundante com pai já ancestral, sem trazer conteúdo. Seus commits do primeiro pai foram reaplicados; a comparação de árvores confirmou que nenhum conteúdo único do merge se perdeu. A ordem topológica entre os commits reaplicados foi mantida. Autores, mensagens e datas de autoria foram preservados; os metadados de committer foram gerados novamente para os commits reconstruídos.

## 3. Correspondência dos commits

| Commit original | Commit sanitizado | Alteração |
|---|---|---|
| `eae77117bb33231b76ee9cc6e0ee7dab03cb1384` | `1168954ee25d905797acc0149181d6b84adb6a67` | V11; caminho do AAB convertido para referência relativa. |
| `bc700791281acda380ad7316a8e70448afd0c9f9` | `67b39066127ca03f4b4832ea3dcb14d486003bc3` | Backup/restauração; conflito do relatório resolvido a partir do snapshot original e sanitizado. |
| `2b88ccd03197365f558a4113e287aae222126667` | `44325d8b29d9db70139ea62f571479578473ca95` | CSV; relatório V11 preservado e sanitizado. |
| `2159ad6788f5b2d51c0e3bf00e2bcce17024d0be` | `6d59102169351fb5471ad888a85c163039645ea2` | Financeiro e relatório por cliente. |
| `4aa9c754c52a51b42515a0ce6cb30fa5843cba03` | `97b562a5497c854a4f0cff9bbfa486a32f8801f1` | Skills e guia de migração. |
| `783c3a66b8d50de479849efd94422b1bd5f339b7` | `548465503e8315ac35db5ac5e350781ad40a76ed` | Cabeçalho, identidade e fotos; relatório preservado e sanitizado. |
| `8fed0b2f496a8c715b98b99e536f5cecee0129cb` | `fb5a243367188bd20c15ccb695572774133a2977` | Cards de clientes e regressão CSV. |
| `0a85fe0ae75c2da638d23df7284ac4536c8b5242` | `47fdf4cf89723aa19ca63bd64c9b8132e886fafc` | Rótulos financeiros e percentuais. |
| `8aa34881755f222f9b090691aaa6127b31b1a363` | `a94dcbc780791119d46b1e834b3a30e04479162f` | Cards de venda; marco/tag V11 original preservado. |
| `b2e65a02868646b0e9122eb41f46523ba526bb9b` | `1536ea166f5eca4c2b30823d525f3ea03670c899` | Validação e transferência segura do backup. |
| `5467c40edd57f8b5b1271e8ead874404e50b426c` | `54846037f0c81e2fadfcdfcd907541167e3f0261` | Política offline e controles de notificação. |
| `25ff1611c7879863eba939eb8d4d180696723ec1` | `db75835e16795e740d46b9cfb66c4e123ea7b99e` | AGP/Gradle/Kotlin. |
| `eb717b7820fba77f56e7316fa415b5b43cde8ff3` | `fa65d3116de4ab2ee763ea25c6bd8451974da983` | Metadados da versão 12. |
| `3d770dfc7e5eeeb9bd096a4e9a6347d6dc78a8e3` | `61b75a9924d828d179c65b0c7fa94d989bd4c18a` | Evidências V12; whitespace dos cinco relatórios corrigido neste commit. |
| `e37eb987e3673c3ca41f0a791fea933e823a1ff8` | `31abccd525cd14e21c42a42349533c2984dfe639` | Consolidação de evidências sanitizadas. |
| `628b9f43ffa5bd0a7e478935729e0e45b3473228` | Já presente em `origin/master` | A única alteração, o blob da página pública, é idêntica à base; commit redundante omitido. |
| `64344855e377dadf5f6b3044cadde6b41e728459` | Base `origin/master` + commits reaplicados | Nó de merge omitido porque o segundo pai já é a base; árvore e conteúdo integrados foram comparados. |
| `775ce36023ba3ec3a317983dac31f9d3ea698625` | `7e86eb69127cf2685a3f423d8930a580ebff09f6` | Arquivo histórico V0–V12 e relatórios 7H–7J. |
| `832b9db2067ad01b2be9b11fe32519b3c6c3d2fe` | `2a8885aff1ce807499b7bdbdadeea1cfb08d10e7` | AGENTS, README e skills de continuidade. |
| `f0e2458533e525c8eef34f61876f498420dd38cd` | `c0df5b7c9b15e2de17cafdec0ad4bc737a212bbc` | Relatório da Fase 8A. |
| `55b70399e645ff39183c7c95c01ec0531ee7b40f` | `53d459be0cc628c0e6f8c7687da892f1e8d7cd7a` | Detalhamento versionCode e V10. |
| `aa255f10eb7429c42fe7ed5be964f180941b15e2` | `71eec8215bd2f3eac11e48165282e053a2a0185b` | Atualização final do relatório Fase 8A. |

Foram reaplicados 20 commits locais; os dois omitidos são equivalentes à base/estrutura conforme descrito. Este relatório será registrado em um commit documental adicional. Nenhum commit ou tag anterior à base foi reescrito.

## 4. Sanitizações e equivalência

Diferenças da árvore final frente a `integration/v12-master-validation` antes deste relatório:

- `docs/RELEASE_V11.md`: caminho local substituído pelo caminho relativo do artefato.
- `docs/FASE7A_CONSOLIDACAO_V12.md`
- `docs/FASE7B_PREPARACAO_RC_V12.md`
- `docs/FASE7B1_COMPATIBILIDADE_KOTLIN.md`
- `docs/FASE7C_PREPARACAO_V12.md`
- `docs/FASE7D2_CHAVE_UPLOAD_E_RECONCILIACAO_GIT.md`

Nos cinco relatórios, foram removidos espaços finais e linhas vazias excedentes no EOF, sem mudança de texto técnico. A comparação de arquivos não encontrou diferenças em Kotlin, Java, Gradle, Wrapper, Manifest, recursos, testes ou ferramentas. `app/build.gradle` confirma `applicationId com.vendassimples.app`, `versionCode 12`, `versionName 1.0.12`, minSdk 24 e compile/target SDK 36.

Após a inclusão deste relatório, ele próprio é a sétima diferença documental frente à branch original.

## 5. Auditoria de segurança do histórico

Foram examinados snapshots de todos os commits reaplicados, inclusive versões intermediárias de arquivos que depois foram atualizados. Também foram pesquisados caminhos de keystore, propriedades privadas, APK/AAB, bancos locais, chaves privadas, tokens comuns, CPF, telefones e padrões de credenciais.

- **Metadado local sanitizado:** o caminho absoluto em `docs/RELEASE_V11.md` foi substituído por caminho relativo em toda a sequência reaplicada. Nenhum caminho absoluto de perfil Windows foi encontrado nos snapshots da branch nova.
- **Segredos, tokens e chaves privadas:** nenhum padrão correspondente encontrado.
- **Assinatura:** não há keystore, propriedades de assinatura ou valores de senha no histórico. A configuração Gradle apenas carrega nomes de propriedades de configuração local; os achados do padrão genérico para `password` eram chamadas `getProperty`, sem segredo literal.
- **Dados de clientes:** nenhum CPF, banco, foto, telefone ou fixture com dados reais identificado. Testes e fixtures permanecem sintéticos.
- **Artefatos:** nenhum APK/AAB, banco SQLite ou arquivo de chave aparece nos objetos da branch. `gradle-wrapper.jar` é o wrapper técnico versionado e foi mantido.
- **Contato público:** e-mail de suporte aparece no site e na política como contato do aplicativo; é conteúdo de contato público previamente confirmado pelo responsável, não dado de cliente nem credencial. O endereço não é repetido neste relatório.
- **Autoria Git:** nome/email de autoria dos commits locais foram preservados conforme o requisito de rastreabilidade. O e-mail é um identificador pessoal nos metadados dos commits e pode ficar visível no GitHub; foi mantido para conservar autoria. Não é token, credencial ou dado de cliente. O responsável deve considerar essa visibilidade antes de autorizar o push.
- **Falso positivo numérico:** a expressão genérica de telefone coincidiu com o limite inteiro `2147483647` em uma validação SQL e no teste correspondente; ambos são limites técnicos de parcelas, não número de telefone ou dado pessoal.
- **Limite:** buscas heurísticas não substituem revisão humana de futuras alterações nem provam ausência matemática de qualquer dado sensível.

## 6. Testes e verificações

Executado no commit reconstruído anterior a este relatório com JBR 21.0.10, Gradle 8.11.1 e cache existente, usando `GRADLE_USER_HOME` temporário. A primeira tentativa não iniciou por `JAVA_HOME` ausente; a segunda apontou para o cache do projeto e falhou offline porque o plugin Kotlin não estava nesse cache. A tentativa final com o cache Gradle do usuário foi concluída:

- `:app:testDebugUnitTest`: 43 testes, zero falhas/erros/skips.
- `:app:testReleaseUnitTest`: 43 testes, zero falhas/erros/skips.
- `:app:lintDebug`: passou, zero erros e 67 avisos.
- `:app:lintRelease`: passou, zero erros e 67 avisos.
- Build conjunto terminou `BUILD SUCCESSFUL` em 5m09s. Não foram chamadas tarefas `assemble` ou `bundle`; nenhum APK/AAB foi gerado nesta fase.
- `git diff --check origin/master...HEAD`: passou depois das correções documentais.
- Equivalência de código e versão: diferenças somente documentais conforme acima.
- Links relativos e referências entre Markdown: passaram em 68 arquivos Markdown, incluindo o relatório desta fase.
- AndroidTest: não executado em runtime; homologação física/API24/Play Billing continuam fora desta validação.

O AAB original no worktree `integration/v12-master-validation` permanece em 5.361.969 bytes, SHA-256 `4CF6052542FA842EC0796D16006A2912BC825039366C20AE7EB504A608CFA75D`. O hash foi conferido antes e depois dos testes; não houve rebuild nem cópia para o worktree sanitizado.

## 7. Estado Git e pendências

- Branch sanitizada baseada em `origin/master`; antes do commit deste relatório, HEAD era `71eec8215bd2f3eac11e48165282e053a2a0185b`, 20 commits reconstruídos à frente da base.
- Branch original segue em `aa255f10eb7429c42fe7ed5be964f180941b15e2`; não foi modificada.
- `master`, `origin/master`, tags, worktrees anteriores e artefatos foram preservados.
- Não houve push, PR, merge, tag, upload Alpha, publicação Play, alteração funcional ou de versão.
- A branch alternativa não foi ainda enviada; Data Safety, homologação física V12/API24, atualização via Play e Billing real continuam como pendências para eventual revisão Alpha.

**Próxima ação recomendada:** revisar o mapeamento, a sanitização e o diff desta branch. A publicação e abertura de PR devem ocorrer em fase posterior, após autorização explícita; não fazer merge automaticamente.
