# Fase 9E — Resposta do Google Play e decisão Data Safety V12

**Base inspecionada:** `master`, commit `11214de6aac8bf1072c1767344c725939d3b1802`\
**Versão candidata:** `com.vendassimples.app`, `versionCode 12`, `versionName 1.0.12`\
**Escopo:** análise documental e estática. Nenhum código, declaração do Console, AAB, faixa ou dado de dispositivo foi alterado.

## 1. Conclusão executiva

**Classificação: DATA SAFETY FUNDAMENTADA COM RESSALVAS.**

A resposta fornecida pelo proprietário como orientação recebida do suporte oficial reforça que dados comerciais mantidos exclusivamente no aparelho não são coleta para a declaração e que dados de pagamento processados diretamente pelo Google Play podem ter tratamento dispensado quando o aplicativo não os acessa. O suporte também distinguiu os SDKs básicos dos serviços Play e informou que a conformidade será avaliada na análise do app, que pode levar até sete dias.

Isso é orientação de suporte relatada pelo proprietário, não aprovação formal do app, decisão sobre a ficha específica nem confirmação técnica do payload do Billing 9.1.0. O código V12 acessa objetos retornados pelo Billing — inclusive estado da compra e token — e envia o token de volta ao método de reconhecimento do Billing. Além disso, o inventário anterior identificou um caminho possível de eventos técnicos via Data Transport/CCT, sem comprovar quais eventos são emitidos em execução. Assim, a resposta global “Não” tem fundamento para dados locais e ausência de backend próprio, mas **não está integralmente validada para todos os fluxos e versões abrangidos pela ficha**.

Não há categoria positiva de Billing que esta revisão consiga confirmar com segurança. Também não se deve selecionar “Sim” ou categorias por precaução sem fechar tipo de dado, finalidade, destinatário e exceção aplicável. Recomenda-se manter a ficha sem alterações até resolver as ressalvas abaixo; isso não impede preparação técnica offline, mas a decisão de upload, envio para análise e disponibilização a testadores permanece separada e requer autorização específica do proprietário.

## 2. Orientação do suporte e seus limites

Conforme texto fornecido pelo proprietário, o suporte informou que:

- dados exclusivamente locais não exigem declaração de coleta;
- dados tratados diretamente pelo sistema de faturamento Google Play podem estar dispensados se o aplicativo não os acessar;
- SDKs básicos dos serviços Play não exigiriam divulgação adicional pelo desenvolvedor;
- a conformidade será avaliada durante a análise do aplicativo, com prazo de revisão que pode chegar a sete dias.

O relato foi registrado sem nome, endereço de e-mail, identificador de chamado ou outros dados privados. O prazo informado é estimativa operacional, não garantia. A análise futura do Google não equivale a aprovação antecipada. A afirmação sobre SDKs básicos não foi estendida a Billing Library, Data Transport ou backend CCT.

## 3. Implementação V12 observada

Em `app/src/main/java/com/example/controlevendas/PlayBillingGateway.kt`, o aplicativo:

- inicializa o `BillingClient` e, ao conectar, consulta detalhes do produto `full_access` e consulta compras existentes;
- lê os detalhes de produto e o preço formatado para apresentá-lo;
- lê a lista de produtos da compra, estado `PENDING`/`PURCHASED` e `isAcknowledged`;
- para compra concluída ainda não reconhecida, lê `purchaseToken` e o passa a `acknowledgePurchase` do Google Play Billing;
- atualiza o estado de licença em `BillingDomain`/`MainActivity`, mantido em memória nesta implementação.

O token é, portanto, **acessado pelo código do app**, embora não tenha sido encontrada persistência própria em SQLite, preferências ou backup, nem envio a servidor do desenvolvedor. A chamada de reconhecimento é uma solicitação ao serviço Billing usando o SDK; isso não prova por si só uma transmissão a terceiro fora do Google Play nem determina sua categoria Data Safety. O aplicativo não lê dados de cartão, credenciais de pagamento ou endereço de cobrança no código inspecionado.

