package com.beratyesbek.Vhoops.Entities.Concrete.Dtos

import android.net.Uri
import com.beratyesbek.Vhoops.Entities.Abstract.IDto
import com.google.firebase.Timestamp

class ChatDto(
    senderId: String, receiverId: String, message:String,
    documentId:String, isSeen:Boolean, timeToSend:Timestamp,
    userPicture: Uri?, userFullName:String) : IDto {

    var senderId : String = senderId
        get

    var receiverId : String = receiverId
        get

    var message : String = message
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