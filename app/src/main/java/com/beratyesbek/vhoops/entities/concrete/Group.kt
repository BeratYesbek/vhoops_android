package com.beratyesbek.vhoops.entities.concrete

import android.net.Uri
import com.beratyesbek.vhoops.entities.abstracts.IEntity
import com.google.firebase.Timestamp

class Group(
    groupId: String?,
    groupName:String?,
    adminId : String?,
    groupImage : Uri?,
    documentId: String?,
    memberIdList: ArrayList<String>,
    createdDate: Timestamp
) : IEntity  {

    val groupId: String? = groupId
        get
    val groupName: String? = groupName
        get
    val adminId: String? = adminId
        get
    val groupImage: Uri? = groupImage
        get
    val documentId: String? = documentId
        get
    val memberIdList: ArrayList<String>? = memberIdList
        get
    val createdDate: Timestamp? = createdDate
        get

}