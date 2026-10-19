---
name: vendas-simples-seguranca
description: Revisar segurança, privacidade, armazenamento local, falhas de licença e migrações do Vendas Simples.
---

Leia `docs/SEGURANCA_PRIVACIDADE.md` e `docs/REGRAS_FINANCEIRAS.md`. Não trazer marca, credenciais, endpoints, banco real ou Google Planilhas do AleJoias. Use dados sintéticos e não exponha dados pessoais em logs, fixtures ou commits.

Falha de licença, Billing ou rede nunca pode apagar dados. Valores novos usam centavos em `Long` ou decimal seguro, nunca Float/Double. Mudança de schema exige migração versionada, fixtures e teste de preservação de IDs, pagamentos e fotos.

Leia docs/HISTORICO_SEGURANCA.md e docs/PROBLEMAS_E_SOLUCOES.md antes de mudar controle existente; diferencie código presente, teste executado e declaração externa. Ao concluir, atualize a evidência de segurança da versão e mantenha pendências Data Safety/Billing explícitas.

Antes de alterar Billing, SDKs, transmissão, armazenamento, exportação ou backup, consulte `docs/CONHECIMENTO_GOOGLE_PLAY_DATA_SAFETY.md`. Reavalie a política e a declaração para qualquer novo SDK ou fluxo externo; a orientação de suporte é condicionada à arquitetura e não é aprovação formal.

## Revisão contínua de privacidade

Consulte `docs/CONHECIMENTO_GOOGLE_PLAY_DATA_SAFETY.md`, `docs/PENDENCIAS_ATUAIS.md` e `docs/MANUTENCAO_POS_LANCAMENTO.md`. A orientação do suporte sobre Billing aplica-se somente à arquitetura e fluxo descritos; não é aprovação formal nem regra universal. Inventarie dependências/fluxos após mudanças, revalide a política offline e URL pública e não trate igualdade dos arquivos locais como prova do conteúdo live.
