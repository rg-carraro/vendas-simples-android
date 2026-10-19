# Fase 9F — Encerramento Data Safety e preparação Alpha V12

**Data:** 18/10/2026
**Branch documental:** `docs/fase9f-conhecimento-google-play`
**Base:** `master`, commit `11214de6aac8bf1072c1767344c725939d3b1802`
**Versão candidata:** `com.vendassimples.app`, `versionCode 12`, `versionName 1.0.12`
**Escopo:** documentação e auditoria estática/read-only; sem alteração funcional ou operação na Play Console.

## 1. Resultado

**DATA SAFETY: FUNDAMENTADA PELA ORIENTAÇÃO OFICIAL E PELA IMPLEMENTAÇÃO VERIFICADA.**

A orientação complementar do Google Play Developer Support, conforme relatada pelo proprietário após descrever os fluxos Billing do app, confirma que o processamento local da compra/licença e a telemetria estritamente pertencente à infraestrutura Play Billing não exigem categorias adicionais para o cenário descrito. A leitura estática do V12 corresponde a esse cenário: Billing é usado para consultar produto/compras, validar a licença e reconhecer a compra pelo próprio serviço; não foi identificado backend do desenvolvedor, SDK independente de analytics/anúncios/crash reporting ou transmissão automática dos registros comerciais.

Esta conclusão é condicional à arquitetura auditada e não constitui aprovação prévia do aplicativo. O suporte disse que não faz pré-verificação; a análise formal ocorre depois da submissão. A declaração na Console não foi aberta, salva ou alterada nesta fase. A responsabilidade pela exatidão continua com o desenvolvedor.

## 2. Evidência e limites

Foram lidos `AGENTS.md`, `README.md`, `docs/DECISOES.md`, as skills de segurança, monetização e release, as Fases 6C, 7I, 7J e os relatórios 9A–9E. As cópias dos relatórios 9A–9E nesta branch vieram do worktree `master-post-merge`; arquivos originais permaneceram intactos. A cópia da Fase 9B teve um caminho absoluto local de AAB removido; os demais dados de conteúdo e hash foram mantidos. Não foram encontrados e-mails pessoais, tokens concretos, chaves ou credenciais nas cópias.

### Implementação

- Dados de clientes, vendas, parcelas, pagamentos, fotos e nome do negócio ficam no armazenamento local do aplicativo. Não foi identificado backend próprio.
- `PlayBillingGateway.kt` consulta `ProductDetails`/preço e compras, lê estado/produto e usa `purchaseToken` para `acknowledgePurchase`. A API Billing envia esse token ao Google Play para reconhecimento; não foi encontrado envio a backend do desenvolvedor ou destino independente, nem persistência própria/backup do token. A orientação complementar do suporte aborda explicitamente o fluxo informado.
- Não foi encontrado código de acesso a cartão, credenciais de pagamento ou endereço de cobrança.
- O inventário de dependências inclui Billing 9.1.0 e Data Transport/CCT. O suporte esclareceu a telemetria estritamente de infraestrutura Play Billing; não há evidência de uso separado desses componentes para uma finalidade independente.
- O backup interno é criado no armazenamento privado do app; exportação/restauração por arquivo usa seletores do sistema e “Compartilhar backup” entrega uma cópia ao destino escolhido pelo usuário. CSV, PDF e WhatsApp também partem de ação do usuário. Não foi identificado envio automático dos registros comerciais. A política local descreve esses fluxos e eventos técnicos de Billing de modo consistente com a orientação recebida.
- Fontes públicas oficiais confirmam que processamento exclusivamente local não é coleta e que dados transmitidos por SDKs precisam ser avaliados; o esclarecimento específico do suporte é aplicado somente ao uso Play Billing descrito neste produto.

### V8 e V11

O proprietário confirmou visualmente V8 em teste interno, V11 em teste fechado Alpha e V12 ainda não enviada. Os artefatos completos V8/V11 não estavam disponíveis para nova comparação integral. Portanto, não se afirma identidade de código ou dependências; nos registros disponíveis não foi encontrada divergência concreta que invalide a orientação. A ficha global continua relevante porque há versão em teste fechado; confirme tracks e códigos na Console antes de submeter.

## 3. Duas orientações do suporte

O documento [Conhecimento Google Play — Data Safety e Billing](CONHECIMENTO_GOOGLE_PLAY_DATA_SAFETY.md) preserva separadamente a primeira resposta e o esclarecimento complementar, as respectivas condições e limitações. A correspondência original, datas, e-mail e ID do chamado não foram fornecidos e não foram inventados ou registrados. A primeira resposta não foi tratada isoladamente como decisão final; o encerramento considera o esclarecimento posterior que recebeu a descrição de Billing, token e CCT.

## 4. Integridade do AAB

