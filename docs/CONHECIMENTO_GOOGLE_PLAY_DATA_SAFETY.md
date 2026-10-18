# Conhecimento Google Play — Data Safety e Billing

**Aplicativo:** Vendas Simples — Controle de Vendas (`com.vendassimples.app`)
**Versão analisada:** `versionCode 12`, `versionName 1.0.12`
**Biblioteca de cobrança:** Google Play Billing Library `9.1.0`
**Data desta consolidação:** 18/10/2026
**Origem das respostas:** orientações do Google Play Developer Support relatadas pelo proprietário; datas, transcrição integral e identificador do chamado não foram fornecidos.

## Conclusão aplicável ao V12

**FUNDAMENTADA PELA ORIENTAÇÃO OFICIAL E PELA IMPLEMENTAÇÃO VERIFICADA.** Para a arquitetura V12 revisada, a resposta atual “Não coleta nem compartilha dados do usuário” está fundamentada na orientação complementar do suporte e nas verificações descritas abaixo: dados comerciais ficam no aparelho; não foi identificado backend próprio ou SDK independente de Analytics, anúncios ou crash reporting; o Billing é utilizado para consultar e validar o produto/licença; o token de compra é passado ao Billing para reconhecimento; e telemetria CCT estritamente pertencente à infraestrutura do Play Billing foi objeto de esclarecimento específico do suporte.

Essa conclusão é condicionada à arquitetura auditada e **não é aprovação prévia nem garantia de aprovação da V12**. O suporte informou que não faz verificação prévia; o Google fará a revisão formal após submissão. A responsabilidade pela exatidão da ficha continua com o desenvolvedor. A Console não foi alterada por esta fase.

## Histórico resumido da investigação

- **Fases 9B–9C:** a revisão identificou Billing 9.1.0 e componentes Data Transport/CCT no inventário V12. A análise estática descreveu consultas de produto/compra e mecanismo técnico de transporte, mas a presença de dependências não foi tratada como prova de transmissão efetiva ou de categoria Data Safety.
- **Fase 9D:** comparou as evidências disponíveis de V8, V11 e V12 e registrou lacunas de artefatos históricos. Não se concluiu que os três códigos sejam idênticos.
- **Fase 9E:** incorporou a primeira orientação do suporte e manteve ressalvas, pois ainda faltava esclarecer como o suporte aplicava as exceções a token de compra e à telemetria CCT.
- **Fase 9F:** o proprietário forneceu esclarecimento complementar específico sobre o uso local do token de compra, o reconhecimento pelo Billing e o transporte CCT da infraestrutura do Billing. A análise do código V12 não encontrou divergência concreta em relação à arquitetura descrita.

Os relatórios anteriores foram preservados como cópias documentais nesta branch; a cópia do relatório 9B substitui somente um caminho absoluto local do AAB por uma indicação de redaction. Os arquivos de origem nas worktrees anteriores permanecem intocados.

## Orientações do suporte recebidas

O conteúdo abaixo é um resumo fiel das informações fornecidas pelo proprietário, não uma citação literal da correspondência.

### Primeira resposta

O suporte informou que dados comerciais processados exclusivamente no dispositivo não precisam ser declarados como coletados ou compartilhados; dados tratados diretamente pelo sistema de faturamento Google Play podem estar dispensados quando o aplicativo não os acessa; SDKs fundamentais dos serviços Play não exigiriam divulgação adicional; e a conformidade só seria avaliada na revisão do aplicativo após submissão. O suporte indicou que a análise padrão pode levar até sete dias, conforme volume e complexidade.

### Esclarecimento complementar após descrição do Vendas Simples

