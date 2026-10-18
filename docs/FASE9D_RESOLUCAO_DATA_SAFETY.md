# Fase 9D — Resolução Data Safety: Billing e versões V8/V11/V12

**Data:** 16/10/2026
**Base oficial:** `master`, `11214de6aac8bf1072c1767344c725939d3b1802`
**Escopo:** investigação estática/documental complementar à Fase 9C. Sem alteração do app, AAB, formulário Play ou faixas.

## Classificação

**DATA SAFETY RESOLVIDA PARCIALMENTE — AÇÃO EXTERNA DEFINIDA.**

O estado de distribuição informado pelo proprietário é: V8 (`8 / 1.0.0`) ativa em teste interno; V11 (`11 / 1.0.11`) ativa em teste fechado Alpha; V12 (`12 / 1.0.12`) ainda não enviada. O relatório preserva essa origem como confirmação visual do proprietário, não como consulta independente pelo Codex.

Conclusão objetiva: a resposta global **“Não coleta nem compartilha dados” não está validada** para o app enquanto V11 está em teste fechado e seu SDK Billing possui mecanismo de telemetria não resolvido. Também não há evidência suficiente para selecionar “Sim” e nomear categorias específicas do Billing. A ausência de captura ou a existência do SDK não resolve a questão em nenhum dos sentidos. Deve-se manter o formulário sem alteração até a resposta técnica do fornecedor/Google; esta é uma recomendação de estado de trabalho, não uma escolha definitiva de resposta.

Dados comerciais mantidos somente no aparelho não são coleta Data Safety. Os fluxos de compartilhamento/SAF/WhatsApp partem de ação expressa; a exceção oficial pode dispensar “sharing” quando a transferência é específica e razoavelmente esperada. Nenhum upload automático de dados comerciais para backend do desenvolvedor foi encontrado.

## 1. Ambiente Java e Gradle

- JDK localizado em `C:\Program Files\Android\Android Studio\jbr`, OpenJDK **21.0.10**. A sessão não tinha `JAVA_HOME` nem `java` em `PATH`.
- Gradle Wrapper da integração V12: **Gradle 8.11.1**, confirmado em `gradle/wrapper/gradle-wrapper.properties`.
- O JBR foi configurado temporariamente só no processo PowerShell. Não houve alteração das variáveis globais.
- Tentativa `:app:dependencies --configuration releaseRuntimeClasspath --offline` no worktree V12 falhou na resolução do plugin `org.jetbrains.kotlin.android:2.2.20`; a distribuição/cache do Wrapper iniciou, mas esse artefato de plugin não estava disponível no cache offline usado. Não se repetiu com rede nem se mudou o cache global.
- Evidência alternativa V12: grafo de release já produzido no worktree `integration/v12-master-validation` confirma Billing 9.1.0, Data Transport API 3.0.0, transport runtime 3.1.8, backend CCT 3.1.8 e Firebase encoders (JSON 18.0.0, proto 16.0.0, encoders 17.0.0), entre outras dependências. Isso é evidência do grafo daquela variante/build, não tráfego real.

## 2. Comparação de versões — sem extrapolação

