/*
Serial port /dev/cu.usbmodem13101:
Connecting...
Connected to ESP32-C6 on /dev/cu.usbmodem13101:
Chip type:          ESP32-C6FH4 (QFN32) (revision v0.1)
Features:           Wi-Fi 6, BT 5 (LE), IEEE802.15.4, Single Core + LP Core, 160MHz
Crystal frequency:  40MHz
USB mode:           USB-Serial/JTAG

/dev/tty.usbmodem13101 - Serial Monitor Port

Connect wire from pin 7 -> pin 6
*/

#define RX_PIN D7
#define TX_PIN D6

int incomingByte = 0;  // for incoming serial data

void setup() {
  Serial.begin(115200);                             // initialise serial monitor port
  Serial1.begin(9600, SERIAL_8N1, RX_PIN, TX_PIN);  //115200);  // initialise Serial1
  pinMode(LED_BUILTIN, OUTPUT);
}

void loop() {

  if (Serial1.available()) {  // read from Serial1 output to Serial
    incomingByte = Serial1.read();
    printf("From serial pins (D6/D7) I received:0x%.2x(%c)\n", incomingByte, incomingByte);
    digitalWrite(LED_BUILTIN, HIGH);
    delay(200);
    digitalWrite(LED_BUILTIN, LOW);
  }

  if (Serial.available()) {  // read from Serial outut to Serial1
    char inByte = Serial.read();
    printf("From serial monitor (USB) I received:0x%.2x(%c)\n", inByte, inByte);
    Serial1.write(inByte);
  }
}