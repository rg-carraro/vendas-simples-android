# Fase 2 — segurança e conformidade (preparação para V12)

Data: 15/10/2026. Branch: `fix/v12-seguranca-conformidade`. Base limpa confirmada em `8aa34881755f222f9b090691aaa6127b31b1a363`; tag `v1.0.11-rc.1` aponta para essa base e permanece inalterada. Código ainda 11 / 1.0.11, somente para desenvolvimento/homologação. Não houve commit, push, tag, deploy ou publicação.

## A. Alterações e escopo

- `MainActivity.kt`, novo `PrivacyPolicy.kt` e `assets/privacy_policy.html`: acesso gratuito à política no menu, texto offline rolável/selecionável, botão Abrir site HTTPS, fallback quando não há navegador. Sem WebView ou permissão nova. Mensagens de backup passam a ser controladas, sem detalhes de SQL.
- Novo `BackupValidator.kt`: whitelist de schema e validação dos dados antes da substituição. `LocalDatabase.kt` e as regras financeiras não foram alterados.
- Novo `BackupTransfer.kt` e `BackupManager.kt`: cópia em fluxo com limites, espaço e verificação do cabeçalho. `RestoreTransaction.kt` foi preservado; journal e recuperação continuam em uso.
- Testes `BackupTransferTest.kt`, `PrivacyPolicyTest.kt`, `BackupInstrumentedTests.kt`, `FinalUiInstrumentedTests.kt`; ferramentas existentes aceitam diretório de evidência separado para não sobrescrever logs da V11.
- Política pública/local, documentação web, segurança e relatório de dados Play: práticas e limitações confirmadas. Não foram alterados contatos ou informações legais de identidade.

## B. R01 — Política

URL oficial: https://rg-carraro.github.io/vendas-simples-android/privacy/ . Em 15/10/2026, o usuário confirmou que esse é o endereço cadastrado na Play Console. Confirmação fornecida pelo responsável, sem acesso automatizado ao Console. A URL já coincide com PrivacyPolicy.officialUrl; foi acrescentada verificação explícita no teste unitário.

O menu sempre mostra Política de Privacidade, antes da condição FULL_ACCESS. O diálogo apresenta o texto local integral sem conexão; Abrir site usa ACTION_VIEW/CATEGORY_BROWSABLE e aceita HTTPS sem credenciais embutidas. Uma falha mantém a cópia local. Fechar e Voltar conservam a tela de origem.

A revisão do site contém exclusão, retenção de cópias, ausência de criptografia própria do backup, migração Android, entrega de cobrança a outro app e diagnóstico do Billing. O texto em assets corresponde ao corpo da política em site/privacy/index.html, com teste de sincronismo. **A revisão local ainda não foi publicada.**

Nova consulta HTTPS em 15/10/2026: HTTP 200, text/html; charset=utf-8, sem redirecionamento. A página pública ainda informa 02/10/2026 e **não corresponde integralmente à revisão local de 15/10/2026**. Faltam sete parágrafos de esclarecimentos da auditoria: backup sem criptografia própria; limitações da migração Android; diagnósticos/metadados Billing/CCT; entrega do texto de WhatsApp; exclusão de parcela/pagamentos/fotos; limpeza Android versus cópias externas; e retenção de cache/cópias privadas. Comparação detalhada e evidência em .gradle-user/v12-policy-public-check/ (response.json, public.html e comparison.json). Publicação continua pendente de autorização; confirmar a URL não resolve essa divergência nem conclui R03.

## C. R02 — Backup e restauração

Antes de substituir o banco, a cópia exata é aberta read-only, com query_only; trusted_schema é desativado onde suportado, sem depender dessa opção em SQLite antigo. A leitura de sqlite_master precede integrity_check e consultas de dados.

