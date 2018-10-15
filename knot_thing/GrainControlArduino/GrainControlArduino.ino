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
int32_t temp_value_2 = 0;
int32_t temp_value_3 = 0;
int32_t temp_value_4 = 0;

static int32_t setPoint_value = 0;

/* Print a timestamp via Serial */
static void printTimestamp(void)
{
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

static int temp_read_2(int32_t *val)
{
  *val = temp_value_2;
  Serial.print(F("Temp value2: "));
  Serial.println(*val);
  return 0;
}

static int temp_write_2(int32_t *val)
{
  temp_value_2 = *val;
  Serial.print(F("Temp value2: "));
  Serial.println(*val);

  return 0;
}

static int temp_read_3(int32_t *val)
{
  *val = temp_value_3;
  Serial.print(F("Temp value3: "));
  Serial.println(*val);
  return 0;
}

static int temp_write_3(int32_t *val)
{
  temp_value_3 = *val;
  Serial.print(F("Temp value3: "));
  Serial.println(*val);

  return 0;
}

static int temp_read_4(int32_t *val)
{
  *val = temp_value_4;
  Serial.print(F("Temp value4: "));
  Serial.println(*val);
  return 0;
}

static int temp_write_4(int32_t *val)
{
  temp_value_4 = *val;
  Serial.print(F("Temp value4: "));
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
  Serial.begin(115200);

  /* init KNoTThing library */
  thing.init(THING_NAME);

  /* Register the LED as a data source/sink */
  /* Send data every 30 seconds */
  thing.registerIntData(TEMP_NAME_1, TEMP_ID_1,
                        KNOT_TYPE_ID_TEMPERATURE, KNOT_UNIT_TEMPERATURE_C,
                        temp_read_1, temp_write_1);

  //  thing.registerDefaultConfig(TEMP_ID_1,
  //                              KNOT_EVT_FLAG_TIME,
  //                              0, 0, 0, 0, 0);

  thing.registerIntData(SETPOINT_NAME, SETPOINT_ID,
                        KNOT_TYPE_ID_SPEED, KNOT_UNIT_SPEED_MS,
                        setPoint_read, setPoint_write);


  thing.registerIntData(TEMP_NAME_2, TEMP_ID_2,
                        KNOT_TYPE_ID_TEMPERATURE, KNOT_UNIT_TEMPERATURE_C,
                        temp_read_2, temp_write_2);
  //
  //  thing.registerDefaultConfig(TEMP_ID_2,
  //                              KNOT_EVT_FLAG_CHANGE | KNOT_EVT_FLAG_TIME,
  //                              300, 0, 0, 0, 0);
  //
  thing.registerIntData(TEMP_NAME_3, TEMP_ID_3,
                        KNOT_TYPE_ID_TEMPERATURE, KNOT_UNIT_TEMPERATURE_C,
                        temp_read_3, temp_write_3);
  //
  //  thing.registerDefaultConfig(TEMP_ID_3,
  //                              KNOT_EVT_FLAG_TIME,
  //                              0, 0, 0, 0, 0);
  //
  //  thing.registerIntData(TEMP_NAME_4, TEMP_ID_4, KNOT_TYPE_ID_TEMPERATURE,
  //                        KNOT_UNIT_TEMPERATURE_C, temp_read_4, temp_write_4);
  //
  //  thing.registerDefaultConfig(TEMP_ID_4,
  //                              KNOT_EVT_FLAG_TIME,
  //                              0, 0, 0, 0, 0);



  /* Print thing name via Serial */
  Serial.println(F(THING_NAME));
}

void loop(void) {
  thing.run();
  temp_value_1 = temp1.getTemp() * 100;
  temp_value_2 = temp2.getTemp() * 100;
  temp_value_3 = temp3.getTemp() * 100;
  //  temp_value_4 = temp4.getTemp() * 100;

  //  Serial.print(F("Temp value 1: "));
  //  Serial.println(temp_value_1);
  //  printTimestamp();
  //
  //  Serial.print(F("Temp value 2: "));
  //  Serial.println(temp_value_2);
  //  printTimestamp();
  //
  //  Serial.print(F("Temp value 3: "));
  //  Serial.println(temp_value_3);
  //  printTimestamp();
  //
  //  Serial.print(F("Temp value 4: "));
  //  Serial.println(temp_value_4);
  //  printTimestamp();
}
