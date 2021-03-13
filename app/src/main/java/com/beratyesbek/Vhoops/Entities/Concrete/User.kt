package com.beratyesbek.Vhoops.Entities.Concrete

import com.beratyesbek.Vhoops.Entities.Concrete.Person

class User(firstName: String, lastName: String, email: String, userID: String, userUUID: String) :
    Person(firstName, lastName, email, userID, userUUID) {

}