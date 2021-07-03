package com.beratyesbek.vhoops.mvvm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.beratyesbek.vhoops.business.abstracts.IFriendRequestService
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavigationBottomViewModel @Inject constructor(
    application: Application,
    private val friendRequestService: IFriendRequestService
) : BaseViewModel(application) {
    val notificationResult = MutableLiveData<Boolean>()
    fun getFriendRequestDetails(){
        val userId = FirebaseAuth.getInstance().currentUser.uid
        friendRequestService.getFriendRequestDetailsByUserId(userId){ iDataResult ->
            if (iDataResult.success()){
                for (item in iDataResult.data()!!){
                    if (!item.status){
                        notificationResult.value = true
                    }
                }
            }
        }
    }
}