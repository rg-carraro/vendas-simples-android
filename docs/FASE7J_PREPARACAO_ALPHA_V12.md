# Fase 7J — Preparação final do Alpha V12

**Data:** 16/10/2026

**Branch / commit:** `integration/v12-master-validation` / `64344855e377dadf5f6b3044cadde6b41e728459`

**Aplicativo:** `com.vendassimples.app` — `versionCode 12`, `versionName 1.0.12`

**Classificação:** **PRONTO COM PENDÊNCIAS DE CONFORMIDADE**. O artefato está pronto para a revisão operacional do Alpha; as pendências abaixo impedem afirmar conformidade completa. Nenhum upload ou publicação foi realizado.

## 1. Resumo executivo

O AAB de release já validado na Fase 7I foi preservado e conferido novamente sem recompilação. Tamanho e SHA-256 coincidem exatamente com o registro anterior; `jarsigner` confirmou a assinatura e `keytool` extraiu os fingerprints esperados da chave de upload. O manifesto e `bundletool validate` já haviam sido verificados na Fase 7I; a tentativa de repetir essa leitura nesta fase não pôde iniciar o Gradle por bloqueio de acesso ao lock do wrapper e falha ao carregar `native-platform.dll`. Como o SHA-256 atual é idêntico ao do bundle validado, os mesmos bytes e metadados permanecem cobertos pela validação registrada na Fase 7I.

Não foram alterados código, versão, assinatura ou AAB. Não houve acesso à Play Console nesta fase. As pendências principais continuam sendo a conferência das declarações e versões ativas na Console, a investigação de dados do Billing/SDK e a homologação física da V12.

## 2. AAB conferido

| Propriedade | Resultado |
|---|---|
| Arquivo | `app/build/outputs/bundle/release/app-release.aab` neste worktree |
| Caminho relativo ao worktree | `app/build/outputs/bundle/release/app-release.aab` |
| Tamanho | **5.361.969 bytes** |
| SHA-256 | **`4CF6052542FA842EC0796D16006A2912BC825039366C20AE7EB504A608CFA75D`** |
| Origem | `integration/v12-master-validation`, commit `64344855e377dadf5f6b3044cadde6b41e728459` |
| Application ID | `com.vendassimples.app` — conferido na Fase 7I |
| VersionCode / VersionName | `12` / `1.0.12` — conferidos na Fase 7I e configuração atual |
| minSdk / targetSdk | `24` / `36` — conferidos na Fase 7I e configuração atual |
| Assinatura | `jarsigner -verify -certs`: `jar verified` |
| Certificado SHA-1 | `FE:B9:79:CD:C1:3F:B4:EE:FA:80:35:8D:70:6F:9F:35:40:0F:34:AE` |
| Certificado SHA-256 | `CD:90:9D:AE:E8:16:B2:7F:4E:32:13:52:73:62:25:80:BE:F0:73:F1:8A:8D:EB:56:04:F5:24:90:9C:63:C6:9E` |

O certificado extraído do bundle coincide com o fingerprint informado para a chave de upload. `jarsigner` também reporta que o certificado é autoassinado e não tem timestamp; esses avisos são registrados para interpretação e não indicam divergência do fingerprint. A identidade final do pacote enviado ainda deve ser conferida na Console.

Na tentativa de repetir `bundletool validate` e `dump manifest`, o wrapper foi bloqueado ao abrir o lock de distribuição do Gradle fora do worktree; a execução direta do Gradle local também não iniciou devido ao carregamento de `native-platform.dll`. Nenhuma tarefa de build ou `bundleRelease` foi executada nesta fase. A validação do manifesto e do bundletool consta da Fase 7I para este SHA-256 idêntico.

## 3. Notas da versão — texto pronto para a Console

```text
Melhorias na validação e transferência de backups, com ajustes para proteger seus dados durante a restauração. Também aprimoramos a estabilidade e a compatibilidade do aplicativo.
```

O texto se limita às melhorias de backup/validação e compatibilidade que constam das alterações integradas e dos testes das Fases 7H/7I. Não anuncia sincronização, criptografia própria do backup, serviço em nuvem ou recurso não implementado. Revisar o texto no contexto da ficha do app antes de inseri-lo.

