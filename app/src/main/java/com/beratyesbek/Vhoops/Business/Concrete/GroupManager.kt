package com.beratyesbek.Vhoops.Business.Concrete

import android.net.Uri
import com.beratyesbek.Vhoops.Business.Abstract.IGroupService
import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Concrete.SuccessDataResult
import com.beratyesbek.Vhoops.DataAccess.Abstract.IGroupDal
import com.beratyesbek.Vhoops.Entities.Concrete.Group

class GroupManager(val groupDal : IGroupDal)  : IGroupService{

    override fun add(entity: Group, result: (IResult) -> Unit) {
        groupDal.add(entity,result)
    }

    override fun update(entity: Group, result: (IResult) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun delete(entity: Group, result: (IResult) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getAll(iDataResult: (IDataResult<ArrayList<Group>>) -> Unit) {
       groupDal.getAll(iDataResult)
    }

    override fun getById(id: String, iDataResult: (IDataResult<ArrayList<Group>>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun uploadGroupIcon(uri: Uri, iDataResult: (IDataResult<Uri>) -> Unit) {

       groupDal.uploadGroupIcon(uri){ result->
           if (result.success()){
               groupDal.getGroupIcon(result.data()){ getResult ->
                   iDataResult(SuccessDataResult<Uri>(getResult.data(),getResult.message()))
               }
           }
       }
    }

    override fun getGroupIcon(path: String, iDataResult: (IDataResult<Uri>) -> Unit) {
        TODO("Not yet implemented")
    }

}