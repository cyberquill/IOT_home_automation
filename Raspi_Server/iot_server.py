import pyrebase
import Adafruit_DHT
import RPi.GPIO as GPIO
import sys
import os
import threading
import multitimer
import time
from MQ import *

# ================================: Initialization :=================================

firebaseConfig = {
    "apiKey": "AIzaSyAXpQL2udr__kKf4zuR9twqhBL5qy6HfDU",
    "authDomain": "home-automation-2a31b.firebaseapp.com",
    "databaseURL": "https://home-automation-2a31b.firebaseio.com",
    "projectId": "home-automation-2a31b",
    "storageBucket": "home-automation-iot-2019.appspot.com",
    "messagingSenderId": "980468795339",
    "appId": "1:980468795339:web:f1fe858c6dfb4d1085184e"
}

DB_INIT = {
    "MQ2_stats": {
        "CO": 0.00,
        "LPG": 0.00,
        "smoke": 0.00
    },
    "configure": "none",
    "geyser": {
        "distance": 0.00,
        "mode": "auto",
        "notify": "none",
        "reference": 0.00,
        "state": "off",
        "vigilant": "yes"
    },
    "power": "on",
    "room_stats": {
        "room1": {
            "bulb": "off",
            "fan": "off",
            "humidity": 0.00,
            "temperature": 0.00
        }
    },
    "water_tank": {
        "distance": 0.00,
        "msg": "none",
        "ref_max": 0.00,
        "ref_min": 0.00
    }
}

firebase = pyrebase.initialize_app(firebaseConfig)
db = firebase.database()
GPIO.setmode(GPIO.BCM)  # choose the pin numbering
os.system('clear')
db.update(DB_INIT)

#Misc. Constants
ACTIVE_TIMER = 0
ROOM1_DHT = 11

# ANSI escape codes
PREVIOUS_LINE = "\x1b[1F"
RED_BACK = "\x1b[41;37m"
GREEN_BACK = "\x1b[42;30m"
YELLOW_BACK = "\x1b[43;30m"
RESET = "\x1b[0m"

# Gas Sensor:
""" mq = MQ()
LPG = 0
SMOKE = 0
CO = 0 """

# DHT-11 Sensor GPIO pin:
ROOM1_DHT_PIN = 25
ROOM1_BULB_PIN = 21 # 17
GPIO.setup(ROOM1_BULB_PIN, GPIO.OUT)

# Geyser Ultrasonic Sensor GPIO Pins:
GEYSER_TRIG = 26
GEYSER_ECHO = 19
GEYSER_PIN = 17
GPIO.setup(GEYSER_TRIG, GPIO.OUT)
GPIO.setup(GEYSER_ECHO, GPIO.IN)
GPIO.setup(GEYSER_PIN, GPIO.OUT)

# Water-tank Ultrasonic Sensor GPIO Pins:
WATER_TRIG = 20
WATER_ECHO = 16
GPIO.setup(WATER_TRIG, GPIO.OUT)
GPIO.setup(WATER_ECHO, GPIO.IN)

# ================================: Utility Methods :=================================


def read_ultra(trig, echo):
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

def timer3_handler():
    global ACTIVE_TIMER, timer3
    print('timer3_handler: timer3 expired')
    db.child('geyser').update({'vigilant': 'yes'})
    ACTIVE_TIMER = 0
timer3 = multitimer.MultiTimer(interval=60, function=timer3_handler, count=1, runonstart=False)

def timer2_handler():
    print('timer2_handler: timer2 expired')
    db.child('geyser').update({'notify': 'OFF'})
timer2 = multitimer.MultiTimer(interval=60, function=timer2_handler, count=1, runonstart=False)

def timer1_handler():
    global ACTIVE_TIMER, timer2
    print('timer1_handler: timer1 expired')
    if ACTIVE_TIMER == 1:
        db.child('geyser').update({'notify': 'SHOW'})       # Want to keep on? 'ON' / 'OFF' 
        ACTIVE_TIMER = 2;
        timer2.start()
timer1 = multitimer.MultiTimer(interval=60, function=timer1_handler, count=1, runonstart=False)

def geyser_handler():
    global ACTIVE_TIMER, timer1
    mode = db.child('geyser').child('mode').get().val()
    state = db.child('geyser').child('state').get().val()
    dist = db.child('geyser').child('distance').get().val()
    ref = db.child('geyser').child('reference').get().val()
    vigilant = db.child('geyser').child('vigilant').get().val()

    if mode == 'auto' and state == 'on' and vigilant == 'yes':
        if dist >= ref:                     # Person not in front
            if ACTIVE_TIMER == 0:
                timer1.start()
                ACTIVE_TIMER = 1
                print('geyser_handler: timer1 started: ', dist, '>=', ref)
        elif ACTIVE_TIMER == 1:                               # Person in front
            timer1.stop()
            ACTIVE_TIMER = 0
            print('geyser_handler: timer1 stopped', dist, '<', ref)

