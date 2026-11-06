# Presença web pública

O conteúdo público inicial fica em `site/` e não depende do código Android:

- `site/index.html`: página institucional inicial;
- `site/privacy/index.html`: Política de Privacidade pública;
- `site/terms/index.html`: minuta de Termos de Serviço, sujeita à aprovação do responsável;
- `site/styles.css`: estilos responsivos compartilhados.

## Publicação no GitHub Pages

O repositório usa o workflow `.github/workflows/pages.yml`, que publica somente o conteúdo de `site/`. O GitHub Pages fornecerá HTTPS automaticamente no endereço:

`https://<proprietario>.github.io/vendas-simples-android/`

A política ficará em:

`https://<proprietario>.github.io/vendas-simples-android/privacy/`

Os Termos de Serviço, após aprovação e publicação, ficarão em:

`https://<proprietario>.github.io/vendas-simples-android/terms/`

O proprietário informou em 06/11/2026 que a V14 (`14 / 1.0.14`) está em testes fechados; esse estado externo não foi verificado nesta tarefa. O código V14 está integrado na master local. A página de Termos não cita versões nem detalhes internos; identifica Rodrigo Geraldi Carraro como desenvolvedor independente pessoa física e mantém `vendassimples.app@gmail.com` como contato oficial, ambos confirmados pelo proprietário. Não houve push, deploy nem confirmação HTTP da rota `/terms/`.

Para ativar, abra **Settings → Pages**, selecione **GitHub Actions** como fonte e execute o workflow `Publicar site no GitHub Pages` uma vez, se ele não for iniciado automaticamente após o push. O proprietário e o domínio devem ser confirmados antes de inserir a URL no Google Play Console.

## Verificação em 15/10/2026 — Fase 2

O usuário confirmou em 15/10/2026 que a [URL oficial da Política de Privacidade](https://rg-carraro.github.io/vendas-simples-android/privacy/) está cadastrada na Play Console. Esse endereço já é utilizado no botão Abrir site do app em desenvolvimento, que também mantém acesso gratuito ao texto local offline.

A consulta HTTPS registrada na Fase 2 respondeu 200, sem redirecionamento e com UTF-8. Naquela verificação, o texto servido permanecia datado de 02/10/2026 e não correspondia à revisão local de 15/10/2026, sincronizada entre `site/privacy/index.html` e `app/src/main/assets/privacy_policy.html`. As evidências foram preservadas em diretório local ignorado pelo Git. A revisão está preparada localmente; publicação depende de autorização, e não houve commit, push ou deploy. A disponibilidade e o conteúdo atuais da página não foram confirmados nesta Fase 7G.1.
