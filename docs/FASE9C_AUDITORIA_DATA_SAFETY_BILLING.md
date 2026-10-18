# Fase 9C — Auditoria Data Safety e Google Play Billing 9.1.0

**Data:** 16/10/2026
**Base:** `master`, commit `11214de6aac8bf1072c1767344c725939d3b1802`
**Versão:** `com.vendassimples.app`, `versionCode 12`, `versionName 1.0.12`
**Escopo:** auditoria estática/documental. Sem alterações no app, AAB, formulário ou Play Console.

## 1. Conclusão executiva

**Classificação: DATA SAFETY PARCIALMENTE VALIDADA — CONFIRMAÇÕES PENDENTES.**

Com a evidência disponível, não é possível validar que a resposta global “Não coleta nem compartilha dados” esteja correta para todas as versões cobertas pela ficha. Também não é possível afirmar categoricamente que “Sim” e selecionar categorias específicas apenas pela presença de Billing/Data Transport: dependência instalada não demonstra, por si, que um dado de usuário foi transmitido em execução, e a taxonomia depende do payload e finalidade.

Há evidência do app iniciar conexão Billing e fazer consultas de produto/compras na inicialização, além de evidência estática anterior de mecanismo Billing → Data Transport/CCT capaz de emitir eventos com metadados técnicos. Não houve captura de rede ou documento oficial específico da Billing 9.1.0 que confirme exatamente quais tipos de dados chegam a ser transmitidos. Assim, **não salvar nem reenviar “Não” como resposta validada; manter o estado atual sem alteração** até resolver a lacuna. Isso não equivale a uma recomendação para selecionar “Sim” agora.

Dados comerciais processados somente no aparelho (clientes, vendas, pagamentos, fotos e nome do negócio) não são “coletados” para a Data Safety se não forem enviados para fora. Exportações, PDFs, backups e mensagens externas são iniciados explicitamente pelo usuário; a exceção de transferência iniciada pelo usuário e razoavelmente esperada pode dispensar declarar compartilhamento. É preciso confirmar que a ficha reflete todas as versões/faixas atualmente distribuídas, o que depende do Console.

## 2. Evidências e método

Foram lidos `AGENTS.md`, as skills de segurança, release e regressão, as Fases 6C, 7J, 9A e 9B, `docs/SEGURANCA_DADOS_PLAY.md`, `docs/MONETIZACAO.md`, `docs/SEGURANCA_PRIVACIDADE.md`, código e manifesto do master. Também foram consultados os documentos oficiais ligados ao final.

O projeto declara diretamente `com.android.billingclient:billing:9.1.0`. O grafo de release existente em `integration/v12-master-validation/app/build/intermediates/lint_report_lint_model/release/generateReleaseLintReportModel/release-artifact-dependencies.xml` (e, se ausente, `lint_vital_report_lint_model/.../release-artifact-dependencies.xml`) lista Billing 9.1.0, Data Transport API 3.0.0, transport runtime 3.1.8, backend CCT 3.1.8, encoders Firebase JSON 18.0.0/proto 16.0.0/encoders 17.0.0, Play services base 18.5.0, basement 18.9.0, tasks 18.2.0, location 19.0.0 e places-placereport 17.0.0, além de AndroidX. Esse relatório vem do worktree de integração; os arquivos de código/configuração do app comparados entre ele e master não apresentam diferenças. O grafo comprova resolução e empacotamento naquela variante, não atividade de rede.

Uma tentativa de reexecutar `:app:dependencies --configuration releaseRuntimeClasspath --offline` falhou antes de iniciar o Gradle porque esta sessão não tem `java` no PATH nem `JAVA_HOME`. Por isso, a lista acima foi lida do artefato de build já existente, não regenerada nesta fase. Nenhum AAB foi aberto, recompilado ou alterado.

## 3. Billing: chamadas, fluxo e limites da evidência

### Confirmado no código V12

`PlayBillingGateway.connect()` chama `BillingClient.startConnection()`. No sucesso chama `queryProduct()` (`queryProductDetailsAsync` para produto `full_access`, tipo `INAPP`) e `restorePurchases()` (`queryPurchasesAsync`). O fluxo é iniciado por `MainActivity` na inicialização. Ao comprar, o código chama `launchBillingFlow`; para compra `PURCHASED` não reconhecida, envia o `purchaseToken` a `acknowledgePurchase`. O token é usado em memória no callback; a inspeção anterior não encontrou persistência própria em banco/preferências/backup.

