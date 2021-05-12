package com.beratyesbek.Vhoops.Core.Utilities.Result.Concrete

import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IDataResult

open class DataResult<T>(data: T?, success: Boolean, message: String) : Result(success, message), IDataResult<T> {

    private var data: T? = data;

    override fun data(): T {
        return data!!;
    }
}