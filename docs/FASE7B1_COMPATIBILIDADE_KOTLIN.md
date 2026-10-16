# Fase 7B.1 — Compatibilidade Kotlin e regressão

**Data:** 16/10/2026
**Aplicativo:** Vendas Simples — `com.vendassimples.app`
**Branch:** `fix/v12-seguranca-conformidade`
**HEAD/tag preservados:** `8aa34881755f222f9b090691aaa6127b31b1a363` / `v1.0.11-rc.1`
**Versão mantida:** `versionCode 11`, `versionName 1.0.11`

## 1. Resultado

A atualização do Kotlin Gradle Plugin para `2.2.20` ficou compatível com o AGP `8.9.1` e Gradle `8.11.1` segundo a matriz oficial e passou testes unitários, lint e compilação das fontes AndroidTest. O lint terminou sem erros. O empacotamento APK debug continuou bloqueado por `AccessDeniedException` no arquivo `debug.keystore.lock`, mesmo com diretório Android isolado e chave debug temporária válida.

**Classificação: APTO COM RESSALVAS PARA RETOMAR A FASE 7B; NÃO APTO PARA DECLARAR HOMOLOGAÇÃO OU GERAR AAB.** A incompatibilidade de matriz foi resolvida e verificada. O APK debug e o APK instrumentado não foram produzidos nesta execução; testes instrumentados em aparelho, confirmação de upload key e demais pendências de homologação continuam fora desta validação. Nenhum AAB, commit, tag, merge, push ou publicação foi feito.

## 2. Estado inicial e preservação

O estado inicial já era uma árvore de trabalho com alterações locais não commitadas das fases anteriores. A branch estava em `8aa3488`, igual à referência `v1.0.11-rc.1`. Todas as alterações preexistentes foram preservadas; não houve reset, checkout destrutivo, limpeza, staging ou descarte. As alterações anteriores em AGP, Gradle Wrapper, segurança, privacidade, testes e documentação não são atribuídas a esta fase.

A única mudança de configuração nesta fase foi a versão do Kotlin no `build.gradle` raiz. Este relatório foi adicionado como documentação. `app/build.gradle` continua com `applicationId com.vendassimples.app`, `minSdk 24`, `compileSdk 36`, `targetSdk 36`, `versionCode 11`, `versionName 1.0.11` e Billing Library `9.1.0`; produto `full_access` não foi alterado. A tag e o commit V11 continuam intactos.

## 3. Compatibilidade e correção aplicada

| Componente | Configuração validada |
|---|---|
| Android Gradle Plugin | 8.9.1 |
| Gradle Wrapper | 8.11.1 |
| Kotlin Gradle Plugin | 2.2.20 |
| JDK | Android Studio JBR 21 |
| API Android | min 24; compile/target 36 |

O plugin `org.jetbrains.kotlin.android` centralizado no `build.gradle` foi atualizado de `1.9.24` para `2.2.20`. O AGP, Gradle, JDK, SDKs e dependências de produto permaneceram inalterados nesta fase. Não foi necessária atualização de Kotlin em arquivos de aplicação nem mudança arquitetural. A compilação Kotlin de produção e de AndroidTest confirmou que o projeto compila com o novo plugin.

As tabelas oficiais indicam suporte pleno de KGP `2.2.20–2.2.21` para Gradle `7.6.3–8.14` e AGP `7.3.1–8.11.1`; a combinação usada está dentro dessas faixas. AGP `8.9` requer Gradle `8.11.1` e AGP `8.9.1` é o mínimo para API 36. AGP 8.x requer JDK 17 ou superior; JBR 21 satisfaz esse requisito.

Fontes oficiais consultadas:

