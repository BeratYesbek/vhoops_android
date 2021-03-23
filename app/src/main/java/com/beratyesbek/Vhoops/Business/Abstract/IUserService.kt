package com.beratyesbek.Vhoops.Business.Abstract

import com.beratyesbek.Vhoops.Core.DataAccess.IEntityRepository
import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IResult
import com.beratyesbek.Vhoops.Entities.Concrete.User

interface IUserService :IServiceRepository<User> {
    fun createUser(entity:User,result :(IResult) -> Unit)
}