O inventário de release existente inclui Billing 9.1.0 e componentes Data Transport/CCT. As Fases 6C/9C registram mecanismo e campos técnicos potencialmente disponíveis, mas não uma captura de payload em execução. A presença dessas dependências demonstra um caminho possível, não a emissão efetiva de cada campo/evento. Não foi feita captura de tráfego nesta fase.

## 4. Decisão por tipo de informação

| Informação / categoria candidata | Evidência V12 | Decisão nesta revisão |
|---|---|---|
| Clientes, vendas, parcelas, pagamentos, fotos e nome comercial | Persistidos localmente; não foi identificado backend próprio ou transmissão automática desses dados. Exportação/backup/compartilhamento depende de ação do usuário. | Se permanecerem no aparelho, não são coleta. Transferências voluntárias devem ser avaliadas segundo o fluxo iniciado e esperado pelo usuário. Não declarar coleta automática com base apenas no armazenamento local. |
| Cartão, credenciais e endereço de cobrança | Nenhuma leitura desses dados foi encontrada no código V12; orientação do suporte contempla dados tratados diretamente pelo Play quando não acessados pelo app. | A condição de não acesso pelo app está atendida para esses dados observados. Não extrapolar essa exceção para tokens, histórico de compras ou diagnósticos. |
| Compra/preço/estado e token (`Purchase`, `ProductDetails`) | O app acessa preço, produtos, estado e token em callbacks do Billing; token é passado a `acknowledgePurchase`. Sem persistência própria ou backend identificados. | Acessar localmente não equivale automaticamente a coleta declarável. Contudo, a condição geral “o app não acessa dados de compra” não descreve integralmente o código. Categoria Purchase history, transmissão, finalidade, papel do destinatário e exceção específica permanecem sem confirmação. Não marcar positiva nem negativa como fato fechado. |
| Diagnósticos e metadados técnicos possivelmente enviados via Data Transport/CCT | Caminho de transporte consta da análise estática anterior e da política local; payload/eventos efetivamente transmitidos não foram observados. | Pendente. A orientação sobre SDKs básicos não fecha Billing/CCT. Não classificar como Analytics, Diagnostics ou outra categoria sem payload/finalidade confirmados. |
| Identificadores de dispositivo/app | Campos técnicos podem existir no caminho CCT; não foi comprovado identificador transmitido nem sua semântica no payload efetivo. | Pendente; não selecionar categoria por inferência. |

Esta tabela descreve o que foi observado no V12. As versões V8 e V11 constam, conforme confirmação visual anterior do proprietário, como ativas em faixas distintas; não se presume equivalência de SDKs ou comportamento. O inventário histórico e as limitações de V8/V11 permanecem conforme Fases 9C e 9D.

## 5. Respostas objetivas

**A resposta “Não” está suficientemente fundamentada?** Parcialmente. É sustentada para os dados comerciais estritamente locais e pela ausência identificada de backend próprio. Ainda não está validada para o caminho Billing/Data Transport/CCT nas versões distribuídas. Portanto, não registrar “Não” como decisão final comprovada.

**Há condição do suporte que não é atendida?** Para informações de compra retornadas pelo Billing, o aplicativo acessa alguns campos e o token. A exceção descrita pelo suporte para dados que o aplicativo não acessa não pode ser aplicada automaticamente a todo o fluxo. Para cartão e credenciais, nenhum acesso pelo app foi encontrado.

**Alguma categoria precisa efetivamente ser declarada?** Nenhuma categoria positiva foi confirmada por evidência suficiente nesta inspeção. Purchase history, Diagnostics e Device or other IDs são pontos a esclarecer, não recomendações de marcação.

**O que impede ou não impede o envio?** Não existe aprovação antecipada do suporte que desbloqueie a ficha. A dúvida de classificação é relevante para submeter uma declaração correta, mas a capacidade técnica de carregar um AAB é diferente da conformidade e do envio para análise. A decisão conservadora é manter a ficha inalterada até esclarecer as lacunas e não iniciar rollout para testadores sem autorização específica.

## 6. Consistência com a política de privacidade

