# Fase 15A — auditoria de legibilidade e contraste

**Data:** 23/10/2026
**Escopo:** diagnóstico documental da interface V14 (`versionCode 14`, `versionName 1.0.14`), sem alterações funcionais ou visuais.
**Fonte do código auditado:** worktree `.worktrees/v14-pdf-identidade-privacidade`, branch `feature/v14-pdf-identidade-privacidade`, commit `d7ed5ae2fd3b4a4b22958c6deb7bdb549cc50195`.
**Distribuição:** o proprietário informa que a V14 já foi distribuída pela Google Play. Não foi consultada a Play Console nesta auditoria.

## 1. Estado do repositório e preservação

O checkout principal disponível está na branch `fix/v12-seguranca-conformidade`, com alterações e arquivos não rastreados preexistentes. Há múltiplas worktrees; elas foram inventariadas e preservadas. A worktree V14 estava limpa e sincronizada com `origin/feature/v14-pdf-identidade-privacidade` no início da inspeção. Nenhum arquivo de código, recurso, versão ou artefato V14 foi alterado; nenhum AAB foi gerado e não houve publicação, PR, merge, commit ou push.

A worktree local `master` está no commit `11214de` e aparece 11 commits atrás de sua referência local `origin/master` (`88c9bb1`). Essa referência local declara `versionCode 13` / `versionName 1.0.13`; portanto, ela não contém o estado V14 auditado. A branch V14 separada declara `14` / `1.0.14`. Não foi feito `fetch`; não é possível afirmar que a referência local de `origin/master` representa o estado remoto atual. Assim, o código analisado para esta auditoria é explicitamente a worktree V14, não a master local.

`AGENTS.md`, `README.md`, `docs/DECISOES.md`, `docs/ARQUITETURA.md`, `docs/VALIDACAO.md`, `docs/RELEASE_PLAY_STORE.md` e as skills locais de interface e validação foram lidos. `docs/CHECKLIST_TESTES.md`, citado no pedido, não existe; o plano aplicável encontrado é `docs/VALIDACAO.md`.

## 2. Mecanismos visuais observados

- A Activity monta a interface programaticamente em `MainActivity.kt`.
- `AppThemeBase` herda `Theme.MaterialComponents.DayNight.NoActionBar`; há tema em `values/styles.xml` e ajuste de barra de navegação em `values-v27/styles.xml`.
- Não há diretório `values-night` nem paleta noturna correspondente.
- A maior parte das telas usa cores RGB fixas em propriedades da Activity e as aplica diretamente a textos, fundos, campos, bordas, botões e estados. O modo DayNight, portanto, não governa uniformemente a interface customizada.
- O estilo de diálogo define superfície branca, texto primário `#1E293B`, texto secundário `#64748B` e cores próprias de marca. Os diálogos da Activity são construídos com `android.app.AlertDialog.Builder`; a aplicação efetiva do atributo Material `materialAlertDialogTheme` a esses diálogos de plataforma não foi confirmada visualmente.
- O tema fixa fundo e barras de sistema em tons claros. Componentes padrão do Android, menus flutuantes, calendário, teclado, Toasts, navegação e estados desativados podem seguir estilos/contrastes do sistema diferentes dos componentes desenhados pela Activity.
- Campos customizados fixam cor de texto, dica e tint. O estado de foco/erro/desativado e as cores internas do teclado não foram medidos em runtime.

## 3. Inventário de telas e componentes examinados por leitura estática

O inventário abaixo foi obtido por inspeção das funções de construção e navegação de `MainActivity.kt`; não representa conferência visual de cada estado.

