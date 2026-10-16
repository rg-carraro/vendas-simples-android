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
| D-06 | Produto offline-first e independente, sem marca, credenciais, endpoints, banco real ou Google Planilhas do AleJoias |
| D-07 | Modelo comercial FREE + Licença Completa (`FULL_ACCESS`); FREE permite até 30 vendas; clientes não têm limite |
| D-08 | Ao atingir 30 vendas, preservar leitura, histórico, saldo, relatórios e pagamentos existentes; a 31ª venda solicita desbloqueio |
| D-09 | Licença Completa (`FULL_ACCESS`) é compra única sem assinatura ou mensalidade, com preço exibido exclusivamente do Google Play (revisão de 07/10/2026) |
| D-10 | A compra da Licença Completa deve ser restaurável pelo Google Play; Billing isolado da UI por abstração de domínio |
| D-11 | MVP pode usar contador gratuito local, documentando que reinstalação poderá reiniciar o trial sem backend |
| D-12 | Não criar conta/backend apenas para antifraude no MVP |
| D-13 | Valores monetários novos usam centavos em `Long` ou tipo decimal seguro; nunca `Float`/`Double` |
| D-14 | Mudanças de schema exigem migração testada; falha de licença nunca apaga dados |

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
| Q-09 | Canal, suporte, direitos de recursos reaproveitados e configuração final do Google Play | Antes de lançamento; sem autorização de gastos/publicação |
| Q-10 | Conteúdo adicional da conversa vinculada | Quando disponibilizado; reconciliar sem inventar decisões |

Atualize este registro quando houver decisão do usuário ou constatação técnica. Registre data, origem da decisão e documentos afetados. Não transforme uma hipótese em requisito apenas por repetição no backlog.

## Revisão de 07/10/2026

Solicitação do usuário: implementar melhorias de navegação, Nova Venda e preço dinâmico no aplicativo existente. A fase de desenvolvimento está autorizada. O preço anterior deixa de ser requisito; o teste comercial pretendido será configurado manualmente no Play Console. Preservar `full_access`, opção `full-access`, FREE 30 e direitos já adquiridos. Não autoriza commit, push ou publicação.

## Revisão de 10/10/2026 — V10 beta da Store

O usuário identifica esta versão como V10 do teste beta da Store e autoriza
revisar documentação/skills e publicar todas as alterações pendentes no GitHub
com a label `v10-beta-store`. Nesta revisão não alterar código. A autorização
não inclui upload ou mudança no Play Console. O histórico Alpha é preservado;
a identificação beta vem do usuário, sem verificação do Console.
Ver [marco V10](MARCO_V10_BETA_STORE.md).

## Revisão de 11/10/2026 — versão 11

Pedido anexado pelo usuário autoriza implementar personalização, padronização da apresentação de datas, ajuste de fotos, testes, commits locais e geração de AAB release assinado. Versão requerida: 11 / 1.0.11, applicationId `com.vendassimples.app`. Não autoriza push, merge ou upload no Play Console.

Nome do negócio é preferência local independente do SQLite, sem migração; configuração inicial opcional oferecida uma vez, edição em Configurações, fallback genérico. O nome é aplicado em mensagens e nos PDFs existentes. Datas persistidas permanecem ISO; conversão ocorre nas fronteiras de apresentação e entrada. Limite de 120 caracteres protege o layout; PDF quebra o nome por largura real. Remover foto recebe 16 dp de margem acima. Regras financeiras, Billing 9.1.0, FREE 30 e FULL_ACCESS preservados. Ver [relatório](RELEASE_V11.md).

## Complemento da V11 — backup do nome do negócio

Solicitação do usuário: incluir/restaurar o nome no backup completo FULL_ACCESS existente, mantendo personalização gratuita, versão 11 / 1.0.11 e compatibilidade com backups antigos. Confirmado formato 2 dos metadados do mesmo SQLite, leitura do formato 1, schema financeiro 3 preservado. Nome ausente/vazio/nulo no backup não substitui nome atual. Restauração valida a cópia exata e coordena banco/preferência por journal privado de recuperação; nenhum dado da licença Google Play é exportado ou restaurado. Sem push ou publicação. Resultados e limites em [RELEASE_V11.md](RELEASE_V11.md).

## Revisão de 15/10/2026 — URL oficial da política

O usuário confirmou na Play Console a URL https://rg-carraro.github.io/vendas-simples-android/privacy/ e determinou seu uso na Fase 2, mantendo consulta gratuita ao texto local inclusive offline. A implementação já utiliza esse endereço. Consulta HTTPS respondeu 200, mas o texto público de 02/10/2026 difere da revisão local de 15/10/2026 preparada após a auditoria. A confirmação não autoriza commit, push ou publicação; atualizar a página pública permanece pendente. Evidências e diferenças no [relatório da Fase 2](FASE2_SEGURANCA_CONFORMIDADE.md).
