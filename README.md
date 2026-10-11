# Vendas Simples

Produto Android independente para controle local de vendas, parcelas e recebimentos.

**Fase atual: desenvolvimento Android e preparação da versão 11 (1.0.11), solicitada em 11/10/2026.** O repositório contém aplicativo, Gradle, testes locais e integração Google Play Billing. O usuário informou 19 cenários aprovados no Alpha; essa informação não substitui a validação das alterações atuais.

A referência é a branch `comercial` de `alejoiasvendasapp`, fixada no commit `ce112c40c4f79368042555e451338b5f318077bd`. O repositório original é somente leitura e não será alterado.

## Documentação

- [Visão e escopo do MVP](docs/MVP.md): público proposto, fluxos, limites e critérios de aceite.
- [Referência AleJoias](docs/REFERENCIA.md): evidências, procedência e limites da análise.
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

Validar as melhorias de UI/UX e Billing em atualização pela Play. Usar `gradlew.bat testDebugUnitTest assembleDebug bundleRelease` com JDK/SDK e assinatura locais configurados. Consulte [relatório da atualização](docs/ATUALIZACAO_ALPHA_UI_BILLING.md).

## Marco V10 beta da Store

Identificação solicitada pelo usuário: `v10-beta-store`, versionCode 10 e
versionName 1.0.0. Consulte [registro do marco](docs/MARCO_V10_BETA_STORE.md).

## Versão 11 — personalização

Nome do negócio configurável offline, oferecido uma vez no primeiro acesso à nova funcionalidade e editável no menu principal em Configurações. Nome na tela inicial, cobrança/agradecimento e resumo compartilhado, além dos três PDFs existentes. Datas completas apresentadas em `dd/MM/yyyy`, com persistência ISO preservada, e espaço de 16 dp entre miniatura e Remover foto.

Consulte [relatório da versão 11](docs/RELEASE_V11.md) para artefato, verificações e homologação pendente. O marco V10 permanece como histórico.

O backup completo FULL_ACCESS inclui o nome no mesmo arquivo SQLite. Backups anteriores continuam restauráveis e preservam o nome do aparelho quando não contêm essa informação. Personalização permanece disponível no FREE.