| Versão / distribuição informada | Evidência versionada e dependências | Caminho de Billing / transmissões | Conclusão específica |
|---|---|---|---|
| **V8 — code 8 / 1.0.0**, teste interno ativo | Commit `69aef58` fixa metadados e declara Billing direto `9.1.0`. Snapshot também usa AGP 8.5.2, Kotlin 1.9.24, Gradle 8.7 e mesmas dependências diretas AndroidX/Material do V11. Código `PlayBillingGateway` existe no snapshot. Não foi encontrado tag, AAB, manifesto mesclado ou grafo de dependências da build enviada. | O código versionado chama `startConnection`, consulta produto/compras, abre fluxo de compra e reconhece compra. No V8 `queryProduct` usa primeiro ProductDetails retornado; não há evidência do bundle de que CCT/transporte foi empacotado, nem captura de tráfego. | Billing e chamadas no código: **confirmados no commit**. Transitivas e transmissão do artefato V8: **não comprovadas**. A faixa interna foi confirmada pelo proprietário. A isenção para apps exclusivamente internos não se aplica ao pacote como um todo, pois V11 está na Alpha; se o artefato interno V8 entra no escopo efetivo da ficha global deve ser confirmado na Console. |
| **V11 — code 11 / 1.0.11**, teste fechado Alpha ativo | Tag local `v1.0.11-rc.1` em `8aa34881755f222f9b090691aaa6127b31b1a363`; Gradle declara Billing 9.1.0, AGP 8.5.2, Kotlin 1.9.24, Gradle 8.7. Relatórios Fases 3/6C contêm inventário release/manifesto e inspeção estática Billing → Data Transport/CCT. Não foi recuperado nesta fase um AAB/grafo específico do artefato atualmente ativo. | Código consulta produto e compras na inicialização e tem fluxo compra/ack. A análise documentada V11 identificou `Transport.send` e metadados técnicos disponíveis no backend CCT. É evidência de mecanismo/capacidade, não de emissão/payload efetivos. Nenhuma captura foi feita. | Mecanismo CCT e metadados possíveis: **evidência estática documentada**. Transmissão efetiva e categoria: **não comprovadas**. Alpha ativa foi confirmada pelo proprietário; esta faixa fechada torna obrigatório manter declaração aplicável ao app publicado, conforme as regras da Play. |
| **V12 — code 12 / 1.0.12**, ainda não enviada | Master `11214de...` e configuração Gradle declaram Billing 9.1.0; AGP 8.9.1, Kotlin 2.2.20, Wrapper 8.11.1. Grafo release do worktree V12 disponível lista Data Transport/CCT e encoders. A tentativa de regerar grafo falhou pelo plugin ausente no cache offline. AAB previamente conhecido permanece fora do escopo de leitura/rebuild desta fase. | O fluxo V12 tem `startConnection`, `queryProductDetailsAsync`, `queryPurchasesAsync`, `launchBillingFlow` e `acknowledgePurchase`. A política V12 descreve metadados Billing/CCT potenciais. Não houve tráfego observado nesta fase. | Código/grafo: **confirmados localmente**. V12 não faz parte das versões enviadas segundo o proprietário; não acrescentar seus comportamentos à declaração de binários distribuídos antes de envio. Fazer revisão do formulário antes da distribuição V12. |

V8/V11 compartilham configurações diretas e toolchain versionada no Gradle, mas isso **não prova paridade transitiva dos bundles**. V11/V12 têm evidência documental mais forte para CCT; não atribuir automaticamente essa evidência ao bundle V8.

## 3. Fontes oficiais específicas e resultado da busca

