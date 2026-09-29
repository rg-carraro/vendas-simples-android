---
name: vendas-simples-monetizacao
description: Planejar ou implementar FREE 30 vendas, LIFETIME e Google Play Billing no Vendas Simples.
---

Leia `docs/MONETIZACAO.md` e `docs/DECISOES.md`. FREE permite 30 vendas, clientes são ilimitados e o limite afeta apenas criar a 31ª venda. LIFETIME é compra única planejada de R$ 49,90, sem assinatura.

Modele Billing por abstração de domínio; a UI não conhece SKU, token ou callback. Inclua restauração, estados pendente/cancelado/indisponível e falha não destrutiva. O contador local pode resetar em reinstalação no MVP; não crie conta/backend apenas para antifraude. Nunca apagar dados por erro de licença, rede ou Billing.
