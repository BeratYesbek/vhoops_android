package com.beratyesbek.vhoops.dataAccess.abstracts

import com.beratyesbek.vhoops.core.dataAccess.abstracts.IFirebaseFriendRequestDal
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IDataResult
import com.beratyesbek.vhoops.entities.concrete.FriendRequest
import com.beratyesbek.vhoops.entities.concrete.dtos.FriendRequestDto

interface IFriendRequestDal : IFirebaseFriendRequestDal<FriendRequest>{
   fun getFriendRequestDetailsByUserId(id: String,iDataResult: (IDataResult<ArrayList<FriendRequestDto>>) -> Unit)
}