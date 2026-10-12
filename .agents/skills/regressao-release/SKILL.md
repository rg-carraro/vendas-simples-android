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


## CSV brasileiro e codificação
- Confirmar unidade financeira na leitura do banco; exportar centavos inteiros com decimal exato, duas casas, vírgula e sem R$ nas células. Manter delimitador ;, oito colunas, datas dd/MM/yyyy e escaping de aspas/CR/LF/separadores.
- Conferir bytes reais e leitura com parser independente. UTF-8 sem BOM no CSV padrão; UTF-8 com BOM na opção para Excel. Mesmo exportador e registros; o BOM deve ser a única diferença. Testar Unicode fora de Windows-1252 e nunca substituir texto do usuário para mascarar mojibake.
- ï»¿/DescriÃ§Ã£o indicam interpretação de bytes UTF-8 como outra codificação; remover o BOM não força o leitor a reconhecer acentos. Não trocar para ANSI com perda de caracteres. Registrar aplicativo/versão/caminho de importação e distinguir parser aprovado de teste no celular.
- Quando abertura direta falhar, orientar importação do arquivo original com UTF-8, delimitador ; e localidade Brasil. Para Google Planilhas, avaliar importação/conversão pelo navegador de computador e posterior abertura da planilha no Android. Para Excel, usar importação De Texto/CSV se autodetecção falhar.

## Evidências para preparação Alpha
- Reexecutar suíte unitária, regressão instrumentada, filtros/pesquisa, mudança de mês e persistência; comparar banco/fotos/preferências antes/depois da atualização isolada.
- Registrar lint aprovado ou erros pendentes, sem ocultá-los com baseline/supressões. Conferir AAB final por manifesto, validação do bundle, assinatura criptográfica e hash; confirmar chave de upload/código disponível no Console pelo responsável.
- Checklist é preparação: upload, preço/produto, envio para análise e distribuição dependem de autorização explícita. Testes de entitlement local não comprovam compras/restauração Google Play reais.

Os padrões são candidatos à adaptação no AleJoias Vendas, preservando marca/cores, funcionalidades, dados e regras comerciais próprios; não autorizam modificar a origem.
