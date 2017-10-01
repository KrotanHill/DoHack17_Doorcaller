#include <Arduino.h>
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>

int buttonPin =D6;
int ledPin =14;
int buzzerPin =D7;
char ssid[] ="DO_Hack";
int status =0;
int openDoorTime=10000;
//Get Response Codes
int statusCodeDoorOpen=202;
int statusCodeDoorLock=403;

//Call Url
char twillioUrl[] ="http://x.x.x.x:8080/doorcaller/call";
//Response Url for Door 
char callbackUrl[] ="http://x.x.x.x:8080/doorcaller/shouldOpen";



void pullForOpenDoorEvent(){
    if(restGetCall(callbackUrl) ==statusCodeDoorOpen){
      openDoor();
    }
}


int restGetCall(char url[]){
  HTTPClient http;
  http.begin(url);
delay(300);
  int returnValue= http.GET();
delay(300);
  http.end();
delay(300);
  return returnValue;
}

void openDoor(){
  Serial.println(F("Unlock Door"));
  delay(500);
  digitalWrite(ledPin,HIGH);
  digitalWrite(buzzerPin, HIGH);
  delay(openDoorTime);
  digitalWrite(ledPin,LOW);
  digitalWrite(buzzerPin, LOW);
  Serial.println(F("Lock Door"));
}

void startTwilioCall(){
  int TwilioStatuscode= restGetCall(twillioUrl);
  delay(500);
  Serial.print(F("Get server responsecode : "));
  Serial.println(TwilioStatuscode);
}

//Quick Fix for working Led on/off
void onBuilt(){
  delay(500 );
  digitalWrite(LED_BUILTIN, LOW);
  delay(500 );
}
void offBuilt(){
  delay(500 );
  digitalWrite(LED_BUILTIN, HIGH);
  delay(500);
}



void setup() {
Serial.begin(115200);
pinMode(LED_BUILTIN, OUTPUT);
pinMode(buttonPin, INPUT);
pinMode(ledPin, OUTPUT);
pinMode(buzzerPin, OUTPUT);

//WLAN Connect Part
    Serial.println(F("Start Connecting in 2 sec"));
    delay(2000);
    Serial.println(F("Starting"));
    
    //Quickfix 
    if(true){
          status = WiFi.begin(ssid);
          delay(100);
          Serial.print(F("Status after begin :"));
          Serial.println(status);
          status = WiFi.waitForConnectResult();
          delay(500);
          Serial.println(status);
          if (status != WL_CONNECTED) {
              Serial.println(F("Connection Failed"));
              while (true) {}
          }
      }

      Serial.println(F("Connected."));
      Serial.print(F("MAC Addr: "));
      Serial.println(WiFi.macAddress());
      Serial.print(F("IP Addr:  "));
      Serial.println(WiFi.localIP());
      Serial.print(F("Subnet:   "));
      Serial.println(WiFi.subnetMask());
      Serial.print(F("Gateway:  "));
      Serial.println(WiFi.gatewayIP());
      Serial.print(F("DNS Addr: "));
      Serial.println(WiFi.dnsIP());
      Serial.print(F("Channel:  "));
      Serial.println(WiFi.channel());
      Serial.print(F("Status: "));
      Serial.println(WiFi.status());
      
      offBuilt();

}


void loop(){
  //Read Button Status
  if(digitalRead(buttonPin)){
    delay(500);
    Serial.println("Door bell pushed - Ding Dong");
    startTwilioCall();
  }
  //Look for Rest Door Opening Event
  pullForOpenDoorEvent();
}
