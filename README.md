# Vendas Simples

Produto Android independente para controle local de vendas, parcelas e recebimentos.

**Estado da base (26/10/2026):** `master` contém a V14 (`versionCode 14`, `versionName 1.0.14`), o Light/Dark Mode da Fase 16 e a Interface Clean da Fase 17B (commit `21e5f824914339944efd6110ad91a0082178aad7`). A Fase 16 entrou pela PR #7; a Fase 17B foi integrada por fast-forward direto após o conector de PR recusar acesso. O proprietário informa que a V14 permanece em teste fechado; a Play Console não foi consultada. O proprietário relata aprovação visual da Fase 17B em claro e escuro no S22. Não houve preparação nem publicação da V15, que depende de autorização específica. Consulte [histórico de versões](docs/HISTORICO_VERSOES.md), [Fase 16](docs/FASE16_LIGHT_DARK_MODE.md), [Fase 17D — master consolidada](docs/FASE17D_MASTER_CONSOLIDADA.md) e [pendências](docs/PENDENCIAS_ATUAIS.md).

**V13 — marco integrado:** a tela Sobre e o contato iniciado pelo usuário foram integrados à master pela PR #4. Build debug, 46 testes unitários, compilação instrumentada e lint (0 erros/67 avisos) estão registrados no [relatório da Fase 13](docs/FASE13_SOBRE_APLICATIVO.md). O AAB local release foi gerado e validado no ciclo seguinte, com hash registrado no [histórico de versões](docs/HISTORICO_VERSOES.md). Testes instrumentados em runtime, atualização direta pela Play, API 24 e Billing real seguem pendentes. O proprietário informou estado Alpha “Em análise”; consulte [Pendências atuais](docs/PENDENCIAS_ATUAIS.md) para a classificação da evidência.

**V14 — versão-base preservada:** `versionCode 14` / `versionName 1.0.14`; o proprietário informa distribuição em teste fechado. A master mantém esses metadados após a integração Fase 16. A implementação de temas não prepara uma nova versão; veja o [relatório funcional V14](docs/FASE14_FUNCIONAL_PDF_PRIVACIDADE.md) e o [relatório Fase 16](docs/FASE16_LIGHT_DARK_MODE.md).

A referência é a branch `comercial` de `alejoiasvendasapp`, fixada no commit `ce112c40c4f79368042555e451338b5f318077bd`. O repositório original é somente leitura e não será alterado.

## Documentação

- [Índice central e memória histórica V0–V13](docs/INDEX.md): navegação por versão, assunto, manutenção e continuidade Codex.

- [Visão e escopo do MVP](docs/MVP.md): público proposto, fluxos, limites e critérios de aceite.
- [Referência AleJoias](docs/REFERENCIA.md): evidências, procedência e limites da análise.
- [Formato e importação CSV](docs/EXPORTACAO_CSV.md): UTF-8 padrão/Excel, precisão financeira e limitações de leitores.
- [Arquitetura e dados](docs/ARQUITETURA.md): base técnica, dados e procedência.
- [Backlog](docs/BACKLOG.md): etapas, dependências e entregas verificáveis.
- [Validação](docs/VALIDACAO.md): cenários para futura homologação.
- [Decisões](docs/DECISOES.md): requisitos confirmados, propostas e questões abertas.
- [Produto](docs/PRODUTO.md): posicionamento, limites do FREE e escopo do produto.
- [Monetização](docs/MONETIZACAO.md): FREE, Licença Completa (`FULL_ACCESS`), Billing e restauração.
- [Segurança e privacidade](docs/SEGURANCA_PRIVACIDADE.md): dados locais, licença e limites do MVP.
- [Release Play Store](docs/RELEASE_PLAY_STORE.md): checklist de publicação e configuração comercial.
- [Roadmap do produto](docs/ROADMAP_PRODUTO.md): fases após a consolidação do MVP.
- [Regras financeiras](docs/REGRAS_FINANCEIRAS.md): dinheiro em centavos e invariantes de cálculo.
- [Auditoria pré-Billing](docs/AUDITORIA_PRE_BILLING.md): achados, riscos e limitações antes da loja.
- [AGENTS.md](AGENTS.md): instruções para trabalhar neste repositório.
- [Histórico V0–V13](docs/HISTORICO_COMPLETO_V0_V13.md): continuação da síntese original até a V13.
- [Pendências atuais](docs/PENDENCIAS_ATUAIS.md): evidências e próximos passos para Alpha, Play e homologação.
- [Manutenção pós-lançamento](docs/MANUTENCAO_POS_LANCAMENTO.md): triagem e regressão segura.
- [Base reutilizável](docs/BASE_REUTILIZAVEL_NOVOS_APPS.md): padrões e limites para outro aplicativo independente.

