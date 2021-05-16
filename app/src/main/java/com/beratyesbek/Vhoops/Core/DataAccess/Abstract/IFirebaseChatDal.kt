package com.beratyesbek.Vhoops.Core.DataAccess.Abstract

import android.net.Uri
import com.beratyesbek.Vhoops.Core.DataAccess.IEntityRepository
import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IResult
import com.beratyesbek.Vhoops.Entities.Abstract.IEntity
import com.beratyesbek.Vhoops.Entities.Concrete.Chat

interface IFirebaseChatDal<T : IEntity> : IEntityRepository<T> {

    fun uploadFile(uri: Uri, type: String, result: (IDataResult<String>) -> Unit)
    fun getFile(path: String, iDataResult: (IDataResult<Uri>) -> Unit)

    fun deleteMulti(arrayList:ArrayList<Chat>,result: (IResult) -> Unit)
}