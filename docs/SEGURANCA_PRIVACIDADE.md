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

O `BackupManager` grava e lê o arquivo somente após ação explícita do usuário pelos contratos SAF `ACTION_CREATE_DOCUMENT` e `ACTION_OPEN_DOCUMENT`. Antes da cópia, executa `wal_checkpoint(FULL)`. O arquivo recebe identificação própria (`VENDAS_SIMPLES_BACKUP_METADATA`, formato 2 e schema 3), valida tabelas/colunas necessárias, versão real do SQLite e `PRAGMA integrity_check`; arquivos SQLite genéricos, inválidos ou incompatíveis são rejeitados. O formato 1 continua aceito. Formato 2 inclui `business_name` somente nos metadados da cópia, sem migração do schema financeiro.

A restauração copia e valida uma única leitura do provedor antes de substituir o banco. Fecha a conexão, preserva cópia de rollback e nome/estado do convite em journal privado persistente, remove WAL/SHM e renomeia a cópia validada no mesmo volume. O nome é gravado com commit síncrono; falhas revertem banco e preferência. Se o processo for interrompido com journal pendente, a recuperação roda antes de abrir SQLite. Se a recuperação falhar, o app não abre a base e mantém os arquivos de recuperação. Falhas permanentes de armazenamento ou energia ainda dependem das garantias do dispositivo/sistema de arquivos.

O conteúdo inclui pagamentos e fotos/BLOBs, mas nunca concede ou altera `FULL_ACCESS`. Somente nome e sinal de convite inicial são tratados na recuperação da preferência; nenhum estado Billing é importado. Backup antigo, nome vazio ou nulo preservam o nome atualmente configurado. Criação e restauração verificam FULL_ACCESS também dentro do gerenciador; personalização permanece disponível no FREE.

Versões futuras devem manter o formato identificável e adicionar migradores explícitos por combinação de `format_version` e `schema_version`, sem aceitar silenciosamente versões desconhecidas.

## Fase 2 em desenvolvimento — 15/10/2026

Na branch fix/v12-seguranca-conformidade, a restauração passa a rejeitar objetos SQLite não permitidos e registros incompatíveis antes da troca. Formatos 1/2 e schema 3 permanecem; veja limites e exceções de compatibilidade no [relatório da Fase 2](FASE2_SEGURANCA_CONFORMIDADE.md). A política recebe acesso gratuito offline e URL pública verificada. A [investigação do Billing](SEGURANCA_DADOS_PLAY.md) mantém as declarações externas pendentes. Essas alterações não estão no AAB Alpha V11 preservado e não comprovam aprovação para produção.
