package com.beratyesbek.vhoops.Business.Abstract

import android.net.Uri
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IResult
import com.beratyesbek.vhoops.entities.concrete.Chat
import com.beratyesbek.vhoops.entities.concrete.dtos.ChatDto
import com.beratyesbek.vhoops.entities.concrete.dtos.ChatListDto

interface IChatService : IServiceRepository<Chat> {
    fun getChatDetail(id :String,iDataResult: (IDataResult<ArrayList<ChatDto>>) -> Unit)
    fun uploadFile(uri: Uri,type : String, result: (IDataResult<String>) -> Unit)
    fun getFile(path : String,iDataResult: (IDataResult<Uri>) -> Unit)
    fun deleteMulti(arrayList:ArrayList<Chat>,result: (IResult) -> Unit)

    fun getChatDetailForList(id: String,iDataResult: (IDataResult<ArrayList<ChatListDto>>) -> Unit)


}