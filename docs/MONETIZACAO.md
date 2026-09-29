# Monetização

## Decisão confirmada

| Estado | Regra |
| --- | --- |
| FREE | Até 30 vendas; clientes ilimitados; leitura, histórico, saldo, relatórios e pagamentos existentes continuam disponíveis após o limite |
| LIFETIME | Compra única planejada de R$ 49,90, sem assinatura e sem mensalidade |

A 31ª tentativa de criar venda solicita desbloqueio. O app não apaga nem oculta dados já existentes.

## Arquitetura de cobrança

Google Play Billing deve ser encapsulado por abstração de domínio. A UI conhece apenas estados como `FreeWithinLimit`, `FreeLimitReached`, `LifetimeEntitled`, `Pending` e `Unavailable`; SKU, tokens, conexão e callbacks ficam na infraestrutura.

A compra LIFETIME precisa ser restaurável pela ação de restaurar compras e quando o Billing estiver disponível. Falha temporária não revoga dados nem apaga licença ou conteúdo.

## Contador local no MVP

O contador FREE pode ser local, sem conta ou backend antifraude. A reinstalação poderá resetar esse contador antes de existir um serviço de identidade; isso é uma limitação conhecida. O contador só é atualizado após a venda persistir com sucesso.

## Estados e falhas

- Compra pendente: manter dados e informar que o desbloqueio aguarda confirmação.
- Compra cancelada ou recusada: permanecer FREE, sem apagar nada.
- Billing indisponível: permitir consulta e pagamentos existentes.
- Erro de restauração: informar e permitir nova tentativa; nunca apagar dados.

Preço e produto no Play Console ainda exigem configuração e revisão antes da publicação. Este documento não autoriza criar produto, cobrar ou publicar.
