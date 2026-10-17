# Decisões históricas do Vendas Simples

Esta linha do tempo aponta para decisões já registradas. A fonte normativa atual continua sendo [DECISOES.md](DECISOES.md), [MONETIZACAO.md](MONETIZACAO.md), [REGRAS_FINANCEIRAS.md](REGRAS_FINANCEIRAS.md) e [AGENTS.md](../AGENTS.md). Repetição em backlog ou histórico não torna proposta aprovada.

| Período | Decisão/marco | Fonte e estado |
|---|---|---|
| 29/09 — início | Criar produto independente Vendas Simples, usar a referência apenas seletivamente e manter origem AleJoias somente leitura. | `docs/DECISOES.md` D-01–D-06; usuário confirmado. |
| 29/09 — produto/comercial | FREE até 30 vendas; clientes ilimitados; dados e funcionalidades existentes continuam consultáveis; compra única FULL_ACCESS, sem assinatura; sem conta/backend antifraude no MVP. | D-07–D-12 e `MONETIZACAO.md`; decisões confirmadas, implementações rastreáveis em `692c663`, `b7bef44`, commits Billing. |
| 29/09 — dinheiro/dados | Valores novos em centavos inteiros ou decimal seguro; mudanças de schema exigem migração; falha de licença não apaga dados. | D-13/D-14, `REGRAS_FINANCEIRAS.md`; implementação centavos em `b6befe5`. |
| 30/09–02/10 — release/Billing | A compra é via Play, restauração de compra separada de restauração de dados, preço vem de ProductDetails/Play; backup exige FULL_ACCESS. | `MONETIZACAO.md`, `RELEASE_PLAY_STORE.md`, commits `2500ef5`, `a18a4d0`, `d8e6fcf`. |
| 10/10 — V10 | Usuário denominou teste beta da Store `v10-beta-store`, code10/name1.0.0 e autorizou naquela fase publicar alterações com label; não significa autorização permanente de pushes nem status atual da Console. | [MARCO_V10_BETA_STORE](MARCO_V10_BETA_STORE.md), tag local. |
| 11/10 — V11 | Manter code11/name1.0.11; personalização disponível no FREE; nome em mensagens/PDF e backup FULL_ACCESS, compatibilidade com backup anterior; preservar schema financeiro, billing e limites. | [RELEASE_V11](RELEASE_V11.md), complemento em `DECISOES.md`. |
| 15/10 — privacidade | Usar URL oficial confirmada pelo usuário e manter texto local/offline. Confirmação da URL não prova paridade pública nem autoriza modificar Play Console. | `docs/DECISOES.md` revisão 15/10, Fases 2/6A/6C. |
| 16/10 — V12 | Atualização técnica para 12/1.0.12, integração controlada em worktree, preservar V11 e tag, validar sem assumir upload/produção. | `docs/FASE7A`–`7J`; merge local `6434485`; upload não realizado. |
| Fase 8A — histórico | Documentar história e criar commits exclusivamente documentais na branch integrada; sem push/PR; preservar código e artefatos. | Pedido da Fase 8A. |

## Decisões que continuam abertas

Status atual da declaração Data Safety; categorias e retenção de Billing/SDK; confirmação atual da página pública; faixa e artefato enviados à Play; homologação física V12 e API 24; produção. Ver [FASE6C](FASE6C_SEGURANCA_DADOS_PLAY.md) e [FASE7J](FASE7J_PREPARACAO_ALPHA_V12.md). O histórico não resolve essas perguntas automaticamente.
