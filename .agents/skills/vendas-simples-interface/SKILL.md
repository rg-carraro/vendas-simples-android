---
name: vendas-simples-interface
description: Evoluir UI, menus, retorno, filtros, cards e diálogos do Vendas Simples preservando seus fluxos e identidade.
---

# Interface do Vendas Simples

Leia [decisões](../../../docs/DECISOES.md),
[relatório de UI](../../../docs/ATUALIZACAO_ALPHA_UI_BILLING.md) e
[validação](../../../docs/VALIDACAO.md). Inspecione as funções afetadas em
`MainActivity.kt`; resultados de rodadas anteriores não homologam o APK atual.

Use `configurarRetorno` para que seta e Voltar Android chamem a mesma ação.
No menu, o callback fica desativado e a seta oculta. Preserve os destinos
de histórico de cliente e detalhamentos financeiros, filtros e meses.
Não prometa restauração de rolagem ou rascunho após rotação/process death.

Mantenha título e ações acessíveis nos diálogos, com conteúdo rolável e
confirmação de descarte quando campos/fotos forem alterados. Preserve campos,
fotos ampliáveis, PDF, pagamentos e todas as ações existentes. Use os helpers
responsivos locais, textos completos e alvos de toque de pelo menos 48 dp.
Paleta, ícones, armazenamento e Billing pertencem ao Vendas Simples.

Verifique o código financeiro e de persistência antes/depois; melhoria visual
não autoriza mudar cálculos, schema, IDs ou licença. Teste teclado, cancelamento,
fonte ampliada, tela estreita, paisagem e retorno. A ferramenta
`tools/validar_ui_build10.py` exige AVD Build10/debug e recusa aparelho real;
`--seed` requer base vazia e usa apenas dados sintéticos, sem mudar schema.
Evidências ficam em `.gradle-user/ui-refinement/`, fora do Git.
Registre limites de smoke tests e execute o build proporcional à alteração.


No cabeçalho compartilhado, mantenha a marca Vendas Simples separada do nome opcional do negócio e preserve a frase de apoio. Personalizar negócio pertence ao menu de três pontos, disponível também no FREE, reutilizando a configuração.
Em fotos de rascunho, priorize miniatura/ação lado a lado com 16 dp, índice acessível e remoção isolada; empilhe quando necessário sem botão de largura total. Os padrões e a candidatura ao AleJoias estão na [skill de UI/UX](../ui-ux-android/SKILL.md); não autorizam editar a origem.

Antes de modificar fluxo existente, use docs/INDEX.md, docs/EVOLUCAO_FUNCIONALIDADES.md e git log --all --follow no arquivo afetado. Preserve causas já corrigidas e registre no relatório da versão qualquer mudança de comportamento/teste; não trate evidência de UI antiga como validação atual.