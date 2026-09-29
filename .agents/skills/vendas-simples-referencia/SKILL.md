---
name: vendas-simples-referencia
description: Consultar a branch comercial do AleJoias e preparar reaproveitamento autorizado no Vendas Simples com origem somente leitura e procedência rastreável.
---

# Consultar a referência comercial

Leia [procedência](../../../docs/REFERENCIA.md) e [arquitetura](../../../docs/ARQUITETURA.md). Use o hash fixado, não outra branch ou arquivos locais modificados.

Consulte com `git -C <origem> show <hash>:<arquivo>` e `git -C <origem> ls-tree -r --name-only <hash>`. Para status, use `git --no-optional-locks -C <origem> status --short --branch`. Não faça checkout, fetch ou qualquer escrita na origem.

Relacione capacidades com arquivos e diferencie documentação de comportamento comprovado. Registre limitações. Se o snapshot estiver ausente, informe sem substituir pela master.

Quando houver solicitação de implementação, copie seletivamente os arquivos necessários para o destino e registre hash/adaptações. Não trazer `.git`, dados reais, builds, chaves, AGENTS.md ou skills da origem. Autorizações de push não se transferem.

Antes de gerar APK, confira applicationId, provider, banco, marca, permissões e decisão sobre instalações comerciais anteriores. Preserve relações de parcelas, pagamentos e fotos. Nesta fase documental, produza análise e plano sem importar código Android.
