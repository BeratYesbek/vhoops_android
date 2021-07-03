package com.beratyesbek.vhoops.dataAccess.concretes

import android.net.Uri
import com.beratyesbek.vhoops.core.dataAccess.concretes.FirebaseFriendRequestDal
import com.beratyesbek.vhoops.core.dataAccess.constants.FirebaseCollection
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IDataResult
import com.beratyesbek.vhoops.core.utilities.results.Concrete.SuccessDataResult
import com.beratyesbek.vhoops.dataAccess.abstracts.IFriendRequestDal
import com.beratyesbek.vhoops.entities.concrete.dtos.FriendRequestDto
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class FriendRequestDal : FirebaseFriendRequestDal(), IFriendRequestDal {

    private lateinit var firebaseFirestore: FirebaseFirestore

    override fun getFriendRequestDetailsByUserId(
        id: String,
        iDataResult: (IDataResult<ArrayList<FriendRequestDto>>) -> Unit
    ) {
        val friendRequestList = ArrayList<FriendRequestDto>()
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseFirestore.collection(FirebaseCollection.FRIEND_REQUEST_COLLECTION)
            .get()
            .addOnSuccessListener { querySnapshot ->

                if (!querySnapshot.isEmpty) {
                    for (document in querySnapshot) {
                        val documentId = document.id as String
                        val receiverId = document.get("ReceiverId") as String
                        val senderId = document.get("SenderId") as String
                        val status = document.get("Status") as Boolean
                        val timeToSend = document.get("TimeToSend") as Timestamp
                        if (senderId == id || receiverId == id) {
                            friendRequestList.add(
                                FriendRequestDto(
                                    senderId,
                                    receiverId,
                                    documentId,
                                    status,
                                    timeToSend,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null
                                )
                            )
                        }
                    }
                    getUserData(friendRequestList, id) {
                        iDataResult(SuccessDataResult(it.data(), ""))
                    }
                }

            }.addOnFailureListener {

            }

    }

    fun getUserData(
        friendRequestList: ArrayList<FriendRequestDto>,
        id: String,
        iDataResult: (IDataResult<ArrayList<FriendRequestDto>>) -> Unit
    ) {
        val tempFriendRequestDtoList =
            firebaseFirestore.collection(FirebaseCollection.USER_COLLECTION).get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        friendRequestList.forEachIndexed { index, friendRequestDto ->
                            for (document in querySnapshot) {
                                val firstName = document.get("FirstName").toString()
                                val lastName = document.get("LastName").toString()
                                val userName = document.get("UserName").toString()
                                val userID = document.get("UserID").toString()
                                val profileImage = document.get("ProfileImage")
                                if ((friendRequestDto.senderId == userID || friendRequestDto.receiverId == userID) && userID != id) {
                                    friendRequestList[index].userFirstName = firstName
                                    friendRequestList[index].userLastName = lastName
                                    friendRequestList[index].userId = userID
                                    friendRequestList[index].userName = userName
                                    var uri: Uri? = null
                                    if (profileImage != null) {
                                        uri = Uri.parse(profileImage.toString())
                                    }
                                    friendRequestList[index].userImage = uri

                                }
                            }
                        }
                        iDataResult(SuccessDataResult(friendRequestList, ""))


                    }

                }
    }
}