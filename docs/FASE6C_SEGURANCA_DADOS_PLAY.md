# Fase 6C — Segurança dos dados Google Play, consolidação e preparação da V12

Data: 16/10/2026. Repositório `rg-carraro/vendas-simples-android`; branch `fix/v12-seguranca-conformidade`; `HEAD` e commit da tag `v1.0.11-rc.1`: `8aa34881755f222f9b090691aaa6127b31b1a363`. Código desta linha de trabalho permanece `versionCode 11` / `versionName 1.0.11`. Este relatório é análise documental; não representa declaração enviada à Play Console, release ou auditoria externa independente.

## 1. Resumo executivo

- O usuário informou **13/13 verificações manuais aprovadas** na V11 em Samsung Galaxy S22+ (SM-S906E), Android 16/API 36, instalado pela Google Play. O registro está em [FASE6B1_HOMOLOGACAO_FISICA_V11.md](FASE6B1_HOMOLOGACAO_FISICA_V11.md) e é explicitamente evidência fornecida pelo usuário; o Codex não controlou o telefone nem recebeu capturas ou logs.
- A revisão de código confirma que dados comerciais e fotos residem no SQLite privado e que o nome do negócio é preferência privada. O código do app não contém backend próprio, login, Analytics, anúncios ou Crashlytics identificados. Isso não equivale a “nenhum dado é transmitido”: Billing consulta Google Play e o inventário estático anterior identificou caminho de telemetria/transporte na biblioteca e dependências.
- O código instancia Billing ao iniciar a Activity, consulta o produto `full_access` e as compras `INAPP` existentes; o callback usa o estado para liberar recursos e reconhece compra com token em memória. A análise estática anterior encontrou mecanismo de eventos via Data Transport/CCT e metadados técnicos possíveis. **Não há fonte oficial específica da Billing 9.1.0, captura de tráfego ou inventário de payloads que determine todos os dados efetivamente transmitidos, finalidade, classificação, destinatário, obrigatoriedade ou retenção.** A matriz deixa essas decisões pendentes.
- A orientação oficial da Play exige considerar SDKs e todas as versões distribuídas do pacote. O formulário real e as faixas/versões ativas não foram acessados. A exceção de plataforma de pagamento é limitada aos dados de cartão que o app não acessa e que o processador recolhe diretamente; não resolve automaticamente histórico de compras ou telemetria.
- O usuário informa que a política pública de 15/10/2026 está atualizada e visível. O asset local e o `site/privacy/index.html` contêm a revisão de 15/10 e o teste local compara esses conteúdos. A consulta web direta desta execução retornou erro interno da ferramenta, então a publicação atual **não foi independentemente reconfirmada aqui**; relatórios anteriores ainda registram que a página remota estava desatualizada em 15/10. A evidência do usuário mais recente deve ser preservada, e recomenda-se anexar ao dossiê a resposta HTTP/data/hash ou captura sanitizada obtida pelo responsável.
- A V12 está **APTA COM RESSALVAS para preparação técnica/documental**, não para produção ou submissão de declarações. Antes de distribuir V12, resolver as pendências de Segurança dos dados com o Console e versões efetivamente ativas, concluir inventário Billing, validar política pública, API 24 e atualização/compra pela faixa Play. Não houve alteração de Console, publicação, AAB ou release nesta fase.

## 2. Estado inicial do Git e método

Na primeira inspeção desta fase, `git status --short` tinha 36 entradas locais, entre mudanças das Fases 2–6B.2, diretórios auxiliares e documentação não commitada. Branch e referências conferidas: `fix/v12-seguranca-conformidade`, `HEAD=8aa34881755f222f9b090691aaa6127b31b1a363` e `v1.0.11-rc.1^{commit}=8aa34881755f222f9b090691aaa6127b31b1a363`. Todo o trabalho preexistente foi preservado.

Foram lidos AGENTS, README, decisões, arquitetura, produto, monetização, segurança, validação, releases, skills de segurança, release, regressão e validação; relatórios das Fases 2–6B.2 e a investigação `SEGURANCA_DADOS_PLAY.md`. A inspeção local incluiu manifesto próprio e mesclado, dependências declaradas, `PlayBillingGateway`, `BillingDomain`, `MainActivity`, `LocalDatabase`, `BusinessPreferences`, `BackupManager`, `FileProvider` paths, política local e teste de paridade local. Nenhum banco, foto, dado de cliente ou conteúdo do Console foi acessado.

