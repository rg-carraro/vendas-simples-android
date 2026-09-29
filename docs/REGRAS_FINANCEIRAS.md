# Regras financeiras

Valores monetários novos devem ser armazenados e calculados em centavos usando `Long` ou tipo decimal seguro. `Float` e `Double` não são permitidos para dinheiro novo.

- total da venda não é negativo;
- soma das parcelas corresponde ao total, com residual de centavo definido;
- saldo é total da parcela menos pagamentos aplicáveis;
- cards, relatórios, PDF e CSV usam a mesma regra;
- falhas de licença não alteram valores financeiros.

Antes do código, definir pagamento acima do saldo, valor zero/negativo, correção, fim do mês e exclusão/edição de parcela paga. Testar R$ 0,01, R$ 49,90, R$ 100,00 em três parcelas, múltiplos pagamentos, arredondamento, fim de mês e migrações sem perda de pagamentos, fotos ou IDs.
