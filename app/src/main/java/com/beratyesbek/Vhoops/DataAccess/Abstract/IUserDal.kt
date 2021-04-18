package com.beratyesbek.Vhoops.DataAccess.Abstract

import com.beratyesbek.Vhoops.Core.DataAccess.Abstract.IFirebaseUserDal
import com.beratyesbek.Vhoops.Core.DataAccess.IEntityRepository
import com.beratyesbek.Vhoops.Entities.Concrete.User

interface IUserDal : IEntityRepository<User> , IFirebaseUserDal<User>{
}