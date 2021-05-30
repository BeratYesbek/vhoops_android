package com.beratyesbek.vhoops.Core.DataAccess.Abstract

import android.net.Uri
import com.beratyesbek.vhoops.Core.DataAccess.IEntityRepository
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IResult
import com.beratyesbek.vhoops.entities.abstracts.IEntity
import com.beratyesbek.vhoops.entities.concrete.Chat

interface IFirebaseChatDal<T : IEntity> : IEntityRepository<T> {

    fun uploadFile(uri: Uri, type: String, result: (IDataResult<String>) -> Unit)
    fun getFile(path: String, iDataResult: (IDataResult<Uri>) -> Unit)

    fun deleteMulti(arrayList:ArrayList<Chat>,result: (IResult) -> Unit)
}