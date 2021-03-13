package com.beratyesbek.Vhoops.Entities.Concrete

import com.beratyesbek.Vhoops.Entities.Abstract.IEntity

open class Person(firstName: String, lastName: String, email: String, userID: String, userUUID: String):IEntity {

    var firstName: String? = firstName
        get
        private set;
    var lastName: String? = lastName
        get
        private set;

    var email: String? = email
        get
        private set;

    var userID: String? = userID
        get
        private set;

    var userUUID: String? = userUUID
        get
        private set;


}