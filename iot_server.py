import pyrebase
import Adafruit_DHT
import RPi.GPIO as GPIO
import time, sys, os
from MQ import *

#================================: Initialization :=================================

firebaseConfig = {
    "apiKey": "AIzaSyAsNEthmi-tVhlaSH4fcNGi4Rr98Q7HSC8",
    "authDomain": "home-automation-iot-2019.firebaseapp.com",
    "databaseURL": "https://home-automation-iot-2019.firebaseio.com",
    "projectId": "home-automation-iot-2019",
    "storageBucket": "home-automation-iot-2019.appspot.com",
    "messagingSenderId": "164364130987",
    "appId": "1:164364130987:web:0596a80fbb65d7f9"
}
firebase = pyrebase.initialize_app(firebaseConfig)
db = firebase.database()
GPIO.setmode(GPIO.BCM)  # choose the pin numbering
power = db.child('power').get()
os.system('clear')

# ANSI escape codes
PREVIOUS_LINE = "\x1b[1F"
RED_BACK = "\x1b[41;37m"
GREEN_BACK = "\x1b[42;30m"
YELLOW_BACK = "\x1b[43;30m"
RESET = "\x1b[0m"

#Gas Sensor:
mq = MQ()
LPG = 0
SMOKE = 0
CO = 0

# DHT-11 Sensor GPIO pin:
ROOM1_DHT = 11
ROOM1_PIN = 25

# Geyser Ultrasonic Sensor GPIO Pins:
GEYSER_TRIG = 26   
GEYSER_ECHO = 19   
GPIO.setup(GEYSER_TRIG, GPIO.OUT)
GPIO.setup(GEYSER_ECHO, GPIO.IN)

# Water-tank Ultrasonic Sensor GPIO Pins:
WATER_TRIG = 20
WATER_ECHO = 16
GPIO.setup(WATER_TRIG, GPIO.OUT)
GPIO.setup(WATER_ECHO, GPIO.IN)

#================================: Utility Methods :=================================

def read_Ultra(trig, echo):
    GPIO.output(trig, False)
    time.sleep(2)
    GPIO.output(trig, True)
    time.sleep(0.00001)
    GPIO.output(trig, False)
    while GPIO.input(echo) == 0:
        pass
    start = time.time()
    while GPIO.input(echo) == 1:
        pass
    stop = time.time()
    distance = (stop-start)*17000
    return distance

#================================: Stream Handlers :=================================

def mainStream_handler(message):
    #print("EVENT : ",message["event"])
    #print("PATH : ",message["path"])
    #print("DATA : ",message["data"])

    #------------ Power ------------
    pass

def geyserStream_handler(message):
    #------------ Mode ------------
    pass
    #------------ State ------------
    pass

def room1Stream_handler(message):
    #------------ Bulb ------------
    pass
    #------------ Fan ------------
    pass

#==============================: Stream Declarations :===============================

#main_stream = db.stream(mainStream_handler)
#geyser_stream = db.child('geyser').stream(geyserStream_handler)
#room1_stream = db.child('room_stats').child('room1').stream(room1Stream_handler)

#===================================: Main Loop :====================================

db.child('geyser').update({'reference': read_Ultra(GEYSER_TRIG, GEYSER_ECHO)})
print('\n', GREEN_BACK,
'\t\t\t\t---------------------------: System Output :---------------------------', RED_BACK)
try:
    while True:
        sys.stdout.write("\r")
        sys.stdout.write("\033[K")
        #------------ Gas Leak ------------
        perc = mq.MQPercentage()
        LPG = perc["GAS_LPG"]
        SMOKE = perc["SMOKE"]
        CO = perc["CO"]
        db.child('MQ2_stats').update({'smoke': SMOKE, 'CO': CO, 'LPG': LPG})
        pass
        #------------ Geyser Distance ------------
        geyserDist = read_Ultra(GEYSER_TRIG, GEYSER_ECHO)
        db.child('geyser').update({'distance': geyserDist})
        pass
        #------------ Temperature & Humidity ------------
        (H1, T1) = Adafruit_DHT.read_retry(ROOM1_DHT, ROOM1_PIN)
        db.child('room_stats').child('room1').update({'humidity': H1, 'temperature': T1})
        pass
        #------------ Water Level ------------
        waterDist = read_Ultra(WATER_TRIG, WATER_ECHO)
        db.child('water_tank').update({'distance': waterDist})
        pass
        #-------------------------------------------------------
        sys.stdout.write("\r")
        sys.stdout.write("\033[K")
        sys.stdout.write("LPG: {0:0.3f}ppm   CO: {1:0.3f}ppm   Smoke: {2:0.3f}ppm   Temperature: {3:0.1f}*C   Humidity: {4:0.1f}%   Geyser Distance: {5:0.3f}cm   Water-tank Distance: {6:0.3f}cm".format(LPG,CO,SMOKE,H1,T1,geyserDist,waterDist))
        sys.stdout.flush()

except KeyboardInterrupt:
    GPIO.cleanup()
    print('\n',RESET)
    sys.exit()
