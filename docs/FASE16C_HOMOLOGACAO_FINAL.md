# Fase 16C — homologação final e APK Debug

**Data:** 23/10/2026
**Branch/worktree:** `feature/fase16-light-dark-mode`, `.worktrees/fase16-light-dark-mode`
**Base V14:** `dfb87e6ddb294834bde8f6ae044b4a1d58ed7525`, versionCode 14 / versionName 1.0.14.
**Dispositivo:** Samsung Galaxy S22 SM-S906E, Android 16/API 36, 1080×2340, 450 dpi.

**Adendo Fase 16D — homologação manual:** o proprietário informou que concluiu os testes manuais no Galaxy S22 e que todos os cenários executados passaram. Essa confirmação é `RELATADA PELO PROPRIETÁRIO`; os cenários individuais não foram enumerados nesta conversa e não são apresentados como observados pelo Codex. O resultado automatizado desta fase está documentado abaixo.

## Diagnóstico e correção do backup

O teste sintético reproduziu um resíduo ao validar arquivo que contém cabeçalho SQLite, mas está corrompido. `SQLiteDatabase.openDatabase` aciona o handler SQLite de corrupção, que renomeia o arquivo temporário para `vendas_validate_….db.corrupt`. O `BackupManager.removeTemporary` removia o caminho original e os sidecars `-wal`, `-shm` e `-journal`, mas não esse sibling `.corrupt`.

A comparação confirmou que o mesmo cleanup constava no código-base V14 e no código pré-Fase16; o problema é preexistente e independente da paleta/tema. A correção mínima acrescenta a remoção de `file.path + ".corrupt"` ao helper de temporários, usado por criação, validação e restauração. Os caminhos são nomes de temporários controlados pelo app. O banco operacional, schema, metadados/formato do backup, permissões e fluxo de restauração válida não foram alterados. Risco residual: `File.delete()` mantém o padrão anterior de remoção best-effort; o teste sintético confirma que o artefato é removido neste cenário Android.

## Resultados dos testes

| Validação | Resultado |
|---|---|
| Teste específico `testRejectsCorruptSQLiteWithValidHeader` | **Passou** após a correção. |
| Suíte instrumentada de backup | **38/38 passou**, no namespace sintético `.fase16.test`; cobre backups válidos/inválidos, interrupção/erro de leitura/espaço, restauração e preservação do banco sintético anterior. |
| Testes de interface | **19/19 em cada combinação**, total 76/76: claro 1,0; escuro 1,0; escuro 1,3; claro 1,3. Runner aponta para `com.vendassimples.app.fase16`. |
| Unit tests debug e release | **46/46** em cada variante. |
| Compilação AndroidTest | **Passou** (`:app:compileDebugAndroidTestKotlin`). |
| Lint debug/release | **Passou**, 0 erros; 63 avisos debug e 65 release. |
| Builds | **Passou** `:app:assembleDebug` e `:app:assembleDebugAndroidTest`, com Gradle offline. AAB não foi gerado. |
| Diff check | `git diff --check` sem erros; aviso Git de conversão LF/CRLF existente em `styles.xml`. |

Os testes instrumentados exercitam tokens e contraste WCAG, diálogos, estados/filtros, totais/gráfico financeiros, preferências, componentes de foto e geração de PDF com fixtures sintéticas. Não equivalem a navegar visualmente por todas as telas. A tela inicial, menu e diálogo “Nome do negócio” foram inspecionados no S22 em verificações visuais anteriores da mesma implementação de UI; nesta Fase 16C os testes de interface foram repetidos nas quatro combinações acima. Capturas não foram retidas.

## Matriz visual e pendências

