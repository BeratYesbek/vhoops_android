package com.beratyesbek.Vhoops.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.beratyesbek.Vhoops.Entities.Concrete.User
import com.beratyesbek.Vhoops.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val user = User("berat","yesbek","beratyesbek@gmail.com","beratybk","655654985665");

    }
}