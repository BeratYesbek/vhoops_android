package com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract

interface IDataResult<T> : IResult {
    fun data(): T
}