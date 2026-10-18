---
name: vendas-simples-monetizacao
description: Planejar ou implementar FREE 30 vendas, FULL_ACCESS e Google Play Billing no Vendas Simples.
---

Leia `docs/MONETIZACAO.md` e `docs/DECISOES.md`. FREE permite 30 vendas, clientes são ilimitados e o limite afeta apenas criar a 31ª venda. Licença Completa (`FULL_ACCESS`) é compra única sem assinatura; o preço exibido vem exclusivamente do Google Play, conforme a decisão vigente.

Modele Billing por abstração de domínio; a UI não conhece SKU, token ou callback. Inclua restauração, estados pendente/cancelado/indisponível e falha não destrutiva. O contador local pode resetar em reinstalação no MVP; não crie conta/backend apenas para antifraude. Nunca apagar dados por erro de licença, rede ou Billing.

Preserve produto `full_access` e opção `full-access`. Use ProductDetails sem preço fictício ou fallback; produto/preço ausentes ou serviço desconectado impedem compra. Teste pendência, reconhecimento e restauração sem apagar dados ou revogar direitos já adquiridos.

Para mudanças de compra/licença, consulte docs/DECISOES_HISTORICAS.md, docs/HISTORICO_SEGURANCA.md e commits Billing, mas siga docs/DECISOES.md/MONETIZACAO.md como fonte normativa. Separe teste local de entitlement de transação real Play; atualize pendências de Console/SDK sem inferir payloads.

Antes de alterar Google Play Billing, seus SDKs auxiliares ou o fluxo de token, consulte `docs/CONHECIMENTO_GOOGLE_PLAY_DATA_SAFETY.md`. Preserve as condições e limites da orientação específica do suporte; qualquer novo dado transmitido, destinatário ou finalidade exige nova análise Data Safety e da política. Não reutilize essa decisão em outro app.
