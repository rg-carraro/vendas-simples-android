# Fase 7D.2 — Chave de upload e reconciliação Git

**Data:** 16/10/2026
**Repositório:** `rg-carraro/vendas-simples-android`
**Branch de trabalho:** `fix/v12-seguranca-conformidade`
**Versão preparada:** `versionCode 12` / `versionName 1.0.12`

## 1. Resumo executivo

O certificado público extraído da chave local configurada para assinatura release tem SHA-256 idêntico ao informado pelo usuário como certificado da chave de upload da Google Play. O SHA-1 local, porém, contém `3F` onde a evidência transcrita da Console contém `35`. Como a validação solicitada exige correspondência exata dos dois fingerprints e não houve acesso independente à tela da Console, o resultado é **VALIDAÇÃO PENDENTE**. A diferença parece compatível com erro de transcrição, mas isso não foi confirmado.

A branch local está em `8aa34881755f222f9b090691aaa6127b31b1a363`, preservando a tag `v1.0.11-rc.1`. O `origin/master` local está defasado: a referência remota consultada somente para leitura apontou para `fb59cf96392b80c63a42974c80c1b91be1657e4c`, enquanto a referência local aponta para `99f41e70984d1aa25cc9eca89cc5c6067f55f47b`. Há trabalho local não commitado que foi preservado. Não foram feitos fetch, merge, commit, tag, build ou publicação.

**Classificação geral: APTO COM RESSALVAS** para continuar a preparação controlada e resolver as pendências. Isto não significa que a chave esteja validada nem autoriza a geração do AAB V12.

## 2. Estado Git inicial

- Branch: `fix/v12-seguranca-conformidade`.
- `HEAD`: `8aa34881755f222f9b090691aaa6127b31b1a363`.
- `v1.0.11-rc.1` aponta para o mesmo commit de `HEAD`.
- A referência local `origin/master` estava em `99f41e70984d1aa25cc9eca89cc5c6067f55f47b`.
- A consulta GitHub somente leitura mostrou `master` em `fb59cf96392b80c63a42974c80c1b91be1657e4c`.
- O diretório de trabalho já continha 20 arquivos rastreados modificados e 26 entradas não rastreadas; nenhuma alteração estava staged. Essas alterações preexistentes foram mantidas.
- `git diff --check` não apontou erros de whitespace; exibiu avisos de normalização CRLF em arquivos preexistentes.

O inventário inclui alterações de build, manifesto, classes de backup/privacidade, testes, documentação, site e ferramentas. Há também diretórios/artefatos não rastreados, como `.codex/` e `.worktrees/`; eles não devem ser incluídos em commits sem revisão explícita. Não há arquivos de keystore, APK ou AAB rastreados pelo Git.

## 3. Certificado local da chave de upload

A configuração Gradle em `app/build.gradle` usa `VENDAS_KEYSTORE_PROPERTIES` quando definida e, como alternativa, procura `keystore.properties` sob `user.home`. As propriedades de assinatura esperadas são carregadas sem imprimir seus valores. O processo isolado atual não herdou `VENDAS_KEYSTORE_PROPERTIES`; a configuração local alternativa foi usada para a inspeção. O arquivo de propriedades e o keystore não foram alterados.

A consulta `keytool -list -v` confirmou uma entrada de chave privada e permitiu extrair apenas os fingerprints do certificado público:

- **SHA-256 local:** `CD:90:9D:AE:E8:16:B2:7F:4E:32:13:52:73:62:25:80:BE:F0:73:F1:8A:8D:EB:56:04:F5:24:90:9C:63:C6:9E`
- **SHA-1 local:** `FE:B9:79:CD:C1:3F:B4:EE:FA:80:35:8D:70:6F:9F:35:40:0F:34:AE`

Senhas, alias, caminho do keystore e conteúdo da chave privada não são registrados neste documento.

## 4. Fingerprints informados da Google Play

O usuário informou ter consultado a seção **Certificado da chave de upload** na Play Console:

- **SHA-256 informado:** `CD:90:9D:AE:E8:16:B2:7F:4E:32:13:52:73:62:25:80:BE:F0:73:F1:8A:8D:EB:56:04:F5:24:90:9C:63:C6:9E`
- **SHA-1 transcrito:** `FE:B9:79:CD:C1:35:B4:EE:FA:80:35:8D:70:6F:9F:35:40:0F:34:AE`

Esses valores são evidência fornecida pelo usuário; a tela da Console não foi acessada diretamente nesta execução. Trata-se do certificado de upload, não do certificado de assinatura gerenciada pelo Google Play.

## 5. Comparação dos certificados

