package com.beratyesbek.vhoops.core.dataAccess.abstracts

import android.net.Uri
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IDataResult
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IResult

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