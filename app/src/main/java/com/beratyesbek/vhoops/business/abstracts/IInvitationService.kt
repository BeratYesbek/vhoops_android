package com.beratyesbek.vhoops.business.abstracts

import com.beratyesbek.vhoops.core.utilities.results.Abstract.IDataResult
import com.beratyesbek.vhoops.entities.concrete.Invitation
import com.beratyesbek.vhoops.entities.concrete.dtos.InvitationDto

interface IInvitationService : IServiceRepository<Invitation> {
     fun getByUserId(id :String, iDataResult: (IDataResult<ArrayList<Invitation>>) -> Unit)
     fun getDetailsByUserId(id :String,iDataResult: (IDataResult<ArrayList<InvitationDto>>) -> Unit)

}