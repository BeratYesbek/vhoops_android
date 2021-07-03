package com.beratyesbek.vhoops.core.utilities.business

import com.beratyesbek.vhoops.core.utilities.results.Abstract.IResult
import com.beratyesbek.vhoops.core.utilities.results.Concrete.SuccessResult

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