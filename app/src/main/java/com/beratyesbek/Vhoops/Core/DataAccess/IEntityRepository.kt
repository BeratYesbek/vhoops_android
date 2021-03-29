package com.beratyesbek.Vhoops.Core.DataAccess

import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IResult
import com.beratyesbek.Vhoops.Entities.Abstract.IEntity

interface IEntityRepository<T : IEntity> {

    fun add(entity: T, result: (IResult) -> Unit)

    fun update(entity: T, result: (IResult) -> Unit)

    fun delete(entity: T, result: (IResult) -> Unit)

    fun getAll(iDataResult: (IDataResult<ArrayList<T>>) -> Unit)

    fun getById(id :String,iDataResult: (IDataResult<ArrayList<T>>) -> Unit)
}