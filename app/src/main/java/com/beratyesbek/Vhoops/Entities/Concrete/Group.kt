package com.beratyesbek.Vhoops.Entities.Concrete

import android.net.Uri
import com.beratyesbek.Vhoops.Entities.Abstract.IEntity
import com.google.firebase.Timestamp

class Group(
    groupId: String?,
    groupName:String?,
    adminId : String?,
    groupImage : Uri?,
    documentId: String?,
    memberIdList: ArrayList<String>,
    createdDate: Timestamp
) : IEntity {

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