import pyrebase

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
# Get a reference to the database service
db = firebase.database()

# data to save
data = {
    "name": 'Adam'
}

#-----------------set new data-------------------
results = db.child('user').set(data)
#--------set new data or update existing---------
results = db.child('user').set(data)
#-----------------remove data--------------------
results = db.child('user').remove()
#------------------------------------------------
print("done")
