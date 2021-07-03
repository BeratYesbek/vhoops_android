package com.beratyesbek.vhoops.core.dataAccess.concretes

import com.beratyesbek.vhoops.core.dataAccess.abstracts.IFirebaseInvitationDal
import com.beratyesbek.vhoops.core.dataAccess.constants.FirebaseCollection
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IDataResult
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IResult
import com.beratyesbek.vhoops.core.utilities.results.Concrete.SuccessDataResult
import com.beratyesbek.vhoops.entities.concrete.Invitation
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

open class FirebaseInvitationDal : IFirebaseInvitationDal {

    private lateinit var cloudFirestore: FirebaseFirestore
    private lateinit var auth : FirebaseAuth


    override fun add(entity: Invitation, result: (IResult) -> Unit) {
        cloudFirestore = FirebaseFirestore.getInstance()

        val hashMap = hashMapOf(
            "SenderId" to entity.senderId,
            "ReceiverId" to entity.receiverId,
            "InvitationToDate" to entity.invitationToDate,
            "Type" to "Incoming"
        )



        cloudFirestore.collection(FirebaseCollection.INVITATION_COLLECTION)
            .document(entity.receiverId!!).collection(entity.receiverId!!).add(hashMap)
            .addOnSuccessListener {

            }
        hashMap["Type"] = "Outgoing"

        cloudFirestore.collection(FirebaseCollection.INVITATION_COLLECTION)
            .document(entity.senderId!!)
            .collection(entity.senderId!!)
            .add(hashMap)
            .addOnSuccessListener {

            }

    }

    override fun update(entity: Invitation, result: (IResult) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun delete(entity: Invitation, result: (IResult) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getAll(iDataResult: (IDataResult<ArrayList<Invitation>>) -> Unit) {



    }
    override fun getByUserId(
        userId: String,
        iDataResult: (IDataResult<ArrayList<Invitation>>) -> Unit
    ) {
        val invitationList = ArrayList<Invitation>()
        cloudFirestore.collection(FirebaseCollection.INVITATION_COLLECTION)
            .document(userId)
            .collection(userId).get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty){
                    for(document in querySnapshot){
                        val senderId = document["SenderId"].toString()
                        val receiverId = document["ReceiverId"].toString()
                        val invitationToDate = document["InvitationToDate"] as Timestamp
                        val type = document["Type"].toString()
                        invitationList.add(Invitation(senderId,receiverId,invitationToDate,type))

                    }
                    iDataResult(SuccessDataResult(invitationList,"Data has been listed"))
                }


            }.addOnFailureListener {
                iDataResult(SuccessDataResult(null,"Data has not been listed "))

            }
    }

    override fun getById(id: String, iDataResult: (IDataResult<ArrayList<Invitation>>) -> Unit) {

    }
}