package com.beratyesbek.Vhoops.Core.Utilities.Abstract

interface IDataResult<T> : IResult {
    fun data(): T
}