O proprietário relatou ao suporte que o app usa Billing 9.1.0 para consultar compras, verificar o direito ao produto `full_access` e reconhecer uma compra com o token retornado pelo Google Play. O suporte esclareceu que o processamento descrito não exige declarar Purchase History ou outras categorias apenas por esse uso. O token é passado à API Billing de reconhecimento, que se comunica com o Google Play; não foi encontrado envio a backend do desenvolvedor ou destino independente. A conclusão deste registro não afirma que o token nunca deixa o dispositivo: ela aplica a orientação específica do suporte ao fluxo Google Play Billing informado. O suporte esclareceu também que a telemetria usada estritamente pela infraestrutura do Google Play Billing segue os termos do Google e não exige declaração adicional pelo desenvolvedor. Com base nessa arquitetura, informou que a resposta global “Não coleta nem compartilha dados do usuário” permanece correta.

O suporte reafirmou que não verifica previamente a conformidade do app. Sua orientação depende da implementação corresponder ao cenário descrito e não substitui a revisão formal. A estimativa de até sete dias não é garantia de prazo ou resultado.

## Verificação técnica da implementação V12

Foi feita leitura estática de `PlayBillingGateway.kt`, `BillingDomain.kt`, `MainActivity.kt`, dependências e política local no commit `11214de6aac8bf1072c1767344c725939d3b1802`.

- Clientes, vendas, parcelas, pagamentos, fotografias e nome do negócio usam armazenamento local do app. Não há backend próprio identificado para esses dados.
- O código conecta o `BillingClient`, consulta detalhes/preço do produto `full_access`, consulta compras existentes e recebe atualizações do fluxo de compra.
- Para uma compra concluída ainda não reconhecida, lê o `purchaseToken` retornado pelo Billing e o entrega a `acknowledgePurchase`. A licença efetiva é refletida no estado de domínio em memória; não foi localizada persistência do token em SQLite/preferências nem inclusão no backup.
- Essa chamada do SDK envia o token ao serviço Google Play para reconhecimento; portanto, não se deve descrever o token como permanecendo fisicamente apenas na memória/localidade do aparelho. O que não foi identificado é envio pelo app a backend do desenvolvedor ou a destino independente. O suporte recebeu a descrição desse fluxo Billing e esclareceu especificamente que não exige declaração adicional nesse cenário; essa conclusão depende do uso permanecer restrito ao serviço e à finalidade informados.
- Não foi identificado código que leia número de cartão, credenciais de pagamento ou endereço de faturamento.
- Não foi identificado backend, Firebase Analytics, publicidade ou crash reporting próprio nas fontes e no inventário consultados. Componentes Data Transport/CCT constam do grafo anterior; a orientação complementar do suporte cobre a telemetria estritamente pertencente à infraestrutura Play Billing. A conclusão deixa de se aplicar se outro uso, destinatário ou finalidade for introduzido.
- Backup é criado localmente no armazenamento privado do app; exportação/restauração por arquivo usa seletores do sistema, e o fluxo “Compartilhar backup” entrega a cópia a um destino escolhido pelo usuário. A licença não é restaurada a partir do arquivo. WhatsApp, CSV, PDF e compartilhamentos iniciam a transferência por ação do usuário e entregam conteúdo ao destino escolhido, não constituindo transmissão automática identificada.
- A política local descreve armazenamento local, compartilhamentos iniciados pelo usuário e a existência de eventos técnicos relacionados ao Billing. Essa transparência é compatível com a orientação complementar específica; a política não transforma por si só um fluxo em coleta declarável.

### Versões distribuídas

Conforme confirmação visual do proprietário, V8 (`versionCode 8`) está em teste interno, V11 (`versionCode 11`) em teste fechado Alpha e V12 (`versionCode 12`) ainda não foi enviada. V8 e V11 não foram reconstruídas ou re-homologadas nesta fase e não se afirma equivalência byte a byte nem igualdade de dependências.

