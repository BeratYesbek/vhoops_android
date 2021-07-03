package com.beratyesbek.vhoops.core.dataAccess.abstracts

import com.beratyesbek.vhoops.core.dataAccess.IEntityRepository
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IDataResult
import com.beratyesbek.vhoops.entities.abstracts.IEntity
import com.beratyesbek.vhoops.entities.concrete.FriendRequest

interface IFirebaseFriendRequestDal<T: IEntity> : IEntityRepository<T>{
    fun getBySenderAndReceiverId(senderId : String,receiverId : String,iDataResult: (IDataResult<FriendRequest>) -> Unit)
}