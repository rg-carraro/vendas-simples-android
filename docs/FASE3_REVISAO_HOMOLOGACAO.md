# Fase 3 — revisão de segurança e preparação da homologação V12

Data: 15/10/2026 (America/Sao_Paulo). Revisão do estado local, sem alteração de código de produção, testes ou configuração do aplicativo. Os resultados desta fase são separados das evidências anteriores. Não houve commit, push, tag, publicação de site/Play, incremento de versão ou geração de AAB.

## 1. Resumo executivo e critérios

Recomendação: **APTO para iniciar homologação controlada**, com dados sintéticos e APK debug. **NÃO APTO para publicar ou gerar a candidata V12 neste estado.** A revisão não substitui homologação física, validação API 24 ou transações reais do Google Play. A aptidão para iniciar testes não significa aprovação dos fluxos ainda pendentes.

| Área | Classificação | Motivo / condição |
| --- | --- | --- |
| R01 — implementação local | APROVADA COM RESSALVAS | Menu gratuito, asset offline, URL oficial e fallback; falha de rede da página é tratada pelo navegador, com a cópia local preservada |
| R01 — política publicada | BLOQUEADA | HTTP 200, mas revisão pública de 02/10 difere da revisão local de 15/10; publicar somente após autorização |
| R02 — implementação | APROVADA COM RESSALVAS | Validação anterior à substituição, compatibilidade 1/2 e recuperação; limites de disponibilidade/autenticidade descritos abaixo |
| R03 — investigação | APROVADA COM RESSALVAS | Evidências estáticas e fontes oficiais sustentam a investigação, não uma declaração final do fornecedor |
| R03 — formulário de Segurança dos dados | BLOQUEADA | Categorias completas, finalidades do fornecedor, retenção e enquadramento ainda PENDENTES DE CONFIRMAÇÃO |
| Regressão automatizada | APROVADA COM RESSALVAS | Suítes repetidas nesta fase aprovadas; limites de cobertura explícitos na seção 6 |
| API 24, aparelho recente, atualização Play e compras | PENDENTE DE TESTE MANUAL | Checklist de 25 cenários na seção 8 |
| Lint completo / ferramentas de release | BLOQUEADA | Três erros existentes; AGP abaixo do mínimo oficial para API 36 |
| Integridade local do AAB V11 | APROVADA | Hash original, assinatura criptográfica e 929 hashes internos conferidos novamente |
| Correspondência do AAB com Alpha / chave de upload | BLOQUEADA | Evidência do Console/arquivo enviado ainda ausente |

## 2. Estado inicial do Git e método de revisão

Branch ativa: `fix/v12-seguranca-conformidade`. HEAD e tag anotada `v1.0.11-rc.1` resolvem para `8aa34881755f222f9b090691aaa6127b31b1a363`. A árvore **não estava limpa**: continha os 19 arquivos da Fase 2 e da confirmação posterior da URL, todos sem commit. Esse estado é esperado pelo pedido da Fase 3 e foi preservado. Não houve checkout, reset, clean, stash ou manipulação do índice.

Alterações existentes: README; BackupManager/MainActivity; novos BackupValidator, BackupTransfer, PrivacyPolicy e asset da política; dois testes JVM novos e duas classes instrumentadas ampliadas; dois wrappers Python; política em site/privacy/index.html; DECISOES, SEGURANCA_PRIVACIDADE, WEB_GITHUB_PAGES e os dois relatórios da Fase 2. A lista completa está em [Fase 2](FASE2_SEGURANCA_CONFORMIDADE.md). A revisão desta fase acrescenta este relatório e seu link no README.

Foram lidos AGENTS, README, decisões, arquitetura, produto, monetização, validação, regras financeiras, releases, os dois relatórios integrais da Fase 2 e as skills de segurança, backup, validação, monetização, regressão, interface e release. Inspecionados histórico Git, diff rastreado completo e conteúdo dos arquivos novos. A referência AleJoias não foi acessada nem alterada.

Evidências novas em `.gradle-user/fase3/`, ignoradas pelo Git: `initial-status.txt`, `initial-files.json` (hashes de 105 arquivos), `history.txt` e `phase2-tracked.diff`. O diff Git comum não inclui arquivos novos sem rastreamento; estes também foram lidos e incluídos no snapshot. A revisão é independente da conclusão anterior no sentido de conferir novamente o código e as evidências; não houve segundo revisor humano ou auditoria externa.

## 3. R01 — política dentro e fora do aplicativo

`MainActivity.kt:310` adiciona a política antes da condição FULL_ACCESS (`:318`). O diálogo em `:421` lê o asset UTF-8, converte HTML em texto selecionável e rolável, sem WebView/scripts/requisição de rede. Está disponível nos estados FREE, FULL e indisponível. Fechar/Voltar conservam a tela. O teste nativo configura os três estados; a automação pelo menu comprova o acesso real em offline, sem simular compra.

`PrivacyPolicy.officialUrl` é exatamente **https://rg-carraro.github.io/vendas-simples-android/privacy/**, confirmada pelo usuário como cadastrada no Console. Abertura externa por ACTION_VIEW/CATEGORY_BROWSABLE, somente HTTPS com host e sem credenciais na URI. A constante não vem do arquivo de backup nem de entrada do usuário. Exceção ao iniciar navegador mantém o diálogo e informa a falha. Ausência de navegador é testada por falha injetada, não por remoção do navegador do aparelho.

