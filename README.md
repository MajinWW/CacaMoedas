# Caça Moedas

Jogo Android offline criado para MajinWW. Arraste o coletor, pegue moedas e desvie das bombas. Tem três vidas, pausa, dificuldade progressiva e recorde salvo. Sem anúncios ou permissões sensíveis. Android 6 ou superior.

## Gerar o APK pelo GitHub

1. Envie o conteúdo desta pasta para a raiz do repositório MajinWW/CacaMoedas, incluindo a pasta oculta .github.
2. Abra Actions e escolha Gerar APK. O envio para main inicia a compilação; também é possível usar Run workflow.
3. Quando ficar verde, abra a execução e baixe CacaMoedas-APK em Artifacts.
4. Extraia o ZIP e instale app-debug.apk no celular, autorizando a instalação dessa origem.

Este é um APK de teste assinado pelo ambiente de compilação. Compilações em ambientes diferentes podem usar chaves diferentes; nesse caso, desinstale a versão antiga antes de instalar (o recorde será apagado).

## Compilar no computador

Use Java 17, Android SDK 35 e Gradle 8.9. Rode `gradle assembleDebug` na raiz. O arquivo sai em app/build/outputs/apk/debug/app-debug.apk.

## Validação

Os arquivos XML e a estrutura do pacote foram verificados. O APK ainda precisa ser compilado e testado em um aparelho Android; o ambiente de criação não tem Java/Android SDK instalados.
