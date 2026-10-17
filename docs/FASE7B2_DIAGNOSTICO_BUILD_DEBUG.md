# Fase 7B.2 — Diagnóstico do build debug

**Data:** 16/10/2026
**Projeto:** Vendas Simples — `com.vendassimples.app`
**Branch/HEAD:** `fix/v12-seguranca-conformidade` / `8aa34881755f222f9b090691aaa6127b31b1a363`
**Referência V11:** tag `v1.0.11-rc.1` aponta para o mesmo commit (objeto anotado `ca2e8417c30728a091150c9bd8aaee93a46aff0d`; commit apontado `8aa3488…`)
**Versão mantida:** `versionCode 11`, `versionName 1.0.11`

## 1. Resumo executivo

O build debug mais recente no Codex falhou em `:app:validateSigningDebug` ao abrir `<RAIZ_REPOSITORIO>\.gradle-user\fase7b1\android-user-home\debug.keystore.lock`. A identidade atual do processo é `<IDENTIDADE_SANDBOX>`. No home Android padrão, a ACL dá ao grupo do sandbox somente leitura no arquivo de lock; no home isolado de teste, a ACL permite Modify, mas Gradle ainda retornou acesso negado. A consulta de processos via CIM foi bloqueada por acesso negado e não havia processo Java visível por `Get-Process` no instante da inspeção.

**Classificação: AGUARDANDO VALIDAÇÃO NO POWERSHELL.** Há evidência de restrição do ambiente isolado ao acesso de lock, mas não é possível afirmar que seja a causa exclusiva sem a execução no terminal interativo do usuário. Não foi feita alteração de código/ACL/configuração global, nem gerado APK novo, AAB ou release. O próximo passo é executar o bloco PowerShell da seção 6 e devolver o log, código de saída e metadados do APK, sem compartilhar arquivo de propriedades ou keystore.

## 2. Estado do Git e configuração do projeto

- Branch ativa: `fix/v12-seguranca-conformidade`.
- `HEAD`: `8aa34881755f222f9b090691aaa6127b31b1a363`.
- `v1.0.11-rc.1^{commit}`: `8aa34881755f222f9b090691aaa6127b31b1a363`; tag intacta.
- Árvore já continha alterações locais das fases anteriores; foram preservadas. Nenhum reset, clean, staging, commit, merge ou descarte foi realizado.
- Versões conferidas: KGP `2.2.20`, AGP `8.9.1`, Gradle Wrapper `8.11.1`, JBR `21`, `minSdk 24`, `compileSdk/targetSdk 36`.
- `applicationId`: `com.vendassimples.app`; `versionCode 11`; `versionName 1.0.11`.
- A configuração de assinatura em `app/build.gradle` declara apenas `signingConfig` no build type `release`; o debug usa a configuração padrão do Android Gradle Plugin.
- O script do projeto carrega e valida o arquivo local `VENDAS_KEYSTORE_PROPERTIES` (ou `user.home\keystore.properties`) durante a configuração Gradle, inclusive ao solicitar tarefas debug. No ambiente anterior, esse caminho era `<DIRETORIO_USUARIO>\keystore.properties`. Não foram exibidos valores, aliases, senha, caminho do arquivo `.jks` ou conteúdo de chave. O arquivo precisa existir no PowerShell usado, mas sua chave release não é configurada como assinatura do APK debug.

## 3. Ambiente e análise de `debug.keystore.lock`

| Dado observado no processo Codex | Valor |
|---|---|
| Diretório de trabalho | `<RAIZ_REPOSITORIO>` |
| `USERPROFILE` | `<DIRETORIO_USUARIO>` |
| `HOMEDRIVE` / `HOMEPATH` | `C:` / `\Users\<USUARIO>` |
| `HOME` | não definida |
| `ANDROID_USER_HOME` | não definida |
| `ANDROID_SDK_HOME` | não definida |
| `ANDROID_HOME` | `<ANDROID_SDK>` |
| `JAVA_HOME` neste processo | não definida; invocações anteriores apontaram explicitamente para Android Studio JBR 21 |
| Identidade de segurança (`whoami`) | `<IDENTIDADE_SANDBOX>` |
| Processos Java visíveis | nenhum no momento da inspeção |
| Consulta CIM de processos/owner | negada pelo ambiente (`Acesso negado`) |

