package com.beratyesbek.Vhoops.Entities.Concrete

import com.beratyesbek.Vhoops.Entities.Abstract.IEntity
import org.jetbrains.annotations.NotNull

open class Person(firstName: String, lastName: String, email: String):IEntity {


    var firstName: String? = firstName
        get
        private set;
    var lastName: String? = lastName
        get
        private set;

    var email: String? = email
        get
        private set;




}