# Fase 9A — Sincronização pós-merge e preparação Alpha V12

**Data:** 16/10/2026 (horário de São Paulo)
**Repositório:** `rg-carraro/vendas-simples-android`
**Classificação:** **MASTER SINCRONIZADA — BASE V12 PREPARADA PARA HOMOLOGAÇÃO ALPHA, com pendências externas.**

Este relatório registra a base mesclada e os checks locais. Preparação não significa homologação física completa, conformidade confirmada na Play Console, upload ou publicação.

## 1. Estado do merge e sincronização Git

- A API do GitHub confirmou `origin/master` em `11214de6aac8bf1072c1767344c725939d3b1802`, merge commit da PR #2. Seus pais são `fb59cf96392b80c63a42974c80c1b91be1657e4c` e o head revisado `000cf5fc351bd03056323b7a217495880895effd`.
- `git fetch origin` atualizou a referência remota de `fb59cf9` para `11214de`.
- A `master` local estava em `99f41e70984d1aa25cc9eca89cc5c6067f55f47b`. A ancestralidade foi confirmada e a referência local avançou por fast-forward, sem checkout, merge adicional ou mudança na árvore de trabalho ativa.
- SHA final local e remoto: `11214de6aac8bf1072c1767344c725939d3b1802`.
- A árvore de `master` tem o tree SHA `71421d266fd3804e7c7263ffd00ca31e97155edc`, igual à árvore do head sanitizado revisado da PR.
- Worktree novo para continuidade: `.worktrees/master-post-merge`, branch `master`, no merge commit esperado. Este relatório está como arquivo não commitado nesse worktree.
- O checkout principal permanece na branch `fix/v12-seguranca-conformidade`, HEAD `628b9f43ffa5bd0a7e478935729e0e45b3473228`. Seus itens não rastreados preexistentes (`.codex/`, `.private-originals-fase7g2/`, `.worktrees/` e três relatórios Fase 7G) foram preservados. Nenhuma branch ou worktree anterior foi removida.
- Worktrees de política, integração, histórico sanitizado e publicação privada permanecem presentes. Nenhum histórico, tag ou artefato anterior foi apagado.

## 2. Conteúdo e equivalência funcional

`AGENTS.md`, skills do projeto, `docs/INDEX.md`, `docs/HISTORICO_COMPLETO_V0_V12.md`, relatórios 7H–7J, documentação de segurança, política offline e documentação de continuidade estão presentes na `master` mesclada. O histórico diferencia commits, versões Android, AAB local e publicação Play; a V12 não é descrita como publicada.

A comparação da árvore mesclada com o head revisado da PR é idêntica. A validação anterior à PR comparou o código funcional com a integração V12 testada e encontrou somente diferenças documentais. Assim, o AAB gerado antes da reconstrução documental/merge corresponde ao mesmo código funcional; o merge GitHub também preservou a árvore do head da PR.

Configuração observada em `app/build.gradle` e no código:

| Item | Valor / comportamento |
|---|---|
| Application ID | `com.vendassimples.app` |
| versionCode / versionName | `12` / `1.0.12` |
| minSdk / targetSdk | `24` / `36` |
| Google Play Billing | `9.1.0` |
| Produto | `full_access`, compra única INAPP |
| FREE | Até 30 vendas; bloqueia a 31ª |
| Backup/restauração | Exigem `FULL_ACCESS`; a licença não é importada do backup |
| Política | URL oficial configurada no app e texto offline incluído |

## 3. Testes e verificações

Executados na worktree `release/v12-github-privado` no commit `000cf5fc351bd03056323b7a217495880895effd`, cuja árvore é idêntica à da `master` mesclada:

- `:app:testDebugUnitTest`: **43 testes**, 0 falhas, 0 erros, 0 ignorados.
- `:app:testReleaseUnitTest`: **43 testes**, 0 falhas, 0 erros, 0 ignorados.
- `:app:lintDebug`: 0 erros, 67 avisos.
- `:app:lintRelease`: 0 erros, 67 avisos.
- Resultado: `BUILD SUCCESSFUL`; execução offline com JBR 21, Gradle 8.11.1 e cache local.
- `git diff --check` no head da PR passou; a worktree nova da `master` também estava limpa antes deste relatório.
- A compilação AndroidTest foi concluída na revisão imediatamente anterior sobre a mesma árvore; os testes instrumentados **não foram executados** em dispositivo/emulador.

