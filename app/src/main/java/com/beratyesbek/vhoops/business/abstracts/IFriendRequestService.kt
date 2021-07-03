package com.beratyesbek.vhoops.business.abstracts

import com.beratyesbek.vhoops.core.utilities.results.Abstract.IDataResult
import com.beratyesbek.vhoops.entities.concrete.FriendRequest
import com.beratyesbek.vhoops.entities.concrete.dtos.FriendRequestDto

interface IFriendRequestService : IServiceRepository<FriendRequest> {
    fun getBySenderAndReceiverId(senderId : String,receiverId : String,iDataResult: (IDataResult<FriendRequest>) -> Unit)
    fun getFriendRequestDetailsByUserId(id: String,iDataResult: (IDataResult<ArrayList<FriendRequestDto>>) -> Unit)

}