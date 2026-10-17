# Fase 6B.1 — Homologação física da V11 e preparação V11 → V12

Data: 16/10/2026. Repositório `rg-carraro/vendas-simples-android`; branch `fix/v12-seguranca-conformidade`; versão de trabalho `11 / 1.0.11`.

## 1. Estado inicial do Git e preservação

Na inspeção desta execução, `HEAD` é `8aa34881755f222f9b090691aaa6127b31b1a363`, na branch `fix/v12-seguranca-conformidade`. A referência de tag `v1.0.11-rc.1` continua presente; o commit apontado pela tag é `8aa34881755f222f9b090691aaa6127b31b1a363` (a referência anotada da tag tem objeto `ca2e8417c30728a091150c9bd8aaee93a46aff0d`). `versionCode`/`versionName` permanecem fora do escopo e não foram alterados.

Antes desta execução havia 35 entradas em `git status --short`, incluindo alterações locais e trabalho das fases anteriores. Nenhum arquivo existente foi editado ou descartado nesta execução. Este relatório é o único arquivo novo. Não houve staging, commit, tag, push, geração de AAB, alteração de produção ou publicação.

## 2. Identificação do aparelho e versão instalada

O pedido informa um Samsung SM-S906E (Galaxy S22+), Android 16 / API 36, com V11 `com.vendassimples.app` 11 / 1.0.11, target SDK 36 e instalador Google Play (`com.android.vending`). O pedido também informa erro de permissão para o usuário Android 150 e proíbe contornar essa restrição.

**Não confirmado por ADB na execução inicial desta fase.** O diagnóstico da Fase 6B.2 confirmou agora que o ADB absoluto e as consultas por serial também falham antes da conexão com `adb_utils.cpp:316 Cannot mkdir '\\.android': Permission denied`, inclusive após definir `ANDROID_USER_HOME` temporariamente no processo para `<ANDROID_USER_HOME>`. Portanto, modelo, serial, sistema, pacote, versão, caminho APK e origem Play continuam sendo dados fornecidos pelo usuário/contexto anterior, não observações independentes pelo ADB nesta sessão. Não houve tentativa de acessar outros perfis ou contornar permissões.

## 3. Estado inicial do aplicativo e privacidade

Não foi possível observar a abertura da V11, tela inicial, cadastro, vendas, fotos, nome do negócio, licença ou estabilidade. Nenhum banco, foto, contato, documento ou log privado foi extraído. Nenhum fluxo do aplicativo foi aberto ou alterado por esta execução.

O pedido registra que é uma instalação real da Play e adverte que pode conter dados importantes. Assim, registros existentes foram tratados como reais. Sem confirmação manual do usuário, não se presume que o ambiente seja apropriado para cadastrar fixtures; nenhuma venda, pagamento, cliente ou foto foi criado, editado, pago, exportado ou removido. A situação de `FULL_ACCESS` também não foi verificada; nenhum estado de compra/licença foi alterado.

## 4. Testes físicos e resultado

| ID | Cenário | Resultado nesta execução | Evidência/limite |
|---|---|---|---|
| P01 | Enumerar dispositivo (`adb devices -l`) | **BLOQUEADO** | ADB não iniciou a consulta por erro de permissão ao criar `\\.android`. |
| P02 | Confirmar modelo, Android/API e pacote/versão instalados | **NÃO EXECUTADO** | Sem serial confirmado; comandos restantes não foram chamados. |
| A01–A05 | Abrir, navegar, fechar/reabrir e observar travamentos | **NÃO EXECUTADO** | Requer inspeção manual no aparelho; sem observação nesta sessão. |
| A06 | Testar sem internet | **NÃO EXECUTADO** | Exigiria mudança temporária de conectividade e autorização; não realizada. |
| B01–B06 | Consultar clientes, vendas, valores, filtros, parcelas e pagamentos | **NÃO EXECUTADO** | Nenhuma leitura de tela foi feita. Não houve alteração de registros. |
| C01–C05 | Abrir foto, câmera e galeria | **NÃO EXECUTADO** | Não observado; não anexar/remover imagem real. |
| D01–D05 | CSV, PDF, compartilhamento e cobrança WhatsApp | **NÃO EXECUTADO** | Nenhum arquivo foi exportado e nenhuma mensagem foi preparada/enviada. |
| E01–E04 | Estado FULL_ACCESS, recursos e Billing | **NÃO EXECUTADO** | Sem consulta à UI/Play; não iniciar compra ou restauração. |

