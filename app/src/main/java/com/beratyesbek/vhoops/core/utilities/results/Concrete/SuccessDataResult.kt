package com.beratyesbek.vhoops.core.utilities.results.Concrete

class SuccessDataResult<T>(data: T?, message: String) : DataResult<T>(data, true, message){

}