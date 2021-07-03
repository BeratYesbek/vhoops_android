package com.beratyesbek.vhoops.dataAccess.concretes

import android.net.Uri
import com.beratyesbek.vhoops.core.dataAccess.concretes.FirebaseInvitationDal
import com.beratyesbek.vhoops.core.dataAccess.constants.FirebaseCollection
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IDataResult
import com.beratyesbek.vhoops.core.utilities.results.Concrete.ErrorDataResult
import com.beratyesbek.vhoops.core.utilities.results.Concrete.SuccessDataResult
import com.beratyesbek.vhoops.dataAccess.abstracts.IInvitationDal
import com.beratyesbek.vhoops.entities.concrete.dtos.InvitationDto
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class InvitationDal : FirebaseInvitationDal(), IInvitationDal {

    private lateinit var cloudFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    override fun getDetailsByUserId(
        userId: String,
        iDataResult: (IDataResult<ArrayList<InvitationDto>>) -> Unit
    ) {
        val invitationList = ArrayList<InvitationDto>()
        cloudFirestore = FirebaseFirestore.getInstance()
        cloudFirestore.collection(FirebaseCollection.INVITATION_COLLECTION)
            .document(userId)
            .collection(userId).get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    firebaseAuth = FirebaseAuth.getInstance()
                    val currentUserId = firebaseAuth.currentUser.uid
                    for (document in querySnapshot) {
                        val senderId = document["SenderId"].toString()
                        val receiverId = document["ReceiverId"].toString()
                        val invitationToDate = document["InvitationToDate"] as Timestamp
                        val type = document["Type"].toString()
                        if (senderId == currentUserId || receiverId == currentUserId) {
                            invitationList.add(
                                InvitationDto(
                                    senderId,
                                    receiverId,
                                    invitationToDate,
                                    type,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null
                                )
                            )
                        }


                    }
                    getUserData(invitationList) {
                        if (it.success()) {
                            iDataResult(SuccessDataResult(it.data(), "Data has been listed"))

                        }
                    }
                }


            }.addOnFailureListener {
                iDataResult(SuccessDataResult(null, "Data has not been listed "))

            }
    }


    private fun getUserData(
        invitationList: ArrayList<InvitationDto>,
        iDataResult: (IDataResult<ArrayList<InvitationDto>>) -> Unit
    ) {
        val cloudFirebase = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUserId = firebaseAuth.currentUser.uid
        cloudFirebase.collection(FirebaseCollection.USER_COLLECTION)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    invitationList.forEachIndexed { index, invitationDto ->
                        for (item in result.documents) {
                            val userId = item.get("UserID").toString()
                            val firstName = item.get("FirstName").toString()
                            val lastName = item.get("LastName").toString()
                            val profileImage = item.get("ProfileImage").toString()
                            val token = item.get("Token").toString()

                            if (invitationDto.senderId == userId && invitationDto.senderId != currentUserId ||
                                invitationDto.receiverId == userId && invitationDto.receiverId != currentUserId
                            ) {
                                invitationList[index].userId = userId
                                invitationList[index].firstName = lastName
                                invitationList[index].lastName = firstName
                                invitationList[index].userToken = token
                                if (profileImage != null) {
                                    invitationList[index].userPicture = Uri.parse(profileImage)
                                }
                                break
                            }

                        }

                    }

                    iDataResult(SuccessDataResult(invitationList, ""))

                } else {
                    iDataResult(ErrorDataResult(null, ""))
                }
            }
    }

}