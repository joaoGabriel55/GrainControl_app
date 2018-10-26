import matplotlib.pyplot as plt
import numpy as np

f = open('testfile.txt')

list = []

line = f.readline()

while line:
    
    #print(line)
    line = f.readline()
    value = line.strip()
    list.append(value)

f.close()

setPointArr = []
tempExtArr = []
tempIntArr = []
resistorArr = []

for i in range(len(list)-1):
    x = list[i].split(' ')
    print(x)
    setPointArr.append(float(x[0]))
    tempExtArr.append(float(x[1]))
    tempIntArr.append(float(x[2]))
    resistorArr.append(int(x[3]))

print(setPointArr)
#print(tempExtArr)

plt.plot(np.arange(len(list)-1), tempExtArr)
plt.plot(np.arange(len(list)-1), setPointArr)
plt.plot(np.arange(len(list)-1), tempIntArr)
plt.plot(np.arange(len(list)-1), resistorArr)


ax = plt.gca()

ax.set_ylim([15, 45])

plt.ylabel('some numbers')
plt.show()

