int pinoFSR = A0; // Pino do sensor
int limitePressao = 300; // Ajuste conforme a força necessária
bool pulando = false;

void setup() {
  Serial.begin(9600);
}

void loop() {
  int valorPressao = analogRead(pinoFSR);

  // Se apertou a bolinha forte o suficiente e não estava pulando antes
  if (valorPressao > limitePressao && !pulando) {
    Serial.println("PULO"); // Envia o sinal para o Java
    pulando = true; // Trava para não enviar "PULO" 1000 vezes enquanto segura
  } 
  // Se soltou a bolinha, destrava para poder pular de novo
  else if (valorPressao < limitePressao - 50) { 
    pulando = false;
  }
  
  delay(10); // Pequeno atraso para estabilizar a leitura
}