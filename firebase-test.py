
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore

# Use a service account
cred = credentials.Certificate('./ServiceAccountKey.json')
firebase_admin.initialize_app(cred)
db = firestore.client()
doc_ref=db.collection(u'appliances').document(u'appliance1')

while True:
    myInput=string(input("Enter a value: "))
    if myInput=="end":
        break

    doc_ref.set({
        u'test':myInput
    })
