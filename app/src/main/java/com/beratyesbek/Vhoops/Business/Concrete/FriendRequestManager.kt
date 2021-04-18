package com.beratyesbek.Vhoops.Business.Concrete

import com.beratyesbek.Vhoops.Business.Abstract.IFriendRequestService
import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IResult
import com.beratyesbek.Vhoops.DataAccess.Abstract.IFriendRequestDal
import com.beratyesbek.Vhoops.Entities.Concrete.FriendRequest

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