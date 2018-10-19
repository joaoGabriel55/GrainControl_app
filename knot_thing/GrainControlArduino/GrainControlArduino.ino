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

#define THING_NAME          "Knot Temp"
#define PRINTING_TIME       30000

//Equivale ao pino que sera ligado no NOVUS
#define SETPOINT_ID         5
#define SETPOINT_NAME       "SetPoint"

/* KNoTThing instance */
KNoTThing thing;

Thermistor temp1(TEMP_PIN_1);
Thermistor temp2(TEMP_PIN_2);
Thermistor temp3(TEMP_PIN_3);
Thermistor temp4(TEMP_PIN_4);

/* Variable that holds LED value */
int32_t temp_value_1 = 0;

static int32_t setPoint_value = 0;

float deltaTemp = 0.5; // variação aceitável 

/* Print a timestamp via Serial */
static void printTimestamp(void) {
  static long current_millis = 0, print_millis = 0, hour = 0, minute = 0, sec = 0;

  if (millis() - current_millis >= 10000) {
    sec++;
    if (sec >= 60) {
      minute++;
      sec = 0;
      if (minute >= 60) {
        hour++;
        minute = 0;
      }
    }
    current_millis = millis();
  }
  if (millis() - print_millis >= PRINTING_TIME) {
    Serial.print(hour);
    Serial.print(":");
    Serial.print(minute);
    Serial.print(":");
    Serial.println(sec);
    print_millis = millis();
  }
}

/* Function used by KNoTThing for read the LED value */
static int temp_read_1(int32_t *val)
{
  *val = temp_value_1;
  Serial.print(F("Temp value1: "));
  Serial.println(*val);
  return 0;
}

/* Function used by KNoTThing for write the LED value */
static int temp_write_1(int32_t *val)
{
  temp_value_1 = *val;
  Serial.print(F("Temp value1: "));
  Serial.println(*val);

  return 0;
}

static int setPoint_read(int32_t *val, int32_t *multiplier)
{

  *val = setPoint_value;
  Serial.print(F("setPoint_read(): "));
  Serial.println(*val);
  return 0;
}

static int setPoint_write(int32_t *val, int32_t *multiplier)
{
  setPoint_value = *val;
  Serial.print(F("setPoint_write(): "));
  Serial.println(*val);
  return 0;
}


void setup(void)
{
  Serial.begin(9600);
  
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

  /* Send data every 30 seconds */
  thing.registerDefaultConfig(TEMP_ID_1,
                              KNOT_EVT_FLAG_TIME | KNOT_EVT_FLAG_CHANGE,
                              30, 0, 0, 0, 0);

  /* Print thing name via Serial */
  Serial.println(F(THING_NAME));
}

void loop(void) {
  thing.run();

  int avgTemp = (temp1.getTemp() + temp2.getTemp() + temp3.getTemp() + temp4.getTemp()) * 100 / 4;

  Serial.print(temp1.getTemp());
  Serial.print("/");
  Serial.print(temp2.getTemp());
  Serial.print("/");
  Serial.print(temp3.getTemp());
  Serial.print("/");
  Serial.print(temp4.getTemp());
  Serial.print("/ AVG: ");
  Serial.println(avgTemp);
  Serial.print("/ SP: ");
  Serial.println(setPoint_value);
  
  float avgControl = avgTemp / 100.0;
  Serial.println(avgControl);
  
  temp_value_1 = avgTemp;
  
  if (avgControl < setPoint_value - deltaTemp) {
    digitalWrite(8, HIGH);
  } else if (avgControl > setPoint_value + deltaTemp) {
    digitalWrite(8, LOW);
  }
  
  
}