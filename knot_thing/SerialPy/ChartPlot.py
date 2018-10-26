import matplotlib.pyplot as plt
import numpy as np

x1 = np.arange(6)

print(x1)

y = [0,1,2,3,4,5]
y2 = [99,1,14,48,6,4]

plt.plot(x1, y,y2)
plt.ylabel('some numbers')
plt.show()