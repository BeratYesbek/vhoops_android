package com.beratyesbek.vhoops.entities.concrete

import com.beratyesbek.vhoops.entities.abstracts.IEntity
import com.google.firebase.Timestamp

class Chat(senderId: String, receiverId: String, message: Any, isSeen: Boolean, timeToSend: Timestamp) : IEntity {

    constructor(senderId: String,receiverId: String,message: Any,documentId: String,isSeen: Boolean,timeToSend: Timestamp)
            : this(senderId,receiverId,message,isSeen,timeToSend){
        this.documentId = documentId
    }

    lateinit var documentId: String
        get

    var isSeen: Boolean = isSeen
        get

    var senderId: String = senderId
        get

    var receiverId: String = receiverId
        get

    var message: Any = message
        get

    var timeToSend: Timestamp = timeToSend
        get

}