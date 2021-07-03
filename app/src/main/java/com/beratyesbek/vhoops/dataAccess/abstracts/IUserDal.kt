package com.beratyesbek.vhoops.dataAccess.abstracts

import com.beratyesbek.vhoops.core.dataAccess.abstracts.IFirebaseUserDal
import com.beratyesbek.vhoops.core.dataAccess.IEntityRepository
import com.beratyesbek.vhoops.entities.concrete.User

interface IUserDal : IEntityRepository<User> , IFirebaseUserDal<User>{
}