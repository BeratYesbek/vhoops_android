package com.beratyesbek.vhoops.core.dataAccess.abstracts

import android.net.Uri
import com.beratyesbek.vhoops.core.dataAccess.IEntityRepository
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IDataResult
import com.beratyesbek.vhoops.entities.concrete.Group

interface IFirebaseGroupDal  : IEntityRepository<Group>{

    fun uploadGroupIcon(uri : Uri, iDataResult: (IDataResult<String>) -> Unit)
    fun getGroupIcon(path : String, iDataResult: (IDataResult<Uri>) -> Unit)

}