| Área | Telas e componentes |
| --- | --- |
| Tela inicial | Cabeçalho/marca, nome opcional do negócio, menu de três pontos, cartões de resumo e navegação para vendas, financeiro e resumo |
| Menu e informação | PopupMenu de opções; tela Sobre; leitura da política de privacidade; contato e diálogos de indisponibilidade/cópia |
| Personalização | Diálogo de primeiro uso/edição, campo de nome, texto auxiliar, botões Salvar e Agora não/Cancelar |
| Vendas | Lista, busca, filtros de texto/período/a receber, chips/estados, cards, datas, valores, cliente e descrição |
| Filtros e datas | Diálogo de intervalo, campos de data, calendário nativo DatePickerDialog e ações Aplicar/Cancelar |
| Ações do card | Diálogo de opções com Ver detalhes, Exportar PDF, Editar, Registrar pagamento, Cobrar, Corrigir pagamento e Deletar |
| Detalhes da venda | Título, etiqueta de estado, dados financeiros e cadastrais, faixa de fotos ampliáveis, Registrar Pagamento, PDF do Card e Fechar |
| Financeiro | Dashboard, cartões de métricas, seletores de mês, detalhamento de métrica e resumo por período/mês |
| Clientes | Resumo de clientes, histórico individual, valores, situação e ações associadas aos cards |
| Formulários | Nova venda/edição, campos de texto/valor/data/parcelas/descrição, fotos, Salvar/Cancelar e confirmação de descarte |
| Pagamentos | Registrar pagamento e corrigir valor pago, campos e mensagens de validação/confirmação |
| Licença | Diálogo de desbloqueio/compra, preço indisponível e botão desativado |
| Backup | Tela/fluxos locais, seletor de arquivo do sistema e mensagens de sucesso/falha |
| Imagem | Visualização ampliada de foto e ação Fechar |
| Mensagens e saída | Toasts de validação, confirmação, erro e estado vazio; notificações; geração/compartilhamento de PDF/CSV e diálogos do sistema |

## 4. Problemas confirmados por análise estática e medições

“Confirmado” nesta seção significa comprovado pelo código ou pelo cálculo sobre as cores declaradas. Não significa que o defeito visual tenha sido observado em uma tela real.

| ID | Evidência | Resultado / impacto |
| --- | --- | --- |
| C-01 | `corTextoSecundario = #64748B`, `corFundo = #F5F7FB`; usado em rótulos, subtítulos e dicas | Contraste calculado de **4,44:1**, abaixo dos 4,5:1 WCAG 2.2 AA para texto normal nesse fundo. Contra branco, o mesmo par é 4,76:1 e passa. Aplica-se a diversos textos secundários sobre a tela-base. |
| C-02 | Texto e superfícies customizados usam cores fixas, sem recursos `values-night` | O tema anuncia suporte DayNight, mas a paleta customizada não tem variante noturna. O conteúdo próprio mantém cores claras no modo escuro do sistema, enquanto elementos padrão podem trocar de aparência. A existência dessa configuração desigual é confirmada; a aparência exata e a severidade em cada dispositivo dependem do runtime. |
| C-03 | `#1E293B` sobre `#F5F7FB`/branco: 13,64:1 / 14,63:1; texto branco sobre primária `#2563EB`: 5,17:1 | Os pares principais medidos passam 4,5:1. Isso não cobre estados pressionados, desativados, tonalidades por alpha ou superfícies diferentes não explicitadas. |
| C-04 | Estados usam texto explícito e fundos pastel: Em Aberto `#7D5720/#F4E5C5`, Parcial `#9C5214/#FCE0B8`, Quitado `#1E3A5F/#C5E7D6`, Vencido `#99372F/#F8CDC7` | Contrastes calculados: **5,18:1**, **4,54:1**, **8,64:1** e **4,95:1** respectivamente. Passam o critério de texto normal, com Parcial próximo do limite; não foi verificado em tela com filtros de cor, brilho ou modo de alto contraste. |
| C-05 | Cor de borda padrão `#CBD5E1` sobre branco | Contraste de aproximadamente **1,48:1**. Se essa borda for o único indicador visual de limite de um campo/controle, pode não atingir 3:1 para componentes gráficos; se for apenas decoração, a exigência pode não se aplicar. É necessário validar a função visual em tela. |

## 5. Prioridades relatadas pelo proprietário e hipóteses técnicas

Não foram recebidas capturas de tela anexadas a esta conversa. Os itens a seguir são tratados como alvos indicados no pedido; a auditoria não os apresenta como falhas visuais confirmadas.

