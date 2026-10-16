# Evolução de funcionalidades

As datas e commits são marcos observáveis no Git, não afirmação de que a função foi publicada na mesma data. Recursos presentes no primeiro snapshot não têm uma proveniência anterior disponível neste clone.

| Área | Marco e mudança | Rastreamento |
|---|---|---|
| Clientes/vendas/parcelas/pagamentos/histórico/fotos | Já aparecem na primeira base Android. Não há evidência de qual release anterior os introduziu. | `5c8c4f2`, `LocalDatabase.kt`, `Models.kt`, `MainActivity.kt`. |
| Regras FREE/dinheiro | FREE até 30; regras em centavos e aplicação do bloqueio à nova venda. | `692c663`, `b7bef44`, `b6befe5`; `docs/DECISOES.md`. |
| Pesquisa, navegação e telas | Série de correções de composição: resumo/relatórios, filtros, botões, voltar, menus, status parcial e paleta. Causas detalhadas não estão descritas para todo commit. | Commits `69078b9`–`801ada1` em 29/09. |
| PDF/CSV/compartilhamento | Saídas e compartilhamentos evoluem na base. Formato pt-BR e escaping CSV são refinados em V11. | `5c8c4f2`, `2b88ccd`, `8fed0b2`; [EXPORTACAO_CSV](EXPORTACAO_CSV.md), [RELEASE_V11](RELEASE_V11.md). |
| Licença Completa | Compra via Play e preço dinâmico; compra única sem assinatura, produto `full_access`. | `896c0f5`, `a18a4d0`, `c2e97c2`; [MONETIZACAO](MONETIZACAO.md). |
| Backup/restauração | Backup FULL_ACCESS; restauração local; em V11 nome comercial e compatibilidade legada; V12 validação mais estrita. | `2500ef5`, `d8e6fcf`, `bc70079`, `b2e65a0`. |
| Nome do negócio | Personalização gratuita, armazenamento de preferência, cabeçalho, WhatsApp/PDF e depois backup completo. | `eae7711`, `bc70079`; `docs/RELEASE_V11.md`. |
| Interface Financeiro/Cliente/Vendas | Financeiro e relatório por cliente compactados, gráficos rotulados por valor/percentual, cards de venda reorganizados sem recalcular status. | `2159ad6`, `8fed0b2`, `0a85fe0`, `8aa3488`; testes relacionados em `RELEASE_V11.md`. |
| Política e notificações | Texto offline, fluxo de acesso à política e controles/permissão contextual para lembretes locais. | `5467c40`; [FASE4](FASE4_CORRECOES_TECNICAS.md), [FASE6A](FASE6A_PUBLICACAO_POLITICA.md). |

## Funcionalidades preservadas por decisão

FREE mantém clientes ilimitados, leitura/pagamentos existentes, relatórios e CSV; o teto bloqueia criação após 30. FULL_ACCESS compra única libera recursos correspondentes e backup/restauração; a restauração da licença ocorre pelo Play e é distinta de restaurar dados. Operação comercial é local/offline; isso não significa que integrações externas (Billing, intents de compartilhamento) nunca usem rede.

Para validação de mudanças, usar [VALIDACAO](VALIDACAO.md), [REGRAS_FINANCEIRAS](REGRAS_FINANCEIRAS.md), [MONETIZACAO](MONETIZACAO.md) e skills específicas.
