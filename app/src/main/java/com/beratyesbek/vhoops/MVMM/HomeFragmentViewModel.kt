package com.beratyesbek.vhoops.MVMM

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.beratyesbek.vhoops.Business.Abstract.IChatService
import com.beratyesbek.vhoops.entities.concrete.dtos.ChatListDto
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel
    @Inject constructor(
    application: Application,
    private val chatService: IChatService

) : BaseViewModel(application) {

    val chatList = MutableLiveData<ArrayList<ChatListDto>>()


    fun getChatData() {
        val userId = FirebaseAuth.getInstance().currentUser.uid
        chatService.getChatDetailForList(userId) { iDataResult ->

            chatList.value = iDataResult.data()
        }
    }
}