Estas chamadas comprovam o uso do serviço Play Billing e requisições relacionadas a catálogo, estado/recuperação de compra e reconhecimento. O app não constrói nem lê dados de cartão, credenciais de pagamento ou endereço de faturamento no código revisado. O serviço Google Play recebe solicitações do Billing para operar; os dados de conta e pagamento processados diretamente pelo Google Play não devem ser tratados como se o app os tivesse lido.

### Evidência estática de transporte adicional

`docs/SEGURANCA_DADOS_PLAY.md` e a Fase 6C registram inspeção anterior de bytecode do grafo resolvido: caminho do logger Billing para `Transport.send` e CCT; a configuração CCT inspecionada adiciona metadados como sdk-version, modelo, hardware, dispositivo/produto, fabricante, Build.ID/fingerprint, fuso, tipo de rede/subtipo móvel, locale/país, operadora MCC/MNC e application_build. Isso demonstra mecanismo/campos disponíveis para eventos técnicos, **não prova que cada um ou qualquer evento foi transmitido por esta versão em uso real**. Não houve captura de tráfego, inspeção de payload protobuf completo em execução ou mensuração de frequência/condições.

O backend CCT 3.1.8 foi documentado como configurado para endpoint HTTPS padrão; isso é evidência estática de uma rota CCT, não prova criptografia de todo tráfego Billing nem de todos os destinos externos do app. A permissão `INTERNET` aparece no manifesto mesclado; a permissão sozinha não demonstra coleta.

### Destinatário e finalidade

O fluxo de Billing comunica com Google Play para disponibilidade/preço do produto, consulta/recuperação e processamento/reconhecimento da compra. Isso é funcionalidade de cobrança/licença. O destino operacional adicional da rota CCT é serviço Google configurado pela biblioteca; a identidade contratual/papel (processador de serviço versus terceiro), retenção, finalidades exatas de cada campo e o payload efetivo não estão comprovados nesta auditoria. Não classificar esse tráfego automaticamente como Analytics, publicidade ou prevenção a fraude.

## 4. Matriz Data Safety

“Não comprovado” significa que faltam evidências para selecionar uma resposta positiva/negativa específica. A matriz descreve V12 localmente; a declaração global do Console deve ainda abranger versões distribuídas.

