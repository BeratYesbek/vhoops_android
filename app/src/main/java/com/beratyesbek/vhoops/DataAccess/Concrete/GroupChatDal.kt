package com.beratyesbek.vhoops.DataAccess.Concrete

import android.net.Uri
import com.beratyesbek.vhoops.Core.DataAccess.Concrete.FirebaseGroupChatDal
import com.beratyesbek.vhoops.Core.DataAccess.Constants.FirebaseCollection
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Concrete.ErrorDataResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Concrete.SuccessDataResult
import com.beratyesbek.vhoops.DataAccess.Abstract.IGroupChatDal
import com.beratyesbek.vhoops.entities.concrete.dtos.GroupChatDto
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import javax.inject.Inject

class GroupChatDal @Inject
constructor() : IGroupChatDal, FirebaseGroupChatDal(){
    override fun getChatDetailById(
        id: String,
        iDataResult: (IDataResult<ArrayList<GroupChatDto>>) -> Unit
    ) {
        val groupChatList = ArrayList<GroupChatDto>()

        val cloudFirebase = FirebaseFirestore.getInstance()

        cloudFirebase.collection(FirebaseCollection.GROUP_CHAT_COLLECTION)
            .document(id)
            .collection(id)
            .orderBy("TimeToSend", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                groupChatList.clear()
                if (value != null) {
                    if (!value.isEmpty) {
                        for (document in value) {
                            val documentId = document.id
                            val senderId = document.get("SenderId").toString()
                            val message = document.get("Message") as Any
                            val isSeen = document.get("IsSeen") as Boolean
                            val timeToSend = document.get("TimeToSend") as Timestamp

                            groupChatList.add(
                                GroupChatDto(
                                    senderId,
                                    documentId,
                                    message,
                                    isSeen,
                                    timeToSend,
                                    null,
                                    null
                                )
                            )

                        }
                        if (groupChatList.size > 0) {
                            getUserData(groupChatList) {
                                if (it.success()) {
                                    iDataResult(SuccessDataResult(it.data(), ""))
                                }
                            }
                        }
                    } else {
                        iDataResult(ErrorDataResult(groupChatList, ""))
                    }


                } else {
                    iDataResult(ErrorDataResult(groupChatList, ""))

                }
            }
    }

    private fun getUserData(
        messageList: ArrayList<GroupChatDto>,
        iDataResult: (IDataResult<ArrayList<GroupChatDto>>) -> Unit
    ) {
        val cloudFirebase = FirebaseFirestore.getInstance()

        cloudFirebase.collection(FirebaseCollection.USER_COLLECTION)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    messageList.forEachIndexed { index, groupChatDto ->
                        for (item in result.documents) {
                            val userId = item.get("UserID").toString()
                            val firstName = item.get("FirstName").toString()
                            val lastName = item.get("LastName").toString()
                            val profileImage = item.get("ProfileImage").toString()

                            if (groupChatDto.senderId.equals(userId)) {
                                messageList[index].senderFullName =
                                    (firstName + " " + lastName).toUpperCase()
                                if (profileImage != null) {
                                    messageList[index].senderImage = Uri.parse(profileImage)
                                }
                                break
                            }
                        }
                    }
                    iDataResult(SuccessDataResult(messageList, ""))
                }
            }

    }
}