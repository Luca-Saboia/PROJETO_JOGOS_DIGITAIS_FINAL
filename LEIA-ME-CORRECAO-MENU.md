# Flappy Game - projeto com dificuldades

## Como abrir
1. Extraia esta pasta inteira para um local novo (nao dentro da pasta antiga do projeto).
2. Abra no IntelliJ IDEA a pasta que contem `settings.gradle`, `build.gradle`, `core/` e `lwjgl3/`.
3. Aguarde o Gradle importar o projeto.
4. Execute `Lwjgl3Launcher` em `lwjgl3/src/main/java/br/mackenzie/lwjgl3/Lwjgl3Launcher.java`.

## Como confirmar que esta rodando o codigo atualizado
Ao iniciar, a janela deve mostrar `FLAPPY GAME` e tres botoes: `FACIL`, `MEDIO` e `DIFICIL`.
No console da aplicacao deve aparecer a mensagem `Iniciando na tela de selecao de dificuldade (MENU)`.

Se a partida iniciar diretamente, confira se a configuracao de execucao usa o `Lwjgl3Launcher` deste projeto e se a classe importada e `br.mackenzie.Main`. Evite executar uma configuracao antiga de outra pasta.

A barra de espaco continua sendo usada para pular.
