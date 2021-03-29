package com.beratyesbek.Vhoops.Views.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.beratyesbek.Vhoops.Business.Concrete.UserManager

import com.beratyesbek.Vhoops.DataAccess.UserDal
import com.beratyesbek.Vhoops.Entities.Concrete.User
import com.beratyesbek.Vhoops.databinding.ActivityUserBinding
import com.squareup.picasso.Picasso

class UserActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUserBinding
    private lateinit var user :User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val userId = intent.getStringExtra("userId")
        getData(userId!!)
    }
    private fun getData(userId:String){
        val userDal :UserDal = UserDal()
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
}