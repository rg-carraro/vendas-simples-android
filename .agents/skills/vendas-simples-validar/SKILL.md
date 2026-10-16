---
name: vendas-simples-validar
description: Planejar ou executar validação financeira, de backup e de isolamento do Vendas Simples. Use para mudanças nessas regras ou homologação do MVP.
---

# Validar Vendas Simples

Leia [validação](../../../docs/VALIDACAO.md) e [decisões](../../../docs/DECISOES.md). Sem aplicativo no destino, valide documentação e declare testes funcionais não executados.

Escolha cenários pelo impacto: dinheiro exige totais independentes; parcelas exigem soma e calendário; fotos exigem vínculo por venda pai; relatórios exigem período e população iguais nas telas/saídas; backup exige consistência e imagens.

Use dados sintéticos. Não fixe resultado para regra aberta: registre a decisão necessária antes de chamar o comportamento de correto. Diferencie recebimento por data da venda de recebimento por data do pagamento.

Na implementação, execute build com wrapper do destino e testes pertinentes. Inclua cancelamentos, reinício e permissões nos fluxos afetados. Não execute build na origem nem trate compilação como homologação.

Registre commit, ambiente, cenários, resultados e limitações. Verifique independência do AleJoias e compatibilidade pretendida com APK comercial anterior. Não confunda arquivo de backup com recuperação validada. Após os checks pertinentes passarem, amplie testes apenas por novas falhas ou alterações.

Para navegação/layout, consulte [interface](../vendas-simples-interface/SKILL.md); para cópia e recuperação SQLite, consulte [backup](../vendas-simples-backup/SKILL.md). Não execute a ferramenta de UI contra aparelho real ou base com dados existentes.

Consulte também docs/HISTORICO_TESTES.md para identificar a cobertura histórica e lacunas; repita no commit atual os checks exigidos pela mudança. Não reclassifique teste antigo, AndroidTest compilado ou relato do usuário como execução física própria.