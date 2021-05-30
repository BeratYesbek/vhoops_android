package com.beratyesbek.vhoops.Business.Abstract

import android.net.Uri
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IResult
import com.beratyesbek.vhoops.entities.concrete.User

interface IUserService :IServiceRepository<User> {
    fun createUser(entity: User, result: (IResult) -> Unit)
    fun loginUser(email: String,password: String, result: (IResult) -> Unit)
    fun getByUserName(userName: String, iDataResult: (IDataResult<ArrayList<User>>) -> Unit)
    fun getByEmail(email: String, iDataResult: (IDataResult<ArrayList<User>>) -> Unit)
    fun uploadPhoto(uri: Uri, result: (IResult) -> Unit)
    fun getPhoto(userId: String, result: (IDataResult<Uri>) -> Unit)
    fun updateUserName(userName: String, documentId: String, result: (IResult) -> Unit)
    fun updateUserProfileImage(uri: Uri?,documentId: String,result: (IResult) -> Unit)
    fun removeProfileImage(result: (IResult) ->Unit);
}