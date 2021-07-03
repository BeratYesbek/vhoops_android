package com.beratyesbek.vhoops.core.utilities.results.Concrete

import com.beratyesbek.vhoops.core.utilities.results.Abstract.IResult

open class Result(success: Boolean, message: String) : IResult {

    private var success: Boolean? = success;
    private var message: String? = message;

    override fun success(): Boolean {
        return success!!;
    }

    override fun message(): String {
        return message!!;
    }

}