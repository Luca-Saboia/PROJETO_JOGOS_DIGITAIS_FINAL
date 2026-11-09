# PrimeiroJogo

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and an empty `ApplicationListener` implementation.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.

## Niveis de dificuldade

Ao iniciar, escolha **FACIL**, **MEDIO** ou **DIFICIL** no menu. Cada partida comeca com a contagem `3`, `2`, `1`, `GO!`.

- **Facil:** canos mais lentos e afastados, com abertura maior e pequenas variacoes de altura.
- **Medio:** canos um pouco mais rapidos e frequentes, com abertura um pouco menor e variacao de altura controlada.
- **Dificil:** canos mais rapidos e frequentes, com abertura menor e variacao de altura maior, mas limitada.

Durante a partida, pressione **ESPACO** ou clique/toque na tela para fazer o passaro subir. Quando a partida terminar, use **JOGAR NOVAMENTE** para reiniciar no mesmo nivel ou **MENU** para escolher outra dificuldade. O controle por Arduino usa o sketch em `arduino/Pressao.ino`; consulte `LEIA-ME-ARDUINO-SEM-PULO.md` para calibração, limiar do FSR e diagnóstico serial.

Os parametros de velocidade, frequencia dos canos, tamanho da abertura e variacao vertical ficam centralizados em `core/src/main/java/br/mackenzie/Difficulty.java`, facilitando o ajuste posterior.


Arduino: a porta serial padrao e COM4. Feche o Monitor Serial do Arduino IDE antes de executar `gradlew.bat lwjgl3:run`. Veja `LEIA-ME-ARDUINO-SEM-PULO.md` para diagnostico do sensor.
