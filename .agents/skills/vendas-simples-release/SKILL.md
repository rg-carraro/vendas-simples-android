---
name: vendas-simples-release
description: Preparar checklist de release, Google Play Billing e publicação do Vendas Simples sem publicar por inferência.
---

Leia `docs/RELEASE_PLAY_STORE.md`, `docs/MONETIZACAO.md` e `docs/VALIDACAO.md`. Verifique identidade independente, assinatura, applicationId, permissões, privacidade e ausência de referências ao AleJoias.

Teste compra única FULL_ACCESS com preço consultado no Google Play, restauração, pendente, cancelada, recusada, Billing indisponível, reinstalação e limite FREE de 30 vendas. Falhas não podem apagar dados. Não executar mudanças no Play Console, criar produtos ou publicar sem autorização explícita.

Para marcos de teste, confronte versionCode/versionName/applicationId com o artefato e registre a origem da identificação Alpha/Beta. Label ou push no GitHub não comprova upload ou aprovação no Play Console. Leia o relatório da atualização antes de reutilizar resultados de build ou smoke tests; mantenha pendências de homologação explícitas.


Na preparação Alpha, documente o AAB exato (hash, versão e assinatura), notas,
checklist de dados/licença, importação CSV e pendências de lint/homologação.
Preço comercial informado pelo usuário não substitui ProductDetails; não
fixar valor no aplicativo. Preparar não autoriza carregar artefato ou enviar
release para análise. Registre origem das evidências e consulte a
[regressão de release](../regressao-release/SKILL.md) para CSV e integração.

Antes de preparar novo marco, consulte docs/HISTORICO_VERSOES.md, docs/HISTORICO_TESTES.md e docs/CONTINUIDADE_CODEX.md. Registre commit/tag, versionCode/name, AAB/hash/certificado e estado Play separadamente; atualize lacunas sem tratar artefato local como release publicada.

Para release com Billing ou novos SDKs, leia `docs/CONHECIMENTO_GOOGLE_PLAY_DATA_SAFETY.md`, confira se a arquitetura ainda satisfaz suas condições e reavalie a ficha/política para novos fluxos de dados. A orientação de suporte não é aprovação formal nem autorização para alterar a Console ou publicar.

## Manutenção e atualização via Play

Consulte `docs/PENDENCIAS_ATUAIS.md`, `docs/HISTORICO_COMPLETO_V0_V13.md` e `docs/MANUTENCAO_POS_LANCAMENTO.md`. Registre separadamente aprovação, disponibilidade na faixa, rollout e produção. Uma restauração de backup ou APK debug não substitui atualização in-place via Play. Faça checklist de API mínima, Billing em teste, política pública e migração antes do próximo rollout; registre data e fonte de cada status da Console.
