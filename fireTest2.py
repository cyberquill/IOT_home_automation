from firebase import firebase

firebase = firebase.FirebaseApplication('https://home-automation-iot-2019.firebaseio.com/', None)
data = {'Name': 'John Doe',
        'RollNo': 3,
        'Percentage': 70.02
        }
result = firebase.post('/test', data)
print(result)
