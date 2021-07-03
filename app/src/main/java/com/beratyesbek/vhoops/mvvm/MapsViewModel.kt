package com.beratyesbek.vhoops.mvvm;

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.beratyesbek.vhoops.business.abstracts.IChatService
import com.beratyesbek.vhoops.business.abstracts.IGroupChatService
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IResult
import com.beratyesbek.vhoops.entities.concrete.Chat
import com.beratyesbek.vhoops.entities.concrete.GroupChat
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
public class MapsViewModel
@Inject constructor(
    application: Application,
    private val groupChatService: IGroupChatService,
    private val chatService: IChatService) : BaseViewModel(application) {

     val liveResult = MutableLiveData<IResult>()

     fun shareLocationWithChat(senderId: String,receiverId:String,userLocation: LatLng) {
        if (receiverId != null) {
            chatService.add(
                Chat(
                    senderId,
                    receiverId!!,
                    userLocation,
                    false,
                    Timestamp.now()
                )
            ) { result ->
                if (result.success()) {
                    liveResult.value = result
                }
            }
        }
    }

     fun shareLocationWithGroupChat(senderId: String,groupId :String,userLocation : LatLng) {
        if (groupId != null) {
            groupChatService.add(
                GroupChat(
                    senderId,
                    groupId!!,
                    userLocation,
                    false,
                    Timestamp.now()
                )
            ) { result ->
                if (result.success()) {
                    liveResult.value = result
                }
            }
        }

    }




}