O schema esperado é obtido criando uma base SQLite somente em memória com o onCreate existente. A validação exige as tabelas e índices próprios, incluindo autoíndices de PK. Metadados Android têm formato específico permitido. Triggers, views (inclusive substituindo tabela conhecida), tabelas/índices desconhecidos, defaults, constraints ou colunas adulteradas são rejeitados. SQL recebido não é reaplicado como DDL. A enumeração interrompe na primeira entrada desconhecida, limitando o mapa aos nomes permitidos, e cada declaração de schema é limitada a 8192 caracteres antes de normalizar/ordenar. Esse limite não é aplicado aos dados comerciais; DDL produzido pelo app é muito menor.

A comparação permite a ordem de colunas diferente gerada pelo ALTER TABLE v2 -> schema financeiro 3. O histórico Git confirma que o formato 1 foi introduzido em d8e6fcf já com SCHEMA_VERSION=3, e o LocalDatabase desse commit é idêntico ao atual. Metadados de backup formatos 1 e 2 permanecem aceitos, com tipos/versões verificados sem truncar números. Nome ausente/vazio preserva o atual. Licença nunca participa da importação.

Validações de dados: tipos persistidos, IDs não vazios e IDs numéricos representáveis pelos consumidores atuais de vendas/pagamentos, clientes referenciados, pagamentos vinculados, fotos BLOB não vazias com grupo existente, datas ISO reais quando informadas (UTC para validar a data civil independentemente de horário de verão), posições/quantidades positivas dentro do intervalo Int consumido pelos modelos, posições duplicadas no mesmo grupo e somas inteiras sem overflow.

Cuidados de compatibilidade: id_venda_pai é agrupador, não FK para uma linha-pai. Não exigir todas as parcelas nem a primeira, pois exclusão individual é legítima. Edição individual pode deixar quantidades diferentes entre registros e não reescreve o grupo; não exigir igualdade artificial. Datas históricas nulas/vazias são preservadas; datas informadas devem ser válidas. Centavos armazenados negativos são rejeitados; **saldo negativo por pagamentos excedentes históricos não é rejeitado**. Valores REAL legados não são comparados aos centavos porque podem permanecer desatualizados após edição. Metadados de exclusão podem referenciar registros já removidos.

Limites: 512 MiB para o arquivo de backup e reserva de 16 MiB. Exportação também recusa cópia acima do limite para não gerar arquivo maior que o suportado na restauração. A importação verifica os bytes efetivamente lidos, mesmo sem tamanho informado pelo provedor, com buffer de 64 KiB. Antes da troca, exige espaço para cópia anterior e recuperação. A validação de fotos consulta tipo/comprimento/referência sem carregar BLOBs na memória. Temporários são removidos em finally.

O limite pode recusar uma base legítima acima de 512 MiB, com mensagem explícita, sem remover dados. Backups modificados por ferramentas externas com objetos extras são incompatíveis, mesmo se integrity_check passar. Não houve migração do banco operacional nem conversão automática de backups antigos.

## D. R03 — Billing

Ver [inventário, evidências, categorias candidatas e proposta para Segurança dos dados](SEGURANCA_DADOS_PLAY.md). Produto full_access, Billing 9.1.0, preço do Google Play, FREE 30 e FULL_ACCESS permanecem intactos. Não há promessa de ausência total de coleta, retenção inventada ou garantia universal de TLS. Campos específicos de diagnóstico/identificadores e enquadramento final ainda exigem confirmação.

## E. Testes e comandos

Resultados intermediários: a suíte Android confirmou 35 testes de backup + 1 em novo processo, depois 37 + 1 com casos adicionais, com banco e identidade operacionais preservados (v12-backup-confirm.log e v12-backup-final/). Uma primeira execução teve 1 falha de preparação do teste de ID inválido por colisão de chave primária; o teste foi corrigido para manter IDs inválidos distintos antes da repetição aprovada.

