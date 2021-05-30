package com.beratyesbek.vhoops.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.beratyesbek.vhoops.Business.Concrete.UserManager
import com.beratyesbek.vhoops.DataAccess.Concrete.UserDal
import com.beratyesbek.vhoops.entities.concrete.User
import com.beratyesbek.vhoops.R
import com.beratyesbek.vhoops.views.fragment.*
import com.beratyesbek.vhoops.databinding.ActivityNavigationBottomBinding
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.custom_search_item.view.*
import kotlinx.android.synthetic.main.nav_toolbar.view.*


class NavigationBottomActivity : AppCompatActivity() {

    private lateinit var dataBinding: ActivityNavigationBottomBinding
    private lateinit var _nowFragment: String;
    private lateinit var transaction: FragmentTransaction;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityNavigationBottomBinding.inflate(layoutInflater)
        val view = dataBinding.root
        setContentView(view);
        //toolbar
        setSupportActionBar(dataBinding.include.toolbar)

        inVisibleSearchBar()

        dataBinding.bottomNavigation.show(1)
        val bottomNavigation = dataBinding.bottomNavigation
        bottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.ic_home))
        bottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.ic_people))
        bottomNavigation.add(MeowBottomNavigation.Model(3, R.drawable.ic_call))



        val homeFragment = HomeFragment()
        val groupFragment = GroupFragment()
        val callFragment = CallFragment()

        makeCurrentFragment(homeFragment, "homeFragment")

        updateTokenAndUserId()


        dataBinding.bottomNavigation.setOnClickMenuListener  { click ->
            when (click.id) {
                1 -> makeCurrentFragment(homeFragment, "homeFragment")
                2 -> makeCurrentFragment(groupFragment, "groupFragment")
                3-> makeCurrentFragment(callFragment, "callFragment")

            }


            if (dataBinding.relativeIncludeNavbar.isVisible) {
                inVisibleSearchBar()

            }
        }

        dataBinding.include.toolbar_search_button.setOnClickListener {
            visibleSearchBar()


        }
        dataBinding.searchInclude.btn_search_back.setOnClickListener {
            inVisibleSearchBar()
        }

        dataBinding.include.btn_toolbar_notification.setOnClickListener {
            val intent = Intent(this,NotificationActivity::class.java)
            startActivity(intent)
        }

        dataBinding.btnPersons.setOnClickListener {
            val intentToPersonsActivity = Intent(this,PersonsActivity::class.java)
            startActivity(intentToPersonsActivity)
        }

    }
    private fun updateTokenAndUserId(){
        val email = FirebaseAuth.getInstance().currentUser.email
        val userDal : UserDal = UserDal()
        val userManager = UserManager(userDal)
        userManager.getByEmail(email){dataResult ->
            if(dataResult.success()){
                val user : User = dataResult.data().get(0);
                user.userID = FirebaseAuth.getInstance().currentUser.uid
                userManager.update(user){

                }
            }else{

            }
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
        if (dataBinding.relativeIncludeNavbar.isVisible) {
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
        transaction.setCustomAnimations(R.anim.fade_out, R.anim.fade_in_anim)
        transaction.remove(fragment!!)
        transaction.commit()
    }


    private fun inVisibleSearchBar() {

        val slideUp: Animation = AnimationUtils.loadAnimation(this, R.anim.search_bar_anim_exit)
        dataBinding.relativeIncludeNavbar.startAnimation(slideUp)
        dataBinding.relativeIncludeNavbar.visibility = View.INVISIBLE
        dataBinding.searchInclude.editText_search.visibility = View.INVISIBLE
        dataBinding.searchInclude.btn_search_back.visibility = View.INVISIBLE

    }

    private fun visibleSearchBar() {
        if (_nowFragment.equals("friendFragment")) {
            setSearchFragment()
        } else {
            val slideUp: Animation =
                AnimationUtils.loadAnimation(this, R.anim.search_bar_anim_enter)
            dataBinding.relativeIncludeNavbar.startAnimation(slideUp)
            dataBinding.relativeIncludeNavbar.visibility = View.VISIBLE
            dataBinding.searchInclude.editText_search.visibility = View.VISIBLE
            dataBinding.searchInclude.btn_search_back.visibility = View.VISIBLE
        }
    }



}


