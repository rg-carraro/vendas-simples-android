# Fase 8A — Consolidação do histórico V0–V12

**Data:** 16/10/2026

**Branch:** `integration/v12-master-validation`

**Commit de integração preservado:** `64344855e377dadf5f6b3044cadde6b41e728459`

**Escopo:** documentação, instruções AGENTS e skills; nenhuma alteração de código funcional, versão ou artefato.

## 1. Resumo executivo

A memória histórica do Vendas Simples foi consolidada a partir do Git local, configurações e documentos existentes. O primeiro registro de concepção é `9c265085` (29/09/2026); o primeiro commit da base Android é `5c8c4f2` (29/09/2026). **“V0 — Origem do Vendas Simples” é rótulo documental**, não versão oficial ou tag.

Foram reconstruídos os marcos comprováveis e as lacunas V0–V12, evolução funcional/arquitetural, testes, segurança, decisões e problemas. A origem inspiradora AleJoias foi distinguida do repositório independente Vendas Simples. Não foi inventada associação entre V1–V9, código Android, tags ou releases.

**Conclusão:** memória local pronta para revisão e publicação posterior. Foram criados commits locais somente documentais na branch integrada. Não houve push, PR, publicação GitHub/Play, alteração de `master`, modificação de commits históricos, código funcional, versão ou AAB. A autorização atual pede aguardar antes de push e PR.

## 2. Primeiro commit e origem do projeto

- `9c265085c721a87b4124ab127436d936dcd58ab6` — 29/09/2026 19:58 -0300 — estrutura inicial do MVP/documentação.
- `5c8c4f2bc607cef4fc1354a330bac44b716b69ca` — 29/09/2026 20:12 -0300 — primeira base Android acessível no histórico. Acrescentou app Gradle/Kotlin, `MainActivity.kt`, `Models.kt`, `LocalDatabase.kt`, manifesto, FileProvider e recursos.
- Na primeira árvore Android foram observados `applicationId com.vendassimples.app`, namespace legado `com.example.controlevendas`, `versionCode 2`, `versionName 1.0-comercial`, minSdk 23, compile/target 35 e SQLite schema 2. Isso é snapshot inicial, não “V0 oficial”.
- A referência `alejoiasvendasapp/comercial` é distinta. Procedência documentada em [REFERENCIA.md](REFERENCIA.md); permaneceu somente leitura.

## 3. Reconstrução de versões e lacunas

O histórico alcançável pelas refs consultadas continha 59 commits; foram observadas três tags (`v1.0.0-base`, `v10-beta-store`, `v1.0.11-rc.1`). O commit `81b9191c50858de0a6abd980b7e133093194bccd` estava inalcançável pelas refs e reproduz o mesmo patch de ajuste API 36 encontrado no commit alcançável `883b60a`; foi mantido intacto. Outros objetos inalcançáveis não foram lidos como conteúdo de produto.

| Rótulo | Evidência consolidada |
|---|---|
| V0 | Nome documental da origem. Primeiro commit Android `5c8c4f2`; snapshot metadata `2 / 1.0-comercial`; sem tag V0. |
| V1–V7 | Nenhuma atribuição individual persistente a commits, metadados, tags ou release foi localizada no conjunto de refs/arquivos examinado. Lacunas mantidas. |
| V8 | Existe build preparado `8 / 1.0.0` em `69aef58`, mas nenhum registro o chama explicitamente de V8. |
| V9 | Não foi localizado `versionCode 9`, `versionName` ou marco V9 no histórico acessível. |
| V10 | Marco denominado pelo usuário “beta da Store”; tag local `v10-beta-store`, commit `99f41e7`, metadata `10 / 1.0.0`. Status remoto/Play não verificado. |
| V11 | `11 / 1.0.11`; tag local `v1.0.11-rc.1` em `8aa3488`; tag identifica candidata, não prova aprovação final da Play. |
| V12 | `12 / 1.0.12`; merge local `6434485`; regressão 7H e AAB local validado 7I; Alpha, homologação física e produção não comprovados. Nenhuma tag V12 identificada. |

