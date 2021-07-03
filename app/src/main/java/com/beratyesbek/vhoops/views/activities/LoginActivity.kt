package com.beratyesbek.vhoops.views.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import com.beratyesbek.vhoops.core.EmailVerification
import com.beratyesbek.vhoops.mvvm.LoginViewModel
import com.beratyesbek.vhoops.R
import com.beratyesbek.vhoops.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity() : AppCompatActivity() {

    private lateinit var dataBinding: ActivityLoginBinding
    private val viewModel : LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = dataBinding.root

        setContentView(view)

        viewModel.context = applicationContext

        dataBinding.textViewForgetPassword.setOnClickListener {
            customDialog()
        }

        dataBinding.btnRegister.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, RegisterActivity::class.java);
            startActivity(intent)
        })
        dataBinding.btnLogin.setOnClickListener(View.OnClickListener {
            val email = dataBinding.editTextLoginEmail.text.toString()
            val password = dataBinding.editTextLoginPassword.text.toString()

            if (!email.isNullOrEmpty() && !password.isNullOrEmpty()){
                dataBinding.progressBarLoginActivity.visibility = View.VISIBLE
                dataBinding.btnLogin.isEnabled = false
                dataBinding.btnRegister.isEnabled = false

                viewModel.login(email,password)
            }else{
                Toast.makeText(this,"Email and Password must not be empty",Toast.LENGTH_LONG).show()
            }

        })
        viewModel.resultLiveData.observe(this,{
            if (it.success()){
                dataBinding.progressBarLoginActivity.visibility = View.INVISIBLE
                val intent = Intent(this, NavigationBottomActivity::class.java);
                startActivity(intent)
                finish()

            }else{
                dataBinding.btnLogin.isEnabled = true
                dataBinding.btnRegister.isEnabled = true
                dataBinding.progressBarLoginActivity.visibility = View.INVISIBLE
                Toast.makeText(this,it.message(),Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun customDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_password_reset_dialog)

        val btnClose = dialog.findViewById<Button>(R.id.btn_close_password_reset)
        val btnSend = dialog.findViewById<Button>(R.id.btn_send_password_reset)
        val editTextEmail = dialog.findViewById<EditText>(R.id.editText_password_reset)


        btnSend.setOnClickListener {
            val email = editTextEmail.text.toString()
            if (!email.isNullOrEmpty()){
                EmailVerification.resetPassword(email)
                messageDialog()
                dialog.dismiss()

            }
        }
        btnClose?.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun messageDialog(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_message_password_dialog)

        val btnClose = dialog.findViewById<Button>(R.id.btn_custom_message_close_dialog)

        btnClose?.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

}


