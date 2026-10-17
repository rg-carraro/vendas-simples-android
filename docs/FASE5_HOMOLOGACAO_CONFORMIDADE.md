# Fase 5 — Política de Privacidade, homologação Android e Google Play

Data da execução: **16/10/2026**. Repositório `rg-carraro/vendas-simples-android`, branch `fix/v12-seguranca-conformidade`. Aplicativo `com.vendassimples.app`, versão de trabalho **11 / 1.0.11**. Esta fase preparou a política, repetiu a validação técnica e gerou um APK debug; não houve publicação, alteração da Play Console ou geração de AAB.

## 1. Resumo executivo

| Situação | Resultado |
| --- | --- |
| **APROVADO** | Build debug e APK de instrumentação; 43 testes unitários em cada variante; lint debug/release sem erros e sem mudança nos 66 avisos existentes; CSV; regressão automatizada no AVD API 35. |
| **APROVADO** | A política local de 15/10 está sincronizada com o texto offline incluído no aplicativo. Revisão do código não encontrou afirmações absolutas de ausência de tratamento por terceiros nem prazos de retenção inventados. |
| **APROVADO** | Preparado o diff de publicação limitado a `site/privacy/index.html`, compatível com o workflow GitHub Pages configurado para `master`. |
| **PENDENTE** | Homologação física, execução em API 24, atualização Play V11 → futura V12 e transações reais de Billing. Acessibilidade da URL pública em 16/10 e comparação HTTP integral não puderam ser confirmadas neste ambiente. |
| **PENDENTE** | Preenchimento final da Segurança dos dados: o Console, as respostas atuais e o AAB efetivamente enviado à faixa Alpha não estavam acessíveis para revisão. |
| **BLOQUEADO** | A checagem HTTP da URL pública foi bloqueada pelas restrições de rede desta sessão. Isso não é evidência de que a página esteja fora do ar. A última evidência local registrada, de 15/10, dizia HTTP 200 com a revisão antiga. |
| **DEPENDE DE AUTORIZAÇÃO** | Criar/mesclar PR ou enviar a alteração para `master`, disparando GitHub Pages. Alterações na Play Console também exigem autorização separada. |

Recomendação: **APTO PARA HOMOLOGAÇÃO TÉCNICA CONTROLADA, COM RESSALVAS**. Isso não conclui homologação física, conformidade declarada no Console nem aprovação para publicação Android.

## 2. Estado Git e preservação

No início e no fim, `HEAD` e a tag `v1.0.11-rc.1` apontavam para `8aa34881755f222f9b090691aaa6127b31b1a363`. A branch ativa era `fix/v12-seguranca-conformidade`; a árvore já continha alterações locais das Fases 2, 3 e 4. Elas foram preservadas. Não houve reset, checkout destrutivo, limpeza, commit, nova tag ou push.

O snapshot local de `master` e `origin/master` era `99f41e70984d1aa25cc9eca89cc5c6067f55f47b`. A leitura remota somente leitura do arquivo `master:site/privacy/index.html` confirmou o conteúdo de 02/10/2026. Nenhuma atualização da referência remota foi baixada nesta fase. A única alteração versionada desta fase é este relatório. APKs, logs, scripts de evidência e resultados ficaram em diretórios ignorados de `.gradle-user/fase5/`.

Versão, Billing 9.1.0, `full_access`, FREE 30, FULL_ACCESS, banco, política incorporada e arquivos AAB originais permaneceram preservados. O código das Fases 2 e 4 não foi alterado nesta fase.

## 3. Política local e pública

URL oficial informada pelo responsável: <https://rg-carraro.github.io/vendas-simples-android/privacy/>. A URL também coincide com `PrivacyPolicy.officialUrl`. A leitura remota de `master:site/privacy/index.html` retornou a revisão de 02/10/2026. O arquivo local `site/privacy/index.html` e a política offline `app/src/main/assets/privacy_policy.html` apresentam a revisão de 15/10/2026.

