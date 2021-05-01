package com.beratyesbek.Vhoops.DataAccess.Abstract

import com.beratyesbek.Vhoops.Core.DataAccess.Abstract.IFirebaseChatDal
import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.Vhoops.Entities.Concrete.Chat
import com.beratyesbek.Vhoops.Entities.Concrete.Dtos.ChatDto

interface IChatDal : IFirebaseChatDal<Chat> {

    fun getChatDetail(id :String,iDataResult: (IDataResult<ArrayList<ChatDto>>) -> Unit)

}