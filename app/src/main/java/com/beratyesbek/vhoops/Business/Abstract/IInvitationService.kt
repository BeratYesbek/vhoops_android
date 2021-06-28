package com.beratyesbek.vhoops.Business.Abstract

import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.vhoops.entities.concrete.Invitation
import com.beratyesbek.vhoops.entities.concrete.dtos.InvitationDto

interface IInvitationService : IServiceRepository<Invitation> {
     fun getByUserId(id :String, iDataResult: (IDataResult<ArrayList<Invitation>>) -> Unit)
     fun getDetailsByUserId(id :String,iDataResult: (IDataResult<ArrayList<InvitationDto>>) -> Unit)

}