A tentativa de abrir diretamente a URL pública falhou por bloqueio de socket no ambiente PowerShell; a ferramenta de pesquisa também não abriu o domínio. Uma busca pelo endereço não retornou cópia indexada. Não se deve interpretar esses erros como HTTP 404 ou indisponibilidade do site. O último registro técnico anterior, de 15/10/2026, informou resposta HTTP 200, sem redirecionamento, `Content-Type: text/html; charset=utf-8`, mas conteúdo público ainda datado de 02/10. Portanto, em 16/10, a **disponibilidade e o conteúdo HTTP atual permanecem não verificados**. A fonte GitHub `master`, por outro lado, foi consultada e continua antiga.

### Revisão das afirmações locais

| Tema | Evidência observada | Avaliação |
| --- | --- | --- |
| Dados comerciais e nome do negócio | `LocalDatabase` e `BusinessPreferences` armazenam as informações localmente. Não há conta nem backend próprio para o conteúdo comercial. | Correto para o código do desenvolvedor. Não equivale a dizer que nenhum dado sai do aparelho: Billing usa serviços Google. |
| Uso offline | Vendas, dados e política local funcionam no aparelho. O teste de privacidade abriu a política com Wi-Fi e dados móveis desligados. | Correto como funcionamento offline-first. O APK mesclado possui `INTERNET` por dependências, além de comunicar com Billing; portanto o produto não deve ser descrito como totalmente sem rede. |
| Exportações e compartilhamento | CSV/PDF/backup são entregues por seleção/ação do usuário; a cobrança abre o texto para um destino externo e depende de confirmação do usuário para envio. | Compatível com o código. A exceção de compartilhamento iniciado pelo usuário no formulário Play deve ser aplicada caso a caso, conforme expectativa razoável. |
| Backup | O usuário escolhe arquivo/destino pelo Storage Access Framework. A cópia SQLite não possui criptografia própria ou senha. Há também fluxo de backup para compartilhamento que cria arquivo privado em `filesDir`. | O esclarecimento local é apropriado. Backups externos dependem do destino escolhido; a cópia em `filesDir` não tem prazo automático de remoção documentado. |
| Cache e cópias externas | PDF/CSV e outros arquivos de compartilhamento são criados em `cacheDir`; o Android pode limpar cache, sem prazo garantido. Aplicativos/destinos de compartilhamento podem manter suas cópias. | A política não promete exclusão automática nem apaga cópias externas. |
| Exclusão de vendas, pagamentos e fotos | `LocalDatabase` remove pagamentos vinculados à parcela; remove as fotos do grupo quando não há mais parcelas vinculadas. O cadastro do cliente pode permanecer. Remover foto do rascunho não apaga o original da galeria. | Texto corresponde ao comportamento encontrado. |
| Backup automático e migração Android | Manifesto final do APK tem `allowBackup=false`; a documentação Android ressalva diferenças de transferência entre fabricantes/versões. | A ressalva local evita prometer bloqueio universal de transferências do sistema. |
| Google Play Billing e diagnósticos | Billing 9.1.0 consulta produto/compra e reconhece compra. A investigação estática local encontrou transporte CCT para eventos técnicos e campos de build, aparelho e conexão. | A política usa “podem acompanhar” e qualifica dados disponíveis. Não houve captura de tráfego nem inventário completo do payload; isso não encerra a análise do SDK. |
| Contato do desenvolvedor | O HTML local contém `vendassimples.app@gmail.com`. | Endereço presente no conteúdo existente, mas a fase não confirmou propriedade, monitoramento ou validade da caixa postal. Confirmar antes de publicar; nenhum contato foi inventado ou editado aqui. |

O teste unitário `offlinePolicyMatchesLocalSiteRevisionAndContainsDeletionAndContact` compara o conteúdo de `<main>` do site com o asset offline e verifica os esclarecimentos essenciais. A execução atual passou. Isso confirma sincronismo **local**, não correspondência com a página publicada.

## 4. Plano isolado para publicação da política

O workflow `.github/workflows/pages.yml` publica a pasta `site` em GitHub Pages quando há push para `master` com alteração sob `site/**`; também existe `workflow_dispatch`. O destino é `master` e a raiz de publicação é `site/`.

