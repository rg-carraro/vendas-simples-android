# Guia de evolução do AleJoias Vendas com lições do Vendas Simples

## Objetivo
Transferir padrões de usabilidade e práticas de QA do Vendas Simples para o AleJoias Vendas, preservando identidade visual, funcionalidades e dados existentes.

## Regra de ouro
Não portar o código ou a monetização automaticamente. Primeiro auditar o repositório AleJoias, seu AGENTS.md, arquitetura, telas, esquema de dados, fluxos, temas, recursos exclusivos, versão e estratégia de distribuição.

## Etapas
1. Inventário: telas, recursos, entidades, armazenamento, backups, exportações, relatórios, branding e testes existentes.
2. Matriz comparativa: padrão do Vendas Simples → tela AleJoias → benefício → risco → decisão aplicar/adaptar/não aplicar.
3. Capturar referência visual antes de mudanças; registrar cores, logo, tipografia e navegação AleJoias.
4. Refatorar em pequenos lotes: navegação; formulários/fotos; diálogos; listas; financeiro/relatórios; personalização somente se fizer sentido.
5. Testes de regressão por lote com dados reais de teste e comparação dos valores.
6. Homologação visual pelo responsável antes de release.

## Proibições
- Não trocar identidade, nome, ícones ou tema AleJoias pelos do Vendas Simples.
- Não copiar regras FREE/FULL, Billing, preço, limite de vendas ou permissões sem decisão explícita.
- Não modificar ou eliminar funções exclusivas, nem executar migração destrutiva.
- Não assumir que os dois apps têm as mesmas classes ou arquitetura.

## Entregáveis do Codex
Matriz de equivalência, lista de recursos preservados, plano incremental, arquivos alterados, testes, screenshots comparativos, riscos e rollback.
