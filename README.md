# Vendas Simples

Produto Android independente para controle local de vendas, parcelas e recebimentos.

**Estado registrado em 16/10/2026:** a branch isolada `integration/v12-master-validation` integra o commit `64344855` e tem regressão automatizada e AAB local V12 validados. A homologação física V12, conferência atual da Play Console/Data Safety e upload Alpha não estão comprovados. Isso não equivale a publicação nem aprovação de produção.

**Candidato V13 (19/10/2026):** a branch `feature/v13-sobre-aplicativo`, baseada na master após a integração da PR #3, prepara `versionCode 13` / `versionName 1.0.13` com tela Sobre e canal de contato por e-mail iniciado pelo usuário. Build debug, 46 testes unitários, compilação instrumentada e lint (0 erros/67 avisos) passaram; testes instrumentados em runtime e atualização direta da Play continuam pendentes. O AAB V13 ainda não foi gerado. Consulte [relatório V13](docs/FASE13_SOBRE_APLICATIVO.md) para o estado do PR e o hash do APK debug.

A referência é a branch `comercial` de `alejoiasvendasapp`, fixada no commit `ce112c40c4f79368042555e451338b5f318077bd`. O repositório original é somente leitura e não será alterado.

## Documentação

- [Índice central e memória histórica V0–V12](docs/INDEX.md): navegação por versão, assunto e continuidade Codex.

- [Visão e escopo do MVP](docs/MVP.md): público proposto, fluxos, limites e critérios de aceite.
- [Referência AleJoias](docs/REFERENCIA.md): evidências, procedência e limites da análise.
- [Formato e importação CSV](docs/EXPORTACAO_CSV.md): UTF-8 padrão/Excel, precisão financeira e limitações de leitores.
- [Arquitetura e dados](docs/ARQUITETURA.md): base técnica e cuidados para a futura derivação.
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

Resolver as pendências de conformidade/Play Console e executar homologação física V11→V12 e API 24 antes de avaliar a faixa Alpha. Consulte [Fases 7H–7J](docs/INDEX.md#documentos-de-produto-e-implementação); nenhum upload foi feito nesta preparação.

## Marco V10 beta da Store

Identificação solicitada pelo usuário: `v10-beta-store`, versionCode 10 e
versionName 1.0.0. Consulte [registro do marco](docs/MARCO_V10_BETA_STORE.md).

## Versão 11 — personalização

Nome do negócio configurável offline, oferecido uma vez no primeiro acesso à nova funcionalidade e editável em Personalizar negócio no menu de três pontos. Cabeçalho mantém a marca Vendas Simples, o nome em linha secundária e a frase Seu controle de vendas. Nome na cobrança/agradecimento e resumo compartilhado, além dos três PDFs existentes. Datas completas apresentadas em `dd/MM/yyyy`, com persistência ISO preservada. Miniatura e Remover foto ficam lado a lado com 16 dp, empilhados quando largura/fonte exigem.

Consulte [relatório da versão 11](docs/RELEASE_V11.md) para artefato, verificações e homologação pendente. O marco V10 permanece como histórico.

O backup completo FULL_ACCESS inclui o nome no mesmo arquivo SQLite. Backups anteriores continuam restauráveis e preservam o nome do aparelho quando não contêm essa informação. Personalização permanece disponível no FREE.

# Pacote de skills — Vendas Simples / AleJoias

Copie `.agents/skills/` para o repositório que utilizará o Codex (ou adapte à estrutura de skills já adotada no projeto). Leia `docs/GUIA_MIGRACAO_ALEJOIAS.md` antes de portar melhorias ao AleJoias.

Este pacote registra decisões e evidências relatadas em conversa, não uma auditoria do código atual. Não substitui AGENTS.md, relatórios de release nem homologação real.

## Fase 2 — segurança e conformidade

Correções em desenvolvimento na branch fix/v12-seguranca-conformidade, sem modificar o artefato Alpha V11. Consulte [relatório técnico e homologação](docs/FASE2_SEGURANCA_CONFORMIDADE.md) e [investigação de Segurança dos dados/Billing](docs/SEGURANCA_DADOS_PLAY.md). Não representa nova publicação nem incremento de versão.

Revisão posterior: [Fase 3 — revisão final e plano de homologação/V12](docs/FASE3_REVISAO_HOMOLOGACAO.md), com análise de lint, política pública, rastreabilidade e critérios para a futura candidata. Não autoriza publicação.

Correções técnicas posteriores: [Fase 4 — lint, compatibilidade e regressão](docs/FASE4_CORRECOES_TECNICAS.md). Versão de trabalho preservada em 11/1.0.11, sem nova publicação.

## V12 — integração e validação

A branch isolada `integration/v12-master-validation` prepara `versionCode 12` / `versionName 1.0.12`. A integração local, regressão automatizada e AAB assinado foram registrados nas Fases 7H/7I; o envio Alpha, homologação física V12, verificação atual da Console e publicação continuam pendentes. Consulte os [relatórios de integração e regressão](docs/FASE7H_INTEGRACAO_REGRESSAO_V12.md), [AAB](docs/FASE7I_AAB_V12.md) e [preparação Alpha](docs/FASE7J_PREPARACAO_ALPHA_V12.md). A criação de AAB local não comprova upload ou release Play.
