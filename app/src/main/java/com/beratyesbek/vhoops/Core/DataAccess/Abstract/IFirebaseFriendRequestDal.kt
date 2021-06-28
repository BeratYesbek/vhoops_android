package com.beratyesbek.vhoops.Core.DataAccess.Abstract

import com.beratyesbek.vhoops.Core.DataAccess.IEntityRepository
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.vhoops.entities.abstracts.IEntity
import com.beratyesbek.vhoops.entities.concrete.FriendRequest

interface IFirebaseFriendRequestDal<T: IEntity> : IEntityRepository<T>{
    fun getBySenderAndReceiverId(senderId : String,receiverId : String,iDataResult: (IDataResult<FriendRequest>) -> Unit)
}