Os testes unitários e o lint não comprovam compra real do Play, instalação por faixa Alpha, upgrade físico ou compatibilidade de runtime em API 24.

## 4. AAB V12 preservado

Artefato existente em `.worktrees/integration-v12-master-validation/app/build/outputs/bundle/release/app-release.aab`:

- Tamanho conferido: **5.361.969 bytes**.
- SHA-256 conferido: **`4CF6052542FA842EC0796D16006A2912BC825039366C20AE7EB504A608CFA75D`**.
- A Fase 7I registra manifesto `com.vendassimples.app`, versionCode 12, versionName 1.0.12, minSdk 24, targetSdk 36, validação bundletool e assinatura correspondente à chave de upload.
- O hash atual coincide com a Fase 7I. Este AAB foi produzido antes do merge documental; não foi reconstruído, copiado ou sobrescrito nesta fase. A evidência da assinatura é a verificação anterior da Fase 7I, não uma nova extração feita aqui.

## 5. Pendências de conformidade e homologação

- **Homologação física:** ainda executar a V12 em aparelho, inclusive no Galaxy S22+ / Android 16, sem substituir a versão V11 instalada por APK debug.
- **Upgrade V11 → V12:** testar pela faixa Play de teste e comparar clientes, vendas, parcelas, pagamentos parciais, fotos, saldos, nome do negócio e licença antes/depois.
- **API 24:** validar instalação, inicialização, vendas, parcelas, pagamentos, PDF/CSV, compartilhamento, permissões e modo offline em dispositivo/emulador compatível.
- **Billing real de teste:** consultar `full_access`, verificar compra aprovada, reconhecimento e restauração; cobrir cancelamento, pendência e indisponibilidade sem perda de dados.
- **Backup:** em instalação descartável e com dados sintéticos, verificar bloqueio no FREE, restauração FULL_ACCESS, fotos e nome personalizado; incluir backup legado, arquivo inválido e recuperação após falha.
- **Data Safety:** revisar as respostas efetivamente salvas, versões/faixas ativas e eventos/dados dos SDKs na Play Console. Essa revisão não foi feita nesta fase.
- **Política pública:** o código contém a URL oficial e o asset local está presente. A tentativa de abrir a URL pública pela ferramenta de navegação não foi acessível; portanto, disponibilidade atual e correspondência integral entre a página pública e o texto offline **continuam sem confirmação**.

## 6. Sequência prática para o upload Alpha — não executada

1. Na Play Console, selecionar `com.vendassimples.app`, confirmar disponibilidade do versionCode 12 e escolher a faixa Alpha/teste adequada.
2. Antes do upload, confirmar localmente o SHA-256 do AAB acima e selecionar exatamente esse arquivo, sem recompilá-lo.
3. Conferir package, versionCode/name e assinatura exibidos pela Console; parar diante de qualquer divergência ou bloqueio inesperado.
4. Revisar as notas de versão propostas na Fase 7J e todas as mensagens/alertas da Console.
5. Resolver a revisão de Data Safety e confirmar a política pública; não declarar respostas sem evidência do código/SDK e da Console.
6. Revisar a lista e elegibilidade dos testadores e as instruções de acesso.
7. Depois de eventual disponibilização autorizada, executar os cenários de upgrade, API 24, Billing e backup acima e registrar evidências sanitizadas.

Notas de versão propostas na Fase 7J (revisar no contexto da Console antes de usar):

> Melhorias na validação e transferência de backups, com ajustes para proteger seus dados durante a restauração. Também aprimoramos a estabilidade e a compatibilidade do aplicativo.

Nenhuma etapa de upload, envio para análise, distribuição Alpha ou publicação foi executada. A ação de publicar continua dependendo de autorização explícita.

## 7. Estado final

`master` local e `origin/master` estão sincronizadas em `11214de6aac8bf1072c1767344c725939d3b1802`, com worktree dedicado pronto em `.worktrees/master-post-merge`. A base V12 está preparada para continuar a homologação Alpha, mas as pendências físicas e de conformidade acima impedem afirmar que a V12 já foi homologada ou que está pronta para distribuição. Não houve alteração funcional, de versão, banco, monetização ou assinatura; também não houve commit, push, tag, geração de AAB ou publicação.
