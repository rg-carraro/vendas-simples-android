# Release versão 11 — personalização

Data: 11/10/2026. Solicitação: pedido anexado pelo usuário. Base: marco V10 beta da Store, branch `master`, árvore limpa no início. Sem push, merge ou upload no Play Console.

## Melhorias implementadas

- Preferência `businessName` local, normalizada, até 120 caracteres e acentos preservados. Primeiro acesso à funcionalidade oferece configuração uma vez, inclusive em atualização V10; Agora não, Voltar ou fechamento não repetem o convite. Configurações no menu principal permitem editar/remover o nome também no FREE.
- Cabeçalho usa o nome, mantendo o slogan e o fallback Vendas Simples. Na UI o título tem até duas linhas com reticências para preservar espaço em tela estreita/fonte ampliada; o nome completo permanece nas Configurações, assinatura e PDF. Persistência em SharedPreferences independente do SQLite e da licença.
- Cobrança WhatsApp mantém valores, parcelas e saldos existentes; agradecimento usa uma única assinatura com o nome. Resumo em imagem recebe texto personalizado, incluindo o fallback de compartilhamento. PDF/CSV compartilhados recebem identificação no texto do Intent; backup continua no fluxo anterior.
- Os três documentos (card, resumo financeiro/mensal e extrato de cliente) usam o gerador PDF comum com nome no cabeçalho, quebrado por largura medida inclusive sem espaços. Valores e cálculos preservados.
- Datas completas apresentadas em `dd/MM/yyyy` em cards, detalhes, histórico, filtros, formulários, cobrança, PDF e CSV. Conversão de volta a ISO ao aplicar filtros ou salvar venda/pagamento/correção. Sem alteração dos registros, schema, ordenação e consultas. Referências mensais e nomes técnicos de arquivos preservados.
- Miniatura do formulário fica acima de Remover foto, com 16 dp entre eles, botão alinhado aos demais e toque na miniatura para ampliar. Galeria, câmera e remoção preservadas.

## Arquivos modificados

`app/build.gradle`, `MainActivity.kt`, novos `BusinessPreferences.kt` e `BusinessIdentity.kt`, novos testes `BusinessIdentityTest.kt` e `ReleaseCompatibilityTest.kt`; `README.md`, `docs/PRODUTO.md`, `docs/DECISOES.md`, `docs/RELEASE_PLAY_STORE.md`, `docs/VALIDACAO.md`, `docs/ARQUITETURA.md` e este relatório.

## Identidade e regras preservadas

Application ID `com.vendassimples.app`, versionCode **11**, versionName **1.0.11**. minSdk 24, compileSdk/targetSdk 36, Billing 9.1.0, produto `full_access`, opção `full-access`. FREE 30, clientes ilimitados, CSV no FREE, backup/restauração no FULL_ACCESS, compra única com preço do Google Play. Sem novas dependências, permissões, backend ou login.

## Verificações

`clean test assembleDebug bundleRelease --offline --no-daemon`: BUILD SUCCESSFUL em 5m09s. Após limitar o título da UI a duas linhas, `test assembleDebug bundleRelease` executado novamente: BUILD SUCCESSFUL em 2m50s. JDK do Android Studio, Gradle 8.7 e SDK local; sem novas dependências. Avisos de depreciação de Locale/ajuste de teclado e parâmetro não utilizado, sem erros.

16 testes em debug e 16 em release, zero falhas/erros: 8 anteriores (dinheiro e formato de backup), 5 de nome/textos/datas e 3 de compatibilidade FREE/FULL_ACCESS/estados Billing. `git diff --check` e links locais dos documentos afetados aprovados. Conferência do manifesto diretamente no AAB por bundletool 1.16.0 confirmou applicationId/versões/minSdk/targetSdk; `bundletool validate` terminou com código 0.

Não havia aparelho/emulador conectado (`adb devices` vazio). Testes funcionais manuais, persistência da preferência pela UI, geração visual de PDFs e envio WhatsApp não executados. Testes de domínio não comprovam transação real no Google Play nem homologam os fluxos completos de cadastro, pagamentos, fotos ou backup.

A primeira tentativa de build falhou ao acessar `C:\.android`. A execução foi ajustada para o JDK do Android Studio, SDK local, cache Gradle do repositório e diretório Java correto. Nenhuma credencial foi copiada ao repositório ou exibida.

## Artefato e assinatura