AAB preservado em `integration-v12-master-validation/app/build/outputs/bundle/release/app-release.aab`, verificado por leitura, sem rebuild. Metadados e assinatura referem-se à validação anterior registrada na [Fase 7I](FASE7I_AAB_V12.md); o checklist Alpha original está na [Fase 7J](FASE7J_PREPARACAO_ALPHA_V12.md):

| Verificação | Resultado |
|---|---|
| Tamanho | `5.361.969 bytes` — confere |
| SHA-256 | `4CF6052542FA842EC0796D16006A2912BC825039366C20AE7EB504A608CFA75D` — confere |
| Metadados e assinatura | Consulta às evidências da Fase 7I; sem nova extração nesta fase |

Nenhum arquivo AAB foi modificado ou reconstruído. A validação criptográfica/metadados é a registrada na Fase 7I; o hash/tamanho foram recalculados nesta fase. O artefato local não prova upload ou aprovação na Play.

## 5. Checklist operacional Alpha — não executado

1. Abrir a Google Play Console e selecionar Vendas Simples (`com.vendassimples.app`).
2. Conferir status da ficha, versionCodes ativos, faixa fechada Alpha e disponibilidade do code 12.
3. Criar uma versão na faixa Alpha e selecionar somente o AAB acima; conferir package ID, `12 / 1.0.12`, assinatura e hash/origem local.
4. Inserir notas em português: “Melhorias na validação e transferência de backups, com ajustes para proteger seus dados durante a restauração. Também aprimoramos a estabilidade e a compatibilidade do aplicativo.” Revisar se ainda corresponde à versão apresentada.
5. Analisar alertas, requisitos, países e lista de testadores; interromper diante de erro ou divergência.
6. Salvar como rascunho, se a Console oferecer essa opção. **Salvar rascunho não envia para análise nem disponibiliza para testadores.**
7. Revisar a declaração Data Safety e política já registrada; enviar à análise somente por ação própria na Console. A revisão pode levar até sete dias segundo o relato do suporte, sem garantia.
8. Após aprovação/estado elegível, revisar o controle de rollout e a audiência; a versão só fica disponível aos testadores quando a etapa de liberação da faixa for concluída. Produção é uma operação separada.

Upload, salvamento de rascunho, envio à análise, liberação aos testadores e publicação em produção são decisões/etapas distintas. Nenhuma foi executada nesta fase.

## 6. Homologação física segura após liberação

No Samsung Galaxy S22+ com V11 da Play e dados reais, instalar V12 **como atualização pela Play**, sem desinstalar, limpar dados ou aplicar APK debug. Comparar antes/depois clientes, vendas, parcelas, pagamentos parciais, fotos, saldos e nome do negócio; validar PDF, CSV, compartilhamento WhatsApp, Billing e restauração da licença; observar também modo offline e persistência após reabrir/reiniciar. Não realizar restauração de backup destrutiva com dados reais.

Executar backup/restauração e arquivos inválidos apenas com dados sintéticos em ambiente descartável. Testar API 24 nesse ambiente; compra deve usar conta de licença de teste/sandbox, sem compra real. Registrar dispositivo, Android/API, versão, cenários, resultado e evidência sanitizada. A execução física continua pendente.

## 7. Alterações desta fase e verificações

- Criado `docs/CONHECIMENTO_GOOGLE_PLAY_DATA_SAFETY.md` como referência duradoura para alterações futuras de Billing, SDKs, transmissão, armazenamento e backup.
- Criadas cópias documentais das Fases 9A–9E; removido da cópia 9B apenas o caminho absoluto do usuário.
- Atualizados `AGENTS.md`, as skills de segurança, monetização e release e `docs/INDEX.md`.
- Criado este relatório.
- Conferência de links internos relativos e links oficiais listados no documento de conhecimento; auditoria direcionada de caminhos pessoais/segredos; `git diff --check`; comparação dos arquivos funcionais com a master-base.

Não houve alteração em Kotlin, Gradle, `AndroidManifest.xml`, recursos funcionais, versão, assinatura ou AAB. Não houve acesso de escrita à Console, upload, envio para revisão, rollout, build, compra, teste físico, commit, push, merge ou publicação.

## 8. Estado Git e próximos passos

Branch documental criada a partir da master confirmada. A árvore desta branch contém somente documentação/instruções. Os relatórios locais 9A–9E originais e as worktrees anteriores permanecem preservados. Nenhum commit foi criado.

**Próxima ação:** revisar esta documentação e, com autorização separada, publicá-la no GitHub. A operação Alpha é independente: após o responsável conferir a Console e autorizar explicitamente, usar o AAB cujo hash foi revalidado, primeiro como rascunho; submissão à análise e disponibilidade aos testadores exigem ações separadas.
