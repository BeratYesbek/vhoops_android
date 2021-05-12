package com.beratyesbek.Vhoops.DataAccess.Concrete

import com.beratyesbek.Vhoops.Core.DataAccess.Concrete.FirebaseChatDal
import com.beratyesbek.Vhoops.Core.DataAccess.Constants.FirebaseCollection
import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Concrete.ErrorDataResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Concrete.SuccessDataResult
import com.beratyesbek.Vhoops.DataAccess.Abstract.IChatDal
import com.beratyesbek.Vhoops.Entities.Concrete.Dtos.ChatDto
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*
import kotlin.collections.ArrayList

class ChatDal : FirebaseChatDal(), IChatDal {

    private lateinit var cloudFirebase: FirebaseFirestore


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
                        iDataResult(SuccessDataResult(chatList, ""))
                    } else {
                        iDataResult(ErrorDataResult(chatList, ""))
                    }


                } else {
                    iDataResult(ErrorDataResult(chatList, ""))

                }
            }
    }
}