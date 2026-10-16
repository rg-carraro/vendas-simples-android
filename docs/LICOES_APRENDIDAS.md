# Lições aprendidas

Estas práticas vêm de evidências e decisões do projeto; são orientação de processo, não novas regras comerciais.

1. **Separar os identificadores.** Nome informal de versão, `versionCode`, `versionName`, tag, commit, AAB local e release Play são coisas diferentes. Registre cada uma e sua prova.
2. **Preservar a cadeia de dados.** Mudança financeira ou de schema exige migração e fixture que confira clientes, parcelas, pagamentos, fotos e IDs; não basta compilar.
3. **Modelar dinheiro pela representação real.** Confirmar que banco usa centavos antes de formatar/exportar; CSV precisa parser independente e campos especiais.
4. **Reproduzir bugs com dados sintéticos.** O caso real “tutu — brinco” não foi inspecionado; reprodução sintética permitiu corrigir o escaping sem divulgar dados pessoais.
5. **Separar código, teste e homologação.** Unit tests, AndroidTest compilado, runtime no AVD, aparelho, transação Play e declaração Console produzem tipos diferentes de evidência.
6. **Não esconder lint.** Corrigir causa por API/fluxo/estrutura; comparar warnings por identificador e contar somente resultado final da variante.
7. **Guardar artefatos e sua proveniência.** Hash, tamanho, versão, certificado e commit devem estar juntos. Release AAB e APK debug têm usos/assinaturas diferentes; não sobrescrever candidatos históricos.
8. **Tratar falha ambiental com transparência.** Lock, sandbox, ADB e cache podem bloquear ferramentas sem indicar defeito do app; registrar erro, evitar alterar ACL/global state e fornecer procedimento manual quando necessário.
9. **Distinguir backup de licença.** Arquivo de dados não deve conceder entitlement. Validar formato, conteúdo e rollback; testar dados antigos e inválidos em ambiente descartável.
10. **Conformidade depende de evidência externa.** Manifesto e inspeção estática não resolvem payload de SDK, status da Console, conteúdo servido nem faixa publicada.
11. **Ler a história antes de refatorar.** Commits pequenos e relatórios explicam por que cálculos, status, marca, permissões e limites foram preservados.
12. **Fechar o estado Git de forma revisável.** Conferir worktrees e status, stage por arquivo, revisar diff e escanear conteúdo antes do commit; não limpar artefatos alheios.

Para aplicar estas lições, consulte [Continuidade Codex](CONTINUIDADE_CODEX.md), [PROBLEMAS_E_SOLUCOES](PROBLEMAS_E_SOLUCOES.md) e skills locais.
