---
name: regressao-release
 description: Regressão de dados, vendas, backup, exportação, Billing e publicação segura de apps Android.
---
# Regressão e release — Vendas Simples

## Invariantes
- Não perder clientes, vendas, parcelas, pagamentos parciais, fotos, saldos, histórico e relatórios.
- Atualização instalada sobre versão anterior deve preservar banco e licença.
- FREE: 30 vendas, clientes ilimitados, CSV liberado, nenhuma propaganda. FULL_ACCESS: compra única R$ 19,90 configurada no Play Console, vendas ilimitadas, backup/restauração. Nunca fixar preço no código; consultar Google Play.
- Backup completo inclui preferências de negócio quando implementadas; backups legados permanecem restauráveis; licença não vem do backup.
- CSV: precisão financeira, escaping de delimitadores/aspas/quebras de linha, codificação e colunas consistentes; não presumir unidade monetária.
- PDF/WhatsApp preservam informações, fotos e valores; nome personalizado quando configurado.

## Evidências anteriores reportadas pelo usuário
Foram validados manualmente: limite FREE e desbloqueio FULL; parcelas; clientes; CSV; PDF com foto; filtros; backup/restauração entre aparelhos; múltiplas contas Google Play; compra e restauração da licença; pagamentos parciais; edição/exclusão e recálculos; fotos câmera/galeria; compartilhamento WhatsApp; upgrade preservando dados. Build 10 foi atualizado com licença preservada. Estes resultados NÃO homologam automaticamente a V11.

## Pendências e regressões por release
- V11: primeiro acesso/nome opcional, edição/persistência, WhatsApp/PDF, nomes longos e acentos, backup/restauração do nome, CSV pt-BR e colunas, layout financeiro/clientes, fonte 200%, fotos, cálculos, limite FREE, Billing e atualização sem perda.
- Casos extremos recomendados: datas de vencimento, arredondamento monetário, compra pendente/cancelada, backup corrompido, fechamento forçado e offline.
- Registrar resultado PASSOU/FALHOU/NÃO EXECUTADO com versão, aparelho, data e evidência. Não declarar teste manual sem execução.

## Processo de release
1. Ler AGENTS.md e documentação atual; inspecionar diff, versionCode e assinatura sem expor segredos.
2. Executar testes e builds; homologar em dispositivo real.
3. Verificar compatibilidade e migração de dados, certificados e bundle.
4. Não fazer push, merge ou upload sem autorização explícita.
5. Enviar à faixa de teste apropriada e validar upgrade pela Play Store.
