package com.beratyesbek.Vhoops.Core.Utilities.Result.Concrete

class SuccessDataResult<T>(data: T?, message: String) : DataResult<T>(data, true, message){

}