## 4. Checklist de upload Alpha — não executado

Usar os nomes e controles apresentados pela Console no momento da operação; esta preparação não presume a nomenclatura atual da interface.

- [ ] Abrir a conta correta da Google Play Console e selecionar Vendas Simples (`com.vendassimples.app`).
- [ ] Confirmar que `versionCode 12` está disponível e identificar a faixa Alpha/teste existente adequada; não substituir outra versão sem revisar seu estado.
- [ ] Iniciar uma nova versão nessa faixa e selecionar exatamente o AAB acima. Não regenerar nem substituir o arquivo validado.
- [ ] Conferir na Console package/application ID, versionCode 12 e versionName 1.0.12.
- [ ] Colar as notas acima e revisar idioma e apresentação.
- [ ] Ler todos os alertas, erros e solicitações da Console. Interromper se houver divergência de identidade, versão, assinatura ou bloqueio inesperado.
- [ ] Revisar a declaração de Segurança dos dados e sua consistência com a política pública e com as evidências da Fase 6C; não confirmar respostas sem evidência.
- [ ] Confirmar lista, elegibilidade e instruções de acesso dos testadores Alpha, sem compartilhar link antes da revisão responsável.
- [ ] Revisar o resumo da versão e as alterações planejadas. Upload, disponibilização aos testadores e publicação exigem ação/autorização separada; nenhum deles ocorreu nesta fase.

## 5. Segurança dos dados e Billing

### Evidência do código e dos artefatos locais

- Dados de clientes, vendas, parcelas, pagamentos e fotos ficam no SQLite privado; o nome do negócio usa preferência privada. Não foi identificado backend próprio de dados comerciais nesta revisão.
- O usuário aciona exportações/compartilhamentos; documentos e mensagens podem levar dados a destinos externos escolhidos por ele.
- O app usa Google Play Billing Library 9.1.0 para consultar `full_access`, compras e reconhecimento. Consultas ao Play podem ocorrer ao iniciar a Activity. O token de compra é usado pelo fluxo de reconhecimento; o estado de licença não integra o backup SQLite.
- FREE permanece limitado a 30 vendas; FULL_ACCESS é compra única. Backup/restauração são benefícios FULL_ACCESS. A declaração da Console não é derivável apenas de permissões ou do inventário do app.
- A política local/offline está incluída. A disponibilidade/paridade atuais da página pública não foram verificadas diretamente nesta fase.

### Evidência externa ainda necessária

- **Google Play Console:** status e respostas salvas de Segurança dos dados, categorias, tipos de dados, coleta/compartilhamento, finalidade, opcionalidade, criptografia em trânsito e mecanismo de exclusão; faixas e versões que continuam distribuídas. A Console não foi acessada.
- **Billing/SDK:** fonte oficial ou confirmação suficiente sobre eventos/payloads efetivos de Billing/Data Transport/CCT, dados técnicos e identificadores, destinatários, finalidade, transmissão automática/opcional, criptografia e retenção. O relatório 6C não encontrou uma ficha completa da versão 9.1.0 que resolva esses itens.
- **Política pública:** evidência atual de que a URL vinculada (`https://rg-carraro.github.io/vendas-simples-android/privacy/`) serve a revisão de 15/10/2026 e corresponde ao conteúdo local; a confirmação anterior do usuário é registrada, mas não foi reconfirmada nesta fase.
- **Proprietário do app:** confirmar as respostas da Console e as faixas ativas, revisar a classificação final de Billing e aprovar qualquer alteração no formulário. Não compartilhar tokens, dados de clientes ou capturas com identificadores pessoais.

Não afirmar conformidade completa nem alterar declarações da Console com base apenas nesta análise local.

## 6. Plano de homologação física

Registrar versão, aparelho/API, resultado, evidência sanitizada e eventuais defeitos. Não usar dados reais em testes destrutivos. No Galaxy S22+ com dados existentes, apenas comparar contagens/saldos e preservar previamente um backup confiável; testar rejeição de backup inválido em dispositivo ou instalação descartável com dados sintéticos.

### Samsung Galaxy S22+ — Android 16 / API 36

