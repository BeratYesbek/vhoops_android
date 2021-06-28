package com.beratyesbek.vhoops.entities.concrete

import com.beratyesbek.vhoops.entities.abstracts.IEntity
import com.google.firebase.Timestamp

// this type is inComing or outGoing
class Invitation(senderId : String?,receiverId :String?,invitationToDate: Timestamp?,type : String?) : IEntity {

    val senderId :String? = senderId
        get

    val receiverId :String? = receiverId
        get
    val invitationToDate :Timestamp? = invitationToDate
        get

    val type :String? = type
        get


}