**Limite confirmado:** sucesso de startActivity significa entrega ao navegador, não conclusão do acesso HTTP. Falha de conexão, DNS ou resposta remota fica sob responsabilidade do navegador; o texto local já está aberto e permanece disponível ao voltar. Não há indicador próprio de download nem dependência de conectividade para consultar a política. Não é necessário introduzir monitoramento de rede para preservar esse acesso.

Consulta nova por HTTPS em 15/10/2026: status **200**, sem redirecionamento, `text/html; charset=utf-8`; SHA-256 do HTML público `3e43a5406cda9fdcd79897735ee3d19302264c9e8f555612636fb15e56403e89`. Evidências: `public-policy.html` e `public-policy-response.json`.

A página pública ainda informa **02/10/2026**. O corpo de `site/privacy/index.html` e `app/src/main/assets/privacy_policy.html` coincide integralmente, revisão **15/10/2026**. A página remota não corresponde a essa revisão. Faltam sete parágrafos: backup sem criptografia própria; limites da migração Android; diagnósticos/metadados Billing/CCT; entrega do texto ao WhatsApp/navegador; efeitos de excluir venda/pagamentos/fotos; limpeza Android versus cópias externas; retenção de cache/cópias privadas. Contato e identidade legal continuam iguais. A divergência é bloqueador de conformidade antes da produção; não se resolve alterando somente a URL.

## 4. R02 — revisão das proteções de backup

### Caminho e garantias verificadas

1. `BackupManager` exige FULL_ACCESS antes de criar/restaurar e não inclui licença, token ou estado Google Play no arquivo. A inspeção prévia `validate` não concede licença nem substitui dados.
2. Cópia real do provedor em fluxo, buffer de 64 KiB, cabeçalho SQLite, mínimo de 100 bytes, máximo de 512 MiB e reserva de 16 MiB; tamanho informado pelo provedor não é usado como garantia. Cópia sincronizada antes de validar.
3. Arquivo privado aberto read-only; query_only ativado e trusted_schema desativado onde suportado. O schema esperado vem de `LocalDatabase.onCreate` executado **em memória**, não de DDL importado aplicado ao banco operacional.
4. Lista fechada de seis tabelas financeiras/legadas, seus autoíndices e três índices explícitos; metadados de backup obrigatórios, android_metadata opcional em formato específico. Objetos desconhecidos, triggers, views, substituição de tabela por view e índices adicionais são recusados. Enumeração para no primeiro nome desconhecido; normalização limita cada declaração a 8192 caracteres.
5. Tipos/nomes/tabela associada e assinatura do DDL são comparados antes de integrity_check/consultas. Constraints/defaults/colunas extras incompatíveis são rejeitados. Não há execução deliberada de triggers, views ou SQL fornecido como comando pelo backup. SQLite ainda precisa abrir/analisar o arquivo: isso não elimina vulnerabilidades do motor da plataforma.
6. Verificação de schema 3, uma única linha de metadados, versões inteiras 1/2 sem truncamento. Datas presentes ISO válidas, validação civil em UTC; tipos, IDs canônicos representáveis, cliente/venda de pagamento, fotos por grupo, posições positivas no intervalo Int, duplicatas de posição e somas sem overflow inteiro.
7. Validação da cópia exata usada pela restauração antes de fechar/substituir a base. Reserva adicional de duas vezes o tamanho atual para rollback/recuperação. Journal de `RestoreTransaction`, fsync, recuperação antes de abrir SQLite e preferência com commit síncrono preservados.

### Compatibilidade e regras financeiras

Mantidos formatos 1/2 e schema financeiro 3; diferenças de ordem de colunas do ALTER v2 -> v3 são permitidas. O histórico confirma que o primeiro backup formato 1 (d8e6fcf) já usava schema 3. Isso não significa aceitar qualquer SQLite antigo sem metadados ou schema 1/2 bruto.

`id_venda_pai` é agrupador: não se exige uma parcela-pai existente, primeira parcela ou grupo completo. Exclusão/edição individual pode produzir posições restantes, quantidades diferentes e posição superior à quantidade editada. Datas históricas nulas/vazias são aceitas. Saldos negativos decorrentes de pagamentos excedentes são preservados; centavos armazenados negativos são recusados. REAL legado pode estar desatualizado após edição; o validador não o força a coincidir com centavos. IDs de exclusões legadas podem apontar a registros removidos. Não houve mudança de schema, cálculos, filtros ou migração operacional.

### Lacunas e riscos residuais

- **P1 — disponibilidade:** restauração/validação continuam síncronas em callbacks da UI. O limite de arquivo não limita duração de consultas, latência do provedor, número de registros nem custo de decodificação de imagens/textos posteriormente carregados. BLOB é validado por tipo, tamanho não zero e referência, sem conferir codec/dimensões. Não prometer proteção absoluta contra DoS, ANR ou OOM. Propor execução fora da UI e política de limites de imagem/texto após levantamento de dados legítimos, sem impor limites novos nesta revisão.
- **P1 — autenticidade:** formatos 1/2 não possuem assinatura/MAC. Alteração coerente de dados não é distinguível de um backup legítimo só por schema/integridade. Criptografia/senha e autenticação exigem projeto de compatibilidade/recuperação separado. Não declarar todo arquivo adulterado detectável.
- **P1 — alcance:** o novo validador não saneia automaticamente um banco já importado por versões anteriores. Não há corpus de todas as versões históricas, apenas fixtures suportadas e histórico inspecionado. API 24 ainda precisa execução.
- **P1 — falha de escrita externa:** falha/cancelamento do provedor pode deixar arquivo exportado incompleto; isso não deve ser confundido com perda do banco operacional. Reimportação deve rejeitar o arquivo. Testar SAF real e armazenamento próximo do limite.
- **P2 — limites deliberados:** bases legítimas acima de 512 MiB e backups modificados externamente com objetos extras são recusados. A mensagem é explícita; dados operacionais permanecem. Falhas de energia/hardware e durabilidade do filesystem não são simuladas integralmente.