A primeira compilação ocorreu durante a edição do helper de privacidade e falhou por referência de teste ainda não disponível; as compilações completas seguintes passaram. Um ensaio de views foi abortado por falha do sistema do AVD, com serviço Activity indisponível e falhas em componentes Google/sistema. Não foi contado como aprovação. O finally do wrapper não conseguiu recuperar o serviço; a identidade original foi restaurada explicitamente e conferida byte a byte. O banco foi comparado ao baseline V11 preservado e permaneceu idêntico. Evidências em v12-views.log/views-normal.log; o AVD foi encerrado para recuperação.

Uma revisão acrescentou UTC ao validador de datas e teste positivo de 04/11/2018 em America/Sao_Paulo, para evitar rejeitar data civil por lacuna de horário de verão. Depois foram acrescentados limites à enumeração e ao parser de schema, com teste de DDL excessivo. A última mudança no APK foi o parágrafo factual sobre metadados CCT, sincronizado no site/asset. Evidências novas usam prefixo/diretórios v12; resultados históricos da V11 não homologam esta fase.

Resultados finais confirmados:

Complemento após confirmação da URL no Console: `gradlew.bat testDebugUnitTest --tests com.example.controlevendas.PrivacyPolicyTest --offline --no-daemon --max-workers=2 -Pkotlin.compiler.execution.strategy=in-process` aprovou os três testes focados (zero falhas/erros), BUILD SUCCESSFUL em 1m07s, log v12-policy-url-confirm-test.log. Verifica URL oficial, HTTPS/fallback e igualdade entre asset e revisão local do site; não afirma igualdade com a página remota. A nova comparação remota confirmou divergência, descrita em B. A implementação e o conteúdo local não precisaram mudar; não houve novo build de APK/AAB ou repetição da suíte completa neste complemento.

| Verificação | Resultado | Evidência local em .gradle-user/ |
| --- | --- | --- |
| Unitários debug | 43, zero falhas/erros | XML em app/build/test-results/testDebugUnitTest |
| Unitários release | 43, zero falhas/erros | XML em app/build/test-results/testReleaseUnitTest |
| test + assembleDebug + assembleDebugAndroidTest | BUILD SUCCESSFUL em 40s, após última atualização da política | v12-policy-final-build.log |
| Backup Android no APK final | 38 testes + 1 em novo processo, todos aprovados; banco e preferência operacionais preservados | v12-backup-delivery/main.txt, restart.txt e v12-backup-delivery.log |
| Política offline pelo menu no APK final | 7 verificações aprovadas, incluindo texto, botão, retorno e preservação | v12-privacy-delivery/results.json |
| Interface nativa | 12 testes em cada uma de quatro configurações: 48 aprovados | v12-views-final/views-*.log |
| Wrapper de responsividade e preservação | 6 verificações aprovadas | v12-views-final/results.json |
| Fluxo real de vendas, filtros, detalhes, fotos e cancelamento de pagamento | 10 verificações aprovadas | v12-sales-final/results.json |
| Leitura CSV por parser | 7 registros de 8 colunas, texto/datas/centavos preservados | v12-csv-confirm.log |

A regressão de 48 execuções de interface e o fluxo de vendas antecedem somente os últimos limites internos do validador e o parágrafo CCT da política; o código dessas telas permaneceu idêntico. A política e o backup são verificados novamente no APK final. A primeira abertura real da política nessa regressão foi impedida por uma janela de ANR do processo de sistema Messages; a evidência foi preservada, e a repetição aprovou sete verificações. A ferramenta trata apenas essa janela identificada, SystemUI/launcher já previstos, com espera limitada; não ignora ANR do Vendas Simples. Na rodada final, uma tentativa de backup começou antes do desbloqueio do usuário Android e não chegou à instrumentação; a repetição ocorreu após sys.boot_completed=1 e run-as disponível (v12-backup-before-unlock.log).

