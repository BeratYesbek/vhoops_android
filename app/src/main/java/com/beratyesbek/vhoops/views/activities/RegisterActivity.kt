package com.beratyesbek.vhoops.views.activities

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.Toast
import com.beratyesbek.vhoops.business.concretes.UserManager
import com.beratyesbek.vhoops.core.EmailVerification
import com.beratyesbek.vhoops.dataAccess.concretes.UserDal
import com.beratyesbek.vhoops.R
import com.beratyesbek.vhoops.entities.concrete.User
import com.beratyesbek.vhoops.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {


    private lateinit var dataBinding: ActivityRegisterBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = dataBinding.root
        setContentView(view)

        dataBinding.btnBackRegister.setOnClickListener {
            finish()
        }
    }


    fun btnRegister(view: View) {

        val firstName = dataBinding.editTextRegisterFirstName.text.toString()
        val lastName =  dataBinding.editTextRegisterLastName.text.toString()
        val email = dataBinding.editTextRegisterEmail.text.toString()
        val userName = dataBinding.editTextRegisterUserName.text.toString()
        val password = dataBinding.editTextRegisterPassword.text.toString()
        val passwordAgain = dataBinding.editTextRegisterPasswordAgain.text.toString()


        if(password.equals(passwordAgain)){
            val userDal: UserDal = UserDal();
            val userManager = UserManager(userDal);
            userManager.add(User(firstName,lastName,email,userName,password)){ result ->

                if(result.success()){
                    userManager.createUser(User(firstName,lastName,email,userName,password)){ createResult ->
                        if(createResult.success()){
                            EmailVerification.sendEmail()
                            showCustomDialog()
                        }else{
                            Toast.makeText(this,createResult.message(),Toast.LENGTH_LONG).show();
                        }
                    }

                }else{
                    Toast.makeText(this,result.message(),Toast.LENGTH_LONG).show();
                }

            }
        }else{
            Toast.makeText(this,"passwords are not equals each other",Toast.LENGTH_LONG).show();
        }
    }
    private fun showCustomDialog(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_email_verfication_dialog)

        val btnClose = dialog.findViewById<Button>(R.id.btn_verification_email)

        btnClose?.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }



}