---
name: ui-ux-android
description: Padrões de usabilidade validados no Vendas Simples, adaptáveis a outros aplicativos sem alterar sua identidade visual.
---
# UI/UX Android — lições do Vendas Simples

## Princípios
- Preservar identidade visual, cores, marca, tipografia e fluxos de cada produto. Reutilizar padrões, não copiar telas inteiras.
- Cabeçalho com seta voltar nas telas internas; botão Android voltar consistente; confirmar descarte de formulário modificado.
- Layout compacto sem sacrificar legibilidade, acessibilidade ou áreas de toque (alvo mínimo recomendado 48 dp).
- Cards de vendas/clientes/financeiro com informações essenciais e hierarquia clara; reduzir espaços vazios e botões redundantes.
- Diálogos de detalhes: título fixo, conteúdo rolável, ações importantes sempre acessíveis no rodapé.
- Formulários: campos e ações acessíveis com teclado aberto; espaçamento adequado para remover/ver foto (referência V11: 16 dp).
- Financeiro: seletor de mês compacto, indicadores e gráficos na mesma tela, evitar informações repetidas.
- Relatório por cliente: busca compacta e cartões com vendido/recebido/faltante; suportar nomes longos.
- Personalização opcional do nome do negócio: persistência, edição, WhatsApp, PDF; não substituir a marca do aplicativo.
- Datas e moedas legíveis em pt-BR, sem alterar os valores internos.

## Checklist visual
- Celular pequeno, fonte 100% e 200%, teclado aberto, rotação se suportada, texto longo e acentos.
- Sem cortes, sobreposição, rolagem horizontal inesperada ou ações ocultas.
- Navegação consistente em todos os caminhos; back não encerra app indevidamente.
- Conferir acessibilidade e contraste sem impor nova paleta.

## Aplicação no AleJoias
Antes de qualquer mudança, mapear telas, recursos, tema, fluxos, entidades e componentes reais do AleJoias. Fazer correspondência de padrões, implementar em etapas, manter recursos exclusivos e solicitar homologação visual comparativa.


## Padrões solicitados na homologação V11
- Separar marca fixa do aplicativo e identidade opcional do negócio. Manter a frase de apoio; restringir a linha secundária quando necessário, com nome completo acessível na configuração. Testar nomes longos, acentos e fonte ampliada sem invadir Voltar ou outras ações.
- Colocar configurações secundárias no menu de três pontos existente, preservando suas opções e reutilizando edição/salvar/cancelar. Atualizar a apresentação imediatamente e manter a persistência existente.
- Miniaturas com ação contextual: imagem à esquerda, remover à direita, cerca de 16 dp de intervalo, ícone e texto, alvo mínimo de 48 dp. Identificar a foto por índice acessível e reconstruir índices após remover. Empilhar se largura/fonte impedirem composição horizontal; evitar botão de largura total. Preservar visualização e rodapé Salvar/Cancelar.

Esses padrões são candidatos à futura aplicação no AleJoias Vendas; a implementação V11 e seus testes ficam em docs/RELEASE_V11.md. Não implicam portabilidade automática nem autorização para alterar o AleJoias. Adaptar à marca, tema, componentes e funcionalidades próprios e homologar separadamente.


## Relatórios financeiros compactos
- Cards de cliente: nome/quantidade em um cabeçalho; Vendido, Recebido e Faltante com rótulos menores e três colunas quando couberem. Somente o valor Faltante em negrito; não alterar somas ou população para obter esse layout.
- Medir os textos com a fonte efetiva e a largura disponível do componente. Reorganizar em duas/uma coluna antes de cortar valores; altura livre e nomes completos. Em uma coluna, permitir quebra de valores extremos sem sobreposição ou reticências.
- Validar valores pequenos/grandes, nomes longos, 320 dp, fonte ampliada, paisagem, pesquisa e retorno. Evidências do Vendas Simples não homologam o AleJoias; adaptar identidade e regras próprias separadamente.


## Valores e percentuais em barras financeiras
- Usar os totais já calculados pelo painel, sem nova consulta, soma ou regra financeira. Nome à esquerda; moeda brasileira e percentual com uma casa decimal à direita, separados por ponto médio.
- Percentuais decimais independentes sobre vendido; denominador zero mostra zero. Não forçar soma 100% nem esconder inconsistências; conferir origem dos totais.
- Dinheiro formatado diretamente dos centavos inteiros com decimal exato. Medir largura real com fonte efetiva; empilhar e permitir quebra antes de cortar valores. Preservar barras, paleta, margens e acessibilidade textual.
- Testar normal/zero/extremos, troca de mês, 320 dp, paisagem e fonte 200%. Candidato ao AleJoias: adaptar sem modificar identidade, filtros, banco ou regras próprias; não implica homologação desse produto.


## Cards compactos de vendas
- Reutilizar a etiqueta de status existente sem reclassificar por data ou saldo. Preservar textos, cores de texto/fundo, prioridade de quitação e tratamento de parcial vencido. Reorganizar apenas seu posicionamento.
- Nome à esquerda e etiqueta no topo à direita; medir largura real incluindo padding. Nomes completos podem quebrar; empilhar a etiqueta alinhada à direita quando faltar espaço. Considerar a escala efetiva da fonte ao reservar largura legível para o nome, evitando colunas estreitas em fonte 200%.
- Parcela/À Vista, compra/vencimento e Valor/Pago/Faltante em faixas compactas. Três colunas quando couberem, duas/uma com largura/fonte ampliada; somente o valor Faltante em negrito. Preservar data de pagamento, fotos e toque/opções existentes.
- Conferir 320 dp, fontes 150%/200%, paisagem, nomes/valores elevados, sem corte/sobreposição. Padrão candidato ao AleJoias, adaptando cores, status, dados e funcionalidades próprios; não autoriza alterar a origem.

## Histórico e continuidade
Antes de mudar uma tela existente, consulte docs/INDEX.md, docs/EVOLUCAO_FUNCIONALIDADES.md e os commits relacionados; confirme no código atual quais comportamentos devem ser preservados. Registre testes e limitações em docs/HISTORICO_TESTES.md/relatório da versão. Padrão histórico não é prova de homologação no app ou na referência.