Resultados automatizados anteriores, como os 43 testes unitários por variante e compilação Android instrumentada, estão no relatório da Fase 6B e não contam como testes físicos desta execução. A compilação de instrumentados não significa execução no Samsung.

## 5. Pendências de participação manual

No aparelho, o responsável pode confirmar sem alterar dados: se a V11 abre; se a tela inicial carrega; se a navegação e retorno funcionam; se fechar/reabrir mantém o estado; se clientes, vendas, parcelas, saldos, nome comercial e licença aparecem como esperado. Não informar nomes, valores ou capturas com conteúdo real neste relatório. A licença pode ser indicada apenas como ativa/inativa/desconhecida.

Não executar offline até autorização para alterar conectividade temporariamente. Não criar registros fictícios até autorização explícita e confirmação de que há ambiente/conta de teste sem dados de clientes. Não exportar CSV/PDF real, iniciar compartilhamento, testar pagamento, usar câmera/galeria em registros reais ou iniciar fluxo do Billing nesta etapa.

## 6. Problemas encontrados

1. **Bloqueador de acesso ADB:** o executável falha ao criar `\\.android` devido à restrição de permissão. A consulta não revela se o telefone está conectado/online nesta sessão.
2. **Sem evidência visual desta execução:** inicialização, navegação, dados e licença precisam de observação humana no aparelho.
3. **Risco de dados reais:** o dispositivo contém instalação Play real segundo o pedido; fluxos de escrita/exportação permanecem suspensos até ambiente sintético autorizado.

Não foi observada falha funcional da V11, pois não foi possível executá-la/inspecioná-la. Isso não equivale a aprovação.

## 7. Plano opcional de dados fictícios

Executar apenas após autorização específica e confirmação de ambiente apropriado/isolado. Preferir um dispositivo de teste sem dados reais ou uma instalação de teste separada; não substituir a V11 Play existente. Fixture proposta, ainda não criada:

- Cliente: `Cliente Teste V12`.
- Venda à vista: R$ 120,00.
- Venda parcelada: R$ 300,00 em três parcelas de R$ 100,00.
- Pagamento parcial: R$ 40,00 em uma parcela, somente após conferir o comportamento efetivo do fluxo.
- Nome do negócio: `Loja Teste V12`.
- Foto: imagem fictícia sem pessoas ou metadados pessoais.

Antes de usar, registrar os totais esperados com base na regra implementada, IDs/quantidades sintéticos sem expor informações pessoais, e um modo de identificar que os registros pertencem ao teste. Não presumir distribuição ou rateio do pagamento parcial. Nenhum desses dados foi cadastrado nesta fase.

## 8. Plano de atualização V11 → V12

Não executar agora; não há V12 assinada/distribuída nesta etapa. Quando existir V12 e houver autorização de publicação/atualização:

1. Em aparelho de teste e conta autorizada, confirmar por ADB package, versão, origem e assinatura da V11; registrar data/modelo/API sem serial público.
2. Confirmar que somente dados fictícios estão presentes e documentar clientes, vendas, parcelas, pagamentos, foto, nome e estado de licença sem capturar conteúdo pessoal.
3. Confirmar entitlement FULL_ACCESS pela Play sem iniciar compra real; registrar apenas ativo/inativo/desconhecido.
4. Distribuir V12 na mesma faixa Play, após autorização específica, com application ID e assinatura compatíveis. Atualizar pelo Google Play; não desinstalar nem instalar APK debug.
5. Conferir se Android reconheceu atualização; repetir os inventários sintéticos e comparar clientes/vendas, relações/parcelas, pagamentos/saldos, fotos, nome e licença.
6. Conferir política local offline e link público, saídas PDF/CSV em dados sintéticos e funcionamento sem rede quando autorizado.
7. Testar backup/restauração apenas em ambiente isolado e FULL_ACCESS de teste; não restaurar sobre dados reais. Em qualquer divergência, parar e preservar aparelho/base para diagnóstico.
8. Registrar cada caso como APROVADO, REPROVADO, BLOQUEADO ou NÃO EXECUTADO, com evidência sanitizada.

