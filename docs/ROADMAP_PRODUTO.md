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

## Incremento funcional V14 — planejado, não implementado

Decisões aprovadas pelo proprietário em 19/10/2026; classificação: **V14 FUNCIONAL — PLANEJADA, NÃO IMPLEMENTADA**.

1. Remover “Política de Privacidade” do menu de três pontos, mantendo o acesso em “Sobre o aplicativo”, a versão local/offline, o link oficial e as demais opções do menu.
2. Atualizar todos os PDFs existentes: destacar o nome personalizado do negócio e inserir o ícone oficial do Vendas Simples somente na primeira página de cada documento. Preservar títulos, valores, fotografias, totais, paginação e fallback atual sem nome personalizado.

Antes da implementação do item de PDF, identificar todos os geradores e pontos de paginação. Homologar documentos curtos e multipágina, com/sem nome e fotografias; conferir presença do ícone na primeira página e ausência nas seguintes. O planejamento não altera a V13 nem constitui registro de implementação.