- [Matriz Kotlin Gradle Plugin, Gradle e AGP](https://kotlinlang.org/docs/gradle-configure-project.html)
- [Compatibilidade Android Gradle Plugin e API](https://developer.android.com/build/releases/about-agp)
- [JDK para builds Android](https://developer.android.com/build/jdks)

## 4. Testes e builds

Comando executado com sucesso após resolver as dependências do plugin:

```text
gradlew.bat --no-daemon --max-workers=1 --console=plain :app:testDebugUnitTest :app:testReleaseUnitTest :app:lintDebug :app:lintRelease :app:compileDebugAndroidTestKotlin
```

Resultado: `BUILD SUCCESSFUL`.

| Verificação | Resultado |
|---|---|
| `:app:testDebugUnitTest` | 43 testes; 0 falhas, 0 erros, 0 ignorados |
| `:app:testReleaseUnitTest` | 43 testes; 0 falhas, 0 erros, 0 ignorados |
| `:app:lintDebug` | 0 erros; 67 avisos |
| `:app:lintRelease` | 0 erros; 67 avisos |
| `:app:compileDebugAndroidTestKotlin` | Passou; apenas compilação, sem executar instrumentados |
| `git diff --check` | Sem erro de whitespace; avisos Git de normalização LF/CRLF em arquivos preexistentes |

Os avisos de lint por variante são: `SetTextI18n` 40, `UseKtx` 10, `IconLauncherShape` 5, `ViewConstructor` 3, `MonochromeLauncherIcon` 2, `UseCompatTextViewDrawableApis` 2, e um cada de `DataExtractionRules`, `DrawAllocation`, `ExifInterface`, `ObsoleteSdkInt` e `UsableSpace`. Não há supressões adicionadas nesta fase. A Fase 7B registrou 66 avisos, incluindo 39 `SetTextI18n`; a contagem atual é uma acima, também nessa categoria. Como a listagem histórica individual não foi preservada em baseline, não é possível afirmar se é um aviso novo ou diferença de contagem anterior. Revisar os itens `SetTextI18n` antes de classificar todos os avisos como preexistentes.

O compilador Kotlin 2.2.20 emitiu avisos de depreciação para `Locale(String, String)` em `FinancialChartPercentage.kt` e `MainActivity.kt`, além de `SOFT_INPUT_ADJUST_RESIZE` em `MainActivity.kt`. A compilação prosseguiu com sucesso. Eles não foram corrigidos nesta alteração de compatibilidade porque isso exigiria mudança de fonte não necessária ao pin do plugin; devem ser avaliados separadamente.

### Empacotamento debug

Também foi tentado:

```text
gradlew.bat --no-daemon --max-workers=1 --console=plain :app:assembleDebug :app:assembleDebugAndroidTest
```

`:app:assembleDebug` falhou em `:app:validateSigningDebug` com:

```text
java.nio.file.AccessDeniedException: ...\.gradle-user\fase7b1\android-user-home\debug.keystore.lock
```

Foi usada uma chave de depuração temporária nova em `.gradle-user/fase7b1/`, sem tocar no keystore release. O bloqueio ocorreu antes de concluir o APK; `:app:assembleDebugAndroidTest` não chegou a ser executada. A chave temporária não é uma chave de release nem credencial de distribuição. A falha reproduz o problema ambiental já registrado na Fase 7B, não prova defeito no código Kotlin. Não foram alteradas ACLs, registro, configurações globais ou arquivos de assinatura release para contorná-la.

## 5. Avisos, erros e comparação com Fase 7B

- Erros de lint `NewApi`, `NotificationPermission` e `SuspiciousIndentation` continuam ausentes; nenhum erro de lint foi introduzido.
- O total automatizado continua 43 testes por variante, todos aprovados.
- A Fase 7B reportou 66 avisos de lint por variante; os XML atuais contêm 67, com a diferença observada em `SetTextI18n` (40 atualmente ante 39 documentados). Isso é uma diferença de contagem a investigar, não evidência suficiente para atribuir regressão ao KGP.
- A atualização do plugin introduziu avisos de depreciação Kotlin registrados acima; não houve erro de compilação Kotlin.
- O bloqueio de assinatura debug permanece. O arquivo APK debug pré-existente tem timestamp anterior à tentativa e não deve ser confundido com saída desta fase.

## 6. Escopo, segurança e riscos

Não foram alterados dados, schema SQLite, cálculos, backup/restauração, política de privacidade, Billing, permissões, limites FREE/FULL, telas ou identidade comercial. Nenhum dado real foi usado. Licença FULL_ACCESS e estado de compra Google Play não foram tocados.

Limitações desta validação:

- Não houve execução de testes instrumentados em aparelho nem homologação física/API 24.
- As fontes AndroidTest compilaram, mas nenhum APK instrumentado foi empacotado nesta tentativa.
- Não foi gerado APK debug novo por causa do lock; a tentativa não valida instalação/execução do aplicativo.
- Não foi gerado AAB e não foi verificada assinatura de upload contra a Google Play Console.
- Avisos de lint permanecem sem erros, e a diferença de uma ocorrência de `SetTextI18n` frente à contagem da Fase 7B deve ser reconciliada.
- Compras/restauração reais, upgrade distribuído via Play, declarações Data safety e outros itens das fases anteriores permanecem pendentes conforme relatórios existentes.

## 7. Arquivos alterados nesta fase

- `build.gradle` — versão do Kotlin Gradle Plugin `1.9.24` → `2.2.20`.
- `docs/FASE7B1_COMPATIBILIDADE_KOTLIN.md` — evidências, resultados e limitações desta fase.

Não houve alteração nos fontes de produção. Artefatos de build e chave de depuração temporária ficaram sob `.gradle-user/`/`app/build/`, ignorados pelo Git; nenhum artefato de distribuição foi produzido.

## 8. Recomendação e próximos passos

A incompatibilidade oficial da matriz Kotlin/AGP/Gradle foi corrigida e a regressão unitária, lint e compilação AndroidTest passaram. Recomenda-se retomar a revisão da Fase 7B com o KGP atualizado, reconciliar a ocorrência adicional de `SetTextI18n` e executar o empacotamento debug em ambiente que permita a operação legítima do lock do keystore. Em seguida, concluir os testes instrumentados e a homologação previstos nas fases anteriores.

Esta evidência **não autoriza** avançar versão, gerar AAB, assinar/publicar, criar tag/commit, fazer merge ou push. A versão e a referência V11 permanecem preservadas. Para essas ações será necessária autorização posterior e revisão das pendências de homologação.
