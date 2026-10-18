# Fase 9B — Conformidade Google Play e preparação Alpha V12

**Data:** 16/10/2026 (America/Sao_Paulo)
**Base revisada:** `master`, `11214de6aac8bf1072c1767344c725939d3b1802`
**Versão candidata:** `com.vendassimples.app`, versionCode `12`, versionName `1.0.12`
**Escopo:** revisão documental e estática, sem alteração de código, Console, AAB ou distribuição.

## Resultado

- **AAB — APTO TECNICAMENTE PARA UPLOAD CONTROLADO, com ressalvas de Console.** O artefato local existente foi lido e seu tamanho/hash conferem com a evidência da Fase 7I. Não foi enviado à Play e não se confirma que o versionCode esteja disponível na Console.
- **Faixa Alpha/testadores — NÃO LIBERADA nesta revisão.** Elegibilidade da faixa, testadores, versão, produto, alertas e declarações atuais da Console não foram verificados. A ficha Data Safety permanece pendente de reconciliação com as transmissões do Billing e versões distribuídas.
- **Produção — NÃO APTA ainda.** Permanecem homologação física V11→V12, API 24, Billing real de teste, revisão Data Safety e confirmação da página pública.

Essas classificações separam integridade local do arquivo, preparação da faixa e aprovação para produção. Nenhuma operação de upload, rollout, publicação ou alteração da Console foi realizada.

## Estado e verificações locais

`master` e `origin/master` estão no merge esperado `11214de6aac8bf1072c1767344c725939d3b1802`. O worktree atual já continha o relatório não commitado da Fase 9A; ele foi preservado. Este relatório também é local e não commitado. As outras worktrees e branches foram mantidas.

Configuração observada na árvore master:

| Item | Evidência local |
|---|---|
| Application ID e versão | `com.vendassimples.app`, 12 / 1.0.12 (`app/build.gradle`) |
| Compatibilidade declarada | minSdk 24, targetSdk 36 (`app/build.gradle`) |
| Billing | Google Play Billing 9.1.0 (`app/build.gradle`) |
| Produto | `full_access`, compra única INAPP (`BillingDomain.kt`; comportamento também descrito em `MONETIZACAO.md`) |
| FREE | Até 30 vendas; bloqueio da 31ª (`DomainRules.kt`) |
| Política no app | URL oficial em `PrivacyPolicy.kt`; visualização do asset local em `MainActivity.kt` |
| Dados comerciais | Armazenamento local; sem backend próprio identificado na revisão documentada da Fase 6C |
| Entitlement | Recuperado pelo Billing; não importado do backup SQLite (`BackupManager`/documentação de segurança) |

Não foram executados testes ou builds de aplicativo nesta fase documental. Resultados anteriores na mesma árvore estão descritos na Fase 9A, mas não são tratados como execução desta revisão.

## AAB existente

Arquivo preservado, produzido no worktree de integração V12:

`[caminho local do AAB omitido; hash preservado]`

- Tamanho observado nesta revisão: **5.361.969 bytes**.
- SHA-256 observado nesta revisão: **`4CF6052542FA842EC0796D16006A2912BC825039366C20AE7EB504A608CFA75D`**.
- Corresponde ao hash registrado na [Fase 7I](FASE7I_AAB_V12.md).
- A Fase 7I contém as evidências anteriores de metadados e assinatura; esta etapa só conferiu tamanho e hash. Não reconstruiu, copiou, assinou, abriu para extração nem sobrescreveu o AAB.

**Limite:** hash válido demonstra identidade com o artefato documentado, não aceitação pela Play, disponibilidade do versionCode, configuração da faixa, aprovação de política ou aptidão de produção.

## Política de privacidade

O app aponta para a URL oficial informada pelo proprietário: <https://rg-carraro.github.io/vendas-simples-android/privacy/>. O arquivo `site/privacy/index.html` e `app/src/main/assets/privacy_policy.html` foram comparados localmente: o conteúdo dentro do `<main>` do site é idêntico ao asset offline, incluindo revisão de **15 de outubro de 2026**. A cópia local continua acessível pelo fluxo no aplicativo sem depender de rede.

