package com.beratyesbek.vhoops.Core.DataAccess.Abstract

import android.net.Uri
import com.beratyesbek.vhoops.Core.DataAccess.IEntityRepository
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.vhoops.entities.concrete.Group

interface IFirebaseGroupDal  : IEntityRepository<Group>{

    fun uploadGroupIcon(uri : Uri, iDataResult: (IDataResult<String>) -> Unit)
    fun getGroupIcon(path : String, iDataResult: (IDataResult<Uri>) -> Unit)

}