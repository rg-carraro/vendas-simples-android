# Continuidade Codex — Vendas Simples

## Antes de iniciar uma tarefa

1. Confirmar repositório, branch, commit, versão, worktree ativo e `git status`; consultar `git worktree list` se houver mais de uma árvore.
2. Ler `AGENTS.md`, `README.md`, `docs/INDEX.md`, `docs/DECISOES.md`, documento afetado e skill aplicável.
3. Para função antiga, consultar [histórico integral V0–V12](HISTORICO_COMPLETO_V0_V12.md), [adendo V0–V13](HISTORICO_COMPLETO_V0_V13.md), [versões](HISTORICO_VERSOES.md), [decisões](DECISOES.md), [problemas e soluções](PROBLEMAS_E_SOLUCOES.md) e commits do arquivo (`git log --all --follow -- <path>`). Verificar implementação atual em vez de supor que registro histórico continua válido.
4. Separar solicitação explícita, decisão confirmada, fato do código, inferência e lacuna. Nunca preencher V1–V9, Console, commit remoto ou teste manual por suposição.
5. Preservar instalações/dados/artefatos existentes. Usar dados sintéticos para alterações financeiras, backup, fotos e Billing.

## Durante a mudança

- Evitar refatoração lateral. Proteger schema/IDs/cálculos e regras FREE/FULL_ACCESS. Migração exige teste de upgrade e rollback.
- Implementar teste de regressão proporcional à falha; teste de build não substitui cenário funcional.
- Registrar falhas intermediárias, causa confirmada versus hipótese e quais verificações foram de fato reexecutadas.
- Para Git, nunca reset/clean/descarte de arquivos do usuário. Stage explícito por arquivo/hunk; revisar diff staged e conteúdo não rastreado antes de commit.
- Skills locais são instruções do repositório e apontam para os documentos de referência; manter regras comerciais nos docs de produto e fluxo reutilizável nas skills.

## Ao concluir

Atualizar documentação do produto e relatório da fase; registrar commit/versão, arquivos, comandos, resultado, limites e próximo passo. Atualizar o catálogo de problemas se houver bug; atualizar a tabela de versões se houver novo marco. Fazer revisão de links, whitespace, segredos, caminhos pessoais e estado Git. Reportar teste não executado e status de upload/publicação. Não presumir autorização de push, PR, tag, Console ou release.

## Modelo para continuidade e manutenção

Copie este bloco para um relatório novo em `docs/FASE_<n>_<tema>.md` e, na conclusão, mantenha um resumo no [histórico de versões](HISTORICO_VERSOES.md). Não atribua número de versão antes da decisão/inspeção do projeto.

```markdown
# Versão <marco> — <objetivo>

Data/período; branch/worktree; commit base e final; versionCode/versionName/applicationId (ou “não definido”).

## Requisitos e decisões
- Requisito do usuário:
- Decisões confirmadas e documentos:
- Propostas/lacunas:

## Alterações
- Funcionalidade/arquitetura/schema:
- Arquivos e commits:
- Compatibilidade/risco:

## Validação
- Testes/builds e comandos:
- Aparelho/API e evidência manual (ou NÃO EXECUTADO):
- Resultados e falhas intermediárias:

## Artefato e distribuição
- Caminho local/hash/tamanho/assinatura/proveniência ou NÃO GERADO:
- Play Console/faixa/estado, somente com evidência:

## Segurança e dados
- Migração/backup/licença/privacidade:
- Pendências externas:

## Conclusão e continuidade
- Classificação:
- Riscos e próximo passo:
- Estado Git e autorizações não exercidas:
```

Registrar lições em [LICOES_APRENDIDAS](LICOES_APRENDIDAS.md), problemas em [PROBLEMAS_E_SOLUCOES](PROBLEMAS_E_SOLUCOES.md) e decisões duradouras em [DECISOES](DECISOES.md), sem duplicar a regra completa.

Consulte também [Pendências atuais](PENDENCIAS_ATUAIS.md) e [Manutenção pós-lançamento](MANUTENCAO_POS_LANCAMENTO.md). Registre commit/tag, versionCode/name, AAB/hash/certificado e estado Play separadamente; atualize lacunas sem tratar artefato local como release publicada. Identifique cada status externo como observado diretamente, relatado pelo proprietário ou não comprovado. Testes de uma versão não aprovam outra.

Ao reaproveitar técnica em outro aplicativo, siga [Base reutilizável](BASE_REUTILIZAVEL_NOVOS_APPS.md); identidade, dados, regras comerciais, Billing, privacidade, chaves e Data Safety exigem decisões e avaliação próprias.
