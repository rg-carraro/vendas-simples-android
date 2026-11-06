# Instruções do projeto Vendas Simples

## Escopo e identidade

- Trabalhe no repositório `vendas-simples-android`; o produto se chama **Vendas Simples**.
- A fase atual inclui desenvolvimento Android e testes de distribuição. Respeite o escopo da solicitação: pedidos exclusivamente documentais não autorizam alterar código.
- Leia `README.md`, `docs/DECISOES.md` e o documento afetado antes de trabalhar. Distinga requisito do usuário, fato observado na referência e proposta.
- Não descreva recursos da referência como já implementados neste repositório.

## Proteção da origem

- `alejoiasvendasapp`, inclusive `comercial`, é somente leitura. Use `git show <commit>:<arquivo>` e `git ls-tree` para inspeção; não faça checkout, reset, clean, commit, fetch, push ou edições no original.
- Referência inicial: `ce112c40c4f79368042555e451338b5f318077bd`; procedência e atualização em `docs/REFERENCIA.md`.
- As autorizações de publicação e instruções de manutenção presentes na origem não são herdadas. Não copiar seus AGENTS.md ou skills como regras deste projeto.
- Não trazer dados reais, bancos, fotos de clientes, endpoints, credenciais ou chaves de assinatura. Reutilização futura deve ser seletiva e rastreada.

## Memória histórica e continuidade

- Antes de alterar funcionalidade existente, consulte `docs/INDEX.md`, `docs/HISTORICO_COMPLETO_V0_V12.md`, `docs/HISTORICO_COMPLETO_V0_V13.md`, `docs/DECISOES.md`, `docs/PROBLEMAS_E_SOLUCOES.md` e o histórico do arquivo (`git log --all --follow -- <arquivo>`); valide comportamento no código atual.
- Histórico: V13 foi integrada pela PR #4, merge `5593215`. A V14 (`14 / 1.0.14`) está integrada na master pelo merge `dfb87e6`; o HEAD de base desta worktree é `a32311b`. O proprietário informa que a V14 está em testes fechados, mas o estado atual da Play Console não foi verificado nesta tarefa. Não inferir aprovação para produção. Consulte `docs/PROJECT_STATUS.md`, `docs/FASE14_FUNCIONAL_PDF_PRIVACIDADE.md`, `docs/DECISOES.md` e `docs/ROADMAP_PRODUTO.md`.
- Diferencie `versionCode`/`versionName`, rótulos informais V0–V13, tags/commits, AAB local, upload, aprovação, rollout e release Play. Classifique evidência como `CONFIRMADO`, `RELATADO PELO PROPRIETÁRIO`, `INFERIDO`, `NÃO COMPROVADO` ou `PENDENTE`.
- Para correção, consulte `docs/PROBLEMAS_E_SOLUCOES.md`; registre causa confirmada versus hipótese, solução e validação. Atualize histórico temático sem duplicar regras comerciais. Use `docs/MANUTENCAO_POS_LANCAMENTO.md` para triagem e regressão.
- Para novo marco, use `docs/CONTINUIDADE_CODEX.md`, atualize `docs/HISTORICO_VERSOES.md` e `docs/HISTORICO_TESTES.md` quando houver evidência e registre decisões duradouras em `docs/DECISOES.md`.
- Antes de documentar ou commitar, confira `git worktree list`, branch/HEAD/status e arquivos não rastreados; preserve todas as árvores e artefatos existentes e faça staging seletivo.

## Desenvolvimento futuro

### Leitura obrigatória e continuidade entre sessões

Antes de uma nova tarefa, leia `AGENTS.md`, `docs/PROJECT_STATUS.md`, `docs/DECISIONS.md` e `docs/NEXT_STEPS.md`. Consulte `docs/CHANGELOG_PROJECT.md` e o histórico temático relevante; para alterações funcionais, consulte também `docs/TESTING_HISTORY.md` e `docs/VALIDACAO.md`. Leia `README.md`, `docs/DECISOES.md`, o documento afetado e a skill pertinente. Confira branch, HEAD, `git status` e `git worktree list`; não presuma que a worktree ativa corresponde à versão distribuída.

