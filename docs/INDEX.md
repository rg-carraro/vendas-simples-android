# Índice documental do Vendas Simples

Este índice reúne a documentação de produto, arquitetura, validação e histórico. O histórico integral V0–V12 preserva a consolidação original da Fase 8A; a [correspondência de versões](HISTORICO_VERSOES.md) e o [adendo V0–V13](HISTORICO_COMPLETO_V0_V13.md) registram a continuidade até a V13. Para retomar trabalho, use [Continuidade Codex](CONTINUIDADE_CODEX.md).

## História do projeto

- [Histórico integral V0–V12](HISTORICO_COMPLETO_V0_V12.md) — síntese com fontes e limites de evidência.
- [Adendo de histórico V0–V13](HISTORICO_COMPLETO_V0_V13.md) — extensão atualizada sem reescrever o snapshot V0–V12 da Fase 8A.
- [Correspondência de versões e tags](HISTORICO_VERSOES.md) — metadados Android separados dos nomes informais e das tags.
- [Evolução da arquitetura](EVOLUCAO_ARQUITETURA.md) — banco, camadas, Billing, backup e toolchain.
- [Evolução das funcionalidades](EVOLUCAO_FUNCIONALIDADES.md) — marcos de produto por commits.
- [Histórico de testes](HISTORICO_TESTES.md) — cobertura e resultados reportados, sem transportar aprovação entre versões.
- [Histórico de segurança](HISTORICO_SEGURANCA.md) — proteções, verificações e pendências.
- [Problemas e soluções](PROBLEMAS_E_SOLUCOES.md) — causas confirmadas, hipóteses e limitações.
- [Decisões históricas](DECISOES_HISTORICAS.md) — decisões confirmadas e sua proveniência.
- [Lições aprendidas](LICOES_APRENDIDAS.md) — práticas extraídas dos registros, sem substituir regras do produto.
- [Continuidade Codex](CONTINUIDADE_CODEX.md) — roteiro de trabalho, evidências e manutenção.
- [Pendências atuais](PENDENCIAS_ATUAIS.md) — status, fonte, lacunas e próximo passo sem inferir estado da Console.
- [Manutenção pós-lançamento](MANUTENCAO_POS_LANCAMENTO.md) — triagem, severidade, regressão e release.
- [Base reutilizável para novos aplicativos](BASE_REUTILIZAVEL_NOVOS_APPS.md) — padrões classificados e limites de reaproveitamento.
- [Relatório da Fase 8A](FASE8A_HISTORICO_INTEGRAL_V0_V12.md) — escopo, auditoria e conclusão desta consolidação.

## Documentos de produto e implementação

