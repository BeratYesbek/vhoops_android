package com.beratyesbek.Vhoops.Core.Utilities.Result.Concrete

class ErrorDataResult<T : Any>(data: T?, message:String): DataResult<T>(data,false,message) {
}