O home padrão do Android é `<ANDROID_USER_HOME>`; contém `debug.keystore` (2.618 bytes) e `debug.keystore.lock` (0 bytes). A ACL lida para o lock concede `RX` a `<GRUPO_SANDBOX>`; a conta proprietária `<USUARIO>` tem Full Control, mas o processo atual executa com identidade sandbox distinta. No diretório isolado usado na tentativa anterior, `<RAIZ_REPOSITORIO>\.gradle-user\fase7b1\android-user-home`, o lock também existe (0 bytes), e ACLs incluem Modify para `<GRUPO_SANDBOX>`; mesmo assim, Gradle retornou `AccessDeniedException` nesse caminho.

O build falhou antes de finalizar o APK. O APK encontrado em `app/build/outputs/apk/debug/app-debug.apk` tem timestamp **16/10/2026 12:07:08**, anterior à tentativa da Fase 7B.1; não é evidência de build concluído nesta validação. A chave temporária daquela tentativa não foi apagada, substituída ou reutilizada como chave release.

### Diagnóstico fundamentado

- **Falha de configuração do projeto:** sem evidência de que o `signingConfig` debug esteja incorreto; o stack falha ao validar/abrir o lock da assinatura padrão. O carregamento de propriedades release durante a configuração Gradle é uma dependência global do script, mas a execução anterior ultrapassou essa fase e parou em `validateSigningDebug`.
- **Falha de configuração do ambiente:** não indicada apenas pela configuração Android/Java atual; variáveis Android de usuário estão unset. O PowerShell interativo informado pelo usuário tem contexto diferente, ainda não medido aqui.
- **Restrição do sandbox:** **provável e apoiada por ACL/identidade** no home padrão. O erro também ocorreu em diretório com permissão Modify, então a ACL visível não explica sozinha todas as tentativas; pode haver restrição adicional do mecanismo isolado.
- **Conclusão:** causa ambiental/sandbox provável, exclusividade ainda não confirmada. O erro deve ser comparado em PowerShell interativo. Não alterei ACLs, registro, configuração permanente, keystore, arquivos globais ou políticas de isolamento.

## 4. Saída de lint adicional

O relatório `app/build/reports/lint-results-debug.xml` atual tem 40 registros `SetTextI18n`, mas apenas 21 pares distintos de arquivo/linha, pois o Lint registra ocorrências repetidas em algumas expressões (por exemplo `MainActivity.kt:533`, `:1381–1386` e `:2332–2336`). Assim, o número 39 informado na Fase 7B e os 40 registros atuais não identificam por si só um único aviso novo. O resumo anterior não guardou o XML detalhado dos 39 registros para comparação direta.

As ocorrências estão em `app/src/main/java/com/example/controlevendas/MainActivity.kt`, incluindo títulos e textos montados em runtime. Exemplo em `:270`: `text = "Vendas Simples"`. O Lint alerta que texto literal atribuído à UI não é traduzível por recursos Android; nas expressões concatenadas, alerta que valores dinâmicos também devem usar placeholders de `strings.xml`. O efeito esperado é limitação de localização, não alteração de valor financeiro ou falha de assinatura.

Na Fase 7B.1, não houve edição de `MainActivity.kt`, regra de Lint, AGP ou dependência Android de Lint; a única configuração atualizada foi KGP. KGP não é o motor que emite essa regra, então não há evidência causal de que os registros SetTextI18n tenham sido introduzidos pela mudança Kotlin. A contagem 39→40 fica classificada como diferença de contagem histórica não reconciliada, sem correção de código nesta fase.

## 5. Validade de APK existente e procedimento de verificação

