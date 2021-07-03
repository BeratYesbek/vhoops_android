package com.beratyesbek.vhoops.core.dataAccess.concretes

import android.net.Uri
import com.beratyesbek.vhoops.core.dataAccess.abstracts.IFirebaseChatDal
import com.beratyesbek.vhoops.core.dataAccess.constants.ExtensionConstants
import com.beratyesbek.vhoops.core.dataAccess.constants.FirebaseCollection
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IDataResult
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IResult
import com.beratyesbek.vhoops.core.utilities.results.Concrete.ErrorDataResult
import com.beratyesbek.vhoops.core.utilities.results.Concrete.SuccessDataResult
import com.beratyesbek.vhoops.core.utilities.results.Concrete.SuccessResult
import com.beratyesbek.vhoops.entities.concrete.Chat
import com.google.android.gms.maps.model.LatLng
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
    private lateinit var firebaseAuth: FirebaseAuth

    override fun add(entity: Chat, result: (IResult) -> Unit) {
        val hashMap = HashMap<String, Any>()


        hashMap.put("SenderId", entity.senderId)
        hashMap.put("ReceiverId", entity.receiverId)
        hashMap.put("TimeToSend", entity.timeToSend)
        hashMap.put("IsSeen", entity.isSeen)

        if (entity.message is LatLng) {
            hashMap.put("Message", entity.message)
        } else {
            hashMap.put("Message", entity.message.toString())
        }

        cloudFirebase = FirebaseFirestore.getInstance()


        cloudFirebase.collection(FirebaseCollection.CHAT_COLLECTION)
            .document(entity.senderId)
            .collection(entity.senderId)
            .add(hashMap)
            .addOnSuccessListener {
                result(SuccessResult(""))
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
        cloudFirebase = FirebaseFirestore.getInstance()
        val writeBatch = cloudFirebase.batch()
        cloudFirebase.collection(FirebaseCollection.CHAT_COLLECTION)
            .document(entity.senderId)
            .collection(entity.senderId)
    }

    override fun getAll(iDataResult: (IDataResult<ArrayList<Chat>>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getById(id: String, iDataResult: (IDataResult<ArrayList<Chat>>) -> Unit) {
        val chatList = ArrayList<Chat>()

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        cloudFirebase = FirebaseFirestore.getInstance()

        cloudFirebase.collection(FirebaseCollection.CHAT_COLLECTION)
            .document(userId!!)
            .collection(userId!!)
            .whereEqualTo("TimeToSend", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                if (value != null) {

                    val list = setData(value, id)
                    chatList.addAll(list)
                    iDataResult(SuccessDataResult<ArrayList<Chat>>(chatList, ""))

                }
            }

    }

    override fun uploadFile(uri: Uri, type: String, result: (IDataResult<String>) -> Unit) {
        firebaseStorage = FirebaseStorage.getInstance()

        val uuid = UUID.randomUUID()
        var path = "ChatFiles/" + uuid.toString()

        if (type.equals(ExtensionConstants.VIDEO)) {
            path = path + "--.video--"
        } else if (type.equals(ExtensionConstants.IMAGE)) {
            path = path + "--.image--"
        } else if (type.equals(ExtensionConstants.DOCUMENT)) {
            path = path + "--.document--"
        } else if (type.equals(ExtensionConstants.AUDIO)) {
            path = path + "--.audio--"
        }

        firebaseStorage.reference.child(path)
            .putFile(uri)
            .addOnSuccessListener { result ->
                result(SuccessDataResult(path, ""))

            }
    }

    override fun getFile(path: String, iDataResult: (IDataResult<Uri>) -> Unit) {
        firebaseStorage = FirebaseStorage.getInstance()
        firebaseStorage.reference.child(path).downloadUrl.addOnSuccessListener { uri ->
            if (uri != null) {

                iDataResult(SuccessDataResult(uri, ""))

            } else {
                iDataResult(ErrorDataResult(null, ""))
            }

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

    override fun deleteMulti(arrayList: ArrayList<Chat>, result: (IResult) -> Unit) {
        cloudFirebase = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        val userId = firebaseAuth.currentUser.uid
        val writeBatch = cloudFirebase.batch()

        for (item in arrayList) {

            val documentReference =
                cloudFirebase.collection(FirebaseCollection.CHAT_COLLECTION)
                    .document(userId)
                    .collection(userId).document(item.documentId)

            writeBatch.delete(documentReference)
        }
        writeBatch.commit().addOnSuccessListener {
            result(SuccessResult("Successful"))
        }
    }


}