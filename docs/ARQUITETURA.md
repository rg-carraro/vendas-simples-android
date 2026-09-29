# Arquitetura e dados

## Estado e direção proposta

Este repositório contém apenas documentação. A proposta é derivar seletivamente a base Android local da referência, preservando comportamento antes de refatorações amplas. Não há decisão de migrar para Compose, Room ou backend.

Na referência: UI programática em `MainActivity.kt` → operações/modelos locais → `LocalDatabase.kt` com SQLiteOpenHelper → saídas PDF, CSV e backup. Compartilhamentos usam Intents para aplicativos externos.

Configuração observada, não recomendação de versões atuais: Kotlin 1.9.24, AGP 8.5.2, Gradle 8.7; minSdk 23, compileSdk/targetSdk 35. Quando houver implementação, verificar ambiente e compatibilidade antes de fixar versões no destino.

## Identidade e armazenamento

| Item | Referência | Direção no destino |
| --- | --- | --- |
| Nome | Vendas Simples | Confirmado pelo usuário |
| applicationId | `com.vendassimples.app` | Proposto; decidir compatibilidade com APK comercial já existente |
| Namespace | `com.example.controlevendas` | Legado interno; renomeação não é requisito inicial |
| Banco | `vendas_simples.db`, schema v2 | Preservar inicialmente se houver derivação |
| FileProvider | `${applicationId}.provider` | Manter derivado do identificador escolhido |
| Rede | Sem INTERNET e backend próprio | Preservar proposta local |

O manifesto de origem usa `allowBackup=true`: ausência de backend próprio não permite afirmar que o Android nunca fará backup externo. Definir política de backup do sistema e explicação ao usuário antes da distribuição.

## Modelo conceitual de referência

| Entidade | Chave/relação | Cuidado |
| --- | --- | --- |
| CLIENTES | `id_cliente` | Nome não deve substituir vínculo existente sem análise |
| VENDAS | `id_venda`, `id_cliente`, `id_venda_pai` | Cada parcela possui registro; pai agrupa compra |
| PAGAMENTOS | `id_pagamento`, `id_venda` | Pagamento pertence à parcela; evitar duplicar totais |
| FOTOS_VENDA | `id_foto`, `id_venda_pai`, imagem BLOB | Fotos compartilhadas pelas parcelas da mesma compra |
| SYNC_DIRTY / SYNC_DELETIONS | Metadados legados | Não representam sincronização ativa; remoção exige migração |

Preservar IDs e relações na importação futura do código. Não converter IDs numéricos de forma que percam precisão. O uso observado de Double para dinheiro e id_venda no modelo merece avaliação; uma mudança exige conferir schema, serialização, cálculo e compatibilidade, não apenas trocar o tipo.

## Invariantes propostos para validação

- Soma de parcelas equivale ao total da compra na precisão monetária definida.
- Total pago deriva dos pagamentos da parcela; saldo deriva de valor menos pagamentos, conforme política de excesso a decidir.
- Relatórios, cards e exportações usam critérios de período explícitos e consistentes.
- Totais de compra não são estimados a partir de subconjuntos filtrados ou relações incompletas.
- Edição/exclusão mantém integridade de pagamentos e fotos; escopo da ação é visível ao usuário.
- Backup é consistente e contém BLOBs das fotos. A origem fecha o banco antes de copiar; verificar concorrência e journaling no código antes de reaproveitar.

## Evolução técnica

Primeiro reproduzir a base no destino e estabelecer validação. Depois extrair regras de cálculo e persistência da Activity conforme necessidade demonstrada. Schema, identificador de aplicativo e semântica financeira são decisões explícitas em [DECISOES.md](DECISOES.md).

Usar somente dados sintéticos em testes e exemplos. Falha ao compartilhar, cancelamento de câmera ou permissão negada não pode gerar registro parcial ou perda silenciosa. Logs e fixtures não devem conter dados reais de clientes.
