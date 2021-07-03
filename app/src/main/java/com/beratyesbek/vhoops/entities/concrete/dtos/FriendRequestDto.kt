package com.beratyesbek.vhoops.entities.concrete.dtos

import android.net.Uri
import com.beratyesbek.vhoops.entities.abstracts.IDto
import com.google.firebase.Timestamp

class FriendRequestDto(
    senderId: String,
    receiverId: String,
    documentId: String,
    status: Boolean,
    timeToSend: Timestamp,
    userFirstName: String?,
    userLastName: String?,
    userName: String?,
    userImage: Uri?,
    userId: String?
) : IDto {

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
    var userFirstName: String? = userFirstName
        get
        set
    var userLastName: String? = userLastName
        get
        set
    var userName: String? = userName
        get
        set
    var userImage: Uri? = userImage
        get
        set
    var userId: String? = userId
        get
        set


}