# Release e Google Play

Este documento prepara a distribuição; não representa publicação autorizada. O aplicativo está em desenvolvimento e o usuário informou distribuição Alpha em teste fechado.

Antes do primeiro release: confirmar `applicationId`, assinatura, backup e coexistência com APKs anteriores; configurar a Licença Completa (`FULL_ACCESS`) no Play Console com preço definido manualmente no Play Console e compra única, sem assinatura; revisar privacidade, permissões e ausência de referências ao AleJoias; testar migração e falhas de licença sem perda de dados.

Usar licença/teste do Google Play para compra aprovada, pendente, cancelada, recusada, restauração de compra e Billing indisponível. Confirmar que `FULL_ACCESS` libera a 31ª venda. Distinguir restaurar compra de restaurar backup; testar reinstalação separadamente, pois o contador FREE local pode resetar no MVP.

## Checklist

- [ ] package, versão e assinatura definidos;
- [ ] ficha descreve FREE 30 vendas e Licença Completa sem assinatura;
- [ ] política de privacidade e classificação preenchidas;
- [ ] screenshots sem dados reais;
- [ ] artefato assinado e smoke test offline concluídos;
- [ ] aprovação explícita para publicar registrada.

Preço, publicação e mudanças no Play Console permanecem decisões operacionais posteriores.

Atualização de 07/10/2026: preparar build 10 / 1.0.0, sujeito à conferência dos códigos já usados no Console. Preço exibido exclusivamente do Play; não há autorização de upload, commit ou push. Ver [relatório](ATUALIZACAO_ALPHA_UI_BILLING.md).

## Revisão de 10/10/2026 — V10 beta da Store

O usuário identifica esta versão como V10 do teste beta da Store e autoriza
revisar documentação/skills e publicar todas as alterações pendentes no GitHub
com a label `v10-beta-store`. Nesta revisão não alterar código. A autorização
não inclui upload ou mudança no Play Console. O histórico Alpha é preservado;
a identificação beta vem do usuário, sem verificação do Console.
Ver [marco V10](MARCO_V10_BETA_STORE.md).