| Alvo | Evidência estática | Hipótese a confirmar em runtime |
| --- | --- | --- |
| Salvar e Cancelar no diálogo de personalização | Ações de `AlertDialog.Builder`; tema define cor primária azul e cores fixas de diálogo | Verificar cor, contraste, destaque de foco/pressionado, fonte ampliada e modo escuro. Ações nativas podem ser coloridas pelo tema, mas não há captura nem inspeção de estado renderizado. |
| Título “Detalhes da Venda” | Texto customizado `#1E3A5F`; tamanho 20sp, reduzido para 16sp em condição compacta | O contraste contra a superfície branca é alto por cálculo; testar alinhamento, truncamento, escala de fonte, largura estreita e modo escuro. |
| Estados “Quitado” e “Em Aberto” | Texto e fundo explicitamente definidos na etiqueta | Pares calculados passam AA; conferir visual, consistência entre card e diálogo, e distinguir estado além da cor. |
| Registrar Pagamento, PDF do Card e Fechar | Botões customizados; os dois primeiros usam branco sobre azul, Fechar usa azul sobre branco | Contraste base calculado passa; testar rótulos em largura curta, fonte ampliada, quebra, alvos, foco e estado desativado quando aplicável. |
| Campos de texto e mensagens | Texto `#1E293B`, dica `#64748B`, tint `#475569`; mensagens são em boa parte Toast do sistema | Dicas falham por 0,06 no fundo principal; outras superfícies podem passar. Toasts/teclado e aparência de erro dependem do Android e não foram medidos. |

## 6. Possíveis causas técnicas

1. Tema `DayNight` combinado com a ausência de `values-night` e cores definidas diretamente no Kotlin pode produzir uma interface de aparência clara no modo escuro, com menus e componentes do sistema em outra paleta.
2. O estilo Material de diálogo é configurado por atributo, mas o código usa `android.app.AlertDialog.Builder`; é necessário verificar se o overlay desejado alcança cada diálogo usado. Pode haver divergência entre ações nativas, títulos e conteúdo customizado.
3. Estados desativados, pressionados, selecionados e foco usam tint/alpha aplicados por componentes nativos ou por implementação própria; a paleta nominal não demonstra o contraste final desses estados.
4. Fonte escalada pode afetar botões e controles em layouts horizontais/rodapés e rótulos de estado. O cabeçalho compacto considera `fontScale > 1.3`, mas isso não prova que todos os componentes refluam sem truncar.
5. A borda clara pode deixar campos e limites de cartão pouco perceptíveis em certas telas/brilhos, mesmo quando texto passa AA.

## 7. Testes realmente executados nesta auditoria

- Leitura estática do tema, manifesto, recursos, construção de telas, cores e estilos na worktree V14.
- Conferência documental de arquitetura, decisões, validação, release e skills de interface/validação.
- Cálculo independente de contraste WCAG para pares de cores listados acima.
- Conferência de branches, HEAD, estado da worktree V14 e comparação das versões declaradas pela worktree V14 e pela referência local `origin/master`.
- Tentativa de executar `:app:testDebugUnitTest`, `:app:testReleaseUnitTest`, `:app:compileDebugAndroidTestKotlin`, `:app:lintDebug`, `:app:lintRelease` e `:app:assembleDebug`. A primeira tentativa não encontrou cache Gradle em `C:\.gradle`; repetida com `GRADLE_USER_HOME` do repositório, falhou porque não foi possível resolver o plugin `org.jetbrains.kotlin.android:2.2.20` nos repositórios. A repetição com acesso escalado também falhou. **Nenhuma tarefa Gradle desta auditoria foi concluída.**

O relatório da Fase 14 registra que, em 19/10/2026, 46 testes unitários por variante passaram, AndroidTest compilou, lint passou com 0 erros e 67 avisos por variante, e `assembleDebug`/`bundleRelease` passaram. São evidências documentadas de uma execução anterior; não foram repetidas nesta auditoria nem constituem validação visual da Fase 15A.

## 8. Testes visuais e técnicos pendentes

- Não foi feita inspeção visual das prioridades citadas: nenhuma captura foi anexada nesta conversa.
- O aparelho ADB Samsung SM-S906E foi identificado como conectado no turno anterior, mas não foi aberto nem fotografado nesta auditoria. A skill local de validação orienta não executar a ferramenta de UI em aparelho real ou base com dados existentes. Portanto, nenhum teste visual é declarado como executado.
- Pendente conferir modo claro e escuro, texto padrão/ampliado, estados ativo/desativado/pressionado, dicas, erros, menus, diálogos, teclado, rolagem, quebra/truncamento, orientação retrato/paisagem e larguras disponíveis em AVD controlado com dados sintéticos.
- Pendente conferir largura de 320 dp, escalas de fonte de 150%/200%, textos longos/acentuados e alvos de toque recomendados de pelo menos 48 dp, conforme checklist local de UI/UX.
- Pendente repetir testes unitários, compilação do AndroidTest, lint e build debug após disponibilidade/resolução das dependências Gradle.
- Pendente conferir configurações de fonte grande e tamanho de exibição no Android, além de tema escuro forçado/contraste do fabricante quando aplicável.
- Não foram testados casos com dados reais e não foram executadas ações de criação, edição, pagamento, exclusão, backup ou restauração.

