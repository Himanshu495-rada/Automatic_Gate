#include "WiFi.h"
 
const char* ssid = "Gate";
const char* password =  "vigyanashram";
 
WiFiServer server(80);
 
const int relay_1 = 2;
const int relay_2 = 4;
 
void processReceivedValue(char command){
 
  if(command == '1'){ digitalWrite(relay_1, LOW); }
  else if(command == '2'){ digitalWrite(relay_2, LOW);}
  else if(command == '0') {digitalWrite(relay_1, HIGH); digitalWrite(relay_2, HIGH);}
 
   return;
}
 
void setup() {
 
  Serial.begin(115200);
 
  delay(1000);
 
  WiFi.softAP(ssid, password);
 
//  while (WiFi.status() != WL_CONNECTED) {
//    delay(1000);
//    Serial.println("Connecting to WiFi..");
//  }
 
  Serial.println("Connecte to IP");
  Serial.println(WiFi.softAPIP());
 
  server.begin();
 
  pinMode(relay_1, OUTPUT);
  digitalWrite(relay_1, HIGH);
  pinMode(relay_2, OUTPUT);
  digitalWrite(relay_2, HIGH);
}
 
void loop() {
 
  WiFiClient client = server.available();
 
  if (client) {
    Serial.write("Client connected");
    
    while (client.connected()) {
 
      while (client.available()>0) {
        char c = client.read();
        processReceivedValue(c);
        Serial.write(c);
      }
 
      delay(10);
    }
 
    client.stop();
    Serial.println("Client disconnected");
 
  }
}
