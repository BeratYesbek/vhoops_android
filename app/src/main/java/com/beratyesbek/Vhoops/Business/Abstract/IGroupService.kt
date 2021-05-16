package com.beratyesbek.Vhoops.Business.Abstract

import android.net.Uri
import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.Vhoops.Entities.Concrete.Group

interface IGroupService : IServiceRepository<Group> {
    fun uploadGroupIcon(uri : Uri, iDataResult: (IDataResult<Uri>) -> Unit)
    fun getGroupIcon(path : String, iDataResult: (IDataResult<Uri>) -> Unit)
}