# Fase 14 — Consolidação de conhecimento e manutenção

**Data:** 19/10/2026

**Branch:** `docs/fase14-consolidacao-conhecimento`

**Base:** master `5593215eb0a7a1d9f672d2c7d1d8ef86ad4665ea` (merge PR #4)

**Escopo:** documentação e instruções de agentes. Sem alteração funcional, build, AAB, Console ou release.

## Resultado e origem das evidências

A master contém V13 (`com.vendassimples.app`, 13/1.0.13) integrada pela PR #4. O proprietário relatou em 19/10/2026 que o teste fechado Alpha mostrava versão 13, envio 6, estado “Em análise” e aproximadamente oito dias restantes. Classificação: **RELATADO PELO PROPRIETÁRIO**; não houve acesso independente à Console nesta fase. Não se declara aprovação, disponibilização, rollout ou publicação pública.

O AAB release V13 validado anteriormente foi preservado, sem reconstrução ou alteração nesta fase: 5.364.930 bytes, SHA-256 `33D0906198E439304887A59AB683AF723636DEEE6ACAF51025BD073C42ED6642`; certificado SHA-256 `CD:90:9D:AE:E8:16:B2:7F:4E:32:13:52:73:62:25:80:BE:F0:73:F1:8A:8D:EB:56:04:F5:24:90:9C:63:C6:9E`. Evidência prévia de artefato, não verificação feita pela Fase 14.

## Documentação consolidada

- Criado [HISTORICO_COMPLETO_V0_V13](HISTORICO_COMPLETO_V0_V13.md), adendo ao snapshot original V0–V12, sem reescrever a auditoria da Fase 8A. Atualizado `HISTORICO_VERSOES.md` com merge V13, AAB e classificação do relato Alpha.
- Atualizados `README.md`, `docs/INDEX.md`, `AGENTS.md`, `docs/DECISOES.md`, `docs/CONTINUIDADE_CODEX.md`, `docs/HISTORICO_TESTES.md`, `docs/PROBLEMAS_E_SOLUCOES.md` e `docs/EVOLUCAO_ARQUITETURA.md` para remover estados superados e apontar para o estado atual sem apagar registros históricos.
- Adicionado [PENDENCIAS_ATUAIS](PENDENCIAS_ATUAIS.md), distinguindo análise, aprovação, rollout e produção; reúne atualização direta, runtime instrumentado, API24, Billing de teste, Data Safety e política pública.
- Adicionados [MANUTENCAO_POS_LANCAMENTO](MANUTENCAO_POS_LANCAMENTO.md) com triagem S0–S3 e [BASE_REUTILIZAVEL_NOVOS_APPS](BASE_REUTILIZAVEL_NOVOS_APPS.md), que classifica padrões e proíbe herdar identidade, dados, chaves, comércio ou conclusão Data Safety.
- `FASE13_SOBRE_APLICATIVO.md` recebeu um aviso contextualizando seu estado da época com a integração posterior e o relato Alpha; testes originais não foram reatribuídos.
- Revisadas oito skills do projeto: `vendas-simples-interface`, `vendas-simples-backup`, `vendas-simples-planejar`, `vendas-simples-referencia`, `vendas-simples-validar`, `vendas-simples-monetizacao`, `vendas-simples-seguranca` e `vendas-simples-release`. Adicionadas referências operacionais de manutenção, privacidade, regressão, release e evidência, sem duplicar decisões comerciais.

## Decisões e lições preservadas

As regras comerciais permanecem FREE até 30 vendas, clientes ilimitados, CSV gratuito e FULL_ACCESS por compra única `full_access`, sem assinatura. R$ 19,90 permanece valor informado pelo proprietário, enquanto ProductDetails é a fonte do preço apresentado pelo app. Restauro do entitlement não vem do backup; direitos não devem ser apagados por falhas. O produto mantém caráter offline-first, sem login obrigatório, backend próprio ou sincronização automática. Manutenção prioriza dados, segurança, compatibilidade e estabilidade, sem promessa de frequência/SLA.

A Fase 9F documenta a orientação recebida do suporte de forma condicionada à implementação/fluxo V12 descritos; não constitui pré-aprovação. O `purchaseToken` é acessado pelo app e usado na chamada de reconhecimento via Billing; não se deve afirmar que nunca deixa o dispositivo. Mudança de SDK, destino, transmissão, finalidade ou arquitetura exige nova análise.

## Estado da política pública e pendências

Na comparação local, o texto principal em `site/privacy/index.html` e o conteúdo de `app/src/main/assets/privacy_policy.html` coincidem após remover marcação HTML e normalizar espaços; ambos indicam 15/10/2026. A URL pública oficial não pôde ser aberta pela ferramenta de navegação nesta execução, portanto disponibilidade e conteúdo live permanecem **NÃO COMPROVADOS**. A divergência histórica e essa lacuna estão registradas em `WEB_GITHUB_PAGES.md` e [Pendências atuais](PENDENCIAS_ATUAIS.md).

A documentação registra como pendentes: resultado/disponibilidade do envio Alpha (além do relato “Em análise”), atualização direta V12→V13 por Play, runtime instrumentado, API24, compra/restauração em ambiente de teste, confirmação pública da política e elegibilidade concreta para produção na Console.

Ajuda oficial atual consultada: [requisitos de teste para contas pessoais novas](https://support.google.com/googleplay/android-developer/answer/14151465?hl=en). A regra publicada é aplicável a contas pessoais criadas após 13/11/2023: mínimo de 12 testadores opted-in continuamente por 14 dias antes de solicitar acesso à produção. O tipo/data de criação da conta deste projeto e seu contador continuam a ser confirmados na Console; o Google pode solicitar teste adicional.

## Arquivos alterados

`AGENTS.md`, `README.md`, `docs/INDEX.md`, `docs/DECISOES.md`, `docs/CONTINUIDADE_CODEX.md`, `docs/HISTORICO_VERSOES.md`, `docs/HISTORICO_TESTES.md`, `docs/PROBLEMAS_E_SOLUCOES.md`, `docs/EVOLUCAO_ARQUITETURA.md`, `docs/FASE13_SOBRE_APLICATIVO.md`, as oito skills listadas acima; novos: `docs/HISTORICO_COMPLETO_V0_V13.md`, `docs/PENDENCIAS_ATUAIS.md`, `docs/MANUTENCAO_POS_LANCAMENTO.md`, `docs/BASE_REUTILIZAVEL_NOVOS_APPS.md` e este relatório.

## Verificações e limitações

- `git fetch origin master` foi executado sem sobrescrever alterações locais; `origin/master` apontou para `5593215...`, base desta worktree.
- Estado inicial da worktree Fase 14 estava limpo e baseada em `origin/master`. As outras worktrees e a árvore raiz foram inspecionadas e preservadas; alterações não relacionadas existentes não foram tocadas.
- Comparação local da política: texto principal idêntico após extração de `<main>` e normalização de tags/espaços; arquivos têm estrutura global diferente por navegação e wrapper.
- Busca online da política pública falhou com erro de carregamento da ferramenta; não se declara HTTP 200 nem correspondência live.
- Requisito de acesso à produção foi conferido na página oficial do Google Play ligada acima. A elegibilidade específica não pode ser concluída sem tipo/data da conta e estado da Console.
- Builds não executados por ser fase documental. `git diff --check` passou; links relativos em 27 arquivos Markdown passaram; varredura de padrões de chave privada, tokens, senha literal e caminho privado do usuário passou. `git diff` em `app/src`, Gradle e arquivos funcionais não mostrou alterações. A lista alterada contém somente documentação e skills; nenhum APK/AAB está no delta.
- Não houve alteração de código, Gradle, dependências, versionamento, recursos Android, AAB/APK, banco, assinatura, Console, publicação, commit, push ou merge durante a preparação inicial deste relatório.

## Próximo passo

Revisar o diff, completar as verificações documentais e publicar somente a branch desta fase. Criar PR documental em draft se o acesso GitHub permitir; não fazer merge. O fluxo V13 Alpha permanece sob responsabilidade da Console e do proprietário, sem interferência desta fase.