As evidências históricas disponíveis não apresentam divergência concreta de arquitetura que invalide a orientação para o comportamento Billing descrito; porém, artefatos completos de V8/V11 não estão disponíveis para uma nova comparação integral. A política oficial informa que apps ativos somente em teste interno são dispensados da ficha Data Safety, enquanto tracks fechado/aberto/produção exigem a ficha; como V11 está em teste fechado, a ficha global do aplicativo continua relevante. Confirme no Console quais versões/faixas estão ativas antes de qualquer submissão.

## Condições para reutilizar esta conclusão

Esta conclusão vale somente enquanto forem verdadeiras todas as condições seguintes:

1. Dados comerciais continuam locais e não são enviados por backend, sincronização, telemetria própria ou outro SDK.
2. O Billing permanece no fluxo descrito e o token é usado somente com o Google Play Billing para a finalidade de reconhecimento/licença coberta pela orientação recebida.
3. Data Transport/CCT permanece restrito à infraestrutura Play Billing coberta pelo esclarecimento, sem uso adicional ou finalidade independente.
4. Não são adicionados SDKs, endpoints, contas, analytics, publicidade, crash reporting ou processamento externo que colete dados de usuário.
5. Exportação, backup e compartilhamento continuam iniciados pelo usuário, com destinos escolhidos por ele.
6. A política de privacidade permanece coerente com os fluxos reais e com os dados tratados.

**Reavaliar Data Safety e a política** antes de adotar novo SDK, novo endpoint, sincronização, login/conta, transmissão automática, nova finalidade do Billing ou mudança em armazenamento/backup. A declaração atual não pode ser copiada automaticamente para V13 ou outros produtos.

## Limitações e responsabilidade

- O conteúdo das respostas foi fornecido pelo proprietário; a correspondência original não foi inspecionada independentemente. Não foram registrados nome da atendente, e-mails, IDs privados ou dados de clientes.
- A orientação não representa aprovação formal nem determinação vinculante da revisão do app.
- A revisão do Google pode identificar divergências e solicitar mudanças; a responsabilidade por respostas completas e exatas é do desenvolvedor.
- O escopo histórico V8/V11 é limitado pela ausência dos bundles completos nesta auditoria. Nenhuma divergência concreta foi encontrada nos registros disponíveis, mas não se declara identidade entre versões.
- Nenhuma declaração da Console foi editada, salva ou reenviada como parte desta documentação.

## Referências

- [Google Play — Data safety](https://support.google.com/googleplay/android-developer/answer/10787469): definição de coleta como transmissão para fora do dispositivo, processamento somente local, tratamento por bibliotecas/SDKs, exceções de compartilhamento e responsabilidade do desenvolvedor.
- [Google Play — SDK Requirements](https://support.google.com/googleplay/android-developer/answer/13323374): requisitos e responsabilidade associados a SDKs.
- [Google Play Billing — integração](https://developer.android.com/google/play/billing/integrate): uso das APIs de Billing para consultar produto/compra e reconhecer compras.
- [Google Play Billing — notas de versão](https://developer.android.com/google/play/billing/release-notes): versões da biblioteca.
- Orientações caso-específicas do Google Play Developer Support, conforme relato do proprietário; sem link público ou ID de atendimento neste registro.

## Relatórios de evidência

- [Fase 9A — Pós-merge e preparação Alpha](FASE9A_POS_MERGE_PREPARACAO_ALPHA.md)
- [Fase 9B — Conformidade Alpha V12](FASE9B_CONFORMIDADE_ALPHA_V12.md)
- [Fase 9C — Auditoria Data Safety/Billing](FASE9C_AUDITORIA_DATA_SAFETY_BILLING.md)
- [Fase 9D — Resolução Data Safety](FASE9D_RESOLUCAO_DATA_SAFETY.md)
- [Fase 9E — Resposta inicial do Google](FASE9E_RESPOSTA_GOOGLE_DATA_SAFETY.md)
- [Fase 9F — Encerramento e preparação Alpha](FASE9F_ENCERRAMENTO_DATA_SAFETY_ALPHA.md)
