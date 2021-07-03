package com.beratyesbek.vhoops.mvvm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.beratyesbek.vhoops.business.abstracts.IFellowService
import com.beratyesbek.vhoops.business.abstracts.IUserService
import com.beratyesbek.vhoops.entities.concrete.Fellow
import com.beratyesbek.vhoops.entities.concrete.Group
import com.beratyesbek.vhoops.entities.concrete.User
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PersonsViewModel @Inject constructor(
    application: Application,
    private val userService : IUserService,
    private val fellowService : IFellowService
) : BaseViewModel(application) {

    private val userList: ArrayList<User> = ArrayList()
    private val fellowList: ArrayList<Fellow> = ArrayList()

    val friendList = MutableLiveData<ArrayList<User>>()
    lateinit var group :Group
    fun getFriendData() {
        val userId = FirebaseAuth.getInstance().currentUser.uid

        fellowService.getById(userId) { iDataResult ->
            fellowList.clear()
            if (iDataResult.success()) {

                fellowList.addAll(iDataResult.data()!!)

                getUserData()
            }
        }
    }

    private fun getUserData(){
        userService.getAll { iDataResult ->
            if (iDataResult.success()) {
                userList.addAll(iDataResult.data()!!)

                val data = setAllDataByFriend()
                friendList.value = data
            }
        }
    }


    private fun setAllDataByFriend() : ArrayList<User>{
        val tempUserList = ArrayList<User>()
        for (friend in fellowList) {

            for (user in userList) {
                if (friend.userId == user.userID ) {
                    tempUserList.add(user)
                }
            }
        }

        return tempUserList

    }
}