package com.beratyesbek.vhoops.entities.concrete

import com.beratyesbek.vhoops.entities.abstracts.IEntity
import java.io.Serializable

open class Person(firstName: String, lastName: String, email: String):IEntity ,Serializable{


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