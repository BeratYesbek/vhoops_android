package com.beratyesbek.vhoops.DataAccess.Abstract

import com.beratyesbek.vhoops.Core.DataAccess.Abstract.IFirebaseChatDal
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.vhoops.entities.concrete.Chat
import com.beratyesbek.vhoops.entities.concrete.dtos.ChatDto
import com.beratyesbek.vhoops.entities.concrete.dtos.ChatListDto

interface IChatDal : IFirebaseChatDal<Chat> {

    fun getChatDetail(id :String,iDataResult: (IDataResult<ArrayList<ChatDto>>) -> Unit)
    fun getChatDetailForList(id: String,iDataResult: (IDataResult<ArrayList<ChatListDto>>) -> Unit)
}