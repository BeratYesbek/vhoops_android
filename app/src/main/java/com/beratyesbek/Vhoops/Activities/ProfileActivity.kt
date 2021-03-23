package com.beratyesbek.Vhoops.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.beratyesbek.Vhoops.R
import com.beratyesbek.Vhoops.databinding.ActivityProfileAcitivtyBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileAcitivtyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileAcitivtyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.includeProfileActivity.toolbar)
        getSupportActionBar()!!.setDisplayShowTitleEnabled(false);
    }
}