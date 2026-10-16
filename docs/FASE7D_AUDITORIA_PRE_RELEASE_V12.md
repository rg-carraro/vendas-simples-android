# Fase 7D — Auditoria pré-release V12

**Data:** 16/10/2026
**Repositório:** `rg-carraro/vendas-simples-android`
**Branch local:** `fix/v12-seguranca-conformidade`
**Candidata configurada:** `com.vendassimples.app`, `versionCode 12`, `versionName 1.0.12`
**Referência V11:** `v1.0.11-rc.1` → `8aa34881755f222f9b090691aaa6127b31b1a363`

## 1. Resumo executivo

A auditoria foi somente leitura, exceto por este documento. Nenhum código funcional foi alterado; não houve fetch, AAB, commit, push, merge, rebase, tag, upload ou operação na Play Console.

Há uma diferença importante desde a Fase 7C: GitHub informa que `master` remoto recebeu em 16/10/2026 o merge `fb59cf96392b80c63a42974c80c1b91be1657e4c`, incorporando `5da7f4af0f5954b14b44d8bf1715eb35304c924e`, a revisão da política. O `origin/master` local continua em `99f41e7`; a candidata não está atualizada com o remoto. A árvore contém muitas alterações não commitadas, inclusive `app/build.gradle` com 12/1.0.12. A política local coincide com `master` remoto, mas a URL do GitHub Pages não pôde ser aberta por esta ferramenta.

A Fase 7C leu o certificado público local de upload e registrou seu SHA-256, mas não o comparou com “Upload key certificate” da Console nem assinou bundle de release. Nesta execução `VENDAS_KEYSTORE_PROPERTIES` não está definida. O código 12 disponível na Console, as declarações Data safety e a homologação de runtime também não estão confirmados.

**Classificação: NÃO APTO PARA GERAR AAB V12 neste estado.** A auditoria documental foi concluída e o trabalho pode prosseguir para reconciliação controlada. O AAB depende de árvore/código consolidado, certificado e versionCode confirmados, pendências de conformidade tratadas e autorização específica. Não está apto para produção.

## 2. Estado Git local no início

Comandos de leitura executados: `git status --short --branch`, `git branch -vv`, `git remote -v`, `git log --oneline --decorate -15`, `git show-ref --tags`, `git diff --stat`, `git diff --check`, `git rev-list --left-right --count` e consultas `git show`/`git grep`.

| Item | Evidência local |
|---|---|
| Branch / HEAD | `fix/v12-seguranca-conformidade` / `8aa34881755f222f9b090691aaa6127b31b1a363` |
| Tag V11 | `v1.0.11-rc.1` continua apontando para o commit V11 `8aa3488` (tag anotada) |
| `master` / `origin/master` locais | `99f41e70984d1aa25cc9eca89cc5c6067f55f47b` |
| Relação local candidata…master local | 9 commits à frente, 0 atrás; base comum `99f41e7` |
| Estado da árvore | Modificada e sem staging; 20 caminhos rastreados modificados e 24 entradas não rastreadas na listagem inicial, antes da criação deste relatório |
| Artefatos sensíveis/gerados rastreados | `git ls-files` não encontrou `.aab`, `.apk`, `.jks`, `.keystore` ou `keystore.properties` |
| `git diff --check` | Sem erro de whitespace; Git avisou sobre normalização LF/CRLF em arquivos preexistentes |

As alterações preexistentes em `README.md`, código de backup/Activity/manifesto, toolchain, política local, scripts, testes, documentos e worktrees foram preservadas. `.codex/` e `.worktrees/` são auxiliares locais; `app/build/` e caches são gerados/ignorados. Não são material de commit. O README ainda descreve a fase como preparação da V11; essa documentação está desatualizada frente ao `app/build.gradle` de trabalho (12/1.0.12).

