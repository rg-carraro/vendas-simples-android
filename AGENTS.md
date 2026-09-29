# Instruções do projeto Vendas Simples

## Escopo e identidade

- Trabalhe no repositório `vendas-simples-android`; o produto se chama **Vendas Simples**.
- A etapa atual é documentação, skills e estruturação do MVP. Não iniciar implementação Android a partir desta solicitação de planejamento. Uma solicitação posterior de desenvolvimento pode avançar a fase sem nova confirmação rotineira.
- Leia `README.md`, `docs/DECISOES.md` e o documento afetado antes de trabalhar. Distinga requisito do usuário, fato observado na referência e proposta.
- Não descreva recursos da referência como já implementados neste repositório.

## Proteção da origem

- `alejoiasvendasapp`, inclusive `comercial`, é somente leitura. Use `git show <commit>:<arquivo>` e `git ls-tree` para inspeção; não faça checkout, reset, clean, commit, fetch, push ou edições no original.
- Referência inicial: `ce112c40c4f79368042555e451338b5f318077bd`; procedência e atualização em `docs/REFERENCIA.md`.
- As autorizações de publicação e instruções de manutenção presentes na origem não são herdadas. Não copiar seus AGENTS.md ou skills como regras deste projeto.
- Não trazer dados reais, bancos, fotos de clientes, endpoints, credenciais ou chaves de assinatura. Reutilização futura deve ser seletiva e rastreada.

## Desenvolvimento futuro

- Use `docs/MVP.md` e `docs/BACKLOG.md` como proposta de trabalho; registre alterações de escopo e decisões confirmadas.
- As decisões comerciais confirmadas estão em `docs/DECISOES.md`, `docs/MONETIZACAO.md` e `docs/REGRAS_FINANCEIRAS.md`; não reabra FREE 30 vendas, LIFETIME R$ 49,90 ou ausência de assinatura como hipóteses.
- Preserve IDs e relações entre clientes, parcelas, pagamentos e fotos ao reaproveitar a base. Mudança de schema exige migração e validação com dados sintéticos.
- Trate valores monetários, arredondamento, vencimentos e agregações como regras explícitas. Não refatore tipos monetários/IDs sem analisar persistência e compatibilidade.
- Mantenha identidade, armazenamento e configuração independentes do AleJoias. Não reintroduza integrações da master por cópia acidental.
- Não prometa restauração, sincronização ou notificações confiáveis em segundo plano sem implementação e testes correspondentes.
- Execute verificações proporcionais à mudança. Documentação: links, consistência e diff; aplicativo: build e cenários afetados em `docs/VALIDACAO.md`. Informe testes não executados.
- Não publique, faça push ou configure distribuição por herança de autorização do repositório de referência.

## Skills locais

Leia a skill pertinente quando a tarefa exigir seu fluxo:

| Skill | Quando usar |
| --- | --- |
| `.agents/skills/vendas-simples-planejar/SKILL.md` | Refinar MVP, critérios de aceite, backlog e decisões |
| `.agents/skills/vendas-simples-referencia/SKILL.md` | Inspecionar ou planejar reaproveitamento da branch comercial |
| `.agents/skills/vendas-simples-validar/SKILL.md` | Planejar ou executar validação financeira e de isolamento |
| `.agents/skills/vendas-simples-monetizacao/SKILL.md` | FREE, LIFETIME, Google Play Billing e restauração |
| `.agents/skills/vendas-simples-seguranca/SKILL.md` | Privacidade, licença, dados locais e migrações |
| `.agents/skills/vendas-simples-release/SKILL.md` | Release, Play Store e checklist de publicação |

Documente em português. Mantenha instruções reutilizáveis nas skills e regras de produto nos documentos, evitando duplicação.
