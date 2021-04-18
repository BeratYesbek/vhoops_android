package com.beratyesbek.Vhoops.Views.Activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.beratyesbek.Vhoops.Core.Constants.Constants
import com.beratyesbek.Vhoops.R
import com.beratyesbek.Vhoops.databinding.ActivityChatBinding
import com.squareup.picasso.Picasso
import java.net.URI

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val userId = intent.getStringExtra(Constants.USER_ID)
        val fullName = intent.getStringExtra(Constants.FULL_NAME)
        val profileImage = intent.getStringExtra(Constants.PROFILE_IMAGE)
        val uri : Uri

        val imageView = binding.includeChatActivity.imageViewProfileChatToolbar

        if (profileImage != null) {
            uri = Uri.parse(profileImage)
            Picasso.get().load(uri).into(imageView)
        }
        binding.includeChatActivity.textViewUserFullNameChatActivity.text = fullName!!.toUpperCase()

    }
}