Entrega: `app/build/outputs/bundle/release/vendas-simples-v11.aab`, cópia idêntica de `app-release.aab`. Tamanho: **5.311.870 bytes**. SHA-256: `e6d86a1158328377eed97aacab20c186d56fc1fd88a1903cce196035dea0cb1c`.

Assinado pela configuração release local existente. `jarsigner -verify -verbose -certs` retornou código 0 e `jar verified.`; certificado válido até 15/02/2054. A ferramenta avisa sobre certificado autoassinado/cadeia não reconhecida pelo truststore Java, ausência de timestamp e diferença de leitura JarFile/JarInputStream. O manifesto ZIP está na posição 931, após o conteúdo; uma verificação independente conferiu os hashes SHA-256 dos **929 arquivos de conteúdo**, todos cobertos pelo manifesto assinado. A assinatura criptográfica passou; a correspondência do certificado com a chave de upload cadastrada na Play ainda exige conferência no Console.

APK debug: `app/build/outputs/apk/debug/app-debug.apk`. Evidências locais ignoradas pelo Git: `.gradle-user/v11-build-final.log`, `v11-bundle-validation.log`, `v11-signature.log`, `v11-manifest.xml` e `v11-artifact.json`. Artefatos e credenciais não integram o commit.

## Git

Commit local de implementação, testes e documentação; branch e hash informados na entrega. Sem alterações preexistentes no início e sem push, merge ou publicação. O bundle foi gerado antes do commit final; sua metadata de procedência Git pode apontar ao HEAD anterior, com as alterações compiladas da árvore local.

## Limites e pendências

Nome do negócio não integra o backup SQLite existente: restauração não transfere a preferência, e reinstalação exige configurá-la novamente. Cabeçalho PDF permite nomes longos, mas a validação visual em PDF real e aparelho continua pendente. Rotação/process death e rascunhos mantêm os limites existentes. Compatibilidade de atualização com certificado da versão publicada e disponibilidade do código 11 precisam ser conferidas no Play Console pelo responsável.

## Checklist manual no celular antes do upload

- [ ] Atualizar sobre V10 com dados sintéticos e confirmar preservação de clientes, vendas, pagamentos, fotos e licença.
- [ ] Primeiro acesso sem nome: convite opcional, apresentação genérica, Agora não/Voltar e reinício sem repetição.
- [ ] Salvar AleJoias, fechar/reabrir; editar para nome com acentos e nome longo de 120 caracteres; apagar o nome e verificar fallback.
- [ ] Tela inicial e Configurações no FREE e FULL_ACCESS; retrato/paisagem, tela estreita, fonte ampliada e teclado sem ações inacessíveis.
- [ ] Cobrar venda à vista e parcelada com pagamento parcial; conferir dados, vencimento brasileiro e única assinatura. Quitada não deve cobrar. Testar com/sem nome e cancelar o envio.
- [ ] Compartilhar imagem do resumo, PDF e CSV; conferir identificação e fallback sem WhatsApp instalado.
- [ ] Gerar os três PDFs com/sem nome, acentos, nome longo com/sem espaços, múltiplas páginas/fotos; conferir cabeçalhos, valores, totais, saldos e datas sem cortes.
- [ ] Criar/editar cliente e venda, datas no calendário e filtros; reiniciar e conferir persistência e ordenação.
- [ ] R$ 100,00 em três parcelas e pagamentos de R$ 30,00 e R$ 20,00; soma das parcelas R$ 100,00, pago R$ 50,00 e saldo R$ 50,00 nos fluxos correspondentes.
- [ ] Adicionar, ampliar e remover fotos; cancelar galeria/câmera/formulário; verificar relação por compra após reabrir.
- [ ] FREE: criar até 30 vendas, bloquear a 31ª; manter consultas, pagamentos existentes e CSV.
- [ ] FULL_ACCESS: venda acima do limite, backup e restauração com fotos; configurar nome separadamente após reinstalação.
- [ ] Pela Play com conta de teste: preço, compra aprovada/pendente/cancelada/recusada, indisponibilidade, reconhecimento e restauração da licença anterior.
- [ ] Testar offline, Android 24 e versão recente; conferir versão e certificado antes do upload manual.

## Notas sugeridas para Google Play

Personalize o aplicativo com o nome do seu negócio e use essa identidade nas mensagens e PDFs. Datas no padrão brasileiro e melhor espaçamento ao remover fotos de vendas.