| Fingerprint | Local | Play Console informado | Resultado |
|---|---|---|---|
| SHA-256 | `CD:90:9D:AE:E8:16:B2:7F:4E:32:13:52:73:62:25:80:BE:F0:73:F1:8A:8D:EB:56:04:F5:24:90:9C:63:C6:9E` | Igual | Coincide exatamente |
| SHA-1 | `FE:B9:79:CD:C1:3F:B4:EE:FA:80:35:8D:70:6F:9F:35:40:0F:34:AE` | `FE:B9:79:CD:C1:35:B4:EE:FA:80:35:8D:70:6F:9F:35:40:0F:34:AE` | Diverge em `3F` / `35` |

Os fingerprints identificam certificados de forma criptográfica; portanto, uma discrepância textual no SHA-1 deve ser reconciliada com a Console antes de declarar validação formal. A igualdade do SHA-256 é evidência forte de que se trata do mesmo certificado, mas este relatório conserva a pendência porque a entrada SHA-1 informada não coincide exatamente. **Status: VALIDAÇÃO PENDENTE**, sem indício atual de keystore ou alias divergente.

## 6. Disponibilidade do `versionCode 12`

O usuário informou que a lista de códigos de versão exibida na Play Console contém `3, 4, 5, 6, 7, 8, 10, 11`, sem `12`. Isso indica, conforme a evidência fornecida, que o `versionCode 12` está disponível. A lista não foi verificada diretamente nesta sessão. Antes do envio, confirmar novamente na Console que nenhuma faixa ou rascunho passou a reservar o código 12.

## 7. Divergência entre branch local e remoto

A referência local `origin/master` está obsoleta em relação à consulta remota. A comparação somente leitura indicou dois commits no `master` remoto após a base local `99f41e7`; a branch de trabalho tem nove commits locais além dessa base. Logo, o histórico divergiu. A consulta apontou alterações remotas em `site/privacy/index.html`; o conteúdo local correspondente foi comparado com a versão remota e estava equivalente, desconsiderando a normalização de newline.

Não se usou `git fetch`, logo a referência local `origin/master` continua desatualizada. Não foram executados merge, rebase, reset ou checkout. A atualização das referências e qualquer integração exigem autorização explícita e devem ocorrer com o trabalho local inventariado e protegido.

## 8. Plano seguro para reconciliação Git

Nenhuma etapa abaixo foi executada. Após autorização explícita para alterações Git:

1. Registrar novamente `git status --short --branch`, `git diff --stat`, `git diff --binary HEAD` e o inventário de arquivos não rastreados. Revisar o conteúdo de cada arquivo antes de decidir o que pertence à entrega. Não incluir `.codex/`, `.worktrees/`, caches, segredos, keystores, APKs ou AABs.
2. Preservar cópias verificáveis dos diffs e dos arquivos locais relevantes em local de trabalho autorizado, sem mover nem apagar os originais.
3. Separar e revisar as mudanças por finalidade; confirmar com o usuário qualquer arquivo cuja inclusão ou exclusão não esteja clara. Só então preparar commits locais aprovados, sem push.
4. Com autorização para sincronizar referências, executar `git fetch origin master` e inspecionar `git log --left-right --graph HEAD...origin/master`, `git diff --stat HEAD...origin/master` e os arquivos em conflito potencial.
5. Preferir um merge explícito de `origin/master` na branch V12, preservando ambos os históricos. Não fazer rebase nem reset. Rever especialmente `site/privacy/index.html` e documentação; manter o conteúdo público remoto equivalente e as alterações locais necessárias, sem sobrescrever silenciosamente nenhuma versão.
6. Resolver conflitos manualmente, revisar o diff completo e rodar as verificações pertinentes. Confirmar que `v1.0.11-rc.1` continua apontando ao commit original e que os arquivos locais não relacionados à entrega permanecem preservados.
7. Antes de criar qualquer artefato de release, validar novamente chave, versão, estado limpo e proveniência do commit integrado.

Riscos a controlar: inclusão acidental de artefatos ou credenciais, conflito no site/política, alteração não intencional de declarações de privacidade e perda de trabalho em arquivos não rastreados. Os pontos 3–7 requerem autorização correspondente; este relatório não a substitui.

## 9. Preservação dos AABs existentes

Os dois arquivos ignorados pelo Git existentes foram inspecionados e permaneceram intactos:

| Arquivo | Tamanho | SHA-256 |
|---|---:|---|
| `app/build/outputs/bundle/release/app-release.aab` | 5.320.483 bytes | `D751F51DC69AC2DA2784D841374288E4456BD666E2A5237C2E91C1EE9D47AABA` |
| `app/build/outputs/bundle/release/vendas-simples-v11.aab` | 5.320.483 bytes | `D751F51DC69AC2DA2784D841374288E4456BD666E2A5237C2E91C1EE9D47AABA` |

