package com.beratyesbek.vhoops.Core.Utilities.Result.Abstract

interface IDataResult<T> : IResult {
    fun data(): T
}