---
name: vendas-simples-seguranca
description: Revisar segurança, privacidade, armazenamento local, falhas de licença e migrações do Vendas Simples.
---

Leia `docs/SEGURANCA_PRIVACIDADE.md` e `docs/REGRAS_FINANCEIRAS.md`. Não trazer marca, credenciais, endpoints, banco real ou Google Planilhas do AleJoias. Use dados sintéticos e não exponha dados pessoais em logs, fixtures ou commits.

Falha de licença, Billing ou rede nunca pode apagar dados. Valores novos usam centavos em `Long` ou decimal seguro, nunca Float/Double. Mudança de schema exige migração versionada, fixtures e teste de preservação de IDs, pagamentos e fotos.