| Tipo/categoria Play candidata | Fonte / transferência | Coletado? | Compartilhado? | Finalidade; requisito/opcional; efêmero | Trânsito / exceção / confiança |
|---|---|---|---|---|---|
| **Personal info — Name / Phone / Other info** dos clientes | Formulários e SQLite local; dados podem aparecer em CSV/PDF/mensagem/backup sob ação do usuário. Nenhuma transmissão própria automática identificada. | **Não** para uso exclusivamente local. Fluxo exportado precisa avaliar exceção e destino específico. | **Não** a declarar para exportação se for ação específica iniciada pelo usuário e razoavelmente esperada; validar expectativa/fluxo. | Funcionalidade local; preenchimento manual opcional. Não se aplica efemeridade quando permanece local. | Destinos variam; criptografia do serviço externo não controlada. Confiança alta no caminho local/Intent, média na exceção por destino.
| **Financial info — Other financial info** (vendas, parcelas, pagamentos/saldos do negócio) | SQLite local; incluído em relatórios e backup explícitos. Não é dado de cartão do titular nem telemetria Billing. | **Não** enquanto exclusivamente local. | Mesma exceção possível para arquivo/relatório intencionalmente enviado; formulário deve refletir exceção Play e comportamento real. | Operação e relatórios locais; entrada é pelo usuário; backup/exportação opcional. | Backup SQLite sem criptografia própria; destino pode ser local/cloud, criptografia não garantida pelo app. Confiança alta.
| **Photos and videos — Photos** | Fotos anexadas são BLOB local; podem integrar PDF/backup que o usuário exporta. | **Não** para processamento só local. | Exceção de ação explícita pode aplicar a PDF/backup; depende do destino e expectativa. | Recurso local opcional; nada indica processamento efêmero quando armazenada. | Destino externo desconhecido; confiança alta no caminho local/exportador.
| **Files and docs — Files and docs** (backup, CSV/PDF) | Exportação SAF/ACTION_SEND, `FileProvider`; usuário escolhe o arquivo/destino. | **Não** para dados somente localmente processados; serviço receptor pode processar o arquivo conforme sua política. | A transferência iniciada/esperada pelo usuário pode ser exceção. Não generalizar para upload automático, inexistente na inspeção do app. | Funcionalidade; opcional por ação do usuário; retenção no provedor não conhecida. | App fornece URI temporária; destino e transporte após handoff não controlados. Confiança alta no caminho, média na aplicação da exceção.
| **Messages — Other in-app messages / Personal info** (cobrança WhatsApp) | App monta mensagem/URI `wa.me` com informações de cliente/venda e abre destino externo; o usuário precisa confirmar envio no WhatsApp/destino. | **Não comprovado** como transmissão pelo app: código abre intenção/link, mas não confirma envio final. O destino pode receber conteúdo após ação do usuário. | Exceção de ação específica e esperada é potencialmente aplicável; é importante não declarar como envio automático do app. | Cobrança solicitada pelo usuário; opcional. Não há retenção do app identificada após montar a URI; destino controla sua retenção. | TLS do destino pode depender do app/serviço; não afirmar trânsito integral do app. Confiança alta no fluxo até abertura, não no envio final.
| **Financial info — User payment info** (cartão, dados de pagamento) | Checkout/Google Play, fora dos campos lidos pelo Vendas Simples. | **Não pelo código próprio inspecionado**. Dados coletados diretamente pelo processador Play podem se enquadrar na exceção de serviço de pagamento se seus requisitos forem cumpridos. | **Não pelo app** com a mesma ressalva; não estender exceção a telemetria/histórico de compras. | Processamento de pagamento Play; dados e retenção do processador não controlados pelo app. | A orientação oficial permite exceção limitada a dados que o serviço de pagamento coleta para processar transação, quando o app nunca acessa e o processador coleta diretamente. Confiança alta para não acesso pelo código, Console/termos não auditados.
| **Financial info — Purchase history** (compra `full_access`) | App consulta compras e lê produto, estado e token devolvidos pelo Billing; token é enviado no `acknowledgePurchase`. Também há chamadas automáticas de consulta ao abrir. | **Não comprovado** como categoria a declarar: existe consulta e tratamento local do objeto Purchase, mas não foi demonstrado que o app transmita histórico para fora além do protocolo de serviço Play nem como Play enquadra os dados. | **Não comprovado** para eventos/telemetria. Google Play é serviço da compra; aplicabilidade da exceção do processador não foi confirmada para histórico/estado/token. | Funcionalidade de licença e processamento da compra; consultas de estado ocorrem automaticamente no início; compra exige ação. Efemeridade/retenção no Google não confirmadas. | Comunicação Billing é TLS do serviço oficial em uso esperado, mas não se audita protocolo/payload nesta execução. Não aplicar exceção de cartão sem confirmação. Confiança alta no acesso pelo app; baixa na declaração Data Safety.
| **App info and performance — Diagnostics** | Mecanismo Billing → Data Transport/CCT com eventos e metadados possíveis identificados estaticamente; sem captura. | **Não comprovado** como transmissão efetiva/categoria, mas é risco material a resolver. Se dados de diagnóstico/user data forem transmitidos pelo SDK, coleta deve ser declarada, inclusive se não chega ao desenvolvedor. | **Não comprovado**; CCT envia a endpoint Google, porém o papel/exceção de service provider para esta transmissão não está confirmado. | Operação/diagnóstico do Billing é finalidade plausível, não comprovada para cada evento. Startup conecta Billing automaticamente; obrigatoriedade depende de confirmar coleta e controle do usuário. Efemeridade desconhecida. | CCT configurado com endpoint padrão HTTPS segundo inventário anterior; não prova todo o tráfego. Confiança média para mecanismo, baixa para execução/payload/categoria.
| **Device or other IDs** | Campos CCT estáticos incluem metadados de build/aparelho/rede/região; nenhum Advertising ID, Android ID, IMEI ou IMSI foi comprovado nessa inspeção. | **Não comprovado**; não classificar `Build.FINGERPRINT`, modelo ou operadora como ID de usuário sem determinar vínculo/uso segundo taxonomia Play. | **Não comprovado** pelo mesmo motivo. | Possível contexto técnico; finalidade, requisito e associação desconhecidos. Efemeridade desconhecida. | TLS CCT estático não resolve tipo/retention. Confiança baixa. |
| **App interactions / Analytics / Crash logs / location** | Nenhuma integração própria Analytics/Crashlytics ou coleta própria de localização identificada. Há `POST_NOTIFICATIONS`; não é evidência de coleta remota. | **Não identificado** no código próprio; CCT eventual não deve ser rotulado analytics ou crash sem payload que sustente. | **Não identificado** independentemente; via CCT pendente. | Notificações locais e navegação são funcionalidades locais. | Não existe evidência positiva de Analytics, Crashlytics, Ads ou localização neste app. Confiança média/alta para integrações próprias ausentes, não conclusão sobre todo SDK.

