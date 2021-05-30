package com.beratyesbek.vhoops.DataAccess.Abstract

import com.beratyesbek.vhoops.Core.DataAccess.Abstract.IFirebaseGroupChatDal
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.vhoops.entities.concrete.dtos.GroupChatDto

interface IGroupChatDal : IFirebaseGroupChatDal {
    fun getChatDetailById(id: String, iDataResult: (IDataResult<ArrayList<GroupChatDto>>) -> Unit)

}