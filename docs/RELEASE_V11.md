# Release versão 11 — personalização

Data: 11/10/2026. Solicitação: pedido anexado pelo usuário. Base: marco V10 beta da Store, branch `master`, árvore limpa no início. Sem push, merge ou upload no Play Console.

## Melhorias implementadas

- Preferência `businessName` local, normalizada, até 120 caracteres e acentos preservados. Primeiro acesso à funcionalidade oferece configuração uma vez, inclusive em atualização V10; Agora não, Voltar ou fechamento não repetem o convite. Configurações no menu principal permitem editar/remover o nome também no FREE.
- Cabeçalho usa o nome, mantendo o slogan e o fallback Vendas Simples. Na UI o título tem até duas linhas com reticências para preservar espaço em tela estreita/fonte ampliada; o nome completo permanece nas Configurações, assinatura e PDF. Persistência em SharedPreferences independente do SQLite e da licença.
- Cobrança WhatsApp mantém valores, parcelas e saldos existentes; agradecimento usa uma única assinatura com o nome. Resumo em imagem recebe texto personalizado, incluindo o fallback de compartilhamento. PDF/CSV compartilhados recebem identificação no texto do Intent; backup continua no fluxo anterior.
- Os três documentos (card, resumo financeiro/mensal e extrato de cliente) usam o gerador PDF comum com nome no cabeçalho, quebrado por largura medida inclusive sem espaços. Valores e cálculos preservados.
- Datas completas apresentadas em `dd/MM/yyyy` em cards, detalhes, histórico, filtros, formulários, cobrança, PDF e CSV. Conversão de volta a ISO ao aplicar filtros ou salvar venda/pagamento/correção. Sem alteração dos registros, schema, ordenação e consultas. Referências mensais e nomes técnicos de arquivos preservados.
- Miniatura do formulário fica acima de Remover foto, com 16 dp entre eles, botão alinhado aos demais e toque na miniatura para ampliar. Galeria, câmera e remoção preservadas.

## Arquivos modificados

`app/build.gradle`, `MainActivity.kt`, novos `BusinessPreferences.kt` e `BusinessIdentity.kt`, novos testes `BusinessIdentityTest.kt` e `ReleaseCompatibilityTest.kt`; `README.md`, `docs/PRODUTO.md`, `docs/DECISOES.md`, `docs/RELEASE_PLAY_STORE.md`, `docs/VALIDACAO.md`, `docs/ARQUITETURA.md` e este relatório.

## Identidade e regras preservadas

Application ID `com.vendassimples.app`, versionCode **11**, versionName **1.0.11**. minSdk 24, compileSdk/targetSdk 36, Billing 9.1.0, produto `full_access`, opção `full-access`. FREE 30, clientes ilimitados, CSV no FREE, backup/restauração no FULL_ACCESS, compra única com preço do Google Play. Sem novas dependências, permissões, backend ou login.

## Verificações da primeira etapa de personalização

`clean test assembleDebug bundleRelease --offline --no-daemon`: BUILD SUCCESSFUL em 5m09s. Após limitar o título da UI a duas linhas, `test assembleDebug bundleRelease` executado novamente: BUILD SUCCESSFUL em 2m50s. JDK do Android Studio, Gradle 8.7 e SDK local; sem novas dependências. Avisos de depreciação de Locale/ajuste de teclado e parâmetro não utilizado, sem erros.

16 testes em debug e 16 em release, zero falhas/erros: 8 anteriores (dinheiro e formato de backup), 5 de nome/textos/datas e 3 de compatibilidade FREE/FULL_ACCESS/estados Billing. `git diff --check` e links locais dos documentos afetados aprovados. Conferência do manifesto diretamente no AAB por bundletool 1.16.0 confirmou applicationId/versões/minSdk/targetSdk; `bundletool validate` terminou com código 0.

Não havia aparelho/emulador conectado (`adb devices` vazio). Testes funcionais manuais, persistência da preferência pela UI, geração visual de PDFs e envio WhatsApp não executados. Testes de domínio não comprovam transação real no Google Play nem homologam os fluxos completos de cadastro, pagamentos, fotos ou backup.

A primeira tentativa de build falhou ao acessar `C:\.android`. A execução foi ajustada para o JDK do Android Studio, SDK local, cache Gradle do repositório e diretório Java correto. Nenhuma credencial foi copiada ao repositório ou exibida.

## Artefato e assinatura

Entrega atual, após o complemento de backup: `app/build/outputs/bundle/release/vendas-simples-v11.aab`, cópia idêntica de `app-release.aab`. Tamanho: **5.315.773 bytes**. SHA-256: `3f033e78b2ed97ac291ac4519bb3e20419fa20c901eff75fa8e65cc9dd6ba538`.

