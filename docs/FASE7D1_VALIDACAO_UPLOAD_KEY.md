# Fase 7D.1 — Validação da chave de upload

**Data:** 16/10/2026
**Repositório:** `rg-carraro/vendas-simples-android`
**Branch:** `fix/v12-seguranca-conformidade`
**Versão de trabalho:** `12 / 1.0.12`

## Resultado

**VALIDAÇÃO PENDENTE para confirmação dos dois fingerprints informados.** O SHA-256 do certificado local selecionado pelo alias configurado no Gradle coincide exatamente com o SHA-256 informado pelo usuário como certificado da chave de upload da Play Console. O SHA-1 local, porém, difere em um caractere do SHA-1 informado. Como a Console não foi acessada diretamente nesta execução, é necessário conferir a transcrição de SHA-1 na Console antes de registrar correspondência exata pelos dois valores.

O SHA-256 correspondente é evidência forte de que o certificado é o mesmo; não encontrei sinal de outro keystore/alias selecionado. O SHA-1 divergente não permite afirmar que ambos os valores copiados da Console estejam exatos. A hipótese de erro de transcrição é apenas uma hipótese, não um resultado confirmado.

## Configuração release inspecionada

`app/build.gradle` declara `signingConfig signingConfigs.release`. O Gradle carrega as propriedades de `VENDAS_KEYSTORE_PROPERTIES`; se essa variável não estiver definida, usa `user.home/keystore.properties`. São requeridos `storeFile`, `storePassword`, `keyAlias` e `keyPassword`.

Nesta execução, `VENDAS_KEYSTORE_PROPERTIES` não estava definida no processo Codex e o fallback do JVM isolado não encontrou a configuração. Para validar a chave associada à configuração externa registrada na Fase 7C, li o arquivo local de propriedades já usado para assinatura, sem exibir caminho, alias, senha ou conteúdo; confirmei os campos requeridos e que o arquivo de keystore existe. `keytool -list -v` foi executado com senha passada por variável de ambiente temporária do processo. Retornou exit code 0 para o alias configurado, identificou uma entrada `PrivateKeyEntry` e imprimiu o certificado público. Senha e variável temporária foram descartadas da sessão; nenhuma configuração ou keystore foi alterada.

Como o processo atual não herda `VENDAS_KEYSTORE_PROPERTIES`, a futura execução Gradle deverá apontar explicitamente para a configuração local correta ou iniciar num ambiente cujo `user.home` a encontre. Isso não altera a identidade do certificado que foi lido.

## Comparação dos fingerprints

| Impressão | Informada pelo usuário da Play Console | Certificado local do alias release | Comparação |
|---|---|---|---|
| SHA-256 | `CD:90:9D:AE:E8:16:B2:7F:4E:32:13:52:73:62:25:80:BE:F0:73:F1:8A:8D:EB:56:04:F5:24:90:9C:63:C6:9E` | `CD:90:9D:AE:E8:16:B2:7F:4E:32:13:52:73:62:25:80:BE:F0:73:F1:8A:8D:EB:56:04:F5:24:90:9C:63:C6:9E` | **Igual** |
| SHA-1 | `FE:B9:79:CD:C1:35:B4:EE:FA:80:35:8D:70:6F:9F:35:40:0F:34:AE` | `FE:B9:79:CD:C1:3F:B4:EE:FA:80:35:8D:70:6F:9F:35:40:0F:34:AE` | **Diferente** (`35` informado; local `3F`) |

Como conferência independente do keystore, o certificado do AAB preexistente `vendas-simples-v11.aab` também apresentou SHA-256 e SHA-1 iguais aos valores locais da tabela. Esse arquivo é um artefato anterior, não foi gerado nesta fase e não substitui a confirmação do valor visível na Console.

## Próxima conferência necessária

Na Google Play Console, abrir **Integridade do app → Assinatura do app → Certificado da chave de upload** e copiar novamente somente o SHA-1. Verificar se o grupo em questão é `3F` (igual ao certificado local) ou `35` (valor informado inicialmente). Não usar o fingerprint do certificado da chave de assinatura do app gerenciada pelo Google nem o certificado Android Debug. Se a Console confirmar `3F`, SHA-1 e SHA-256 corresponderão e a chave poderá ser classificada como validada. Se confirmar `35`, encaminhar a evidência para investigação antes de assinar qualquer bundle; não trocar keystore/alias nem redefinir chave por inferência.

## Estado e restrições

O `versionCode` permanece 12 e o `versionName` 1.0.12. Não gerei AAB, não alterei código funcional, assinatura, keystore ou versão; não executei commit, push, merge, rebase, tag ou publicação. O estado Git anterior continua sujo com alterações locais preservadas; esta fase adiciona apenas este relatório. Permanecem pendentes, além de confirmar o SHA-1 da Console, a disponibilidade do código 12, a reconciliação Git documentada na Fase 7D, Data safety/política e homologação runtime/Alpha.

## Evidência e limite

Verificações executadas: leitura de `AGENTS.md`, relatórios 7C/7D e `app/build.gradle`; `git status`, branch, `HEAD` e tag V11; `keytool -list -v` sobre o alias configurado; `keytool -printcert -jarfile` sobre o AAB preexistente; comparação textual normalizada dos fingerprints. A informação de Console foi fornecida pelo usuário e não foi consultada diretamente. Não houve geração de artefato.
