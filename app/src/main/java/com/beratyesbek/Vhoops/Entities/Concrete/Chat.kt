package com.beratyesbek.Vhoops.Entities.Concrete

import com.beratyesbek.Vhoops.Entities.Abstract.IEntity
import com.google.firebase.Timestamp

class Chat(senderId: String, receiverId: String, message: String, isSeen: Boolean, timeToSend: Timestamp) : IEntity {

    constructor(senderId: String,receiverId: String,message: String,documentId: String,isSeen: Boolean,timeToSend: Timestamp)
            : this(senderId,receiverId,message,isSeen,timeToSend){
        this.documentId = documentId
    }

    private lateinit var documentId: String
        get

    var isSeen: Boolean = isSeen
        get

    var senderId: String = senderId
        get

    var receiverId: String = receiverId
        get

    var message: String = message
        get

    var timeToSend: Timestamp = timeToSend
        get

}