O nome de tag `v1.0.0-base` aponta para `801ada1`, cuja configuração continha `2 / 1.0-comercial`; seu nome não é espelho dos metadados Android. A tabela completa, tags, SDKs e fontes está em [HISTORICO_VERSOES.md](HISTORICO_VERSOES.md).

## 4. Linha do tempo do produto e da arquitetura

- **29/09:** base local SQLite/Activity; decisões do MVP, FREE e regras de dinheiro; UI, busca, relatórios e navegação receberam ajustes incrementais. Metadados iniciais de dinheiro foram migrados para centavos em `b6befe5` (schema 3); limite FREE aplicado em `b7bef44`.
- **30/09–02/10:** backup restrito ao FULL_ACCESS (`2500ef5`), assinatura release configurada externamente, banco inicializado antes da UI (`b99ae53`), Billing preparado/integrado (`896c0f5`, `a18a4d0`), estado da tela preservado em callback Billing (`c2e97c2`), backup/restauração adicionados (`d8e6fcf`), política/site inicial (`5b496bc`). Em `69aef58`, build metadata era `8 / 1.0.0`.
- **10/10:** marco V10 dado pelo usuário; mudanças em skills/documentação e metadata `10 / 1.0.0`.
- **11–12/10:** V11 personalizou o negócio em preferência local, aplicou nome em cabeçalho/PDF/WhatsApp, padronizou datas de apresentação, melhorou fotos e cards; backup passou a transportar nome com compatibilidade; CSV ganhou precisão/escaping pt-BR; telas financeiras/clientes e barras percentuais foram refinadas. Commits `eae7711`–`8aa3488`.
- **15–16/10:** Fases 2–7 endureceram validação de backup e política offline, corrigiram três erros lint e ajustaram AGP/Gradle/API; KGP foi depois atualizado a 2.2.20. V12 foi preparada, integrada e testada em branch/worktree isolado; AAB local validado, sem envio.

UI programática em Activity e SQLiteOpenHelper continuaram. Schema financeiro atual é 3; formato backup 2 inclui identidade opcional e lê formato 1; entitlement não é incluído. Billing atual documentado: 9.1.0, INAPP `full_access`, preço fornecido pelo Play. Detalhes: [EVOLUCAO_ARQUITETURA](EVOLUCAO_ARQUITETURA.md) e [EVOLUCAO_FUNCIONALIDADES](EVOLUCAO_FUNCIONALIDADES.md).

## 5. Histórico de testes e qualidade

- `MoneyRulesTest` aparece em `4e08155` (29/09); `BackupFormatTest` em `d8e6fcf` (02/10). V11 acrescentou testes de identidade/compatibilidade, transação de restore, CSV, gráfico e UI/backup instrumentados.
- [RELEASE_V11](RELEASE_V11.md) e tag RC registram 35 testes por variante, 44 execuções nativas de layout, 10 checks de UI e 12 testes de backup, além de artefato V11. O relatório 6B.1 registra 13/13 verificações físicas V11 **relatadas pelo usuário**, não executadas pelo Codex.
- Fases 2–4 registraram testes de backup/restore/política/UI e correção lint. Os relatórios preservam tentativas falhas/intermediárias e não as contam como aprovação final.
- **V12 integrada (7H):** 43/43 testes debug e release; lint com 0 erros e 67 avisos por variante; AndroidTest Kotlin compilado, não executado; `assembleDebug` passou.
- **AAB (7I):** validação bundletool/manifesto e assinatura registradas para SHA-256 `4CF6052542FA842EC0796D16006A2912BC825039366C20AE7EB504A608CFA75D`, size 5.361.969 bytes; `12 / 1.0.12`, `com.vendassimples.app`, min24/target36, certificado de upload conferido.
- Físico V12, API 24, update V11→V12 via Play e transações Play Billing reais permanecem pendentes. Ver [HISTORICO_TESTES](HISTORICO_TESTES.md) e os relatórios 7H–7J.

## 6. Segurança, decisões e problemas

