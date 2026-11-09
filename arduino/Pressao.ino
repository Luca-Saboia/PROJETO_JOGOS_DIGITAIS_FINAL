// Flappy Game - Sensor FSR para Arduino Uno / libGDX
// Comunicacao serial: 9600 baud. Ao pressionar, envia uma linha "JUMP".
// Ligacao recomendada (divisor de tensao):
//   5V ---- FSR ----+---- A0
//                  |
//                 10k ohm
//                  |
//                 GND
// Deixe o sensor totalmente SOLTO durante os 1,5 s de calibracao.

const int FSR_PIN = A0;

// Gatilho mais sensivel do que o valor 100 do sketch anterior.
// Ajuste LIMIAR_PRESSIONAR observando DELTA no Monitor Serial.
const int LIMIAR_PRESSIONAR = 40;
const int LIMIAR_SOLTAR = 18;

const unsigned long CALIBRACAO_MS = 1500;
const unsigned long INTERVALO_STATUS_MS = 250;
const unsigned long COOLDOWN_MS = 180;

int valorBase = 0;
bool sensorPressionado = false;
unsigned long ultimaLeituraStatus = 0;
unsigned long ultimoPuloTempo = 0;

void setup() {
  Serial.begin(9600);
  delay(300);
  Serial.println("READY");

  // Calcula a referencia sem pressao.
  unsigned long inicio = millis();
  long soma = 0;
  int quantidade = 0;

  while (millis() - inicio < CALIBRACAO_MS) {
    soma += analogRead(FSR_PIN);
    quantidade++;
    delay(10);
  }

  valorBase = quantidade > 0 ? (int)(soma / quantidade) : analogRead(FSR_PIN);
  Serial.print("BASELINE:");
  Serial.println(valorBase);
}

void loop() {
  unsigned long agora = millis();
  int leitura = analogRead(FSR_PIN);

  // A diferenca absoluta permite detectar mudancas para cima ou para baixo.
  int diferenca = abs(leitura - valorBase);

  // Diagnostico: permite confirmar se a leitura realmente muda ao apertar.
  if (agora - ultimaLeituraStatus >= INTERVALO_STATUS_MS) {
    Serial.print("FSR:");
    Serial.println(leitura);
    Serial.print("DELTA:");
    Serial.println(diferenca);
    ultimaLeituraStatus = agora;
  }

  // Um unico JUMP por aperto; so rearma quando o sensor for solto.
  if (!sensorPressionado && diferenca >= LIMIAR_PRESSIONAR
      && (ultimoPuloTempo == 0 || agora - ultimoPuloTempo >= COOLDOWN_MS)) {
    sensorPressionado = true;
    ultimoPuloTempo = agora;
    Serial.println("JUMP");
  }

  if (sensorPressionado && diferenca <= LIMIAR_SOLTAR) {
    sensorPressionado = false;
  }

  delay(10);
}
