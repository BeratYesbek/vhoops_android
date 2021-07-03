package com.beratyesbek.vhoops.business.concretes

import com.beratyesbek.vhoops.business.abstracts.IFellowService
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IDataResult
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IResult
import com.beratyesbek.vhoops.dataAccess.abstracts.IFellowDal
import com.beratyesbek.vhoops.entities.concrete.Fellow
import javax.inject.Inject

class FellowManager
@Inject constructor(val fellowDal: IFellowDal) : IFellowService {

    override fun getByUserId(userId: String, iDataResult: (IDataResult<Fellow>) -> Unit) {
        fellowDal.getByUserId(userId,iDataResult)
    }

    override fun add(entity: Fellow, result: (IResult) -> Unit) {

        fellowDal.add(entity,result);

    }

    override fun update(entity: Fellow, result: (IResult) -> Unit) {
        fellowDal.update(entity,result)
    }

    override fun delete(entity: Fellow, result: (IResult) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getAll(iDataResult: (IDataResult<ArrayList<Fellow>>) -> Unit) {
        TODO("Not yet implemented")
    }
    override fun getById(id: String, iDataResult: (IDataResult<ArrayList<Fellow>>) -> Unit) {
        fellowDal.getById(id,iDataResult);
    }
}