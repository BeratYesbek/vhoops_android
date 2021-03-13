package com.beratyesbek.Vhoops.Core.Utilities.Concrete

class ErrorDataResult<T>(data:T,message:String): DataResult<T>(data,false,message) {
}