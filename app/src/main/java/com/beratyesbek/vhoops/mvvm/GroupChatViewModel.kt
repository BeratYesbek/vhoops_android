package com.beratyesbek.vhoops.mvvm

import android.app.Application
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.beratyesbek.vhoops.business.abstracts.IGroupChatService
import com.beratyesbek.vhoops.business.abstracts.IGroupService
import com.beratyesbek.vhoops.entities.concrete.Group
import com.beratyesbek.vhoops.entities.concrete.dtos.GroupChatDto
import com.beratyesbek.vhoops.entities.concrete.GroupChat
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject

@HiltViewModel
class GroupChatViewModel @Inject constructor(
    application: Application,
    private val groupChatService: IGroupChatService,
    private val groupService: IGroupService,

    ) : BaseViewModel(application) {


    val messageList = MutableLiveData<ArrayList<GroupChatDto>>()
    var groupId = ""
    val userId = FirebaseAuth.getInstance().currentUser.uid
    val group  = MutableLiveData<ArrayList<Group>>()
    val leaveGroupResult = MutableLiveData<Boolean>()
    fun sendMessage(entity: GroupChat) {

        groupChatService.add(entity) {

        }
    }

    fun getGroupData(groupId  :String){
        groupService.getById(groupId){
            if (it.success()){
                group.value = it.data()
            }
        }
    }

    fun leaveGroup(group : Group){
        groupService.update(group){
            leaveGroupResult.value = it.success()
        }
    }

    fun getData(groupId: String) {

        groupChatService.getChatDetailById(groupId) { iDataResult ->
            messageList.value = iDataResult.data()
        }
    }


    fun uploadFile(message: Any, type: String) {
        groupChatService.uploadFile(message as Uri, type) { iDataResult ->
            if (iDataResult.success()) {
                getFile(iDataResult.data()!!)
            }
        }
    }

    fun getFile(path: String) {
        groupChatService.getFile(path) { iDataResult ->
            if (iDataResult.success()) {
                val message = iDataResult.data()
                sendMessage(GroupChat(userId, groupId, message!!, false, Timestamp.now()))
            }
        }
    }


}