# Vendas Simples

Produto Android independente para controle local de vendas, parcelas e recebimentos.

**Fase atual: documentação e definição do MVP.** Este repositório ainda não contém aplicativo, Gradle ou APK. Nenhuma funcionalidade está implementada aqui.

A referência é a branch `comercial` de `alejoiasvendasapp`, fixada no commit `ce112c40c4f79368042555e451338b5f318077bd`. O repositório original é somente leitura e não será alterado.

## Documentação

- [Visão e escopo do MVP](docs/MVP.md): público proposto, fluxos, limites e critérios de aceite.
- [Referência AleJoias](docs/REFERENCIA.md): evidências, procedência e limites da análise.
- [Arquitetura e dados](docs/ARQUITETURA.md): base técnica e cuidados para a futura derivação.
- [Backlog](docs/BACKLOG.md): etapas, dependências e entregas verificáveis.
- [Validação](docs/VALIDACAO.md): cenários para futura homologação.
- [Decisões](docs/DECISOES.md): requisitos confirmados, propostas e questões abertas.
- [AGENTS.md](AGENTS.md): instruções para trabalhar neste repositório.

## Skills do projeto

As skills ficam em `.agents/skills/`, com instruções locais versionadas:

- [vendas-simples-planejar](.agents/skills/vendas-simples-planejar/SKILL.md): evoluir escopo, backlog e decisões.
- [vendas-simples-referencia](.agents/skills/vendas-simples-referencia/SKILL.md): consultar a origem e preparar reaproveitamento rastreável.
- [vendas-simples-validar](.agents/skills/vendas-simples-validar/SKILL.md): validar regras financeiras e independência do produto.

## Próxima etapa

Consolidar as decisões abertas do MVP e, quando solicitado o desenvolvimento, trazer seletivamente a base comercial para este repositório. Não há comando de build aplicável nesta fase. O histórico de builds da referência não comprova validação deste produto.
