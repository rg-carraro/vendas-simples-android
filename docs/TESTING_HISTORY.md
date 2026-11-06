# Histórico de testes e homologações

Diferencie `PASSOU`, `FALHOU`, `NÃO EXECUTADO` e `RELATADO PELO PROPRIETÁRIO`. Build, teste de outra versão ou inspeção estática não homologa automaticamente a V14.

| Data | Versão/base | Escopo e ambiente | Resultado/evidência |
|---|---|---|---|
| 11–12/10/2026 | V11 (`11 / 1.0.11`) | Suíte unitária, verificações estruturais e homologação física reportada pelo usuário. | Detalhes e limites: `RELEASE_V11.md`, `FASE6B_HOMOLOGACAO_ANDROID.md`, `FASE6B1_HOMOLOGACAO_FISICA_V11.md`. Não transfere aprovação à V14. |
| 16/10/2026 | V12 (`12 / 1.0.12`) | Testes, lint, build e validação de bundle registrados; runtime instrumentado, aparelho/API mínima e Billing real tinham pendências nos relatórios. | `FASE7H_INTEGRACAO_REGRESSAO_V12.md`, `FASE7I_AAB_V12.md`. Histórico, não estado atual. |
| 19/10/2026 | V13 (`13 / 1.0.13`) | Relatório registra 46 testes unitários, build debug, compilação AndroidTest e lint. Proprietário relatou validação visual/fluxos em S22+ e restore V12→V13 Debug. | `FASE13_SOBRE_APLICATIVO.md`, `HISTORICO_VERSOES.md`; não comprova runtime de AndroidTest nem estado Play. |
| 23/10/2026 | V14 (`14 / 1.0.14`) | Auditoria estática de contraste/acessibilidade na worktree V14, sem inspeção visual de runtime dos casos relatados. | `FASE15A_AUDITORIA_CONTRASTE.md`; não é homologação funcional completa. |
| 06/11/2026 | Termos/site na base `a32311b` | Validação local desta tarefa: HTMLParser, âncoras e links locais, workflow Pages, metadados V14, diff-check e escopo de arquivos. | Resultados detalhados no relatório da tarefa. Sem preview visual de navegador, HTTP ou deploy. |
| 06/11/2026 | Finalização editorial dos Termos | Responsável e contato foram fornecidos/confirmados pelo proprietário; revisão textual e remoção dos avisos editoriais resolvidos. | Não é validação jurídica independente. Revalidar HTML/links/diff-check após esta edição. Preview visual e deploy ainda não executados. |
| 06/11/2026 | Correção do nome do responsável | Proprietário confirmou o nome completo Rodrigo Geraldi Carraro; Termos e documentos permanentes foram atualizados. Política de Privacidade verificada: contato oficial consistente, sem identificação nominal formal a substituir. | Consistência textual verificada por busca. Nenhum arquivo Android foi alterado; revalidar HTML/links/diff-check ao finalizar. |
| 06/11/2026 | Identificação do fornecedor e CPF | Incluída seção nos Termos com nome, natureza, campo editorial de CPF, CNPJ ausente e contato oficial. O número autorizado não estava visível no conteúdo recebido e não foi gravado em arquivos. | `HTMLParser`, âncoras/links do site, verificação estrutural CSS e `git diff --check` passaram. Sem acesso à Play Console ou prévia visual. Arquivos Android não alterados. |
| 06/11/2026 | Campo de identificação do fornecedor/CPF | Seção preparada nos Termos com nome, natureza, CPF pendente de inserção e e-mail oficial. O número do CPF autorizado não constava no pedido recebido; não foi persistido em arquivos. | Parser, links e `git diff --check` reexecutados; Console e preview visual não verificados. |

| 06/11/2026 | Auditoria pré-publicação dos Termos | Conferidos base V14 `a32311b`, diff acumulado, workflow Pages, conteúdo pessoal restrito à identificação dos Termos, HTML, CSS, links/âncoras e escopo. | `git diff --check` limpo; nenhum arquivo Android/Gradle/Billing modificado; sem arquivos temporários identificados. Revisão jurídica do endereço, preview visual e publicação não executados.

## Declarações do proprietário — não verificadas nesta sessão

O pedido de 06/11/2026 informa V14 estável e em nova rodada de testes fechados, sem pendências técnicas conhecidas; migração concluída e homologada; botão Voltar corrigido e validado; formulário e feedback inicial positivo. Não foram fornecidos logs, capturas nem métricas da Console. Respostas do desenvolvedor devem ser separadas de avaliações independentes; formulário não substitui métricas de participação. Não foi declarada aprovação para produção.

Ao registrar novos testes, indique commit/artefato, dispositivo/API, cenários, resultados, problemas, evidência e limites.
