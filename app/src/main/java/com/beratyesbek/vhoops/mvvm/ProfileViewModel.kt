package com.beratyesbek.vhoops.mvvm

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.beratyesbek.vhoops.business.abstracts.IUserService
import com.beratyesbek.vhoops.entities.concrete.User
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class ProfileViewModel @Inject
constructor(
    application: Application,
    private val userService: IUserService

) : BaseViewModel(application) {

    val userLiveData = MutableLiveData<User>()
    var context : Context? = null
     fun getData() {
        val id = FirebaseAuth.getInstance().currentUser.uid
        userService.getById(id) { dataResult ->
            if (dataResult.success()) {
                userLiveData.value = dataResult.data()!![0]

            }
        }
    }

     fun updateUserName(userName: String) {
        val documentId = userLiveData.value!!.documentID
        userService.updateUserName(userName, documentId) { result ->
            when(result.success()){
                false -> Toast.makeText(context,result.message(),Toast.LENGTH_LONG)
                true -> Toast.makeText(context,result.message(),Toast.LENGTH_LONG)

            }
        }
    }

     fun updateData(textValue: String, editTextId: Int) {
        when (editTextId) {
            1 -> userLiveData.value!!.firstName = textValue
            2 -> userLiveData.value!!.lastName = textValue
            4 -> userLiveData.value!!.about = textValue

        }

        userService.update(userLiveData.value!!) { result ->
            when(result.success()){
                false -> Toast.makeText(context,result.message(),Toast.LENGTH_LONG)
                true -> Toast.makeText(context,result.message(),Toast.LENGTH_LONG)

            }
        }
    }
}