package com.beratyesbek.vhoops.business.abstracts

import com.beratyesbek.vhoops.core.utilities.results.Abstract.IDataResult
import com.beratyesbek.vhoops.entities.concrete.Fellow

interface IFellowService : IServiceRepository<Fellow> {
    fun getByUserId(userId : String,iDataResult :(IDataResult<Fellow>) -> Unit)

}