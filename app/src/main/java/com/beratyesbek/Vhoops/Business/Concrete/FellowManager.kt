package com.beratyesbek.Vhoops.Business.Concrete

import com.beratyesbek.Vhoops.Business.Abstract.IFellowService
import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IResult
import com.beratyesbek.Vhoops.DataAccess.Concrete.FellowDal
import com.beratyesbek.Vhoops.Entities.Concrete.Fellow

class FellowManager(val fellowDal: FellowDal) : IFellowService {

    private val _fellowDal : FellowDal = fellowDal;
    override fun add(entity: Fellow, result: (IResult) -> Unit) {

       _fellowDal.add(entity,result);

    }

    override fun update(entity: Fellow, result: (IResult) -> Unit) {
        _fellowDal.update(entity,result)
    }

    override fun delete(entity: Fellow, result: (IResult) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getAll(iDataResult: (IDataResult<ArrayList<Fellow>>) -> Unit) {
        TODO("Not yet implemented")
    }
    override fun getById(id: String, iDataResult: (IDataResult<ArrayList<Fellow>>) -> Unit) {
       _fellowDal.getById(id,iDataResult);
    }
}