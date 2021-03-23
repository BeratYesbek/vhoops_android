package com.beratyesbek.Vhoops.Business.Concrete

import com.beratyesbek.Vhoops.Business.Abstract.IUserService
import com.beratyesbek.Vhoops.Business.Rules.CustomUserRules
import com.beratyesbek.Vhoops.Business.Validation.UserValidator
import com.beratyesbek.Vhoops.Core.DataAccess.IEntityRepository
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
                CustomUserRules.checkUserName(entity.userName!!, _userDal),
                CustomUserRules.checkProperty(entity)
            )
        )
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

    }

    override fun delete(entity: User, result: (IResult) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getAll(iDataResult: (IDataResult<ArrayList<User>>) -> Unit) {
        _userDal.getAll(iDataResult);
    }

    override fun createUser(entity: User, result: (IResult) -> Unit) {

        _userDal.createUser(entity, result)

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