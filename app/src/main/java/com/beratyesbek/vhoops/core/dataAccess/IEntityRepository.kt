package com.beratyesbek.vhoops.core.dataAccess

import com.beratyesbek.vhoops.core.utilities.results.Abstract.IDataResult
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IResult
import com.beratyesbek.vhoops.entities.abstracts.IEntity

interface IEntityRepository<T : IEntity>  {

    fun add(entity: T, result: (IResult) -> Unit)

    fun update(entity: T, result: (IResult) -> Unit)

    fun delete(entity: T, result: (IResult) -> Unit)

    fun getAll(iDataResult: (IDataResult<ArrayList<T>>) -> Unit)

    fun getById(id :String,iDataResult: (IDataResult<ArrayList<T>>) -> Unit)
}