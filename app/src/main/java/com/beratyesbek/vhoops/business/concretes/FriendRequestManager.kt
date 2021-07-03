package com.beratyesbek.vhoops.business.concretes

import com.beratyesbek.vhoops.business.abstracts.IFriendRequestService
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IDataResult
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IResult
import com.beratyesbek.vhoops.dataAccess.abstracts.IFriendRequestDal
import com.beratyesbek.vhoops.entities.concrete.FriendRequest
import com.beratyesbek.vhoops.entities.concrete.dtos.FriendRequestDto
import javax.inject.Inject

class FriendRequestManager @Inject constructor(
    val friendRequestDal: IFriendRequestDal) :
    IFriendRequestService {

    override fun getBySenderAndReceiverId(
        senderId: String,
        receiverId: String,
        iDataResult: (IDataResult<FriendRequest>) -> Unit
    ) {
        friendRequestDal.getBySenderAndReceiverId(senderId, receiverId, iDataResult)
    }

    override fun getFriendRequestDetailsByUserId(
        id: String,
        iDataResult: (IDataResult<ArrayList<FriendRequestDto>>) -> Unit
    ) {
        friendRequestDal.getFriendRequestDetailsByUserId(id,iDataResult)
    }


    override fun add(entity: FriendRequest, result: (IResult) -> Unit) {
        friendRequestDal.add(entity, result)
    }

    override fun update(entity: FriendRequest, result: (IResult) -> Unit) {
        friendRequestDal.add(entity, result)
    }

    override fun delete(entity: FriendRequest, result: (IResult) -> Unit) {
        friendRequestDal.delete(entity, result)
    }

    override fun getAll(iDataResult: (IDataResult<ArrayList<FriendRequest>>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getById(id: String, iDataResult: (IDataResult<ArrayList<FriendRequest>>) -> Unit) {
        friendRequestDal.getById(id, iDataResult);
    }
}