package com.beratyesbek.vhoops.MVMM

import android.app.Application
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.beratyesbek.vhoops.Business.Abstract.IGroupChatService
import com.beratyesbek.vhoops.Business.Concrete.GroupChatManager
import com.beratyesbek.vhoops.Business.Concrete.UserManager
import com.beratyesbek.vhoops.DataAccess.Concrete.GroupChatDal
import com.beratyesbek.vhoops.DataAccess.Concrete.UserDal
import com.beratyesbek.vhoops.entities.concrete.dtos.GroupChatDto
import com.beratyesbek.vhoops.entities.concrete.GroupChat
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@HiltViewModel
class GroupChatViewModel @Inject constructor(
    application: Application,
    private val groupChatService: IGroupChatService,
) : BaseViewModel(application) {


    val messageList = MutableLiveData<ArrayList<GroupChatDto>>()
    var groupId = ""
    val userId = FirebaseAuth.getInstance().currentUser.uid

    fun sendMessage(entity: GroupChat) {

        groupChatService.add(entity) {

        }
    }

    fun getData(groupId: String) {

        groupChatService.getChatDetailById(groupId) { iDataResult ->
            if (iDataResult.success()) {
                messageList.value = iDataResult.data()
            }
        }
    }


    fun uploadFile(message: Any, type: String) {
        println(158879999   )
        groupChatService.uploadFile(message as Uri, type) { iDataResult ->
            if (iDataResult.success()){
                getFile(iDataResult.data())
            }
        }
    }

    fun getFile(path: String) {
       groupChatService.getFile(path){ iDataResult ->
           if (iDataResult.success()){
               val message = iDataResult.data()
               sendMessage(GroupChat(userId,groupId,message,false, Timestamp.now()))
           }
       }
    }


}