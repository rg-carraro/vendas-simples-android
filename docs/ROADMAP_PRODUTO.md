# Roadmap do produto

## Fase 0 — documentação (atual)

Consolidar decisões, regras financeiras, monetização, segurança, critérios de aceite e referência. Não iniciar refatoração ampla.

## Fase 1 — núcleo local do MVP

Implementar e validar vendas, clientes, parcelas, pagamentos, fotos, consultas e relatórios offline. Usar centavos em `Long` ou decimal seguro e migrações testadas.

## Fase 2 — entitlement e Billing

Adicionar abstração de domínio para FREE/FULL_ACCESS, contador local de 30 vendas e fluxo de desbloqueio. Integrar Google Play Billing atrás da abstração, com restauração de compra e falhas não destrutivas.

## Fase 3 — piloto e release

Testar aparelho/emulador, compra de teste, reinstalação, migrações, backup e isolamento do AleJoias. Resolver privacidade, assinatura e checklist Play Store.

## Posterior, sem compromisso

Backend antifraude, conta, sincronização automática em nuvem, restauração de backup pela UI, planos adicionais, assinatura, estoque e recursos fiscais só entram após decisão explícita e escopo próprio. Login e sincronização não fazem parte do roadmap atual.