Há dois AABs preexistentes e ignorados pelo Git em `app/build/outputs/bundle/release/`: `vendas-simples-v11.aab` (12/10/2026 22:37:56) e `app-release.aab` (12/10/2026 22:32:07). Ambos têm 5.320.483 bytes e o mesmo SHA-256 `D751F51DC69AC2DA2784D841374288E4456BD666E2A5237C2E91C1EE9D47AABA`; o nome do primeiro indica V11, mas o manifesto interno não foi extraído nesta fase. `jarsigner -verify -verbose -certs` retornou exit code 0 e “jar verified”; também emitiu avisos de certificado autoassinado/sem timestamp e de inconsistência entre leitura `JarFile`/`JarInputStream` para entradas, incluindo manifesto e arquivos de transporte. Não há `bundletool` encontrado no Android SDK inspecionado; a validação estrutural/metadados desses AABs fica pendente. Eles não foram criados nem alterados nesta Fase 7D. O futuro `bundleRelease` pode sobrescrever `app-release.aab`, portanto preservar estes artefatos antes de qualquer build autorizado.

## 3. Divergência local/remota e plano Git

Consulta somente leitura ao GitHub, sem `fetch`: a lista remota expõe `master` e `docs/privacy-policy-2026-10-review`; não há branch remota V12. O commit mais recente de `master` observado é `fb59cf96392b80c63a42974c80c1b91be1657e4c`, merge do PR #1. Comparação GitHub de `99f41e7` com `fb59cf9` informa `master` remoto 2 commits à frente, ambos relativos à atualização da política. Assim, em relação à base comum, a candidata local tem 9 commits próprios e o remoto tem 2 commits novos; os históricos divergiram. `origin/master` está desatualizado e não representa o remoto atual.