O diff local preparado contra `master` afeta **somente** `site/privacy/index.html` (`9 inserções, 1 remoção`). Ele troca a data de 2 para 15 de outubro e acrescenta os esclarecimentos sobre: backup sem criptografia; limites da migração Android; eventos Billing/CCT; entrega do texto de cobrança; exclusão de parcelas/pagamentos/fotos; dados privados versus cópias externas; e retenção de cache/cópias privadas. Os arquivos Android, asset offline, builds, testes e documentos internos não fazem parte da publicação do Pages.

Como há alterações não commitadas em outro trabalho, a execução futura deve usar worktree isolado, depois de autorização e de confirmar o `master` remoto mais recente. Exemplo proposto, **não executado**:

```powershell
git fetch origin master
git worktree add -b docs/politica-privacidade-2026-10-15 ..\vendas-simples-politica origin/master
Set-Location ..\vendas-simples-politica
Copy-Item <RAIZ_REPOSITORIO>\site\privacy\index.html .\site\privacy\index.html
git diff --check
git diff --stat
git diff -- site/privacy/index.html
git add -- site/privacy/index.html
git commit -m "Atualiza política de privacidade"
git push -u origin docs/politica-privacidade-2026-10-15
```

Depois, abrir PR com destino `master`; a mesclagem aprovada deve ativar o workflow Pages. Antes de publicar, o diff do PR precisa continuar contendo só `site/privacy/index.html`. O exemplo de comandos é plano e não autoriza commit, push, merge ou deploy.

**Verificação após deploy:** confirmar a conclusão do workflow `Publicar site no GitHub Pages`; consultar a URL HTTPS e registrar código HTTP, destino final e `Content-Type`; baixar o HTML e verificar data de 15/10, todos os parágrafos novos, acentos e ausência do texto antigo; comparar o conteúdo de `<main>` com `site/privacy/index.html`. Repetir a abertura pelo link oficial da Play Console.

**Rollback:** criar uma reversão rastreável do commit da política por PR para `master`. A reversão do arquivo `site/privacy/index.html` deve disparar o mesmo workflow. Verificar novamente a URL HTTP e registrar que o conteúdo voltou ao estado anterior. Não usar reset/force push.

## 5. Testes automatizados e builds

Comando executado integralmente, com tarefas forçadas para não contar resultados `UP-TO-DATE` como reexecução:

```text
gradlew.bat test assembleDebug assembleDebugAndroidTest lintDebug lintRelease
  --offline --no-daemon --max-workers=1 --rerun-tasks
  -Pkotlin.compiler.execution.strategy=in-process
  -Dorg.gradle.jvmargs=-Xmx1536m -Dfile.encoding=UTF-8
```

Resultado: **BUILD SUCCESSFUL em 6m49s; 102 tarefas executadas**. Unitários debug: **43/43**, 0 falhas, 0 erros, 0 ignorados. Unitários release: **43/43**, 0 falhas, 0 erros, 0 ignorados. APK debug e APK de instrumentação foram gerados. `lintDebug` e `lintRelease`: **0 erros e 66 avisos em cada variante**. Comparação por ID, severidade, mensagem e arquivo com os relatórios da Fase 4 não encontrou avisos adicionados nem removidos. Os avisos de compilação Kotlin existentes sobre `Locale`, `SOFT_INPUT_ADJUST_RESIZE` e parâmetro não usado continuaram visíveis; não houve erro de compilação.

`python tools/validar_csv_v11.py`: aprovado — 7 registros, 8 colunas, UTF-8 com/sem BOM, textos intactos e datas/valores exatos.

### Regressão instrumentada

Ambiente: AVD **Build10**, Android 15 / API 35, x86_64, emulador isolado. Não é aparelho físico. A regressão usou a fixture sintética já existente e os wrappers de teste que conferem restauração de identidade, dados e configurações.

