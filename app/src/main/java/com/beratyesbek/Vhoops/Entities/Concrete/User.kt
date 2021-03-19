package com.beratyesbek.Vhoops.Entities.Concrete

import android.net.Uri
import com.beratyesbek.Vhoops.Entities.Abstract.IEntity
import com.beratyesbek.Vhoops.Entities.Concrete.Person

class User(firstName: String, lastName: String, email: String, userName: String, password: String) :
    Person(firstName, lastName, email), IEntity {


    constructor(
        firstName: String,
        lastName: String,
        email: String,
        userName: String,
        password: String,
        userID: String,
        token: String,
        profileImage: Uri?,
    )
            : this(firstName, lastName, email, userID, password) {
        this.userName = userName;
        this.userID = userID;
        this.token = token
        if (profileImage != null) {
            this.profileImage = profileImage
        }

    }

    var password: String? = password
        get
        private set

    var userName: String? = userName
        get
        private set

    lateinit var userID: String
        get
        private set

    lateinit var token: String
        get
        private set

    lateinit var profileImage: Uri
        get
        private set


}

