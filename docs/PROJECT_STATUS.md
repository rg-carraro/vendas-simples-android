# Estado do projeto

**Atualizado em:** 06/11/2026
**Base desta documentação:** master local `a32311b`
**Aplicativo:** Vendas Simples — Controle de Vendas
**Application ID:** `com.vendassimples.app`

## Versão e distribuição

- **CONFIRMADO no código desta base:** `versionCode 14`, `versionName 1.0.14` em `app/build.gradle`.
- **CONFIRMADO no histórico Git local:** V14 foi integrada à master pelo merge `dfb87e6`; commits posteriores das fases 16–17 estão incluídos até `a32311b`.
- **RELATADO PELO PROPRIETÁRIO em 06/11/2026:** V14 está em testes fechados da Google Play, está estável e sem pendências técnicas conhecidas. Esta sessão não consultou a Play Console. Não concluir que produção está aprovada.
- A ausência de tag V14 não invalida os metadados/merge confirmados; tag, artefato, envio, estado de revisão e disponibilidade são evidências diferentes.

## Produto, funcionalidades e decisões

O produto funciona offline e mantém os dados comerciais no armazenamento local. O proprietário relata que migração histórica foi concluída e homologada, e que o aplicativo passou a ser utilizado comercialmente com dados reais. Não modificar ou reabrir a migração sem solicitação.

Na implementação V14 inspecionada, FREE permite até 30 vendas, mantém clientes sem limite conhecido no gate e oferece exportação CSV. A interface preserva consulta de registros existentes após o limite. `FULL_ACCESS` é produto Google Play de compra única (`full_access`, tipo `one_time`, opção `full-access`), sem mensalidade; o preço exibido vem de `ProductDetails`. Backup e restauração completos são protegidos pelo entitlement `FULL_ACCESS`. Restaurar compra não restaura dados do dispositivo. Consulte `MONETIZACAO.md`, `SEGURANCA_PRIVACIDADE.md` e o código antes de atualizar declarações.

## Termos e site

A página `site/terms/index.html` identifica Rodrigo Geraldi Carraro como desenvolvedor independente pessoa física, sem CNPJ, e contém os dados de identificação fornecidos e confirmados pelo proprietário, além de `vendassimples.app@gmail.com` como e-mail de suporte. CPF e endereço estão somente nessa página; os documentos permanentes registram o status sem reproduzir esses valores. O proprietário confirmou que o e-mail de perfil do desenvolvedor permanece conforme a Play Console e é distinto do suporte do aplicativo; a Console não foi acessada diretamente. A Política de Privacidade pública mantém o e-mail de suporte. O código V14 da tela Sobre usa `RGCarraro` e `rgcarraro@gmail.com`; não foi alterado. O workflow Pages publica `site/`; o caminho esperado dos Termos é `/terms/`.

## Estado desta worktree documental

Worktree isolada baseada em `a32311b`, criada apenas para preparar integração local dos Termos e documentos. Sem commit, push ou publicação. Arquivos Android, Gradle, Billing e versão não foram editados.

## Pendências

- Autorização explícita para commit, push e publicação; nenhuma foi dada nesta etapa.
- Revisão das informações do fornecedor exigidas para comércio eletrônico, incluindo a aplicabilidade de identificação cadastral e endereço físico. O CPF e o endereço físico oficial foram incluídos somente nos Termos, com autorização do proprietário. Permanece pendente a avaliação jurídica da forma de apresentação do endereço antes da publicação.
- Conferir diretamente na Play Console nome legal, perfil de pagamentos, e-mail de suporte e endereço exibido para a conta pessoal monetizada. A Console não foi acessada nesta tarefa.
- Validar por navegador os temas claro/escuro e layout desktop/mobile, quando ambiente estiver disponível.
- Após autorização separada, integrar, revisar diff, publicar via Pages e verificar a rota HTTPS. Até então, não afirmar que `/terms/` está publicada.
- Estado atual dos testes fechados e produção da Play Console permanece sem verificação independente nesta sessão.

Consulte [decisões](DECISIONS.md), [histórico](CHANGELOG_PROJECT.md), [testes](TESTING_HISTORY.md) e [próximas ações](NEXT_STEPS.md).

- **Possível melhoria futura, sem tarefa aberta:** avaliar a padronização dos contatos de suporte exibidos no aplicativo com os contatos públicos do site. Esta observação não autoriza nem inicia alteração Android.