| Grupo | Resultado desta fase |
| --- | --- |
| Backup/compatibilidade/reinício | 38 testes + 1 teste em novo processo aprovados; banco e preferência preservados byte a byte. |
| Interface responsiva | 13 testes em cada configuração normal, 320 dp/fonte 150%, 320 dp/fonte 200% e paisagem/fonte 200%: **52 execuções aprovadas**. |
| Política local sem rede | **7 verificações aprovadas**. |
| Vendas, filtros, detalhes, fotos e cancelamento | **10 verificações aprovadas**. |
| Permissão de notificações | **13 testes com permissão negada + 13 com permissão concedida**; início sem prompt, ação explícita, emissão/ausência conforme cenário e retorno da permissão original aprovados. Dez verificações de fluxo/preservação aprovadas. |

São 78 execuções dos 13 testes nativos de interface em seis rodadas; não são 78 métodos diferentes. O estado inicial da permissão era negado, sem flags de escolha do usuário; Wi-Fi estava ligado e dados móveis desligados. Esses estados foram restaurados. Banco financeiro e preferência do negócio foram comparados byte a byte, e o AVD foi encerrado.

Na primeira tentativa instrumentada após a retomada, o runner terminou com `INSTRUMENTATION_ABORTED: System has crashed` durante a etapa de backup. Essa tentativa foi registrada como falha ambiental, não como teste aprovado. Banco e preferência continuaram iguais ao baseline; os rádios foram restaurados explicitamente. Após reboot, a repetição completa passou em todas as etapas acima. O log da primeira tentativa foi preservado separadamente.

O build validou API 24 como `minSdk`, compilação contra API 36 e `targetSdk 36`; execução de dispositivo ocorreu somente em API 35. Não houve execução em API 24, API 36 ou telefone físico nesta fase.

## 6. APK debug atualizado

| Campo | Valor verificado |
| --- | --- |
| Arquivo | `app/build/outputs/apk/debug/app-debug.apk` |
| Tamanho | 7.256.135 bytes |
| SHA-256 | `0E10A305DA88F32756733A1BF4E7C2BA3F90F5A4E9BB5CAAD01C09435B3BBCD2` |
| Assinatura | `apksigner verify` aprovado; certificado **Android Debug**, não é chave de upload Play. |
| Application ID | `com.vendassimples.app` |
| Versão | `versionCode 11`, `versionName 1.0.11` |
| SDK | `minSdk 24`, `compileSdk 36`, `targetSdk 36` |
| Permissões mescladas relevantes | `POST_NOTIFICATIONS`, `com.android.vending.BILLING`, `INTERNET`, `ACCESS_NETWORK_STATE`. `INTERNET` vem da configuração/dependências mescladas; não prova envio do banco comercial para servidor próprio. |
| Backup Android automático | `allowBackup=false` no manifesto empacotado. |

O APK é apropriado para homologação em ambiente isolado. **Não instalar por cima de uma cópia Play com dados importantes:** a assinatura debug não comprova compatibilidade de atualização com a chave Play. Para compra real, restauração de compra e atualização da faixa Alpha, usar o canal e o artefato assinado adequados.

Os AABs V11 históricos não foram reconstruídos ou sobrescritos. `vendas-simples-v11.aab` e `app-release.aab` continuam com SHA-256 `d751f51dc69ac2da2784d841374288e4456bd666e2a5237c2e91c1ee9d47aaba`. Nenhuma tarefa `bundleRelease` ou `assembleRelease` foi executada.

## 7. Checklist manual para homologação

Executar o Ambiente A em API 24 e o Ambiente B em aparelho físico Android recente (preferencialmente API 35/36). O APK debug desta fase serve para instalação limpa em dispositivo de teste sem dados; atualização pela Play exige artefato do canal Play. Em telefone com dados existentes, conferir assinatura e backup antes de substituir qualquer instalação. Registrar aparelho/modelo, versão Android, artefato, data, resultado e evidência. **Todos os itens abaixo continuam pendentes de homologação manual**, salvo as verificações automatizadas identificadas na seção 5.

