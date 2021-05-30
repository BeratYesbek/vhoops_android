package com.beratyesbek.vhoops.entities.concrete.dtos



import android.net.Uri
import com.beratyesbek.vhoops.entities.abstracts.IDto
import com.google.firebase.Timestamp

data class GroupChatDto(
    var senderId: String, var groupId : String, var message: Any, var isSeen: Boolean,
    var timeToSend: Timestamp, var senderImage : Uri?, var senderFullName :String?) : IDto {

}