Assinado pela configuração release local existente. `jarsigner -verify -verbose -certs` retornou código 0 e `jar verified.`; certificado válido até 15/02/2054. A ferramenta avisa sobre certificado autoassinado/cadeia não reconhecida pelo truststore Java, ausência de timestamp e diferença de leitura JarFile/JarInputStream. O manifesto ZIP está na posição 931, após o conteúdo; uma verificação independente conferiu os hashes SHA-256 dos **929 arquivos de conteúdo**, todos cobertos pelo manifesto assinado. A assinatura criptográfica passou; a correspondência do certificado com a chave de upload cadastrada na Play ainda exige conferência no Console.

APK debug: `app/build/outputs/apk/debug/app-debug.apk`. Evidências locais ignoradas pelo Git: `.gradle-user/v11-backup-build.log`, `v11-backup-tests/`, `v11-bundle-validation.log`, `v11-signature.log`, `v11-manifest.xml` e `v11-artifact.json`. Artefatos e credenciais não integram o commit.

## Git

Commit local de implementação, testes e documentação; branch e hash informados na entrega. Sem alterações preexistentes no início e sem push, merge ou publicação. O bundle foi gerado antes do commit final; sua metadata de procedência Git pode apontar ao HEAD anterior, com as alterações compiladas da árvore local.

## Limites e pendências

O nome integra o backup SQLite formato 2; backups antigos/formato 1 preservam o nome local, ou a apresentação genérica em instalação nova. Cabeçalho PDF permite nomes longos, mas a validação visual em PDF real e aparelho continua pendente. Rotação/process death e rascunhos mantêm os limites existentes. Compatibilidade de atualização com certificado da versão publicada e disponibilidade do código 11 precisam ser conferidas no Play Console pelo responsável.

## Checklist manual no celular antes do upload

- [ ] Atualizar sobre V10 com dados sintéticos e confirmar preservação de clientes, vendas, pagamentos, fotos e licença.
- [ ] Primeiro acesso sem nome: convite opcional, apresentação genérica, Agora não/Voltar e reinício sem repetição.
- [ ] Salvar AleJoias, fechar/reabrir; editar para nome com acentos e nome longo de 120 caracteres; apagar o nome e verificar fallback.
- [ ] Tela inicial e Configurações no FREE e FULL_ACCESS; retrato/paisagem, tela estreita, fonte ampliada e teclado sem ações inacessíveis.
- [ ] Cobrar venda à vista e parcelada com pagamento parcial; conferir dados, vencimento brasileiro e única assinatura. Quitada não deve cobrar. Testar com/sem nome e cancelar o envio.
- [ ] Compartilhar imagem do resumo, PDF e CSV; conferir identificação e fallback sem WhatsApp instalado.
- [ ] Gerar os três PDFs com/sem nome, acentos, nome longo com/sem espaços, múltiplas páginas/fotos; conferir cabeçalhos, valores, totais, saldos e datas sem cortes.
- [ ] Criar/editar cliente e venda, datas no calendário e filtros; reiniciar e conferir persistência e ordenação.
- [ ] R$ 100,00 em três parcelas e pagamentos de R$ 30,00 e R$ 20,00; soma das parcelas R$ 100,00, pago R$ 50,00 e saldo R$ 50,00 nos fluxos correspondentes.
- [ ] Adicionar, ampliar e remover fotos; cancelar galeria/câmera/formulário; verificar relação por compra após reabrir.
- [ ] FREE: criar até 30 vendas, bloquear a 31ª; manter consultas, pagamentos existentes e CSV.
- [ ] FULL_ACCESS: venda acima do limite, backup e restauração com fotos e nome do negócio após reinstalação; licença deve ser restaurada separadamente pela Play.
- [ ] Pela Play com conta de teste: preço, compra aprovada/pendente/cancelada/recusada, indisponibilidade, reconhecimento e restauração da licença anterior.
- [ ] Testar offline, Android 24 e versão recente; conferir versão e certificado antes do upload manual.

## Notas sugeridas para Google Play

Personalize o aplicativo com o nome do seu negócio e use essa identidade nas mensagens e PDFs. Datas no padrão brasileiro e melhor espaçamento ao remover fotos de vendas.

## Complemento — backup do nome do negócio

O backup completo continua exclusivo de FULL_ACCESS, com personalização no FREE. O arquivo `.db` existente passa ao formato 2 somente em `VENDAS_SIMPLES_BACKUP_METADATA`, incluindo `business_name`. Schema financeiro 3, IDs, relações, fotos e valores preservados; não há segundo mecanismo de backup do usuário. A criação SAF e o fluxo interno de compartilhamento usam o mesmo `BackupManager`.

A restauração lê/valida uma única cópia, reconhece formatos 1 e 2, grava o nome com commit síncrono e atualiza o título da Activity. WhatsApp e PDFs leem a mesma preferência. Nome ausente, vazio ou nulo preserva o nome atual; instalação nova com backup antigo mantém fallback e convite opcional. Nome não vazio restaurado marca o convite como já oferecido.

