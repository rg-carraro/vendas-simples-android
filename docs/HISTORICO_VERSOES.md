# Correspondência histórica de versões

## Critério

“V0”, “V10” e outros nomes informais, `versionCode`/`versionName`, tags Git e releases da Google Play são identificadores distintos. Só se relacionam quando uma fonte os associa explicitamente. O clone local não comprova, por si só, que uma tag foi enviada ao GitHub ou que um artefato foi publicado.

## Linha do tempo com lacunas explícitas

| Marco | Metadados Android comprovados | Período/commit ou tag | Estado e evidência |
|---|---|---|---|
| **V0 — Origem do Vendas Simples** (rótulo documental) | No primeiro commit Android, `versionCode 2`, `versionName 1.0-comercial`, min 23, compile/target 35. Isso não constitui versão oficial “V0”. | 29/09/2026; concepção/documentação `9c265085`; primeira base Android `5c8c4f2`. | **CONFIRMADO** por Git e Gradle. Não há tag V0; não criar retroativamente. |
| **V1** | Nenhum `versionCode`/`versionName` identificado como V1. | Sem commit/tag de V1 identificado. | **NÃO COMPROVADO**; não inferir do primeiro snapshot Android. |
| **V2** | Nenhuma correspondência versionada identificada. | Sem commit/tag de V2 identificado. | **NÃO COMPROVADO**. |
| **V3** | Nenhuma correspondência versionada identificada. | Sem commit/tag de V3 identificado. | **NÃO COMPROVADO**. |
| **V4** | Nenhuma correspondência versionada identificada. | Sem commit/tag de V4 identificado. | **NÃO COMPROVADO**. |
| **V5** | Nenhuma correspondência versionada identificada. | Sem commit/tag de V5 identificado. | **NÃO COMPROVADO**. |
| **V6** | Nenhuma correspondência versionada identificada. | Sem commit/tag de V6 identificado. | **NÃO COMPROVADO**. |
| **V7** | Nenhuma correspondência versionada identificada. | Sem commit/tag de V7 identificado. | **NÃO COMPROVADO**. |
| Marco `v1.0.0-base` | Na árvore marcada, `2 / 1.0-comercial`, min 23, compile/target 35. | Tag local anotada `v1.0.0-base` → `801ada1`. | **CONFIRMADO**. O nome da tag não é a versão Android contida nesse commit. |
| Preparação “build 8” | `8 / 1.0.0`, min 24, compile/target 36. | 02/10/2026, `69aef58` (“preparar release 1.0.0 build 8”). | **CONFIRMADO** na configuração do commit. Build/publicação na Play não comprovados por esse dado. Não há prova de que seja um marco informal chamado V8. |
| **V8** | Foi encontrado `versionCode 8` / `versionName 1.0.0` no commit `69aef58`, mas nenhum documento associa esse build ao rótulo informal V8. | Preparação de release em 02/10/2026. | **METADADO CONFIRMADO; equivalência V8 NÃO COMPROVADA**. Sem evidência de status Play. |
| **V9** | Não foi localizado `versionCode 9`, `versionName` de V9 ou release correspondente nas refs/commits acessíveis. | Sem commit/tag de V9 identificado. | **NÃO COMPROVADO**; lacuna permanece. |
| **V10 beta da Store** | `10 / 1.0.0`, min 24, compile/target 36. | 10/10/2026; commit `99f41e7`; tag anotada local `v10-beta-store`. | **CONFIRMADO** por tag/commit e metadados; a descrição do marco foi fornecida pelo usuário. Faixa e status remotos da Play não foram verificados nesta reconstrução. |
| **V11** | `11 / 1.0.11`, `com.vendassimples.app`, min 24, compile/target 36. | 11–12/10/2026; tag `v1.0.11-rc.1` → `8aa3488`. | **CONFIRMADO** por configuração, tag e [RELEASE_V11](RELEASE_V11.md). Tag identifica RC; não comprova release final da Play. O tag message registra AAB local, testes e pendências. |
| **V12** | `12 / 1.0.12`, applicationId igual, min 24, compile/target 36. | 16/10/2026; configuração em `eb717b7`; integração `6434485` (branch `integration/v12-master-validation`). | **CONFIRMADO** por Gradle/merge e [Fases 7H–7J](INDEX.md#documentos-de-produto-e-implementação). AAB local assinado e validado na Fase 7I. Upload Alpha, homologação física V12 e produção **não comprovados**. Não há tag V12 identificada. |
| **V13** | `13 / 1.0.13`, applicationId `com.vendassimples.app`, min 24, compile/target 36. | 19/10/2026; feature `09f49af` e merge da V13 na master `5593215` (PR #4; Git/GitHub). | **CONFIRMADO** no Gradle e no merge. AAB release local: 5.364.930 bytes, SHA-256 `33D0906198E439304887A59AB683AF723636DEEE6ACAF51025BD073C42ED6642`; certificado SHA-256 `CD:90:9D:AE:E8:16:B2:7F:4E:32:13:52:73:62:25:80:BE:F0:73:F1:8A:8D:EB:56:04:F5:24:90:9C:63:C6:9E` conforme evidência de build anterior. Em 19/10/2026, o proprietário relatou Play Console: teste fechado Alpha, envio 6, “Em análise”, aproximadamente oito dias restantes. Esse estado da Console é **RELATADO PELO PROPRIETÁRIO**, não verificado independentemente; não afirma aprovação, disponibilidade aos testadores ou publicação pública. Atualização direta via Play e outras pendências estão em [Pendências atuais](PENDENCIAS_ATUAIS.md). |

## Tags e refs observadas

Tags acessíveis no clone: `v1.0.0-base`, `v10-beta-store`, `v1.0.11-rc.1`. Refs observadas incluem `fix/v12-seguranca-conformidade`, `integration/v12-master-validation`, `release/v11-personalizacao`, `master`, `origin/master` e branches de documentação da política. Os estados e commits exatos da coleta estão no [relatório da Fase 8A](FASE8A_HISTORICO_INTEGRAL_V0_V12.md). Não houve consulta remota para verificar se todas as tags/branches locais estão publicadas.

## Mudanças de toolchain que não são versões comerciais

O histórico inclui compile/target API 36/minSdk 24 (29/09), AGP 8.9.1/Gradle 8.11.1 (Fase 4) e KGP 2.2.20 (Fase 7B.1). Essas alterações não são incrementos de produto. Ver [evolução da arquitetura](EVOLUCAO_ARQUITETURA.md) e relatórios técnicos referenciados no [índice](INDEX.md).

## Histórico de `versionCode` observado no Gradle

Esta sequência é comprovada por commits e **não** deve ser renomeada como sequência informal V1–V12:

| `versionCode` / `versionName` | Commit/data | Conteúdo observável na alteração |
|---|---|---|
| `2 / 1.0-comercial` | `5c8c4f2`, 29/09 | Primeira árvore Android. |
| `3 / 1.0.0` | `b018719`, 30/09 | `minSdk` passa 23→24, metadata 1.0.0; target já API 36. |
| `4 / 1.0.0` | `b99ae53`, 01/10 | Corrige ordem de inicialização do banco antes da tela. |
| `5 / 1.0.0` | `896c0f5`, 01/10 | Inclui Billing Library 9.1.0 e preparação inicial. |
| `6 / 1.0.0` | `a18a4d0`, 01/10 | Integra fluxo de licença completa via Billing. |
| `7 / 1.0.0` | `c2e97c2`, 02/10 | Ajuste para evitar reconstrução da interface pelo Billing. |
| `8 / 1.0.0` | `69aef58`, 02/10 | Prepara build 8; esse commit só altera o `versionCode`. |
| `9` | Não localizado nas refs/commits examinados. | O salto 8→10 é observado; motivo do salto não documentado. |
| `10 / 1.0.0` | `99f41e7`, 10/10 | Build 10 e refinamento de UI/Billing descrito abaixo e em `ATUALIZACAO_ALPHA_UI_BILLING.md`. |
| `11 / 1.0.11` | `eae7711`, 11/10 | Identidade personalizada do negócio e preparação V11. |
| `12 / 1.0.12` | `eb717b7`, 16/10 | Atualiza metadados V12; integração final no merge `6434485`. |
| `13 / 1.0.13` | `09f49af`, 19/10 | Tela Sobre e contato; PR #4 integrada no merge `5593215`. AAB local e estado Alpha relatado documentados acima. |

## Dossiês por rótulo V0–V13

Os campos são agrupados para legibilidade. “Não documentado” quer dizer que as fontes locais examinadas não sustentam atribuição; não quer dizer que o trabalho não tenha ocorrido.

### V0 — Origem do Vendas Simples

- **Identidade/data/ref:** nome documental; 29/09/2026; concepção `9c265085`, primeira árvore Android `5c8c4f2`; sem tag V0.
- **Metadata:** snapshot inicial code 2 / `1.0-comercial`, applicationId `com.vendassimples.app`, min23, compile/target35. Não há “versionName V0”.
- **Produto no primeiro snapshot:** clientes, vendas com parcelas, pagamentos, fotos, resumos/listas, PDF, CSV e ações WhatsApp aparecem na Activity/SQLite inicial. Há menu local de dados/backup, mas ainda não prova gate FULL_ACCESS.
- **Arquitetura/dados:** Activity programática, modelos Kotlin e `SQLiteOpenHelper`, schema 2; tabelas de clientes/vendas/pagamentos/fotos e tabelas legadas de marcação. Dinheiro ainda estava no formato pré-migração a centavos.
- **Monetização/segurança/testes:** decisões FREE/centavos e primeira suíte aparecem em commits seguintes; Billing Play não estava integrado no commit inicial. FileProvider está na base; resultado de teste/build/distribuição específico de V0 não localizado.
- **Problemas/decisões/lição:** não há relatório inicial de bugs nem homologação. O primeiro commit é linha de base acessível, não certificação de qualidade nem prova de origem anterior dos recursos.

### V1

Rótulo V1, código Android correspondente, data/período, commits de release, tag, funcionalidades adicionadas/modificadas, bugs, arquitetura/schema, monetização/segurança, testes/build/AAB, status Play, problemas, decisões e lições: **NÃO DOCUMENTADOS como marco V1** nas fontes locais examinadas. Há commits de desenvolvimento de 29/09, mas não foram associados a esse rótulo.

### V2

Rótulo V2, metadata, data/ref/tag, funcionalidades, correções, arquitetura/schema, monetização/segurança, resultados de teste/build/AAB/Play, problemas, decisões e lições: **NÃO DOCUMENTADOS como marco V2**. Não equiparar code2 inicial a V2.

### V3

Rótulo V3 e release própria: **NÃO DOCUMENTADOS**. Há `versionCode 3 / 1.0.0` em `b018719` como parte da configuração técnica de 30/09, mas nenhuma fonte local o chama V3 ou comprova distribuição.

### V4

Rótulo V4 e release própria: **NÃO DOCUMENTADOS**. Há `versionCode 4 / 1.0.0` em `b99ae53`, associado à correção da ordem de abertura do banco/UI. Teste, artefato e Play não atribuídos especificamente a V4.

### V5

Rótulo V5 e release própria: **NÃO DOCUMENTADOS**. Há `versionCode 5 / 1.0.0` em `896c0f5`, que adiciona Billing 9.1.0 e prepara sua integração. Não há evidência de Play release sob o rótulo V5.

### V6

Rótulo V6 e release própria: **NÃO DOCUMENTADOS**. Há `versionCode 6 / 1.0.0` em `a18a4d0`, com integração de fluxo de Licença Completa. Estado de compra real/Play não comprovado por esse commit.

### V7

Rótulo V7 e release própria: **NÃO DOCUMENTADOS**. Há `versionCode 7 / 1.0.0` em `c2e97c2`, com correção de reconstrução de UI no callback Billing. Teste/build/Play específico não atribuído ao rótulo V7.

### V8

O rótulo informal V8 não foi confirmado. `versionCode 8 / 1.0.0` foi preparado em `69aef58` (02/10), e esse commit só atualiza o code. A árvore ancestral já contém a integração Billing e backup de `d8e6fcf`; isso não prova qual bundle foi distribuído. Tag `v1.0.0-base` é anterior e aponta a code2/`1.0-comercial`, não ao build8. Testes/AAB/Play específicos do build8 não confirmados.

### V9

Rótulo V9, `versionCode 9`, `versionName`, commits, tag, funcionalidade, correções, schema, monetização/segurança, testes/build/AAB/Play, problemas, decisões e lições: **NÃO COMPROVADOS**. O Gradle observado salta de code8 para code10 em `99f41e7`; motivo não registrado.

### V10 — beta da Store

- **Identidade/ref:** usuário denominou “V10 beta da Store”; commit/tag local `99f41e7`/`v10-beta-store`, 10/10; metadata `10 / 1.0.0`, min24, target36. Console não consultada.
- **Funcionalidades/mudanças:** refinamento de detalhes/listas, cabeçalho e navegação Voltar pela mesma ação, filtros responsivos, remoção de botões Voltar redundantes, diálogos roláveis e exibição de pagamento somente quando há saldo. Nova Venda/descartar, ícones e acessibilidade preservados. Billing passou a impedir compra quando produto/preço/conexão não estão disponíveis; ProductDetails e preço do Play mantidos.
- **Arquitetura/dados/comercial:** Activity/SQLite preservados; o relatório afirma sem mudança de schema, migração, cálculos, limite FREE30 ou modelo FULL_ACCESS. Billing 9.1.0, `full_access` e `full-access` permanecem.
- **Problemas/qualidade:** causa confirmada para navegação inconsistente era `onBackPressed` manter destinos diferentes; o dispatcher passou a compartilhar a ação configurada. Dialogs tinham espaço excessivo/sem limite e ação de pagamento sem saldo apesar de o formulário rejeitar o valor; o layout e a condição visual foram corrigidos sem mudar cálculo. Foram registrados 8 testes unitários aprovados; build/testDebugUnitTest/assembleDebug/bundleRelease passaram. AVD Android15/API35 teve checks de UI, mas tentativas em fonte 200% tiveram ANR de System UI/árvore nula; homologação final ficou pendente.
- **Artefato/Play/pendências:** o relatório de 07/10 registra AAB local de 5.307.635 bytes, hash `0ac43af0fab778976430752e5a08e4192ea2552a619dec1b93b631bcb25c31b5`, code10/name1.0.0. Isso é evidência local/documentada, não prova de upload/aprovação. Upgrade Play, Billing real, API24 e telas ampliadas permaneceram no checklist.

Fontes: [MARCO_V10_BETA_STORE](MARCO_V10_BETA_STORE.md), [ATUALIZACAO_ALPHA_UI_BILLING](ATUALIZACAO_ALPHA_UI_BILLING.md), commit `99f41e7`. Não atribuir a V10 as verificações adicionadas em sessões posteriores.

### V11 — personalização

- **Identidade/ref:** code11/name1.0.11, `com.vendassimples.app`; commit/tag `8aa3488`/`v1.0.11-rc.1`, 11–12/10.
- **Funcionalidades:** personalização FREE, nome em tela inicial/PDF/WhatsApp; exibição de datas pt-BR com armazenamento ISO; fotos/cartões; CSV brasileiro escapado com moeda exata; Financeiro/relatório por cliente compactos; barras com valores/percentuais; cards de venda compactos sem alterar prioridade/status.
- **Arquitetura/schema/comercial:** nome em SharedPreferences; schema financeiro 3 mantido. Backup metadata formato2 inclui nome e lê formato1; licença não é exportada. FREE30, compra única `full_access` Billing9.1.0 e FULL_ACCESS preservados.
- **Testes/artefato:** release doc e tag registram até 35 testes unitários por variante e regressões automatizadas; AAB local SHA-256 `D751F51DC69AC2DA2784D841374288E4456BD666E2A5237C2E91C1EE9D47AABA`. O tag message lista 44 execuções nativas de layout, 10 checks de UI e 12 de backup. O usuário relatou ainda 13/13 cenários físicos V11 em S22+/Android16; atribuição e limites em Fase6B.1.
- **Play/pendências:** V11 havia sido instalada pela Play conforme evidência relatada; tag permanece RC. API24, lint inicial com erros e Billing real foram pendências anteriores, tratadas/reavaliadas em fases seguintes; não transpor status para V12.

Fonte principal: [RELEASE_V11](RELEASE_V11.md) e [HISTORICO_TESTES](HISTORICO_TESTES.md).

### V12 — segurança e compatibilidade

- **Identidade/ref:** code12/name1.0.12, `com.vendassimples.app`, min24/compile36/target36; metadados `eb717b7`; merge `6434485` na branch isolada.
- **Mudanças:** endurecimento/validação de SQLite backup/restore e transferência; política de privacidade local/offline e controle de notificações; correções API/lint; AGP8.9.1, Gradle8.11.1, KGP2.2.20; documentação consolidada. Schema financeiro e regras comerciais permanecem.
- **Testes/build/AAB:** 43/43 debug e release, lint 0 erros/67 avisos por variante; AndroidTest compilado, sem runtime. AAB local 5.361.969 bytes, SHA-256 `4CF6052542FA842EC0796D16006A2912BC825039366C20AE7EB504A608CFA75D`, assinado e validado em Fase7I.
- **Play/segurança/problemas:** sem upload/produção comprovados; Data Safety/Billing SDK, política pública, update físico V11→V12, API24 e Billing real pendentes. Ver Fases 6C e 7H–7J.

### V13 — Sobre e contato

- **Identidade/ref:** `versionCode 13` / `versionName 1.0.13`, applicationId `com.vendassimples.app`, min24/compile36/target36; PR #4 integrada à master no merge `5593215`.
- **Mudanças:** item “Sobre o aplicativo” no menu; ícone oficial, nome/subtítulo, versão dinâmica, descrição, desenvolvedor `RGCarraro`, contato único por `ACTION_SENDTO` e acesso à política existente. O corpo do e-mail não inclui dados comerciais; o usuário revisa e envia.
- **Testes:** 46 testes unitários aprovados, `compileDebugAndroidTestKotlin`, `assembleDebug` e `lintDebug` aprovados; 0 erros/67 avisos de lint. O proprietário relatou verificação visual e de fluxos no S22+ e restauração de backup V12→V13 Debug; Codex não repetiu esses testes. Instrumentados não foram executados em runtime; atualização direta da Play não foi comprovada.
- **Distribuição:** AAB local release identificado pelo hash acima. Em 19/10/2026 o proprietário relatou upload Alpha 6 “Em análise”; não há comprovação independente de estado final, aprovação ou disponibilidade. A V13 não deve ser descrita como produção/publicação pública. O relatório da Fase 13 é um retrato da etapa anterior e seu estado de artefato/distribuição foi superado por esta atualização.

### V14 — identidade dos PDFs e acesso à política (release local preparado)

- **Identidade/ref:** branch local `feature/v14-pdf-identidade-privacidade`, baseada no master `88c9bb1f9b5455a4b03230932d4b2de488ab780a`; metadados alterados nesta branch para `versionCode 14` / `versionName 1.0.14`. Não integrada nem enviada à Play.
- **Mudanças:** remove atalho duplicado de política no menu, mantendo a ação dentro de Sobre, leitura local e link HTTPS. Três ações de PDF convergem para `gerarPdfGenerico`; este desenha o ícone oficial, nome configurado/fallback e texto sutil na primeira página, sem cabeçalho repetido em páginas seguintes.
- **Testes/build:** 46 testes unitários debug e 46 release passaram; `compileDebugAndroidTestKotlin`, `lintDebug`, `lintRelease` e `bundleRelease` passaram. Lint: 0 erros, 67 avisos por variante (ver relatório da Fase 14). Runtime AndroidTest e teste API24 não executados por indisponibilidade de ADB/AVD no sandbox.
- **Artefato/distribuição:** AAB local assinado, validado e identificado em [relatório da Fase 14](FASE14_FUNCIONAL_PDF_PRIVACIDADE.md), sem upload à Play. O certificado SHA-256 coincide com o fingerprint da chave de upload conhecido. AAB V13 não foi modificado. Atualização V13→V14 pela Play permanece pendente.
