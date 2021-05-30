package com.beratyesbek.vhoops.Core.Utilities.Business

import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Concrete.SuccessResult

class BusinessRules {

    companion object {
        //  business rules method is run by this class and method
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