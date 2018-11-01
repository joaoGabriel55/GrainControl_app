import serial
import time
 
comport = serial.Serial('/dev/ttyACM0', 9600)

PARAM_CARACTER='t'
PARAM_ASCII=str(chr(116))       # Equivalente 116 = t
 
# Time entre a conexao serial e o tempo para escrever (enviar algo)
time.sleep(1.8) # Entre 1.5s a 2s
 
#comport.write(PARAM_CARACTER)
comport.write(PARAM_ASCII)

cont = 0
file = open('testfile.txt','w')
end_time = time.time() + 60 * 10
print "Start : %s" % time.time()
while True:
    VALUE_SERIAL=comport.readline()
    
    print '%s' % (VALUE_SERIAL)
    #time.sleep(1)
    file.write(VALUE_SERIAL) 

    if time.time() >= end_time:
        break

# Fechando conexao serial
comport.close()
print "End : %s" % time.time()

# if file.mode == 'r':
#     contents =file.read()
#     print(contents)