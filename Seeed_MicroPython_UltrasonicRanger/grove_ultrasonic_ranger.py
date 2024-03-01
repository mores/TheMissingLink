"""
A library for grove ultrasonic ranger written for microPython

Ported from: https://github.com/Seeed-Studio/Seeed_Arduino_UltrasonicRanger/blob/master/Ultrasonic.cpp
"""
import machine
import time

class GroveUltrasonicRanger:

    def __init__(self, pin):
        self._pin = pin
        
        self._timeout = 1000000
    
    def duration(self):
        
        pin = machine.Pin(1, machine.Pin.OUT)
        pin.value(0)
        time.sleep_us(2)
        pin.value(1)
        time.sleep_us(5)
        pin.value(0)
        pin.init(machine.Pin.IN)
        
        begin = time.ticks_us()
        
        # wait for any previous pulse to end
        while pin.value():
            if time.ticks_us() - begin >= self._timeout:
                return 0
            
        # wait for the pulse to start
        while not pin.value():
            if time.ticks_us() - begin >= self._timeout:
                return 0
            
        pulseBegin = time.ticks_us()
            
        # wait for the pulse to stop
        while pin.value():
            if time.ticks_us() - begin >= self._timeout:
                return 0
            
        pulseEnd = time.ticks_us()
        
        return pulseEnd - pulseBegin

    def measureInCentimeters(self):
        rangeInCentimeters = self.duration() / 29 / 2
        return rangeInCentimeters
    
    def measureInInches(self):
        rangeInInches = self.duration() / 74 / 2
        return rangeInInches
