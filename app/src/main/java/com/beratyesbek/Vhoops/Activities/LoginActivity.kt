package com.beratyesbek.Vhoops.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.beratyesbek.Vhoops.Business.Concrete.UserManager
import com.beratyesbek.Vhoops.DataAccess.IUserDal
import com.beratyesbek.Vhoops.DataAccess.UserDal
import com.beratyesbek.Vhoops.Entities.Concrete.User

import com.beratyesbek.Vhoops.databinding.ActivityLoginBinding


class LoginActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        binding.btnRegister.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, RegisterActivity::class.java);
            startActivity(intent)
        })
        binding.btnLogin.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, NavigationBottomActivity::class.java);
            startActivity(intent)
        })










//
//        val userDal: UserDal = UserDal();
//        val userManager = UserManager(userDal);
//        userManager.createUser(User("","yesbek","omera25s@gmail.com","beratybk","5566","123458")){
//            println(it.message() +  " result : "  + it.success())
//        }

//
//        val userDal: UserDal = UserDal();
//        val userManager = UserManager(userDal);
//        userDal.getData {
//            val message = it.message();
//            val data = it.data();
//            val result = it.success();
//        }


//            val message = it.message();
//            val data = it.data();
//            val result = it.success();
//


    }


}


