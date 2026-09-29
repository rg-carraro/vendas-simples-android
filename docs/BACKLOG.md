# Backlog inicial

Nenhuma tarefa Android foi iniciada. A sequência abaixo é proposta; não estima prazo nem autoriza publicação.

| Etapa | Entrega | Dependência | Conclusão verificável |
| --- | --- | --- | --- |
| B-00 — concluída | Documentação, instruções e skills locais | Pedido atual | Arquivos presentes e referências verificadas |
| B-01 — pendente | Consolidar escopo e decisões financeiras | Q-01, Q-03 a Q-05 | MVP e decisões atualizados com respostas |
| B-02 — concluída | Trazer seletivamente projeto Android da referência | Pedido de desenvolvimento, Q-02 | Arquivos rastreados ao hash, identidade definida, build no destino; origem intacta |
| B-03 — em andamento | Primeiro uso, clientes e vendas | B-01, B-02 | MVP-01/02/03/05 validados com dados sintéticos |
| B-04 — pendente | Pagamentos, correções e integridade | B-03 | MVP-04 e cenários financeiros validados |
| B-05 — pendente | Fotos, relatórios, exportação e cobrança | B-04 | MVP-06/07/08 validados, inclusive cancelamento de ações externas |
| B-06 — pendente | Backup e decisão sobre recuperação | B-05, Q-06, Q-08 | MVP-09 verificado; recuperação declarada com precisão |
| B-07 — pendente | Homologação do piloto | B-03 a B-06, Q-07 | MVP-10, checklist executado e limitações registradas |
| B-08 — posterior | Preparação de distribuição | Piloto, Q-08/09 | Canal, assinatura, suporte e autorização de lançamento definidos |

Cada etapa de implementação começa verificando o que já funciona no código derivado: os itens representam entregas e validações, não reimplementações obrigatórias.

### B-02 — registro da primeira execução

Arquivos técnicos foram trazidos do commit `ce112c40c4f79368042555e451338b5f318077bd`, sem `.git`, documentação, skills, dados ou credenciais da origem. O nome do projeto Gradle foi ajustado para `VendasSimples`; `applicationId` e banco permanecem decisões a validar em Q-02. A compilação foi validada com o JDK bundled do Android Studio usando `assembleDebug`; o APK de debug foi gerado. O uso de `Double` no código herdado permanece débito explícito para uma fase financeira posterior, sem alterar comportamento neste commit.

### B-03 — primeiro incremento de domínio

`DomainRules.kt` adiciona `MoneyCents` para valores novos e `FreeSalesGate` para o limite FREE de 30 vendas. A regra deixa leitura, histórico, saldo, relatórios e pagamentos existentes fora do bloqueio; a integração com persistência e UI ficará em incrementos posteriores. Billing real ainda não foi adicionado.

O primeiro ponto de integração conta compras distintas no SQLite e intercepta apenas a ação de nova venda. O desbloqueio ainda é informativo até a fase de Billing; nenhum dado existente é bloqueado ou removido.

## Critério de pronto por entrega

Escopo e comportamento documentados, alteração restrita ao destino, diff revisado, verificações aplicáveis com resultado registrado e limitações explícitas. Para recursos financeiros, incluir exemplos de entrada e saída conferidos independentemente da implementação.

Futuras tarefas devem indicar IDs de MVP e decisões relacionadas, arquivos afetados, critério de aceite e evidência de validação. Restauração, estoque ou nuvem entram no backlog somente com escopo próprio, sem expansão implícita deste MVP.
