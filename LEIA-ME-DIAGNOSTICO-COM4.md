# Diagnostico da conexao serial COM4

A COM4 esta mantida como porta padrao do jogo. Se `openPort()` falhar, o controlador agora informa no console:

- quais portas seriais a biblioteca jSerialComm detectou;
- o codigo de erro nativo do Windows e a linha nativa de origem;
- uma explicacao inicial para os codigos comuns 2, 5 e 31.

## Como executar e obter o diagnostico

Abra um terminal na pasta do projeto e execute:

```powershell
.\gradlew.bat lwjgl3:run
```

Copie as linhas que comecam com `ArduinoController`, especialmente `Portas seriais detectadas` e `Falha ao abrir COM4`. O codigo 5 significa acesso negado; pode ser conflito com outro programa, mas nao prova que a porta esteja errada. O codigo 31 aponta mais para dispositivo/cabo/driver.

A mensagem anterior que mandava fechar o Monitor Serial era genérica e não mostrava a causa real. Esta versão evita presumir a causa sem ler o erro do sistema.
