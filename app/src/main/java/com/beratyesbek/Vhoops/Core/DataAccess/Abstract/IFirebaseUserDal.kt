package com.beratyesbek.Vhoops.Core.DataAccess.Abstract

import android.net.Uri
import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IResult

interface IFirebaseUserDal<T> {
    fun createUser(entity: T, result: (IResult) -> Unit)
    fun loginUser(email: String,password: String, result: (IResult) -> Unit)
    fun getByUserName(userName: String, iDataResult: (IDataResult<ArrayList<T>>) -> Unit)
    fun getByEmail(email: String, iDataResult: (IDataResult<ArrayList<T>>) -> Unit)
    fun uploadPhoto(uri: Uri, result: (IResult) -> Unit)
    fun getPhoto(userId: String, result: (IDataResult<Uri>) -> Unit)
    fun updateUserProfileImage(uri: Uri?,documentId: String,result : (IResult) -> Unit)
    fun updateUserName(userName: String, documentId: String, result: (IResult) -> Unit)
    fun verificationEmail(email: String,result: (IResult) -> Unit)
    fun removeProfileImage(result :(IResult) -> Unit)
}