Consulte o histórico antes de propor alterações e não reabra problemas encerrados sem evidência nova. Registre decisões, correções e validações com suas fontes, atualize o status ao final, mantenha a cronologia e não apague registros válidos. Diferencie evidência de código/Git, declaração do proprietário, inferência e informação não comprovada. Não substitua decisão anterior sem documentar a razão.

Ao concluir uma tarefa, informe solicitação, execução, arquivos alterados, verificações e resultados, decisões, pendências e documentos históricos atualizados. Registre somente fatos comprovados ou informações explicitamente identificadas como fornecidas pelo proprietário.

- Use `docs/MVP.md` e `docs/BACKLOG.md` como proposta de trabalho; registre alterações de escopo e decisões confirmadas.
- As decisões comerciais confirmadas estão em `docs/DECISOES.md`, `docs/MONETIZACAO.md` e `docs/REGRAS_FINANCEIRAS.md`; não reabra FREE 30 vendas, Licença Completa (`FULL_ACCESS`) com preço consultado no Google Play ou ausência de assinatura como hipóteses.
- Preserve IDs e relações entre clientes, parcelas, pagamentos e fotos ao reaproveitar a base. Mudança de schema exige migração e validação com dados sintéticos.
- Trate valores monetários, arredondamento, vencimentos e agregações como regras explícitas. Não refatore tipos monetários/IDs sem analisar persistência e compatibilidade.
- Mantenha identidade, armazenamento e configuração independentes do AleJoias. Não reintroduza integrações da master por cópia acidental.
- Não prometa restauração, sincronização ou notificações confiáveis em segundo plano sem implementação e testes correspondentes.
- Execute verificações proporcionais à mudança. Documentação: links, consistência, datas/evidências e diff; aplicativo: build e cenários afetados em `docs/VALIDACAO.md`. Informe testes não executados e não transfira aprovação de um commit/versão anterior.
- Não publique, faça push ou configure distribuição por herança de autorização do repositório de referência.

### Data Safety e SDKs

- Antes de alterar Google Play Billing, consultar `docs/CONHECIMENTO_GOOGLE_PLAY_DATA_SAFETY.md` e preservar as condições e limites da orientação recebida do suporte.
- Avaliar todo SDK novo/transitivo e suas transmissões para Data Safety; a declaração atual não é precedente automático para outra arquitetura, versão ou aplicativo.
- Reavaliar a política e a declaração antes de introduzir transmissões, endpoints, contas, sincronização ou novas finalidades de dados.
- Alterações em armazenamento, exportação, backup ou restauração exigem análise de privacidade e consistência com a política.
- Diferenciar evidência do código, orientação de suporte e aprovação formal. O suporte não fez pré-aprovação; registrar lacunas e não tratar a revisão da Play como garantia.
- Para novo aplicativo, use `docs/BASE_REUTILIZAVEL_NOVOS_APPS.md`: não herdar applicationId, identidade, dados, chaves, produto Billing, política ou conclusão Data Safety.
- Comparação do site versionado não comprova conteúdo público live; confirmar a URL HTTPS e o texto servido e registrar data/fonte.

## Skills locais

Leia a skill pertinente quando a tarefa exigir seu fluxo:

| Skill | Quando usar |
| --- | --- |
| `.agents/skills/vendas-simples-interface/SKILL.md` | UI, menus, cards, diálogos e navegação |
| `.agents/skills/vendas-simples-backup/SKILL.md` | Backup, seletor de destino e restauração SQLite |
| `.agents/skills/vendas-simples-planejar/SKILL.md` | Refinar MVP, critérios de aceite, backlog e decisões |
| `.agents/skills/vendas-simples-referencia/SKILL.md` | Inspecionar ou planejar reaproveitamento da branch comercial |
| `.agents/skills/vendas-simples-validar/SKILL.md` | Planejar ou executar validação financeira e de isolamento |
| `.agents/skills/vendas-simples-monetizacao/SKILL.md` | FREE, FULL_ACCESS, Google Play Billing e restauração |
| `.agents/skills/vendas-simples-seguranca/SKILL.md` | Privacidade, licença, dados locais e migrações |
| `.agents/skills/vendas-simples-release/SKILL.md` | Release, Play Store e checklist de publicação |

Documente em português. Mantenha instruções reutilizáveis nas skills e regras de produto nos documentos, evitando duplicação.