| # | Cenário | Ambiente sugerido | Resultado manual |
|---:|---|---|---|
| 1 | Instalação limpa, sem dados anteriores | A e B | ☐ Pendente |
| 2 | Abrir o aplicativo e navegar na tela inicial | A e B | ☐ Pendente |
| 3 | Cadastrar cliente com dados sintéticos | A e B | ☐ Pendente |
| 4 | Cadastrar uma venda | A e B | ☐ Pendente |
| 5 | Criar venda à vista | A e B | ☐ Pendente |
| 6 | Criar venda parcelada e conferir parcelas | A e B | ☐ Pendente |
| 7 | Registrar pagamento parcial e conferir saldo | A e B | ☐ Pendente |
| 8 | Editar venda e conferir valores/parcelas | A e B | ☐ Pendente |
| 9 | Excluir venda e conferir pagamentos/fotos relacionados | A e B | ☐ Pendente |
| 10 | Conferir valores pagos, faltantes e relatórios | A e B | ☐ Pendente |
| 11 | Anexar imagem pela galeria/Photo Picker | A e B | ☐ Pendente |
| 12 | Capturar foto com a câmera | B físico | ☐ Pendente |
| 13 | Abrir/ampliar a foto vinculada à venda | A e B | ☐ Pendente |
| 14 | Remover foto do rascunho e confirmar que o original não foi apagado | A e B | ☐ Pendente |
| 15 | Gerar e abrir PDF financeiro/de venda | A e B | ☐ Pendente |
| 16 | Exportar CSV com acentos e abrir no leitor disponível | A e B | ☐ Pendente |
| 17 | Abrir cobrança no WhatsApp, revisar e cancelar; enviar apenas mensagem de teste autorizada | B físico | ☐ Pendente |
| 18 | Personalizar e salvar o nome do negócio | A e B | ☐ Pendente |
| 19 | Confirmar nome personalizado em PDF e mensagem de cobrança | B físico | ☐ Pendente |
| 20 | Abrir a política local com modo avião/rede desligada | A e B | ☐ Pendente |
| 21 | Abrir a URL oficial online e conferir data/conteúdo após publicação | B com rede | ☐ Pendente |
| 22 | Criar backup completo com FULL_ACCESS em destino escolhido | B / conta de teste Play | ☐ Pendente |
| 23 | Restaurar backup legítimo; conferir clientes, parcelas, pagamentos, fotos, nome e licença | B / aparelho de teste | ☐ Pendente |
| 24 | Restaurar arquivo inválido e conferir que dados/preferência não mudaram | B / fixture sintética | ☐ Pendente |
| 25 | Fechar e reabrir o app; conferir persistência | A e B | ☐ Pendente |
| 26 | Trabalhar offline e confirmar que os dados locais continuam acessíveis | A e B | ☐ Pendente |
| 27 | Com FREE, criar vendas até atingir 30 | Ambiente de teste isolado | ☐ Pendente |
| 28 | Tentar a 31ª venda em FREE e confirmar bloqueio sem perder histórico | Ambiente de teste isolado | ☐ Pendente |
| 29 | Testar compra FULL_ACCESS pela faixa Play com conta licenciada | Faixa de teste Play | ☐ Pendente |
| 30 | Testar recuperação de compra FULL_ACCESS pela Google Play | Faixa de teste Play | ☐ Pendente |

**Atualização V11 → futura V12:** pendente em cenário separado. Requer artefato distribuído pelo canal apropriado e compatibilidade de assinatura confirmada. Comparar clientes, vendas, parcelas, pagamentos, fotos, nome e entitlement antes/depois. Não usar o APK debug para atualizar instalação com dados importantes.

## 8. Guia de preparação — Segurança dos dados na Play Console

