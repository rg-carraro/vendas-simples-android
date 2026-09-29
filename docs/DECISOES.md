# Decisões e questões abertas

Registro inicial: 29/09/2026. “Proposto” não significa aprovado nem implementado.

## Confirmado pelo pedido atual

| ID | Decisão |
| --- | --- |
| D-01 | Trabalhar em `vendas-simples-android` |
| D-02 | Criar produto independente chamado Vendas Simples |
| D-03 | Usar `alejoiasvendasapp`, branch comercial, como referência |
| D-04 | Criar documentação, AGENTS.md, skills e estruturar MVP antes de desenvolver |
| D-05 | Não alterar o repositório original AleJoias |

## Propostas derivadas da referência

| ID | Proposta | Motivo |
| --- | --- | --- |
| P-01 | Operação individual local, sem conta ou backend | É o modelo existente na comercial |
| P-02 | Preservar vendas, parcelas, pagamentos, fotos e saídas | Reaproveitar fluxos já presentes, sujeitos a validação |
| P-03 | Identidade azul/cinza e navegação da comercial | Continuidade da referência sem vínculo com joalheria |
| P-04 | Derivação seletiva com SQLiteOpenHelper inicialmente | Evitar reescrita simultânea de persistência e produto |

## Questões abertas

| ID | Decisão necessária | Quando resolver |
| --- | --- | --- |
| Q-01 | Público inicial e concordância com o escopo proposto | Antes de priorizar implementação |
| Q-02 | Manter applicationId comercial e compatibilidade com instalações anteriores ou criar outro | Antes do primeiro APK |
| Q-03 | Arredondamento, calendário de parcelas e pagamentos excedentes | Antes de implementar/alterar regras financeiras |
| Q-04 | Escopo de edição/exclusão e conservação do histórico | Antes de implementar esses fluxos |
| Q-05 | “Recebido no mês” por venda ou pagamento; necessidade de fluxo de caixa | Antes de validar relatórios |
| Q-06 | Incluir restauração no MVP ou em incremento posterior | Antes de ampliar distribuição |
| Q-07 | Alertas apenas com app ativo ou também em segundo plano | Antes de prometer notificações |
| Q-08 | Política de backup Android, privacidade e recuperação | Antes de distribuição |
| Q-09 | Monetização, assinatura, canal, suporte e direitos de recursos reaproveitados | Antes de lançamento; sem autorização de gastos/publicação |
| Q-10 | Conteúdo adicional da conversa vinculada | Quando disponibilizado; reconciliar sem inventar decisões |

Atualize este registro quando houver decisão do usuário ou constatação técnica. Registre data, origem da decisão e documentos afetados. Não transforme uma hipótese em requisito apenas por repetição no backlog.