Caminho de saída esperado, confirmado pela estrutura do projeto e pela saída antiga existente:

```text
app\build\outputs\apk\debug\app-debug.apk
```

O comando manual abaixo registra log no `%TEMP%` com timestamp, executa somente `assembleDebug`, verifica existência/tamanho/data/hash e, se disponível, lê identidade e versão via `aapt` e valida a assinatura via `apksigner`. O certificado público apresentado pelo verificador não contém chave privada. O bloco não instala o APK, não remove dados e não toca no APK/AAB release nem na instalação Play do Samsung.

## 6. Comandos PowerShell para execução manual

Copiar o bloco inteiro para o PowerShell interativo do Windows. Ele usa o JBR e Android SDK identificados; mantém os valores do usuário Android como estão no terminal e configura somente variáveis do processo para localizar as propriedades locais requeridas pelo script Gradle. Se o arquivo de propriedades não existir nesse caminho no terminal, interromper e ajustar **somente o caminho do arquivo local**, sem exibir seu conteúdo.

```powershell
$ErrorActionPreference = 'Stop'
Set-Location -LiteralPath '<RAIZ_REPOSITORIO>'

$jbr = 'C:\Program Files\Android\Android Studio\jbr'
if (Test-Path -LiteralPath (Join-Path $jbr 'bin\java.exe')) {
    $env:JAVA_HOME = $jbr
}
Write-Host "Diretório: $(Get-Location)"
Write-Host "USERPROFILE=$env:USERPROFILE"
Write-Host "HOME=$env:HOME"
Write-Host "HOMEDRIVE=$env:HOMEDRIVE"
Write-Host "HOMEPATH=$env:HOMEPATH"
Write-Host "ANDROID_USER_HOME=$env:ANDROID_USER_HOME"
Write-Host "ANDROID_SDK_HOME=$env:ANDROID_SDK_HOME"
Write-Host "ANDROID_HOME=$env:ANDROID_HOME"
Write-Host "JAVA_HOME=$env:JAVA_HOME"
if ($env:JAVA_HOME -and (Test-Path -LiteralPath (Join-Path $env:JAVA_HOME 'bin\java.exe'))) {
    & (Join-Path $env:JAVA_HOME 'bin\java.exe') -version 2>&1
} else {
    java -version 2>&1
}
& .\gradlew.bat --version

$env:VENDAS_KEYSTORE_PROPERTIES = '<DIRETORIO_USUARIO>\keystore.properties'
if (-not (Test-Path -LiteralPath $env:VENDAS_KEYSTORE_PROPERTIES -PathType Leaf)) {
    throw 'O arquivo local de propriedades Gradle não foi encontrado; não mostre seu conteúdo. Ajuste apenas o caminho local e tente novamente.'
}

$apk = Join-Path (Get-Location) 'app\build\outputs\apk\debug\app-debug.apk'
$started = Get-Date
$log = Join-Path $env:TEMP ("vendas-simples-fase7b2-debug-{0}.log" -f $started.ToString('yyyyMMdd-HHmmss'))
Write-Host "Log: $log"
& .\gradlew.bat --no-daemon --max-workers=1 --console=plain :app:assembleDebug 2>&1 | Tee-Object -FilePath $log
$gradleExitCode = $LASTEXITCODE
Write-Host "Gradle exit code: $gradleExitCode"

if ($gradleExitCode -eq 0 -and (Test-Path -LiteralPath $apk -PathType Leaf)) {
    $file = Get-Item -LiteralPath $apk
    $hash = Get-FileHash -LiteralPath $apk -Algorithm SHA256
    Write-Host "APK: $($file.FullName)"
    Write-Host "Tamanho: $($file.Length) bytes"
    Write-Host "Data: $($file.LastWriteTime.ToString('o'))"
    Write-Host "SHA-256: $($hash.Hash)"

    $sdk = '<ANDROID_SDK>'
    $aapt = Join-Path $sdk 'build-tools\36.1.0\aapt.exe'
    $apksigner = Join-Path $sdk 'build-tools\36.1.0\apksigner.bat'
    if (Test-Path -LiteralPath $aapt) {
        & $aapt dump badging $apk | Select-String '^package:'
    } else {
        Write-Host 'aapt não encontrado; applicationId/version não foram lidos automaticamente.'
    }
    if (Test-Path -LiteralPath $apksigner) {
        & $apksigner verify --verbose --print-certs $apk
    } else {
        Write-Host 'apksigner não encontrado; a assinatura não foi validada automaticamente.'
    }
} elseif ($gradleExitCode -eq 0) {
    Write-Host "Gradle terminou, mas o APK esperado não existe: $apk"
} else {
    Write-Host 'Build debug falhou. APK pré-existente, se houver, não deve ser considerado saída desta execução.'
}
```

