# Correcao do pulo pelo Arduino

## O que foi alterado
- O controlador desktop agora usa eventos de bytes da jSerialComm, sem `readLine()` com timeout que podia encerrar a leitura em momentos sem dados.
- O jogo enfileira um acionamento recebido durante `COUNTDOWN`, aplicando-o assim que a partida iniciar, em vez de descartar esse primeiro comando.
- O sketch faz calibracao sem pressao, mede a variacao absoluta (serve se a leitura do FSR subir ou cair), envia `JUMP` e mostra `DELTA:<valor>` para ajustar a sensibilidade.

## Como testar
1. Abra `arduino/Pressao.ino` no Arduino IDE.
2. Deixe o sensor sem pressao nos primeiros 1,5 segundo e carregue o sketch.
3. Feche o Monitor Serial do Arduino IDE. Ele nao pode compartilhar a COM4 com o jogo.
4. Execute `./gradlew lwjgl3:run` (PowerShell/Windows: `\gradlew.bat lwjgl3:run`).
5. Selecione uma dificuldade e pressione/solte o sensor durante a partida.

## Mensagens esperadas no console
- `Recebido: READY` e `Recebido: FSR:...`: bytes estao chegando ao jogo.
- `COMANDO DE PULO RECEBIDO pela serial.`: o Arduino enviou `JUMP` e o controlador recebeu.
- `PULO ACIONADO PELO ARDUINO.`: Main chamou `Player.jump()` durante o jogo.
- `Pulo do Arduino enfileirado durante a contagem regressiva.`: o acionamento sera aplicado quando a partida comecar.

## Sensor
A ligacao recomendada e FSR de 5V para A0 e resistor de 10k de A0 para GND. O baseline e capturado no inicio, sem apertar o sensor. `LIMIAR_PRESSIONAR` esta em 40 unidades e `LIMIAR_SOLTAR` em 18. Se `DELTA` ao pressionar nao chegar a 40, reduza o limiar (por exemplo, para 25). Se `DELTA` em repouso oscilar muito, confira a ligacao do resistor de 10k e aumente o limiar. Nao pressione durante a calibracao.


## Diagnostico rapido do FSR

1. Confirme a ligacao em divisor de tensao: 5V -> FSR -> ponto A0; desse mesmo ponto, resistor de 10k -> GND. Nao ligue o FSR sozinho ao A0.
2. Com o Monitor Serial em 9600 baud, solte o sensor durante a calibracao e depois pressione/solte. Observe `FSR` e `DELTA`.
3. Se `DELTA` mudar de 0 para 70 ao pressionar, por exemplo, o limiar 40 deve disparar. Se subir apenas para 20, reduza `LIMIAR_PRESSIONAR` para um valor abaixo da variacao observada e mantenha `LIMIAR_SOLTAR` menor.
4. Para testar o jogo, feche o Monitor Serial/Plotter antes de executar o jogo: apenas um programa pode usar a porta COM por vez. Confirme a porta em Ferramentas > Porta no Arduino IDE.
5. No console do jogo, procure `Recebido: DELTA:...`, `COMANDO DE PULO RECEBIDO pela serial.` e `PULO ACIONADO PELO ARDUINO.`. Esses registros separam problema de sensor, comunicacao serial e logica do jogo.
