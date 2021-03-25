package com.beratyesbek.Vhoops.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.beratyesbek.Vhoops.R
import com.beratyesbek.Vhoops.databinding.ActivityProfileAcitivtyBinding

class ProfileEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileAcitivtyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)

        setSupportActionBar(binding.includeProfileActivity.toolbar)
        getSupportActionBar()!!.setDisplayShowTitleEnabled(false);
    }
}