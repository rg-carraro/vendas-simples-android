# Fase 16G — build Debug unificada da master

**Data:** 23/10/2026

**Commit-fonte:** `a742c9139950c6098b4a4960c181e97a45b48fb7` (merge PR #7)

**Worktree de build:** `.worktrees/fase16g-build-unificada`, destacada no commit-fonte

**Versão:** `versionCode 14`, `versionName 1.0.14`

**Distribuição:** sem AAB, upload ou publicação.

## APK gerado

- **Arquivo:** `C:\Users\rgcar\git\vendas-simples-android\dist\debug\VendasSimples_Build_Unificada_Debug.apk`
- **Tamanho:** 7.331.503 bytes
- **SHA-256:** `31922EBD99CA55DE27DD5698A9C93E150D5C52F9B7AB418E8586229E4A554258`
- **ApplicationId:** `com.vendassimples.app.fase16` (sufixo Debug existente, separado do package oficial `com.vendassimples.app`)
- **Rótulo:** `Vendas Simples — Build Unificada`
- **Assinatura:** APK Debug verificado; APK Signature Scheme v2 válido, um signatário.

O APK veio do código integrado no SHA acima. A única alteração local de origem para este artefato foi o rótulo no manifesto Debug; o sufixo `.fase16` existente foi mantido porque o runner de backup aceita esse namespace isolado. `applicationId`, `versionCode` e `versionName` da variante Release não foram alterados. O worktree de build preserva essa alteração Debug local, sem commit.

## Verificações executadas

| Verificação | Resultado |
|---|---|
| `:app:testDebugUnitTest` | **46/46 passou** |
| `:app:testReleaseUnitTest` | **46/46 passou** |
| `:app:connectedDebugAndroidTest` | **38/38 passou** no Galaxy S22 SM-S906E, Android 16/API 36; testes sintéticos de backup |
| `:app:compileDebugAndroidTestKotlin` | **Passou** |
| `:app:lintDebug` | **Passou**, 0 erros e 63 avisos |
| `:app:lintRelease` | **Passou**, 0 erros e 65 avisos |
| `:app:assembleDebug` | **Passou** |
| Manifesto/assinatura/hash | **Passou**; package, label e metadados conferem; SHA-256 da cópia coincide com o build |

A execução instrumentada offline foi interrompida antes dos testes por falta em cache do plugin host Android Test `31.9.1`; a repetição online concluiu 38/38 sem falhas. Os testes de UI não foram repetidos nesta build de rótulo; a [Fase 16C](FASE16C_HOMOLOGACAO_FINAL.md) já registra 76/76 execuções instrumentadas de UI nas quatro combinações de tema e escala de fonte, no mesmo código integrado. Teste visual manual desta build permanece disponível ao proprietário.

## Galaxy S22 e proteção da V14

O APK foi instalado em paralelo via `adb install -r` como `com.vendassimples.app.fase16`. A V14 oficial `com.vendassimples.app` continuou em `versionCode 14` / `versionName 1.0.14` e manteve `lastUpdateTime 2026-10-21 12:53:45` antes e depois dos testes. O runner usou fixtures sintéticas; nenhum dado comercial foi criado, alterado ou apagado no app oficial.

Para instalar ou atualizar o app paralelo no S22 conectado:

```powershell
adb -s RQCTB02SZWL install -r "C:\Users\rgcar\git\vendas-simples-android\dist\debug\VendasSimples_Build_Unificada_Debug.apk"
adb -s RQCTB02SZWL shell monkey -p com.vendassimples.app.fase16 -c android.intent.category.LAUNCHER 1
```

Este APK Debug destina-se a teste. A V14 da Play não foi removida nem atualizada. Nenhum APK/AAB de Release foi gerado; preparação de V15 depende de autorização específica.
