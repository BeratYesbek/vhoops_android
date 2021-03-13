package com.beratyesbek.Vhoops.Core.Utilities.Concrete

import com.beratyesbek.Vhoops.Core.Utilities.Abstract.IDataResult

open class DataResult<T>(data: T, success: Boolean, message: String) : Result(success, message),
    IDataResult<T> {

    private var data: T? = data;

    override fun data(): T {
        return data!!;
    }
}