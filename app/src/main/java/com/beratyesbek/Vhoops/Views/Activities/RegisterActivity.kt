package com.beratyesbek.vhoops.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.beratyesbek.vhoops.Business.Concrete.UserManager
import com.beratyesbek.vhoops.DataAccess.Concrete.UserDal
import com.beratyesbek.vhoops.entities.concrete.User
import com.beratyesbek.vhoops.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {


    private lateinit var dataBinding: ActivityRegisterBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = dataBinding.root
        setContentView(view)

        

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
                            Toast.makeText(this,createResult.message(),Toast.LENGTH_LONG).show()
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



}