package com.beratyesbek.Vhoops.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.beratyesbek.Vhoops.Business.Concrete.UserManager
import com.beratyesbek.Vhoops.DataAccess.UserDal
import com.beratyesbek.Vhoops.Entities.Concrete.User
import com.beratyesbek.Vhoops.R
import com.beratyesbek.Vhoops.databinding.ActivityRegisterBinding
import java.lang.reflect.Array

class RegisterActivity : AppCompatActivity() {


    private lateinit var binding: ActivityRegisterBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        

    }


    fun btnRegister(view: View) {

        val firstName = binding.editTextRegisterFirstName.text.toString()
        val lastName =  binding.editTextRegisterLastName.text.toString()
        val email = binding.editTextRegisterEmail.text.toString()
        val userName = binding.editTextRegisterUserName.text.toString()
        val password = binding.editTextRegisterPassword.text.toString()
        val passwordAgain = binding.editTextRegisterPasswordAgain.text.toString()


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