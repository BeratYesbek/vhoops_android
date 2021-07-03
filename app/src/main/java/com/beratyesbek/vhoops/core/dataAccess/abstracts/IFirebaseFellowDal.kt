package com.beratyesbek.vhoops.core.dataAccess.abstracts

import com.beratyesbek.vhoops.core.dataAccess.IEntityRepository
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IDataResult
import com.beratyesbek.vhoops.entities.abstracts.IEntity
import com.beratyesbek.vhoops.entities.concrete.Fellow

interface IFirebaseFellowDal<T : IEntity> : IEntityRepository<T> {
       fun getByUserId(userId : String,iDataResult :(IDataResult<Fellow>) -> Unit)
}