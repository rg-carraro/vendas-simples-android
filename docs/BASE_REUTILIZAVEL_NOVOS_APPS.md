# Base reutilizável para novos aplicativos Android

Este guia separa técnicas que podem inspirar outro aplicativo de regras e configurações exclusivas do Vendas Simples. Reaproveitar significa revisar, adaptar e testar em um repositório independente; não significa copiar o produto inteiro.

| Componente | Classificação | Condição de reaproveitamento |
|---|---|---|
| Organização Android/Kotlin, Gradle e estrutura de testes | Reutilizável mediante adaptação | Rever AGP, Kotlin, SDKs, JDK, min/target/compile SDK e dependências suportadas no momento. |
| Offline-first, banco local e separação de camadas | Reutilizável mediante adaptação | Definir modelo, ameaças, ciclo de vida, migrações, backup automático e retenção para o novo domínio. |
| SQLite e relações entre entidades | Específico do Vendas Simples | O padrão de transações/migrações é referência; não copiar schema de clientes/vendas sem necessidade e revisão. |
| Abstração de Billing e estados de licença | Reutilizável mediante adaptação | Cada aplicativo precisa de seu próprio produto, tipo de compra, entitlement, estados, testes e configuração Play. Não copiar `full_access` por padrão. |
| Limite FREE de 30 vendas e compra única | Específico do Vendas Simples | Regra comercial deste produto; não transportar como default. |
| Backup/restore validado por SQLite e metadados versionados | Reutilizável mediante adaptação | Escolher formato/domínio, compatibilidade, atomicidade, rollback, anexos e licença do novo app; testar dados sintéticos e arquivo malformado. |
| CSV, PDF, compartilhamento e SAF | Reutilizável mediante adaptação | Rever privacidade, escaping, locale, destinatário e conteúdo realmente necessário. A ação explícita do usuário deve continuar clara. |
| Navegação compacta, estados vazios e acessibilidade | Reutilizável mediante adaptação | Revalidar identidade visual, textos, fontes ampliadas, tamanhos de tela e áreas de toque. |
| Padrões de testes unitários/instrumentados e regressão | Reutilizável com poucas alterações | Ajustar fixtures, regras, dispositivos/API, dados sintéticos e evidências; não transportar aprovação entre produtos/versões. |
| Checklist de assinatura, metadados, hash e release | Reutilizável com poucas alterações | Usar chave e processo autorizados para aquele app; segredos nunca devem entrar no Git ou nos relatórios. |
| Estrutura documental, AGENTS.md e skills | Reutilizável mediante adaptação | Remover decisões e referências específicas, conferir links e estabelecer regras próprias de produto e segurança. |
| Identidade, applicationId, contatos, política, canais e textos públicos | Não reutilizar automaticamente | Criar valores e política próprios; confirmar URL, responsável e disponibilidade pública. |
| Dados, fotos, bancos, backups reais, segredos, keystores e tokens | Não reutilizar | Nunca copiar. Usar dados sintéticos e segredos novos sob controle do proprietário. |
| Conclusão de Data Safety ou resposta de suporte | Não reutilizar automaticamente | Depende dos fluxos, SDKs, destinos, finalidades e versões de cada app. Fazer avaliação nova. |

## Checklist de início isolado

1. Criar repositório e `applicationId` próprios; estabelecer nome, pacote, ícones e contatos independentes.
2. Documentar objetivo, usuários, dados, regras, retenção, exportação, restore e exclusão.
3. Escolher armazenamento, modelo de ameaça e migrações próprios; gerar apenas fixtures sintéticas.
4. Se houver monetização, criar configuração de produto/licença apropriada, chaves próprias e fluxos de compra/restauração; preço vem da loja.
5. Escrever uma política de privacidade fiel aos fluxos e hospedá-la em URL controlada pelo responsável; verificar a versão online e offline.
6. Inventariar todos os SDKs transitivos, rede, destinatários e finalidades; decidir Data Safety com evidência específica, sem herdar a conclusão de outro app.
7. Configurar assinatura segura, CI/build e documentação; validar metadados e certificado sem expor credenciais.
8. Executar regressão, acessibilidade, compatibilidade Android, backup e distribuição em ambiente apropriado.

A origem AleJoias descrita em `REFERENCIA.md` permanece somente leitura e é uma fonte de procedência histórica, não de código autorizado a copiar automaticamente.
