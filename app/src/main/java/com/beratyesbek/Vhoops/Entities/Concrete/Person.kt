package com.beratyesbek.Vhoops.Entities.Concrete

import android.os.Parcel
import android.os.Parcelable
import com.beratyesbek.Vhoops.Entities.Abstract.IEntity
import org.jetbrains.annotations.NotNull
import java.io.Serializable

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