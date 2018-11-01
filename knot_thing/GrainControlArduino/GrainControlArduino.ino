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

float deltaTemp = 0.5; // variação aceitável

int stats = 0;

/* Function used by KNoTThing for read the LED value */
static int temp_read_1(int32_t *val)
{
  *val = temp_value_1;
  //Serial.print(F("temp_read_1(): "));
  //Serial.print(*val);
  //Serial.print(";");

  return 0;
}

/* Function used by KNoTThing for write the LED value */
static int temp_write_1(int32_t *val)
{
  temp_value_1 = *val;
  //Serial.print(F("temp_write_1(): "));
  //Serial.print(*val);
  //Serial.print(";");

  return 0;
}

static int setPoint_read(int32_t *val, int32_t *multiplier)
{

  *val = setPoint_value;
  //Serial.print(*val);
  //Serial.print(";");

  return 0;
}

static int setPoint_write(int32_t *val, int32_t *multiplier)
{
  setPoint_value = *val;
  //Serial.print(*val);
  //Serial.print(";");
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

  int avgTemp = (temp1.getTemp() + temp2.getTemp() + temp3.getTemp() + temp4.getTemp() + temp5.getTemp()) * 100 / 5;

  //  Serial.print(temp1.getTemp());
  //  Serial.print("/");
  //  Serial.print(temp2.getTemp());
  //  Serial.print("/");
  //  Serial.print(temp3.getTemp());
  //  Serial.print("/");
  //  Serial.print(temp4.getTemp());
  //  Serial.print("/");
  //  Serial.print(temp5.getTemp());
  //  Serial.print("/ AVG: ");
  //  Serial.print(avgTemp);
  //  Serial.print("/ SP: ");
  Serial.print(setPoint_value);
  Serial.print(" ");

  float avgControl = avgTemp / 100.0;

  Serial.print(tempExt.getTemp());
  Serial.print(" ");

  Serial.print(avgControl);
  Serial.print(" ");

  temp_value_1 = avgTemp;


  if (avgControl < setPoint_value - deltaTemp) {
    stats = 1;
    digitalWrite(3, HIGH);
    //Serial.println("1");
  } else if (avgControl > setPoint_value + deltaTemp) {
    stats = 0;
    digitalWrite(3, LOW);
    //Serial.println("0");
  }

  Serial.print(stats);
  Serial.print(" ");
  
  long finish = millis();

  long deltaT = finish - start;
  Serial.println(deltaT);
  //delayMicroseconds(625);
}
