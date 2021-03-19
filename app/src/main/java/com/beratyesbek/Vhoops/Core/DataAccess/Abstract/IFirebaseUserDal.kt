package com.beratyesbek.Vhoops.Core.DataAccess.Abstract

import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IResult

interface IFirebaseUserDal<T> {
    fun createUser(entity: T, result: (IResult) -> Unit)
    fun loginUser(entity: T, result: (IResult) -> Unit)
    fun getByUserName(userName: String, iDataResult: (IDataResult<ArrayList<T>>) -> Unit)
}