- [ ] Instalar a V12 pela faixa Play de teste como atualização da V11, sem sideload de APK debug.
- [ ] Comparar clientes, vendas, parcelas, pagamentos parciais, fotos/anexos, saldos e nome do negócio antes/depois.
- [ ] Criar/visualizar PDF, exportar CSV, abrir fluxo de compartilhamento WhatsApp e conferir caracteres e valores.
- [ ] Testar fluxos essenciais online e offline; reabrir o app e conferir persistência do nome/dados.
- [ ] Reiniciar o aparelho e conferir persistência novamente.
- [ ] Confirmar FREE até 30 vendas e bloqueio da 31ª; não tentar compra real sem conta/licença de teste configurada.

### Android API 24

- [ ] Instalar a versão distribuída de teste e iniciar sem crash.
- [ ] Criar dados sintéticos e conferir banco, cliente, venda, parcela e pagamento.
- [ ] Exercitar PDF/CSV/compartilhamento, permissões contextualizadas e operação offline.
- [ ] Registrar fabricante/modelo, versão/API e evidência sem dados pessoais.

### Billing em teste do Google Play

- [ ] Consultar o produto único `full_access` e confirmar disponibilidade/preço retornados pelo Play.
- [ ] Executar compra de teste aprovada e confirmar entitlement FULL_ACCESS e reconhecimento (acknowledgment).
- [ ] Fechar/reabrir e usar restauração de compras; confirmar recuperação do direito sem confundir com restauração de backup.
- [ ] Cobrir cancelamento/recusa, Billing indisponível e compra pendente, se oferecida pelo ambiente de teste; os dados existentes devem permanecer acessíveis.

### Backup e restauração — dados somente sintéticos

- [ ] Confirmar que backup e restauração são bloqueados no FREE e disponíveis em FULL_ACCESS.
- [ ] Criar backup FULL_ACCESS e restaurar em instalação descartável; comparar clientes, vendas, parcelas, pagamentos, fotos, saldos e nome do negócio.
- [ ] Testar backup antigo sem nome, nome com acentos/caracteres especiais e arquivo inválido/corrompido, quando os fixtures estiverem disponíveis.
- [ ] Em arquivo inválido ou falha, confirmar que banco, preferência do nome e estado FULL_ACCESS permanecem íntegros; licença não deve ser importada do backup.

## 7. Critérios para Alpha e produção

**Liberar Alpha para testadores:** confirmar pacote/versão/assinatura no resumo da Console; faixa e público corretos; inexistência de bloqueios; revisão responsável das declarações de Segurança dos dados; notas revisadas; AAB idêntico ao hash deste relatório. Após disponibilizar, executar os testes de update, Billing, API 24 e dados sintéticos e registrar evidências. A decisão de upload/disponibilização permanece fora desta fase.

**Considerar produção:** além dos critérios Alpha, concluir e documentar homologação física V11→V12 e API 24, cenários FULL_ACCESS/Billing/backup, revisão de toda a matriz Data Safety incluindo SDKs e versões ainda ativas, validação da política pública, tratamento dos alertas da Console, screenshots/ficha e aprovação operacional do responsável. A validação local do AAB não equivale à aprovação da Console nem à autorização de produção.

## 8. Próxima ação recomendada

O responsável deve revisar na Console o estado de Segurança dos dados, faixas/códigos ativos e disponibilidade do versionCode 12; reunir confirmação/documentação do fornecedor Billing para as lacunas da Fase 6C; e preparar um dispositivo/instalação descartável com dados sintéticos. Depois, pode iniciar a etapa operacional de upload Alpha com o AAB de hash confirmado, após autorização específica. O pacote não foi enviado.

## 9. Estado Git e limites desta fase

- Branch e HEAD permaneceram em `integration/v12-master-validation` / `64344855e377dadf5f6b3044cadde6b41e728459`.
- A tag de referência `v1.0.11-rc.1` permanece presente; não foi criada nem modificada tag.
- Os relatórios 7H e 7I já estavam não rastreados; este relatório também é apenas documental e não foi commitado. Nenhuma alteração rastreada de código foi feita.
- Não houve recompilação ou modificação do AAB, alteração de versão/keystore, merge adicional, commit, push, upload ou publicação.