Proteções documentadas: SQLite local, migrações versionadas, valores em centavos, backup com metadata/validações/rollback, licença independente de backup, política offline, permissões revisadas e assinatura release com fingerprint conhecido. Segurança implementada, testada e externa está separada em [HISTORICO_SEGURANCA](HISTORICO_SEGURANCA.md).

Pendências não resolvidas por documentação: respostas atuais Data Safety/faixas Play, telemetria/payloads/destinos/retention Billing/Data Transport/CCT, paridade pública da política, execução V12 em API 24/aparelho, upgrade Play e Billing real. Backup local não tem criptografia própria. Não se afirma conformidade completa.

Decisões comerciais confirmadas permanecem em `DECISOES.md`/`MONETIZACAO.md`: FREE 30 vendas, clientes ilimitados, compra única FULL_ACCESS, produto `full_access`, preço dinâmico Play, sem assinatura, ausência de login/backend próprio para os dados comerciais, CSV gratuito e backup FULL_ACCESS. A decisão não substitui prova de implementação/Console.

O catálogo [PROBLEMAS_E_SOLUCOES](PROBLEMAS_E_SOLUCOES.md) diferencia causa confirmada/hipótese: migração de centavos, ordem de inicialização DB, callback Billing, CSV mal escapado, lint `NewApi`/`NotificationPermission`/`SuspiciousIndentation`, problemas de ambiente ADB/Gradle e lacunas Data Safety. O caso real “tutu — brinco” não foi inspecionado; CSV foi reproduzido com fixture sintética.

## 7. Documentos criados e atualizados

Novos documentos históricos: `docs/INDEX.md`, `HISTORICO_COMPLETO_V0_V12.md`, `HISTORICO_VERSOES.md`, `EVOLUCAO_ARQUITETURA.md`, `EVOLUCAO_FUNCIONALIDADES.md`, `HISTORICO_TESTES.md`, `HISTORICO_SEGURANCA.md`, `PROBLEMAS_E_SOLUCOES.md`, `DECISOES_HISTORICAS.md`, `LICOES_APRENDIDAS.md` e `CONTINUIDADE_CODEX.md`, além deste relatório.

Foram incluídos no arquivo Git os relatórios 7H/7I/7J, que já existiam sem rastreamento neste worktree. Removidos caminhos absolutos pessoais desnecessários e mantidos caminhos relativos ao worktree; nenhuma evidência técnica foi alterada.

## 8. Skills locais e AGENTS

`AGENTS.md` foi atualizado com leitura de histórico, classificação de evidências, registro de problemas, preparação de versões e preservação/staging de worktrees. `README.md` agora aponta para o índice e reflete corretamente o estado da V12.

Dez skills existentes foram atualizadas: `regressao-release`, `ui-ux-android`, `vendas-simples-backup`, `vendas-simples-interface`, `vendas-simples-monetizacao`, `vendas-simples-planejar`, `vendas-simples-referencia`, `vendas-simples-release`, `vendas-simples-seguranca` e `vendas-simples-validar`. As referências exigem leitura histórica pertinente e separam evidência de versão antiga de validação atual. Não foram criadas skills genéricas duplicadas: os escopos sugeridos de Android, SQLite/segurança, teste, release, Billing e Git/documentação ficaram cobertos por `AGENTS.md`, skills existentes e documentos de referência.

## 9. Commits documentais locais

Foram criados antes deste relatório, por staging seletivo:

1. `775ce36023ba3ec3a317983dac31f9d3ea698625` — `docs: add V0-V12 project history archive` (históricos, índice e relatórios 7H–7J sanitizados).
2. `832b9db2067ad01b2be9b11fe32519b3c6c3d2fe` — `docs: add historical continuity workflow` (AGENTS, README e skills).

Este relatório é acrescentado como terceiro commit documental local. Os commits anteriores e o merge `64344855` são ancestrais preservados; nenhum commit histórico foi alterado. Os commits não foram enviados ao remoto.

## 10. Auditoria de informações sensíveis

