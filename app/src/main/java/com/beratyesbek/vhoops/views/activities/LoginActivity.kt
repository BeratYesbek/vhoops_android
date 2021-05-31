package com.beratyesbek.vhoops.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.beratyesbek.vhoops.Business.Concrete.UserManager
import com.beratyesbek.vhoops.DataAccess.Concrete.UserDal
import com.beratyesbek.vhoops.databinding.ActivityLoginBinding


class LoginActivity() : AppCompatActivity() {

    private lateinit var dataBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = dataBinding.root

        setContentView(view)

        dataBinding.btnRegister.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, RegisterActivity::class.java);
            startActivity(intent)
        })
        dataBinding.btnLogin.setOnClickListener(View.OnClickListener {
            login()
        })
    }

    private fun login() {
        val email = dataBinding.editTextLoginEmail.text.toString()
        val password = dataBinding.editTextLoginPassword.text.toString()

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


