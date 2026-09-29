# Plano de validação

Status: cenários do aplicativo **não executados neste repositório**, que ainda não contém código Android. Não reaproveitar marcações de sucesso da origem.

## Documentação nesta fase

Verificar links locais, consistência entre escopo e backlog, frontmatter das skills e ausência de código/dados importados. Revisar mudanças apenas no destino e conferir estado da origem em leitura sem locks opcionais.

## Cenários futuros

| ID | Cenário | Resultado esperado |
| --- | --- | --- |
| V-01 | Instalação limpa e modo avião | Primeiro uso vazio; venda salva e recuperada após reinício |
| V-02 | Venda de R$ 100,00, pagamentos de R$ 30,00 e R$ 20,00 | Pago R$ 50,00; saldo R$ 50,00 em card, cliente e relatório |
| V-03 | R$ 100,00 em três parcelas | Soma R$ 100,00; centavo residual conforme decisão Q-03 |
| V-04 | Vencimento no dia 31, fevereiro e virada de ano | Datas conforme calendário escolhido, sem mudança silenciosa de regra |
| V-05 | Correção de pagamento e edição/exclusão de parcela paga | Efeitos e confirmação conforme Q-04; sem órfãos ou totais duplicados |
| V-06 | Zero, negativo, valor acima do saldo e entrada inválida | Comportamento explícito conforme Q-03; nenhum crash ou persistência parcial |
| V-07 | Venda sem foto, várias fotos, câmera cancelada e permissão negada | Venda sem foto funciona; imagens válidas persistem em todas as parcelas |
| V-08 | Busca combinada com filtros; histórico do cliente | Lista consistente; totais da compra não dependem de subconjunto incompleto |
| V-09 | Venda em janeiro paga em fevereiro | Indicador recebido obedece Q-05; período está claro no painel/PDF |
| V-10 | Trocar mês após relatório por período; mês vazio | Painel, detalhamento e PDF usam o mês correto, inclusive totais zerados |
| V-11 | PDF com várias fotos e CSV com acentos/separadores | Conteúdo íntegro, totais conferidos; documentos legíveis e CSV interpretável |
| V-12 | Cobrar parcela parcial, quitada e compra com vínculo incompleto | Texto sem total estimado; quitada não gera cobrança indevida; envio sob controle do usuário |
| V-13 | Criar backup após gravar venda/pagamento/foto | Cópia consistente; inspeção isolada comprova registros, relações e BLOBs |
| V-14 | AleJoias original e Vendas Simples no mesmo aparelho | Apps/dados independentes; original preservado |
| V-15 | APK comercial anterior instalado | Atualização ou coexistência corresponde à decisão Q-02 e assinatura |
| V-16 | Tela estreita, fonte ampliada, teclado e rotação | Sem campos/ações inacessíveis ou sobreposição do cabeçalho |
| V-17 | Alertas com app aberto/fechado e permissão negada | Resultado registrado por ambiente; sem prometer comportamento não comprovado |

Após implementação, compilar com o wrapper do destino (`.\gradlew.bat assembleDebug`, se adotado) e executar cenários em aparelho/emulador. Registrar versão/commit, versão Android, dispositivo, dados sintéticos, resultado e falhas. Testar no mínimo a versão Android mínima adotada e uma versão recente pertinente ao piloto.

Backup: inspeção estrutural do arquivo não é teste de restauração pela UI. Quando existir recuperação, testar arquivo inválido, versão incompatível, interrupção e preservação da base anterior. Migrações exigem fixtures sintéticas das versões suportadas.

## Registro de execução

Cada execução deve informar data, responsável, commit, ambiente, IDs dos cenários, resultado (passou/falhou/não executado), evidências e limitações. Nesta fase não há build, APK ou homologação funcional para registrar.