## 9. Riscos, bloqueadores e classificação

O bloqueio ADB impede confirmação independente de identidade e versão; a participação manual necessária não ocorreu nesta sessão; os dados e a licença não foram inspecionados. Não houve alteração de dados, rede, licença, instalação ou versão.

**Classificação: APTO COM RESSALVAS para continuar a preparação documental; homologação física NÃO CONCLUÍDA.** A V12 não está homologada nesta fase. A próxima etapa recomendada é o usuário confirmar manualmente o estado observável no Samsung, e depois corrigirmos o acesso ADB dentro do perfil autorizado pelo usuário (sem acessar o perfil Android 150 nem contornar permissões) para validar modelo/pacote/versão por leitura. Só então executar os fluxos de leitura permitidos; escrita de fixtures e alteração de conectividade continuam exigindo autorização específica.

## 10. Estado final do Git

Branch continua `fix/v12-seguranca-conformidade`; `HEAD` e o commit da tag continuam em `8aa34881755f222f9b090691aaa6127b31b1a363`. Permanecem as 35 entradas locais preexistentes e este relatório novo (36 entradas ao final). Nenhum arquivo preexistente foi revertido ou limpo. Sem commit, push, tag, AAB ou publicação.

## 11. Adendo — diagnóstico ADB da Fase 6B.2

Ambiente observado no processo Codex em 16/10/2026:

| Item | Valor observado |
|---|---|
| Diretório atual | `<RAIZ_REPOSITORIO>` |
| `USERPROFILE` | `<DIRETORIO_USUARIO>` |
| `HOME` | não definida |
| `HOMEDRIVE` / `HOMEPATH` | `C:` / `\Users\<USUARIO>` |
| `ANDROID_USER_HOME` / `ANDROID_SDK_HOME` | não definidas inicialmente |
| `ANDROID_HOME` | `<ANDROID_SDK>` |
| ADB | `<ANDROID_SDK>\platform-tools\adb.exe`, executável encontrado |
| `.android` | `<ANDROID_USER_HOME>` existe; leitura de sua ACL foi permitida e inclui `<GRUPO_SANDBOX>: ReadAndExecute, Synchronize`, além de SYSTEM/Administradores/usuário com FullControl |
| Perfil `<DIRETORIO_USUARIO>` | consulta de ACL negada pela sandbox; sem alteração de ACL |

Testes: `adb.exe devices -l` pelo caminho absoluto falhou com `Cannot mkdir '\\.android': Permission denied`. Repetição com `ANDROID_USER_HOME=<ANDROID_USER_HOME>` definida somente no processo falhou da mesma forma. Consultas `-s <SERIAL_DISPOSITIVO>` para modelo, API e `dumpsys package com.vendassimples.app` também falharam antes de executar o comando remoto. Nenhum comando de escrita ao dispositivo foi executado.

**Causa provável:** diferença entre o contexto do PowerShell interativo e o processo Codex/sandbox. Apesar de o diretório existir e ser legível, o processo ADB tenta criar/usar `\\.android` (raiz do drive) e a sandbox nega a operação. Isso aponta para resolução de diretório de usuário/configuração de processo ou isolamento da integração, mas a causa interna exata não pode ser confirmada pela sessão. `ANDROID_USER_HOME` apontando para o diretório normal não corrigiu. Não se alteraram variáveis globais, registro ou ACL; não se tentou conceder acesso elevado nem contornar o sandbox.

### Procedimento híbrido manual

O usuário pode executar no PowerShell interativo já funcional e compartilhar somente as saídas sanitizadas:

