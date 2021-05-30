package com.beratyesbek.vhoops.entities.concrete

import com.beratyesbek.vhoops.entities.abstracts.IEntity
import com.google.firebase.Timestamp

class GroupChat(senderId: String,groupId : String ,message: Any, isSeen: Boolean, timeToSend: Timestamp) : IEntity {


    val senderId: String? = senderId
        get


    val message: Any? = message
        get


    val isSeen: Boolean? = isSeen
        get


    val timeToSend: Timestamp? = timeToSend
        get

    val groupId: String? = groupId
        get


}