`app/src/main/assets/privacy_policy.html` informa que os dados comerciais ficam no dispositivo, descreve exportações iniciadas pelo usuário e declara que a biblioteca Billing pode enviar eventos técnicos de operação/diagnóstico a serviços Google, inclusive em consultas sem compra concluída, com possíveis metadados do aparelho, Android/app, idioma/região, conexão e operadora. Também afirma que não há envio próprio dos registros comerciais a servidor do desenvolvedor.

Essa redação é cautelosa e compatível com o mecanismo potencial descrito nas auditorias anteriores; ela não prova que os eventos ocorrem nem resolve a classificação Data Safety. A resposta global “Não” só será coerente se esses eventos não constituírem coleta declarável ou estiverem cobertos por exceção aplicável, algo que a orientação recebida ainda não determinou. Nenhuma alteração foi feita na política local ou pública.

## 7. AAB e estado da Console

O AAB existente foi lido sem reconstrução ou sobrescrita em `integration-v12-master-validation/app/build/outputs/bundle/release/app-release.aab`: tamanho **5.361.969 bytes**; SHA-256 **`4CF6052542FA842EC0796D16006A2912BC825039366C20AE7EB504A608CFA75D`**, igual ao valor fornecido para o artefato preservado. A conferência não altera o arquivo.

Conforme confirmação visual anterior do proprietário: V8 está ativa no teste interno, V11 está ativa no teste fechado Alpha e V12 ainda não foi enviada. O estado atual da Console não foi acessado nesta fase. A ficha Data Safety precisa cobrir as versões/faixas que a Console inclui; confirme novamente o escopo antes de qualquer envio.

## 8. Retomada Alpha — etapas separadas

1. **Fechar a classificação:** solicitar ao suporte esclarecimento específico sobre acesso a `Purchase`/token e eventos de Billing 9.1.0/Data Transport/CCT, ou documentação oficial equivalente que mapeie dados, finalidade, destinatário e exceções. Recuperar os artefatos/inventários exatos de V8 e V11 se disponíveis. Não enviar logs com tokens, dados reais ou identificadores.
2. **Revisar a ficha e política:** após resposta, concluir cada categoria para todas as versões distribuídas. A decisão de salvar/re-enviar a declaração é do proprietário na Console.
3. **Upload do AAB:** após autorização explícita de upload, verificar versão, hash, assinatura e alertas atuais e carregar somente o AAB existente. Upload não significa envio para análise nem disponibilização.
4. **Enviar para análise:** requer autorização separada e ficha/política coerentes. A estimativa de até sete dias relatada pelo suporte não garante prazo ou resultado.
5. **Disponibilizar aos testadores:** requer autorização própria após análise/estado da Console confirmado; verificar faixa e lista de testadores antes de rollout.
6. **Homologação:** teste V11→V12 no Samsung S22+ deve ser atualização sobre a instalação existente, sem desinstalar, limpar dados ou instalar APK debug. Preservar dados reais e validar vendas, parcelas, pagamentos, fotos, nome do negócio e licença. Testes Billing devem usar license testers/sandbox, sem compra real. Testar também API 24 com dados sintéticos. Acesso direto do Codex ao dispositivo não foi estabelecido nesta fase.

## 9. Próxima ação recomendada

Pedir ao suporte uma resposta complementar, referindo-se à orientação já recebida e restringindo a pergunta a: (1) se o acesso local do app a `Purchase.purchaseToken`, estado/produto e `ProductDetails` muda a aplicação da exceção de dados tratados pelo sistema Billing; (2) se e quais eventos da Billing 9.1.0 são enviados via Data Transport/CCT sem compra concluída; (3) categorias Data Safety, finalidade, destinatário, retenção e criptografia correspondentes; e (4) se a exceção de processador/SDK cobre esses dados para a ficha que inclui V8/V11. Até resposta suficiente, não alterar a ficha.

Nenhum código, versão, Billing, política, Console, AAB ou dados reais foi modificado. Não houve build, teste físico, compra, captura de rede, upload, envio para análise, rollout, commit ou push.