```powershell
$adb = '<ANDROID_SDK>\platform-tools\adb.exe'
& $adb devices -l
& $adb -s <SERIAL_DISPOSITIVO> shell getprop ro.product.model
& $adb -s <SERIAL_DISPOSITIVO> shell getprop ro.build.version.release
& $adb -s <SERIAL_DISPOSITIVO> shell getprop ro.build.version.sdk
& $adb -s <SERIAL_DISPOSITIVO> shell pm path --user 0 com.vendassimples.app
& $adb -s <SERIAL_DISPOSITIVO> shell dumpsys package com.vendassimples.app | Select-String 'versionCode=|versionName=|installerPackageName'
```

Esses comandos são de leitura. Antes de compartilhar, remover serial/identificadores que não sejam necessários. Não coletar banco ou `logcat` abrangente. Em seguida, o usuário pode observar manualmente abertura, navegação, retorno e persistência, sem editar registros. Informar apenas resultado PASSOU/FALHOU e estado da licença (ativo/inativo/desconhecido), sem dados comerciais reais. O Codex registra os resultados como fornecidos pelo usuário, não como testes executados pelo Codex. Os testes que exigem conexão, exportação, câmera, escrita de fixture ou Billing ficam pendentes até autorização específica.

**Continuidade da Fase 6B:** enviar as saídas ADB sanitizadas e a confirmação manual básica. Se necessário, a investigação do contexto do Codex deve ser encaminhada ao administrador da integração; não modificar permissões/configurações Windows para contornar o sandbox. Até isso ocorrer, a homologação física permanece **BLOQUEADA / NÃO CONCLUÍDA**.

## 12. Adendo posterior — homologação manual V11 informada pelo usuário

Em solicitação posterior da Fase 6C, o usuário informou que executou duas rodadas manuais no Samsung Galaxy S22+ (SM-S906E), Android 16/API 36, com Vendas Simples instalado pela Google Play em `versionCode 11` / `versionName 1.0.11`. O usuário informou que o ADB funciona no PowerShell interativo dele, embora continue bloqueado no ambiente isolado do Codex conforme seção 11.

**Resultado relatado: 13/13 verificações aprovadas.** Esta informação é evidência manual fornecida pelo usuário. O Codex não controlou a interface, não recebeu capturas/logs e não observou os resultados diretamente. Nenhum resultado adicional é inferido.

| # | Verificação | Resultado e limite |
|---|---|---|
| 1 | Aplicativo abre sem travamentos | APROVADO, informado pelo usuário |
| 2 | Navegação entre clientes, vendas e resumo financeiro | APROVADO, informado pelo usuário |
| 3 | Clientes e vendas existentes preservados | APROVADO, informado pelo usuário; nenhum dado real foi copiado para o relatório |
| 4 | Valores pagos e a receber corretos | APROVADO, informado pelo usuário; valores individuais não foram registrados |
| 5 | Persistência após fechar e reabrir | APROVADO, informado pelo usuário |
| 6 | Visualização de fotos existentes, quando aplicável | APROVADO, informado pelo usuário |
| 7 | Nome personalizado do negócio exibido corretamente, quando configurado | APROVADO, informado pelo usuário |
| 8 | PDF | APROVADO, informado pelo usuário; conteúdo real não coletado |
| 9 | CSV | APROVADO, informado pelo usuário; arquivo não coletado pelo Codex |
| 10 | WhatsApp sem enviar cobrança real | APROVADO, informado pelo usuário; nenhum destinatário/mensagem foi registrado |
| 11 | Funcionamento offline | APROVADO, informado pelo usuário; estado e duração do teste não detalhados |
| 12 | Câmera e galeria | APROVADO, informado pelo usuário |
| 13 | Reinício do celular com preservação do funcionamento | APROVADO, informado pelo usuário |

Este adendo substitui, quanto a esses 13 cenários, a pendência de participação manual descrita nas seções 4–6 na situação atual. As consultas ADB pelo Codex continuam bloqueadas. A rodada não valida V12, API 24, compra/restauração Billing, licença FULL_ACCESS, backup/restauração, nem atualização V11 → V12; não foram listados como parte dos 13 testes.
