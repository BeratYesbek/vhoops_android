package com.beratyesbek.Vhoops.Core.Utilities.Business

import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Concrete.ErrorResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Concrete.SuccessResult

class BusinessRules {

    companion object {
        fun run(array : Array<IResult> ) : IResult{
            for (item in array){
                if(!item.success()){
                    return item;
                }
            }
            return SuccessResult("");
        }
    }
}