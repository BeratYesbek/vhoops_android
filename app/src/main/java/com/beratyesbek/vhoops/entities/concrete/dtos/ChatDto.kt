package com.beratyesbek.vhoops.entities.concrete.dtos

import android.net.Uri
import com.beratyesbek.vhoops.entities.abstracts.IDto
import com.google.firebase.Timestamp

class ChatDto(
    senderId: String, receiverId: String, message:Any,
    documentId:String, isSeen:Boolean, timeToSend:Timestamp,
    userPicture: Uri?, userFullName:String) : IDto {

    var senderId : String = senderId
        get

    var receiverId : String = receiverId
        get

    var message : Any = message
        get

    var documentId : String = documentId
        get

    var isSeen : Boolean = isSeen
        get

    var timeToSend : Timestamp = timeToSend
        get

    var userPicture : Uri? = userPicture
        get
        set

    var userFullName : String = userFullName
        get
        set

}