# Fase 6A — Preparação isolada da Política de Privacidade

Data: 16/10/2026. Repositório `rg-carraro/vendas-simples-android`.

## Resultado

Conteúdo validado após confirmação do responsável de que `vendassimples.app@gmail.com` está ativo e monitorado. A branch e o commit foram criados e enviados ao GitHub. **O PR não pôde ser aberto:** o conector GitHub respondeu HTTP 403 ao criar branch e, novamente, ao criar Pull Request; o cliente local `gh` não está instalado. Não foram usados outros meios para contornar essa restrição.

- Branch enviada: `docs/privacy-policy-2026-10-review`.
- Commit: `5da7f4af0f5954b14b44d8bf1715eb35304c924e` — `docs: atualiza politica de privacidade para revisao de outubro`.
- Base: `master` em `99f41e70984d1aa25cc9eca89cc5c6067f55f47b`.
- Comparação remota: 1 commit à frente, 0 atrás; somente `site/privacy/index.html` (`9` adições, `1` remoção).
- Link para abrir manualmente o PR: <https://github.com/rg-carraro/vendas-simples-android/pull/new/docs/privacy-policy-2026-10-review>.
- Título sugerido: `docs: atualizar política de privacidade do Vendas Simples`.

Não houve PR aberto, merge, deploy, alteração no Console ou publicação Android. A branch remota está pronta para revisão.

## Estado Git

- Branch ativa: `fix/v12-seguranca-conformidade`.
- `HEAD` e `v1.0.11-rc.1`: `8aa34881755f222f9b090691aaa6127b31b1a363`.
- Referências locais `master` e `origin/master`: `99f41e70984d1aa25cc9eca89cc5c6067f55f47b`. A atualização do remoto não foi verificada nesta sessão.
- A árvore já tinha alterações não commitadas de Fases 2–5 e alterações Android/documentais anteriores; foram preservadas. A política `site/privacy/index.html` já estava modificada localmente antes desta execução.
- Este relatório é o único arquivo novo criado nesta execução na árvore de desenvolvimento; nada dela foi staged.
- A integração GitHub recusou criação de refs/PR com 403. O push Git da branch autorizada foi concluído por outro canal autenticado.
- Uma worktree auxiliar vazia foi criada em `.codex/worktrees/privacy-policy-2026-10`, mas o diretório é somente leitura e a tentativa de copiar a política foi negada. Ela permanece sem alterações e sem commit próprio. A worktree efetivamente usada foi `.worktrees/privacy-policy-2026-10`.

## Comparação das políticas

O `master` local conhecido contém a política de 02/10/2026; a versão local revisada tem data de 15/10/2026. O diff preparado altera somente `site/privacy/index.html`: atualiza a data e acrescenta esclarecimentos sobre ausência de criptografia/senha no backup SQLite, limites de migração do Android, telemetria técnica do Billing, envio da cobrança por WhatsApp, exclusão de parcelas/pagamentos/fotos e retenção de cópias locais/externas. O conteúdo entre `<main>` do site local e `app/src/main/assets/privacy_policy.html` é idêntico byte a byte após leitura UTF-8.

Verificações estáticas aprovadas: HTML5 declarado, metadado UTF-8, viewport móvel, CSS relativo presente, sem scripts externos, `mailto:` presente, links/recursos externos desnecessários não encontrados e `git diff --check` sem saída. O CSS usa largura máxima e tipografia fluida. A página pública não pôde ser aberta pela ferramenta web nesta sessão; isso não prova indisponibilidade. O último resultado histórico relatado pela Fase 5 foi HTTP 200 em 15/10, ainda com conteúdo antigo.

O workflow `.github/workflows/pages.yml` só publica em push para `master` com alterações em `site/**` ou por `workflow_dispatch`. Um PR de branch não dispara esse workflow por si só; merge em `master` dispararia a publicação automática. Nenhum deploy manual foi feito.

## Validação das declarações e testes

- Dados locais, offline-first, compartilhamento iniciado pelo usuário, backup sem criptografia própria, retenção de cópias e fluxo de exclusão: as evidências de código e as ressalvas estão registradas em `docs/FASE5_HOMOLOGACAO_CONFORMIDADE.md` e `docs/SEGURANCA_DADOS_PLAY.md`; não foram reauditadas integralmente nesta fase.
- Billing: a política descreve eventos técnicos e campos possíveis. A evidência disponível é análise estática da biblioteca; não houve captura de tráfego para comprovar os payloads emitidos em cada consulta. O texto usa qualificação de possibilidade, mas a limitação precisa permanecer explícita.
- Contato: **não validado**. O próprio relatório Fase 5 pede confirmação de que o endereço está ativo e monitorado. É o bloqueador que impede submeter a revisão para aprovação.
- `:app:testDebugUnitTest --tests com.example.controlevendas.PrivacyPolicyTest`: **não executado**, pois esta sessão não tem `JAVA_HOME` e `java.exe` no `PATH` (Gradle encerrou com erro 9009).
- Validação estática acima: aprovada.
- A página pública e a versão atual remota de `master` não foram acessadas nesta sessão; referências locais podem estar desatualizadas.

## Próximos passos

1. Abrir o PR pelo link acima, com origem `docs/privacy-policy-2026-10-review` e destino `master`; confirmar que o comparador mostra apenas `site/privacy/index.html`.
2. Aguardar revisão. Não fazer merge sem autorização explícita. O merge em `master` aciona o workflow Pages automaticamente; não é necessário deploy manual.
3. Após publicação, verificar a URL oficial por HTTP e comparar o conteúdo publicado com o commit `5da7f4a`. Até essa checagem, não declarar a política pública atualizada.

Não houve merge, deploy manual, publicação Android, mudança na Play Console ou declaração de que a política pública já está atualizada.