Ao encerrar, banco e identidade coincidem byte a byte com o baseline anterior à regressão, e Wi-Fi/dados móveis foram restaurados ao estado original: quatro verificações verdadeiras em v12-delivery-preservation.json. O AVD isolado foi encerrado. A captura politica-local.png final foi inspecionada: título, texto rolável e botões legíveis; isso é evidência automatizada com inspeção da captura, não homologação manual em celular. A ausência de navegador é coberta por falha injetada no teste unitário; não foi removido o navegador do AVD. Não foram realizadas compras reais nem testes manuais em aparelho físico.

Comando de desenvolvimento: `gradlew.bat test assembleDebug assembleDebugAndroidTest --offline --no-daemon --max-workers=2 -Pkotlin.compiler.execution.strategy=in-process`, com JDK/SDK/cache locais e heap Gradle de 1536 MiB para o host de 8 GB. Emulador executado separadamente da compilação. Não executar bundleRelease nem gerador de artefatos V11 nesta fase.

Comandos Android: `python tools/validar_backup_v11.py --adb <SDK>/platform-tools/adb.exe --serial emulator-5556 --output-dir .gradle-user/v12-backup-delivery`; `python tools/validar_ajustes_finais_v11.py --serial emulator-5556 --privacy-only --output-dir .gradle-user/v12-privacy-delivery`. As mesmas opções --views-only e --sales-only produziram as evidências de interface indicadas acima. CSV: `python tools/validar_csv_v11.py`.

Testes de tamanho, leitura indisponível e capacidade usam limites/streams/espaço injetados em namespace sintético, sem consumir centenas de MB nem encher o armazenamento do aparelho. Não equivalem a testes de falha física de disco/provedor remoto. Interrupção e rollback têm testes do journal existente.

## F. Regressão e limites

Preservados por escopo e diff: LocalDatabase, RestoreTransaction, modelos financeiros, Billing, identidade, CSV, PDFs, WhatsApp e telas homologadas (exceto opção de privacidade e mensagens de falha de backup). Nenhuma base real foi utilizada. UI/backup somente no AVD Build10 e armazenamento sintético separado.

API mínima 24: novos recursos usam APIs disponíveis nessa versão; Html.fromHtml com flags e Math.multiplyExact exigem exatamente API 24. PRAGMA trusted_schema desconhecido em SQLite antigo é ignorado; a whitelist continua obrigatória. Sem emulador API 24 instalado: execução real nessa API permanece pendente.

## G. Riscos remanescentes e lint

P0: concluir revisão das declarações reais do Console/diagnósticos de SDK; comparar o AAB enviado com a evidência local e publicar a política revisada após autorização. R01/R02 dependem dos resultados de homologação desta fase; não declarar produção aprovada.

P1: os formatos 1/2 não possuem assinatura/autenticação de origem; uma alteração coerente dos dados não pode ser distinguida de um backup legítimo somente por validação estrutural/semântica. Não se promete detectar toda adulteração de conteúdo, nem sanear automaticamente uma base já adulterada em versão anterior. Trabalho pesado de backup ainda usa callbacks da UI; limites reduzem consumo, mas não eliminam ANR em provedor lento. Exclusão/retensão, escopo FileProvider, CSV com fórmulas e recuperação/persistência do Billing continuam fora desta implementação. O validador verifica referência/tipo de fotos, não todos os codecs ou conteúdos de imagem.

P2: backup criptografado com recuperação planejada; fortalecimento comercial conforme modelo; monitoramento de dependências.

Lint histórico V11: 3 erros e 55 avisos por variante. NewApi: windowLightNavigationBar em styles.xml, API 27 contra minSdk 24. NotificationPermission: notify sem POST_NOTIFICATIONS, podendo impedir avisos; uma exceção nesse fluxo é capturada por carregarRelatorio e pode impedir seu callback. SuspiciousIndentation: indentação em etiquetaStatus, sem mudança de classificador nesta fase. Essas pendências não foram ocultadas por baseline/supressões.

