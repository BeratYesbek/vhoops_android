package com.beratyesbek.vhoops.entities.concrete

import com.beratyesbek.vhoops.entities.abstracts.IEntity
import com.google.firebase.Timestamp

class FriendRequest(
    senderId: String,
    receiverId: String,
    documentId: String,
    status: Boolean,
    timeToSend: Timestamp
) : IEntity {

    val senderId: String = senderId
        get

    val receiverId: String = receiverId
        get

    val documentId: String = documentId
        get

    var status: Boolean = status
        get
        set

    var timeToSend: Timestamp = timeToSend
        get
        set
}