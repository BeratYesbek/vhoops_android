package com.beratyesbek.vhoops.core.dataAccess.abstracts

import com.beratyesbek.vhoops.core.dataAccess.IEntityRepository
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IDataResult
import com.beratyesbek.vhoops.entities.concrete.Invitation

interface IFirebaseInvitationDal : IEntityRepository<Invitation> {
    fun getByUserId(id :String,iDataResult: (IDataResult<ArrayList<Invitation>>) -> Unit)
}