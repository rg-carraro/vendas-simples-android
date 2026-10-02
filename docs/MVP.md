# MVP — proposta inicial

Status: proposta para revisão, baseada na branch comercial. As confirmações do pedido atual estão em [DECISOES.md](DECISOES.md). Nenhum recurso abaixo está implementado neste repositório.

## Objetivo e público

Permitir a uma pessoa que vende produtos registrar vendas, acompanhar parcelas e pagamentos e identificar valores a receber no Android. Público inicial proposto: vendedores autônomos e pequenos negócios com operação individual em um aparelho. Ainda não há pesquisa ou validação desse público.

Hipótese de valor: registrar a primeira venda sem conta remota, trabalhar sem internet e consultar claramente o saldo de cada cliente. Critério de piloto: um participante consegue cadastrar uma venda, registrar um pagamento parcial e localizar o saldo restante sem intervenção; registrar dificuldades antes de definir metas quantitativas.

## Escopo proposto

| ID | Capacidade | Aceite observável |
| --- | --- | --- |
| MVP-01 | Primeiro uso local | Abrir base vazia e cadastrar primeira venda em modo avião, sem convite de planilha |
| MVP-02 | Clientes e vendas | Identificar cliente, descrição, valor, data e vencimento; salvar, consultar, editar e excluir com confirmação e efeitos claros |
| MVP-03 | Parcelamento | Exibir parcelas e vencimentos; soma dos valores coincide com total; cada parcela mantém vínculo com a compra |
| MVP-04 | Recebimentos | Registrar pagamentos parciais, quitar e corrigir pagamento; saldo e histórico permanecem coerentes após reiniciar |
| MVP-05 | Consulta | Buscar e filtrar vendas; consultar histórico e saldo por cliente; distinguir pago, pendente e vencido por texto |
| MVP-06 | Fotos opcionais | Salvar venda sem foto ou com várias; visualizar imagens nas parcelas; preservar fotos após reinício e no backup |
| MVP-07 | Financeiro e relatórios | Painel mensal, período e cliente; totais, detalhamento e PDF usam o mesmo conjunto de registros |
| MVP-08 | Saídas e cobrança | Gerar PDF/CSV; abrir compartilhamento e cobrança por WhatsApp por ação do usuário; permitir cancelar sem alterar dados |
| MVP-09 | Backup local | Criar, salvar e restaurar cópia consistente com vendas, pagamentos e fotos por seletor SAF |
| MVP-10 | Identidade independente | Nome e recursos próprios; instalação e dados separados do AleJoias original; sem backend/planilha da origem |

O escopo preserva o núcleo local da referência, em vez de definir uma reconstrução. Notificações existentes entram na avaliação de compatibilidade; confiabilidade com app fechado e permissões Android permanece pendente e não é promessa do MVP.

## Fluxos principais

1. **Registrar:** Vendas → Nova venda → cliente e dados → parcelas/vencimentos → fotos opcionais → salvar → conferir cards.
2. **Receber:** card → pagamento → valor e data → salvar → conferir pago/saldo → histórico do cliente.
3. **Acompanhar:** Financeiro → mês/período → indicador → vendas correspondentes → relatório ou PDF.
4. **Cobrar:** parcela pendente → revisar texto com parcela, pago e saldo → abrir aplicativo externo → usuário decide enviar.
5. **Guardar cópia:** menu Dados e backup → criar backup → compartilhar/salvar em destino escolhido.

Navegação proposta: Vendas, Nova venda, Financeiro e Clientes; Dados e backup no menu do cabeçalho. Azul e cinza, linguagem genérica, telas legíveis e estados vazios com ação útil. Validar fonte ampliada, teclado, rotação e telas estreitas.

## Fora do escopo inicial

Sincronização, Google Sheets/Apps Script, login, backend próprio, multiusuário, estoque, catálogo completo, emissão fiscal, cobrança automática, processamento de pagamentos, anúncios, assinatura e publicação em loja. Migração automática de dados AleJoias e restauração de backup pela interface não são capacidades herdadas.

O backup e a restauração usam o seletor SAF. O arquivo é identificado por `VENDAS_SIMPLES_BACKUP_METADATA`, `FORMAT_VERSION = 1` e `SCHEMA_VERSION = 3`; a validação exige tabelas obrigatórias e `PRAGMA integrity_check`. O backup não contém entitlement. A restauração mantém rollback do banco atual, mas uma interrupção extrema durante a substituição física pode exigir recuperação manual; tornar essa troca plenamente transacional é melhoria futura.

## Regras a consolidar antes de implementar

- Unidade monetária proposta: BRL, apresentação pt-BR. Definir arredondamento e destino do centavo residual no parcelamento.
- Definir vencimento mensal por calendário ou intervalo de dias, incluindo fim de mês e ano bissexto; não supor que o legado já atende à decisão.
- Definir tratamento de pagamento acima do saldo, zero/negativo e correção para valor inferior/superior ao atual.
- Definir se edição/exclusão afeta apenas uma parcela ou toda a compra e o destino de pagamentos/fotos associados.
- Confirmar significado de “recebido no mês”: a referência documenta painel filtrado pela data da venda, o que difere de fluxo de caixa por data do pagamento.

## Condição de conclusão

MVP aceito quando os itens incluídos têm evidência em aparelho/emulador, resultados financeiros conferidos com dados sintéticos, isolamento verificado e limitações descritas. Não basta compilar. Distribuição pública exige ainda decisões de recuperação de dados, privacidade, assinatura, suporte e canal, sem implicar autorização para publicação.
