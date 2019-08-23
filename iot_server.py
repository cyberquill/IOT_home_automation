import pyrebase
import Adafruit_DHT

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
power = 'on'

# DHT-11 Sensor is connected to GPIO4
sensor = 11
pin1 = 4

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

main_stream = db.stream(mainStream_handler)
geyser_stream = db.child('geyser').stream(geyserStream_handler)
room1_stream = db.child('room_stats').child('room1').stream(room1Stream_handler)

#===================================: Main Loop :====================================

while power=='on':
    #------------ Gas Leak ------------
    pass
    #------------ Geyser Distance ------------
    pass
    #------------ Temperature & Humidity ------------
    (H1, T1) = Adafruit_DHT.read_retry(sensor, pin1)
    db.child('room_stats').child('room1').update({'humidity': H1, 'temperature': T1})
    print('Humidity={1:0.1f}%\tTemp={0:0.1f}*C'.format(H1, T1))
    #------------ Water Level ------------
    pass
    print("-------------------------------------------------------")
