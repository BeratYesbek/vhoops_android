package com.beratyesbek.vhoops.entities.concrete

import android.net.Uri
import com.beratyesbek.vhoops.entities.abstracts.IEntity
import com.google.firebase.Timestamp

class Group(
    groupId: String?,
    groupName:String?,
    description : String?,
    adminId : String?,
    groupImage : Uri?,
    documentId: String?,
    memberIdList: ArrayList<String>,
    createdDate: Timestamp
) : IEntity  {

    val groupId: String? = groupId
        get
    var groupName: String? = groupName
        get
        set
    var groupDescription :String? = description
        get
        set

    val adminId: String? = adminId
        get
    var groupImage: Uri? = groupImage
        get
        set
    val documentId: String? = documentId
        get
    val memberIdList: ArrayList<String>? = memberIdList
        get
    val createdDate: Timestamp? = createdDate
        get

}