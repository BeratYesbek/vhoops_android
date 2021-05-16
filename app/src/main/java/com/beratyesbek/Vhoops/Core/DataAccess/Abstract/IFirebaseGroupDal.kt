package com.beratyesbek.Vhoops.Core.DataAccess.Abstract

import android.net.Uri
import com.beratyesbek.Vhoops.Core.DataAccess.IEntityRepository
import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.Vhoops.Entities.Concrete.Group

interface IFirebaseGroupDal  : IEntityRepository<Group>{

    fun uploadGroupIcon(uri : Uri, iDataResult: (IDataResult<String>) -> Unit)
    fun getGroupIcon(path : String, iDataResult: (IDataResult<Uri>) -> Unit)

}