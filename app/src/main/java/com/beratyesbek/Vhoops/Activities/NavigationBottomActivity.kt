package com.beratyesbek.Vhoops.Activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.contains
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.adapter.FragmentViewHolder
import com.beratyesbek.Vhoops.Fragment.CallFragment
import com.beratyesbek.Vhoops.Fragment.FriendsFragment
import com.beratyesbek.Vhoops.Fragment.HomeFragment
import com.beratyesbek.Vhoops.Fragment.SearchFragment
import com.beratyesbek.Vhoops.R
import com.beratyesbek.Vhoops.databinding.ActivityNavigationBottomBinding
import java.lang.Exception


class NavigationBottomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigationBottomBinding
    private lateinit var _nowFragment: String;
    private lateinit var transaction: FragmentTransaction;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBottomBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view);
        //toolbar
        setSupportActionBar(binding.include.toolbar)
        getSupportActionBar()!!.setDisplayShowTitleEnabled(false);


       inVisibleSearchBar()


        val homeFragment = HomeFragment()
        val friendsFragment = FriendsFragment()
        val callFragment = CallFragment()

        makeCurrentFragment(homeFragment, "homeFragment")

        binding.bottomMenu.setOnItemSelectedListener {
            when (it) {
                R.id.home -> makeCurrentFragment(homeFragment, "homeFragment")
                R.id.friends -> makeCurrentFragment(friendsFragment, "friendFragment")
                R.id.call -> makeCurrentFragment(callFragment, "callFragment")
            }


            if (binding.relativeIncludeNavbar.isVisible) {
                inVisibleSearchBar()

            }
        }

        binding.include.toolbarSearchButton.setOnClickListener {
            visibleSearchBar()


        }
        binding.searchInclude.btnSearchBack.setOnClickListener {
            inVisibleSearchBar()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intentToProfileActivity = Intent(this,ProfileActivity::class.java)
        when(item.itemId){
            R.id.profile -> startActivity(intentToProfileActivity)
        }
        return true
    }
    /*---------------------------------set fragment-----------------------------*/
    private fun makeCurrentFragment(fragment: Fragment, nowFragment: String) =
        supportFragmentManager.beginTransaction().apply {
            _nowFragment = nowFragment;
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.exit_left, R.anim.enter_right);
            transaction.replace(R.id.nav_activity_fragment, fragment)
            transaction.commit()
        }

    override fun onBackPressed() {
        if (binding.relativeIncludeNavbar.isVisible) {
            inVisibleSearchBar()
        }
        else if (supportFragmentManager.findFragmentById(R.id.search_people_frameLayout) != null) {
            removeSearchFragment()
        }
        else {
            super.onBackPressed()

        }

    }
    /*----------------SearchBar and SearchFragment------------------*/
    private fun setSearchFragment() {

        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.fade_in_anim, R.anim.slide_out_anim)
        transaction.replace(R.id.search_people_frameLayout, SearchFragment())
        transaction.commit()
    }
    private fun removeSearchFragment(){
        val fragment: Fragment? = supportFragmentManager.findFragmentById(R.id.search_people_frameLayout)
        transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.fade_out, R.anim.fragment_fade_exit)
        transaction.remove(fragment!!)
        transaction.commit()
    }


    private fun inVisibleSearchBar() {

        val slideUp: Animation = AnimationUtils.loadAnimation(this, R.anim.search_bar_anim_exit)
        binding.relativeIncludeNavbar.startAnimation(slideUp)
        binding.relativeIncludeNavbar.visibility = View.INVISIBLE
        binding.searchInclude.editTextSearch.visibility = View.INVISIBLE
        binding.searchInclude.btnSearchBack.visibility = View.INVISIBLE

    }

    private fun visibleSearchBar() {
        if (_nowFragment.equals("friendFragment")) {
            setSearchFragment()
        } else {
            val slideUp: Animation =
                AnimationUtils.loadAnimation(this, R.anim.search_bar_anim_enter)
            binding.relativeIncludeNavbar.startAnimation(slideUp)
            binding.relativeIncludeNavbar.visibility = View.VISIBLE
            binding.searchInclude.editTextSearch.visibility = View.VISIBLE
            binding.searchInclude.btnSearchBack.visibility = View.VISIBLE
        }
    }



}


