package com.beratyesbek.vhoops.Core.DataAccess.Abstract

import com.beratyesbek.vhoops.Core.DataAccess.IEntityRepository
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.vhoops.entities.abstracts.IEntity
import com.beratyesbek.vhoops.entities.concrete.Fellow

interface IFirebaseFellowDal<T : IEntity> : IEntityRepository<T> {
       fun getByUserId(userId : String,iDataResult :(IDataResult<Fellow>) -> Unit)
}