## 5. Compartilhamentos iniciados pelo usuário

Google Play descreve transferência para outro app no dispositivo como “sharing”, mas lista exceção quando decorre de ação específica do usuário e é razoavelmente esperada (ex.: compartilhar documento). Vendas Simples usa `ACTION_SEND`, URI de `FileProvider`, SAF e link para WhatsApp; são caminhos acionados pela pessoa. Para a ficha, documentar exatamente esse comportamento e aplicar a exceção somente a esses fluxos. Não é evidência de que o app envie dados de vendas em segundo plano ou ao desenvolvedor. Não foi analisado o comportamento interno dos aplicativos de destino.

## 6. Exceções e hipóteses rejeitadas

- **Dados somente no dispositivo:** não contam como coleta pela definição oficial enquanto não saem do dispositivo.
- **Pagamento Google Play:** há exceção estreita para dados coletados diretamente pelo processador para completar o pagamento, como cartão que o app nunca acessa. Isso não resolve automaticamente purchase history, token, Billing diagnostics ou outros dados.
- **Processamento efêmero:** não usar essa exceção sem evidência de uso em memória apenas e descarte imediato após a requisição; retenção do SDK não foi confirmada.
- **Criptografia ponta a ponta:** não aplicável aos dados locais/backup e não comprovada para as transmissões pertinentes. HTTPS CCT estático não prova que todos os dados compartilhados/coletados estejam criptografados.
- **Service provider:** pode excluir compartilhamento em certas condições contratuais/operacionais. O papel do Google/CCT para cada tipo não foi confirmado por documento específico do Billing 9.1.0.
- **Anônimo:** não comprovado. Não inferir anonimização de dados de build/modelo.

## 7. Versões e faixas distribuídas

O proprietário confirmou visualmente na Play Console, nesta conversa, os seguintes estados. Não houve acesso independente do Codex à Console; a origem fica identificada como confirmação do proprietário.

| Faixa | Versão ativa/disponível | Estado da evidência |
|---|---|---|
| Teste interno | versionCode 8 / versionName 1.0.0; ativo e disponível aos testadores internos | **Confirmado pelo proprietário por inspeção visual.** |
| Teste fechado / Alpha | versionCode 11 / versionName 1.0.11; ativo e disponível aos testadores selecionados | **Confirmado pelo proprietário por inspeção visual.** |
| Teste aberto | Nenhuma informada | **Não confirmado**; proprietário não informou essa faixa. |
| Produção | Nenhuma informada | **Não confirmado**; proprietário não informou essa faixa. |
| V12 | versionCode 12 / 1.0.12 ainda não enviado à Console | **Confirmado pelo proprietário.** O AAB local e o Git não são distribuição. |

### Rastreamento local das versões

