# Histórico integral do Vendas Simples — V0 a V12

## Origem e método

O primeiro registro de concepção é `9c265085` (29/09/2026, estrutura inicial do MVP); a primeira base Android é `5c8c4f2` no mesmo dia. A partir da primeira árvore Android, o app já tinha Activity programática extensa, `SQLiteOpenHelper`, modelos e fluxos de vendas/clientes/fotos. “V0 — Origem do Vendas Simples” é um nome documental para esse começo, não uma versão oficial nem tag. O projeto usou como referência uma aplicação comercial anterior; essa referência não é a origem Git do Vendas Simples e permanece somente leitura conforme [REFERENCIA.md](REFERENCIA.md).

A reconstrução usa refs e commits disponíveis no clone, configurações Gradle históricas e documentação existente. O estado consultado tinha 59 commits alcançáveis pelas refs locais/remotas, três tags e um commit órfão adicional. O commit órfão `81b9191` repete um ajuste de target API 36 visto também na história alcançável; foi preservado e não foi usado como marco. Os demais objetos inalcançáveis não foram inspecionados como fonte de produto. Nenhum commit histórico foi reescrito. Pull requests/Issues completos, refs removidas, uploads e a Console não foram auditados remotamente.

Use `CONFIRMADO` para fato sustentado por Git/arquivo/relatório identificado, observando a natureza da evidência; `INFERIDO` para interpretação de sequência; `NÃO COMPROVADO` para ausência de evidência. Relatório histórico não significa reexecução de teste. A tabela detalhada está em [HISTORICO_VERSOES.md](HISTORICO_VERSOES.md).

## Linha do tempo executiva

