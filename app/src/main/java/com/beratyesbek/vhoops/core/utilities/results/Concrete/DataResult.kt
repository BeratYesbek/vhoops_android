package com.beratyesbek.vhoops.core.utilities.results.Concrete

import com.beratyesbek.vhoops.core.utilities.results.Abstract.IDataResult

open class DataResult<T>(data: T?, success: Boolean, message: String) : Result(success, message), IDataResult<T> {

    private var data: T? = data;

    override fun data(): T? {
        return data;
    }
}