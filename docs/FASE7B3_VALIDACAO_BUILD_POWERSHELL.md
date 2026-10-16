# Fase 7B.3 — Build debug validado e preparação V12

**Data:** 16/10/2026
**Aplicativo:** Vendas Simples — `com.vendassimples.app`
**Branch:** `fix/v12-seguranca-conformidade`
**HEAD/tag V11:** `8aa34881755f222f9b090691aaa6127b31b1a363` / `v1.0.11-rc.1`
**Versão ainda configurada:** `versionCode 11`, `versionName 1.0.11`

## 1. Resultado executivo

**BUILD DEBUG VALIDADO.** O usuário informou que executou `gradlew.bat :app:assembleDebug --stacktrace` no PowerShell interativo e obteve `BUILD SUCCESSFUL` em 3m49s, `BUILD_EXIT_CODE=0` e 32 tarefas (6 executadas e 26 atualizadas). O usuário encontrou o APK com 7.318.615 bytes. O sucesso do build é evidência informada pelo usuário.

O APK correspondente ficou acessível no workspace no mesmo caminho, com o mesmo tamanho e timestamp `2026-10-16 15:45:45 -03:00`. Nesta execução, as ferramentas locais confirmaram o hash, a identidade, a versão e a assinatura debug desse arquivo. Assim, os metadados listados como pendentes na evidência original agora estão verificados para este APK.

**Classificação final: APTO COM RESSALVAS PARA PREPARAR V12.** O build debug e seus metadados estão validados, então análise, fechamento de gates, checklist e regressão planejada da futura versão podem continuar. Isso não libera incremento de versão ou geração de AAB: upload key, disponibilidade do código na Console e homologação/conformidade permanecem pendentes. Não instalar no Samsung, publicar, criar commit/tag, fazer merge ou push. A geração do AAB aguarda autorização explícita.

## 2. Estado do Git e configuração

- Branch: `fix/v12-seguranca-conformidade`.
- `HEAD`: `8aa34881755f222f9b090691aaa6127b31b1a363`.
- `v1.0.11-rc.1^{commit}`: `8aa34881755f222f9b090691aaa6127b31b1a363`; tag V11 preservada.
- KGP `2.2.20`, AGP `8.9.1`, Gradle Wrapper `8.11.1`, JBR `21.0.10`.
- `applicationId com.vendassimples.app`; `minSdk 24`, `compileSdk/targetSdk 36`; versão ainda 11/1.0.11.
- A árvore já estava suja com mudanças locais das fases anteriores; foram preservadas. Não houve reset, clean, staging, alteração de código funcional, commit, merge, tag ou push nesta fase.
- As verificações de artefato foram somente leitura. O AAB não foi gerado e nenhum arquivo de assinatura foi alterado.

## 3. Resultado do build reportado pelo usuário

| Evidência | Resultado |
|---|---|
| Comando executado pelo usuário | `gradlew.bat :app:assembleDebug --stacktrace` |
| Ambiente | PowerShell interativo do Windows; informado pelo usuário |
| Gradle | `BUILD SUCCESSFUL`, `BUILD_EXIT_CODE=0` |
| Duração | 3m49s |
| Tarefas | 32 no total: 6 executadas, 26 atualizadas |
| APK encontrado pelo usuário | `app/build/outputs/apk/debug/app-debug.apk`, 7.318.615 bytes |
| SHA-256 informado pelo usuário | `78E4D238FFF8763EF1682A8F225179D8180F574163B0A8F9F5B2131E0B8580F7` |

O log completo do terminal não foi fornecido. Registro os dados do build como evidência do usuário e as verificações do arquivo como evidência obtida diretamente nesta execução. O SHA-256 calculado diretamente no APK corresponde ao fornecido pelo usuário. O build foi de debug; não é build de release nem valida assinatura de upload.

## 4. Verificação direta do APK

Caminho verificado:

```text
<RAIZ_REPOSITORIO>\app\build\outputs\apk\debug\app-debug.apk
```

| Campo | Valor observado |
|---|---|
| Tamanho | 7.318.615 bytes |
| Modificado | `2026-10-16T15:45:45.5658966-03:00` |
| SHA-256 do APK (calculado localmente e igual ao informado pelo usuário) | `78E4D238FFF8763EF1682A8F225179D8180F574163B0A8F9F5B2131E0B8580F7` |
| Application ID (`aapt dump badging`) | `com.vendassimples.app` |
| VersionCode | `11` |
| VersionName | `1.0.11` |
| Compile SDK do manifesto | `36` |
| `apksigner verify` | `Verifies`; assinatura APK Signature Scheme v2 válida |
| DN do certificado | `C=US, O=Android, CN=Android Debug` |
| SHA-256 do certificado debug | `413DC97B411355B68D9A6BAB11C2BFDA5A5A4F77C2AC82CD20B1CC3FAE2F4563` |

Ferramentas encontradas no SDK local:

- `<ANDROID_SDK>\build-tools\36.1.0\aapt.exe`
- `<ANDROID_SDK>\build-tools\36.1.0\apksigner.bat`
- JBR: `C:\Program Files\Android\Android Studio\jbr` (`21.0.10`)

