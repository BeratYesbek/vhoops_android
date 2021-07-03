package com.beratyesbek.vhoops.mvvm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.beratyesbek.vhoops.business.abstracts.IFriendRequestService
import com.beratyesbek.vhoops.entities.concrete.dtos.FriendRequestDto
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    application: Application,
    private val friendRequestService: IFriendRequestService
) : BaseViewModel(application) {

    val friendRequestList  = MutableLiveData<ArrayList<FriendRequestDto>>()

    fun getFriendRequestDetails(){
        val userId = FirebaseAuth.getInstance().currentUser.uid
        friendRequestService.getFriendRequestDetailsByUserId(userId){ iDataResult ->
            if (iDataResult.success()){
                friendRequestList.value = iDataResult.data()
            }
        }
    }
}