Ambos têm a mesma impressão digital e data de geração anterior a esta fase. Não são AABs V12 e não devem ser apresentados como tal. Um futuro `bundleRelease` no checkout atual pode substituir `app-release.aab`; para preservar os originais, gerar o novo bundle em worktree de build isolado, com saída separada, após autorização, e confirmar hashes antes/depois. Nenhum AAB foi gerado ou modificado nesta fase.

## 10. Pendências de conformidade

- Confirmar diretamente os dois fingerprints na seção **Certificado da chave de upload** da Console, em especial o grupo `3F`/`35` do SHA-1.
- Verificar na Console a disponibilidade do `versionCode 12` antes de enviar.
- Confirmar o formulário Data Safety, faixas/track e status efetivo da política na Play Console; essas telas não foram acessadas nesta fase.
- A análise anterior não identificou backend próprio, Analytics ou Crashlytics, mas dependências podem incluir DataTransport/CCT. Destino, conteúdo, finalidade, retenção e categorias de eventuais transmissões precisam de confirmação antes de declarações definitivas.
- O backup completo é local e não criptografado por senha própria; a licença não faz parte do backup. Manter comunicação e homologação coerentes com essa implementação.
- A política local foi comparada à fonte remota do repositório e encontrada equivalente; o acesso direto à página publicada não foi validado nesta execução.

## 11. Bloqueadores técnicos para o AAB local

1. Resolver a discrepância transcrita do SHA-1 e registrar evidência dos fingerprints da Console.
2. Consolidar a branch divergente sem perder as alterações locais e sem incluir arquivos indevidos.
3. Garantir a versão V12 (`12` / `1.0.12`) no commit final e confirmar disponibilidade na Console.
4. Gerar o AAB apenas em ambiente isolado, preservando os dois artefatos anteriores; verificar assinatura e metadados internos do bundle final.
5. Executar a regressão necessária no commit consolidado. Os testes unitários e lint da Fase 7C não cobrem essa integração futura.

O SHA-256 local já coincide com o informado pela Console e a configuração local contém uma entrada de chave privada. O bloqueio é de confirmação formal e reconciliação do estado de entrega, não evidência de chave diferente.

## 12. Bloqueadores para faixa Alpha

- AAB V12 assinado, com metadados e certificado verificados.
- Branch integrada/revisada, testes automatizados no commit candidato e instalação/upgrade homologados em dispositivo.
- Validar atualização V11 → V12 sem perda de clientes, vendas, parcelas, pagamentos, fotos e personalização, além de cenários FREE/FULL_ACCESS e Billing em ambiente de teste.
- Confirmar Data Safety e política de privacidade aplicável à versão enviada.
- O build de instrumentação compilado anteriormente não equivale a execução dos testes em dispositivo; não há evidência nesta fase de homologação física Alpha da V12.

## 13. Bloqueadores para produção

Além dos itens de Alpha:

- Aprovação da homologação Alpha e de compra/restauração de `FULL_ACCESS` com conta de teste/licença apropriada.
- Revisão final de conteúdo, política, Data Safety, listagem, países, preço e faixa de produção na Console.
- Aprovação explícita do usuário para upload/publicação. Este trabalho não autoriza qualquer ação na Play Console.

## 14. Estado Git final desta fase

Não houve operação Git mutável. A branch, `HEAD`, tag V11 e alterações locais permanecem como no início. Foi criado apenas este relatório, que está novo/não rastreado até ser incluído em uma futura alteração aprovada. Não houve build, AAB, commit, push, merge, rebase, fetch ou tag.

## 15. Próxima ação recomendada

Conferir na Console se o SHA-1 correto contém `...C1:3F:B4...` ou `...C1:35:B4...` e registrar a evidência dos fingerprints completos. Se confirmar `3F`, a correspondência SHA-256/SHA-1 fica consistente; se a Console realmente mostrar `35`, interromper a preparação do bundle e investigar a configuração de certificado sem alterar chaves. Em seguida, com autorização para operações Git, atualizar referências, inspecionar a divergência e consolidar o candidato preservando o trabalho local. Só depois da regressão consolidada e da validação da chave, solicitar/usar autorização para gerar o AAB V12 isoladamente.

### Decisão

**APTO COM RESSALVAS** para seguir com diagnóstico e preparação controlada.
**VALIDAÇÃO PENDENTE** para a chave de upload até reconciliar o SHA-1.
**AAB V12 NÃO GERADO**; não iniciar envio Alpha ou produção com base neste relatório.
