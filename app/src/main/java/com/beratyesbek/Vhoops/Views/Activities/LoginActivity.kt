package com.beratyesbek.Vhoops.Views.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.beratyesbek.Vhoops.Business.Concrete.UserManager
import com.beratyesbek.Vhoops.DataAccess.UserDal

import com.beratyesbek.Vhoops.databinding.ActivityLoginBinding


class LoginActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        binding.btnRegister.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, RegisterActivity::class.java);
            startActivity(intent)
        })
        binding.btnLogin.setOnClickListener(View.OnClickListener {
            login()
        })
    }

    private fun login() {
        val email = binding.editTextLoginEmail.text.toString()
        val password = binding.editTextLoginPassword.text.toString()

        val userDal: UserDal = UserDal()
        val userManager = UserManager(userDal)
        userManager.loginUser(email, password) { result ->
            if (result.success()) {
                val intent = Intent(this, NavigationBottomActivity::class.java);
                startActivity(intent)
            } else {
                Toast.makeText(this, result.message(), Toast.LENGTH_LONG).show()
            }
        }
    }

}


