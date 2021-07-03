package com.beratyesbek.vhoops.core.utilities.results.Abstract

interface IDataResult<T> : IResult {
    fun data(): T?
}