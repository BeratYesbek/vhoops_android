package com.beratyesbek.vhoops.DataAccess.Abstract

import com.beratyesbek.vhoops.Core.DataAccess.Abstract.IFirebaseUserDal
import com.beratyesbek.vhoops.Core.DataAccess.IEntityRepository
import com.beratyesbek.vhoops.entities.concrete.User

interface IUserDal : IEntityRepository<User> , IFirebaseUserDal<User>{
}