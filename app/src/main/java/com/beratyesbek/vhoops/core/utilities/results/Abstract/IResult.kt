package com.beratyesbek.vhoops.core.utilities.results.Abstract

interface IResult {

    fun success() : Boolean
    fun message() : String
}