| Fluxo no S22 | Estado |
|---|---|
| Tema claro/escuro, fonte normal/ampliada | **Testes instrumentados passaram** nas quatro combinações; configurações do sistema foram restauradas. |
| Diálogos Material, contraste semântico, texto e ação desabilitada | **Passou** nos testes instrumentados. |
| Tela inicial/menu e “Nome do negócio” | Inspeção manual parcial já registrada na Fase 16; não equivale ao percurso completo em cada configuração. |
| Homologação manual comunicada pelo proprietário | **Aprovado, segundo relato do proprietário** | O proprietário afirma que concluiu os cenários manuais no S22 e todos os cenários realizados passaram. Não foi fornecida uma lista individual por fluxo; esta evidência é atribuída ao proprietário. |
| Clientes, vendas, pagamentos, relatórios, PDFs e SAF | **Aprovado segundo o relato acima; evidência independente pendente** | Os testes automatizados cobrem regras/componentes sintéticos, mas não constituem navegação manual observada pelo Codex. |
| Paisagem e outros tamanhos/densidades | **Não especificado no relato manual** | A execução instrumentada ocorreu no S22 em retrato, 1080×2340. |

O proprietário aprovou os cenários manuais que executou; a implementação e a regressão automatizada foram aprovadas neste ambiente. A cobertura manual exata por fluxo/orientação não foi detalhada nos registros técnicos.

## APK Debug entregue

- **Arquivo:** [VendasSimples_Fase16_LightDark_Debug.apk](../dist/debug/VendasSimples_Fase16_LightDark_Debug.apk)
- **Caminho absoluto:** `C:\Users\rgcar\git\vendas-simples-android\.worktrees\fase16-light-dark-mode\dist\debug\VendasSimples_Fase16_LightDark_Debug.apk`
- **Tamanho:** 8.660.187 bytes (8,26 MiB).
- **SHA-256:** `C036F5E3996E7A02AD0F55456E123A6FA08B75C88FCB2491F936B74120CCF1BD`.
- **applicationId:** `com.vendassimples.app.fase16`.
- **versionCode / versionName:** 14 / 1.0.14 (inalterados; são metadados da base V14, não uma V15).
- **Rótulo:** “Vendas Simples — Teste Light/Dark”.
- **Build:** `assembleDebug` concluído em 23/10/2026 às 15:45:40 (-03:00); `aapt` confirmou package/version/rótulo e `apksigner verify` confirmou assinatura APK v2. Instalado em paralelo no S22. O pacote `.fase16.test` do runner foi desinstalado depois dos testes; o app `.fase16` permanece instalado para uso manual.
- **Compras:** o APK tem package ID distinto do cadastrado para distribuição oficial (`com.vendassimples.app`); não foi feita compra. Nenhum produto de teste ou publicação foi configurado.

Instalação/atualização no S22 conectado por ADB, a partir do PowerShell:

```powershell
adb install -r "C:\Users\rgcar\git\vendas-simples-android\.worktrees\fase16-light-dark-mode\dist\debug\VendasSimples_Fase16_LightDark_Debug.apk"
adb shell monkey -p com.vendassimples.app.fase16 -c android.intent.category.LAUNCHER 1
```

Esse identificador permite coexistência com o app oficial. O pacote oficial não deve ser removido ou atualizado por este APK. Para instalar manualmente sem USB, copie o APK para o aparelho, abra o arquivo e autorize a instalação da origem conforme o Android solicitar.

## Integridade da V14 e estado Git

Antes e depois dos testes, ADB confirmou `com.vendassimples.app`, versionCode 14, versionName 1.0.14 e lastUpdateTime `2026-10-21 12:53:45`. O pacote da V14 não foi instalado, desinstalado, limpo nem usado como alvo instrumentado; nenhum banco real foi lido ou alterado. O aparelho terminou em tema claro e fonte 1,0.

Na atualização deste relatório, a integração Fase 16D está em andamento e deve ser registrada pelo SHA do merge no histórico de versões após sua conclusão. Não houve AAB ou publicação e nenhuma preparação de V15 foi feita. A autorização atual cobre integração Git, sem distribuição na Google Play.