Nos documentos novos e relatórios 7H–7J foi feita varredura direcionada para chaves privadas, atribuições de senha, padrões comuns de token, e-mail e caminho absoluto de usuário. Nenhuma ocorrência foi encontrada. Os caminhos pessoais dos relatórios 7H–7J foram substituídos por caminhos relativos. AABs/APKs, banco/fotos de cliente, keystore, propriedades de assinatura, logs brutos e dados de Console não foram adicionados. SHA-256 de artefato e fingerprints públicos do certificado de upload foram mantidos para rastreabilidade. A varredura é heurística e não prova matemática de ausência de qualquer conteúdo sensível.

## 11. Estado Git final e preservação

- Branch de trabalho: `integration/v12-master-validation`. O commit `64344855e377dadf5f6b3044cadde6b41e728459` permanece ancestral.
- `fix/v12-seguranca-conformidade` segue no commit `628b9f43ffa5bd0a7e478935729e0e45b3473228`; `release/v11-personalizacao` e a tag V11 permanecem em `8aa34881755f222f9b090691aaa6127b31b1a363`.
- `master` e `origin/master` não foram alterados. O merge local contém `origin/master` como ancestral conforme 7H.
- Tag refs `v1.0.0-base`, `v10-beta-store` e `v1.0.11-rc.1` foram apenas lidas, não modificadas. Nenhuma tag nova.
- Foram preservados o worktree raiz, `.codex/worktrees/privacy-policy-2026-10`, `.worktrees/privacy-policy-2026-10`, arquivos privados não rastreados, três relatórios G1–G3 e worktree integrado. Não houve reset, clean, checkout destrutivo ou descarte.
- Nenhum arquivo de código funcional, versão ou AAB foi modificado. Nenhum build, upload, push ou publicação ocorreu nesta fase.

Após o commit deste relatório, o worktree integrado fica limpo; o hash final está disponível no histórico Git da branch (não é repetido dentro do próprio commit). O estado não rastreado/preexistente do worktree raiz e os demais worktrees foram preservados e permaneceram fora dos commits desta fase.

## 12. Plano para GitHub — pendente de autorização

1. Revisar os três commits documentais e o diff final na branch `integration/v12-master-validation`.
2. Confirmar com o responsável se essa branch é a fonte escolhida para PR e comparar o estado do remoto no momento futuro; esta consolidação não consultou nem modificou GitHub.
3. Com autorização explícita separada, enviar somente a branch/documentação revisada e abrir PR para o destino definido pelo responsável. Não atualizar `master` diretamente.
4. Depois do push, validar no GitHub links relativos, renderização de tabelas/acentos e presença dos documentos. Não publicar no Google Play como consequência do PR.

## 13. Próximos passos da V12

1. Reconciliar Data Safety, Billing SDK e versões/faixas ativos com evidência da Play Console e fonte do fornecedor.
2. Confirmar a página pública da política e sua paridade com o asset local.
3. Executar homologação física V11→V12, API 24, fotos, saldos, PDFs/CSV/WhatsApp, offline e reinício, com dados sintéticos/controlados.
4. Executar Billing de teste, reconhecimento/restauração de compra e cenários de falha; manter dados íntegros.
5. Só então reavaliar Alpha. Upload exige autorização específica e o AAB deve ser o arquivo exato registrado na Fase 7I.
6. Produção exige aprovação dos gates de conformidade, regressão e Console; o estado atual não é aprovação de produção.

## 14. Verificações desta fase

`git log --all`, `git branch -a -vv`, `git tag -n`, `git worktree list`, inspeção histórica seletiva (`git show`, `git log --follow`, `git diff`), revisão de documentos/skills e varredura sensível direcionada foram executados. `git diff --check` passou antes dos commits. A verificação local posterior passou para links relativos e whitespace em 27 arquivos Markdown afetados; a varredura sensível direcionada não encontrou padrões de credencial, chave, e-mail ou caminho absoluto nos documentos selecionados.

Não foram executados testes Android nem builds, pois nenhuma alteração de código foi feita. Nenhuma auditoria externa do GitHub/Google Play foi realizada. Próxima ação operacional: revisão humana dos commits/documentos; em seguida, aguardar autorização específica para push/PR.