Não foi identificada nesta revisão uma nova falha comprovada que permita aos casos adversariais cobertos substituir o banco. A conclusão é restrita ao código inspecionado e aos testes, não uma certificação de ausência de vulnerabilidades.

## 5. R03 — Billing e matriz para Segurança dos dados

Reconfirmados `com.android.billingclient:billing:9.1.0`, produto `full_access`, tipo INAPP, opção `full-access`, preço via ProductDetails, compra única e ausência de assinatura. `PlayBillingGateway` inicializa o cliente, consulta produto/compras e reconhece compras; não houve mudança na Fase 2. Os testes locais de entitlement não comprovam compras/restauração reais.

O manifesto extraído novamente do **AAB V11 original** contém BILLING, INTERNET, ACCESS_NETWORK_STATE e permissão interna signature de receiver. Não contém localização, contatos, armazenamento amplo, CAMERA ou POST_NOTIFICATIONS. Câmera/galeria são delegadas por Intent. Launcher exportado; FileProvider/Startup, proxies Billing, GoogleApiActivity e serviços/receiver Data Transport não exportados. ProfileInstallReceiver é exportado com proteção DUMP; serviço JobInfoScheduler exige BIND_JOB_SERVICE. Essa inspeção é do AAB histórico; a futura V12 deve repetir a conferência no seu artefato final.

Revisados POM/modelo de dependências, manifesto e evidência de bytecode da Fase 2. Data Transport API 3.0.0 e runtime/backend CCT 3.1.8 integram a resolução do Billing. O logger `PLAY_BILLING_LIBRARY` tem caminho para Transport.send, inicializado antes de uma compra. CCT adiciona informações de versão/build, modelo/fabricante, idioma/região configurados, rede/operadora quando disponíveis. Build.FINGERPRINT não prova identificador único de aparelho; região configurada não prova GPS; MCC/MNC não é IMEI/IMSI. Não há captura de tráfego nem inventário completo dos protobufs/retensão do fornecedor. Não inferir Advertising ID ou ausência de qualquer identificador desconhecido.

| Item | Evidência | Resposta recomendada | Confiança | Ação pendente |
| --- | --- | --- | --- | --- |
| Dados coletados | Banco comercial privado sem backend próprio; consultas Billing e caminho CCT de envio técnico | Não marcar globalmente “nenhuma coleta”. Dados só locais não são coleta; diagnósticos e dados recebidos de compras exigem avaliação | Alta para código próprio; PENDENTE DE CONFIRMAÇÃO para inventário externo completo | Confirmar payload/identificadores, demais versões ativas e respostas atuais do Console |
| Dados compartilhados | Intents PDF/CSV/backup/WhatsApp e SDK Google | Avaliar separadamente cada transferência e eventual exceção de ação específica do usuário/provedor de serviço | Alta para caminhos; PENDENTE DE CONFIRMAÇÃO para enquadramento final | Não aplicar exceção de pagamentos a todo o SDK; validar expectativa do usuário e papéis do fornecedor |
| Finalidades | Consulta de produto/compra/reconhecimento; eventos técnicos do logger | Funcionalidade/cobrança evidenciadas; finalidade final dos diagnósticos PENDENTE DE CONFIRMAÇÃO. Não presumir publicidade, analytics ou fraude | Alta para chamadas; parcial para fornecedor | Obter declaração aplicável ao SDK/versão antes de preencher categorias/finalidades |
| Criptografia em trânsito | URL da política e endpoint padrão CCT HTTPS | Não responder “todos os dados criptografados” apenas com essa evidência | Configuração estática confirmada; garantia completa PENDENTE DE CONFIRMAÇÃO | Verificar demais fluxos/destinos e práticas do SDK; backup SQLite não tem criptografia própria |
| Exclusão e retenção | Exclusão local de parcelas/pagamentos/fotos; Android limpa dados privados; cópias externas persistem | Descrever limites locais e externos, sem prazo inventado | Alta para código local; retenção Google PENDENTE DE CONFIRMAÇÃO | Confirmar práticas e direitos perante fornecedor; publicar texto revisado |
| Diagnósticos | Logger Billing e metadados CCT; inicialização sem compra concluída | Categoria candidata: informações do app/desempenho — diagnósticos. Não afirmar ausência de coleta nem coleta opcional só porque comprar é opcional | Caminho estático confirmado; emissão/payload completo PENDENTE DE CONFIRMAÇÃO | Confirmar dados enviados, obrigatoriedade, identificadores e finalidade; observação controlada se viável |

A exceção oficial para informações de pagamento depende de o aplicativo não acessar os dados e de coleta direta pelo prestador sob seus termos. Não foi encontrado acesso próprio a cartão/endereço de faturamento; isso não exclui histórico/token/diagnósticos. Transferência a outro app também pode ser compartilhamento; exceções precisam análise, não suposição. A URL cadastrada foi confirmada pelo usuário; o formulário atual não foi disponibilizado, portanto não há comparação comprovada com suas respostas atuais.

