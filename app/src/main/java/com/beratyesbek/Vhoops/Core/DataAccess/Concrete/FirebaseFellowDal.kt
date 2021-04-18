package com.beratyesbek.Vhoops.Core.DataAccess.Concrete

import android.net.Uri
import com.beratyesbek.Vhoops.Core.DataAccess.Abstract.IFirebaseFellowDal
import com.beratyesbek.Vhoops.Core.DataAccess.Constants.FirebaseCollection
import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Concrete.ErrorDataResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Concrete.ErrorResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Concrete.SuccessDataResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Concrete.SuccessResult
import com.beratyesbek.Vhoops.Entities.Concrete.Fellow
import com.beratyesbek.Vhoops.Entities.Concrete.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

open class FirebaseFellowDal : IFirebaseFellowDal<Fellow> {
    private lateinit var cloudFirebase: FirebaseFirestore;

    override fun add(entity: Fellow, result: (IResult) -> Unit) {

        cloudFirebase = FirebaseFirestore.getInstance()
        val hashmap = hashMapOf(
            "UserId" to entity.userId,
            "Status" to entity.status
        )

        cloudFirebase
            .collection(FirebaseCollection.FELLOW_COLLECTION)
            .document(entity.addedUserId)
            .collection(entity.addedUserId)
            .document(entity.userId).set(hashmap)
            .addOnSuccessListener {
                result(SuccessResult(""))
            }
            .addOnFailureListener {
                result(ErrorResult(""))

            }

    }

    override fun update(entity: Fellow, result: (IResult) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun delete(entity: Fellow, result: (IResult) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getAll(iDataResult: (IDataResult<ArrayList<Fellow>>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getById(id: String, iDataResult: (IDataResult<ArrayList<Fellow>>) -> Unit) {
        cloudFirebase = FirebaseFirestore.getInstance()
        val fellowList = ArrayList<Fellow>()
        cloudFirebase.collection(FirebaseCollection.FELLOW_COLLECTION)
            .document(id)
            .collection(id)
            .get()
            .addOnSuccessListener { result ->

                if(!result.isEmpty){
                    for (document in result){
                        val userId = document.get("UserId").toString()
                        val status = document.get("Status") as Boolean
                        fellowList.add(Fellow(userId,status));
                        iDataResult(SuccessDataResult(fellowList, ""))
                    }
                }
                iDataResult(ErrorDataResult(fellowList, ""))

            }
            .addOnFailureListener {

            }
    }

}