O merge público incorporou o commit `5da7f4a`; os links de evidência são [merge `fb59cf9`](https://github.com/rg-carraro/vendas-simples-android/commit/fb59cf96392b80c63a42974c80c1b91be1657e4c) e [revisão da política `5da7f4a`](https://github.com/rg-carraro/vendas-simples-android/commit/5da7f4af0f5954b14b44d8bf1715eb35304c924e). A consulta de comparação direta contra `8aa3488` retornou 404 porque esse commit candidato ainda não existe no remoto; não prova ausência local nem substitui a comparação após uma reconciliação.

**Plano seguro, não executado:** primeiro inventariar/revisar os arquivos locais por área e decidir o escopo que deve ser preservado; consolidar esse trabalho em commits locais revisáveis, excluindo worktrees, caches, segredos e artefatos; depois atualizar refs de forma explícita e controlada, comparar o merge público e integrar `master` por merge numa branch de integração, preservando os commits existentes. A árvore atual altera `site/privacy/index.html`, arquivo que também mudou no merge remoto; embora o conteúdo da cópia local coincida hoje com `master`, o estado sujo impede uma integração segura imediata. Após integrar, resolver README/relatórios e repetir testes. Não executar merge/rebase/reset nesta fase.

## 4. Política pública

O conteúdo de `site/privacy/index.html` no `master` remoto foi obtido pelo conector GitHub (blob `35fd014488c54c8919416c70cd3bfb184ba5318d`) e comparado ao arquivo local: **iguais após normalizar finais de linha**. A página-fonte está atualizada para 15/10/2026 e inclui as seções de limitações de criptografia do backup SQLite, dependências/Billing, compartilhamento por WhatsApp e retenção/exclusão.

A tentativa de abrir `https://rg-carraro.github.io/vendas-simples-android/privacy/` falhou na ferramenta web. Portanto, a auditoria confirma o conteúdo da branch `master`, não disponibilidade HTTP, implantação de Pages ou conteúdo efetivamente servido agora. A Fase 6C registra que o usuário informou a página publicada/visível. Para fechar a verificação, o responsável deve abrir a URL e registrar status HTTP/data e conferir texto/data da política, sem cookies ou dados da conta. Também comparar a URL no Console e a cópia offline empacotada.

## 5. Chave de upload

O relatório 7C registra uma leitura local via `keytool -list -v`, com senha ocultada, e o seguinte fingerprint público SHA-256:

```text
CD:90:9D:AE:E8:16:B2:7F:4E:32:13:52:73:62:25:80:BE:F0:73:F1:8A:8D:EB:56:04:F5:24:90:9C:63:C6:9E
```

O caminho de configuração observado no Gradle é `VENDAS_KEYSTORE_PROPERTIES` ou, como fallback, `user.home/keystore.properties`; o arquivo externo não é rastreado. Nesta execução `VENDAS_KEYSTORE_PROPERTIES` estava ausente. Não reli o arquivo/keystore fora do workspace nem executei assinatura privada. A verificação do AAB preexistente mostrou `SHA384withRSA`, chave RSA 2048 bits e o mesmo fingerprint acima. Isso demonstra que aquele artefato foi assinado por certificado correspondente ao fingerprint já registrado; **não demonstra correspondência com a Play Console, validade de acesso atual à chave privada, nem que o processo atual consegue assinar**. O alias/caminho atual do keystore e o algoritmo configurado continuam sem revalidação.

Antes de assinar, apontar a variável de processo para a configuração local ignorada pelo Git e confirmar que o keystore e alias estão acessíveis sem exibir senhas. Comparar o SHA-256 do certificado local exclusivamente com **Google Play Console → Integridade do app → Assinatura do app → Certificado da chave de upload**. Não comparar com “certificado da chave de assinatura do app” gerenciada pelo Google nem com o certificado Android Debug `B9579026…55115` da Fase 7C. O algoritmo/chave pública não foi retido no relatório 7C; capturá-lo de forma não sensível quando a chave for revalidada. A assinatura do AAB será JAR; `apksigner` valida APK e não deve ser usado como verificação principal do AAB.

## 6. Disponibilidade do `versionCode 12`

**Pendente:** a Play Console não foi acessada e nenhum artefato V12 foi carregado. A confirmação deve ser feita pelo responsável no app `com.vendassimples.app`, revisando App Bundle Explorer e releases/artefatos em produção, teste aberto, fechado, interno e rascunhos/em revisão, além de rejeitados ou uploads anteriores. Conferir o histórico de códigos efetivamente consumidos; o fato de Alpha estar na V11 e a configuração local ser 12 não prova que 12 esteja disponível. Registrar o código e o status observado antes do bundle. Se 12 já tiver sido usado, não o reutilizar: apresentar o próximo código livre e obter decisão explícita antes de alterar `app/build.gradle`.

## 7. Segurança dos dados — evidência, inferência e pendência

Esta seção resume a Fase 6C, não é uma nova investigação de tráfego nem uma declaração para a Console.

| Classe | O que se sustenta | O que não se conclui |
|---|---|---|
| **Evidência confirmada no código/documentação local** | Dados de cliente, venda, parcela, pagamento, foto e nome são usados/armazenados localmente; não foi identificado backend próprio, Analytics ou Crashlytics nesta revisão. CSV/PDF/backup e WhatsApp saem por ações/fluxos do usuário para o destino escolhido. Backup SQLite não tem criptografia/senha própria, não inclui entitlement e é protegido por FULL_ACCESS. Billing 9.1.0 conecta ao Play, consulta `full_access`/compras INAPP e reconhece compras. Política local descreve os limites e o processamento do Billing. | O código local não comprova respostas da Console, dados de todas as versões ativas nem o que terceiros retêm. `INTERNET` ou conexão não provam, isoladamente, uma categoria específica coletada. |
| **Inferência técnica da Fase 6C** | O grafo/bytecode estático encontrou caminho de Data Transport/CCT e metadados técnicos possíveis em dependências Billing; a conexão começa na inicialização do fluxo Billing. | Isso não prova que eventos/campos específicos sejam transmitidos neste app, destino/papel, finalidade, obrigatoriedade, associação ou retenção. Não afirmar “Billing não coleta” nem declarar IDs/diagnósticos como fato fechado apenas por inferência. |
| **Pendente de confirmação** | Status e respostas salvas de Data safety para o conjunto de versões/tracks distribuídos; inventário/documentação oficial específica Billing 9.1.0 ou observação controlada e sanitizada; histórico de compra e categorias; criptografia em trânsito para todos os fluxos; finalidade, compartilhamento e mecanismo de exclusão; URL/texto publicado. | Não preencher ou alterar formulário Play nesta fase. Exceção de cartão do processador não resolve automaticamente histórico de compras, diagnósticos ou outros dados. |

Checklist manual curto: abrir **Conteúdo do app → Segurança dos dados**; anotar estado/data do formulário; revisar cada categoria, finalidade, compartilhamento, obrigatoriedade, criptografia e exclusão; enumerar tracks e versões ativas; confrontar Billing e exportações com a matriz da Fase 6C; abrir URL da política; não salvar/submeter respostas até haver evidência suficiente. A localização dos menus pode variar.

## 8. Integridade funcional e caminho V11 → V12

A inspeção do estado de trabalho confirmou `LocalDatabase.DB_VERSION = 3`; `LocalDatabase.kt` não tem diff e a migração v3 continua convertendo colunas monetárias antigas para centavos. A V12 não incrementa schema nem pede migração SQLite. O código de trabalho de backup mantém metadados de formato 2/schema 3, valida SQLite antes da substituição e exige `FULL_ACCESS`; `BackupManager.kt` está modificado por trabalho anterior, então sua revisão e os testes da Fase 7C são evidência da árvore local, não do commit `8aa3488` sozinho. Billing (`full_access`, produto INAPP, compra única), `BillingDomain.kt` e `PlayBillingGateway.kt` não aparecem modificados; entitlement não é dado do backup.

As regras descritas no código/docs continuam FREE até 30 vendas, clientes ilimitados, compra única `FULL_ACCESS` e backup/restauração completos exclusivos de FULL_ACCESS. As saídas PDF, CSV e WhatsApp, funcionamento offline e personalização devem ser revalidados no artefato final. Fase 7C executou 43 testes unitários em cada variante, lint sem erros/67 avisos por variante e compilação AndroidTest; o runtime instrumentado, upgrade V11→V12, Billing de Play e aparelho não foram executados nesta fase. Os 13 cenários físicos V11 citados na Fase 6C foram informados pelo usuário e não homologam V12.

## 9. Pendências de homologação

- Atualizar V11 instalada da Play para V12 pela Alpha, sem desinstalar, limpar dados ou instalar debug; comparar clientes, vendas, parcelas, pagamentos, saldos, fotos, nome e licença antes/depois.
- Executar testes instrumentados de backup/restauração em banco sintético, arquivo antigo, arquivo inválido/corrompido e rollback consistente; confirmar FULL_ACCESS e que restore não altera Billing/licença.
- Homologar API 24 e Android recente, offline, reinício, notificações conforme aplicável, PDF, CSV e WhatsApp.
- Testar compra única, restauração, pendente, cancelada e Billing indisponível por contas autorizadas; verificar FREE na 30ª/31ª venda e FULL_ACCESS.
- Validar política pública e offline; rever Console/Data safety para todas as versões distribuídas.

Não realizar teste destrutivo com dados reais ou sobre a instalação V11 do usuário.

## 10. Checklist de geração e validação do AAB — preparado, não executado

Somente depois de reconciliar o Git, obter árvore limpa no commit exato a validar, confirmar code 12, provisionar a configuração local de assinatura, preservar os AABs antigos existentes e receber autorização explícita para gerar o AAB:

```powershell
# Usar apenas caminho local fora do repositório; nunca imprimir conteúdo/senhas.
$env:VENDAS_KEYSTORE_PROPERTIES = '<caminho-local-ignorado-pelo-Git>\keystore.properties'
.\gradlew.bat --no-daemon --max-workers=1 --console=plain :app:bundleRelease

$aab = (Resolve-Path 'app\build\outputs\bundle\release\app-release.aab').Path
Get-Item -LiteralPath $aab | Select-Object FullName, Length, LastWriteTime
Get-FileHash -LiteralPath $aab -Algorithm SHA256

# JAR signature do AAB; guardar/verificar Signer e SHA-256 com o certificado de upload confirmado.
& "$env:JAVA_HOME\bin\jarsigner.exe" -verify -verbose -certs $aab
if ($LASTEXITCODE -ne 0) { throw 'Verificação JAR do AAB falhou.' }

# Requer bundletool-all.jar obtido/validado previamente; não substitui jarsigner.
$bundletool = '<caminho-local>\bundletool-all.jar'
& "$env:JAVA_HOME\bin\java.exe" -jar $bundletool validate --bundle=$aab
& "$env:JAVA_HOME\bin\java.exe" -jar $bundletool dump manifest --bundle=$aab --module=base

# Registrar origem e ambiente do artefato.
git rev-parse HEAD
.\gradlew.bat --version
```

Conferir no manifesto do módulo `base`: pacote `com.vendassimples.app`, `versionCode 12`, `versionName 1.0.12`, `minSdk 24`, target/compile conforme esperado. Registrar resultado/exit code de cada ferramenta, versão do JDK/Gradle/AGP/Kotlin, caminho/tamanho/hash, commit limpo de origem e impressão do certificado signatário. `jarsigner` valida assinatura JAR do bundle; a validação do bundle verifica estrutura. A Play gera APKs assinados com a chave de assinatura do app gerenciada pelo Google após o upload; isso é separado da upload key. Não usar `apksigner` no AAB.

Notas propostas, não submetidas: “Reforçamos as verificações e a recuperação durante a restauração de backups. A Política de Privacidade pode ser consultada dentro do aplicativo, inclusive offline. Atualizamos a compatibilidade do ambiente Android e corrigimos pontos de estabilidade.” Revisar para corresponder apenas aos itens efetivamente incluídos e homologados.

## 11. Checklist para upload Alpha — não executado

- [ ] Aprovar escopo e consolidar commits; conferir que a origem do artefato é o commit limpo registrado.
- [ ] Confirmar `versionCode 12` disponível, chave upload coincidente e Data safety/política revisadas.
- [ ] Revisar o AAB exato, hash SHA-256, metadados e assinatura JAR verificada.
- [ ] Revisar notas, versão, application ID, produto `full_access` e artefatos já ativos.
- [ ] Receber autorização específica para upload Alpha; não inferir autorização a partir desta preparação.
- [ ] Após upload autorizado, registrar status do Console, faixa, código e hash do bundle enviado; homologar upgrade V11→V12 no Galaxy S22+ pela Play.

## 12. Riscos e bloqueadores

1. Árvore local muito suja e candidata sem commits da Fase 2–7C; HEAD ainda é commit V11 8aa3488. Um bundle agora não teria proveniência de commit que represente o conteúdo local.
2. Remoto `master` avançou em relação a refs locais; conteúdo de política converge, mas o histórico precisa integração segura.
3. Fingerprint local não comparado à upload key da Console; processo atual sem configuração explícita de assinatura. Um AAB antigo tem assinatura `SHA384withRSA`/RSA-2048 correspondente ao fingerprint conhecido, mas também avisos JarFile/JarInputStream ainda sem explicação estrutural.
4. Code 12 não verificado na Console.
5. Declaração Data safety, SDK Billing 9.1.0, páginas publicadas e versões ativas não foram confirmadas.
6. Faltam runtime instrumentado, upgrade pela Alpha, API 24 e fluxos reais Billing em contas de teste.
7. README e parte do checklist de release ainda descrevem V11; documentação deve ser reconciliada antes de distribuir.

## 13. Próxima ação recomendada

Fazer uma fase de consolidação Git controlada, primeiro revisando e separando o conjunto local em commits auditáveis sem incluir credenciais, worktrees ou builds; atualizar as referências remotas explicitamente quando autorizado e integrar o merge público de política sem sobrescrever arquivos locais; então atualizar documentação de versão e repetir a regressão. Em paralelo, o responsável confirma na Console o upload certificate, code 12, versões/tracks e Data safety e registra evidências sanitizadas. Depois disso, executar testes runtime/Alpha conforme autorização. Gerar o AAB somente com origem limpa identificável e nova autorização específica.

## 14. Estado Git final

Esta auditoria não modificou código nem referências Git. Continua a branch `fix/v12-seguranca-conformidade` em `8aa3488`, com alterações preexistentes preservadas e sem staging. Este arquivo é o único novo documento criado nesta Fase 7D. Os AABs existentes não foram modificados. Após salvar o relatório, `git diff --check` não apontou erros (somente avisos LF/CRLF em arquivos preexistentes), e uma verificação textual do novo relatório não encontrou espaços finais. Nenhum arquivo gerado/segredo foi adicionado.

## 15. Recomendação final

**NÃO APTO PARA GERAR AAB V12 neste momento; APTO COM RESSALVAS para continuar a preparação e reconciliação.** Os bloqueadores não são uma falha de teste unitário: são proveniência/estado Git divergente e sujo, assinatura não comparada com a Console, disponibilidade de código desconhecida, pendências de Data safety e ausência de validação runtime/Alpha. A V12 não está homologada para produção.