Fontes oficiais reconsultadas: [Segurança dos dados](https://support.google.com/googleplay/android-developer/answer/10787469?hl=pt-BR), [notas Billing 9.1.0](https://developer.android.com/google/play/billing/release-notes), [segurança Billing](https://developer.android.com/google/play/billing/security) e [disclosure Play services](https://developers.google.com/android/guides/play-data-disclosure). As notas confirmam versão/disponibilidade, não um inventário completo de privacidade. Não extrapolar disclosure dos SDKs core listados para todo o Billing/Data Transport. Inventário detalhado em [SEGURANCA_DADOS_PLAY.md](SEGURANCA_DADOS_PLAY.md).

## 6. Execuções e cobertura de regressão

Comando executado: `gradlew.bat test assembleDebug assembleDebugAndroidTest lintDebug lintRelease --continue --offline --no-daemon --max-workers=2 -Pkotlin.compiler.execution.strategy=in-process`, heap Gradle 1536 MiB, JDK do Android Studio, SDK/cache locais. Não foi solicitado assembleRelease ou bundleRelease.

Resultado global: **BUILD FAILED em 3m23s exclusivamente por lintDebug/lintRelease**, com os três erros existentes em cada variante. Testes e tarefas de APK debug/androidTest concluíram. Não chamar esse comando de build integral aprovado. Log `build-tests-lint.log`; XMLs e relatórios de lint preservados em fase3.

| Verificação desta fase | Resultado | Evidência |
| --- | --- | --- |
| Unitários debug | 43, zero falhas/erros/skips | app/build/test-results/testDebugUnitTest |
| Unitários release | 43, zero falhas/erros/skips | app/build/test-results/testReleaseUnitTest |
| APK debug e APK instrumentado | Tarefas concluídas; sem novo AAB | build-tests-lint.log |
| Lint debug/release | 3 erros e 56 avisos em cada variante | after-lint-debug.xml / after-lint-release.xml |
| Backup e reinício Android | 38 + 1 testes aprovados, 102,8 s | backup/main.txt, backup/restart.txt e backup.log |
| Interface nativa em quatro configurações | 12 testes por configuração, 48 execuções aprovadas; 170,2 s | views/views-*.log |
| Wrapper de interface | 6 verificações aprovadas, incluindo preservação | views/results.json |
| Política offline pelo menu | 7 verificações aprovadas, 46,1 s | privacy/results.json |
| Vendas, pesquisa/filtros, detalhes/fotos e cancelamento de pagamento | 10 verificações aprovadas, 116,6 s | sales/results.json |
| CSV por parser independente | 7 registros, 8 colunas, textos/datas/valores exatos, UTF-8 com/sem BOM | csv.log |

Comandos Android: `python tools/validar_backup_v11.py --adb <SDK>/platform-tools/adb.exe --serial emulator-5556 --output-dir .gradle-user/fase3/backup`; `python tools/validar_ajustes_finais_v11.py --serial emulator-5556 --views-only --output-dir .gradle-user/fase3/views`, repetido com --privacy-only/privacy e --sales-only/sales. CSV: `python tools/validar_csv_v11.py`. Orquestração local em run_android.py/android.log, durações e saídas em android-runs.json. Emulador Build10, Android 15/API 35, x86_64, 2048 MiB, dois núcleos, separado da compilação; rede temporariamente desativada para os testes offline.

Os quatro grupos concluíram sem falhas. O wrapper de vendas registrou aviso transitório de UIAutomator “null root node”; a obtenção seguinte de árvore prosseguiu e as dez verificações passaram, sem alterar critérios de aprovação. Não houve ANR do aplicativo reportado nessa rodada. Comparação final comprovou banco e identidade byte a byte iguais ao baseline, inclusive ao baseline anterior da Fase 2, e Wi-Fi/dados móveis restaurados: quatro verificações verdadeiras em preservation.json. O AVD foi encerrado ao concluir. Não foram usadas evidências antigas para completar a contagem desta fase.

Os 43 JVM cobrem dinheiro/parcelamento, formato de backup, identidade/datas, FREE 30/estados locais Billing, journal/rollback/interrupção simulada, CSV, percentuais e transferência/política. O teste de interrupção usa erro simulado no journal; não é corte físico de energia. A suíte nativa de backup cobre formatos 1/2, nova instalação, acentos, parcelas/pagamentos/fotos/IDs/nome, licença-sentinela e bloqueio FREE; rejeição de objetos SQL, órfãos, tipos/valores/datas/IDs, corrupção, limite/espaço/leitura e preservação do banco/preferência. O runner usa namespace sintético separado.

Inspeção comparativa confirmou 10 arquivos protegidos idênticos ao commit 8aa3488 após normalizar somente CRLF: configuração de versão/manifesto/Gradle, LocalDatabase, RestoreTransaction, PlayBillingGateway, BusinessPreferences, BusinessIdentity e SalesCsvExporter (`protected-files.json`). O diff de MainActivity limita-se ao menu/diálogo de privacidade e mensagens de backup: cálculos, cadastros, edição/exclusão, histórico, filtros, financeiro, fotos, PDF/CSV e WhatsApp não tiveram suas implementações alteradas pela Fase 2. Preservação por diff não equivale a testar cada fluxo por UI.

Compras, reconhecimento real, restauração Play, câmera/galeria externas, todos os PDFs/WhatsApp e atualização assinada pela Play permanecem manuais. Os testes de fotos usam BLOB sintético no backup e imagens sintéticas na UI; preservar bytes não comprova todos os codecs. Nenhuma base real foi utilizada. O SDK instalado oferece imagens API 35 e 37; não existe imagem API 24 instalada. Não houve download de SDK ou homologação física nesta revisão.

## 7. Lint, avisos e compatibilidade de ferramentas

| Erro | Local atual | Causa e risco real | API 24 | Proposta mínima, não implementada | Risco de regressão / validação |
| --- | --- | --- | --- | --- | --- |
| NewApi | app/src/main/res/values/styles.xml:7 | windowLightNavigationBar pertence à API 27 e está no recurso base. Atributo incompatível, inclusive risco de conflito em customizações OEM; lint não comprova crash em todo aparelho | Não há suporte ao atributo; comportamento visual/inflação precisa teste | Mover somente esse item para override values-v27, preservando o tema base | Baixo: cor/contraste da barra em APIs 24/26/27/recente e tema atual |
| NotificationPermission | MainActivity.kt:2300, chamada em carregarRelatorio:2310 | notify sem POST_NOTIFICATIONS no manifesto/fluxo runtime. API 33+ pode bloquear avisos; se houver exceção, o catch de carregarRelatorio captura e deixa de chamar aoFinalizar, podendo manter tela/status indevidos | Não existe permissão runtime de notificação nessa API; o fluxo legado precisa permanecer | Guardar emissão com checagem de permissão em API 33+ e isolar falha da notificação da leitura/callback. Decidir separadamente UX para declarar/solicitar permissão e preservar o recurso de alertas | Médio: permissão concedida/negada, instalação/upgrade, vencidos, callback e telas em API 24/33+; não suprimir a regra |
| SuspiciousIndentation | MainActivity.kt:718, anterior :717 | setTextColor desalinhado após typeface dentro de apply. Ambos são incondicionais; não foi encontrada condição oculta mudando o status | Mesmo Kotlin/bytecode; risco é legibilidade/manutenção | Apenas alinhar setTextColor/when ao nível correto, mantendo expressão e cores | Muito baixo: repetir estados/cores quitado, vencido, parcial e aberto |

Os três trechos existem no commit auditado; linhas históricas eram 683 e 2265 antes da inserção da política. Foram identificados por XML, diff e leitura, não apenas pela contagem. Nenhuma correção/supressão/baseline foi aplicada. Para notificações, a proposta não afirma que toda chamada sem permissão lança SecurityException; o bloqueio de exibição é documentado, e o efeito sobre callback depende de ocorrer exceção. Fonte: [permissão de notificações Android](https://developer.android.com/develop/ui/compose/notifications/notification-permission).

Comparação nova do XML release V11 preservado (3/55) com a execução atual (3/56), por ID/mensagem/arquivo sem depender do deslocamento das linhas: **um aviso novo, UsableSpace em BackupManager.kt:18; nenhum removido**. A Fase 2 debug já tinha 3/56; esta revisão não acrescentou avisos.

| Avisos atuais por variante | Quantidade |
| --- | --- |
| SetTextI18n | 39 |
| IconLauncherShape | 5 |
| ViewConstructor | 3 |
| UseCompatTextViewDrawableApis | 2 |
| MonochromeLauncherIcon | 2 |
| ExifInterface, DataExtractionRules, DrawAllocation, ObsoleteSdkInt, UsableSpace | 1 de cada (5) |
| Total | 56 |

UsableSpace sugere StorageManager.getAllocatableBytes/allocateBytes (API 26+), que considera cache liberável. O guard atual de espaço físico é conservador e compatível com API 24; pode recusar operação que funcionaria após liberação de cache. Proposta: avaliar APIs 26+ com fallback, em alteração separada, sem remover o guard nem limpar dados do usuário. DataExtractionRules permanece relevante para migração D2D; política descreve a limitação, sem prometer bloqueio universal. Demais avisos não foram reclassificados como seguros só por serem antigos.

AGP **8.5.2**, Gradle **8.7**, Kotlin **1.9.24**, compileSdk/targetSdk **36**, minSdk **24**. A [tabela oficial do AGP](https://developer.android.com/build/releases/about-agp) exige **AGP 8.9.1 para API 36**; AGP 8.9 requer Gradle 8.11.1. Sucesso dos testes não substitui suporte oficial. Estratégia proposta: autorizar branch/commit técnico separado, escolher patch estável compatível ao menos 8.9.1, atualizar wrapper correspondente e confirmar JDK suportado, sem salto automático para AGP 9/Kotlin novo. Repetir lint, ambos os testes JVM, debug, instrumentação API 24/recente e futura validação de release/AAB/assinatura, mantendo schema/Billing. Não reduzir targetSdk para mascarar o problema. Nada foi atualizado nesta fase.

## 8. Checklist manual — dispositivos A e B

A = API 24, emulador ou físico dedicado. B = Android recente, preferencialmente físico. Registrar fabricante/modelo, API, versão do app, assinatura/canal, data, responsável e evidência com dados sintéticos. Cada célula começa **NÃO EXECUTADO**; preencher PASSOU/FALHOU com evidência. Resultados anteriores do Alpha/V10/V11 não preenchem este checklist automaticamente.

| ID | Cenário e conferência | A / API 24 | B / recente |
| --- | --- | --- | --- |
| M01 | Instalação limpa em ambiente de teste: vazio, FREE, política acessível sem login | NÃO EXECUTADO | NÃO EXECUTADO |
| M02 | Atualização sobre V11 com assinatura compatível: comparar clientes, compras, parcelas, pagamentos, fotos, nome e licença | NÃO EXECUTADO | NÃO EXECUTADO |
| M03 | Cadastrar cliente com acentos/nome longo, consultar e reabrir | NÃO EXECUTADO | NÃO EXECUTADO |
| M04 | Venda à vista R$ 49,90, conferir card/histórico/financeiro | NÃO EXECUTADO | NÃO EXECUTADO |
| M05 | Venda R$ 100,00 em três parcelas: 33,33 + 33,33 + 33,34, datas e vínculos | NÃO EXECUTADO | NÃO EXECUTADO |
| M06 | Pagamento parcial R$ 10,00 em uma parcela; outra permanece intacta, totais e saldo consistentes | NÃO EXECUTADO | NÃO EXECUTADO |
| M07 | Editar/excluir venda/parcela e corrigir pagamento; confirmar efeitos, fotos e ausência de órfãos | NÃO EXECUTADO | NÃO EXECUTADO |
| M08 | Câmera: capturar, cancelar e negar acesso no app externo; sem registro parcial | NÃO EXECUTADO | NÃO EXECUTADO |
| M09 | Galeria: selecionar/cancelar fotos, tipos usuais e rotação | NÃO EXECUTADO | NÃO EXECUTADO |
| M10 | Ampliar/remover fotos, conferir todas as parcelas e persistência | NÃO EXECUTADO | NÃO EXECUTADO |
| M11 | CSV padrão/Excel, acentos, aspas, ;, quebra de linha, oito colunas e centavos; Planilhas e Excel por caminho real de abertura | NÃO EXECUTADO | NÃO EXECUTADO |
| M12 | Três PDFs, fotos, nome longo, totais/datas e cancelamento/compartilhamento | NÃO EXECUTADO | NÃO EXECUTADO |
| M13 | WhatsApp: cobrança parcial/quitada, valores/assinatura; cancelar, navegador/app ausente | NÃO EXECUTADO | NÃO EXECUTADO |
| M14 | Nome no FREE/FULL, acentos/120 caracteres, editar/apagar/reabrir, PDF/WhatsApp | NÃO EXECUTADO | NÃO EXECUTADO |
| M15 | Política local sem rede em FREE/FULL/indisponível, rolar, fonte ampliada, Voltar/Fechar | NÃO EXECUTADO | NÃO EXECUTADO |
| M16 | Abrir site: HTTPS oficial, revisão correta após publicação autorizada, erro de rede/navegador ausente e retorno ao texto local | NÃO EXECUTADO | NÃO EXECUTADO |
| M17 | FULL: criar backup com fotos/pagamentos/nome pelo SAF; cancelar e destino indisponível | NÃO EXECUTADO | NÃO EXECUTADO |
| M18 | Restaurar formatos 1/2 em nova instalação e com nome atual; comparar IDs, registros, totais e BLOBs | NÃO EXECUTADO | NÃO EXECUTADO |
| M19 | Recusar backup não SQLite, corrompido, versão incompatível e trigger/view sintéticos | NÃO EXECUTADO | NÃO EXECUTADO |
| M20 | Após falha de leitura/espaço/restauração: banco, nome e licença anteriores íntegros; temporários/recuperação | NÃO EXECUTADO | NÃO EXECUTADO |
| M21 | Conta de teste Play: preço retornado, compra FULL, cancelada/pendente/recusada; FREE bloqueia 31ª, mantém CSV/pagamentos e bloqueia backup | NÃO EXECUTADO | NÃO EXECUTADO |
| M22 | Reconhecer/recuperar compra pela Play, múltiplas contas quando aplicável; falha não apaga dados | NÃO EXECUTADO | NÃO EXECUTADO |
| M23 | Encerrar/reabrir aplicativo após nome, venda, pagamento e restauração; persistência | NÃO EXECUTADO | NÃO EXECUTADO |
| M24 | Offline: consultas, cadastro permitido, pagamentos, relatórios, fotos e política; falha Billing não apaga dados | NÃO EXECUTADO | NÃO EXECUTADO |
| M25 | Após atualização Play futura, repetir comparação e fechamento/reabertura; registrar certificado/versão e totais antes/depois | NÃO EXECUTADO | NÃO EXECUTADO |

Em B, adicionar 320 dp quando disponível, fonte 150%/200%, paisagem e teclado: cards/financeiro/cliente/política sem sobreposição. Trocar mês e combinar filtros/pesquisa; comparar indicadores com registros da mesma população. Sem ajustar artificialmente percentuais/saldos. Na homologação de M06/M07, conferir também histórico e relatório por cliente.

### Instalação e logs mínimos

APK existente: `app/build/outputs/apk/debug/app-debug.apk`, assinatura **debug**, versão 11/1.0.11. Conferir serial e usar dispositivo dedicado sem dados reais. Comandos para o responsável, não executados em aparelho físico nesta fase:

```powershell
adb devices -l
adb -s <SERIAL_DE_TESTE> shell getprop ro.build.version.sdk
adb -s <SERIAL_DE_TESTE> install -r app/build/outputs/apk/debug/app-debug.apk
```

Não usar uninstall, pm clear ou wipe para resolver atualização sobre dados que precisam ser preservados. APK debug não atualiza instalação Play assinada por chave diferente; INSTALL_FAILED_UPDATE_INCOMPATIBLE é conflito de assinatura, não motivo para apagar a instalação. M02/M25 com V11 da loja exigem futura versão de teste com assinatura de distribuição compatível, após autorização. Compras reais requerem conta/configuração de teste e canal Google Play apropriados; APK debug/entitlement simulado não comprovam isso.

Reproduzir somente com fixture sintética, sem contas pessoais ou registros de clientes reais. Exemplo de coleta mínima durante a reprodução (interromper com Ctrl+C):

```powershell
$processoTeste = (adb -s <SERIAL_DE_TESTE> shell pidof -s com.vendassimples.app).Trim()
# Prosseguir somente se o PID acima existir e corresponder ao app de teste.
adb -s <SERIAL_DE_TESTE> logcat --pid=$processoTeste -v brief 'AndroidRuntime:E' '*:S' |
    Select-String 'FATAL EXCEPTION|at com\.example\.controlevendas\.' |
    Set-Content logs-minimos-homologacao.txt -Encoding UTF8
```

Esse filtro preserva marcador de falha/frames próprios e omite mensagens de exceção, SQL e valores. Não captura todos os problemas (ANR, UI, Billing). Registrar passos/resultado e captura anonimizada; revisar mesmo o arquivo filtrado antes de compartilhar. Não anexar logcat completo, bugreport, banco, backup, XML de preferências, tokens/IDs de compra ou fotos reais. Não habilitar logging detalhado de Billing para contas pessoais.

## 9. GitHub Pages — publicação isolada proposta

Observação do repositório: `.github/workflows/pages.yml` publica o diretório **site/**. Gatilho automático: push em **master** que modifique site/** ou o próprio workflow. Existe também workflow_dispatch. Usa checkout@v4, configure-pages@v5, upload-pages-artifact@v3 com path site e deploy-pages@v4, ambiente github-pages. Não compila nem publica Android. `site/privacy/index.html` corresponde a `/vendas-simples-android/privacy/`; index/styles compartilhados devem ser preservados.

Isso identifica a configuração versionada e a branch de gatilho, não comprova a configuração efetiva de Settings → Pages ou o SHA do último deploy remoto. Consulta pública `/repos/rg-carraro/vendas-simples-android/pages` retornou 404 sem autenticação: não prova ausência de Pages. A página pública 200 comprova disponibilidade. Configuração/remoto e permissões do ambiente devem ser conferidos pelo responsável antes da publicação.

Procedimento futuro, **não executado**, condicionado a autorização específica de commit/push/merge/deploy:

1. Aprovar o texto revisado e conferir Settings → Pages, workflow e proteção do ambiente. Registrar SHA de master remoto e cópia/hash da página pública anterior para eventual reversão.
2. Atualizar a referência remota somente quando autorizado e abrir checkout/worktree isolado a partir de master remoto confirmado; manter a árvore atual da Fase 2 intacta, sem stash/reset. Branch proposta: docs/politica-privacidade-2026-10-15.
3. Copiar **somente** o arquivo revisado site/privacy/index.html. Conferir diff contra a master escolhida, preservando alterações remotas legítimas e todos os demais arquivos do site. Não copiar app/, docs/, keystore, AAB, configurações Android nem todo o diff da Fase 2.
4. Validar HTML/UTF-8, contato, links e texto; `git diff --check`. Stage explícito `git add -- site/privacy/index.html`; `git diff --cached --name-only` deve listar exatamente esse arquivo. Revisar `git diff --cached`; não usar git add . ou cherry-pick do futuro commit Android misto.
5. Após autorização, commit isolado e push dessa branch para revisão por PR contra master. Merge deve ter autorização que inclua deploy: o push resultante em master dispara Pages automaticamente. Não habilitar auto-merge nem acionar workflow_dispatch antecipadamente.
6. Acompanhar deploy e conferir URL sem cache: HTTP 200, data 15/10 e igualdade do corpo com o asset local. Conferir também início/CSS/navegação do site. Só então marcar divergência resolvida. Reversão, se necessária, por commit que reverta somente a política após aprovação, preservando master e o Android.

É tecnicamente possível publicar a política isoladamente. O site **inteiro** é enviado pelo workflow; o isolamento é garantido pela base limpa/atual e pelo diff de um arquivo, não por supor que deploy publica apenas o HTML alterado. Nenhuma etapa de publicação foi iniciada.

## 10. Rastreabilidade do AAB V11

Arquivo preservado: `app/build/outputs/bundle/release/vendas-simples-v11.aab`, cópia idêntica de app-release.aab. **5.320.483 bytes**, SHA-256 **d751f51dc69ac2da2784d841374288e4456bd666e2a5237c2e91c1ee9d47aaba**.

Nova leitura do manifesto por bundletool confirmou com.vendassimples.app, 11/1.0.11; minSdk 24/targetSdk 36. Jarsigner retornou 0 e jar verified; verificação independente confirmou os **929 hashes de conteúdo** cobertos pelo manifesto assinado. Certificado autoassinado/cadeia Java, ausência de timestamp e diferença JarFile/JarInputStream continuam ressalvas documentadas, não equivalem a falha criptográfica detectada. Correspondência com chave de upload Play não foi confirmada.

Metadata interna de controle de versão: **0a85fe0ae75c2da638d23df7284ac4536c8b5242**. Histórico e logs `v11-sales-final-build-lint.log`, `v11-sales-final-confirm-build.log`, `v11-artifact.json` e [RELEASE_V11](RELEASE_V11.md) registram geração da árvore local antes do commit final 8aa3488; por isso HEAD anterior no bundle. Isso explica o descompasso, mas não prova sozinho que todos os bytes derivam de uma árvore limpa reproduzível. Não reescrever metadata ou substituir o arquivo histórico.

Não existe evidência verificável fornecida do arquivo efetivamente enviado à Alpha. Comparar o AAB original do upload com esse SHA e conferir versão/certificado no Console. APK baixado/instalado pela Play tem transformação/assinatura de distribuição e não deve ter seu hash comparado diretamente ao AAB. Se só houver APKs gerados, inspecionar pacote/versão/certificado/conteúdo com suas limitações, sem declarar igualdade binária. Evidência nova em v11-artifact-review.json, v11-manifest.xml e v11-signature.log. Nenhum AAB novo foi gerado.

## 11. Pendências priorizadas e decisões

| Prioridade / natureza | Pendência | Ação proposta / responsável |
| --- | --- | --- |
| P0 conformidade | Política pública desatualizada | Responsável aprovar texto e autorizar publicação isolada; depois conferir corpo remoto |
| P0 conformidade | R03 e formulário atual incompletos | Responsável confirmar fornecedor/categorias e respostas reais do Console; revisar matriz sem afirmações absolutas |
| P1 publicação | Lint completo reprovado | Autorizar correções mínimas separadas da revisão, repetir lint/regressão e não criar baseline genérico |
| P1 publicação | AGP sem suporte oficial a API 36 | Autorizar atualização planejada de AGP/wrapper, com regressão; não efetuar atualização indiscriminada |
| P1 rastreabilidade | Identidade do upload Alpha e certificado não comprovados | Responsável fornecer/comparar arquivo original e registros do Console |
| P1 segurança/estabilidade | Disponibilidade na restauração síncrona, imagens/textos volumosos e provedor lento | Homologar casos sintéticos, projetar execução fora da UI/limites compatíveis, não prometer blindagem completa |
| P1 validação operacional | API 24, celular recente, atualização e compras reais | Executar checklist e anexar evidências antes da candidata |
| P1 futuro a avaliar | Escopo FileProvider, retenção, fórmulas CSV e persistência/recuperação Billing | Permanecem fora das correções desta fase; priorizar por ameaça/impacto, não tratá-los como resolvidos |
| P2 evolução | Autenticidade/criptografia do backup e limites históricos | Projeto versionado com migração/recuperação; sem mudança de regra financeira |

Não foi encontrado novo bloqueador P0 de execução nos caminhos cobertos. Os bloqueadores P0 de conformidade continuam abertos. Lint e compatibilidade são bloqueadores do fluxo de candidata; não impedem iniciar testes debug controlados. A necessidade de priorizar disponibilidade/recursos além das proteções atuais deve ser decidida antes do release com base na homologação e no perfil de uso.

## 12. Plano V12 — proposta, não execução

1. Responsável revisar/aprovar diff da Fase 2 e este relatório; decidir correções de lint/ferramentas e demais riscos. Manter mudanças separadas e rastreáveis.
2. Resolver bloqueadores técnicos aprovados, repetir testes de segurança/regressão e lint completo. Registrar ambiente/falhas, sem reinterpretar testes antigos como novos.
3. Aprovar/publicar somente política pelo procedimento isolado e concluir revisão das respostas de Segurança dos dados. Confirmar demais versões distribuídas.
4. Executar homologação manual A/B e compras em canal/configuração apropriados. Conferir preservação de dados/licença, falhas e atualização com assinatura correta.
5. Somente após autorização, criar commits locais das mudanças aprovadas com staging explícito e árvore revisável. Não incluir credenciais, artefatos, logs ou dados. A revisão atual permanece sem commit.
6. Em alteração controlada posterior, propor versionCode **12**, versionName **1.0.12**, verificar disponibilidade no Console e repetir testes/builds autorizados. Versão atual permanece **11/1.0.11**.
7. Com autorização de release, gerar AAB assinado pelo mecanismo local, validar assinatura/certificado de upload, pacote, SDKs, permissões/componentes e hash. Registrar SHA do commit limpo e origem do artefato; não sobrescrever a V11.
8. Testar o AAB por ferramenta/canal apropriados e smoke tests; criar tag candidata apenas com aprovação explícita e evidências aprovadas. Não mover v1.0.11-rc.1.
9. Somente com autorização de upload/publicação, enviar à Alpha, registrar hash/versão/resultado e validar atualização instalada pela Play sobre V11. Monitorar falhas/regressões e permitir interrupção da distribuição se necessário.

**Próximo comando de preparação recomendado:** um novo pedido de autorização delimitado, por exemplo: “Autorize corrigir os três erros de lint e planejar/aplicar a atualização mínima de AGP/Gradle em commits separados, mantendo 11/1.0.11, sem push, tag, AAB ou publicação; depois repetir a regressão.” Publicar a política e mudar a versão exigem decisões separadas. Antes disso, iniciar a homologação debug pelo checklist é permitido; gerar/publicar V12 não é.

## 13. Estado final e limites da entrega

Este relatório e o link no README são as únicas alterações da Fase 3. A comparação de hashes contra o snapshot inicial confirmou que todos os arquivos existentes de produção, testes, ferramentas, configuração, política/site e documentação anterior da Fase 2 permanecem iguais; somente README mudou e este relatório foi criado. Versionamento 11/1.0.11, HEAD e tag original preservados. Os dois AABs originais permanecem com o SHA d751f51dc69ac2da2784d841374288e4456bd666e2a5237c2e91c1ee9d47aaba. Estado final: árvore ainda com as alterações anteriores não commitadas e os dois documentos desta revisão; sem alteração do índice. Verificação final em final-preservation.json; diff e links locais conferidos. Sem commit, push, tag, AAB novo ou publicação.
