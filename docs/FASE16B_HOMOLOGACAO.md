# Fase 16B — homologação de Light Mode e Dark Mode

**Data:** 23/10/2026
**Branch/worktree:** `feature/fase16-light-dark-mode`, `.worktrees/fase16-light-dark-mode`
**Base:** V14, `dfb87e6ddb294834bde8f6ae044b4a1d58ed7525` (`versionCode 14`, `versionName 1.0.14`).
**Dispositivo:** Samsung Galaxy S22 SM-S906E, Android 16/API 36, 1080×2340, 450 dpi.
**Segurança:** o app de debug usa `com.vendassimples.app.fase16`, e o runner aponta exclusivamente para esse pacote. O pacote Play `com.vendassimples.app` continuou na versão 14; ADB confirmou o último update de 21/10/2026. Foram usados fixtures sintéticos. Nenhum dado real, compra ou envio de compartilhamento foi manipulado.

**Adendo Fase 16C:** a linha da suíte de backup e a matriz Light/Dark abaixo foram atualizadas após correção controlada do cleanup temporário. O APK debug foi mantido na worktree `.worktrees/fase16-light-dark-mode/dist/debug/VendasSimples_Fase16_LightDark_Debug.apk`, fora do Git; veja o relatório [Fase 16C — homologação final](FASE16C_HOMOLOGACAO_FINAL.md) para hash, instalação e estado.

**Adendo Fase 16D:** o proprietário relata que concluiu seus testes manuais no S22 e que todos os cenários que executou passaram. Essa aprovação é relatada pelo proprietário; a lista detalhada por fluxo não foi fornecida. A feature foi integrada à `master` pela PR #7, sem publicação na Play Store.

## Matriz de validação

| Cenário | Resultado | Evidência/limite |
|---|---|---|
| Unit tests debug | **Aprovado** | 46/46. |
| Unit tests release | **Aprovado** | 46/46. |
| Compilação Kotlin instrumentada | **Aprovado** | `:app:compileDebugAndroidTestKotlin`. |
| Lint debug e release | **Aprovado** | Ambos sem erros; avisos existentes registrados em `FASE16_LIGHT_DARK_MODE.md`. |
| APK debug e APK release | **Aprovado** | `:app:assembleDebug` e `:app:assembleRelease`, execução offline. Nenhum AAB gerado. |
| Contraste e componentes semânticos | **Aprovado** | 19 testes instrumentados em cada combinação claro/escuro e fonte 1,0/1,3: 76/76 execuções. Inclui diálogo Material e estado desabilitado; o corpo do diálogo tinha 4,08:1 com opacidade padrão e passou a usar token secundário opaco. |
| Venda/pagamentos sintéticos e finanças | **Aprovado** | Testes instrumentados exercitaram filtros/ordenação/estados, atualização de pagamento e integridade financeira, mês/totais/gráfico. Cobertura de regra/controle, não percurso completo dos formulários. |
| Fotos/PDF sintéticos | **Aprovado parcialmente** | Testes adicionam/removem foto temporária pequena e geram PDF sintético; não houve inspeção integral do PDF nem uso da galeria/câmera do aparelho. |
| Preferência e recriação de Activity | **Aprovado** | Persistência de preferência e teste em nova Activity. |
| Rejeição/isolamento de backups | **Aprovado** | Após incluir a limpeza do sibling `.corrupt`, o caso específico passou e a suíte completa passou 38/38, com armazenamento sintético isolado e verificações de preservação. A falha era preexistente na base V14; formato/schema e fluxo de restauração bem-sucedida não mudaram. |
| Inspeção inicial do app e diálogo “Nome do negócio” | **Aprovado** | Inspeção em S22 claro/escuro. Menu e placeholder foram revistos com fonte 1,3 e o truncamento observado inicialmente foi corrigido. |
| Todos os formulários e diálogos comerciais | **Não executado** | Pendem CRUD visual de cliente/venda, Detalhes da Venda, Registrar Pagamento, PDF do Card/Fechar e confirmação/erro em percurso integral. |
| Clientes, relatórios e licença | **Não executado integralmente** | Alguns componentes/identidade e cálculo são cobertos em teste; telas e percurso manual completo pendem. Nenhuma compra real ou operação de licença foi feita. |
| Backup/restauração pela interface SAF | **Não executado** | Apenas suíte automatizada isolada; nenhum backup/restauração com dados do proprietário. A suíte contém as falhas descritas acima. |
| Compartilhamento/WhatsApp | **Não executado** | Nenhum envio foi feito. |
| Retrato/paisagem e outras telas/densidades | **Não executado** | Só resolução nativa retrato do S22. |
| Tema, fonte e pacotes de teste restaurados | **Aprovado** | ADB confirmou modo claro (`night: no`) e escala 1,0. O runner `.fase16.test` foi removido; o app `.fase16` permanece instalado para avaliação manual. A V14 permaneceu instalada como versionCode 14/versionName 1.0.14. |
| APK debug para testes manuais | **Gerado e instalado em paralelo** | Pacote `.fase16`, rótulo “Vendas Simples — Teste Light/Dark”; runner `.fase16.test` foi removido após os testes. APK e SHA-256 no relatório Fase 16C. |

## Recomendação e pendências

No momento da execução da Fase 16B, a homologação manual estava pendente. O proprietário posteriormente informou aprovação de todos os cenários manuais que executou no S22; a cobertura por fluxo não foi detalhada. O defeito `.corrupt` foi corrigido e passou em 38/38. A integração Git foi concluída pela PR #7, sem gerar nova versão ou publicar. Os critérios WCAG AA e os pares calculados estão no relatório [Fase 16 — implementação e validação](FASE16_LIGHT_DARK_MODE.md).

Capturas temporárias foram inspecionadas e descartadas, sem anexar imagens potencialmente contendo notificações ou dados pessoais. As telas sintéticas automatizadas não geraram arquivo de evidência de screenshot retido.

Este snapshot registra os resultados da Fase 16B. A integração foi concluída pela PR [#7](https://github.com/rg-carraro/vendas-simples-android/pull/7), merge `a742c9139950c6098b4a4960c181e97a45b48fb7`; o estado final está no [relatório 16C](FASE16C_HOMOLOGACAO_FINAL.md) e no [histórico de versões](HISTORICO_VERSOES.md). Não houve AAB ou publicação. A V14 continua 14 / 1.0.14.