O log e a saída do comando mostrarão identidade, versão, hash e certificado público, sem imprimir senhas/chaves privadas. Enviar de volta: código de saída Gradle, resultado de `aapt`, resultado de `apksigner`, tamanho/hash/data do APK e trecho final do log em torno da falha ou `BUILD SUCCESSFUL`. Não anexar `keystore.properties`, keystore ou dados pessoais.

## 7. Critérios para classificar o resultado do PowerShell

Marcar **BUILD DEBUG VALIDADO** apenas se `assembleDebug` terminar com código zero, o APK existir, `aapt` mostrar `com.vendassimples.app`, `versionCode 11` e `versionName 1.0.11`, e `apksigner verify` confirmar assinatura válida. Registrar SHA-256, tamanho e timestamp observados. O nome/distinguished name da chave deve indicar a assinatura debug padrão; se não for identificável, registrar validação parcial e revisar antes de instalar.

Um APK debug não deve ser instalado sobre a V11 distribuída pela Google Play: o certificado é diferente e bloquearia atualização normal. Não desinstalar a V11, limpar dados, nem alterar o dispositivo Samsung operacional. O APK validado serve para build local/ambiente de teste separado; não comprova compatibilidade da upload key release.

## 8. Bloqueios para V12

| Etapa | Situação após esta inspeção |
|---|---|
| Corrigir matriz Kotlin/AGP/Gradle | Resolvida: KGP 2.2.20 e testes/lint/compilação AndroidTest passaram na Fase 7B.1 |
| Empacotar APK debug | Pendente da reprodução no PowerShell interativo; bloqueado no Codex pelo lock |
| Incrementar para 12 / 1.0.12 | Não feito; manter 11 / 1.0.11 até aprovação dos gates e Console |
| Gerar AAB V12 | Proibido nesta fase; não executado |
| Validar upload key versus Play Console | Pendente; configuração local não prova correspondência |
| Distribuir Alpha e atualizar V11 → V12 | Pendente de build, autorização futura e fluxo Play |
| Conformidade e homologação | Pendências documentadas na Fase 7B permanecem: Data safety, Billing real, política pública, API 24/aparelho, teste de upgrade e licença |

## 9. Arquivos, testes e estado final

Arquivo criado nesta fase: `docs/FASE7B2_DIAGNOSTICO_BUILD_DEBUG.md`. Nenhum arquivo de produção foi alterado. `git diff --check` não indicou erro de whitespace; exibiu somente avisos Git de normalização LF/CRLF em arquivos alterados previamente. Branch, `HEAD`, tag e versão continuam como descritos acima. Alterações locais anteriores foram preservadas; não houve stage, commit, tag nova, merge, push, geração AAB ou acesso Play.

## 10. Próxima ação recomendada

Executar o bloco PowerShell acima no terminal interativo que já acessa ADB, sem copiar keystore nem propriedades para a conversa. Enviar os resultados sanitizados para análise. Só depois confirmar a causa do bloqueio, validar o APK e decidir se há caminho para prosseguir com a V12. Mesmo com build debug aprovado, geração de AAB, validação da upload key e homologação Alpha exigem gates e autorização próprios.
