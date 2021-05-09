package com.beratyesbek.Vhoops.Core.DataAccess.Concrete

import android.net.Uri
import com.beratyesbek.Vhoops.Core.Constants.Constants
import com.beratyesbek.Vhoops.Core.DataAccess.Abstract.IFirebaseChatDal
import com.beratyesbek.Vhoops.Core.DataAccess.Constants.ExtensionConstants
import com.beratyesbek.Vhoops.Core.DataAccess.Constants.FirebaseCollection
import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Concrete.ErrorDataResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Concrete.SuccessDataResult
import com.beratyesbek.Vhoops.Entities.Concrete.Chat
import com.google.android.gms.maps.model.LatLng
import com.google.common.collect.Maps
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

open class FirebaseChatDal : IFirebaseChatDal<Chat> {

    private lateinit var cloudFirebase: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage

    override fun add(entity: Chat, result: (IResult) -> Unit) {
        val hashMap = HashMap<String,Any>()


        hashMap.put("SenderId",entity.senderId)
        hashMap.put("ReceiverId",entity.receiverId)
        hashMap.put("TimeToSend",entity.timeToSend)
        hashMap.put("IsSeen",entity.isSeen)

        if(entity.message is LatLng){
            hashMap.put("Message",entity.message)
        }else{
            hashMap.put("Message",entity.message.toString())
        }

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

    override fun uploadFile(uri: Uri,type : String, result: (IDataResult<String>) -> Unit) {
        firebaseStorage = FirebaseStorage.getInstance()

        val uuid = UUID.randomUUID()
        var path = "ChatFiles/" + uuid.toString()

        if(type.equals(ExtensionConstants.VIDEO)) {
            path = path  + "--.video--"
        }
        else if (type.equals(ExtensionConstants.IMAGE))  {
            path = path + "--.image--"
        }
        else if(type.equals(ExtensionConstants.DOCUMENT)) {
            path = path + "--.document--"
        }

        firebaseStorage.reference.child(path)
            .putFile(uri)
            .addOnSuccessListener { result ->
                result(SuccessDataResult(path,""))

        }
    }

    override fun getFile(path: String, iDataResult: (IDataResult<Uri>) -> Unit) {
        firebaseStorage = FirebaseStorage.getInstance()
        firebaseStorage.reference.child(path).downloadUrl.
        addOnSuccessListener {
            if(it != null){

                iDataResult(SuccessDataResult(it,""))

            }
            iDataResult(ErrorDataResult(it,""))

        }.addOnFailureListener {

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