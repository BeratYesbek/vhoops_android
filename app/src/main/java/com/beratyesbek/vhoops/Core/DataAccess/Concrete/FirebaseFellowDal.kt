package com.beratyesbek.vhoops.Core.DataAccess.Concrete

import com.beratyesbek.vhoops.Core.DataAccess.Abstract.IFirebaseFellowDal
import com.beratyesbek.vhoops.Core.DataAccess.Constants.FirebaseCollection
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Concrete.ErrorDataResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Concrete.ErrorResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Concrete.SuccessDataResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Concrete.SuccessResult
import com.beratyesbek.vhoops.entities.concrete.Fellow
import com.google.firebase.firestore.FirebaseFirestore

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
                        println("fellow list size " + fellowList.size)
                        iDataResult(SuccessDataResult(fellowList, ""))
                    }
                }else{
                    iDataResult(ErrorDataResult(fellowList, ""))
                }

            }
            .addOnFailureListener {

            }
    }

}