`RestoreTransaction` grava banco anterior e estado da identidade em journal privado com sincronização de arquivos antes da troca. Falha reverte os dois; interrupção pendente é recuperada antes de abrir SQLite no próximo início. Falha de recuperação impede abrir a base e preserva os arquivos para nova tentativa. Journal é recuperação interna, não outro backup exportado. Não contém nem altera estado de licença. Energia/armazenamento permanentemente indisponíveis não são garantidos por testes de interrupção de processo.

Arquivos deste complemento: `BackupManager.kt`, `BusinessPreferences.kt`, `MainActivity.kt`, novo `RestoreTransaction.kt`, `BackupFormatTest.kt`, novo `RestoreTransactionTest.kt`, runner/contexto/cenários em `app/src/androidTest/`, `app/build.gradle` (runner somente de testes), `tools/validar_backup_v11.py`, README, arquitetura, produto, decisões, segurança e este relatório.

### Resultados do complemento

- `test assembleDebug assembleDebugAndroidTest bundleRelease --no-daemon`: BUILD SUCCESSFUL em 1m39s. **21 testes unitários por variante** (debug e release), zero falhas/erros; 5 novos testes cobrem sucesso conjunto, falha de persistência, interrupção simulada, nome legado/vazio e falha de substituição. Sem dependências novas de produção ou de testes; runner instrumentado usa APIs Android nativas.
- AVD isolado **Build10, Android 15/API 35**: `tools/validar_backup_v11.py --adb <adb-local> --serial emulator-5556` terminou com código 0. **11 testes instrumentados + 1 após encerrar e reiniciar o processo**, todos aprovados. SQLite/SharedPreferences reais em namespace sintético separado, sem abrir/apagar a base operacional. Hashes do banco e preferência operacionais do AVD preservados.
- Cenários instrumentados: nome no backup e igualdade de registros exportados; restauração em instalação vazia; acentos/caracteres especiais; parcelas/pagamentos parciais/fotos e sentinelas de preferências alheias à identidade preservados; formato 1 com/sem nome atual; nomes vazios/nulos; arquivo corrompido; versão de metadados inválida; bloqueio de exportação/restauração em todos os estados diferentes de FULL_ACCESS; novo backup depois de restaurar formato 1; persistência do nome e pagamentos em novo processo.
- Bundletool aprovou o AAB final, manifesto confirmou `com.vendassimples.app`, `11 / 1.0.11`, minSdk 24 e targetSdk 36. Jarsigner confirmou a assinatura; os 929 hashes de conteúdo cobertos pelo manifesto assinado foram verificados. Artefato atual e SHA-256 estão na seção de assinatura acima.
- `git diff --check`, links documentais e sintaxe do script aprovados. SQLite operacional, cálculos, Billing e regras comerciais não foram alterados; não há novas permissões.

Ocorrências do ambiente: duas tentativas offline do APK instrumentado falharam por dependência transitiva Kotlin `kotlin-stdlib-jdk8:1.8.0` ausente; o Gradle mascarou a resolução com erro interno de caminho. A resolução pela rede concluiu o build. O primeiro APK debug tinha chave diferente da instalação existente no AVD; apenas os APKs debug/teste foram regenerados com a chave debug local anterior (`assembleDebug assembleDebugAndroidTest --offline`: sucesso em 1m10s), sem apagar dados do AVD. A assinatura release não foi alterada e suas credenciais não foram expostas. O emulador foi temporariamente encerrado durante a compilação para liberar memória.

Limites: testes instrumentados usam arquivos locais e namespace sintético, não o seletor SAF real, provedores remotos ou transações Google Play. Reinício automatizado comprova persistência Android da preferência e banco em outro processo; não homologa visualmente a tela inicial/WhatsApp/PDFs nem substitui teste manual no celular. API 24, perda abrupta de energia e falha permanente de armazenamento não foram executadas. Nenhum teste manual no celular foi declarado realizado. Os resultados da primeira etapa acima são históricos; o AAB desta entrega contém o complemento.

### Homologação adicional no celular

- [ ] FULL_ACCESS: salvar nome com acentos, gerar backup pelo seletor de destino e conferir nome após restaurar em instalação nova; fechar/reabrir e verificar início, WhatsApp e os três PDFs.
- [ ] Restaurar formato 1 com/sem nome no aparelho; conferir que nome válido não foi apagado e dados financeiros/fotos permanecem íntegros.
- [ ] Formato 2 com nome vazio/nulo: preservar nome existente; novo backup após restauração deve conter o nome atualmente configurado.
- [ ] Comparar clientes, IDs, parcelas, pagamentos parciais, saldos e fotos antes/depois; testar o fluxo interno de compartilhamento quando acessível.
- [ ] Backup corrompido, versão incompatível, cancelamento do seletor/confirmação, provedor indisponível e espaço insuficiente: nenhum sucesso indevido nem perda de banco/nome.
- [ ] Interromper em ambiente sintético e reabrir; verificar recuperação conjunta e ausência de convite repetido após nome restaurado.
- [ ] FREE: personalização funciona, backup/restauração bloqueados; FULL_ACCESS e restauração de compras devem permanecer independentes do arquivo restaurado.
