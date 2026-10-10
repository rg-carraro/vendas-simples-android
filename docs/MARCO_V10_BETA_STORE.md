# Marco V10 — teste beta da Store

Data: 10/10/2026. Identificação beta fornecida pelo usuário nesta sessão;
o Play Console não foi consultado. Publicação solicitada no GitHub:
`rg-carraro/vendas-simples-android`, branch master, label `v10-beta-store`.

Código existente: applicationId com.vendassimples.app, versionCode 10,
versionName 1.0.0. A revisão atual modifica somente documentação/instruções,
publicando também as alterações de aplicativo, recursos e ferramenta de UI
que já estavam pendentes. Hashes dos arquivos de app/tools foram comparados
antes/depois da revisão para garantir que não receberam novas mudanças.

Lacunas corrigidas: skills de interface/navegação e backup/restauração;
monetização/release alinhadas ao preço exclusivo do Google Play; índice no
README/AGENTS e orientação de validação. Relatório histórico:
[UI/UX e Billing](ATUALIZACAO_ALPHA_UI_BILLING.md).

O relatório anterior registra oito testes unitários aprovados e build
testDebugUnitTest/assembleDebug/bundleRelease bem-sucedido. A revisão documental
não reexecuta build nem testes Android. Os smoke tests intermediários não
homologam o layout final: fonte ampliada, teclado, compra/restauração,
gravação financeira, exportações e regressão completa mantêm os limites
registrados. Confira [validação](VALIDACAO.md).

Label GitHub identifica o marco; não representa aprovação ou publicação na
Google Play. APKs/AABs, caches, bancos, evidências do emulador e assinatura
local não entram no commit.
