---
name: vendas-simples-backup
description: Alterar ou validar backup e restauração SQLite do Vendas Simples com escolha de destino, formato compatível e preservação de dados e licença.
---

# Backup e restauração do Vendas Simples

Leia [segurança](../../../docs/SEGURANCA_PRIVACIDADE.md),
[monetização](../../../docs/MONETIZACAO.md) e
[validação](../../../docs/VALIDACAO.md). Inspecione `BackupManager.kt` e
os launchers de `MainActivity.kt`; existem fluxos SAF e um fluxo interno de
compartilhamento, que não devem ser confundidos.

Preserve `CreateDocument` para escolher destino/nome e `OpenDocument` para
selecionar backup. Cancelar não deve gravar/restaurar. Confirme sucesso apenas
após concluir a escrita; falha não pode apagar dados operacionais. Provedor
remoto tem disponibilidade própria: escolha de destino não comprova upload.

Preserve checkpoint, cópia intermediária e limpeza de temporários. Consulte
o formato e schema no código/documentação; não aceite SQLite genérico ou
backup AleJoias como compatível. Na restauração, mantenha validação de tabelas,
metadata e integrity_check, confirmação de substituição e rollback de falha.
Confira vendas, parcelas, pagamentos, IDs e fotos/BLOBs com fixture sintética.

Backup é benefício de FULL_ACCESS conforme documento de produto. Restaurar
compra restaura licença; restaurar backup restaura dados e nunca concede
FULL_ACCESS. Não importar regras comerciais ou formato de outro aplicativo.

Teste seleção/cancelamento, destino indisponível, arquivo inválido/versão
incompatível, backup com fotos e restauração com preservação da base anterior
em falha. Inspeção de arquivo não equivale a restauração homologada; registre
limites de interrupção, migração e reinício. Nunca use dados reais nos testes.
