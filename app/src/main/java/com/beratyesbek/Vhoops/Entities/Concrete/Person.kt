package com.beratyesbek.vhoops.entities.concrete

import com.beratyesbek.vhoops.entities.abstracts.IEntity

open class Person(firstName: String, lastName: String, email: String):IEntity {


    var firstName: String? = firstName
        get
        set;
    var lastName: String? = lastName
        get
        set;

    var email: String? = email
        get
        set;



}