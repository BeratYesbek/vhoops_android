package com.beratyesbek.vhoops.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.beratyesbek.vhoops.Business.Concrete.FellowManager
import com.beratyesbek.vhoops.Business.Concrete.FriendRequestManager
import com.beratyesbek.vhoops.Business.Concrete.UserManager
import com.beratyesbek.vhoops.Core.Constants.Constants
import com.beratyesbek.vhoops.Core.Utilities.Animation.Animation
import com.beratyesbek.vhoops.DataAccess.Concrete.FellowDal
import com.beratyesbek.vhoops.DataAccess.Concrete.FriendRequestDal

import com.beratyesbek.vhoops.DataAccess.Concrete.UserDal
import com.beratyesbek.vhoops.entities.concrete.FriendRequest
import com.beratyesbek.vhoops.entities.concrete.User
import com.beratyesbek.vhoops.databinding.ActivityUserBinding
import com.beratyesbek.vhoops.entities.concrete.Fellow
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import java.util.*

class UserActivity : AppCompatActivity() {
    private lateinit var dataBinding:ActivityUserBinding
    private lateinit var user :User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityUserBinding.inflate(layoutInflater)
        val view = dataBinding.root
        setContentView(view)

        val userId = intent.getStringExtra(Constants.USER_ID)
        val currentUser =FirebaseAuth.getInstance().currentUser.uid
        if (userId == currentUser){
            dataBinding.btnAddFriendUserProfile.visibility = View.GONE
        }else{
            getFellowInfo(userId!!);
            getFriendRequest(userId)
        }
        getData(userId)



        dataBinding.btnAddFriendUserProfile.setOnClickListener {
            sendFriendRequest(userId)
        }

        dataBinding.btnCancelFriendUserProfile.setOnClickListener {
            removeFriendRequest(userId)
        }

    }
    private fun getData(userId:String){
        val userDal : UserDal = UserDal()
        val userManager = UserManager(userDal)
        userManager.getById(userId){ result ->
            if (result.success()){
                user = result.data().get(0)
                dataBinding.textViewFirstNameUserProfile.text = user.firstName
                dataBinding.textViewLastNameUserProfile.text = user.lastName
                dataBinding.textViewUserNameUserProfile.text = user.userName
                if(user.profileImage != null){
                    Picasso.get().load(user.profileImage).into(dataBinding.imageViewUserProfile)
                }

                if(user.about != null){
                    dataBinding.textViewAboutUserProfile.text = user.about

                }else{
                    dataBinding.textViewAboutUserProfile.text = "açıklama yok"

                }

            }
        }
    }

    private fun getFriendRequest(receiverId : String){
        val senderId = FirebaseAuth.getInstance().currentUser.uid
        val friendRequestDal = FriendRequestDal()
        val friendRequestManager = FriendRequestManager(friendRequestDal)
        friendRequestManager.getBySenderAndReceiverId(senderId,receiverId){ result ->
            if (result.success()){
                if (!result.data().status){
                    Animation.hideAnim(dataBinding.btnAddFriendUserProfile)
                    Animation.revealAnim(dataBinding.btnCancelFriendUserProfile)
                }
            }

        }
    }
    private fun getFellowInfo(userId : String){
        val fellowDal : FellowDal = FellowDal()
        val fellowManager = FellowManager(fellowDal)
        fellowManager.getByUserId(userId){ dataResult ->
            if(dataResult.success()){
                if (dataResult.data().status){
                    dataBinding.btnAddFriendUserProfile.visibility = View.INVISIBLE
                }else{
                    dataBinding.btnAddFriendUserProfile.visibility = View.VISIBLE

                }
            }
        }
    }


    private fun removeFriendRequest(receiverId: String){
        val senderId = FirebaseAuth.getInstance().currentUser.uid
        dataBinding.progressBarActivityUser.visibility = View.VISIBLE
        val friendRequestDal = FriendRequestDal()
        val friendRequestManager = FriendRequestManager(friendRequestDal)
        friendRequestDal.delete(FriendRequest(senderId,receiverId,"",false, Timestamp.now())){ result ->
            if (result.success()){
                dataBinding.progressBarActivityUser.visibility = View.INVISIBLE
                Animation.revealAnim(dataBinding.btnAddFriendUserProfile)
                Animation.hideAnim(dataBinding.btnCancelFriendUserProfile)
            }

        }
    }

    private fun sendFriendRequest(receiverId : String){
        val senderId = FirebaseAuth.getInstance().currentUser.uid
        dataBinding.progressBarActivityUser.visibility = View.VISIBLE
        val friendRequestDal = FriendRequestDal()
        val friendRequestManager = FriendRequestManager(friendRequestDal)
        val documentID = UUID.randomUUID().toString()
        friendRequestDal.add(FriendRequest(senderId,receiverId,documentID,false, Timestamp.now())){ result ->
            if (result.success()){
                dataBinding.progressBarActivityUser.visibility = View.INVISIBLE
                Animation.hideAnim(dataBinding.btnAddFriendUserProfile)
                Animation.revealAnim(dataBinding.btnCancelFriendUserProfile)
            }

        }

    }

}