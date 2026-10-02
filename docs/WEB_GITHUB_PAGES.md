# Presença web pública

O conteúdo público inicial fica em `site/` e não depende do código Android:

- `site/index.html`: página institucional inicial;
- `site/privacy/index.html`: Política de Privacidade pública;
- `site/styles.css`: estilos responsivos compartilhados.

## Publicação no GitHub Pages

O repositório usa o workflow `.github/workflows/pages.yml`, que publica somente o conteúdo de `site/`. O GitHub Pages fornecerá HTTPS automaticamente no endereço:

`https://<proprietario>.github.io/vendas-simples-android/`

A política ficará em:

`https://<proprietario>.github.io/vendas-simples-android/privacy/`

Para ativar, abra **Settings → Pages**, selecione **GitHub Actions** como fonte e execute o workflow `Publicar site no GitHub Pages` uma vez, se ele não for iniciado automaticamente após o push. O proprietário e o domínio devem ser confirmados antes de inserir a URL no Google Play Console.
