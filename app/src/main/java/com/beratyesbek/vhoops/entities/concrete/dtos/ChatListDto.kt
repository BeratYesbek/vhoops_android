package com.beratyesbek.vhoops.entities.concrete.dtos

import android.net.Uri
import com.beratyesbek.vhoops.entities.abstracts.IDto
import com.google.firebase.Timestamp

class ChatListDto(
    senderId: String,
    receiverId: String,
    lastMessage: Any,
    documentId: String,
    isSeen: Boolean,
    lastMessageTime: Timestamp,
    unSeenMessageCount: Number,
    userPicture: Uri?,
    userFirstName: String?,
    userLastName :String?,
    userId:String?,
    userToken : String?
) : IDto {


    var senderId: String = senderId
        get

    var receiverId: String = receiverId
        get

    var lastMessage: Any = lastMessage
        get

    var documentId: String = documentId
        get

    var isSeen: Boolean = isSeen
        get

    var lastMessageTime: Timestamp = lastMessageTime
        get

    var userPicture: Uri? = userPicture
        get
        set

    var unSeenMessageCount: Number? = unSeenMessageCount
        get

    var userFirstName: String? = userFirstName
        get
        set
    var userLastName: String? = userLastName
        get
        set


    var userId : String? = userId
        get
        set

    var userToken : String? = userToken
        get
        set
}