A política descreve dados comerciais locais, ausência de backend próprio para esses dados, exportação/backup iniciados pelo usuário, ausência de criptografia/senha própria no arquivo SQLite, limites de cópias externas, consultas Billing e metadados técnicos potencialmente transmitidos pelo transporte do Billing. Não se afirma ausência total de tratamento por terceiros nem que os registros comerciais sejam enviados pelo desenvolvedor.

**URL pública: NÃO VERIFICADA nesta sessão.** A ferramenta web não conseguiu abrir a URL. A igualdade local dos arquivos e a evidência histórica de uma execução Pages bem-sucedida não demonstram resposta HTTPS atual, conteúdo atualmente servido, certificado, disponibilidade ou indexação. O registro antigo de 15/10 sobre divergência remota também não substitui uma checagem atual. Solicitar ao responsável confirmação por navegador da página, data de revisão e seções visíveis antes de avançar a distribuição.

## Data Safety — fatos, lacunas e orientação

### Confirmado pelo código/documentação estática

- Clientes, vendas, parcelas, pagamentos, fotos e nome do negócio são usados localmente. Não foi identificado backend próprio, Analytics, anúncios ou Crashlytics na revisão registrada.
- CSV, PDF, backup e textos de cobrança podem sair do aparelho quando o usuário inicia uma exportação ou escolhe um destino/app. Arquivos SQLite de backup não têm criptografia/senha própria.
- O app conecta ao Google Play Billing ao abrir, consulta produto e compras e usa Billing para compra/restauração. O token de compra não é exportado no backup nem persistido pelo código próprio inspecionado.
- A investigação da Fase 6C encontrou estaticamente caminho Billing → Data Transport/CCT com metadados técnicos possíveis (modelo/fabricante, versão/build Android e app, locale/região, rede/operadora). Não houve captura de tráfego nem confirmação de todos os campos, eventos, destinatários, retenção ou vínculo.

### Não confirmado; não declarar como fato no formulário

- Respostas atualmente salvas no Data Safety, escopo de versões/faixas ativas e alertas da Console.
- Enquadramento completo dos eventos e identificadores do Billing 9.1.0, finalidades/categorias, compartilhamento, retenção e obrigatoriedade.
- Criptografia de todos os fluxos em trânsito, sobretudo destinos selecionados pelo usuário e todo o tráfego de SDK.
- Estado de exclusão exigido pela Console e aplicabilidade exata a dados de terceiros.
- Se há versão V11 ou outra versão em produção/teste, além do histórico descrito nos relatórios.

