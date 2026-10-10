---
name: vendas-simples-monetizacao
description: Planejar ou implementar FREE 30 vendas, FULL_ACCESS e Google Play Billing no Vendas Simples.
---

Leia `docs/MONETIZACAO.md` e `docs/DECISOES.md`. FREE permite 30 vendas, clientes são ilimitados e o limite afeta apenas criar a 31ª venda. Licença Completa (`FULL_ACCESS`) é compra única sem assinatura; o preço exibido vem exclusivamente do Google Play, conforme a decisão vigente.

Modele Billing por abstração de domínio; a UI não conhece SKU, token ou callback. Inclua restauração, estados pendente/cancelado/indisponível e falha não destrutiva. O contador local pode resetar em reinstalação no MVP; não crie conta/backend apenas para antifraude. Nunca apagar dados por erro de licença, rede ou Billing.

Preserve produto `full_access` e opção `full-access`. Use ProductDetails sem preço fictício ou fallback; produto/preço ausentes ou serviço desconectado impedem compra. Teste pendência, reconhecimento e restauração sem apagar dados ou revogar direitos já adquiridos.