| Período | Marco confirmado | Evidência principal |
|---|---|---|
| 29/09/2026 | MVP/documentação e base Android; dados de clientes, vendas, pagamentos e fotos em SQLite. A configuração inicial observada era `2 / 1.0-comercial`, API 35 e min 23. | `9c265085`, `5c8c4f2`, `docs/MVP.md`, `LocalDatabase.kt` no commit inicial. |
| 29–30/09 | Limite FREE, regras monetárias, navegação, status parcial, centavos, proteção de compartilhamento/backup, configuração de assinatura e versão `8 / 1.0.0`. | Commits `692c663`–`69aef58`; [AUDITORIA_PRE_BILLING](AUDITORIA_PRE_BILLING.md), [REGRAS_FINANCEIRAS](REGRAS_FINANCEIRAS.md). |
| 01–02/10 | Ordem de abertura banco/UI corrigida; integração Billing; fluxo de compra única; estabilização da UI em callbacks; backup/restauração local; política/site inicial. | `b99ae53`, `896c0f5`, `a18a4d0`, `c2e97c2`, `d8e6fcf`, `5b496bc`. |
| 10/10 | Marco chamado V10 beta da Store pelo usuário; tag local `v10-beta-store`, code 10 e name 1.0.0. | `99f41e7`, [MARCO_V10_BETA_STORE](MARCO_V10_BETA_STORE.md). |
| 11–12/10 | V11: nome comercial opcional, datas de apresentação, CSV brasileiro, telas financeiras compactas, barras com valores/percentuais e cards compactos de venda; backup do nome em metadados compatíveis. | `eae7711`–`8aa3488`, [RELEASE_V11](RELEASE_V11.md), tag `v1.0.11-rc.1`. |
| 15–16/10 | Fases 2–7: segurança de restore, política local, correções de lint/API, toolchain, versão 12, consolidação Git, regressão, AAB e preparação Alpha. | [Fases 2–7](INDEX.md#documentos-de-produto-e-implementação), commits `b2e65a0`–`6434485`. |

V1–V9 são rótulos incompletamente registrados; não devem ser transformados em releases imaginárias. V10 e V11 têm marcos explícitos; V12 tem artefato local validado, mas não publicação comprovada. Detalhes: [correspondência de versões](HISTORICO_VERSOES.md).

O marco V10 também trouxe refinamentos de navegação e interface compacta, controle de diálogos e tratamento para não iniciar compra quando faltassem produto, preço dinâmico ou conexão válida. O histórico registra oito testes unitários, builds debug/release e checks de UI em AVD; a rodada final com fonte ampliada encontrou ANR e ficou pendente, portanto os resultados anteriores não aprovam o layout final. Consulte [histórico de testes](HISTORICO_TESTES.md) e o relatório V10.

## Evolução técnica e funcional

- **Persistência:** começou em `SQLiteOpenHelper` com tabelas de clientes, vendas e pagamentos, relações de parcelas e tabelas de fotos. A migração `DB_VERSION 3` acrescentou colunas monetárias em centavos, sem abandonar as colunas/compatibilidade existentes. Preferência de nome do negócio permanece fora do schema financeiro.
- **Dinheiro:** a decisão inicial documentada determina centavos inteiros; commit `b6befe5` acrescenta a migração para valores em centavos. V11 CSV formata os `Long` com `BigDecimal` e duas casas, sem modificar cálculos.
- **Entidades e fluxo:** clientes, vendas à vista/parceladas, pagamentos parciais, saldos, histórico, fotos e relatórios já constam da base inicial. Melhorias de busca, navegação, filtros e cards ocorreram em commits pequenos de 29/09 e nos refinamentos V11. PDF, CSV e compartilhamento/WhatsApp foram mantidos e evoluídos sem prova de todas as versões antigas da Play.
- **Monetização:** decisões locais confirmam FREE até 30 vendas, clientes ilimitados e compra única `FULL_ACCESS`; Billing usa produto `full_access`, preço retornado do Play, sem assinatura. Backup/restauração são restritos ao FULL_ACCESS; CSV e personalização ficam no FREE.
- **Backup:** cópia/restauração local entrou em `d8e6fcf`; endurecimento e validação adversarial aparecem nas Fases 2/4. V11 inclui nome no formato 2 de metadados do mesmo arquivo SQLite e lê formato 1, mantendo schema financeiro 3. Licença Play não é copiada/restaurada.
- **Privacidade:** site/política inicial em `5b496bc`; política offline e controles de notificação em `5467c40`; revisão local 15/10 integrada a `master` em `6434485`. A página pública e a declaração Data Safety não foram confirmadas nesta fase histórica.
- **Toolchain:** inicialmente compile/target 35, min 23; API 36/min 24 entrou em 29/09. AGP/Gradle foram ajustados à matriz documentada na Fase 4 e Kotlin/KGP a 2.2.20 na Fase 7B.1. Esses marcos técnicos não representam releases comerciais.

Fontes temáticas: [arquitetura](EVOLUCAO_ARQUITETURA.md), [funcionalidades](EVOLUCAO_FUNCIONALIDADES.md), [segurança](HISTORICO_SEGURANCA.md) e [decisões](DECISOES_HISTORICAS.md).

## Qualidade, problemas e segurança

Os testes começaram com `MoneyRulesTest` em 29/09, depois cobriram formato e transação de backup, identidade/compatibilidade, CSV, percentuais, segurança de política e casos instrumentados de UI/backup. A Fase 7H registra V12 com 43/43 testes unitários em debug e release, zero erros de lint e 67 avisos por variante; AndroidTest foi compilado, não executado. A Fase 7I registra AAB assinado/validado. Não há evidência local de homologação física V12, API 24 em dispositivo ou compra Play real. Ver [histórico de testes](HISTORICO_TESTES.md).

As principais soluções incluem centavos/migração, inicialização do banco antes da tela, preservação da tela durante callbacks Billing, CSV escapado, separação de tema por API, permissão de notificação contextual, validação transacional de restauração e assinatura verificável. Ainda há pendências externas de Data Safety/Billing SDK, estado da política pública, testes físicos/update V11→V12 e testes Play. Ver [problemas e soluções](PROBLEMAS_E_SOLUCOES.md) e [histórico de segurança](HISTORICO_SEGURANCA.md).

## Repositório e continuidade

O código permanece em worktree de integração `integration/v12-master-validation` no commit `64344855`; a branch original de segurança, a tag V11, worktrees e AABs foram preservados. A memória documental foi criada localmente, sem push ou publicação. O relatório desta Fase 8A registra o estado final, os commits documentais e a auditoria de conteúdo.

Para trabalho futuro, leia primeiro [INDEX](INDEX.md), [AGENTS](../AGENTS.md), [DECISOES](DECISOES.md) e [Continuidade Codex](CONTINUIDADE_CODEX.md). Cada nova mudança deve ligar requisito, commit, teste e versão sem confundir evidência histórica com validação atual.
