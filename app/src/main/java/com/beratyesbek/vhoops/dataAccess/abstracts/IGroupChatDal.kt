package com.beratyesbek.vhoops.dataAccess.abstracts

import com.beratyesbek.vhoops.core.dataAccess.abstracts.IFirebaseGroupChatDal
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IDataResult
import com.beratyesbek.vhoops.entities.concrete.dtos.GroupChatDto

interface IGroupChatDal : IFirebaseGroupChatDal {
    fun getChatDetailById(id: String, iDataResult: (IDataResult<ArrayList<GroupChatDto>>) -> Unit)

}