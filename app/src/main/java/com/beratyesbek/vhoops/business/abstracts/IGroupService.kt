package com.beratyesbek.vhoops.business.abstracts

import android.net.Uri
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IDataResult
import com.beratyesbek.vhoops.entities.concrete.Group

interface IGroupService : IServiceRepository<Group> {
    fun uploadGroupIcon(uri : Uri, iDataResult: (IDataResult<Uri>) -> Unit)
    fun getGroupIcon(path : String, iDataResult: (IDataResult<Uri>) -> Unit)
}