package com.beratyesbek.Vhoops.Views.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.beratyesbek.Vhoops.Business.Concrete.FellowManager
import com.beratyesbek.Vhoops.Business.Concrete.FriendRequestManager
import com.beratyesbek.Vhoops.Business.Concrete.UserManager
import com.beratyesbek.Vhoops.Core.Constants.Constants
import com.beratyesbek.Vhoops.Core.DataAccess.Concrete.FirebaseFriendRequestDal
import com.beratyesbek.Vhoops.DataAccess.Concrete.FellowDal
import com.beratyesbek.Vhoops.DataAccess.Concrete.FriendRequestDal

import com.beratyesbek.Vhoops.DataAccess.Concrete.UserDal
import com.beratyesbek.Vhoops.Entities.Concrete.Fellow
import com.beratyesbek.Vhoops.Entities.Concrete.FriendRequest
import com.beratyesbek.Vhoops.Entities.Concrete.User
import com.beratyesbek.Vhoops.databinding.ActivityUserBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import java.util.*

class UserActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUserBinding
    private lateinit var user :User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val userId = intent.getStringExtra(Constants.USER_ID)

        getFellowInfo(userId!!);

        getData(userId)
        println(userId);
        binding.btnAddFriendUserProfile.setOnClickListener {
            sendFriendRequest(userId)
        }

    }
    private fun getData(userId:String){
        val userDal : UserDal = UserDal()
        val userManager = UserManager(userDal)
        userManager.getById(userId){ result ->
            if (result.success()){
                user = result.data().get(0)
                binding.textViewFirstNameUserProfile.text = user.firstName
                binding.textViewLastNameUserProfile.text = user.lastName
                binding.textViewUserNameUserProfile.text = user.userName
                if(user.profileImage != null){
                    Picasso.get().load(user.profileImage).into(binding.imageViewUserProfile)
                }

                if(user.about != null){
                    binding.textViewAboutUserProfile.text = user.about

                }else{
                    binding.textViewAboutUserProfile.text = "açıklama yok"

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