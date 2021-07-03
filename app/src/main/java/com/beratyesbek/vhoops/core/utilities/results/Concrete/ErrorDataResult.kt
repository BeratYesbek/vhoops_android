package com.beratyesbek.vhoops.core.utilities.results.Concrete

class ErrorDataResult<T : Any>(data: T?, message:String): DataResult<T>(data,false,message) {
}