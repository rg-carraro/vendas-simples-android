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

## Tags e refs observadas

Tags acessíveis no clone: `v1.0.0-base`, `v10-beta-store`, `v1.0.11-rc.1`. Refs observadas incluem `fix/v12-seguranca-conformidade`, `integration/v12-master-validation`, `release/v11-personalizacao`, `master`, `origin/master` e branches de documentação da política. Os estados e commits exatos da coleta estão no [relatório da Fase 8A](FASE8A_HISTORICO_INTEGRAL_V0_V12.md). Não houve consulta remota para verificar se todas as tags/branches locais estão publicadas.

## Mudanças de toolchain que não são versões comerciais

O histórico inclui compile/target API 36/minSdk 24 (29/09), AGP 8.9.1/Gradle 8.11.1 (Fase 4) e KGP 2.2.20 (Fase 7B.1). Essas alterações não são incrementos de produto. Ver [evolução da arquitetura](EVOLUCAO_ARQUITETURA.md) e relatórios técnicos referenciados no [índice](INDEX.md).
