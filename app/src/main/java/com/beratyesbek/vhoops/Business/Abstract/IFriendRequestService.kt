package com.beratyesbek.vhoops.Business.Abstract

import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.vhoops.entities.concrete.FriendRequest

interface IFriendRequestService : IServiceRepository<FriendRequest> {
    fun getBySenderAndReceiverId(senderId : String,receiverId : String,iDataResult: (IDataResult<FriendRequest>) -> Unit)

}