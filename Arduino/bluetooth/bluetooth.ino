#include <SoftwareSerial.h>

float temperature;
int readingtemp;
const int pinTemp = A0;
const int pinLed = 8;
const int pinTx = 6;
const int pinRx = 7;

SoftwareSerial btSerial(pinTx, pinRx);

void setup() 
{
  pinMode(pinLed, OUTPUT);
  analogReference(INTERNAL);
  Serial.begin(9600);
  btSerial.begin(9600);
}

void loop() 
{
  if(btSerial.available())
  {
    char tog = btSerial.read();
    if(tog == 'T')
    {
      readingtemp = analogRead(pinTemp);
      temperature = readingtemp / 9.31;
      Serial.println(temperature);
      btSerial.println(temperature);
    }
    if(tog == 'H')
    {
      digitalWrite(pinLed, HIGH);
      Serial.println(temperature);
    }
    if(tog == 'L')
    {
      digitalWrite(pinLed, LOW);
      Serial.println(temperature);
    }
  }
  if(Serial.available())
  {
    btSerial.write(Serial.read());
  }
}
