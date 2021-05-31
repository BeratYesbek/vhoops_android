package com.beratyesbek.vhoops.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.beratyesbek.vhoops.Business.Concrete.FellowManager
import com.beratyesbek.vhoops.Business.Concrete.FriendRequestManager
import com.beratyesbek.vhoops.Business.Concrete.UserManager
import com.beratyesbek.vhoops.Core.Constants.Constants
import com.beratyesbek.vhoops.DataAccess.Concrete.FellowDal
import com.beratyesbek.vhoops.DataAccess.Concrete.FriendRequestDal

import com.beratyesbek.vhoops.DataAccess.Concrete.UserDal
import com.beratyesbek.vhoops.entities.concrete.FriendRequest
import com.beratyesbek.vhoops.entities.concrete.User
import com.beratyesbek.vhoops.databinding.ActivityUserBinding
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

        getFellowInfo(userId!!);

        getData(userId)
        println(userId);
        dataBinding.btnAddFriendUserProfile.setOnClickListener {
            sendFriendRequest(userId)
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
    private fun getFellowInfo(userId : String){
        //val fellow = Fellow(userId,false)
        val fellowDal : FellowDal = FellowDal()
        val fellowManager = FellowManager(fellowDal)
        fellowManager.getById(userId){ dataResult ->
            if(dataResult.success()){
               // binding.btnAddFriendUserProfile.visibility = View.INVISIBLE

            }
        }
    }

    private fun sendFriendRequest(receiverId : String){
        val senderId = FirebaseAuth.getInstance().currentUser.uid
        val friendRequestDal = FriendRequestDal()
        val friendRequestManager = FriendRequestManager(friendRequestDal)
        val documentID = UUID.randomUUID().toString()
        friendRequestDal.add(FriendRequest(senderId,receiverId,documentID,false, Timestamp.now())){

        }

    }

}