| Versão da Console | Evidência do código/configuração local | Limites |
|---|---|---|
| V8 — `8 / 1.0.0` | Commit `69aef58` (`chore: preparar release 1.0.0 build 8`) fixa esses metadados em `app/build.gradle`. Nesse snapshot a dependência direta `com.android.billingclient:billing:9.1.0` já está declarada. O histórico mostra integração Billing anterior (`896c0f5`, `a18a4d0`, `c2e97c2`); os arquivos `PlayBillingGateway.kt` e fluxo de consulta/compra estão presentes. | Não foi encontrado tag, AAB preservado ou relatório de grafo release específico do versionCode 8. A presença direta do Billing e do código é comprovada no commit; transitivas efetivamente empacotadas e tráfego do bundle distribuído **não comprovados**. V8 na faixa interna é confirmação do proprietário. |
| V11 — `11 / 1.0.11` | Tag local `v1.0.11-rc.1` aponta para `8aa34881755f222f9b090691aaa6127b31b1a363`; Gradle declara Billing 9.1.0. Fases 3 e 6C registram manifesto/grafo V11 e caminho Billing → Data Transport/CCT, com limites de que isso prova mecanismo e não tráfego efetivo. | O estado Alpha ativo/disponível veio de confirmação visual do proprietário. Não foi consultada a Console nem capturado tráfego desta distribuição. |
| V12 — `12 / 1.0.12` | Master `11214de...` e AAB local de integração têm Billing 9.1.0. Grafo do release existente lista Data Transport API 3.0.0, runtime/CCT 3.1.8 e encoders, além das transitivas anotadas na seção 2. Código/manifesto de rede e fluxo Billing foram inspecionados. | Conforme proprietário, ainda não enviado. Não integrar V12 ao conjunto atual de versões distribuídas; atualizar a declaração quando V12 for enviada/ativa e avaliar sua possível exposição por qualquer faixa, antes da distribuição conforme políticas aplicáveis. |

Em V8 (`69aef58`) e V11 (`v1.0.11-rc.1`) também foram comparados `build.gradle` de raiz e wrapper: ambos registram AGP 8.5.2, Kotlin 1.9.24 e Gradle 8.7; `app/build.gradle` mantém as mesmas dependências diretas AndroidX/Material e Billing 9.1.0. O gateway evoluiu entre os snapshots: V11 filtra o produto e exige preço utilizável antes da compra; V8 usa o primeiro ProductDetails retornado. Isso altera lógica/condições de operação, mas não prova diferença de payload SDK. Os manifestos fonte V8/V11 não incluem `INTERNET` explícita; a permissão de rede e BILLING aparecem no manifesto mesclado V11 por dependências, enquanto o manifesto mesclado histórico do V8 não foi encontrado. Portanto, não afirmar paridade de dependências transitivas nem transmissão entre os dois bundles.

### Consequência para a ficha global

A Play documenta que a ficha é por pacote e representa práticas das versões distribuídas; apps ativos apenas em teste interno são isentos da exibição/obrigação indicada, mas o V11 está ativo em teste fechado Alpha. Portanto, o estado informado exige uma ficha aplicável ao pacote e análise das práticas das versões V8 e V11, não apenas do V12 local. Não se deve presumir que V8/V11/V12 são iguais só porque os arquivos Gradle consultados declaram a mesma versão direta do Billing. No momento, há evidência de que V8 e V11 também incluem Billing 9.1.0; a composição transitiva empacotada de V8 não foi localizada, e os bundles exatos de V8/V11 não foram reextraídos nesta fase.

O AAB V12 ainda não enviado não muda as respostas que descrevem apenas o estado atualmente distribuído. Antes de envio/ativação da V12, verificar regras e estado do Console e atualizar/revisar a ficha para abranger os binários/faixas que passarão a ser distribuídos. A mudança da resposta atual não deve ser usada como substituto para a evidência do SDK.

## 8. Comparação com a política

`site/privacy/index.html` e `app/src/main/assets/privacy_policy.html` têm o mesmo conteúdo local na seção principal, revisão de 15/10/2026. Ambos declaram que:

- dados de clientes/vendas/pagamentos/fotos são locais e não há backend próprio para esses registros;
- exportação, backup/restauração e compartilhamento dependem de ação explícita; cópias externas ficam sob controle dos destinos;
- backup SQLite não tem criptografia/senha própria e não carrega entitlement;
- Billing consulta Google Play para preço, compra, restauração, pendência/erro;
- Billing inclui mecanismo de eventos técnicos e metadados que **podem** acompanhar eventos “quando disponíveis”, inclusive consultas sem compra concluída;
- o app não afirma que nenhum dado é tratado por terceiros.

