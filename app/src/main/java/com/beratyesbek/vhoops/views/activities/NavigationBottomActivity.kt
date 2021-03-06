package com.beratyesbek.vhoops.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.beratyesbek.vhoops.business.abstracts.IUserService
import com.beratyesbek.vhoops.core.firebaseCloudMessaging.FirebaseTokenOperationService
import com.beratyesbek.vhoops.mvvm.NavigationBottomViewModel
import com.beratyesbek.vhoops.R
import com.beratyesbek.vhoops.databinding.ActivityNavigationBottomBinding
import com.beratyesbek.vhoops.entities.concrete.User
import com.beratyesbek.vhoops.views.fragment.*
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class NavigationBottomActivity : AppCompatActivity() {

    private lateinit var dataBinding: ActivityNavigationBottomBinding
    private lateinit var _nowFragment: String;
    private lateinit var transaction: FragmentTransaction;

    @Inject
    lateinit var userService: IUserService
    private val viewModel: NavigationBottomViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityNavigationBottomBinding.inflate(layoutInflater)
        val view = dataBinding.root
        setContentView(view);

        val toolbar = dataBinding.include.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        viewModel.getFriendRequestDetails()

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


        dataBinding.bottomNavigation.setOnClickMenuListener { click ->
            when (click.id) {
                1 -> makeCurrentFragment(homeFragment, "homeFragment")
                2 -> makeCurrentFragment(groupFragment, "groupFragment")
                3 -> makeCurrentFragment(callFragment, "callFragment")

            }


        }

        dataBinding.include.toolbarAddFriendButton.setOnClickListener {
            setSearchFragment()
        }




        dataBinding.include.btnToolbarNotification.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }

        dataBinding.btnPersons.setOnClickListener {
            val intentToPersonsActivity = Intent(this, PersonsActivity::class.java)
            startActivity(intentToPersonsActivity)
        }

        viewModel.notificationResult.observe(this, { result ->
            if (result) {
                dataBinding.include.relativeLayoutRedPointNoti.visibility = View.VISIBLE
            } else {
                dataBinding.include.relativeLayoutRedPointNoti.visibility = View.INVISIBLE

            }
        })


    }

    private fun updateToken() {
        val firebaseTokenOperationService = FirebaseTokenOperationService()
        firebaseTokenOperationService.getToken(this)
    }

    private fun updateTokenAndUserId() {
        val email = FirebaseAuth.getInstance().currentUser.email
        userService.getByEmail(email) { dataResult ->
            if (dataResult.success()) {
                val user: User = dataResult.data()!!.get(0);
                user.userID = FirebaseAuth.getInstance().currentUser.uid
                userService.update(user) {
                    updateToken()
                }
            } else {

            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intentToProfileActivity = Intent(this, ProfileActivity::class.java)
        when (item.itemId) {
            R.id.profile -> startActivity(intentToProfileActivity)
            R.id.exitAccount -> {
                FirebaseAuth.getInstance().signOut()
                val intentToLogin = Intent(this, LoginActivity::class.java)
                startActivity(intentToLogin)
                finish()
            }
            R.id.feedBack -> {
            }
            else -> return super.onOptionsItemSelected(item);
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
        if (supportFragmentManager.findFragmentById(R.id.search_people_frameLayout) != null) {
            removeSearchFragment()
        } else {
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

    private fun removeSearchFragment() {
        val fragment: Fragment? =
            supportFragmentManager.findFragmentById(R.id.search_people_frameLayout)
        transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.fade_out, R.anim.fade_in_anim)
        transaction.remove(fragment!!)
        transaction.commit()
    }


}


