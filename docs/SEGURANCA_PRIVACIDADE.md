# Segurança e privacidade

Vendas Simples é offline-first e não deve reutilizar credenciais, endpoints, banco real ou Google Planilhas do AleJoias. O MVP não cria conta nem backend apenas para antifraude.

Dados de clientes, vendas, pagamentos e fotos permanecem no armazenamento local. Não incluir dados reais em fixtures, logs, commits ou documentação. Compartilhamento de PDF, CSV, backup e WhatsApp só ocorre após ação explícita do usuário.

Falha de licença, Billing, restauração ou rede nunca pode apagar dados. Estados de licença devem ser separados das entidades financeiras. O limite FREE controla apenas a criação de novas vendas.

Valores monetários novos usam centavos em `Long` ou decimal seguro, com arredondamento explícito. Não introduzir `Float` ou `Double` para dinheiro. Mudanças de schema exigem versão, migração testada com fixtures sintéticas e preservação de vendas, pagamentos, fotos e IDs.

Antes da distribuição, documentar permissões efetivas, política de backup Android, retenção, exclusão e limitações de privacidade.

## Decisão pré-Store sobre backup e compartilhamento

`android:allowBackup` fica `false` no MVP. O aplicativo guarda dados financeiros e fotos de clientes localmente; desativar o backup automático evita que uma cópia do dispositivo seja criada fora do fluxo de exportação que o usuário iniciou conscientemente. Isso não substitui um recurso de backup/ restauração do próprio produto, que deve ser definido em etapa posterior.

O `FileProvider` permanece não exportado (`android:exported="false"`) e concede somente permissões temporárias (`grantUriPermissions="true"`) para o aplicativo escolhido no compartilhamento. Os caminhos autorizados continuam limitados aos diretórios privados `cache` e `files`; nenhum caminho externo ou armazenamento público é exposto.
