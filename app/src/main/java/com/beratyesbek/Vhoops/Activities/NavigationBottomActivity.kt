package com.beratyesbek.Vhoops.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.beratyesbek.Vhoops.R
import com.beratyesbek.Vhoops.databinding.ActivityNavigationBottomBinding

class NavigationBottomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigationBottomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBottomBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }
}