## 9. Recomendação de correção mínima para uma fase futura

Sem aplicar mudanças nesta Fase 15A, recomenda-se priorizar:

1. Ajustar somente o token de texto secundário para que atinja pelo menos 4,5:1 contra cada fundo onde aparece; a combinação medida atual em `#F5F7FB` é 4,44:1.
2. Tornar explícita uma política visual para modo escuro: ou definir paleta noturna completa para componentes próprios e tema, ou declarar e validar a interface como clara independentemente da preferência do sistema, evitando o estado misto. A escolha deve preservar a identidade existente.
3. Harmonizar os AlertDialogs usados com o overlay de diálogo efetivo e conferir todos os botões nativos, inclusive desativados, foco e estados de pressão.
4. Reavaliar contraste de bordas apenas onde elas são necessárias para identificar controles, sem escurecer bordas meramente decorativas por padrão.
5. Manter os estados textuais e não depender apenas da cor; ajustar os casos que falharem em runtime sem tocar em cálculos, persistência, Billing, backup ou PDFs.

## 10. Riscos de regressão e critérios de aceite para eventual V15

**Riscos:** alterar tokens compartilhados pode afetar todas as telas e documentos que usam o mesmo valor; troca do tema pode mudar diálogos, menu, calendário e barras do sistema; variantes noturnas podem revelar componentes que não usam tokens; fontes grandes podem aumentar altura e rolagem de diálogos; mudança de borda/tint pode reduzir distinção de foco ou erro. Qualquer correção deve preservar todos os fluxos, nomes, cálculos e IDs da V14.

**Critérios propostos para aceite V15:**

- Todos os textos normais avaliados alcançam contraste ≥ 4,5:1; texto grande ≥ 3:1; limites/indicadores essenciais de controles ≥ 3:1, conforme aplicabilidade WCAG 2.2 AA.
- Os itens prioritários (personalização, detalhes da venda, estados, ações, campos e mensagens) são conferidos em capturas no modo claro e escuro, fonte padrão e ampliada, em retrato/paisagem e em tela estreita e ampla disponíveis.
- Estados ativo, desativado, pressionado, foco, erro e selecionado permanecem distinguíveis; ações não truncam nem ficam inacessíveis com fonte ampliada/teclado.
- Alvos interativos recomendados medem pelo menos 48 dp; em 320 dp e fonte 150%/200%, nomes, valores e ações quebram ou reorganizam sem reticências indevidas, sobreposição ou rolagem horizontal inesperada.
- Diálogos e menus mantêm contraste consistente com a tela que os abriu; conteúdo longo rola e botões Salvar/Cancelar permanecem legíveis e acionáveis.
- Testes unitários, AndroidTest aplicável, lint e `assembleDebug` passam; falhas/avisos preexistentes são separados de regressões novas.
- Regressão funcional usa dados sintéticos e confirma os fluxos existentes; nenhum teste destrutivo usa a base real do proprietário.
- Evidências registram commit, dispositivo/Android ou AVD, escala de fonte, modo, dimensões/orientação, capturas, resultados e limitações. V14 e sua distribuição permanecem preservadas.

## Referências locais

- Código auditado: `app/src/main/java/com/example/controlevendas/MainActivity.kt`, `app/src/main/res/values/styles.xml`, `app/src/main/res/values-v27/styles.xml`, `app/src/main/AndroidManifest.xml` e `app/build.gradle` na worktree V14.
- Contexto da versão: `docs/FASE14_FUNCIONAL_PDF_PRIVACIDADE.md` na worktree V14.
- Plano geral: `docs/VALIDACAO.md`.
- Skills: `.agents/skills/vendas-simples-interface/SKILL.md` e `.agents/skills/vendas-simples-validar/SKILL.md`.
