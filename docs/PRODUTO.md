# Produto Vendas Simples

Vendas Simples é um aplicativo Android independente do AleJoias, offline-first, para registrar vendas, acompanhar parcelas, pagamentos, clientes e relatórios. A branch `comercial` do AleJoias é apenas referência de fluxos; não é fonte de identidade, dados ou serviços.

O FREE permite até 30 vendas e clientes ilimitados. Ao chegar a 30, os dados continuam disponíveis para leitura, histórico, saldo, relatórios e pagamentos das vendas existentes. A tentativa de criar a 31ª venda abre o fluxo de desbloqueio; ela não apaga dados nem bloqueia o restante do app.

O único acesso pago planejado é a Licença Completa (`FULL_ACCESS`), compra única de R$ 49,90. Não existe assinatura ou mensalidade. A compra é restaurável via Google Play.

Mensagem comercial: “30 vendas grátis. Gostou? Desbloqueie a versão completa com um único pagamento. Sem mensalidade.” A comunicação não usa “vitalício” ou “Lifetime” e não promete suporte ou atualizações eternas.

O FREE oferece exportação CSV. O backup completo e a restauração de backup são benefícios da Licença Completa; a perda ou a validação pendente da licença nunca modifica nem apaga dados ou arquivos de backup.

O produto não reutiliza marca, credenciais, endpoints, banco real ou Google Planilhas do AleJoias. Não há conta ou backend no MVP. O contador FREE é local e poderá resetar após reinstalação; essa limitação deve ser apresentada na documentação e não pode ser tratada como antifraude.

## Critérios de aceite

- Instalação limpa abre offline e permite cadastrar até 30 vendas.
- Cliente 31 ou mais continua consultável sem limite.
- Venda 31 solicita desbloqueio e não cria registro parcial.
- Falha de licença, Billing ou rede não apaga vendas, pagamentos, fotos ou relatórios.
- A UI não conhece detalhes do Google Play Billing; recebe estados por uma abstração de domínio.