Fontes atuais consultadas: [Google Play — Segurança dos dados](https://support.google.com/googleplay/android-developer/answer/10787469?hl=pt-BR), [Política de dados do usuário](https://support.google.com/googleplay/android-developer/answer/10144311?hl=pt-BR), [integração oficial do Billing](https://developer.android.com/google/play/billing/integrate) e [notas Billing 9.1.0](https://developer.android.com/google/play/billing/release-notes). A documentação geral do Billing descreve integração/consultas/transações, não oferece uma ficha completa de dados transmitidos pela 9.1.0.

## 3. Homologação manual relatada da V11

O usuário informou dispositivo Samsung Galaxy S22+ (SM-S906E), Android 16/API 36, instalação V11 da Google Play, `versionCode 11`, `versionName 1.0.11`. A execução ADB no PowerShell interativo foi confirmada pelo usuário; o ambiente isolado do Codex continuou sem acesso por `Cannot mkdir '\\.android': Permission denied`. O relato manual abaixo foi atualizado também no relatório da Fase 6B.1.

| # | Verificação | Resultado relatado |
|---|---|---|
| 1 | Abriu sem travamentos | APROVADO pelo usuário |
| 2 | Navegação clientes, vendas e resumo financeiro | APROVADO pelo usuário |
| 3 | Clientes e vendas existentes preservados | APROVADO pelo usuário |
| 4 | Valores pagos e a receber corretos | APROVADO pelo usuário |
| 5 | Persistência ao fechar e reabrir | APROVADO pelo usuário |
| 6 | Visualização de fotos existentes, quando aplicável | APROVADO pelo usuário |
| 7 | Nome personalizado do negócio, quando configurado | APROVADO pelo usuário |
| 8 | PDF | APROVADO pelo usuário; documento não recebido |
| 9 | CSV | APROVADO pelo usuário; arquivo não recebido |
| 10 | WhatsApp sem envio de cobrança real | APROVADO pelo usuário |
| 11 | Funcionamento offline | APROVADO pelo usuário; detalhes de conectividade não fornecidos |
| 12 | Câmera e galeria | APROVADO pelo usuário |
| 13 | Reinício do telefone e preservação do funcionamento | APROVADO pelo usuário |

**Síntese:** 13/13 aprovados segundo o responsável. O Codex não executou esses testes, não viu a tela nem recebeu evidências visuais/logs. Não se registraram dados financeiros, contatos ou conteúdo real. A licença FULL_ACCESS, compra/restauração, backup/restauração, upgrade V11→V12 e API 24 não fazem parte da lista relatada e continuam sem homologação manual nesta fase. V11 aprovada em 13 fluxos não homologa o código de desenvolvimento/V12.

## 4. Inventário de dados e transmissões

“Coleta” abaixo usa a definição da Play: transmissão para fora do dispositivo por código próprio ou SDK. Processamento exclusivamente local, sem transmissão, não é coleta para o formulário. Compartilhamento por escolha do usuário precisa ser analisado fluxo a fluxo segundo a exceção oficial de ação específica iniciada pelo usuário e expectativa razoável; não é exceção automática para qualquer `Intent` ou provedor SAF.

| Dados/categoria e origem | Armazenamento e finalidade | Transmissão/destino e iniciativa | Exclusão/retenção observada | Evidência e confiança |
|---|---|---|---|---|
| Clientes: nomes e dados cadastrais digitados | Tabela `CLIENTES` do SQLite privado `vendas_simples.db`; exibir cadastro e associar vendas | Nenhum envio próprio identificado. Podem constar de CSV, PDF, backup ou texto de cobrança escolhido pelo usuário; destino pode ser outro app/provedor selecionado. Não há serviço próprio de contas/dados. | Exclusão pelo fluxo de cadastro; dados locais também podem ser removidos ao limpar/desinstalar app. Cópias exportadas têm ciclo independente. | `LocalDatabase.kt`, `MainActivity.kt`, `allowBackup=false`; alta para código inspecionado, cobre apenas esta revisão e não as versões do Console. |
| Vendas, descrições, valores, datas, parcelas e pagamentos | `VENDAS`/`PAGAMENTOS` no mesmo SQLite; histórico, cálculo, saldo, relatórios | Não há upload próprio observado. Conteúdo pode ser incluído em CSV/PDF/backup explícito e mensagem de cobrança endereçada pelo usuário a WhatsApp/navegador; expectativa e destino variam pelo fluxo. | Exclusão local segundo a regra de parcela e pagamentos vinculados; cópias/exportações externas permanecem sob controle dos destinos escolhidos. | `LocalDatabase.kt`, funções de exportação/PDF/WhatsApp; alta sobre persistência e operações codificadas, limitada a payload de SDK/destino externo. |
| Fotografias anexadas às vendas | BLOB em `FOTOS_VENDA` no banco local; visualização/PDF e recuperação | Nenhum upload próprio identificado. Podem ser incluídas no backup completo e PDF se o usuário exportar/compartilhar. Seleção da origem usa contrato Android, iniciada pelo usuário. | Excluir foto/venda conforme vínculos; arquivo original da galeria não é apagado ao remover anexo. Backup e cópias de PDF não são removidos automaticamente pelo app. | `LocalDatabase.kt`, `MainActivity.kt`, política; alta para código atual. Não inspecionamos mídia pessoal. |
| Identificação do negócio e convite inicial | `SharedPreferences` privado `business_identity`; cabeçalho, mensagens, PDFs; nome pode também ir no backup completo | Nenhum envio próprio automático identificado. Nome integra mensagens/documentos/backup acionados pelo usuário. | Editável e limpável no app; restauração preserva/configura o valor conforme metadata. Backup e cópias externas independentes. | `BusinessPreferences.kt`, `BackupManager.kt`; alta para código atual. |
| Licença, entitlement, produto e compra | Estado atual em memória; BillingClient consulta Google Play. Token recebido no objeto `Purchase` é usado no callback de `acknowledgePurchase`, sem persistência própria encontrada. | Comunicação de produto/compra/restauração com Google Play por Billing. A conexão e consultas começam automaticamente ao iniciar a Activity; não dependem de concluir uma nova compra. Uma transação real depende de ação de compra. Telemetria potencial do SDK segue pendente. | Estado FULL_ACCESS não é gravado no backup SQLite. Recuperação usa consulta à Play; token/licença não é apagado por restore de dados. Registro e retenção dentro do Google Play não foram auditados. | `PlayBillingGateway.kt`, `MainActivity.onCreate`, `BackupManager.kt`, Billing 9.1.0; alta para chamadas do app, baixa/inconclusiva para payload/retention do fornecedor. |
| Dados técnicos/diagnósticos de SDK e sistema | Não há Analytics/Crashlytics próprio identificado. Logs próprios observados: estado de entitlement (`Log.d`) e aviso genérico de notificação; nenhum dado de cliente/venda aparece nesses textos. | A investigação de bytecode relata caminho Billing → Data Transport/CCT com metadados técnicos possíveis (modelo, fabricante, versões/build, locale/região, rede/operadora e fingerprint de build). A conexão Billing é iniciada na abertura do app. Se/quanto eventos são transmitidos em cada cenário, endpoint final, campos efetivos, destinatário jurídico, finalidade, associação e retenção não foram capturados. | Buffer/serviço de SDK sob política Google; não há mecanismo no app para pedido/exclusão individual de telemetria. Retenção não confirmada. | `docs/SEGURANCA_DADOS_PLAY.md`, grafo resolvido e inspeção estática CCT/Billing reportados na Fase 2; evidência de mecanismo, não tráfego real nem payload fechado. |
| CSV/PDF/PNG temporários | Arquivos em `cacheDir`, para exibir chooser e permitir leitura compartilhada | `ACTION_SEND` com `FileProvider` não exportado e permissão URI temporária; o usuário escolhe app/destino no chooser. Isso pode efetivamente passar os dados ao app escolhido. | Cache privado pode ser removido pelo sistema; sem prazo garantido. Destinatário pode manter cópia. | `MainActivity.kt`, `file_paths.xml`; alta para caminhos e Intents do código. Aplicação da exceção Play depende do fluxo e expectativa. |
| Backup completo SQLite | Cópia temporária no cache; fluxo interno pode criar cópia em `filesDir`; exportação usa destino escolhido por SAF. Contém dados, fotos e nome; não inclui entitlement. | Criado/compartilhado após ação explícita. SAF pode apontar para armazenamento local ou provedor remoto escolhido, inclusive app de nuvem. Sem servidor próprio. Arquivo SQLite sem criptografia/senha própria. | Temporário removido em `finally`; cópia `filesDir` não tem limpeza temporal garantida; destino externo é independente. Usuário pode remover dados/cópias nos respectivos locais. | `BackupManager.kt`, `MainActivity.kt`, `SEGURANCA_PRIVACIDADE.md`, política local; alta sobre implementação atual. |
| Texto/valores enviados à cobrança por WhatsApp | Construído em memória com cliente/venda, nome de negócio, data e valores; URI `wa.me` | Lançamento intencional pelo usuário para app/navegador externo; texto preenchido não implica envio: usuário confirma no destino. O serviço externo trata o conteúdo após abertura. | Sem armazenamento remoto pelo app identificado; histórico/cache do destino sob política própria. | `MainActivity.kt` (URI `https://wa.me/?text=`), política local e relato manual sem cobrança real; alta sobre o fluxo pretendido, destino exato depende do Android. |
| Avisos locais de vencimento | Notificação local com informação da parcela, criada no app e entregue pelo serviço de notificações Android após permissão | Não foi encontrado agendamento em backend nem envio próprio. Notificação é exibida pelo sistema no mesmo aparelho. Permissão `POST_NOTIFICATIONS`; registro não equivale a transmissão remota pelo app. | Pode ser substituída/removida pelo sistema/usuário; dados de origem permanecem na base até exclusão. | `MainActivity.kt`, manifesto; alta para chamadas observadas. Diagnósticos próprios sem telemetria de desempenho não identificados. |

## 5. Billing 9.1.0 — conclusão delimitada

### O que o código confirma

- `app/build.gradle` declara `com.android.billingclient:billing:9.1.0`; o manifesto mesclado de release declara `com.android.vending.BILLING` e `INTERNET` (a permissão de Internet vem de dependência mesclada, não consta no manifesto-fonte próprio).
- `MainActivity.onCreate` cria o `PlayBillingGateway` e chama `connect()`; o setup bem-sucedido consulta `ProductDetails` de `full_access` e chama `queryPurchasesAsync(INAPP)`.
- Na compra, `launchBillingFlow` usa a oferta de produto do Google Play. A consulta de compras também permite recuperação de compra existente. O callback trata cancelamento, pendência, erro e `ITEM_ALREADY_OWNED`; compra `PURCHASED` libera o entitlement e, se não reconhecida, chama `acknowledgePurchase` com `purchaseToken`.
- O produto é compra única `INAPP`, `full_access`; não há serviço próprio ou backend de verificação. O token não foi encontrado salvo em `SharedPreferences`, banco ou backup; é passado ao método de reconhecimento em memória. Não há cartão nem credenciais de pagamento tratados diretamente pelo código próprio.
- A política offline/local diz que terceiros, incluindo Google Play/dependências, podem processar dados técnicos para distribuição, segurança e cobrança; relata mecanismo de eventos Billing/CCT como possível e qualifica campos como “quando disponíveis”.

### O que permanece incerto

A investigação estática anterior identificou `Transport.send`/Data Transport/CCT no caminho resolvido do SDK Billing e metadados técnicos construídos pelo backend CCT. Isso é evidência de mecanismo e campos que podem acompanhar eventos. **Não comprova que cada evento é emitido, que todos os campos chegam a ser transmitidos neste app, nem inclui payload completo, frequência, endpoint efetivo, retenção, associação, finalidade específica, se alguma parte é efêmera ou se é compartilhamento a terceiro versus processamento do serviço.** A documentação geral oficial da integração confirma conexão, consultas e reconhecimento com Play, mas não substitui uma ficha de Segurança dos dados específica do Billing 9.1.0; as notas 9.1.0 publicam alterações de API, não um inventário de telemetria.

Logo, não afirmar “Billing não coleta”; também não declarar categorias/compartilhamentos específicos como comprovados somente a partir dos campos CCT estáticos. Manter **PENDENTE DE CONFIRMAÇÃO** para Diagnostics/App info & performance e Device or other IDs até documentação do fornecedor, resposta técnica oficial ou observação controlada do tráfego com ambiente de teste. Não enviar token, dados comerciais ou identificadores reais a ferramenta de captura. Uma captura não pode, isoladamente, definir finalidade jurídica/retention.

### Exceção de pagamento

A FAQ oficial permite não declarar dados de cartão coletados pelo processador se (1) o app nunca os acessa e (2) o processador coleta diretamente do usuário sob seus termos. O código inspecionado não lê cartão, endereço de cobrança ou credenciais. A exceção não deve ser estendida automaticamente ao histórico de compras, estado de licença, telemetria diagnóstica, dados técnicos, nem aos registros comerciais mantidos no app; a própria FAQ orienta avaliar purchase history e dados recebidos para risco/antifraude. Histórico e transações da Play devem ser avaliados separadamente no formulário. Fontes: [FAQ oficial de pagamento em Segurança dos dados](https://support.google.com/googleplay/android-developer/answer/10787469?hl=pt-BR) e [integração Billing](https://developer.android.com/google/play/billing/integrate).

## 6. Matriz de Segurança dos dados — recomendação para revisão, não submissão

A classificação oficial vigente deve ser conferida no formulário real. A Play define coleta como transmissão off-device e inclui código de terceiros/SDKs; declarações são globais para o conjunto de versões distribuídas por package name, não apenas o build local. Categorias abaixo refletem evidência da revisão local e identificam as lacunas.

| Categoria | Coletado? | Compartilhado? | Finalidade possível | Obrigatório ou opcional | Evidência | Confiança / decisão sugerida |
|---|---|---|---|---|---|---|
| **Informações pessoais** — nomes de clientes e conteúdo cadastral | Pelo código próprio, não quando só gravado/processado localmente. Dados podem sair no arquivo/texto exportado pelo usuário. SDK: vínculo com pessoa não demonstrado. | Possível para app/provedor escolhido pelo usuário; avaliar exceção de ação específica e expectativa. Não há backend próprio observado. | Funcionalidade: cadastro, histórico e cobrança. | Usuário escolhe cadastrar; certos campos podem ser necessários para a ação que ele pede. Não marcar “opcional” globalmente sem confrontar versões ativas e cada campo. | `CLIENTES`, modelos, PDF/CSV/WhatsApp e SAF. | **Alta** para armazenamento local; **média/pendente** para exportação/Console e SDK. Não marcar coleta automática de nome por inferência. |
| **Informações financeiras** — registro de venda, pagamentos/parcelas e saldos; compra FULL_ACCESS da Play é separada | Registros financeiros comerciais locais; nenhum envio próprio automático encontrado. Dados exportados podem ser transmitidos pelo destino selecionado. Purchase history/licença pelo Billing precisa avaliação independente. Cartão processado pela Play é caso distinto. | Conteúdo de relatório/backup pode ser compartilhado sob ação do usuário; Billing comunica com Google para cobrança/estado da compra. Não equiparar vendas dos clientes do comerciante a dados de pagamento do cartão do titular. | Funcionalidade local/relatórios; compra/licenciamento para transação Play. | Dados comerciais são inseridos conforme uso; consulta de Billing inicia automaticamente; compra em si exige ação. Atributos de coleta dependem do payload/finalidade confirmados. | `VENDAS`, `PAGAMENTOS`, `PlayBillingGateway`, exportadores e FAQ oficial. | **Alta** para armazenamento local/caminho de compra; **pendente** taxonomia Play para purchase history e payload/Console. Exceção de cartão estreita. |
| **Fotos e vídeos** — fotos de venda | Não pelo código próprio quando armazenadas localmente; leitura/seleção é mediada por ação do usuário. | Backup/PDF escolhidos podem conter fotos e passar a outro app/provedor. Aplicar exceção somente se critérios oficiais do fluxo forem satisfeitos. | Função de associar imagem à venda; backup/relatório solicitado. | Opcional; foto não é necessária para usar venda sem imagem. | `FOTOS_VENDA`, seleção por contrato, PDF e backup. | **Alta** para local/opcional; **média** para compartilhamento por exportação, verificar Console e versões antigas. |
| **Arquivos e documentos** — CSV, PDF, backup | Arquivos temporários locais não são coleta. Ao usar provedor remoto escolhido pelo usuário, conteúdo sai do dispositivo. | Transferência escolhida via SAF/chooser; possível exceção de ação específica, não presumir para todo destino. | Exportar relatório ou recuperar cópia. | Opcional; ação deliberada. | `cacheDir`, `filesDir`, SAF `CreateDocument`/`OpenDocument`, `ACTION_SEND`, `FileProvider`. | **Alta** para os fluxos; classificação/formulário deve refletir destinos e versões ativas, ainda sem Console. |
| **Identificadores do dispositivo** | **PENDENTE DE CONFIRMAÇÃO** para os eventos Billing/CCT: campos de build/aparelho vistos estaticamente, mas não foi confirmado ID persistente/identificador individual enviado. | Destino/compartilhamento dependem do payload real e papel do receptor; não resolvido. | Possivelmente diagnóstico/funcionalidade Billing, ainda sem finalidade completa confirmada. | Caminho Billing começa no startup; obrigatoriedade da transmissão não estabelecida. | `SEGURANCA_DADOS_PLAY.md`; CCT adiciona modelo/fabricante/fingerprint de build e metadados de rede/localidade possíveis; não se provou Advertising ID/Android ID/IMEI/IMSI. | **Baixa/inconclusiva**. Não marcar Device or other IDs só por `Build.FINGERPRINT`, modelo ou fabricante; obter confirmação do fornecedor/tráfego. |
| **Informações e desempenho do app** — diagnósticos | **PENDENTE DE CONFIRMAÇÃO**, com evidência estática de mecanismo de eventos técnicos/diagnóstico no Billing. `INTERNET` mesclada habilita rede, mas permissão por si só não prova coleta. | Google Play/serviço CCT potencial; classificar compartilhamento somente após conhecer o serviço e papel contratual. | Diagnóstico/operação de Billing é candidato; não o chamar de analytics/marketing sem prova. | A conexão/consultas Billing iniciam em `onCreate`; possível automação pelo SDK. Se a transmissão ocorrer automaticamente, avaliar “required” de acordo com definição global Play. | `PlayBillingGateway`, manifesto mesclado, grafo de dependências e bytecode descritos na auditoria. Não houve captura de rede. | **Média para mecanismo; baixa para os eventos/campos efetivos.** Resolver antes de submeter formulário atualizado. |
| **Atividade no aplicativo** — taps, pesquisa, navegação, conteúdo gerado | Nenhum tracking de page views, pesquisas ou taps próprios foi encontrado. Billing pode registrar eventos de operação, mas conteúdo/semântica ainda não conhecidos. | Não comprovado. Dados de atividade de SDK ficam pendentes apenas se payload mapeado para essa categoria. | Funcionalidade local; finalidade de logs Billing pendente. | Uso do app é natural; qualquer coleta SDK e opcionalidade não determinadas. | Buscas no código não encontraram Analytics/event logger próprio; `Log.d` registra apenas estado entitlement local. | **Média para ausência de tracking próprio nesta revisão; pendente para SDK e versões da Play.** Não declarar “nenhuma atividade” até fechar SDK. |

**Não usar a tabela como valores finais para o formulário.** O desenvolvedor precisa confirmar as categorias de todas as versões ainda distribuídas, as respostas já salvas, provedores de compartilhamento e os campos/propósitos do Billing. A Google Play informa que a declaração é responsabilidade do desenvolvedor e deve ser atualizada quando as práticas mudarem.

## 7. Política de privacidade — consistência e pendências

| Tema | Código/política local | Situação e recomendação |
|---|---|---|
| Dados locais e offline-first | SQLite privado para registros/fotos; `SharedPreferences` para nome; app sem conta/back-end próprio observado. | Coerente. “Offline-first” não significa app sem rede: Billing exige comunicação; o manifesto mesclado declara `INTERNET`. |
| Compartilhamento WhatsApp | Texto contém informações da cobrança/nome do negócio e é entregue por link externo; enviar depende de confirmação do usuário. | Coerente com a política local. Manter clara a diferença entre abrir/preencher e enviar. Usuário relatou uso sem cobrança real. |
| CSV/PDF/backup | Gerados localmente e entregues por chooser/SAF; destino escolhido pode ser local/remoto. | Política local esclarece que cópias ficam sob controle do destino. Revisar exceção de ação do usuário por fluxo concreto. |
| Backup e retenção | SQLite sem criptografia/senha própria; cache e cópia interna podem permanecer sem expiração garantida; `allowBackup=false`, sujeito aos limites de migração Android/fabricante. | Política local descreve esses limites. Não afirmar backup automático impossível em todos os fabricantes ou transferência de dispositivo universalmente bloqueada. |
| Google Play Billing | Política local reconhece tratamento por Google/dependências e fala de mecanismo/eventos possíveis e campos técnicos possíveis. | Mais cautelosa que alegar ausência total de coleta, mas ainda precisa alinhamento com os dados reais declarados e eventual esclarecimento de categorias/destinos depois da auditoria de SDK. |
| Exclusão e retenção | Exclusão local de venda/pagamentos/fotos conforme vínculo; Android limpa dados próprios; cópias externas seguem no provedor. Política traz contato `vendassimples.app@gmail.com`. | Email foi confirmado ativo e monitorado pelo usuário em conversa anterior. Confirmar processo e apresentar esse canal na Play se a declaração de exclusão depender dele; não prometer exclusão de dados retidos por Google/terceiros sem política deles. |
| Página pública | Pedido da Fase 6C informa que a revisão 15/10 está publicada e exibição confirmada. A revisão local em `site/privacy/index.html` e asset offline é de 15/10 e `PrivacyPolicyTest` exige igualdade do conteúdo principal. | A tentativa de abertura na ferramenta web nesta execução falhou com erro interno. Relatórios anteriores documentaram conteúdo remoto antigo em 15/10, antes da publicação informada agora. Registrar a nova informação como fornecida pelo usuário; obter evidência atual (HTTP 200, data, texto/hash/captura sanitizada) e comparar antes de fechar a conformidade. A URL consta em `PrivacyPolicy.officialUrl`. |

O teste `offlinePolicyMatchesLocalSiteRevisionAndContainsDeletionAndContact` verifica igualdade do trecho principal de `site/privacy/index.html` com o asset incluído e presença de exclusão, ausência de criptografia própria e contato. Isso é validação local automatizada; não prova estado remoto atual, fluxo `ACTION_VIEW` físico, conteúdo do Console ou práticas de todos os SDKs.

## 8. Guia prático de revisão manual da Play Console

O Console não foi acessado nesta fase. O responsável deve revisar sem salvar/submeter qualquer alteração até comparar evidências.

1. Abrir **Google Play Console → Vendas Simples → Política → Conteúdo do app → Segurança dos dados** (os nomes dos menus podem mudar; usar a página App content/Conteúdo do app e o cartão Data safety/Segurança dos dados).
2. Registrar o status do formulário: rascunho/enviado/aprovado/requer ação e data da última submissão. Não presumir respostas atuais.
3. Abrir a visão geral do formulário, “Data collection and security/Coleta e segurança” e “Data types/Tipos de dados”; revisar todas as categorias, se cada tipo é coletado/compartilhado, finalidade, obrigatório/opcional e medidas de segurança.
4. Revisar respostas de criptografia em trânsito e mecanismo para solicitar exclusão. Só afirmar criptografia de todos os dados se todos os dados de usuário transmitidos por app e SDKs forem cobertos pelo mesmo critério oficial; não usar HTTPS de um único caminho como prova global.
5. Conferir que a política vinculada no Console é exatamente `https://rg-carraro.github.io/vendas-simples-android/privacy/`; abrir em navegador e conferir data 15/10/2026, seções de Billing, exportação, backup/criptografia, exclusão/retenção e contato. A URL configurada não comprova o corpo servido.
6. Abrir release overview/Tracks e listar versões `com.vendassimples.app` ativas em produção, teste aberto, fechado/Alpha e regiões. Obter `versionCode`, bundle enviado e informações de SDK/dependências. A declaração da Play deve cobrir a soma de práticas das versões ainda distribuídas; a página oficial dispensa somente apps exclusivamente em teste interno da inclusão.
7. Comparar a matriz desta fase e o inventário SDK com cada resposta já salva. Investigar compra/histórico de compra, diagnósticos Billing/CCT, destino e expectativa de arquivos exportados, opcionalidade e exclusão. Não aplicar a exceção de cartão a essas outras categorias.
8. Revisar discrepâncias antes de alterar. Guardar rascunho/evidência local; só salvar/submeter após decisão do responsável. Nenhuma resposta foi modificada por este trabalho.

### O que pode ser sustentado pelas evidências locais

- Armazenamento local de clientes/vendas/parcelas/pagamentos/fotos/nome, sem backend próprio observado nesta revisão.
- Ações de compartilhamento/exportação e os destinos do Android dependem de seleção/ação do usuário; o conteúdo pode incluir dados comerciais e fotos.
- Google Play Billing 9.1.0 é usado para produto, compras e reconhecimento; cartão não é acessado pelo código do app revisto.
- Existem mecanismo/possibilidade de dados técnicos Billing/CCT com lacunas sobre eventos/payload efetivos.
- `allowBackup=false`, cópia offline da política, sem criptografia própria de backup, limites de cópias/cache e ausência de upload comercial próprio são sustentados pela revisão atual.

### O que exige confirmação de Console ou fornecedor

- Respostas atuais e status do formulário; categorias da publicação ativa; canais/fins/opcionalidade já declarados.
- Lista real de faixas e versões ativas e qual artefato está na Alpha; AAB local não comprova o arquivo submetido.
- Classificação final de Billing diagnostics, identificadores, purchase history, destino/papel do serviço, finalidade, requisito/opt-in, criptografia e retenção.
- Se todas as transmissões declaradas usam criptografia em trânsito e se canal/contato de exclusão cobre de fato pedidos relevantes.
- Página pública atual e igualdade do conteúdo servido com o asset local (o usuário informou a confirmação; captura web Codex falhou nesta execução).

### Evidência a solicitar ao responsável

- Capturas da página Segurança dos dados: status geral; lista de tipos marcados; detalhe por tipo com coleta/compartilhamento/finalidade/obrigatoriedade; criptografia; exclusão. Recortar e ocultar e-mail pessoal, ID de conta de desenvolvedor, pedidos/compra, dados de pagamento e quaisquer dados de clientes.
- Captura/registro do release overview com faixas e versões/códigos ativos, sem dados de conta. Se necessário, fornecer somente lista de track + versionCode/versionName + nome do bundle.
- Para a política, URL aberta, data da política e status HTTP/headers ou hash do HTML, sem cookies, tokens, parâmetros privados ou dados da conta.
- Resposta oficial do provedor Billing/SDK ou documentação que discrimine tipos de dados, finalidade, destinatários, transmissão automática/opcional e retention. Não compartilhar tokens de compra nem captura contendo identificadores reais.

## 9. Conformidade esperada para V12

O diff analisado das Fases 2/4 mantém os requisitos de produto: offline-first para a operação comercial, sem login/back-end de vendas; FREE até 30 vendas, clientes ilimitados e CSV disponível; FULL_ACCESS compra única `full_access`; backup/restauração restritos a FULL_ACCESS; dados locais financeiros/fotos e preferência de negócio; cópia gratuita/offline da política; Billing Library 9.1.0. A V12 não deve incluir licença em backup nem apagar dados em falha de Billing/rede. Não foram identificadas novas APIs de coleta própria ou permissão de localização, contatos, mídia abrangente ou câmera no manifesto-fonte.

O manifesto mesclado atual inclui `INTERNET`, `com.android.vending.BILLING`, `POST_NOTIFICATIONS`, permissão interna de receiver e `ACCESS_NETWORK_STATE`. `INTERNET` está no manifesto mesclado por dependência; permission list não determina se há coleta, mas aponta a rede que o exame de SDK deve cobrir. As notificações são lembretes locais e pedem `POST_NOTIFICATIONS` em contexto; revisar o fluxo e a finalidade, mas não marcar por permissão sozinha.

### Bloqueadores antes de candidatar/distribuir a V12

1. Conferir respostas existentes do Console, versões/faixas ativas e bundle Alpha real; atualizar/formalizar a matriz com o proprietário do app. Sem essa evidência, não há base para declarar Segurança dos dados completa.
2. Fechar a análise de Billing 9.1.0 (documentação oficial do fornecedor ou evidência técnica controlada) para os campos e classificações candidatas, ou obter confirmação oficial suficiente para registrar explicitamente os limites.
3. Reconciliar a divergência documental sobre política pública: a confirmação mais recente do usuário diz publicada/visível; esta sessão não pôde abrir a URL e os registros anteriores dizem que estava desatualizada antes da publicação. Guardar prova pós-publicação; confirmar paridade com asset.
4. Executar runtime na API mínima 24 e a regressão V12; os testes da V11 e os 13 testes relatados não cobrem a nova versão nem sua migração.
5. Obter V12 assinada/distribuída pela Play em faixa autorizada e validar update V11 → V12 com dados fictícios e FULL_ACCESS controlado; não instalar APK debug sobre a V11 Play.

## 10. Plano de preparação do AAB V12 — não executado

1. Consolidar somente alterações revisadas/aceitas sem descartar arquivos locais; conferir diff integral e arquivos não rastreados.
2. Resolver política e declaração de Segurança dos dados com evidências e aprovação do responsável; não editar a Console por esta fase.
3. Executar suíte unitária, instrumentada, lint e builds debug/release previstos; separar avisos/falhas e retestar em API 24 e Android 16/API 36.
4. Conferir `applicationId`, min/target/compile SDK, permissões e manifesto mesclado, Billing `9.1.0`, SKU/offer `full_access`/`full-access`, políticas offline, backup, CSV/PDF e regras FREE/FULL.
5. Só após incremento autorizado, preparar `versionCode 12` / `versionName 1.0.12`; gerar AAB assinado pelo mecanismo local configurado sem exibir credenciais.
6. Registrar SHA-256, validação do bundle, certificado/esquema de assinatura e manifesto; comparar identidade e compatibilidade com a V11 distribuída. O certificado/hash local, sozinho, não prova identidade do upload na Console.
7. Upload para Alpha, publicação e qualquer envio à análise só depois de autorização explícita separada.
8. Atualizar o mesmo aparelho/faixa pela Google Play; comparar contagens sintéticas, parcelas, pagamentos, fotos, nome e licença antes/depois. Interromper e preservar dados em divergência.

Nenhum passo deste plano foi iniciado. Não houve build AAB, alteração de versão, assinatura, upload ou publicação.

## 11. Pendências e conclusão

| Item | Estado |
|---|---|
| Manual V11 — 13 cenários | **APROVADO segundo relato do usuário**; não executado pelo Codex |
| Inventário de código local | **Revisado**, limitado à árvore/artefatos locais disponíveis |
| Taxonomia Google Play e exceções | **Fonte oficial atual consultada**; aplicação ao Console da V11 pendente |
| Billing 9.1.0 — comportamento funcional do código | **Confirmado estaticamente** |
| Billing 9.1.0 — payload real, categorias, destinos, finalidade e retention | **PENDENTE DE CONFIRMAÇÃO** |
| Respostas atuais/versões ativas no Console | **PENDENTE; Console não acessado** |
| Política pública | Usuário informou publicação/confirmação; abertura direta Codex falhou; paridade online pendente de evidência independente |
| API 24 e runtime V12 | **PENDENTE** |
| Upgrade V11 → V12 e Billing Play de teste | **PENDENTE; sem V12** |
| Commit, push, tag, AAB ou publicação | **Não realizados** |

**Classificação: APTO COM RESSALVAS para preparar a V12; NÃO apto a declarar conformidade completa, submeter alteração de Segurança dos dados ou publicar V12 com base somente nesta análise.** A aprovação manual relatada da V11 é evidência positiva, mas não substitui a revisão da Console, a confirmação do SDK e a regressão da V12.

## 12. Estado final do Git

`HEAD`, branch e tag de referência não mudaram. As alterações locais anteriores foram preservadas. Novo arquivo: `docs/FASE6C_SEGURANCA_DADOS_PLAY.md`; alteração documental autorizada: atualização de `docs/FASE6B1_HOMOLOGACAO_FISICA_V11.md` com a evidência relatada pelo usuário. Sem staging, commit, push, tag nova, alteração de código de produção, build AAB, mudança na Play Console, publicação do site ou release Android.
