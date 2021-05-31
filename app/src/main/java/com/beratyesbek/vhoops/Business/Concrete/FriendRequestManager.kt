package com.beratyesbek.vhoops.Business.Concrete

import com.beratyesbek.vhoops.Business.Abstract.IFriendRequestService
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IResult
import com.beratyesbek.vhoops.DataAccess.Abstract.IFriendRequestDal
import com.beratyesbek.vhoops.entities.concrete.FriendRequest

class FriendRequestManager(friendRequestDal : IFriendRequestDal) : IFriendRequestService {

    private val _friendRequestDal: IFriendRequestDal = friendRequestDal


    override fun add(entity: FriendRequest, result: (IResult) -> Unit) {
        _friendRequestDal.add(entity,result)
    }

    override fun update(entity: FriendRequest, result: (IResult) -> Unit) {
        _friendRequestDal.add(entity,result)
    }

    override fun delete(entity: FriendRequest, result: (IResult) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getAll(iDataResult: (IDataResult<ArrayList<FriendRequest>>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getById(id: String, iDataResult: (IDataResult<ArrayList<FriendRequest>>) -> Unit) {
        _friendRequestDal.getById(id,iDataResult);
    }
}