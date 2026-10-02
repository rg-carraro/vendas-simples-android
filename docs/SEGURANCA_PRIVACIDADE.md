# Segurança e privacidade

Vendas Simples é offline-first e não deve reutilizar credenciais, endpoints, banco real ou Google Planilhas do AleJoias. O MVP não cria conta nem backend apenas para antifraude.

Dados de clientes, vendas, pagamentos e fotos permanecem no armazenamento local. Não incluir dados reais em fixtures, logs, commits ou documentação. Compartilhamento de PDF, CSV, backup e WhatsApp só ocorre após ação explícita do usuário.

Falha de licença, Billing, restauração ou rede nunca pode apagar dados. Estados de licença devem ser separados das entidades financeiras. O limite FREE controla apenas a criação de novas vendas.

Valores monetários novos usam centavos em `Long` ou decimal seguro, com arredondamento explícito. Não introduzir `Float` ou `Double` para dinheiro. Mudanças de schema exigem versão, migração testada com fixtures sintéticas e preservação de vendas, pagamentos, fotos e IDs.

Antes da distribuição, documentar permissões efetivas, política de backup Android, retenção, exclusão e limitações de privacidade.

## Decisão pré-Store sobre backup e compartilhamento

`android:allowBackup` fica `false` no MVP. O aplicativo guarda dados financeiros e fotos de clientes localmente; desativar o backup automático evita que uma cópia do dispositivo seja criada fora do fluxo de exportação que o usuário iniciou conscientemente. Isso não substitui um recurso de backup/ restauração do próprio produto, que deve ser definido em etapa posterior.

O `FileProvider` permanece não exportado (`android:exported="false"`) e concede somente permissões temporárias (`grantUriPermissions="true"`) para o aplicativo escolhido no compartilhamento. Os caminhos autorizados continuam limitados aos diretórios privados `cache` e `files`; nenhum caminho externo ou armazenamento público é exposto.

## Backup e restauração local

O `BackupManager` grava e lê o arquivo somente após ação explícita do usuário pelos contratos SAF `ACTION_CREATE_DOCUMENT` e `ACTION_OPEN_DOCUMENT`. Antes da cópia, executa `wal_checkpoint(FULL)`. O arquivo recebe identificação própria (`VENDAS_SIMPLES_BACKUP_METADATA`, formato 1 e schema 3), valida as tabelas necessárias e executa `PRAGMA integrity_check`; arquivos SQLite genéricos, inválidos ou incompatíveis são rejeitados.

A restauração fecha a conexão local, preserva uma cópia de rollback, remove WAL/SHM antigos e substitui o banco somente depois da validação. Falhas restauram a cópia anterior e arquivos temporários são removidos. O conteúdo restaurado inclui os dados do banco, inclusive pagamentos e fotos/BLOBs, mas nunca concede ou altera `FULL_ACCESS`. Uma interrupção extrema entre a remoção e a renomeação do arquivo ainda pode exigir recuperação manual; uma troca transacional mais forte fica como melhoria futura.

Versões futuras devem manter o formato identificável e adicionar migradores explícitos por combinação de `format_version` e `schema_version`, sem aceitar silenciosamente versões desconhecidas.
