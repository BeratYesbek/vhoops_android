package com.beratyesbek.vhoops.entities.concrete

import android.net.Uri
import com.beratyesbek.vhoops.entities.abstracts.IEntity

class User(firstName: String, lastName: String, email: String, userName: String, password: String) :
    Person(firstName, lastName, email), IEntity{


    constructor(firstName: String, lastName: String, email: String, userName: String, password: String,userID: String, token: String, profileImage: Uri?,documentID : String,about:String?) : this(firstName, lastName, email, userID, password) {

        this.userName = userName
        this.userID = userID
        this.token = token
        this.documentID = documentID
        this.profileImage = profileImage
        this.about = about
    }

    var password: String? = password
        get
        set

    var userName: String? = userName
        get
        private set
    var about : String? = null
        get
        set

    var profileImage : Uri? = null
        get
        set

    lateinit var userID: String
        get
        set

    lateinit var token: String
        get
        set


    lateinit var documentID: String
        get
        set



}

