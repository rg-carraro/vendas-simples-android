# Monetização

## Decisão confirmada

| Estado | Regra |
| --- | --- |
| FREE | Até 30 vendas; clientes ilimitados; leitura, histórico, saldo, relatórios e pagamentos existentes continuam disponíveis após o limite |
| Licença Completa (`FULL_ACCESS`) | Compra única planejada de R$ 49,90, sem assinatura e sem mensalidade |

A 31ª tentativa de criar venda solicita desbloqueio. O app não apaga nem oculta dados já existentes.

No MVP, o backup local do SQLite é um benefício da Licença Completa. Usuários FREE não veem o menu de três pontos nem as ações de gerar/compartilhar backup, mas têm exportação CSV; isso não impede consulta, relatórios ou pagamentos de vendas já registradas.

## Arquitetura de cobrança

Google Play Billing deve ser encapsulado por abstração de domínio. A UI conhece apenas estados como `FreeWithinLimit`, `FreeLimitReached`, `FullAccessEntitled`, `Pending` e `Unavailable`; SKU, tokens, conexão e callbacks ficam na infraestrutura.

A compra da Licença Completa precisa ser restaurável pela ação de restaurar compras quando o Billing estiver disponível. Restaurar a compra confirma o entitlement, mas não restaura automaticamente dados; restaurar backup é uma operação separada. Falha temporária não revoga dados nem apaga licença ou conteúdo.

## Contador local no MVP

O contador FREE pode ser local, sem conta ou backend antifraude. A reinstalação poderá resetar esse contador antes de existir um serviço de identidade; isso é uma limitação conhecida. O contador só é atualizado após a venda persistir com sucesso.

## Estados e falhas

- Compra pendente: manter dados e informar que o desbloqueio aguarda confirmação.
- Compra cancelada ou recusada: permanecer FREE, sem apagar nada.
- Billing indisponível: permitir consulta e pagamentos existentes.
- Erro de restauração: informar e permitir nova tentativa; nunca apagar dados.

Preço e produto no Play Console ainda exigem configuração e revisão antes da publicação. Este documento não autoriza criar produto, cobrar ou publicar.
