package com.beratyesbek.Vhoops.Core.Utilities.Result.Concrete

import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IResult

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