O lint executado na Fase 2 inicialmente apontou quatro WrongConstant novos, por literais de tipo de Cursor. Foram corrigidos com Cursor.FIELD_TYPE_*; a repetição terminou somente com os **3 erros preexistentes e 56 avisos em debug**. O aviso adicional UsableSpace recomenda StorageManager/allocateBytes: o guard atual usa espaço físico de forma conservadora, compatível com API 24, sem apagar cache do usuário; essa evolução fica separada. Última execução: v12-parser-build-lint.log, BUILD FAILED em 4m51s por lint, com testes e APKs executados. As confirmações sem lint passaram, inclusive a última política; nenhuma supressão foi adicionada. Lint release não foi repetido nesta fase.

AGP 8.5.2 está abaixo do mínimo documentado 8.9.1 para API 36 ([tabela oficial](https://developer.android.com/build/releases/about-agp)). A execução anterior de builds não prova suporte oficial. Recomenda-se atualização planejada com regressão, separada desta fase; nenhuma ferramenta/dependência foi atualizada indiscriminadamente.

## H. Preparação para V12 e AAB original

Não gerar release até homologar as correções e revisar os P0 documentais externos. Uma V12 é recomendável após essa revisão; durante esta fase os códigos permanecem 11 / 1.0.11 e não devem ser enviados novamente à Play.

AAB V11 preservado: app/build/outputs/bundle/release/vendas-simples-v11.aab; 5.320.483 bytes; SHA-256 d751f51dc69ac2da2784d841374288e4456bd666e2a5237c2e91c1ee9d47aaba. A metadata embutida aponta 0a85fe0ae75c2da638d23df7284ac4536c8b5242 porque foi gerado da árvore de trabalho antes do commit 8aa3488, conforme relatório original. Logs do build/assinatura/testes anteriores existem em .gradle-user; isso não prova a identidade do upload Alpha. Conferir o arquivo enviado/registro do Console pelo responsável.

## I. Homologação e Git

- Instalar APK debug somente no ambiente de teste; atualização Store exige assinatura/distribuição adequadas.
- Conferir política no FREE/FULL, sem rede, fonte ampliada, Voltar/Fechar e Abrir site; testar navegador ausente.
- Restaurar backups legítimos 1/2 com parcelas restantes, pagamentos, fotos e nome; conferir IDs, valores, relatórios e reinício.
- Cancelar seleção/confirmação; rejeitar arquivos inválidos, observar mensagens e preservação dos dados.
- Homologar armazenamento/provedor indisponível e arquivos próximos do limite com dados sintéticos.
- Repetir em API 24 e aparelho recente; conferir compras reais separadamente.
- Revisar categorias e política publicada antes de preparar a versão 12.

Sem commit automático, sem nova tag e sem push/publicação. Alterações permanecem na branch de trabalho para revisão.

Arquivos alterados nesta fase (19):

- Aplicativo: app/src/main/java/com/example/controlevendas/{MainActivity,BackupManager,BackupValidator,BackupTransfer,PrivacyPolicy}.kt e app/src/main/assets/privacy_policy.html.
- Testes: app/src/test/java/com/example/controlevendas/{BackupTransferTest,PrivacyPolicyTest}.kt e app/src/androidTest/java/com/example/controlevendas/{BackupInstrumentedTests,FinalUiInstrumentedTests}.kt.
- Ferramentas: tools/validar_backup_v11.py e tools/validar_ajustes_finais_v11.py.
- Documentação/site: README.md, docs/FASE2_SEGURANCA_CONFORMIDADE.md, docs/SEGURANCA_DADOS_PLAY.md, docs/SEGURANCA_PRIVACIDADE.md, docs/WEB_GITHUB_PAGES.md, docs/DECISOES.md e site/privacy/index.html.

APK para homologação: app/build/outputs/apk/debug/app-debug.apk, assinatura debug. Não é artefato de publicação. O AAB release original não contém estas correções.
