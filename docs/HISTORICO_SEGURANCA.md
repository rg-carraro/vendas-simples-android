# Histórico de segurança e privacidade

## Escopo e níveis de evidência

Esta linha do tempo diferencia controles que o código implementa, testes que foram executados e questões que exigem verificação externa. Ela não é certificação de segurança nem declaração aprovada da Google Play. Relatórios de fase são a fonte para os detalhes e limites.

| Período/marco | Proteção ou decisão | Estado da evidência |
|---|---|---|
| 29/09 — base e auditoria pré-Billing | Dados comerciais locais em SQLite; FileProvider/compartilhamento revisados; decisões offline-first, dinheiro seguro e validações registradas. | Código e [AUDITORIA_PRE_BILLING](AUDITORIA_PRE_BILLING.md); não prova ausência de transmissão por dependências posteriores. |
| 29/09–02/10 | Assinatura release externa ao código; backup oculto no FREE; Billing/entitlement; backup/restauração FULL_ACCESS; política inicial/site. | Commits `87588cf`, `2500ef5`, `ce6abb3`, `a18a4d0`, `d8e6fcf`, `5b496bc`. |
| V11 | Preferência de negócio privada; inclusão opcional em backup; formatos antigos mantidos; entitlement separado do banco/arquivo. `allowBackup=false` aparece no projeto. | [RELEASE_V11](RELEASE_V11.md) e testes registrados. Proteção de backup não significa criptografia própria: essa limitação está documentada. |
| Fases 2–4 | Validação SQLite/metadata/IDs/relações/datas/fotos, rejeição de arquivos incompatíveis, cópia intermediária/rollback e controle de notificações/permissões/API. Política offline e teste de paridade local. | [FASE2](FASE2_SEGURANCA_CONFORMIDADE.md), [FASE3](FASE3_REVISAO_HOMOLOGACAO.md), [FASE4](FASE4_CORRECOES_TECNICAS.md); relatórios documentam testes e gaps de runtime. |
| Fase 6C | Revisão de categorias Data Safety e Billing/Data Transport/CCT, versões/faixas ativas e política pública. | **PENDENTE**: payloads/finalidades/destinos/retention de SDK, respostas atuais da Console e paridade remota não confirmadas de forma independente. |
| V12 | Build/AAB local assinado e validado; código estático de backup/privacidade e regressão unitária/integrada. | **CONFIRMADO localmente** nos relatórios 7H–7J. Não comprova upload, aprovação, Data Safety correta ou testes físicos. |

## Controles documentados no código

- Banco e preferência de negócio são locais; não foi encontrado backend próprio de vendas, login ou conta do app na revisão da Fase 6C.
- Saídas CSV/PDF/backup e mensagem de cobrança podem conter dados comerciais. O compartilhamento depende de ação e destino selecionado; cópias externas têm ciclo independente.
- A licença vem do Google Play Billing. Entitlement não é importado do backup e falha de licença não deve apagar dados.
- Validação de backup verifica estrutura/tabelas/metadata/relacionamentos e integridade; falha deve preservar a base anterior. Backup SQLite local **não tem criptografia própria**.
- A política existe em asset offline e em página local do site. A igualdade local testada não comprova o que a página pública está servindo hoje.
- Manifesto/permissões e implementação de notificações foram revistos na Fase 4; permissões não são por si só evidência de coleta.

## Evidência externa ainda necessária

1. Inspecionar na Play Console as respostas atuais de Segurança dos dados, status, tipos, coleta/compartilhamento, finalidade, obrigatoriedade, criptografia e exclusão.
2. Listar faixas e todos os `versionCode`s ainda distribuídos; declaração deve considerar versões e SDKs que continuam disponíveis.
3. Obter documento oficial/confirmatório do provedor Billing/SDK ou evidência técnica controlada sobre diagnóstico, identificadores, eventos, destinos, retenção e transmissão automática/opcional. A análise local detectou caminho de transporte possível, mas não classificou com segurança o payload final.
4. Verificar a URL pública e conteúdo servido da política, comparando com a revisão local de 15/10/2026.
5. Executar update V11→V12, API 24, Billing Play e restore em ambiente de teste com dados sintéticos.

Não usar exceção de dados de cartão para inferir classificação de histórico de compras ou diagnósticos. Não modificar a Console nem afirmar conformidade completa até reconciliar evidências. Ver [FASE6C](FASE6C_SEGURANCA_DADOS_PLAY.md), [SEGURANCA_DADOS_PLAY](SEGURANCA_DADOS_PLAY.md) e [FASE7J](FASE7J_PREPARACAO_ALPHA_V12.md).