A primeira chamada a `apksigner` não encontrou Java porque `JAVA_HOME` não estava definido nesta sessão Codex. Repeti a validação definindo `JAVA_HOME` apenas no processo para o JBR localizado. O verificador confirmou a assinatura; nenhum segredo ou chave privada foi impresso.

**A assinatura confirmada é de debug.** Ela não comprova que a chave release local seja a upload key aceita pelo Play Console, nem permite atualizar a instalação Play V11 por sideload. Não instalar este APK debug sobre o V11 do Samsung e não desinstalar/limpar dados.

## 5. Comandos PowerShell reutilizáveis para validar o APK

Estes comandos são somente leitura; configuram `JAVA_HOME` apenas no processo PowerShell atual, calculam hash, extraem package/version e verificam assinatura. Não iniciam build, não usam credenciais release e não instalam APK.

```powershell
$ErrorActionPreference = 'Stop'
Set-Location -LiteralPath '<RAIZ_REPOSITORIO>'
$env:JAVA_HOME = 'C:\Program Files\Android\Android Studio\jbr'
$apk = Join-Path (Get-Location) 'app\build\outputs\apk\debug\app-debug.apk'
$sdk = '<ANDROID_SDK>'
$aapt = Join-Path $sdk 'build-tools\36.1.0\aapt.exe'
$apksigner = Join-Path $sdk 'build-tools\36.1.0\apksigner.bat'

if (-not (Test-Path -LiteralPath $apk -PathType Leaf)) { throw "APK ausente: $apk" }
Get-Item -LiteralPath $apk | Select-Object FullName, Length, LastWriteTime
Get-FileHash -LiteralPath $apk -Algorithm SHA256
& $aapt dump badging $apk | Select-String '^package:'
if ($LASTEXITCODE -ne 0) { throw "aapt falhou: $LASTEXITCODE" }
& $apksigner verify --verbose --print-certs $apk
if ($LASTEXITCODE -ne 0) { throw "apksigner falhou: $LASTEXITCODE" }
```

Os resultados observados nesta execução são os da tabela na seção 4. Caso o APK seja substituído por um novo build, repetir as verificações e registrar o novo hash; não reutilizar o SHA-256 anterior.

## 6. Bloqueios e gates antes do AAB V12

| Gate | Estado |
|---|---|
| Compatibilidade KGP/AGP/Gradle | Resolvida e validada na Fase 7B.1; KGP 2.2.20 |
| Build debug empacotado | **Passou**, segundo o resultado PowerShell informado pelo usuário; tamanho, hash, metadados e assinatura debug do artefato foram confirmados localmente |
| Testes unitários | Fase 7B.1: 43 aprovados por variante, sem falhas |
| Lint | Fase 7B.1: zero erros; 67 avisos por variante. Há 40 registros `SetTextI18n` atuais contra 39 contados no relatório 7B; diferença documentada, sem evidência de vínculo com KGP |
| Compilação fontes AndroidTest | Passou na Fase 7B.1; testes instrumentados não foram executados |
| Empacotamento de APK instrumentado | Não verificado/passado nesta fase |
| Homologação física Android/API 24 e upgrade V11→V12 pela Play | Pendente |
| Upload key release correspondente ao Play Console | Pendente; assinatura debug não serve como evidência |
| Disponibilidade/uso de `versionCode 12` na Console | Não verificada |
| Data safety, Billing real, política pública e pendências de conformidade da Fase 7B | Permanecem pendentes conforme relatório Fase 7B |
| Incremento para 12/1.0.12 | Não feito |
| AAB V12 | Não gerado; requer gates completos e autorização própria |

É seguro iniciar a **preparação técnica** da V12 (revisão de pendências, planejamento de alteração isolada de versão e regressão). Ainda não é recomendável incrementar a versão ou gerar AAB enquanto assinatura de upload, disponibilidade do código na Console e homologação/conformidade permanecerem pendentes.

## 7. Recomendação para a Fase 7C

Prosseguir com a preparação V12 sem mudar versão nem gerar AAB:

1. Fechar e registrar as pendências de homologação e conformidade já listadas em `docs/FASE7B_PREPARACAO_RC_V12.md`, incluindo testes instrumentados/Android recente e mínimo, Data safety, política pública e Billing real.
2. Confirmar de forma segura o certificado da upload key contra a Play Console e se `versionCode 12` está disponível. O certificado `CN=Android Debug` verificado aqui é apenas da assinatura debug.
3. Planejar a alteração isolada para `12 / 1.0.12` e a regressão completa após autorização dessa etapa.
4. Solicitar autorização específica antes de gerar o AAB; conferir manifesto, hash, assinatura/upload certificate e validação do bundle antes de qualquer distribuição.

Fase 7C pode ser iniciada como preparação técnica/documental. Geração de AAB, incremento de versão e publicação permanecem fora do escopo autorizado nesta fase.

## 8. Arquivo e limites desta fase

Criado nesta fase: `docs/FASE7B3_VALIDACAO_BUILD_POWERSHELL.md`. Não alterei código, versão ou arquivos de assinatura. Não gerei AAB, não instalei APK, não fiz commit, push, merge, tag nem publicação. A ocorrência de build debug bem-sucedido é atribuída ao usuário; hash, metadados, ferramentas e assinatura debug foram verificados no arquivo disponível neste workspace.
