package com.beratyesbek.vhoops.entities.concrete

import com.beratyesbek.vhoops.entities.abstracts.IEntity

class Fellow : IEntity {

    constructor(userId: String, addedUserId: String, status: Boolean) : this(userId, status) {
        this.addedUserId = addedUserId
    }

    constructor(userId: String, status: Boolean) {
        this.userId = userId
        this.status = status
    }

    val userId: String
        get

    lateinit var addedUserId: String
        get

    val status: Boolean
        get;
}