# Histórico consolidado do Vendas Simples — V0 a V13

Este é o adendo de continuidade à [consolidação V0–V12 da Fase 8A](HISTORICO_COMPLETO_V0_V12.md), preservada como registro histórico do escopo e das evidências disponíveis à época. A adição da V13 corrige apenas o estado corrente em documentos vivos; não altera retroativamente as evidências antigas nem atribui aos rótulos V1–V9 marcos que não estejam comprovados. Para detalhes por marco, consulte [Histórico de versões](HISTORICO_VERSOES.md), [Evolução de funcionalidades](EVOLUCAO_FUNCIONALIDADES.md), [Arquitetura](EVOLUCAO_ARQUITETURA.md), [Testes](HISTORICO_TESTES.md), [Segurança](HISTORICO_SEGURANCA.md), [Problemas e soluções](PROBLEMAS_E_SOLUCOES.md) e [Decisões históricas](DECISOES_HISTORICAS.md).

## Marcos documentados

| Período / marco | Necessidade e decisão | Implementação e evidência | Limites atuais |
|---|---|---|---|
| Origem independente | Separar identidade e operação do produto original usado como referência; manter o repositório de referência somente leitura. | Primeira árvore Android e histórico inicial em Git; procedência em [REFERENCIA](REFERENCIA.md). | A referência não transfere dados, regras comerciais, credenciais nem autorização. Os rótulos V0–V9 não são todos releases identificadas. |
| MVP e persistência | Fluxo simples, offline-first, sem login ou backend próprio; preservar relações financeiras. | SQLite local para clientes, vendas, parcelas/pagamentos e fotografias; decisões e esquema em [DECISOES](DECISOES.md), [ARQUITETURA](ARQUITETURA.md) e [regras financeiras](REGRAS_FINANCEIRAS.md). | Perda do armazenamento local pode causar perda dos dados não exportados; não há sincronização automática. |
| Inicialização e compatibilidade | Corrigir ordem de inicialização do banco/tela e suportar API 24 como mínimo. | Correção inicial registrada em `b99ae53`; API 36 em compile/target; mitigação de API específica em recursos compatíveis conforme Fase 4. | Build e lint não substituem execução em dispositivo/API 24; ver [Pendências](PENDENCIAS_ATUAIS.md). |
| Billing e modelo comercial | Limite FREE e compra única FULL_ACCESS com preço dinâmico Play. | Billing Library 9.1.0, produto `full_access`; FREE até 30 vendas e clientes ilimitados, compra INAPP única sem assinatura; ver [MONETIZACAO](MONETIZACAO.md). | Compra/restore real de licença e comportamento no serviço Play precisam de teste sandbox/Console; preço real vem de ProductDetails. |
| Interface e saídas | Tornar operação e relatórios úteis em telas compactas, preservando dados/cálculos. | Melhorias de navegação e UI (marco build 10); PDF, CSV, WhatsApp e gráficos; detalhamento em [HISTORICO_VERSOES](HISTORICO_VERSOES.md) e [PROBLEMAS_E_SOLUCOES](PROBLEMAS_E_SOLUCOES.md). | Saídas são iniciadas pelo usuário; revisar compatibilidade e conteúdo após mudanças. |
| V11 | Personalizar nome do negócio e preservar informações em saída/backups. | Nome em tela, WhatsApp e PDF; metadados do backup evoluídos com compatibilidade; decisões e testes em [RELEASE_V11](RELEASE_V11.md). | Backup não representa licença; dados de licença Play não podem ser restaurados pelo arquivo. |
| V12 | Consolidar segurança, validação de backup, privacidade e documentação histórica. | Merge PR #2 e AAB local V12 documentado na Fase 7I; fases 8 e 9 registram integração, privacidade/Data Safety e fluxo Alpha. | AAB local não prova distribuição; orientação de Data Safety é condicionada à arquitetura observada, não pré-aprovação. |
| V13 | Informar sobre o aplicativo e oferecer contato explícito com desenvolvedor. | PR #4 integrada na master pelo merge `5593215`; `13 / 1.0.13`, `com.vendassimples.app`, min24/compile36/target36. 46 testes unitários, compilação instrumentada e lint sem erros estão registrados na [Fase 13](FASE13_SOBRE_APLICATIVO.md). | AAB local e estado Alpha atual constam abaixo; runtime instrumentado, API24 e atualização direta permanecem pendentes. |

## AAB e distribuição V13

- AAB release local: 5.364.930 bytes; SHA-256 `33D0906198E439304887A59AB683AF723636DEEE6ACAF51025BD073C42ED6642`.
- Certificado local registrado: SHA-256 `CD:90:9D:AE:E8:16:B2:7F:4E:32:13:52:73:62:25:80:BE:F0:73:F1:8A:8D:EB:56:04:F5:24:90:9C:63:C6:9E`.
- O proprietário relatou em 19/10/2026 que o teste fechado Alpha exibia versão 13 (1.0.13), envio 6, estado “Em análise” e cerca de oito dias restantes no período fechado. Classificação: **RELATADO PELO PROPRIETÁRIO**, não inspecionado diretamente nesta consolidação. Isso não significa aprovado, disponível a todos os testadores, produção ou publicação pública.
- A restauração de backup V12→V13 Debug relatada não substitui atualização in-place V12→V13 através da Play.

## Critério de leitura

Use `CONFIRMADO` para evidência local identificável (Git, configuração, arquivo, teste executado); `RELATADO PELO PROPRIETÁRIO` para Console/dispositivo cuja imagem ou execução foi informada, mas não observada diretamente aqui; `INFERIDO` para conclusão parcial; `NÃO COMPROVADO` para lacuna; `PENDENTE` para ação aberta. A Fase 8A continua válida como fotografia histórica e deve ser lida junto a este adendo.
