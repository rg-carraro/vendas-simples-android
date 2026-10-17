# Produto Vendas Simples

Vendas Simples é um aplicativo Android independente do AleJoias, offline-first, para registrar vendas, acompanhar parcelas, pagamentos, clientes e relatórios. A branch `comercial` do AleJoias é apenas referência de fluxos; não é fonte de identidade, dados ou serviços.

O FREE permite até 30 vendas e clientes ilimitados. Ao chegar a 30, os dados continuam disponíveis para leitura, histórico, saldo, relatórios e pagamentos das vendas existentes. A tentativa de criar a 31ª venda abre o fluxo de desbloqueio; ela não apaga dados nem bloqueia o restante do app.

O único acesso pago é a Licença Completa (`FULL_ACCESS`), compra única com preço consultado no Google Play. Não existe assinatura ou mensalidade. A compra é restaurável via Google Play.

Mensagem comercial: “30 vendas grátis. Gostou? Desbloqueie a versão completa com um único pagamento. Sem mensalidade.” A comunicação não usa “vitalício” ou “Lifetime” e não promete suporte ou atualizações eternas.

O FREE oferece exportação CSV. O backup completo e a restauração de backup são benefícios da Licença Completa; a perda ou a validação pendente da licença nunca modifica nem apaga dados ou arquivos de backup.

O produto não reutiliza marca, credenciais, endpoints, banco real ou Google Planilhas do AleJoias. Não há conta ou backend no MVP. O contador FREE é local e poderá resetar após reinstalação; essa limitação deve ser apresentada na documentação e não pode ser tratada como antifraude.

## Critérios de aceite

- Instalação limpa abre offline e permite cadastrar até 30 vendas.
- Cliente 31 ou mais continua consultável sem limite.
- Venda 31 solicita desbloqueio e não cria registro parcial.
- Falha de licença, Billing ou rede não apaga vendas, pagamentos, fotos ou relatórios.
- A UI não conhece detalhes do Google Play Billing; recebe estados por uma abstração de domínio.

## Personalização — versão 11

Nome do negócio ou do usuário persistido localmente em preferências, disponível também no FREE. A configuração inicial pode ser adiada e não se repete; a edição permanece acessível no menu principal. Sem nome, mantém Vendas Simples e agradecimento genérico. Limite de 120 caracteres, espaços normalizados e acentos preservados.

O nome identifica a tela inicial, a assinatura da cobrança e o texto do resumo compartilhado, os compartilhamentos de PDF/CSV e os cabeçalhos dos PDFs de card, resumo financeiro e extrato do cliente. Nomes longos quebram linha no cabeçalho PDF. Datas completas usam `dd/MM/yyyy` na UI e saídas; meses de referência continuam mensais.

O backup completo FULL_ACCESS inclui o nome nos metadados do mesmo arquivo SQLite (formato 2). A restauração recupera o nome e preserva o nome atual quando o backup antigo/formato 1 não contém essa informação ou o nome importado está vazio/nulo. Personalização continua gratuita; backup não concede licença. Não há conta, backend ou sincronização.
