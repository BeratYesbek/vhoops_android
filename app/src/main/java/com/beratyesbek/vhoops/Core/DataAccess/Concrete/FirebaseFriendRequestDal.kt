package com.beratyesbek.vhoops.Core.DataAccess.Concrete

import com.beratyesbek.vhoops.Core.DataAccess.Abstract.IFirebaseFriendRequestDal
import com.beratyesbek.vhoops.Core.DataAccess.Constants.FirebaseCollection
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Concrete.ErrorDataResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Concrete.ErrorResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Concrete.SuccessDataResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Concrete.SuccessResult
import com.beratyesbek.vhoops.entities.concrete.FriendRequest
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlin.collections.ArrayList

open class FirebaseFriendRequestDal : IFirebaseFriendRequestDal<FriendRequest> {

    private lateinit var cloudFirebase : FirebaseFirestore
    override fun add(entity: FriendRequest, result: (IResult) -> Unit) {
        cloudFirebase = FirebaseFirestore.getInstance()
        val hashMap = hashMapOf(
            "SenderId" to entity.senderId,
            "ReceiverId" to entity.receiverId,
            "TimeToSend" to entity.timeToSend,
            "Status" to entity.status
        )
        cloudFirebase.collection(FirebaseCollection.FRIEND_REQUEST_COLLECTION)
            .document(entity.documentId)
            .set(hashMap)
            .addOnSuccessListener {
                result(SuccessResult(""))

             }
            .addOnFailureListener {
                result(ErrorResult(""))

            }

    }

    override fun update(entity: FriendRequest, result: (IResult) -> Unit) {

        cloudFirebase = FirebaseFirestore.getInstance()
        val hashMap = hashMapOf<String,Any>(
            "Status" to entity.status
        )
        cloudFirebase.collection(FirebaseCollection.FRIEND_REQUEST_COLLECTION)
            .document(entity.documentId)
            .update(hashMap)
            .addOnSuccessListener {
                result(SuccessResult(""))
            }
            .addOnFailureListener {
                result(ErrorResult(""))
            }
    }

    override fun delete(entity: FriendRequest, result: (IResult) -> Unit) {
        cloudFirebase = FirebaseFirestore.getInstance()
        cloudFirebase.collection(FirebaseCollection.FRIEND_REQUEST_COLLECTION)
            .whereEqualTo("ReceiverId",entity.receiverId)
            .whereEqualTo("SenderId",entity.senderId).get().addOnSuccessListener {
                for (item in it){
                     cloudFirebase.collection(FirebaseCollection.FRIEND_REQUEST_COLLECTION).document(item.id)
                         .delete()
                         .addOnSuccessListener {
                             result(SuccessResult(""))

                         }
                }
            }
    }

    override fun getAll(iDataResult: (IDataResult<ArrayList<FriendRequest>>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getById(id: String, iDataResult: (IDataResult<ArrayList<FriendRequest>>) -> Unit) {
        val friendRequestList = ArrayList<FriendRequest>()
        cloudFirebase = FirebaseFirestore.getInstance()
        cloudFirebase.collection(FirebaseCollection.FRIEND_REQUEST_COLLECTION)
            .whereEqualTo("ReceiverId",id)
            .get()
            .addOnSuccessListener { task ->
                if(!task.isEmpty){
                    friendRequestList.addAll(setData(task))
                    iDataResult(SuccessDataResult<ArrayList<FriendRequest>>(friendRequestList,""))
                }


            }.addOnFailureListener {
                iDataResult(ErrorDataResult<ArrayList<FriendRequest>>(friendRequestList,""))
            }
    }

    fun setData(value : QuerySnapshot) : ArrayList<FriendRequest>{
        val friendRequestList =  ArrayList<FriendRequest>()
        for (document in value){
            val documentId = document.id as String
            val receiverId = document.get("ReceiverId") as String
            val senderId = document.get("SenderId") as String
            val status = document.get("Status") as Boolean
            val timeToSend = document.get("TimeToSend") as Timestamp

            friendRequestList.add(FriendRequest(senderId,receiverId,documentId,status,timeToSend))

        }
        return friendRequestList
    }

    override fun getBySenderAndReceiverId(
        senderId: String,
        receiverId: String,
        iDataResult: (IDataResult<FriendRequest>) -> Unit
    ) {
        cloudFirebase = FirebaseFirestore.getInstance()
        cloudFirebase.collection(FirebaseCollection.FRIEND_REQUEST_COLLECTION)
            .whereEqualTo("ReceiverId",receiverId)
            .whereEqualTo("SenderId",senderId)
            .get()
            .addOnSuccessListener {
                for (document in it){
                    val documentId = document.id as String
                    val receiverId = document.get("ReceiverId") as String
                    val senderId = document.get("SenderId") as String
                    val status = document.get("Status") as Boolean
                    val timeToSend = document.get("TimeToSend") as Timestamp
                    iDataResult(SuccessDataResult(FriendRequest(senderId,receiverId,documentId,status,timeToSend),""))
                }
            }.addOnFailureListener {

            }
    }
}