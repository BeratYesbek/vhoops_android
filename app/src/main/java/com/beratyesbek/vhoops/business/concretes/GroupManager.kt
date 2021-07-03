package com.beratyesbek.vhoops.business.concretes

import android.net.Uri
import com.beratyesbek.vhoops.business.abstracts.IGroupService
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IDataResult
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IResult
import com.beratyesbek.vhoops.core.utilities.results.Concrete.SuccessDataResult
import com.beratyesbek.vhoops.dataAccess.abstracts.IGroupDal
import com.beratyesbek.vhoops.entities.concrete.Group
import javax.inject.Inject

class GroupManager
@Inject
constructor(val groupDal : IGroupDal)  : IGroupService{

    override fun add(entity: Group, result: (IResult) -> Unit) {
        groupDal.add(entity,result)
    }

    override fun update(entity: Group, result: (IResult) -> Unit) {
        groupDal.update(entity,result)
    }

    override fun delete(entity: Group, result: (IResult) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getAll(iDataResult: (IDataResult<ArrayList<Group>>) -> Unit) {
       groupDal.getAll(iDataResult)
    }

    override fun getById(id: String, iDataResult: (IDataResult<ArrayList<Group>>) -> Unit) {
        groupDal.getById(id,iDataResult)
    }

    override fun uploadGroupIcon(uri: Uri, iDataResult: (IDataResult<Uri>) -> Unit) {

       groupDal.uploadGroupIcon(uri){ result->
           if (result.success()){
               groupDal.getGroupIcon(result.data()!!){ getResult ->
                   iDataResult(SuccessDataResult<Uri>(getResult.data(),getResult.message()))
               }
           }
       }
    }

    override fun getGroupIcon(path: String, iDataResult: (IDataResult<Uri>) -> Unit) {
        TODO("Not yet implemented")
    }

}