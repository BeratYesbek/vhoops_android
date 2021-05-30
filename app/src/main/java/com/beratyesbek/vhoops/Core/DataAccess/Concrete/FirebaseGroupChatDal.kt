package com.beratyesbek.vhoops.Core.DataAccess.Concrete

import android.net.Uri
import com.beratyesbek.vhoops.Core.DataAccess.Abstract.IFirebaseGroupChatDal
import com.beratyesbek.vhoops.Core.DataAccess.Constants.ExtensionConstants
import com.beratyesbek.vhoops.Core.DataAccess.Constants.FirebaseCollection
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Concrete.ErrorDataResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Concrete.SuccessDataResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Concrete.SuccessResult
import com.beratyesbek.vhoops.entities.concrete.GroupChat
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

open class FirebaseGroupChatDal : IFirebaseGroupChatDal {

    private lateinit var cloudFirebase: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var firebaseAuth: FirebaseAuth


    override fun uploadFile(uri: Uri, type: String, result: (IDataResult<String>) -> Unit) {
        firebaseStorage = FirebaseStorage.getInstance()

        val uuid = UUID.randomUUID()
        var path = "GroupChatFiles/" + uuid.toString()

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

    override fun add(entity: GroupChat, result: (IResult) -> Unit) {
        val hashMap = HashMap<String, Any>()


        hashMap.put("SenderId", entity.senderId!!)
        hashMap.put("TimeToSend", entity.timeToSend!!)
        hashMap.put("IsSeen", entity.isSeen!!)

        if (entity.message is LatLng) {
            hashMap.put("Message", entity.message!!)
        } else {
            hashMap.put("Message", entity.message.toString())
        }

        cloudFirebase = FirebaseFirestore.getInstance()


        cloudFirebase.collection(FirebaseCollection.GROUP_CHAT_COLLECTION)
            .document(entity.groupId!!)
            .collection(entity.groupId!!)
            .add(hashMap)
            .addOnSuccessListener {
                result(SuccessResult(""))
            }


    }

    override fun update(entity: GroupChat, result: (IResult) -> Unit) {
        println("Dependency Injection made it")
    }

    override fun delete(entity: GroupChat, result: (IResult) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getAll(iDataResult: (IDataResult<ArrayList<GroupChat>>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getById(id: String, iDataResult: (IDataResult<ArrayList<GroupChat>>) -> Unit) {
        TODO("Not yet implemented")
    }


}