package com.beratyesbek.Vhoops.Core.DataAccess.Concrete

import com.beratyesbek.Vhoops.Core.Constants.Constants
import com.beratyesbek.Vhoops.Core.DataAccess.Abstract.IFirebaseChatDal
import com.beratyesbek.Vhoops.Core.DataAccess.Constants.FirebaseCollection
import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Concrete.SuccessDataResult
import com.beratyesbek.Vhoops.Entities.Concrete.Chat
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

open class FirebaseChatDal : IFirebaseChatDal<Chat> {

    private lateinit var cloudFirebase: FirebaseFirestore

    override fun add(entity: Chat, result: (IResult) -> Unit) {

        val hashMap = hashMapOf(
            "SenderId" to entity.senderId,
            "ReceiverId" to entity.receiverId,
            "Message" to entity.message,
            "TimeToSend" to entity.timeToSend,
            "IsSeen" to entity.isSeen
        );

        cloudFirebase = FirebaseFirestore.getInstance()

        cloudFirebase.collection(FirebaseCollection.CHAT_COLLECTION)
            .document(entity.senderId)
            .collection(entity.senderId)
            .add(hashMap)
            .addOnSuccessListener {

            }
        cloudFirebase.collection(FirebaseCollection.CHAT_COLLECTION)
            .document(entity.receiverId)
            .collection(entity.receiverId)
            .add(hashMap).addOnSuccessListener {

            }
    }

    override fun update(entity: Chat, result: (IResult) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun delete(entity: Chat, result: (IResult) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getAll(iDataResult: (IDataResult<ArrayList<Chat>>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getById(id: String, iDataResult: (IDataResult<ArrayList<Chat>>) -> Unit) {
        val chatList = ArrayList<Chat>()

        val userId = FirebaseAuth.getInstance().currentUser.uid

        cloudFirebase = FirebaseFirestore.getInstance()

        cloudFirebase.collection(FirebaseCollection.CHAT_COLLECTION)
            .document(userId)
            .collection(userId)
            .whereEqualTo("TimeToSend", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                if (value != null) {

                    val list = setData(value, id)
                    chatList.addAll(list)
                    iDataResult(SuccessDataResult<ArrayList<Chat>>(chatList,""))

                }
            }

    }

    fun setData(value: QuerySnapshot, oppositeUserId: String): ArrayList<Chat> {

        val chatList = ArrayList<Chat>()

        val userId = FirebaseAuth.getInstance().currentUser.uid


        for (document in value) {
            val documentId = document.id
            val senderId = document.get("SenderId").toString()
            val receiverId = document.get("ReceiverId").toString()
            val message = document.get("Message").toString()
            val isSeen = document.get("IsSeen") as Boolean
            val timeToSend = document.get("TimeToSend") as Timestamp

            if (userId == senderId || oppositeUserId == receiverId
                && oppositeUserId == senderId || userId == receiverId
            ) {

                chatList.add(Chat(senderId, receiverId, message, documentId, isSeen, timeToSend))
            }
        }

        return chatList

    }
}