A definição oficial da Play inclui dados transmitidos por SDKs e atribui ao desenvolvedor a responsabilidade pelas respostas. Por isso, “sem backend próprio” não equivale a “nenhuma coleta”. A exceção de pagamento não deve ser estendida automaticamente a diagnósticos. Ver [orientação Data Safety](https://support.google.com/googleplay/android-developer/answer/10787469?hl=en) e [disclosure dos SDKs Google Play services](https://developers.google.com/android/guides/play-data-disclosure), que se limita aos SDKs listados e não resolve por si só o Billing. A [documentação de Billing](https://developer.android.com/google/play/billing/integrate) explica consultas/transações, mas não é inventário completo de telemetria.

**Recomendação:** não submeter nem atualizar respostas Data Safety até revisar a ficha efetivamente salva, todas as versões ainda distribuídas e obter confirmação suficiente do fornecedor/inventário de eventos do Billing. Não foi alterada nenhuma resposta da Console.

## Acesso à Play Console

Não há browser nem aplicativo conectado disponível para leitura nesta sessão (`cua.getState()` retornou nenhuma superfície). Assim, foram classificados como **NÃO VERIFICADOS**: status de políticas e Data Safety, tracks e versões ativas, disponibilidade do versionCode 12, `full_access` e testadores de licença, lista de testers, alertas/rejeições, identidade da release selecionada, notas de versão salvas e configuração do formulário. Nenhum dado da conta foi acessado e nenhuma alteração foi feita.

## Checklist do proprietário antes de qualquer Alpha

1. Na Play Console, confirmar o app `com.vendassimples.app`, eventuais avisos/bloqueios e a versão atualmente ativa em cada faixa. Verificar se o versionCode 12 ainda está disponível.
2. Abrir a ficha Data Safety salva e comparar cada categoria, finalidade, compartilhamento, criptografia e exclusão com o código, dependências Billing/CCT e todas as versões distribuídas. Não marcar “nenhuma coleta” apenas porque os dados comerciais ficam localmente.
3. Confirmar a política pública no navegador em <https://rg-carraro.github.io/vendas-simples-android/privacy/>: conexão sem erro, data 15/10/2026, conteúdo sobre Billing, backup sem criptografia e cópias externas, exclusão e contato. A política indicada na Console deve ser essa URL.
4. Confirmar produto `full_access` como compra única e revisar configuração de licença/testadores. A existência local do SKU não comprova disponibilidade ou configuração correta na Console.
5. Antes do upload futuro, conferir que selecionou exatamente o AAB com o SHA-256 acima e interromper se a Console mostrar versão, assinatura ou pacote diferentes. Upload só depois de autorização explícita.
6. Se e quando a versão for distribuída a testadores, seguir o fluxo de opt-in e registrar a versão recebida; um upload em rascunho não significa que o teste esteja disponível.

## Homologação física pendente

Com o Galaxy S22+ que contém a V11 da Play: preservar instalação e dados; após autorização e disponibilização de uma faixa de teste, atualizar pela Play e validar clientes, vendas, parcelas, pagamentos parciais, fotos, nome, saldo, PDF/CSV/WhatsApp, licença e operação offline. Não substituir essa instalação por APK debug nem apagar dados.

Para API 24, usar aparelho/emulador e dados sintéticos. Cobrir instalação, inicialização, cadastro/venda/parcelas/pagamentos, PDFs/CSV/compartilhamento, offline, FREE 30 e Billing em ambiente de teste. Para backup, usar instalação descartável: FREE bloqueado, FULL_ACCESS, backup legado, restauração com fotos/nome, arquivo inválido e consistência após reinício. Os testes físicos e Billing real de teste não foram executados por esta revisão.

## Fontes e limites

- Evidência local: `app/build.gradle`, `DomainRules.kt`, `BillingDomain.kt`, `PrivacyPolicy.kt`, `MainActivity.kt`, `site/privacy/index.html`, `app/src/main/assets/privacy_policy.html`, `docs/SEGURANCA_DADOS_PLAY.md`, `docs/FASE6C_SEGURANCA_DADOS_PLAY.md`, `docs/FASE7I_AAB_V12.md`, `docs/FASE7J_PREPARACAO_ALPHA_V12.md` e `docs/FASE9A_POS_MERGE_PREPARACAO_ALPHA.md`.
- O relatório da Fase 9A registra testes unitários e lint executados anteriormente no commit de conteúdo funcional equivalente; não se repetiram nesta fase.
- Fontes externas consultadas: [Data Safety](https://support.google.com/googleplay/android-developer/answer/10787469?hl=en), [tracks de teste](https://support.google.com/googleplay/android-developer/answer/9845334?hl=en), [Billing](https://developer.android.com/google/play/billing/integrate), [notas Billing](https://developer.android.com/google/play/billing/release-notes), [disclosure de Play services](https://developers.google.com/android/guides/play-data-disclosure).
- A política HTTPS, a Console, o formulário, testadores e a aceitação Play dependem de verificação externa pelo responsável; resultados locais não provam esses estados.

## Estado final

**AAB APTO TECNICAMENTE PARA UPLOAD CONTROLADO, condicionado à revisão da Console; ALPHA NÃO LIBERADA; PRODUÇÃO PENDENTE.** URL pública e Play Console ficam **NÃO VERIFICADAS** nesta sessão. Nenhum código, versão, assinatura ou configuração Play foi alterado; não houve build, upload, rollout, commit ou push.
