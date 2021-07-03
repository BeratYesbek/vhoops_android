package com.beratyesbek.vhoops.mvvm

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.beratyesbek.vhoops.business.abstracts.IUserService
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application,
    private val userService: IUserService
) : BaseViewModel(application) {

    var context : Context? = null

    val resultLiveData = MutableLiveData<IResult>()

     fun login(email : String,password:String) {
        userService.loginUser(email, password) { result ->

               resultLiveData.value = result

        }
    }
}