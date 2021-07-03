package com.beratyesbek.vhoops.mvvm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.beratyesbek.vhoops.business.abstracts.IInvitationService
import com.beratyesbek.vhoops.entities.concrete.dtos.InvitationDto
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CallFragmentViewModel @Inject constructor(
    application:Application,
   val invitationService: IInvitationService
)
    :BaseViewModel(application) {

        private lateinit var firebaseAuth: FirebaseAuth

        val invitationList = MutableLiveData<ArrayList<InvitationDto>>()

        fun getInvitationData(){
            firebaseAuth = FirebaseAuth.getInstance()
            val userId = firebaseAuth.currentUser.uid
            invitationService.getDetailsByUserId(userId){iDataResult ->
                if (iDataResult.success()){
                    invitationList.value = iDataResult.data()
                }
            }
        }
}