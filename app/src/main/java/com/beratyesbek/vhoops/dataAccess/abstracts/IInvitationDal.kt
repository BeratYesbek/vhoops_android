package com.beratyesbek.vhoops.dataAccess.abstracts

import com.beratyesbek.vhoops.core.dataAccess.abstracts.IFirebaseInvitationDal
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IDataResult
import com.beratyesbek.vhoops.entities.concrete.dtos.InvitationDto

interface IInvitationDal : IFirebaseInvitationDal {
    fun getDetailsByUserId(id :String,iDataResult: (IDataResult<ArrayList<InvitationDto>>) -> Unit)

}