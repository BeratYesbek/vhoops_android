package com.beratyesbek.vhoops.Core.DataAccess.Abstract

import com.beratyesbek.vhoops.Core.DataAccess.IEntityRepository
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.vhoops.entities.concrete.Invitation

interface IFirebaseInvitationDal : IEntityRepository<Invitation> {
    fun getByUserId(id :String,iDataResult: (IDataResult<ArrayList<Invitation>>) -> Unit)
}