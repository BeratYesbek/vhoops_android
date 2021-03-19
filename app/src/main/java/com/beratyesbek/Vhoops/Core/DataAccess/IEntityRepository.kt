package com.beratyesbek.Vhoops.Core.DataAccess

import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IResult
import com.beratyesbek.Vhoops.Entities.Abstract.IEntity

interface IEntityRepository<T : IEntity> {

    fun addData(entity: T, result: (IResult) -> Unit)

    fun updateData(entity: T, result: (IResult) -> Unit)

    fun deleteData(entity: T, result: (IResult) -> Unit)

    fun getData(iDataResult: (IDataResult<ArrayList<T>>) -> Unit)
}