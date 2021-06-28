package com.beratyesbek.vhoops.Business.Abstract

import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.vhoops.entities.concrete.Fellow

interface IFellowService : IServiceRepository<Fellow> {
    fun getByUserId(userId : String,iDataResult :(IDataResult<Fellow>) -> Unit)

}