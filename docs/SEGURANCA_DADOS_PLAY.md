# Segurança dos dados — investigação da Fase 2

Data: 15/10/2026. Código-base: `8aa34881755f222f9b090691aaa6127b31b1a363`, V11. Trabalho em `fix/v12-seguranca-conformidade`, ainda com 11 / 1.0.11 para desenvolvimento. Não representa declaração enviada à Play. Não houve acesso ao Console nem captura de tráfego.

## Inventário observado

Diretas: Core-KTX 1.13.1 declarado (Core/Core-KTX 1.15.0 resolvidos), AppCompat 1.7.0, Material 1.12.0 e Google Play Billing 9.1.0. JUnit 4.13.2 apenas em testes.

Transitivas relevantes no modelo release/manifesto mesclado: Data Transport API 3.0.0, runtime/backend CCT 3.1.8; Play services base 18.5.0, basement 18.9.0, tasks 18.2.0, location 19.0.0, places-placereport 17.0.0; Firebase encoders JSON 18.0.0, proto 16.0.0 e encoders 17.0.0; Activity 1.8.0, Lifecycle 2.6.2, Startup 1.1.1, ProfileInstaller 1.3.1, Kotlin 1.9.24 e coroutines 1.6.4. Ferramentas: AGP 8.5.2, Gradle 8.7.

Não foi encontrada integração própria com anúncios, Analytics, Crashlytics ou backend. Encoders Firebase não equivalem à integração desses serviços. A presença de location não comprova acesso à localização: não há permissão de localização nem chamada própria correspondente.

Evidências reproduzíveis: `app/build.gradle`; POM `billing-9.1.0.pom` no cache Maven; `app/build/intermediates/lint_report_lint_model/release/generateReleaseLintReportModel/release-artifact-dependencies.xml`; `app/build/outputs/logs/manifest-merger-release-report.txt`. Os arquivos gerados são evidência local, não arquivos a versionar.

## Tratamento identificado

| Informação | Evidência | Confiança / limite |
| --- | --- | --- |
| Clientes, descrição, vendas, parcelas, pagamentos, fotos e nome do negócio | LocalDatabase, BusinessPreferences | Confirmado: armazenamento local privado, sem upload próprio |
| Documentos e texto de cobrança | ACTION_SEND, SAF e ACTION_VIEW wa.me | Confirmado: entrega solicitada pelo usuário a outro app/provedor; confirmar expectativa do usuário no formulário |
| Produto, token e estado da compra | PlayBillingGateway usa full_access, queryPurchases e acknowledgePurchase | Confirmado: comunicação funcional com Google Play; token não é persistido/exportado pelo código próprio |
| Dados do cartão e endereço de faturamento | Não recebidos pelo código próprio; compra na interface Play | Não declarar automaticamente como dados coletados pelo app; conferir condições da exceção oficial de pagamentos |
| Eventos de operação/diagnóstico do Billing | Bytecode BillingClientImpl -> zzdr -> zzdt -> Transport.send, origem PLAY_BILLING_LIBRARY | Confirmado estaticamente: implementação instanciada; emissão efetiva em cada cenário não capturada |
| Pacote do app e API Android no contexto de diagnóstico | BillingClientImpl preenche zzke com getPackageName e Build.VERSION.SDK_INT, usado pelo logger | Confirmado estaticamente; não é uma enumeração completa dos payloads |
| Identificadores pessoais/dispositivo, histórico, IP e retenção | Payload completo e prática operacional do fornecedor não determinados | Não confirmado; não inferir Advertising ID, localização ou períodos de retenção |
| Criptografia do CCT | CCTDestination do backend 3.1.8 define endpoint padrão HTTPS | Confirmado para configuração estática padrão; não comprova todo tráfego, redirects ou destinos escolhidos pelo usuário |

O cliente Billing é inicializado no onCreate, consulta produto/compras e configura o logger antes de uma compra. Portanto, o caminho técnico não depende de uma transação concluída. A biblioteca não foi removida nem reconfigurada. Não há evidência de que os eventos técnicos incluam o banco comercial.

## Metadados adicionais do transporte CCT

O bytecode do backend CCT 3.1.8 confirma que decorate(EventInternal) adiciona sdk-version, model, hardware, device, product, os-uild (Build.ID), manufacturer, fingerprint (Build.FINGERPRINT), tz-offset, net-type, mobile-subtype, country/locale de Locale.getDefault, mcc_mnc de TelephonyManager.getSimOperator e application_build. O caminho send monta AndroidClientInfo e NetworkConnectionInfo com esses campos. Evidência local filtrada: .gradle-user/v12-cct-metadata-evidence.txt; não contém valores de um usuário, somente instruções do SDK.

Isso confirma potencial de transmissão de informações técnicas do aparelho, build, configuração e conexão junto aos eventos do Billing que usam esse transporte. Não é captura de tráfego: não comprova emissão de cada campo em cada cenário, vínculo com pessoa ou retenção. Build.FINGERPRINT identifica uma build do sistema, não comprova ID único de aparelho. Country/locale vêm da configuração regional, não provam localização física. MCC/MNC identificam a operadora; não devem ser chamados de IMEI/IMSI nem de coordenadas. Não foi demonstrada leitura de Advertising ID, Android ID, IMEI ou localização pelo caminho inspecionado.

Para o formulário, diagnósticos/informações do app e desempenho são candidatos; confirmar com fornecedor/Console o enquadramento dos demais campos e eventuais identificadores presentes no payload protobuf do Billing. Não inferir categorias pessoais apenas pelos nomes device/fingerprint. A política local descreve as categorias técnicas observadas sem prometer retenção ou ausência absoluta de coleta.

