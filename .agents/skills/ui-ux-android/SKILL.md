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
