import time
from grove_ultrasonic_ranger import GroveUltrasonicRanger

ultrasonic = GroveUltrasonicRanger(1)

while True:

    centimeters = ultrasonic.measureInCentimeters()
    print('centimeters {}'.format( centimeters ) )
    time.sleep_ms(250)
    
    inches = ultrasonic.measureInInches()
    print('inches {}'.format( inches ) )
    time.sleep_ms(250)