**Divergência observada:** não há contradição textual direta entre política offline/site e as evidências da auditoria estática. A política qualifica os campos como potenciais, compatível com o limite de que a transmissão efetiva ainda não foi capturada. A resposta global “Não coleta nem compartilha dados”, se aplicada a esta versão e apesar de uma transmissão real de diagnóstico enquadrável, seria potencialmente inconsistente. Como Console e transmissão efetiva não foram confirmadas, essa inconsistência fica **condicional, não comprovada**. A disponibilidade e o conteúdo HTTPS atual do site também não foram verificados aqui.

## 9. Decisão recomendada para o formulário

**A. “Não” pode ser mantido como declaração validada?** Não. Para o estado informado, a ficha cobre pelo menos a V11 Alpha e precisa considerar também a V8 interna no conjunto de práticas aplicável. Há evidência de Billing 9.1.0 no código versionado de ambas; para V11 há mecanismo técnico possível de eventos CCT, mas a transmissão efetiva e taxonomia não estão fechadas. A resposta “Não” não deve ser considerada validada até resolver essa lacuna para as versões distribuídas. A presença da V8 interna, isoladamente, não altera esse ponto, pois a V11 está ativa em faixa fechada.

**B. “Sim” deve ser selecionado agora?** Também não há base bastante para afirmar quais dados de usuário são efetivamente transmitidos/classificados. A recomendação é manter sem salvar/reenviar e classificar a decisão como pendente, em vez de trocar resposta com base apenas no nome da dependência ou nos campos possíveis do bytecode.

**C. Categorias a avaliar primeiro se a transmissão for confirmada:**

1. **App info and performance → Diagnostics**, para eventos técnicos/diagnósticos Billing, apenas se payload/transmissão e finalidade forem confirmados.
2. **Device or other IDs**, apenas se algum campo se enquadrar na definição Play de identificador relacionado a um dispositivo e for efetivamente transmitido; `Build.FINGERPRINT`/modelo não bastam por si.
3. **Financial info → Purchase history**, após confirmar quais elementos de compra/estado/token são transmitidos e aplicação da exceção específica do serviço de pagamento. **User payment info/cartão não deve ser declarado pelo app** com base no código visto; o app não acessa esses campos e a Play processa pagamento diretamente, sujeito aos critérios oficiais.
4. Dados comerciais locais, fotos, arquivos e mensagens não são coleta automática. Para transferência voluntária, avaliar exceção de compartilhamento por ação específica e expectativa razoável.

**D. Confirmações mínimas para resolver:**

1. Obter do suporte/documentação oficial do Google para Billing 9.1.0 uma ficha de Data Safety: tipos exatos transmitidos (incluindo logger/Data Transport/CCT), automaticidade/condições, destino e papel, finalidade por tipo, persistência/retention, possibilidade de opt-out, efemeridade e criptografia. As páginas oficiais de integração/release notes e o SDK Index consultado não forneceram essa ficha específica.
2. Se o Google não publicar a resposta, fazer captura controlada de tráfego em instalação descartável e conta de teste, sem dados reais, cobrindo cold start sem compra, consulta produto/compras, fluxo cancelado e compra/acknowledgement de teste. A captura pode confirmar endpoints/campos efetivamente enviados, mas não estabelece sozinha finalidade contratual, retention ou classificação jurídica; complementar com resposta do fornecedor. Redigir/mascarar tokens e identificadores.
3. Recuperar, se possível, metadados/grafo de dependências e AABs exatos distribuídos da V8 e V11; a configuração versionada prova Billing direto 9.1.0, mas não substitui o conteúdo dos bundles. Para V8 esta é a lacuna maior.
4. Exportar/revisar a ficha Data Safety salva sem alterar ou reenviar, e conferir se descreve as práticas de V8 e V11. Confirmação atual de faixas pelo proprietário já recebida; ainda falta saber se há faixa aberta/produção ou outra versão ativa.
5. Só então decidir categorias, coleta/compartilhamento, propósito, obrigatório/opcional, efemeridade, criptografia global e exclusão. Se respostas dependerem de informação não obtida, não inventar preenchimento.

