package com.beratyesbek.Vhoops.Business.Abstract

import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.Vhoops.Entities.Concrete.Chat
import com.beratyesbek.Vhoops.Entities.Concrete.Dtos.ChatDto

interface IChatService : IServiceRepository<Chat> {
    fun getChatDetail(id :String,iDataResult: (IDataResult<ArrayList<ChatDto>>) -> Unit)

}