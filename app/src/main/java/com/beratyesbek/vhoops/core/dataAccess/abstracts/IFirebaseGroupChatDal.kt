package com.beratyesbek.vhoops.core.dataAccess.abstracts

import android.net.Uri
import com.beratyesbek.vhoops.core.dataAccess.IEntityRepository
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IDataResult
import com.beratyesbek.vhoops.entities.concrete.GroupChat

interface IFirebaseGroupChatDal : IEntityRepository<GroupChat> {
    fun uploadFile(uri: Uri, type : String, result: (IDataResult<String>) -> Unit)
    fun getFile(path : String,iDataResult: (IDataResult<Uri>) -> Unit)
}