package com.beratyesbek.vhoops.DataAccess.Concrete

import android.net.Uri
import com.beratyesbek.vhoops.Core.DataAccess.Concrete.FirebaseChatDal
import com.beratyesbek.vhoops.Core.DataAccess.Constants.FirebaseCollection
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Concrete.ErrorDataResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Concrete.SuccessDataResult
import com.beratyesbek.vhoops.DataAccess.Abstract.IChatDal
import com.beratyesbek.vhoops.entities.concrete.dtos.ChatDto
import com.beratyesbek.vhoops.entities.concrete.dtos.ChatListDto
import com.beratyesbek.vhoops.entities.concrete.dtos.GroupChatDto
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlin.collections.ArrayList

class ChatDal : FirebaseChatDal(), IChatDal {

    private lateinit var cloudFirebase: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth


    override fun getChatDetail(id: String, iDataResult: (IDataResult<ArrayList<ChatDto>>) -> Unit) {

        val chatList = ArrayList<ChatDto>()


        val userId = FirebaseAuth.getInstance().currentUser.uid

        cloudFirebase = FirebaseFirestore.getInstance()

        cloudFirebase.collection(FirebaseCollection.CHAT_COLLECTION)
            .document(userId)
            .collection(userId)
            .orderBy("TimeToSend", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                chatList.clear()
                if (value != null) {
                    if (!value.isEmpty) {
                        for (document in value) {
                            val documentId = document.id
                            val senderId = document.get("SenderId").toString()
                            val receiverId = document.get("ReceiverId").toString()
                            val message = document.get("Message") as Any
                            val isSeen = document.get("IsSeen") as Boolean
                            val timeToSend = document.get("TimeToSend") as Timestamp

                            if ((userId.equals(senderId) && id.equals(receiverId))
                                || (id.equals(senderId) && userId.equals(receiverId))
                            ) {
                                chatList.add(
                                    ChatDto(
                                        senderId,
                                        receiverId,
                                        message,
                                        documentId,
                                        isSeen,
                                        timeToSend,
                                        null,
                                        ""
                                    )
                                )
                            }
                        }
                        if (chatList.size > 0) {
                            iDataResult(SuccessDataResult(chatList, ""))
                        }
                    } else {
                        iDataResult(ErrorDataResult(chatList, ""))
                    }


                } else {
                    iDataResult(ErrorDataResult(chatList, ""))

                }
            }
    }

    override fun getChatDetailForList(
        id: String,
        iDataResult: (IDataResult<ArrayList<ChatListDto>>) -> Unit
    ) {
        val arrayChatListDto = ArrayList<ChatListDto>()
        var control = false
        cloudFirebase = FirebaseFirestore.getInstance()

        cloudFirebase.collection(FirebaseCollection.CHAT_COLLECTION)
            .document(id)
            .collection(id)
            .orderBy("TimeToSend", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                arrayChatListDto.clear()
                if (value != null) {
                    value.forEachIndexed { index, document ->
                        val documentId = document.id
                        val senderId = document["SenderId"] as String
                        val receiverId = document["ReceiverId"] as String
                        val isSeen = document["IsSeen"] as Boolean
                        val timestamp = document["TimeToSend"] as Timestamp
                        val message = document["Message"] as Any

                        for (chatListDto in arrayChatListDto) {
                            if (receiverId == chatListDto.senderId && senderId == chatListDto.receiverId
                                || receiverId == chatListDto.receiverId && senderId == chatListDto.senderId
                            ) {
                                control = true
                                break; } else {
                                control = false

                            }
                        }
                        if (!control) {
                            var unSeenCount = 0;
                            for (ControlDocument in value) {
                                val senderIdControl = document["SenderId"] as String
                                val receiverIdControl = document["ReceiverId"] as String
                                val isSeenControl = document["IsSeen"] as Boolean
                                if ((senderIdControl.equals(senderId) && receiverIdControl.equals(
                                        receiverId
                                    ))
                                    || (receiverIdControl.equals(senderId) && senderIdControl.equals(
                                        receiverId
                                    ))
                                ) {
                                    if (!isSeenControl) {
                                        unSeenCount++
                                    }
                                }
                            }
                            arrayChatListDto.add(
                                ChatListDto(
                                    senderId,
                                    receiverId,
                                    message,
                                    documentId,
                                    isSeen,
                                    timestamp,
                                    unSeenCount,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null
                                )
                            )
                        }
                    }

                    if (arrayChatListDto.size > 0) {
                        getUserData(arrayChatListDto) { iDataResult ->
                            if (iDataResult.success()) {
                                iDataResult(SuccessDataResult(iDataResult.data(), ""))
                            }
                        }

                    }
                }
            }


    }


    private fun getUserData(
        messageList: ArrayList<ChatListDto>,
        iDataResult: (IDataResult<ArrayList<ChatListDto>>) -> Unit
    ) {
        val cloudFirebase = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUserId = firebaseAuth.currentUser.uid
        cloudFirebase.collection(FirebaseCollection.USER_COLLECTION)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    messageList.forEachIndexed { index, ChatDto ->
                        for (item in result.documents) {
                            val userId = item.get("UserID").toString()
                            val firstName = item.get("FirstName").toString()
                            val lastName = item.get("LastName").toString()
                            val profileImage = item.get("ProfileImage").toString()
                            val token = item.get("Token").toString()

                            if (ChatDto.senderId == userId && ChatDto.senderId != currentUserId ||
                                ChatDto.receiverId == userId && ChatDto.receiverId != currentUserId
                            ) {
                                messageList[index].userId = userId
                                messageList[index].userLastName = lastName
                                messageList[index].userFirstName = firstName
                                messageList[index].userToken = token
                                if (profileImage != null) {
                                    messageList[index].userPicture = Uri.parse(profileImage)
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