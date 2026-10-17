# Índice documental do Vendas Simples

Este índice reúne a documentação de produto, arquitetura, validação e histórico. Para entender marcos de desenvolvimento, comece pelo [histórico integral V0–V12](HISTORICO_COMPLETO_V0_V12.md); para continuar uma tarefa, use [Continuidade Codex](CONTINUIDADE_CODEX.md).

## História do projeto

- [Histórico integral V0–V12](HISTORICO_COMPLETO_V0_V12.md) — síntese com fontes e limites de evidência.
- [Correspondência de versões e tags](HISTORICO_VERSOES.md) — metadados Android separados dos nomes V0–V12 e das tags.
- [Evolução da arquitetura](EVOLUCAO_ARQUITETURA.md) — banco, camadas, Billing, backup e toolchain.
- [Evolução das funcionalidades](EVOLUCAO_FUNCIONALIDADES.md) — marcos de produto por commits.
- [Histórico de testes](HISTORICO_TESTES.md) — cobertura e resultados reportados, sem transportar aprovação entre versões.
- [Histórico de segurança](HISTORICO_SEGURANCA.md) — proteções, verificações e pendências.
- [Problemas e soluções](PROBLEMAS_E_SOLUCOES.md) — causas confirmadas, hipóteses e limitações.
- [Decisões históricas](DECISOES_HISTORICAS.md) — decisões confirmadas e sua proveniência.
- [Lições aprendidas](LICOES_APRENDIDAS.md) — práticas extraídas dos registros, sem substituir regras do produto.
- [Continuidade Codex](CONTINUIDADE_CODEX.md) — roteiro e modelo documental para V13 em diante.
- [Relatório da Fase 8A](FASE8A_HISTORICO_INTEGRAL_V0_V12.md) — escopo, auditoria e conclusão desta consolidação.

## Documentos de produto e implementação

- [README](../README.md), [AGENTS](../AGENTS.md), [decisões](DECISOES.md), [MVP](MVP.md), [produto](PRODUTO.md), [backlog](BACKLOG.md) e [roadmap](ROADMAP_PRODUTO.md).
- [Arquitetura e dados](ARQUITETURA.md), [regras financeiras](REGRAS_FINANCEIRAS.md), [validação](VALIDACAO.md) e [exportação CSV](EXPORTACAO_CSV.md).
- [Monetização](MONETIZACAO.md), [segurança e privacidade](SEGURANCA_PRIVACIDADE.md), [investigação Data Safety/Billing](SEGURANCA_DADOS_PLAY.md) e [release Play](RELEASE_PLAY_STORE.md).
- [Release V11](RELEASE_V11.md) e [registro V10 beta](MARCO_V10_BETA_STORE.md).
- [Fases 2–6](FASE2_SEGURANCA_CONFORMIDADE.md), [Fase 3](FASE3_REVISAO_HOMOLOGACAO.md), [Fase 4](FASE4_CORRECOES_TECNICAS.md), [Fase 5](FASE5_HOMOLOGACAO_CONFORMIDADE.md), [Fase 6B](FASE6B_HOMOLOGACAO_ANDROID.md), [homologação física V11](FASE6B1_HOMOLOGACAO_FISICA_V11.md) e [Fase 6C](FASE6C_SEGURANCA_DADOS_PLAY.md).
- [Consolidação V12 7A](FASE7A_CONSOLIDACAO_V12.md), [preparação RC 7B](FASE7B_PREPARACAO_RC_V12.md), [Kotlin 7B.1](FASE7B1_COMPATIBILIDADE_KOTLIN.md), [diagnóstico debug 7B.2](FASE7B2_DIAGNOSTICO_BUILD_DEBUG.md), [validação APK 7B.3](FASE7B3_VALIDACAO_BUILD_POWERSHELL.md), [preparação 7C](FASE7C_PREPARACAO_V12.md), [auditoria 7D](FASE7D_AUDITORIA_PRE_RELEASE_V12.md), [chave 7D.1](FASE7D1_VALIDACAO_UPLOAD_KEY.md), [chave/Git 7D.2](FASE7D2_CHAVE_UPLOAD_E_RECONCILIACAO_GIT.md), [consolidação Git 7E](FASE7E_CONSOLIDACAO_GIT_V12.md), [commits 7F](FASE7F_COMMITS_V12.md), [integração 7G](FASE7G_INTEGRACAO_MASTER_V12.md), [integração/regressão 7H](FASE7H_INTEGRACAO_REGRESSAO_V12.md), [AAB 7I](FASE7I_AAB_V12.md) e [preparação Alpha 7J](FASE7J_PREPARACAO_ALPHA_V12.md).
- [Referência AleJoias](REFERENCIA.md) e [guia de migração](GUIA_MIGRACAO_ALEJOIAS.md): análise seletiva; não são implementação nem autorização para alterar a origem.

## Como ler as evidências

`CONFIRMADO` significa que há commit, arquivo, teste ou artefato local identificável; quando a fonte for relato do usuário ou relatório anterior, isso é indicado. `INFERIDO` identifica uma conclusão plausível baseada em evidência parcial. `NÃO COMPROVADO` indica que o dado não está no histórico local ou depende de Console, dispositivo, Play, fornecedor ou publicação externa. Commit local, tag local, build local e AAB local não provam publicação ou aprovação na Google Play.
