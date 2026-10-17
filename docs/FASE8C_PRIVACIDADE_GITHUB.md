# Fase 8C — Privacidade do histórico no GitHub

**Data:** 16/10/2026 · **Repositório:** `rg-carraro/vendas-simples-android` · **Branch nova:** `release/v12-github-privado` · **Base remota confirmada:** `origin/master` em `fb59cf96392b80c63a42974c80c1b91be1657e4c`

## Objetivo e decisão

Reconstruir em branch e worktree isolados os commits locais da candidata sanitizada da Fase 8B.1, trocando somente os e-mails pessoais de autoria e committer do proprietário por seu endereço GitHub privado `noreply` confirmado. Nenhum conteúdo funcional ou versão foi alterado.

**Classificação: PRIVACIDADE VALIDADA COM RESSALVAS.** Os 21 commits reconstruídos não contêm o e-mail pessoal do proprietário em metadados, mensagens ou conteúdo versionado. A auditoria, porém, encontrou o mesmo e-mail em 41 commits de autoria e 41 campos de committer da história que já estava publicada em `origin/master`. Essa história remota não foi reescrita. A publicação da nova branch não removerá nem ocultará esses registros existentes.

## Origem e estado preservado

| Referência | Estado observado |
| --- | --- |
| `integration/v12-master-validation` | HEAD `aa255f10eb7429c42fe7ed5be964f180941b15e2`, preservado |
| `release/v12-historico-sanitizado` | HEAD `a4cf3a1ca6b7266a31f076345b67ebe37797cfe6`, preservado |
| Base `origin/master` | `fb59cf96392b80c63a42974c80c1b91be1657e4c` |
| `release/v12-github-privado`, após reconstrução | `6c947f0acaef6e4e2229e8bf7978b4aa7f7393d3`, antes do commit deste relatório |

`git fetch origin` não conseguiu gravar `.git/FETCH_HEAD` no ambiente isolado. Para não contornar essa restrição, a base foi conferida pela API pública do GitHub: o `master` remoto continuava em `fb59cf96392b80c63a42974c80c1b91be1657e4c`. O proprietário confirmou visualmente o endereço privado nas configurações oficiais do GitHub; a API também associa o commit público a `rg-carraro` (ID 73358993) e publica nele o endereço `noreply`. O endereço integral não é repetido neste relatório.

O repositório estava na branch `fix/v12-seguranca-conformidade`, com arquivos e worktrees locais não commitados preexistentes. Nenhum foi alterado ou removido. As branches `integration/v12-master-validation`, `release/v12-historico-sanitizado`, `master`, `origin/master`, `release/v11-personalizacao` e as tags existentes foram preservadas. A nova branch foi criada num worktree próprio, baseada diretamente na mesma base remota da Fase 8B.1.

## Reconstrução e rastreabilidade

Foram reconstruídos os 21 commits da branch sanitizada, em ordem linear. Todos tinham o mesmo autor e committer, o proprietário; nenhum commit de terceiro foi reatribuído. Em cada commit foram trocados apenas os dois campos de e-mail: **42 campos no total**. Nomes, datas de autoria, datas de committer e mensagens foram comparados e preservados. Não houve squash, tag retroativa, mudança de árvore ou reescrita das branches anteriores.

A árvore Git final antes deste relatório foi idêntica à da Fase 8B.1 (`259f3b47111bad4a1b7d1fac64be0d7b690d2518`). O commit adicional deste documento é apenas documental. Kotlin, Gradle, manifesto, recursos, testes, banco, Billing, regras FREE/FULL_ACCESS, backup, política e arquivos de histórico permaneceram idênticos.

| Commit da Fase 8B.1 | Commit reconstruído |
| --- | --- |
| `1168954ee25d905797acc0149181d6b84adb6a67` | `180f5f3a18c42409b296814462c35a337d4959e1` |
| `67b39066127ca03f4b4832ea3dcb14d486003bc3` | `ee5e6c21cef124d506cde51eba9585c4bb34d58c` |
| `44325d8b29d9db70139ea62f571479578473ca95` | `3e6dc4da6827008c48e6b547963ff422328ca8dd` |
| `6d59102169351fb5471ad888a85c163039645ea2` | `22f6b6a37167745175138fa546346415a8c4365c` |
| `97b562a5497c854a4f0cff9bbfa486a32f8801f1` | `cf6f0e8b89ef3de056bcd9939c0c4e4778d80663` |
| `548465503e8315ac35db5ac5e350781ad40a76ed` | `c398e8f4bbdc29180eddd88b3d18897ff4c31d16` |
| `fb5a243367188bd20c15ccb695572774133a2977` | `3c8664113d4ae685cca4ca18a290e0be62963294` |
| `47fdf4cf89723aa19ca63bd64c9b8132e886fafc` | `8b2d10599aa716156a0dcb7cd0ab2cc495fc0030` |
| `a94dcbc780791119d46b1e834b3a30e04479162f` | `1594ac3a9dac4aa48d848d201392ee7ee79371c4` |
| `1536ea166f5eca4c2b30823d525f3ea03670c899` | `2e89945e024edb65709bc5312c0137277a166eca` |
| `54846037f0c81e2fadfcdfcd907541167e3f0261` | `d9552c1d61ee7bdfe5c95b3d28d1e0d63c3303c4` |
| `db75835e16795e740d46b9cfb66c4e123ea7b99e` | `24ac2e67ed0288f9483cb422f7d222c9da0c8eeb` |
| `fa65d3116de4ab2ee763ea25c6bd8451974da983` | `61212336244f4332f831825190b04c0f05d0ba8a` |
| `61b75a9924d828d179c65b0c7fa94d989bd4c18a` | `bea41ebed1af975df3a48e8297b8a7fb0fa16ea4` |
| `31abccd525cd14e21c42a42349533c2984dfe639` | `8c77bb287c140c35bfa369bd7e12f4c0f9a46b7f` |
| `7e86eb69127cf2685a3f423d8930a580ebff09f6` | `4ada27fdba180ea880177a3a36ccb56d0769fb05` |
| `2a8885aff1ce807499b7bdbdadeea1cfb08d10e7` | `8ba4d8d370e6d11c8af911dfc4e12951ac5ef464` |
| `c0df5b7c9b15e2de17cafdec0ad4bc737a212bbc` | `10abe7f7643cc494b89da43b05d262fa872aeb20` |
| `53d459be0cc628c0e6f8c7687da892f1e8d7cd7a` | `f51a74a5f8e0da5aad9e69d399936944a0153016` |
| `71eec8215bd2f3eac11e48165282e053a2a0185b` | `f92dd876a667a9e59dd2460a4d273d9eeaae7d42` |
| `a4cf3a1ca6b7266a31f076345b67ebe37797cfe6` | `6c947f0acaef6e4e2229e8bf7978b4aa7f7393d3` |

