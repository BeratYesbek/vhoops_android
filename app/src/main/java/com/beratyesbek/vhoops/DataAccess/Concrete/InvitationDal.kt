package com.beratyesbek.vhoops.DataAccess.Concrete

import android.net.Uri
import com.beratyesbek.vhoops.Core.DataAccess.Concrete.FirebaseInvitationDal
import com.beratyesbek.vhoops.Core.DataAccess.Constants.FirebaseCollection
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Concrete.ErrorDataResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Concrete.SuccessDataResult
import com.beratyesbek.vhoops.DataAccess.Abstract.IInvitationDal
import com.beratyesbek.vhoops.entities.concrete.Invitation
import com.beratyesbek.vhoops.entities.concrete.dtos.ChatListDto
import com.beratyesbek.vhoops.entities.concrete.dtos.InvitationDto
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class InvitationDal : FirebaseInvitationDal(),IInvitationDal {

    private lateinit var cloudFirestore: FirebaseFirestore
    private lateinit var firebaseAuth : FirebaseAuth

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
                if (!querySnapshot.isEmpty){
                    for(document in querySnapshot){
                        val senderId = document["SenderId"].toString()
                        val receiverId = document["ReceiverId"].toString()
                        val invitationToDate = document["InvitationToDate"] as Timestamp
                        val type = document["Type"].toString()
                        invitationList.add(InvitationDto(senderId,receiverId,invitationToDate,type,null,null,null,null,null))

                    }
                    getUserData(invitationList){
                        if (it.success()){
                            iDataResult(SuccessDataResult(it.data(),"Data has been listed"))

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

                } else{
                    iDataResult(ErrorDataResult(null,""))
                }
            }
    }

}