## Skills do projeto

As skills ficam em `.agents/skills/`, com instruções locais versionadas:

- [vendas-simples-interface](.agents/skills/vendas-simples-interface/SKILL.md): UI, navegação, filtros, cards e diálogos.
- [vendas-simples-backup](.agents/skills/vendas-simples-backup/SKILL.md): destino, cópia e restauração SQLite.

- [vendas-simples-planejar](.agents/skills/vendas-simples-planejar/SKILL.md): evoluir escopo, backlog e decisões.
- [vendas-simples-referencia](.agents/skills/vendas-simples-referencia/SKILL.md): consultar a origem e preparar reaproveitamento rastreável.
- [vendas-simples-validar](.agents/skills/vendas-simples-validar/SKILL.md): validar regras financeiras e independência do produto.
- [vendas-simples-monetizacao](.agents/skills/vendas-simples-monetizacao/SKILL.md): trabalhar com FREE, FULL_ACCESS e Google Play Billing.
- [vendas-simples-seguranca](.agents/skills/vendas-simples-seguranca/SKILL.md): revisar privacidade, licença, dados locais e migrações.
- [vendas-simples-release](.agents/skills/vendas-simples-release/SKILL.md): preparar release e checklist da Play Store.

## Próxima etapa

Consultar [Pendências atuais](docs/PENDENCIAS_ATUAIS.md). A Fase 16 está integrada e homologada; qualquer preparação da V15 requer autorização específica do proprietário. Status da V14 na Play Console não foi verificado nesta atualização.

As mudanças funcionais da V14 e a implementação visual da Fase 16 estão integradas na `master`. A futura V15 permanece pendente de autorização e de planejamento de release; consulte [decisões](docs/DECISOES.md), [roadmap](docs/ROADMAP_PRODUTO.md) e [pendências](docs/PENDENCIAS_ATUAIS.md).

## Marco V10 beta da Store

Identificação solicitada pelo usuário: `v10-beta-store`, versionCode 10 e
versionName 1.0.0. Consulte [registro do marco](docs/MARCO_V10_BETA_STORE.md).

## Versão 11 — personalização

Nome do negócio configurável offline, oferecido uma vez no primeiro acesso à nova funcionalidade e editável em Personalizar negócio no menu de três pontos. Cabeçalho mantém a marca Vendas Simples, o nome em linha secundária e a frase Seu controle de vendas. Nome na cobrança/agradecimento e resumo compartilhado, além dos três PDFs existentes. Datas completas apresentadas em `dd/MM/yyyy`, com persistência ISO preservada. Miniatura e Remover foto ficam lado a lado com 16 dp, empilhados quando largura/fonte exigem.

Consulte [relatório da versão 11](docs/RELEASE_V11.md) para artefato, verificações e homologação pendente. O marco V10 permanece como histórico.

O backup completo FULL_ACCESS inclui o nome no mesmo arquivo SQLite. Backups anteriores continuam restauráveis e preservam o nome do aparelho quando não contêm essa informação. Personalização permanece disponível no FREE.

# Skills locais do projeto

As skills em `.agents/skills/` ensinam procedimentos deste repositório. Uma adaptação para outro aplicativo precisa ser revisada conforme [Base reutilizável](docs/BASE_REUTILIZAVEL_NOVOS_APPS.md), removendo identidade e regras específicas.

Este pacote registra decisões e evidências relatadas em conversa, não uma auditoria do código atual. Não substitui AGENTS.md, relatórios de release nem homologação real.

## Fase 2 — segurança e conformidade

Correções em desenvolvimento na branch fix/v12-seguranca-conformidade, sem modificar o artefato Alpha V11. Consulte [relatório técnico e homologação](docs/FASE2_SEGURANCA_CONFORMIDADE.md) e [investigação de Segurança dos dados/Billing](docs/SEGURANCA_DADOS_PLAY.md). Não representa nova publicação nem incremento de versão.

Revisão posterior: [Fase 3 — revisão final e plano de homologação/V12](docs/FASE3_REVISAO_HOMOLOGACAO.md), com análise de lint, política pública, rastreabilidade e critérios para a futura candidata. Não autoriza publicação.

Correções técnicas posteriores: [Fase 4 — lint, compatibilidade e regressão](docs/FASE4_CORRECOES_TECNICAS.md). Versão de trabalho preservada em 11/1.0.11, sem nova publicação.

## V12 — integração e validação

A V12 permanece como histórico de integração e preparação; seus relatórios documentam o AAB local daquele marco, sem comprovar release pública. O marco corrente é V13, integrado pela PR #4. Consulte o [histórico V0–V13](docs/HISTORICO_COMPLETO_V0_V13.md) e a [linha do tempo detalhada](docs/HISTORICO_VERSOES.md).
