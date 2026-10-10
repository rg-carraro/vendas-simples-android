---
name: vendas-simples-release
description: Preparar checklist de release, Google Play Billing e publicação do Vendas Simples sem publicar por inferência.
---

Leia `docs/RELEASE_PLAY_STORE.md`, `docs/MONETIZACAO.md` e `docs/VALIDACAO.md`. Verifique identidade independente, assinatura, applicationId, permissões, privacidade e ausência de referências ao AleJoias.

Teste compra única FULL_ACCESS com preço consultado no Google Play, restauração, pendente, cancelada, recusada, Billing indisponível, reinstalação e limite FREE de 30 vendas. Falhas não podem apagar dados. Não executar mudanças no Play Console, criar produtos ou publicar sem autorização explícita.

Para marcos de teste, confronte versionCode/versionName/applicationId com o artefato e registre a origem da identificação Alpha/Beta. Label ou push no GitHub não comprova upload ou aprovação no Play Console. Leia o relatório da atualização antes de reutilizar resultados de build ou smoke tests; mantenha pendências de homologação explícitas.