def reset_all_timers():
    global ACTIVE_TIMER, timer1, timer2, timer3
    if ACTIVE_TIMER == 1:
        timer1.stop()
    if ACTIVE_TIMER == 2:
        timer2.stop()
    if ACTIVE_TIMER == 3:
        timer3.stop()

    ACTIVE_TIMER = -1
    print('ALL TIMER RESET')


# ===================================: Main Loop :====================================


try:
    while True:
        sys.stdout.write("\r")
        sys.stdout.write("\033[K")
        if db.child('power').get().val() == 'off':
            os.system('clear')
            continue

        # --------- Configuration ----------
        config = db.child('configure').get()
        if config.val() == 'geyser':
            db.child('geyser').update(
                {'reference': read_ultra(GEYSER_TRIG, GEYSER_ECHO)})
            db.update({'configure': 'none'})

        elif config.val() == 'tank_min':
            db.child('water_tank').update(
                {'ref_min': read_ultra(WATER_TRIG, WATER_ECHO)})
            db.update({'configure': 'none'})

        elif config.val() == 'tank_max':
            db.child('water_tank').update(
                {'ref_max': read_ultra(WATER_TRIG, WATER_ECHO)})
            db.update({'configure': 'none'})

        # ----------------- Fan Trigger ------------------

        if db.child('room_stats').child('room1').child('bulb').get().val() == 'on':
            GPIO.output(ROOM1_BULB_PIN, GPIO.LOW)
        else:
            GPIO.output(ROOM1_BULB_PIN, GPIO.HIGH)

        # ----------------- Geyser Triggers ------------------

        if db.child('geyser').child('mode').get().val() == 'manual':
            print('point-1: geyser mode to manual')
            reset_all_timers()
            GPIO.output(GEYSER_PIN, GPIO.LOW)

        if db.child('geyser').child('state').get().val() == 'on':
            print('point-2: geyser start')
            GPIO.output(GEYSER_PIN, GPIO.LOW)
        else:
            print('point-3: geyser stop')
            reset_all_timers()
            GPIO.output(GEYSER_PIN, GPIO.HIGH)

        if db.child('geyser').child('notify').get().val() == 'ON':
            print('point-4: geyser notify: ON')
            db.child('geyser').update({'vigilant': 'no'})
            db.child('geyser').update({'notify': 'none'})
            if ACTIVE_TIMER == 2:
                timer2.stop()
                ACTIVE_TIMER = 3
                timer3.start()
                print('point-5: timer3 start')

        if db.child('geyser').child('notify').get().val() == 'OFF':
            print('point-6: geyser notify: OFF')
            db.child('geyser').update({'state': 'off'})
            db.child('geyser').update({'notify': 'none'})
            if ACTIVE_TIMER == 2:
                timer2.stop()
                ACTIVE_TIMER = 0

        # ------------ Gas Leak ------------

        """ perc = mq.MQPercentage()
        LPG = perc["GAS_LPG"]
        SMOKE = perc["SMOKE"]
        CO = perc["CO"]
        db.child('MQ2_stats').update({'smoke': SMOKE, 'CO': CO, 'LPG': LPG})
 """
        # ------------ Geyser Distance ------------

        if db.child('geyser').child('reference').get().val() > 0.00:
            geyserDist = read_ultra(GEYSER_TRIG, GEYSER_ECHO)
            db.child('geyser').update({'distance': geyserDist})
            geyser_handler()

        # ------------ Temperature & Humidity ------------

        (H1, T1) = Adafruit_DHT.read_retry(ROOM1_DHT, ROOM1_DHT_PIN)
        db.child('room_stats').child('room1').update(
            {'humidity': H1, 'temperature': T1})

        # ----------------- Water Level ------------------

        if  (
                db.child('water_tank').child('ref_max').get().val() > 0.00 and 
                db.child('geyser').child('ref_min').get().val() > 0.00
            ):
            waterDist = read_ultra(WATER_TRIG, WATER_ECHO)
            db.child('water_tank').update({'distance': waterDist})

        # -------------------------------------------------------

        """ os.system('clear')
        print('-----------------: System Output :-----------------')
        print("LPG:                {} ppm".format(LPG))
        print("CO:                 {} ppm".format(CO))
        print("Smoke:              {} ppm".format(SMOKE))
        print("Temperature:        {} *C".format(T1))
        print("Humidity:           {} %".format(H1))
        print("Geyser Distance:    {} cm".format(geyserDist))
        print("Water Distance:     {} cm".format(waterDist))
 """
except KeyboardInterrupt:
    GPIO.cleanup()
    """ print('\n', RESET)
    os.system('clear') """
    sys.exit()
