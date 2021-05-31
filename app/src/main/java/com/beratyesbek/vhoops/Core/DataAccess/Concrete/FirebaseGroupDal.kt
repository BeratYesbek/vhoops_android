package com.beratyesbek.vhoops.Core.DataAccess.Concrete

import android.net.Uri
import com.beratyesbek.vhoops.Core.DataAccess.Abstract.IFirebaseGroupDal
import com.beratyesbek.vhoops.Core.DataAccess.Constants.FirebaseCollection
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Concrete.ErrorDataResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Concrete.ErrorResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Concrete.SuccessDataResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Concrete.SuccessResult
import com.beratyesbek.vhoops.entities.concrete.Group
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

open class FirebaseGroupDal  : IFirebaseGroupDal {

    private lateinit var cloudFirebase : FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseStorage : FirebaseStorage

    override fun add(entity: Group, result: (IResult) -> Unit) {
        cloudFirebase = FirebaseFirestore.getInstance()

        val hashMapGroup = HashMap<String,Any>()

        hashMapGroup.put("GroupName",entity.groupName!!)
        hashMapGroup.put("CreatedDate",entity.createdDate!!)
        hashMapGroup.put("AdminId",entity.adminId!!)
        hashMapGroup.put("GroupId",entity.groupId!!)

        entity.memberIdList?.add(entity.adminId!!)

        hashMapGroup.put("Members",entity.memberIdList!!.toList())

        if (entity.groupImage != null) {
            hashMapGroup.put("GroupImage",entity.groupImage.toString())
        }

      cloudFirebase.collection(FirebaseCollection.GROUP_COLLECTION).document().set(hashMapGroup)
            .addOnSuccessListener {
                result(SuccessResult("Successfully"))
            }.addOnFailureListener {
                result(ErrorResult(""))
            }

    }

    override fun uploadGroupIcon(uri: Uri, iDataResult: (IDataResult<String>) -> Unit) {

        firebaseStorage = FirebaseStorage.getInstance()

        val uuid = UUID.randomUUID()

        val path = "GroupIcon/${uuid.toString()}"
        firebaseStorage.reference
            .child(path)
            .putFile(uri)
            .addOnSuccessListener {
                iDataResult(SuccessDataResult<String>(path,""))
             }
            .addOnFailureListener {
                iDataResult(ErrorDataResult<String>(null,"failed"))
            }

    }

    override fun getGroupIcon(path: String, iDataResult: (IDataResult<Uri>) -> Unit) {

        firebaseStorage = FirebaseStorage.getInstance()

        firebaseStorage
            .reference
            .child(path)
            .downloadUrl
            .addOnSuccessListener {
                iDataResult(SuccessDataResult<Uri>(it,"Successfully"))
            }
            .addOnFailureListener { exception ->
                iDataResult(SuccessDataResult<Uri>(null,exception.toString()))
            }
    }


    override fun update(entity: Group, result: (IResult) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun delete(entity: Group, result: (IResult) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getAll(iDataResult: (IDataResult<ArrayList<Group>>) -> Unit) {

        val groupList = ArrayList<Group>()

        cloudFirebase = FirebaseFirestore.getInstance()
        val id = FirebaseAuth.getInstance().currentUser.uid
        cloudFirebase.collection(FirebaseCollection.GROUP_COLLECTION).whereArrayContains("Members",id)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty){
                    for (item in querySnapshot) {
                        val documentId = item.id
                        val groupId = item.get("GroupId").toString()
                        val groupName = item.get("GroupName").toString()
                        val createData = item.get("CreatedDate") as Timestamp
                        val groupImage = item.get("GroupImage").toString()
                        val memberIdList = item.get("Members") as ArrayList<String>
                        val adminId = item.get("AdminId").toString()
                        var uri :Uri? = null

                        if(groupImage != null){
                            uri = Uri.parse(groupImage)
                        }

                        groupList.add(Group(groupId,groupName,adminId,uri,documentId,memberIdList,createData))

                    }
                    iDataResult(SuccessDataResult<ArrayList<Group>>(groupList,"Successfully"))
                }else{
                    iDataResult(ErrorDataResult<ArrayList<Group>>(null,"Failed"))

                }


            }
            .addOnFailureListener {
                iDataResult(ErrorDataResult<ArrayList<Group>>(null,"Failed"))

            }
    }

    override fun getById(id: String, iDataResult: (IDataResult<ArrayList<Group>>) -> Unit) {
        TODO("Not yet implemented")
    }
}