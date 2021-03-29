package com.beratyesbek.Vhoops.Business.Concrete

import android.net.Uri
import com.beratyesbek.Vhoops.Business.Abstract.IUserService
import com.beratyesbek.Vhoops.Business.Rules.CustomUserRules
import com.beratyesbek.Vhoops.Core.Utilities.Business.BusinessRules
import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Concrete.ErrorResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Concrete.SuccessResult
import com.beratyesbek.Vhoops.DataAccess.IUserDal
import com.beratyesbek.Vhoops.Entities.Concrete.User

class UserManager(userDal: IUserDal) : IUserService {

    private val _userDal: IUserDal = userDal;

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
                        _userDal.add(entity, result);
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
            _userDal.update(entity,result)

        }else{
            result(ErrorResult(resultRules.message()))
        }

    }

    override fun delete(entity: User, result: (IResult) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getAll(iDataResult: (IDataResult<ArrayList<User>>) -> Unit) {
        _userDal.getAll(iDataResult);
    }

    override fun getById(id: String, iDataResult: (IDataResult<ArrayList<User>>) -> Unit) {
        _userDal.getById(id,iDataResult)
    }

    override fun createUser(entity: User, result: (IResult) -> Unit) {

        _userDal.createUser(entity, result)

    }

    override fun loginUser(email: String, password: String, result: (IResult) -> Unit) {
        _userDal.loginUser(email,password,result)
    }

    override fun getByUserName(
        userName: String,
        iDataResult: (IDataResult<ArrayList<User>>) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun getByEmail(email: String, iDataResult: (IDataResult<ArrayList<User>>) -> Unit) {
        _userDal.getByEmail(email,iDataResult)
    }

    override fun uploadPhoto(uri: Uri, result: (IResult) -> Unit) {
        _userDal.uploadPhoto(uri,result)
    }

    override fun getPhoto(userId: String, result: (IDataResult<Uri>) -> Unit) {
        _userDal.getPhoto(userId,result)
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
                   _userDal.updateUserName(userName,documentId,result)
                }
            }

        } else {
            result(ErrorResult(rules.message()));
        }
    }

    override fun updateUserProfileImage(uri: Uri?,documentId: String, result: (IResult) -> Unit) {

        _userDal.updateUserProfileImage(uri,documentId,result)
    }

    override fun removeProfileImage(result: (IResult) ->Unit) {

       _userDal.removeProfileImage(result)

    }


    fun checkUserName(userName: String, result: (IResult) -> Unit) {
        _userDal.getByUserName(userName) { iDataResult ->

            if (iDataResult.success()) {
                result(SuccessResult("User has been created"))
            }else{
                result(ErrorResult("User has not been created"))
            }

        }
    }


}