## Auditoria de privacidade

Foram examinados metadados, mensagens e conteúdo de cada um dos 21 snapshots locais a publicar, não apenas a ponta da branch.

| Verificação | Resultado |
| --- | --- |
| E-mail pessoal em author/committer dos 21 commits reconstruídos | 0 ocorrências; todos usam o endereço `noreply` confirmado |
| Autor ou committer de terceiro nos 21 commits | Nenhum |
| Mensagens de commit contendo e-mail | 0 |
| E-mail pessoal no conteúdo versionado desses snapshots | 0 |
| Correspondências de e-mail no conteúdo histórico | 94 ocorrências repetidas; limitadas a páginas e documentos públicos de política/conformidade em `app/src/main/assets/privacy_policy.html`, `app/src/test/java/com/example/controlevendas/PrivacyPolicyTest.kt`, `site/index.html`, `site/privacy/index.html` e relatórios das Fases 5 e 6; nenhum e-mail pessoal identificado |
| Padrões de token, senha ou chave privada | Nenhuma correspondência |
| Caminho absoluto de perfil Windows | Nenhuma correspondência |
| Caminho de APK/AAB, banco local ou keystore | Nenhum nos snapshots |
| Commits já publicados em `origin/master` com e-mail pessoal do proprietário | 41 campos de autor e 41 de committer em 42 commits; preservados conforme o escopo |

As buscas de conteúdo são heurísticas e não provam ausência matemática de toda informação sensível. O resultado não equivale a privacidade histórica total, porque os 41 registros remotos já são públicos. Uma eventual remoção dessa história exigiria uma decisão e processo separado; não foi feita aqui.

## Configuração de commits futuros

O endereço confirmado foi configurado em `user.email` na configuração local compartilhada do repositório, para os próximos commits em seus worktrees. O worktree privado também tem a mesma identidade específica. A configuração global permaneceu inalterada. O endereço integral não é repetido neste relatório. O commit deste relatório usa essa identidade privada.

Versão permanece `versionCode 12` / `versionName 1.0.12`, `applicationId com.vendassimples.app`. O AAB original no worktree de integração permaneceu com 5.361.969 bytes e SHA-256 `4CF6052542FA842EC0796D16006A2912BC825039366C20AE7EB504A608CFA75D`; nenhuma tarefa de bundle ou assemble foi executada nesta fase.

## Testes e validações

Executados na nova branch com JBR 21, cache Gradle existente e modo offline:

- `:app:testDebugUnitTest`: 43 testes, 0 falhas, 0 erros, 0 ignorados.
- `:app:testReleaseUnitTest`: 43 testes, 0 falhas, 0 erros, 0 ignorados.
- `:app:lintDebug`: 0 erros e 67 avisos.
- `:app:lintRelease`: 0 erros e 67 avisos.
- Resultado conjunto: `BUILD SUCCESSFUL`, 61 tarefas executadas em 4m11s.
- `git diff --check origin/master...HEAD`: passou antes de incluir este relatório.
- Comparação de hashes de árvore: idêntica antes deste relatório; após ele, somente um arquivo Markdown será acrescentado.
- Metadados e mensagens: nomes, datas e textos preservados nos 21 pares; e-mails reconstruídos verificados.
- Links Markdown relativos: passaram após salvar este relatório.
- Verificação de versionCode/versionName: 12 / 1.0.12.
- AAB: hash e tamanho conferidos após os testes; não houve rebuild.
- `AndroidTest` em dispositivo/emulador: não executado nesta fase.

O lint mantém 67 avisos por variante; não foram suprimidos. A compilação também emitiu avisos de depreciação de construtores `Locale` e de `SOFT_INPUT_ADJUST_RESIZE`. Eles pertencem ao código idêntico ao da Fase 8B.1 e não foram alterados nesta fase.

## Estado final e próxima etapa

O relatório foi incluído em um commit documental adicional, usando a identidade `noreply` configurada no worktree. Não houve push, Pull Request, merge, tag, alteração em `master`, publicação Google Play, upload Alpha ou rebuild do AAB.

Próximo passo: revisar este relatório e, se aceitar a ressalva dos 41 commits já publicados, autorizar explicitamente o push normal apenas de `release/v12-github-privado`. A publicação da branch não altera nem corrige os metadados já visíveis em `origin/master`; nenhuma PR deve ser criada nesta fase.

Consulte também [Fase 8B.1 — histórico sanitizado](FASE8B1_HISTORICO_SANITIZADO.md), [arquivo histórico V0–V12](HISTORICO_COMPLETO_V0_V12.md), [AAB V12](FASE7I_AAB_V12.md) e [instruções do projeto](../AGENTS.md).
