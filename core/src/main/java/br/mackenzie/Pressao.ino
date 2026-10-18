// =========================================================================
// Projeto de Reabilitação - Flappy Bird com Sensor FSR (Pressão na Bolinha)
// =========================================================================

const int FSR_PIN = A0;           // Pino analógico onde o FSR está conectado

// CALIBRAÇÃO DE FORÇA (0 a 1023)
// Ajuste este valor conforme a força/evolução do paciente:
// - Valores menores (ex: 100 - 200): Para pacientes com baixa força muscular.
// - Valores maiores (ex: 400 - 600): Para fases mais avançadas de fortalecimento.
const int LIMIAR_PRESSAO = 200;   

// EVITA DISPAROS DUPLOS (Debounce)
const unsigned long COOLDOWN_MS = 250; // Tempo mínimo em milissegundos entre pulos

bool estadoApertadoAnterior = false;
unsigned long ultimoPuloTempo = 0;

void setup() {
  Serial.begin(9600); // Inicializa a comunicação serial a 9600 bps
}

void loop() {
  int valorFSR = analogRead(FSR_PIN); // Lê o valor analógico do sensor (0 a 1023)
  
  // Verifica se a pressão atual ultrapassa o limiar configurado
  bool estaApertado = (valorFSR >= LIMIAR_PRESSAO);

  // Detecta a transição (borda de subida): dispara APENAS no início do aperto
  if (estaApertado && !estadoApertadoAnterior && (millis() - ultimoPuloTempo > COOLDOWN_MS)) {
    Serial.println("JUMP"); // Envia o sinal lido pela classe ArduinoController em Java
    ultimoPuloTempo = millis();
  }

  estadoApertadoAnterior = estaApertado;

  // -----------------------------------------------------------------------
  // DICA DE CALIBRAÇÃO:
  // Descomente a linha abaixo e abra o 'Serial Plotter' do Arduino IDE 
  // para visualizar graficamente a força da mão do paciente em tempo real!
  // -----------------------------------------------------------------------
  // Serial.println(valorFSR); 

  delay(10); // Pequeno atraso para estabilização das leituras analógicas
}