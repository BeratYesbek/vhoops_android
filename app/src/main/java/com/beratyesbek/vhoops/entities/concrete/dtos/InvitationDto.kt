package com.beratyesbek.vhoops.entities.concrete.dtos

import android.net.Uri
import com.beratyesbek.vhoops.entities.abstracts.IDto
import com.google.firebase.Timestamp

class InvitationDto(
    senderId: String?,
    receiverId: String?,
    invitationToDate: Timestamp?,
    type: String?,
    userPicture: Uri?,
    firstName: String?,
    lastName: String?,
    userId: String?,
    userToken: String?
) : IDto {
    var senderId: String? = senderId
        get
    var receiverId: String? = receiverId
        get
    var invitationToDate: Timestamp? = invitationToDate
        get
    var type: String? = type
        get
    var userPicture: Uri? = userPicture
        get
        set
    var firstName: String? = firstName
        get
        set
    var lastName: String? = lastName
        get
        set
    var userId: String? = userId
        get
        set
    var userToken: String? = userId
        get
        set

}