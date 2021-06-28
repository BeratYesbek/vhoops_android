package com.beratyesbek.vhoops.DataAccess.Abstract

import com.beratyesbek.vhoops.Core.DataAccess.Abstract.IFirebaseInvitationDal
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.vhoops.entities.concrete.Invitation
import com.beratyesbek.vhoops.entities.concrete.dtos.InvitationDto

interface IInvitationDal : IFirebaseInvitationDal {
    fun getDetailsByUserId(id :String,iDataResult: (IDataResult<ArrayList<InvitationDto>>) -> Unit)

}