package com.beratyesbek.vhoops.Business.Abstract

import android.net.Uri
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.vhoops.entities.concrete.Group

interface IGroupService : IServiceRepository<Group> {
    fun uploadGroupIcon(uri : Uri, iDataResult: (IDataResult<Uri>) -> Unit)
    fun getGroupIcon(path : String, iDataResult: (IDataResult<Uri>) -> Unit)
}