- O [formulário Data Safety oficial](https://support.google.com/googleplay/android-developer/answer/10787469) diz que coleta inclui dados de usuário transmitidos por bibliotecas/SDKs mesmo quando enviados diretamente a terceiros; define processamento efêmero, uso apenas local e exceção de transferência iniciada pelo usuário. A responsabilidade pela precisão permanece com o desenvolvedor. Dados tratados apenas no dispositivo não contam como coletados. Os dados que um processador de pagamento coleta diretamente para completar transação podem ter exceção limitada, por exemplo cartão que o app nunca acessa; não estender automaticamente a compra/histórico/diagnóstico.
- Os [requisitos de SDKs](https://support.google.com/googleplay/android-developer/answer/13323374) responsabilizam o app por entender dados, propósitos e práticas do terceiro integrado.
- A [integração Billing](https://developer.android.com/google/play/billing/integrate) documenta `queryProductDetailsAsync`, `queryPurchasesAsync`, fluxo de pagamento e reconhecimento. **Não apresenta inventário de telemetria/Data Safety** específico da 9.1.0.
- As [release notes Billing](https://developer.android.com/google/play/billing/release-notes) descrevem mudanças funcionais/API da 9.1.0, não payload, finalidades ou retenção.
- O [Google Play SDK Index](https://developer.android.com/distribute/sdk-index) é recomendado pela própria Data Safety para procurar orientação do provedor. A página pública consultada não retornou entrada/texto para “Billing”; busca oficial não encontrou uma declaração Data Safety específica de `com.android.billingclient:billing:9.1.0`.
- O [disclosure Google Play services](https://developers.google.com/android/guides/play-data-disclosure) limita a conclusão de ausência de coleta a `play-services-base`, `basement`, `oss-licenses` e `tasks` listados; não cobre por extensão Billing, transport-runtime ou backend CCT.
- A documentação/inventário oficial público encontrado não contém a resposta específica de Billing 9.1.0 sobre envio de diagnóstico, payload, destinatários, finalidades, retenção ou efemeridade. Logo, a lacuna requer resposta do dono do SDK ou evidência técnica e operacional complementar.

## 4. Matriz resumida para a decisão atual

| Dado candidato | V8 | V11 | V12 | Pode declarar agora? |
|---|---|---|---|---|
| Dados comerciais: nome/contatos de clientes, valores, fotos, CSV/PDF/backup | Código histórico mantém uso local e exportações explícitas; bundle V8 não foi reconstituído integralmente. | Local no código; exportações iniciadas pelo usuário. | Local no código; exportações iniciadas pelo usuário. | **Não como coleta automática** quando permanece local. Aplicar exceção de compartilhamento só aos handoffs específicos esperados. Nenhuma categoria de coleta confirmada por esse processamento local.
| Cartão/credenciais de pagamento | App não lê cartão no código versionado consultado. | App não lê cartão. | App não lê cartão. | **Não declarar como dado coletado pelo app** com base no fluxo observado; confirmar a ficha antiga e não generalizar essa exceção ao restante do Billing.
| Histórico/estado/token de compra | Código Billing consulta compras e recebe objeto Purchase; nenhum grafo/bundle de V8 disponível. | Consulta/recuperação/ack no código; uso de token em memória registrado. | Consulta/recuperação/ack no código; token passa ao acknowledge. | Acesso funcional pelo app confirmado; se o Play considera isso `Purchase history` coletado/compartilhado e quais campos saem do dispositivo **pendente**.
| Diagnostics / app info & performance de Billing | Possibilidade não comprovada no bundle V8. | Caminho de logger/DataTransport/CCT estático documentado; envio real não observado. | Grafo inclui CCT; configuração/logger documentados, envio real não observado. | **Não selecionar por hipótese**; pedir dados e confirmar transmissão/categoria.
| Device or other IDs | Não identificados no artefato V8. | Campos CCT potenciais; não se provou identificador persistente do usuário/dispositivo. | Campos CCT potenciais no inventário; captura não feita. | **Pendente**; Build.FINGERPRINT/modelo não bastam isoladamente para classificar como ID.
| App interactions, Analytics, Ads, Crash logs, location | Não identificado no código próprio consultado; bundle V8 não reconstituído. | Não há integração própria identificada. | Não há integração própria identificada. | **Não marcar** sem evidência; dados CCT eventuais não podem ser chamados de analytics/crash sem payload/finalidade.

Nenhuma categoria de dados Billing foi confirmada a ponto de recomendar seleção positiva segura no formulário. Isso não é prova de que nenhum dado de usuário seja transmitido.

## 5. Pergunta pronta para suporte oficial (não enviada)

**Canal:** Play Console → Ajuda/Help → Contact developer support, solicitando encaminhamento à equipe responsável pelo Google Play Billing Library. A página oficial [Get Developer Support](https://support.google.com/googleplay/android-developer/answer/10357403) direciona problemas de desenvolvedor ao formulário/suporte online. O formulário Data Safety serve para declarar práticas; não é um canal técnico de consulta ao fornecedor.

> We maintain an Android app using `com.android.billingclient:billing:9.1.0` (one-time INAPP product). Our app creates a BillingClient on startup, calls `startConnection`, `queryProductDetailsAsync` and `queryPurchasesAsync`, launches `launchBillingFlow` only after a user action, and calls `acknowledgePurchase` for an unacknowledged purchase. Our release dependency inventory also contains Data Transport API 3.0.0, transport-runtime 3.1.8 and transport-backend-cct 3.1.8. The app has no developer backend, Analytics, ads or Crashlytics integration identified. For Google Play Data safety, please provide or point us to authoritative, version-specific documentation for Billing Library 9.1.0 and its associated logger/transport path: (1) exact end-user data types/fields transmitted, including on startup/product/purchase queries and without completing a purchase; (2) which events/fields are automatic versus conditional on purchase or app calls; (3) endpoints/recipients and whether each acts as Google Play payment processor, service provider or another third party; (4) purpose by data type; (5) whether purchase history, purchase token, diagnostics, app/device identifiers, model/build, locale, network/carrier metadata are transmitted and how they map to Play Data safety categories; (6) retention/deletion and ephemeral processing; (7) encryption in transit for each path; and (8) which data, if any, is covered by the payment-processing or service-provider exceptions. Please distinguish Billing 9.1.0 from the core Play services base/basement/tasks SDK disclosure and from CCT’s generic behavior. We can provide dependency reports and a synthetic test reproduction without purchase/customer data if needed.

Não foi feito contato. Anexar apenas grafo, versões e descrição do fluxo; não enviar purchase token, credenciais, logs com identificadores ou dados reais de cliente.

## 6. Respostas objetivas

**A. Evidência para manter “Não”?** Não suficiente para validar. V11 está ativa em Alpha; há caminho SDK documentado que pode transmitir dados técnicos, ainda sem confirmação do payload/execução. “Sem backend próprio” não resolve o papel do SDK.

**B. Evidência para selecionar “Sim”?** Não suficiente para selecionar com segurança tipos/campos para Billing sem confirmação do que efetivamente é transmitido e da taxonomia aplicável. Não marcar categorias especulativas “por precaução”.

**C. Categorias que podem ser declaradas com segurança agora?** Dados estritamente locais não são coleta. Ação do usuário com exportação/compartilhamento pode se enquadrar na exceção de sharing esperada. Não há categoria positiva de Billing confirmada. Cartão não é acessado pelo app observado; usar somente a exceção estreita do processador, sem extrapolar para purchase history/diagnóstico.

**D. Campos pendentes:** `Diagnostics`, `Device or other IDs`, `Purchase history`; finalidade, obrigatório/opcional, efemeridade, destinatário/papel, retenção/exclusão e se todos os fluxos aplicáveis usam criptografia. Também confirmar se existe teste aberto/produção e o escopo da faixa V8 dentro da ficha global.

**E. Confirmação externa indispensável:** resposta do Google Billing/Developer Support ou documentação oficial específica para 9.1.0; além de confirmação no Console de todas as faixas/códigos e revisão da ficha salva (sem alterar). Se o suporte não puder fornecer payloads, uma captura controlada pode demonstrar endpoints/campos observados, mas não comprova ausência de eventos em outros cenários nem determina sozinha retenção, papel legal e finalidade.

**F. Preparar upload sem liberar aos testers?** Preparação offline do AAB e checklist é possível. Tecnicamente uma release pode em alguns fluxos ser carregada e mantida em rascunho sem rollout, mas a exigência concreta e os bloqueios de política dependem da Console e não foram verificados; este relatório não autoriza upload. Não enviar à revisão nem iniciar rollout/testers enquanto a declaração aplicável continuar pendente. Confirmar status de política/versionCode no Console antes de qualquer upload e obter autorização explícita para upload separado.

## 7. Menor sequência para retomar V12 Alpha

1. Proprietário confirma se há faixa aberta/produção e se o versionCode 8 interno aparece como versão distribuída/considerada no escopo da ficha.
2. Proprietário envia a pergunta técnica acima pelo suporte oficial; paralelamente, recuperar os bundles/relatórios exatos de V8 e V11 se ainda existirem. Não reconstruir versões históricas para fingir equivalência.
3. Com a resposta/documentos, fechar a matriz e rever a ficha atual e a política. Manter sem salvar/re-enviar enquanto as categorias não forem sustentadas.
4. Confirmar status da política, elegibilidade do code 12, assinatura/hash e alertas na Console.
5. Após revisão e autorização explícita, upload controlado do AAB V12 exato como rascunho sem rollout; conferir que nenhum tester recebeu a versão. Envio para revisão/ativação de Alpha exige autorização própria e declaração coerente.

## Estado final

**DATA SAFETY RESOLVIDA PARCIALMENTE — AÇÃO EXTERNA DEFINIDA.** Evidência adicional: JBR local identificado; tentativa Gradle esclareceu limitação específica do plugin Kotlin ausente no cache offline; histórico V8/V11 comparado; documentação oficial consultada e ausência de guidance pública Billing 9.1.0 registrada; canal e pergunta técnica prontos. Permanecem não confirmados payload Billing/CCT, papel/finalidade/retention, conteúdo exato dos artefatos V8/V11 e faixas adicionais. Nenhum código, AAB, formulário ou faixa foi alterado. Sem build de artefato, compra, captura de tráfego, upload, rollout, commit ou push.
