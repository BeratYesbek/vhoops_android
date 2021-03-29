package com.beratyesbek.Vhoops.Business.Rules

import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Concrete.ErrorResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Concrete.SuccessResult
import com.beratyesbek.Vhoops.Entities.Concrete.User

class CustomUserRules {

    companion object {

        fun checkPassword(password: String): IResult {
            //validation of password
            val PASSWORD_PATTERN =
                """^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#${'$'}%!\-_?&])(?=\S+${'$'}).{8,}""".toRegex()
            var message = ""

            if (password.length < 8) {
                message = "Your password should be min 8 length"
                return ErrorResult(message)


            }
            if (!PASSWORD_PATTERN.matches(password)) {

                message = "Your password should include at least one upper letter"
                return ErrorResult(message)

            }
            return SuccessResult("");
        }

        fun checkUserName(userName: String): IResult {
            //validation of username
            val USERNAME_PATTERN = """^(?=[a-zA-Z0-9._]{8,20}${'$'})(?!.*[_.]{2})[^_.].*[^_.]${'$'}""".toRegex();

            var message = "";

            if (userName.isNullOrEmpty() || userName.length < 4) {
                message = "UserName cannot be empty and less than 2 characters"

                return ErrorResult(message);
            }
            println(USERNAME_PATTERN.matches(userName))

            if (!USERNAME_PATTERN.matches(userName)) {
                message = "invalid argument"
                return ErrorResult(message);
            }

            return SuccessResult(message)

        }


        fun checkProperty(user: User): IResult {

            //validation of property
            var message = "";
            var check = false;

            if (user.firstName.isNullOrEmpty() || user.firstName!!.length < 2) {
                message = "FirstName cannot be empty and  less than 2 characters";
                check = true
            }
            if (user.lastName.isNullOrEmpty() || user.lastName!!.length < 2) {
                message = "LastName cannot be empty and less than 2 characters";
                check = true;
            }

            if (check) {
                return ErrorResult(message)
            }
            return SuccessResult(message)
        }


    }
}


