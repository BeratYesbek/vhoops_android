package com.beratyesbek.vhoops.business.concretes

import android.net.Uri
import com.beratyesbek.vhoops.business.abstracts.IUserService
import com.beratyesbek.vhoops.business.rules.CustomUserRules
import com.beratyesbek.vhoops.core.utilities.business.BusinessRules
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IDataResult
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IResult
import com.beratyesbek.vhoops.core.utilities.results.Concrete.ErrorResult
import com.beratyesbek.vhoops.core.utilities.results.Concrete.SuccessResult
import com.beratyesbek.vhoops.dataAccess.abstracts.IUserDal
import com.beratyesbek.vhoops.entities.concrete.User
import javax.inject.Inject

class UserManager @Inject constructor(val userDal : IUserDal) : IUserService {


    override fun add(entity: User, result: (IResult) -> Unit) {
        val resultRules = BusinessRules.run(
            arrayOf
                (
                CustomUserRules.checkPassword(entity.password!!),
                CustomUserRules.checkUserName(entity.userName!!),
                CustomUserRules.checkProperty(entity)
            )
        )
        // check username exist with async
        if (resultRules.success()) {
            checkUserName(entity.userName!!) {
                if (it.success()) {
                    result(ErrorResult(it.message()))
                }
                else {
                    // if here is throw exception you have to apply try and catch
                    userDal.add(entity, result);
                }
            }

        } else {
            result(ErrorResult(resultRules.message()));
        }
    }

    override fun update(entity: User, result: (IResult) -> Unit) {
        val resultRules = BusinessRules.run(
            arrayOf
                (
                CustomUserRules.checkProperty(entity)
            )
        )
        if(resultRules.success()){
            userDal.update(entity,result)

        }else{
            result(ErrorResult(resultRules.message()))
        }

    }

    override fun delete(entity: User, result: (IResult) -> Unit) {
    }

    override fun getAll(iDataResult: (IDataResult<ArrayList<User>>) -> Unit) {
        println("haydaaaa 12 65")
        userDal.getAll(iDataResult);
    }

    override fun getById(id: String, iDataResult: (IDataResult<ArrayList<User>>) -> Unit) {
        userDal.getById(id,iDataResult)
    }

    override fun createUser(entity: User, result: (IResult) -> Unit) {

        userDal.createUser(entity, result)

    }

    override fun loginUser(email: String, password: String, result: (IResult) -> Unit) {
        userDal.loginUser(email,password,result)
    }

    override fun getByUserName(
        userName: String,
        iDataResult: (IDataResult<ArrayList<User>>) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun getByEmail(email: String, iDataResult: (IDataResult<ArrayList<User>>) -> Unit) {
        userDal.getByEmail(email,iDataResult)
    }

    override fun uploadPhoto(uri: Uri, result: (IResult) -> Unit) {
        userDal.uploadPhoto(uri,result)
    }

    override fun getPhoto(userId: String, result: (IDataResult<Uri>) -> Unit) {
        userDal.getPhoto(userId,result)
    }

    override fun updateUserName(userName: String, documentId: String, result: (IResult) -> Unit){
        val rules = BusinessRules.run(
            arrayOf(
                CustomUserRules.checkUserName(userName),
            )
        )
        if (rules.success()) {
            checkUserName(userName) {
                if (it.success()) {
                    result(ErrorResult(it.message()))
                }
                else {
                    // if here is throw exception you have to apply try and catch
                    userDal.updateUserName(userName,documentId,result)
                }
            }

        } else {
            result(ErrorResult(rules.message()));
        }
    }

    override fun updateUserProfileImage(uri: Uri?,documentId: String, result: (IResult) -> Unit) {

        userDal.updateUserProfileImage(uri,documentId,result)
    }

    override fun removeProfileImage(result: (IResult) ->Unit) {

        userDal.removeProfileImage(result)

    }


    fun checkUserName(userName: String, result: (IResult) -> Unit) {
        userDal.getByUserName(userName) { iDataResult ->

            if (iDataResult.success()) {
                result(SuccessResult("User has been created"))
            }else{
                result(ErrorResult("User has not been created"))
            }

        }
    }


}