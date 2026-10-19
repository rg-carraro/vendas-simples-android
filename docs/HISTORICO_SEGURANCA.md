# Histórico de segurança e privacidade

## Escopo e níveis de evidência

Esta linha do tempo diferencia controles que o código implementa, testes que foram executados e questões que exigem verificação externa. Ela não é certificação de segurança nem declaração aprovada da Google Play. Relatórios de fase são a fonte para os detalhes e limites.

| Período/marco | Proteção ou decisão | Estado da evidência |
|---|---|---|
| 29/09 — base e auditoria pré-Billing | Dados comerciais locais em SQLite; FileProvider/compartilhamento revisados; decisões offline-first, dinheiro seguro e validações registradas. | Código e [AUDITORIA_PRE_BILLING](AUDITORIA_PRE_BILLING.md); não prova ausência de transmissão por dependências posteriores. |
| 29/09–02/10 | Assinatura release externa ao código; backup oculto no FREE; Billing/entitlement; backup/restauração FULL_ACCESS; política inicial/site. | Commits `87588cf`, `2500ef5`, `ce6abb3`, `a18a4d0`, `d8e6fcf`, `5b496bc`. |
| V11 | Preferência de negócio privada; inclusão opcional em backup; formatos antigos mantidos; entitlement separado do banco/arquivo. `allowBackup=false` aparece no projeto. | [RELEASE_V11](RELEASE_V11.md) e testes registrados. Proteção de backup não significa criptografia própria: essa limitação está documentada. |
| Fases 2–4 | Validação SQLite/metadata/IDs/relações/datas/fotos, rejeição de arquivos incompatíveis, cópia intermediária/rollback e controle de notificações/permissões/API. Política offline e teste de paridade local. | [FASE2](FASE2_SEGURANCA_CONFORMIDADE.md), [FASE3](FASE3_REVISAO_HOMOLOGACAO.md), [FASE4](FASE4_CORRECOES_TECNICAS.md); relatórios documentam testes e gaps de runtime. |
| Fase 6C e fases 9C–9F | Revisão de categorias Data Safety e Billing/Data Transport/CCT, versões/faixas ativas e política pública. Após informação ao suporte sobre os fluxos V12, Fase 9F registrou orientação condicional sobre dados locais e telemetria estritamente da infraestrutura Play Billing descrita. | A conclusão é condicionada à arquitetura e não é pré-aprovação. Conferir ficha vigente/versões na Console e conteúdo da página pública; reavaliar novos SDKs ou fluxos. Ver [Pendências atuais](PENDENCIAS_ATUAIS.md). |
| V12 | Build/AAB local assinado e validado; código estático de backup/privacidade e regressão unitária/integrada. | **CONFIRMADO localmente** nos relatórios 7H–7J. Não comprova upload, aprovação, Data Safety correta ou testes físicos. |

## Controles documentados no código

- Banco e preferência de negócio são locais; não foi encontrado backend próprio de vendas, login ou conta do app na revisão da Fase 6C.
- Saídas CSV/PDF/backup e mensagem de cobrança podem conter dados comerciais. O compartilhamento depende de ação e destino selecionado; cópias externas têm ciclo independente.
- A licença vem do Google Play Billing. Entitlement não é importado do backup e falha de licença não deve apagar dados.
- Validação de backup verifica estrutura/tabelas/metadata/relacionamentos e integridade; falha deve preservar a base anterior. Backup SQLite local **não tem criptografia própria**.
- A política existe em asset offline e em página local do site. A igualdade local testada não comprova o que a página pública está servindo hoje.
- Manifesto/permissões e implementação de notificações foram revistos na Fase 4; permissões não são por si só evidência de coleta.

## Pendências externas remanescentes (estado atualizado)

1. Confirmar na Console a declaração atual, as faixas e versões ativas; a orientação Fase 9F não é aprovação formal.
2. Confirmar que o artefato/fluxo V13 continua no escopo descrito ao suporte; reapreciar se houver novo SDK, transmissão, destino ou finalidade.
3. Verificar a URL pública e conteúdo servido da política, comparando com os arquivos locais de 15/10/2026. A checagem live não foi possível nesta Fase 14.
4. Executar update V12→V13, runtime instrumentado, API 24 e Billing Play em ambiente de teste com dados sintéticos.

Não usar exceção de dados de cartão para inferir classificação de histórico de compras ou diagnósticos. Não confundir orientação de suporte com aprovação, nem editar a Console sem autorização. Ver [FASE6C](FASE6C_SEGURANCA_DADOS_PLAY.md), [SEGURANCA_DADOS_PLAY](SEGURANCA_DADOS_PLAY.md), [Fase 9F](FASE9F_ENCERRAMENTO_DATA_SAFETY_ALPHA.md) e [Pendências atuais](PENDENCIAS_ATUAIS.md).
