package com.beratyesbek.vhoops.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.beratyesbek.vhoops.Business.Concrete.GroupChatManager
import com.beratyesbek.vhoops.R
import com.beratyesbek.vhoops.entities.concrete.GroupChat
import com.google.firebase.Timestamp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var groupChatManager: GroupChatManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        groupChatManager.update(GroupChat("","","",false, Timestamp.now())){

        }

        val intent = Intent(this,LoginActivity::class.java);
        startActivity(intent)


    }

}