import time

timeout = time.time() + 60 * 10

while True:
    #print 'massa %s' % time.time() + 'legal'
    if time.time() >= timeout:
        break