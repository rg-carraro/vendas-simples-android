# Auditoria pré-Billing

## Escopo desta etapa

- `compileSdk` e `targetSdk` atualizados para API 36 (`883b60a`).
- O banco passou da versão 2 para a 3. Vendas e pagamentos novos usam colunas `INTEGER` em centavos (`valor_total_centavos` e `valor_pago_centavos`). A migração v2 converte os valores `REAL` com arredondamento para centavos e preserva clientes, vendas, parcelas, pagamentos, fotos e IDs.
- O aplicativo trabalha com `Long`/`MoneyCents` no domínio; conversão para `pt-BR` ocorre somente na apresentação.
- `allowBackup=false` foi adotado para impedir cópia automática de dados financeiros locais. O `FileProvider` não é exportado e concede apenas URI temporária para cache/files privados.

## Validação

Os testes unitários cobrem R$ 0,01, R$ 49,90, divisão de R$ 100 em três parcelas, pagamento parcial, correção de pagamento, saldo zerado e conversão da migração v2. O `assembleDebug` foi executado após a alteração de SDK; a compilação Kotlin da migração deve ser repetida no ambiente Android local caso o daemon do Gradle esteja ocupado.

## Fora do escopo

Google Play Billing, conta, backend antifraude e sincronização remota continuam fora desta etapa. A falha de licença não altera nem apaga dados locais.
