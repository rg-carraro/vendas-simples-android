# Auditoria pré-Billing

Data: 29/09/2026. Escopo: inspeção estática do APK/debug e código local antes da integração do Google Play Billing. Não substitui testes em aparelho, revisão de privacidade ou homologação da Play Store.

## Resultado

| Área | Resultado | Observação |
| --- | --- | --- |
| Identidade | Conforme | `Vendas Simples`, `com.vendassimples.app` e ícone próprio no manifesto/build |
| Rede | Conforme | Não há permissão `INTERNET`, cliente HTTP ou endpoint; WhatsApp usa Intent externo |
| Compartilhamento | Revisar antes do release | FileProvider usa somente `cache` e `files`; revisar subdiretórios e concessão de URI em cada fluxo |
| Dados | Conforme no escopo atual | SQLite local; falhas de Billing ainda não alteram dados porque Billing não está integrado |
| Limite FREE | Parcialmente implementado | Bloqueia somente nova venda ao atingir 30; entitlement LIFETIME ainda é provisório |
| Valores monetários | Bloqueador de produção | Código herdado ainda usa `REAL`/`Double`; `MoneyCents` existe para código novo, mas migração dos fluxos antigos está pendente |
| Schema | Pendente | Banco v2 da referência foi preservado; qualquer conversão monetária exige nova versão e migração com fixtures |
| UX | Parcialmente validada | Build passa; fluxos de teclado, rotação, fonte ampliada, câmera e compartilhamento precisam de teste em aparelho |
| Release | Pendente | Não há assinatura de release, produto Play, política de privacidade publicada ou Billing configurado |

## Verificações executadas

- `assembleDebug`: passou usando o JDK bundled do Android Studio (Java 21).
- APK: `app/build/outputs/apk/debug/app-debug.apk`.
- Revisão de manifesto, permissões, provider, dependências e referências textuais.
- Origem `alejoiasvendasapp`: working tree sem alterações.

## Riscos que impedem chamar de pronto para loja

1. Migrar dinheiro de `REAL`/`Double` para centavos em `Long` ou decimal seguro, com migração testada e preservação de pagamentos, fotos e IDs.
2. Integrar Google Play Billing atrás de `EntitlementRepository`, com compra única, restauração, pendência, cancelamento e falhas não destrutivas.
3. Executar cenários em aparelho: primeira venda offline, 30ª/31ª venda, pagamentos, fotos, PDF/CSV, backup, rotação, fonte ampliada e permissões.
4. Definir política de backup Android, privacidade, assinatura e configuração de release antes da Play Store.

## Próximo incremento recomendado

Separar o acesso monetário do SQLite em uma migração dedicada. Não misturar essa alteração com Billing ou redesign de UI; cada mudança deve manter um APK de debug reproduzível.
