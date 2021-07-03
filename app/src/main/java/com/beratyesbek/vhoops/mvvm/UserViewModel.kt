package com.beratyesbek.vhoops.mvvm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.beratyesbek.vhoops.business.abstracts.IFriendRequestService
import com.beratyesbek.vhoops.business.abstracts.IUserService
import com.beratyesbek.vhoops.business.concretes.FellowManager
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IDataResult
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IResult
import com.beratyesbek.vhoops.dataAccess.concretes.FellowDal
import com.beratyesbek.vhoops.entities.concrete.Fellow
import com.beratyesbek.vhoops.entities.concrete.FriendRequest
import com.beratyesbek.vhoops.entities.concrete.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject
constructor(
    application: Application,
    private val userService: IUserService,
    private val friendRequestService: IFriendRequestService,
) :
    BaseViewModel(application) {

    val userLiveData = MutableLiveData<User>()
    val liveDataFriendRequestResult = MutableLiveData<IDataResult<FriendRequest>>()
    val liveDataFellowResult = MutableLiveData<IDataResult<Fellow>>()
    val liveDataFriendRequestRemoveResult = MutableLiveData<IResult>()
    val liveDataFriendRequestSendResult = MutableLiveData<IResult>()

    fun getData(userId: String) {

        userService.getById(userId) { result ->
            if (result.success()) {
                userLiveData.value = result.data()!![0]

            }
        }
    }

     fun getFriendRequest(receiverId: String) {
        val senderId = FirebaseAuth.getInstance().currentUser.uid
        friendRequestService.getBySenderAndReceiverId(senderId, receiverId) { result ->
            liveDataFriendRequestResult.value = result
        }
    }

     fun getFellowInfo(userId: String) {
        val fellowDal: FellowDal = FellowDal()
        val fellowManager = FellowManager(fellowDal)
        fellowManager.getByUserId(userId) { dataResult ->
            liveDataFellowResult.value = dataResult

        }
    }


     fun removeFriendRequest(receiverId: String) {
        val senderId = FirebaseAuth.getInstance().currentUser.uid
        friendRequestService.delete(
            FriendRequest(
                senderId,
                receiverId,
                "",
                false,
                Timestamp.now()
            )
        ) { result ->
            liveDataFriendRequestRemoveResult.value = result

        }
    }

     fun sendFriendRequest(receiverId: String) {
        val senderId = FirebaseAuth.getInstance().currentUser.uid
        val documentID = UUID.randomUUID().toString()
        friendRequestService.add(
            FriendRequest(
                senderId,
                receiverId,
                documentID,
                false,
                Timestamp.now()
            )
        ) { result ->
            liveDataFriendRequestSendResult.value = result


        }

    }


}