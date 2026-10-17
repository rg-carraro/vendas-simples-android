# Evolução da arquitetura

## Base inicial — 29/09/2026

O primeiro commit de app `5c8c4f2` adicionou Gradle Android/Kotlin, `MainActivity.kt`, `Models.kt`, `LocalDatabase.kt`, manifesto, FileProvider e recursos visuais. A UI era programática em uma Activity; a persistência usava `SQLiteOpenHelper` e o banco local `vendas_simples.db`. A configuração inicial observável: namespace `com.example.controlevendas`, applicationId `com.vendassimples.app`, compile/target 35, minSdk 23. A base já continha tabelas de clientes, vendas, pagamentos e fotos; o código tinha também tabelas `SYNC_DIRTY`/`SYNC_DELETIONS`, mas não se encontrou integração de backend ativa no produto documentado.

## Persistência e dinheiro

| Marco | Evolução |
|---|---|
| Commit inicial | Schema `DB_VERSION 2`; entidades comerciais, pagamentos, fotos e índices locais. |
| `b6befe5` — 29/09 | Migração para centavos, schema 3, acrescentando colunas inteiras de valor na venda/pagamento; migração converte valores anteriores com arredondamento SQL documentado. Regras em [REGRAS_FINANCEIRAS](REGRAS_FINANCEIRAS.md). |
| V11 `eae7711` | Preferência `businessName` em SharedPreferences, independente de DB/schema e licença. Datas persistidas continuam ISO e são transformadas nas bordas de apresentação. |
| V11 `bc70079` | Metadados de backup formato 2 incluem nome opcional; leitura de formato 1 mantida; schema financeiro 3 preservado. Backup não leva entitlement. |
| V12 `b2e65a0` | Validação do SQLite/metadata/relacionamentos e transferência/restauração reforçadas, com cópia intermediária/journal e testes de integridade/rollback conforme [Fase 2](FASE2_SEGURANCA_CONFORMIDADE.md). |

Não foi identificada migração de Room, Compose, cloud database, login ou backend próprio no histórico local. Não prometa sincronização com base nas tabelas antigas de marcação.

## Billing e fronteira de domínio

Billing foi preparado em `896c0f5` e integrado em `a18a4d0`/`c2e97c2`. O produto atual documentado usa Billing Library 9.1.0, compra INAPP `full_access`, preço obtido de ProductDetails e estados de domínio separados da UI. Os callbacks foram ajustados para evitar reconstrução desnecessária da tela. Consulta/restauração do entitlement é independente do arquivo de backup. Eventos e categorias reais de telemetria do SDK permanecem questão de conformidade; ver [Data Safety](HISTORICO_SEGURANCA.md).

## Arquivos, exportação e identidade

PDF/CSV e intents de compartilhamento coexistem com FileProvider/SAF para transferências. CSV V11 moveu a escrita para `SalesCsvExporter`, mantendo campos de relatório e banco. Nome do negócio é preferência privada e pode ser incluído nos PDFs/mensagens; a política offline reside em assets. Câmera/galeria são acionadas por interfaces do Android segundo os documentos V11; comportamento em cada dispositivo continua sujeito a homologação.

## Plataforma e toolchain

| Período | Estado registrado |
|---|---|
| Base 29/09 | AGP/Gradle/Kotlin da base, compile/target API 35, min 23. |
| 29/09 | Ajuste de compile/target para API 36 e min 24; configuração V0 já demonstrava `com.vendassimples.app`. |
| Fase 4 (15–16/10) | AGP 8.5.2→8.9.1 para API 36, Wrapper Gradle 8.7→8.11.1, JBR 21; Kotlin 1.9.24 mantido nesta fase. Três erros lint corrigidos sem supressão. |
| Fase 7B.1 | Kotlin Gradle Plugin atualizado para 2.2.20 com regressão específica. |
| V12 integrada | applicationId `com.vendassimples.app`, namespace legado, min 24, compile/target 36, Billing 9.1.0. |

Os relatórios de fase contêm a matriz exata, fontes oficiais consultadas e limitações. Não usar esta tabela como recomendação para versões futuras; conferir compatibilidade na época da mudança.

## Invariantes de arquitetura para mudanças

Preservar IDs e relações cliente→venda→parcela/pagamento/foto, migração versionada, escrita monetária exata, separação de Billing, preferência de nome fora do schema e rollback seguro. Testar com dados sintéticos e não copiar arquitetura/segredos da referência. Consulte [AGENTS](../AGENTS.md), [DECISOES](DECISOES.md), [skill de backup](../.agents/skills/vendas-simples-backup/SKILL.md) e [skill de segurança](../.agents/skills/vendas-simples-seguranca/SKILL.md).
