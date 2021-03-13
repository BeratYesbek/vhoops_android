package com.beratyesbek.Vhoops.Core.Utilities.Concrete

class SuccessDataResult<T>(data: T, message: String) : DataResult<T>(data, true, message) {
}