Este guia propõe como revisar o formulário; **não é uma resposta final nem foi enviada ao Console**. A política do Google define coleta como transmissão de dados do app para fora do dispositivo, inclui transmissões feitas por SDKs, e trata transferências para outro app no mesmo aparelho como compartilhamento, ressalvada a ação específica iniciada pelo usuário quando ele espera razoavelmente o compartilhamento. Os dados processados só no dispositivo não são coleta para esse formulário. A ficha é global e precisa cobrir as versões que continuam distribuídas, inclusive faixas fechadas. Ver [orientação oficial da Segurança dos dados](https://support.google.com/googleplay/android-developer/answer/10787469?hl=pt-BR) e [Política de Dados do Usuário](https://support.google.com/googleplay/android-developer/answer/10144311?hl=pt-BR).

| Categoria/caso | Proposta de resposta | Evidência e fonte | Confiança | Confirmação manual necessária |
|---|---|---|---|---|
| Clientes, vendas, valores, parcelas, pagamentos, fotos e nome do negócio | Não marcar como coleta pelo código próprio quando apenas armazenados/processados localmente. | `LocalDatabase`, `BusinessPreferences`; definição oficial exclui processamento exclusivamente no aparelho. | Alta para o código próprio da revisão; incompleta para o conjunto de versões/SDKs. | Conferir todas as versões atualmente distribuídas e o AAB Alpha real. |
| Backend, conta e login próprios | Não identificado no aplicativo. | Inventário de código e dependências em `docs/SEGURANCA_DADOS_PLAY.md`; não há serviço próprio de contas/dados comerciais. | Alta para a revisão local. | Confirmar que Console e todas as versões ativas não incluem outra integração. |
| Internet e Billing | Não responder “app sem rede”. O APK mesclado declara `INTERNET`; Billing comunica com Google Play. | `aapt dump permissions` no APK debug e integração Billing 9.1.0. A [documentação Billing](https://developer.android.com/google/play/billing/integrate) descreve a conexão. | Alta quanto à permissão/integração; payload completo não determinado. | Conferir versão Alpha e documentação específica da biblioteca/fornecedor. |
| Diagnóstico técnico do Billing/CCT | Tratar “Diagnóstico” em Informações e desempenho do app como categoria candidata a coleta. Não afirmar compartilhamento, finalidade ou retenção final sem mapear a transmissão. | Inspeção estática da biblioteca resolvida no relatório de segurança identifica eventos CCT e campos técnicos; a política local descreve-os como possíveis. A fonte geral do Google exige considerar SDKs, mas [o disclosure de Play services](https://developers.google.com/android/guides/play-data-disclosure) cobre somente SDKs listados ali, não o Billing. | Média para existência do mecanismo e campos observados; baixa para cada evento/payload enviado em execução. | Confirmar com a documentação/fornecedor do Billing 9.1.0 e, quando viável, captura controlada do payload/destino. Determinar finalidade, obrigatoriedade, compartilhamento e retenção. |
| Identificadores do dispositivo | Não selecionar “Device or other IDs” apenas por `Build.FINGERPRINT`, modelo ou fabricante. A auditoria não mostrou que esse valor identifique persistentemente um aparelho ou seja vinculado a pessoa. | Inventário estático em `docs/SEGURANCA_DADOS_PLAY.md`; definições oficiais de tipos no formulário Google. | Baixa/inconclusiva quanto à categoria final. | Identificar todos os campos do protobuf/evento Billing e se algum é ID persistente ou vinculável. |
| Histórico de compras / estado da Licença Completa | Candidato a revisão; Billing consulta compras e a app usa o resultado para liberar funcionalidade. Não confundir histórico de compras com número de cartão. | `PlayBillingGateway`, SKU `full_access`, consulta e reconhecimento de compra; FAQ oficial orienta analisar separadamente histórico de compras mesmo em integração de pagamento. | Média quanto ao acesso funcional; incompleta quanto ao enquadramento do formulário. | Confirmar quais dados são acessados/transmitidos na versão Play e responder conforme a FAQ de pagamentos. |
| Número de cartão e credenciais de pagamento | A análise do app não encontrou acesso próprio a número de cartão. A exceção de plataforma de pagamento pode dispensar declarar os dados que o serviço coleta diretamente somente se o app nunca os acessa e a coleta é regida pelos termos do serviço. | Billing abre o fluxo Play; [FAQ de pagamentos na orientação oficial](https://support.google.com/googleplay/android-developer/answer/10787469?hl=pt-BR) descreve as duas condições. | Alta para ausência de campo de cartão no código próprio; média para a aplicação completa da exceção. | Conferir a integração e não ampliar a exceção para diagnóstico ou histórico de compras. |
| PDF, CSV, backup e texto de cobrança entregues a outro app | Avaliar a exceção de ação iniciada pelo usuário para cada fluxo. Se uma transferência não satisfizer a exceção, declarar os tipos de conteúdo enviados e o destino/finalidade conforme formulário. | Intents/SAF/`wa.me`, chooser e confirmação explícita; a orientação Google inclui transferência entre apps e descreve exceção de ação do usuário. | Alta sobre o fluxo; média sobre enquadramento da exceção por destino. | Validar cada tipo de compartilhamento e expectativa do usuário. Não tratar a exceção como automática para qualquer Intent. |
| Analytics, publicidade, localização, contatos e dados de crash pelo código próprio | Nenhuma integração própria correspondente foi identificada nesta revisão; não usar isso para concluir ausência em SDKs, sistema ou versões anteriores. | Dependências, manifesto e buscas de código; não há SDK próprio de Analytics/Crashlytics ou permissão de localização no manifesto próprio. | Alta para ausência de integração explícita no código analisado; limitada ao pacote revisado. | Revisar manifestos mesclados/AAB Alpha e inventário integral dos SDKs ativos. |
| Criptografia em trânsito | Não declarar “todos os dados criptografados em trânsito” com as evidências atuais. HTTPS observado em alguns caminhos não comprova toda transmissão ou aplicativo de destino. | Billing/CCT e destinos externos; ausência de captura/inventário total; política oficial pergunta pela proteção de todos os dados coletados. | Alta quanto à insuficiência de evidência. | Confirmar cada canal de dados que venha a ser declarado como coletado/compartilhado. |
| Exclusão/solicitação de exclusão | Descrever exclusão local e limites de cópias conforme implementação; não marcar mecanismo de solicitação externa sem definir processo efetivo de atendimento. | Funções de exclusão no `LocalDatabase`, limpeza via Android, cache privado e política local. | Alta para os fluxos locais; média para o enquadramento da pergunta do Console. | Confirmar se o e-mail da política é monitorado e se há processo de atendimento/apagamento aplicável aos dados transmitidos por SDK. |
| Obrigatório/opcional e finalidade | Dados comerciais são inseridos pelo usuário para a funcionalidade local; Billing atende compra/licença; diagnóstico Billing é uma finalidade técnica candidata, sem classificar como marketing/analytics genérico por inferência. | Telas, `PlayBillingGateway`, código SDK e finalidades oficiais da seção. | Média. | Inspecionar formulário atual, SDKs, todas as versões e respostas existentes no Console. |

O Google também informa que aplicativos publicados em faixas fechadas/abertas precisam preencher Segurança dos dados; o formulário não se restringe ao APK novo. Como a V11 foi informada como distribuída em Alpha, as práticas da versão realmente enviada precisam ser incluídas. A identidade do AAB local e seu hash não comprovam qual arquivo foi enviado. A [exceção de pagamento](https://support.google.com/googleplay/android-developer/answer/10787469?hl=pt-BR) não isenta automaticamente diagnósticos, identificadores ou histórico de compras.

### Perguntas para revisão manual do Console

1. Quais respostas estão atualmente salvas/publicadas, por tipo de dado, coleta, compartilhamento, finalidade, opcionalidade, criptografia e exclusão?
2. Quais AABs permanecem ativos em produção, teste aberto, fechado/Alpha e outros países? Conferir o bundle enviado e assinatura/hash.
3. Para Billing 9.1.0: quais campos saem do aparelho, para quais serviços, com quais finalidades e retenção? O fornecedor/documentação específica confirma a análise estática?
4. O formulário trata a consulta/estado de compra como histórico de compras? Cartão permanece inteiramente no fluxo Play e inacessível ao código do app?
5. O e-mail de privacidade da política está ativo e há processo real para pedidos pertinentes?
6. A resposta de criptografia em trânsito corresponde a todos os dados que forem declarados, incluindo SDKs e serviços externos?

Não salvar nem enviar declarações sem revisão do responsável. A Google afirma que o desenvolvedor responde pela exatidão do formulário; revisão automatizada da Play não substitui evidência do próprio app.

## 9. Pendências externas

- **Política pública:** conferir HTTP e conteúdo atual em 16/10 e, após autorização de publicação, repetir validação completa pós-deploy.
- **Play Console:** acesso e captura das declarações atuais, categorias/versões ativas, política cadastrada, destinatários e status de análise não disponíveis.
- **Billing:** fechar inventário de dados do SDK 9.1.0 e finalidade/retenção/compartilhamento com evidência do fornecedor ou captura controlada. Nenhuma captura foi feita nesta fase.
- **AAB Alpha:** comparar o bundle realmente enviado com o arquivo local e o certificado/hash. AABs locais preservados não provam identidade do upload.
- **Homologação:** telefone físico, API 24, Android recente físico, compra e recuperação FULL_ACCESS Play, SAF real, câmera/galeria e atualização Play V11 → V12.
- **Contato:** confirmar que `vendassimples.app@gmail.com` é o canal correto, ativo e monitorado antes de publicar a revisão local.

## 10. Riscos remanescentes

1. A URL oficial pode continuar exibindo texto antigo ou estar temporariamente inacessível; situação HTTP atual não foi medida nesta sessão.
2. A declaração global da Play depende de todas as versões ainda distribuídas e do conteúdo real da faixa Alpha, que não foram consultados.
3. A biblioteca Billing envia eventos técnicos conforme mecanismos verificados estaticamente, porém payload completo, destinatários de todos os eventos, associação e retenção não foram determinados. Não afirmar que o app não coleta dados.
4. O APK debug é assinado com certificado Android Debug e não valida atualização sobre V11 Play nem compras reais.
5. Testes runtime cobriram API 35 num AVD; API 24/36 e dispositivos físicos seguem pendentes.
6. A primeira execução instrumentada abortou por crash do sistema Android do AVD. Reboot e repetição passaram; manter o evento inicial registrado.
7. Limitações e ressalvas das Fases 2/4 continuam válidas, incluindo KGP fora da matriz de suporte plenamente testada documentada e limitações operacionais conhecidas do backup. Esta fase não as alterou.

## 11. Próximas ações recomendadas

1. Homologar checklist em API 24 e aparelho recente, começando por instalação de teste e sem substituir instalação Play com dados importantes.
2. Confirmar AAB Alpha e examinar as respostas reais da Play Console com o responsável.
3. Concluir revisão específica dos metadados Billing 9.1.0; atualizar a matriz proposta da seção 8 com a evidência.
4. Confirmar o endereço de contato.
5. Após aprovação do conteúdo e autorização explícita, publicar apenas `site/privacy/index.html` pela rota revisada na seção 4 e validar HTTP/conteúdo pós-deploy.
6. Tratar atualização V11 → futura V12, versão Android, assinatura e eventual publicação em tarefa separada, com autorização própria.

## 12. Estado Git final

`HEAD` e `v1.0.11-rc.1` continuam em `8aa34881755f222f9b090691aaa6127b31b1a363`; `versionCode 11` / `versionName 1.0.11` preservados. O APK debug foi criado em `app/build/outputs/apk/debug/app-debug.apk`; os AABs V11 mantiveram o SHA histórico. As alterações locais preexistentes das Fases 2–4 permanecem intactas. O relatório desta fase é o único novo arquivo versionado. Não houve commit, push, tag, alteração do site publicado ou Console, build AAB, publicação Android ou declaração de homologação física concluída.

**Próxima ação que depende de autorização:** publicar a revisão da política para `master` por PR e permitir o deploy GitHub Pages. Qualquer edição/envio da Segurança dos dados no Play Console ou publicação de uma versão Android requer autorização independente.