## Proposta para o formulário

| Campo | Proposta | Classificação |
| --- | --- | --- |
| Criação de conta própria | Não permite | Confirmado pelo código |
| Login próprio obrigatório | Não | Confirmado pelo código |
| Envio de dados comerciais ao desenvolvedor | Não identificado | Confirmado pelo código próprio, sem garantia sobre todos os SDKs |
| Coleta global de dados | Não marcar automaticamente “nenhuma” | Não confirmado até concluir enquadramento dos diagnósticos |
| Informações do app/desempenho — diagnósticos | Categoria candidata para os eventos técnicos | Provável, mas exige validação do fornecedor/payload |
| Identificadores e histórico de compras | Conferir os dados efetivamente transmitidos/acessados e as exceções | Não confirmado integralmente |
| Finalidades | Funcionalidade/cobrança e diagnóstico técnico são evidenciados; analytics/fraude não devem ser presumidos | Confirmado parcialmente / exige validação |
| Compartilhamento voluntário | Há entrega de arquivos e texto a terceiros | Confirmado pelo código; avaliar exceção de ação específica esperada |
| Obrigatoriedade/opcionalidade | Recursos locais independem de compra; consultas Billing iniciam ao abrir o app | Confirmado no código; enquadramento das categorias exige revisão |
| Dados criptografados em trânsito | HTTPS presente em configurações conhecidas | Não confirmar “todos” sem verificar SDK e destinos pertinentes |
| Exclusão | Parcela/pagamentos/fotos vinculadas e limpeza Android; cliente e cópias externas têm limites descritos na política | Confirmado pelo código |

O processamento apenas local não constitui, por si, coleta na definição do formulário. A exceção de pagamentos não isenta automaticamente diagnósticos ou todo dado recebido do serviço. A exceção de compartilhamento iniciado pelo usuário depende de ação específica e expectativa razoável. Revisar também outras versões distribuídas antes de enviar respostas globais.

## Fontes oficiais consultadas

- [Google Play — Segurança dos dados](https://support.google.com/googleplay/android-developer/answer/10787469?hl=pt-BR): definições de coleta, compartilhamento e exceções. A análise não substitui conferir cada categoria efetivamente utilizada.
- [Política de Dados do Usuário](https://support.google.com/googleplay/android-developer/answer/10144311?hl=pt-BR): transparência, política no app e responsabilidade por SDKs.
- [Notas do Billing](https://developer.android.com/google/play/billing/release-notes): versão 9.1.0 publicada em 18/06/2026. Não contém inventário completo de dados/retensão específico dessa versão.
- [Integração Billing](https://developer.android.com/google/play/billing/integrate): consultas, estado/token e reconhecimento. Nenhuma mudança de monetização foi feita nesta fase.
- [Disclosure de Play services](https://developers.google.com/android/guides/play-data-disclosure): informa ausência de coleta nos SDKs core listados; não estender essa declaração ao Billing ou à versão antiga de qualquer outra biblioteca.
- [Backup Android](https://developer.android.com/identity/data/autobackup): limites de allowBackup e migração D2D conforme fabricante.

Não foi localizada orientação oficial exaustiva de coleta/retensão exclusiva do Billing 9.1.0. Também não houve validação das declarações atuais do Console, pois não foram disponibilizadas. R03 permanece uma pendência de confirmação, não uma declaração de conformidade concluída.

## Confirmações exigidas antes da produção

1. Publicar a revisão do conteúdo da política após autorização. O usuário confirmou em 15/10/2026 a URL cadastrada no Console; a página pública responde 200, mas ainda apresenta o texto de 02/10/2026, anterior à revisão local da auditoria.
2. Dados, finalidades, destinatários, retenção e identificadores dos diagnósticos da versão efetivamente embarcada, com fornecedor e observação controlada quando viável.
3. Garantias aplicáveis de transporte e enquadramento em coleta/compartilhamento, sem extrapolar a exceção de pagamentos.
4. Categorias, exclusão, obrigatoriedade/opcionalidade e outras versões ativas no Console.
5. Comparação entre AAB Alpha e hash da V11 local; não houve evidência suficiente para afirmar equivalência.

Não enviar o formulário com afirmações absolutas de ausência de coleta ou criptografia integral com base apenas nesta investigação.

## URL pública confirmada

O usuário indicou a pasta `https://github.com/rg-carraro/vendas-simples-android/tree/master/site`. Em 15/10/2026, uma requisição HTTPS verificou status 200 e o título da política em `https://rg-carraro.github.io/vendas-simples-android/privacy/`. A API pública de Pages retornou 404; não foi usada como prova de inexistência. A URL do documento foi confirmada pelo conteúdo servido, não apenas pelo padrão do endereço.

Em 15/10/2026, o usuário confirmou expressamente que essa URL está cadastrada na Play Console. Essa informação vem do responsável; não houve acesso automatizado ao Console nem validação das demais declarações.

O app oferece a cópia local gratuita/offline e o botão Abrir site com essa URL. A nova consulta HTTPS respondeu 200 sem redirecionamento, mas o texto público ainda é de 02/10/2026. O corpo de site/privacy/index.html e o asset coincidem na revisão de 15/10/2026; faltam na página publicada os esclarecimentos de backup sem criptografia, migração Android, Billing/CCT, WhatsApp e exclusão/retenção. Evidência: .gradle-user/v12-policy-public-check/. Não houve push/deploy; a publicação da revisão segue pendente de autorização.