- [README](../README.md), [AGENTS](../AGENTS.md), [decisões](DECISOES.md), [MVP](MVP.md), [produto](PRODUTO.md), [backlog](BACKLOG.md) e [roadmap](ROADMAP_PRODUTO.md).
- [Arquitetura e dados](ARQUITETURA.md), [regras financeiras](REGRAS_FINANCEIRAS.md), [validação](VALIDACAO.md) e [exportação CSV](EXPORTACAO_CSV.md).
- [Monetização](MONETIZACAO.md), [segurança e privacidade](SEGURANCA_PRIVACIDADE.md), [investigação Data Safety/Billing](SEGURANCA_DADOS_PLAY.md) e [release Play](RELEASE_PLAY_STORE.md).
- [Conhecimento Google Play — Data Safety e Billing](CONHECIMENTO_GOOGLE_PLAY_DATA_SAFETY.md) — orientação recebida, condições de aplicação, limites e gatilhos para reavaliação.
- [Release V11](RELEASE_V11.md) e [registro V10 beta](MARCO_V10_BETA_STORE.md).
- [Fase 13 — Tela Sobre e contato com desenvolvedor](FASE13_SOBRE_APLICATIVO.md) — implementação e validações; artefato e estado de distribuição atualizados no adendo V0–V13 e em Pendências.
- [Fase 14 — Consolidação documental](FASE14_CONSOLIDACAO_CONHECIMENTO.md) — alterações, fontes, limites e verificações.
- [Fase 14 — Implementação de identidade PDF e acesso à política](FASE14_FUNCIONAL_PDF_PRIVACIDADE.md) — worktree funcional, geradores inventariados, testes e pendências da V14.
- [Fase 15A — auditoria de contraste](FASE15A_AUDITORIA_CONTRASTE.md) — diagnóstico da interface antes da implementação.
- [Fase 16 — Light Mode e Dark Mode](FASE16_LIGHT_DARK_MODE.md) — tokens semânticos, diálogos, cobertura de teste e limites de validação.
- [Fase 16B — homologação](FASE16B_HOMOLOGACAO.md) e [Fase 16C — homologação final](FASE16C_HOMOLOGACAO_FINAL.md) — regressão de backup, matriz S22, APK debug isolado e aprovação manual relatada pelo proprietário.
- [Fase 17B — Interface Clean](FASE17B_INTERFACE_CLEAN.md) — disposição dos botões de foto, filtros sem ícones e resultados de build/homologação no S22 (relato do proprietário e testes instrumentados).
- [Fases 2–6](FASE2_SEGURANCA_CONFORMIDADE.md), [Fase 3](FASE3_REVISAO_HOMOLOGACAO.md), [Fase 4](FASE4_CORRECOES_TECNICAS.md), [Fase 5](FASE5_HOMOLOGACAO_CONFORMIDADE.md), [Fase 6B](FASE6B_HOMOLOGACAO_ANDROID.md), [homologação física V11](FASE6B1_HOMOLOGACAO_FISICA_V11.md) e [Fase 6C](FASE6C_SEGURANCA_DADOS_PLAY.md).
- [Fases 9A–9E](FASE9A_POS_MERGE_PREPARACAO_ALPHA.md) ([9B](FASE9B_CONFORMIDADE_ALPHA_V12.md), [9C](FASE9C_AUDITORIA_DATA_SAFETY_BILLING.md), [9D](FASE9D_RESOLUCAO_DATA_SAFETY.md), [9E](FASE9E_RESPOSTA_GOOGLE_DATA_SAFETY.md)) e [Fase 9F — encerramento Data Safety e preparação Alpha](FASE9F_ENCERRAMENTO_DATA_SAFETY_ALPHA.md).
- [Consolidação V12 7A](FASE7A_CONSOLIDACAO_V12.md), [preparação RC 7B](FASE7B_PREPARACAO_RC_V12.md), [Kotlin 7B.1](FASE7B1_COMPATIBILIDADE_KOTLIN.md), [diagnóstico debug 7B.2](FASE7B2_DIAGNOSTICO_BUILD_DEBUG.md), [validação APK 7B.3](FASE7B3_VALIDACAO_BUILD_POWERSHELL.md), [preparação 7C](FASE7C_PREPARACAO_V12.md), [auditoria 7D](FASE7D_AUDITORIA_PRE_RELEASE_V12.md), [chave 7D.1](FASE7D1_VALIDACAO_UPLOAD_KEY.md), [chave/Git 7D.2](FASE7D2_CHAVE_UPLOAD_E_RECONCILIACAO_GIT.md), [consolidação Git 7E](FASE7E_CONSOLIDACAO_GIT_V12.md), [commits 7F](FASE7F_COMMITS_V12.md), [integração 7G](FASE7G_INTEGRACAO_MASTER_V12.md), [integração/regressão 7H](FASE7H_INTEGRACAO_REGRESSAO_V12.md), [AAB 7I](FASE7I_AAB_V12.md) e [preparação Alpha 7J](FASE7J_PREPARACAO_ALPHA_V12.md).
- [Referência AleJoias](REFERENCIA.md) e [guia de migração](GUIA_MIGRACAO_ALEJOIAS.md): análise seletiva; não são implementação nem autorização para alterar a origem.

## Como ler as evidências

`CONFIRMADO` significa que há commit, arquivo, teste ou artefato local identificável; quando a fonte for relato do usuário ou relatório anterior, isso é indicado. `INFERIDO` identifica uma conclusão plausível baseada em evidência parcial. `NÃO COMPROVADO` indica que o dado não está no histórico local ou depende de Console, dispositivo, Play, fornecedor ou publicação externa. Commit local, tag local, build local e AAB local não provam publicação ou aprovação na Google Play.

## Skills do repositório

- [Interface](../.agents/skills/vendas-simples-interface/SKILL.md), [backup](../.agents/skills/vendas-simples-backup/SKILL.md), [planejamento](../.agents/skills/vendas-simples-planejar/SKILL.md), [referência](../.agents/skills/vendas-simples-referencia/SKILL.md).
- [Validação](../.agents/skills/vendas-simples-validar/SKILL.md), [monetização](../.agents/skills/vendas-simples-monetizacao/SKILL.md), [segurança](../.agents/skills/vendas-simples-seguranca/SKILL.md), [release](../.agents/skills/vendas-simples-release/SKILL.md).
