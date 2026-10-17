# Fase 7I — Geração e validação do AAB V12

**Data:** 16/10/2026

**Branch:** `integration/v12-master-validation`

**Commit de origem:** `64344855e377dadf5f6b3044cadde6b41e728459` (merge commit da Fase 7H)

**Aplicativo:** `com.vendassimples.app`, versionCode 12, versionName 1.0.12

**Classificação:** **AAB V12 VALIDADO PARA PREPARAÇÃO DE UPLOAD**. Esta classificação não autoriza upload.

## Estado inicial e configuração

O worktree isolado estava na branch e commit esperados. Não havia alterações rastreadas/staged de código; o relatório 7H continuava não rastreado. O versionamento no `app/build.gradle` permaneceu `12 / 1.0.12`, application ID `com.vendassimples.app`, minSdk 24 e targetSdk 36. A política pública local está datada de 15/10/2026 e seu `<main>` coincide textualmente com o asset offline.

O `app/build.gradle` configura `release` com `signingConfigs.release`, lido de `VENDAS_KEYSTORE_PROPERTIES` ou do fallback de `user.home`. O processo Codex usa `user.home` diferente do perfil Windows; por isso, para esta execução apenas, `VENDAS_KEYSTORE_PROPERTIES` apontou à configuração local existente. Foram confirmados os quatro campos necessários e a existência do keystore, sem imprimir ou registrar caminho, alias, senha ou propriedade sensível. A tarefa Gradle `signingReport` reportou para a variante release SHA-256 igual ao certificado de upload esperado. `bundleRelease` executou `validateSigningRelease` e `signReleaseBundle` com essa configuração.

O SHA-1 esperado nesta fase é `FE:B9:79:CD:C1:3F:B4:EE:FA:80:35:8D:70:6F:9F:35:40:0F:34:AE`, conforme a confirmação posterior registrada na Fase 7E e o contexto fornecido para a Fase 7I. A Fase 7D.1 preserva a transcrição anterior com `C1:35`; essa discrepância histórica foi reconciliada posteriormente como `C1:3F`. A Console não foi acessada diretamente nesta execução.

## Artefato gerado

- Comando: `.\gradlew.bat --offline --no-daemon --max-workers=1 --console=plain :app:bundleRelease`.
- Resultado: **BUILD SUCCESSFUL**, 48 tarefas (30 executadas, 18 atualizadas).
- Caminho relativo ao worktree: `app/build/outputs/bundle/release/app-release.aab`.
- Tamanho: **5.361.969 bytes**.
- SHA-256 do arquivo: **`4CF6052542FA842EC0796D16006A2912BC825039366C20AE7EB504A608CFA75D`**.
- Origem: branch `integration/v12-master-validation`, commit `64344855e377dadf5f6b3044cadde6b41e728459`.
- Gerado em 16/10/2026, no worktree isolado. O diretório de saída não existia antes; os arquivos V11 da raiz principal não foram usados como destino.

## Metadados, integridade e assinatura

`bundletool 1.17.2 validate` passou. `dump manifest --module=base` leu o manifesto do bundle e confirmou:

| Campo | Valor do AAB |
|---|---|
| Application ID | `com.vendassimples.app` |
| VersionCode | `12` |
| VersionName | `1.0.12` |
| minSdk | `24` |
| targetSdk | `36` |

O bundletool 1.17.2 disponível no cache não tinha dois artefatos exatos do POM na execução offline. A validação foi executada com o classpath resolvido localmente, substituindo `aapt2-proto` 7.3.0-alpha07 pelo `aapt2-proto` 8.9.1 já usado pelo AGP do projeto e Gson 2.10.1; `validate` e `dump manifest` concluíram com exit code 0. O uso dessa combinação local fica registrado para rastreabilidade.

`jarsigner -verify -certs` terminou com exit code 0 e informou `jar verified`. `keytool -printcert -jarfile` extraiu do certificado contido no AAB:

- **SHA-1:** `FE:B9:79:CD:C1:3F:B4:EE:FA:80:35:8D:70:6F:9F:35:40:0F:34:AE`
- **SHA-256:** `CD:90:9D:AE:E8:16:B2:7F:4E:32:13:52:73:62:25:80:BE:F0:73:F1:8A:8D:EB:56:04:F5:24:90:9C:63:C6:9E`

Os dois fingerprints correspondem ao certificado local de upload validado anteriormente e aos valores esperados na Fase 7I. Essa validação confirma assinatura criptográfica do AAB e correspondência do certificado público; não substitui inspeção do Console nem autoriza envio.

## Inspeção de conteúdo e preservação

Inspeção do ZIP: 936 entradas; zero nomes compatíveis com keystore, propriedades de assinatura, relatórios Fase 7, diretórios privados/worktrees ou AAB histórico. Foram examinadas 666 entradas textuais até 2 MiB por marcadores de chave privada e atribuições típicas de senha/token; zero correspondências. É uma varredura direcionada, não prova matemática de ausência de todo conteúdo sensível.

Os AABs V11 na raiz principal foram verificados antes e depois da geração; ambos permanecem com 5.320.483 bytes e SHA-256 `D751F51DC69AC2DA2784D841374288E4456BD666E2A5237C2E91C1EE9D47AABA`. O novo arquivo está no worktree isolado e não os sobrescreveu.

## Estado Git final e limites

Nenhuma alteração rastreada de código ou configuração foi feita nesta fase. Este relatório e o relatório 7H estão não rastreados; saídas Gradle/AAB permanecem locais/ignoradas. Não houve commit documental, alteração de keystore, mudança de versão, push, merge adicional, rebase, tag, upload Alpha ou publicação.

Permanecem pendentes antes de Alpha: homologação física e upgrade V11→V12, compra/restauração real da FULL_ACCESS, conferência na Play Console da disponibilidade de versionCode 12 e conclusão da revisão de Data Safety/Billing. A página pública atual não foi confirmada por navegador nesta fase. O AAB está validado tecnicamente para a preparação do upload, mas nenhum envio à Play foi feito ou autorizado.

**Próxima ação:** revisar este relatório e o AAB exato; concluir homologação/conformidade e, em fase separada, obter autorização explícita antes de qualquer upload.
