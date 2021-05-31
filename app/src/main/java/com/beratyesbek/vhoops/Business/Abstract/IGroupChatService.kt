package com.beratyesbek.vhoops.Business.Abstract

import android.net.Uri
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.vhoops.entities.concrete.dtos.GroupChatDto
import com.beratyesbek.vhoops.entities.concrete.GroupChat

interface IGroupChatService : IServiceRepository<GroupChat> {

    fun getChatDetailById(id: String, iDataResult: (IDataResult<ArrayList<GroupChatDto>>) -> Unit)
    fun uploadFile(uri: Uri, type : String, result: (IDataResult<String>) -> Unit)
    fun getFile(path : String,iDataResult: (IDataResult<Uri>) -> Unit)
}