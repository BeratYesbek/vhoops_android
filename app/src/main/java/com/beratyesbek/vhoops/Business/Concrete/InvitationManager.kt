package com.beratyesbek.vhoops.Business.Concrete

import com.beratyesbek.vhoops.Business.Abstract.IInvitationService
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IResult
import com.beratyesbek.vhoops.DataAccess.Abstract.IChatDal
import com.beratyesbek.vhoops.DataAccess.Abstract.IInvitationDal
import com.beratyesbek.vhoops.entities.concrete.Invitation
import com.beratyesbek.vhoops.entities.concrete.dtos.InvitationDto
import javax.inject.Inject

class InvitationManager @Inject constructor(val invitationDal : IInvitationDal) : IInvitationService {

    override fun add(entity: Invitation, result: (IResult) -> Unit) {
        invitationDal.add(entity,result)
    }
    override fun getByUserId(
        id: String,
        iDataResult: (IDataResult<ArrayList<Invitation>>) -> Unit
    ) {
        invitationDal.getByUserId(id,iDataResult)
    }

    override fun getDetailsByUserId(
        id: String,
        iDataResult: (IDataResult<ArrayList<InvitationDto>>) -> Unit
    ) {
        invitationDal.getDetailsByUserId(id,iDataResult)
    }


    override fun update(entity: Invitation, result: (IResult) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun delete(entity: Invitation, result: (IResult) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getAll(iDataResult: (IDataResult<ArrayList<Invitation>>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getById(id: String, iDataResult: (IDataResult<ArrayList<Invitation>>) -> Unit) {
        TODO("Not yet implemented")
    }
}