## 11. Próximos passos para upload controlado da V12

1. **Não enviar V12 agora.** Primeiro resolver ou documentar a resposta pendente do Billing/CCT para o alcance atual V8 + V11. A atualização do formulário permanece sob responsabilidade do proprietário e não deve ser salva/re-enviada sem decisão sustentada.
2. Solicitar ao Google suporte/resposta específica de Billing 9.1.0 e obter artefato/grafo da V8 se disponível; usar os artefatos V11/V12 preservados para comparar a composição do SDK sem tratar presença como transmissão.
3. Confirmar se teste aberto ou produção tem outra versão ativa e revisar a ficha existente/exportação sem alterá-la. Registrar que a V8 interna, sozinha, seria um escopo isento, mas o V11 está em Alpha fechada, então o app não está exclusivamente em teste interno.
4. Antes do upload, verificar na Console o versionCode 12 disponível, alertas e faixa; comparar package/version/assinatura com o AAB local, cujo SHA-256 esperado permanece `4CF6052542FA842EC0796D16006A2912BC825039366C20AE7EB504A608CFA75D`. Não reconstruir o arquivo validado.
5. Confirmar política pública e alinhamento com eventual decisão final Data Safety. Só depois de revisão e autorização explícita do proprietário, fazer upload controlado como rascunho/sem rollout, conferir alertas do Play e compartilhar evidências sanitizadas para revisão. Disponibilização a testers ou produção requer autorização própria e validação de upgrade.

Esta recomendação é sequência de preparação, não autorização para alterar a Console, upload ou distribuição.

## 10. Fontes oficiais consultadas

- [Google Play — Data safety](https://support.google.com/googleplay/android-developer/answer/10787469): coleta inclui transmissão por bibliotecas/SDKs; define exceção para transferência iniciada pelo usuário; define dados efêmeros; exige respostas globais e atribui responsabilidade ao desenvolvedor. A orientação de pagamento é limitada a dados que o processador coleta diretamente para a transação quando o app não os acessa.
- [Google Play — SDK Requirements](https://support.google.com/googleplay/android-developer/answer/13323374): o desenvolvedor responde por código e práticas dos SDKs integrados e precisa saber dados, permissões e propósitos.
- [Google Play Billing — integração](https://developer.android.com/google/play/billing/integrate): documenta consultas de produto/compras e tratamento/acknowledgement. Não é uma declaração completa de Data Safety/telemetria.
- [Billing — release notes](https://developer.android.com/google/play/billing/release-notes): confirma versão/release e alterações de APIs; não foi localizada orientação completa de coleta/retention para 9.1.0.
- [SDK Index](https://developer.android.com/distribute/sdk-index): recurso recomendado pela Play para orientações do provedor. Não foi localizada ficha específica de Data Safety de Billing 9.1.0 nesta consulta.
- [Google Play services — disclosure de dados](https://developers.google.com/android/guides/play-data-disclosure): declara que base/basement/OSS licenses/tasks listados não coletam dados de usuário; a própria página limita o escopo aos SDKs listados. Isso não cobre automaticamente Billing, Data Transport/CCT ou todas as dependências do app.

## Estado final

**DATA SAFETY PARCIALMENTE VALIDADA — CONFIRMAÇÕES PENDENTES.** A lista de faixas informada pelo proprietário identifica V8 interna e V11 Alpha ativas, com V12 ainda fora da Console; a confirmação visual é evidência do proprietário, não acesso independente. Gradle e código versionados mostram Billing 9.1.0 tanto em V8 como V11; apenas V11 tem inventário anterior de transitivas CCT e mesmo esse demonstra mecanismo, não transmissão efetiva. A composição exata do bundle V8, outras faixas, payloads/finalidades e papel do destinatário permanecem por confirmar. Não foi alterado formulário, app, faixas ou AAB. Não houve commit, push, build, captura de tráfego, upload ou publicação. A resposta global “Não coleta nem compartilha dados” permanece não validada para a distribuição atual V8/V11; V12 exigirá revisão antes de sua distribuição.
