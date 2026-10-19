# Manutenção pós-lançamento

Este checklist orienta correções pontuais após a disponibilização de uma versão. Não estabelece SLA nem frequência de atualizações.

## Severidade

| Nível | Impacto | Resposta inicial |
|---|---|---|
| S0 — crítico | Perda/corrupção ampla de dados, exposição de dados pessoais, risco de cobrança/licença ou falha que impeça uso essencial sem alternativa. | Interromper a distribuição/rollout se possível, preservar evidências sanitizadas, acionar responsáveis e priorizar contenção e recuperação segura. |
| S1 — alto | Cálculo financeiro incorreto, falha relevante de vendas/pagamentos/backup, ou indisponibilidade extensa com workaround limitado. | Reproduzir rapidamente com dados sintéticos e preparar correção isolada após identificar causa. |
| S2 — moderado | Função degradada com alternativa prática; impacto limitado ou circunscrito a uma versão/dispositivo. | Registrar cenário, avaliar alcance e incluir na próxima correção apropriada. |
| S3 — baixo | Ajuste visual, texto ou desconforto sem perda de dados nem bloqueio de tarefa essencial. | Priorizar junto do backlog, sem promessa de data. |

## Fluxo de triagem

1. Registrar relato, data e fonte; pedir versão `versionCode`/`versionName`, aparelho, fabricante, Android/API, passos, frequência e resultado esperado/observado. Remover nomes, imagens, valores e identificadores reais dos anexos.
2. Classificar severidade e avaliar risco a clientes, vendas, parcelas, pagamentos, saldos, fotografias, backup, Billing, privacidade e atualização. Se houver dúvida de perda/exposição, escalar provisoriamente o nível.
3. Consultar `docs/INDEX.md`, `docs/DECISOES.md`, `docs/PROBLEMAS_E_SOLUCOES.md`, histórico do arquivo e pendências; confirmar comportamento no código atual, sem assumir que documentação antiga continua válida.
4. Reproduzir com dados sintéticos em dispositivo/emulador apropriado. Anotar configuração, passos, evidência sanitizada e se a causa está confirmada ou ainda é hipótese.
5. Corrigir em branch isolada com escopo mínimo. Preservar IDs/relações; mudança de schema requer migração e cenários de compatibilidade. Nunca apagar dados para contornar falha de licença.
6. Executar testes unitários, instrumentados disponíveis, lint, build e regressões afetadas. Para backup, testar restauração em cópia descartável, arquivos antigos/inválidos e fotografias; nunca fazer restauração destrutiva em dados reais.
7. Rever Billing/Data Safety/política ao alterar SDK, transmissão, destino, conta, finalidade, compra, restore ou persistência. Reavaliar assinatura/compatibilidade antes de release.
8. Gerar artefato somente no fluxo autorizado; conferir package/versionCode/versionName, assinatura, hash e origem. Atualização por Play deve ser testada como atualização in-place, não substituída por instalação debug ou restauração de backup.
9. Registrar causa e evidência, solução, arquivos, testes aprovados/falhos/não executados, versão afetada, limitações, estado de rollout e próximos passos. Distinguir fatos do Git, relato do proprietário e observação direta.

## Antes de distribuir a correção

- [ ] Diff restrito e sem dados sensíveis/artefatos locais.
- [ ] Regressão das rotas diretamente afetadas e dos fluxos financeiros/dados relacionados.
- [ ] Backup compatível e restore íntegro quando relevante.
- [ ] Billing/licença e regra FREE/FULL_ACCESS sem regressão quando relevantes.
- [ ] Privacidade, SDKs e declaração Data Safety reavaliados se houve mudança de fluxo.
- [ ] Metadados, assinatura de upload e hash conferidos.
- [ ] Notas correspondem às mudanças reais; sem alegar publicação antes da evidência da Play Console.
- [ ] Evidências e pendências registradas em documentação e `docs/HISTORICO_TESTES.md`.
