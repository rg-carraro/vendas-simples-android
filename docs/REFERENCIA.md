# Referência e procedência

Inspeção em 29/09/2026, somente leitura.

| Item | Referência |
| --- | --- |
| Repositório | `alejoiasvendasapp` |
| Local inspecionado | `C:/Users/rgcar/git/alejoiasvendasapp` |
| Branch local | `comercial` |
| Commit fixado | `ce112c40c4f79368042555e451338b5f318077bd` |
| Estado inicial | Working tree limpa; branch comercial acompanhando origin/comercial |
| Destino | `vendas-simples-android`, inicialmente vazio e sem commits |

Não houve consulta ao remoto para comprovar se o commit local é o mais recente. A referência é o snapshot acima, não a master nem arquivos não versionados.

## Evidências consultadas

Os caminhos desta tabela pertencem ao commit da origem, não ao novo repositório.

| Caminho na origem | Constatação |
| --- | --- |
| `docs/COMERCIAL.md`, `docs/ESTRATEGIA_COMERCIAL.md` | Produto genérico local, azul/cinza; ausência de integração com planilha; restauração pela UI inexistente |
| `app/build.gradle` | Kotlin/Android, applicationId `com.vendassimples.app`, namespace legado, SDKs 23/35/35 |
| `build.gradle`, `gradle/wrapper/gradle-wrapper.properties` | AGP 8.5.2, Kotlin 1.9.24, Gradle 8.7 |
| `app/src/main/AndroidManifest.xml` | Nome Vendas Simples, FileProvider derivado do applicationId, sem permissão INTERNET; `allowBackup=true` |
| `app/src/main/java/com/example/controlevendas/Models.kt` | Modelos de vendas/pagamentos; valores e id_venda no relatório usam Double |
| `app/src/main/java/com/example/controlevendas/LocalDatabase.kt` | SQLite, funções de venda/pagamento/fotos e tabelas legadas de sync |
| `app/src/main/java/com/example/controlevendas/MainActivity.kt` | Telas, relatórios, PDFs, CSV, backup, WhatsApp e rotinas de notificação concentrados na Activity |
| `docs/BANCO_DE_DADOS.md`, `docs/FUNCIONALIDADES.md` | Relações de IDs, fotos por venda pai e comportamento esperado dos fluxos |
| `docs/INTERFACE_VISUAL.md`, `docs/CHECKLIST_TESTES.md` | Navegação e cenários de regressão; testes em telefone ainda pendentes |
| `AGENTS.md` | Regras específicas da origem, inclusive autorização de push que não se transfere para cá |

Foi feita inspeção documental e de trechos do código, não auditoria completa nem execução do aplicativo. Builds registrados na origem são históricos, não resultados desta sessão.

## Como consultar sem modificar

```powershell
git -C ../alejoiasvendasapp rev-parse comercial
git -C ../alejoiasvendasapp ls-tree -r --name-only ce112c40c4f79368042555e451338b5f318077bd
git -C ../alejoiasvendasapp show ce112c40c4f79368042555e451338b5f318077bd:app/build.gradle
git --no-optional-locks -C ../alejoiasvendasapp status --short --branch
```

Em outra máquina, ajuste apenas o caminho de leitura. Se a referência não estiver disponível, registre a limitação; não substitua silenciosamente pela master. Para atualizar o snapshot, registre novo hash, motivo e diferenças relevantes.

## Reaproveitamento futuro

Avaliar código Android, recursos genéricos e configuração Gradle por arquivo. Reescrever documentação e regras locais para o novo contexto. Não importar `.git`, builds, configurações da máquina, bancos, segredos nem permissões operacionais da origem. Verificar os direitos de reutilização dos recursos antes de distribuição; esta inspeção não os estabeleceu.

O identificador comercial já existe na referência: reutilizá-lo pode atualizar/substituir uma instalação comercial anterior, dependendo da assinatura. Independência do AleJoias original não significa coexistência com o APK comercial. Essa decisão precisa ser explícita antes do primeiro APK.

## Conversa indicada

O usuário forneceu [esta conversa](https://chatgpt.com/c/6abbf28f-1cd4-83e9-8449-facaf3336aa4). A tentativa de leitura não retornou seu conteúdo. Nenhuma decisão foi atribuída a ela; incorporar futuramente trechos fornecidos pelo usuário e reconciliar com as propostas atuais.
