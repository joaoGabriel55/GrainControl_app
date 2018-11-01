#include <AutoPID.h>

#include <KNoTThing.h>
#include <Thermistor.h>

#define KNOT_BOARD_VXX_XX

#define TEMP_PIN_1          A0
#define TEMP_ID_1           1
#define TEMP_NAME_1         "NTC Sensor1"

#define TEMP_PIN_2          A1
#define TEMP_ID_2           2
#define TEMP_NAME_2         "NTC Sensor2"

#define TEMP_PIN_3          A2
#define TEMP_ID_3           3
#define TEMP_NAME_3         "NTC Sensor3"

#define TEMP_PIN_4          A3
#define TEMP_ID_4           4
#define TEMP_NAME_4         "NTC Sensor4"

#define TEMP_PIN_5          A4
#define TEMP_ID_5           5
#define TEMP_NAME_5         "NTC Sensor5"

#define THING_NAME          "Knot Temp"
#define PRINTING_TIME       30000

//Equivale ao pino que sera ligado no NOVUS
#define SETPOINT_ID         6
#define SETPOINT_NAME       "SetPoint"

//pins
#define OUTPUT_PIN 3

#define TEMP_READ_DELAY 55 //can only read digital temp sensor every ~750ms

//pid settings and gains
#define OUTPUT_MIN 0
#define OUTPUT_MAX 255
#define KP 1/0.6
#define KI KP/1.77
#define KD KP*6

/* KNoTThing instance */
KNoTThing thing;

Thermistor temp1(TEMP_PIN_1);
Thermistor temp2(TEMP_PIN_2);
Thermistor temp3(TEMP_PIN_3);
Thermistor temp4(TEMP_PIN_4);
Thermistor temp5(TEMP_PIN_5);

Thermistor tempExt(A5);

/* Variable that holds LED value */
int32_t temp_value_1 = 0;

static int32_t setPoint_value = 20;

float deltaTemp = 1.0; // variação aceitável

int stats = 0;

double temperature, setPoint, outputVal, avg;

//input/output variables passed by reference, so they are updated automatically
AutoPID myPID(&temperature, &setPoint, &outputVal, OUTPUT_MIN, OUTPUT_MAX, KP, KI, KD);

unsigned long lastTempUpdate; //tracks clock time of last temp update

//call repeatedly in loop, only updates after a certain time interval
//returns true if update happened
bool updateTemperature() {

  double temperature1 = temp1.getTemp();
  double temperature2 = temp2.getTemp();
  double temperature3 = temp3.getTemp();
  double temperature4 = temp4.getTemp();
  double temperature5 = temp5.getTemp();

  avg = (temperature1 + temperature2 + temperature3 + temperature4 + temperature5) / 5;

  if ((millis() - lastTempUpdate) > TEMP_READ_DELAY) {
    temperature = avg; //get temp reading
    temp_value_1 = avg * 100;
    lastTempUpdate = millis();
    return true;
  }
  return false;
}//void updateTemperature

/* Function used by KNoTThing for read the LED value */
static int temp_read_1(int32_t *val) {
  *val = temp_value_1;
  //Serial.print(F("temp_read_1(): "));
  //Serial.print(*val);
  //Serial.print(";");

  return 0;
}

/* Function used by KNoTThing for write the LED value */
static int temp_write_1(int32_t *val) {
  temp_value_1 = *val;
  //Serial.print(F("temp_write_1(): "));
  //Serial.print(*val);
  //Serial.print(";");

  return 0;
}

static int setPoint_read(int32_t *val, int32_t *multiplier) {

  *val = setPoint_value;
  //Serial.print(*val);
  //Serial.print(";");

  return 0;
}

static int setPoint_write(int32_t *val, int32_t *multiplier) {
  setPoint_value = *val;
  //Serial.print(*val);
  //Serial.print(";");
  return 0;
}


void setup(void) {
  Serial.begin(9600);

  TCCR2A = _BV(COM2A1) | _BV(COM2B1) | _BV(WGM20);
  TCCR2B = TCCR2B & B11111000 | B00000111;

  while (!updateTemperature()) {} //wait until temp sensor updated

  //if temperature is more than 4 degrees below or above setpoint, OUTPUT will be set to min or max respectively
  //myPID.setBangBang(1.0);
  //set PID update interval to 4000ms
  //myPID.setTimeStep(1000);

  pinMode(8, OUTPUT);
  //digitalWrite(8, LOW);

  /* init KNoTThing library */
  thing.init(THING_NAME);

  /* Register the LED as a data source/sink */

  thing.registerIntData(SETPOINT_NAME, SETPOINT_ID,
                        KNOT_TYPE_ID_SPEED, KNOT_UNIT_SPEED_MS,
                        setPoint_read, setPoint_write);

  thing.registerIntData(TEMP_NAME_1, TEMP_ID_1,
                        KNOT_TYPE_ID_TEMPERATURE, KNOT_UNIT_TEMPERATURE_C,
                        temp_read_1, temp_write_1);

  /* Send data every 10 seconds */
  thing.registerDefaultConfig(TEMP_ID_1,
                              KNOT_EVT_FLAG_TIME, 10, 0, 0, 0, 0);

  /* Print thing name via Serial */
  Serial.println(F(THING_NAME));
  //delay(625);
}

void loop(void) {
  long start = millis();
  thing.run();

  setPoint = setPoint_value;

  updateTemperature();

  myPID.run();

  Serial.print(setPoint_value);
  Serial.print(" ");

  Serial.print(tempExt.getTemp());
  Serial.print(" ");

  Serial.print(avg);
  Serial.print(" ");

  Serial.print(outputVal);  
  Serial.print(" ");

  analogWrite(OUTPUT_PIN, outputVal);

  long finish = millis();

  long deltaT = finish - start;
  Serial.println(deltaT);
  //delayMicroseconds(625);
}
