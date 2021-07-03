package com.beratyesbek.vhoops.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.beratyesbek.vhoops.core.constants.Constants
import com.beratyesbek.vhoops.core.utilities.animations.Animation
import com.beratyesbek.vhoops.core.utilities.extensions.downloadFromUrl

import com.beratyesbek.vhoops.mvvm.UserViewModel
import com.beratyesbek.vhoops.entities.concrete.User
import com.beratyesbek.vhoops.databinding.ActivityUserBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserActivity : AppCompatActivity() {
    private lateinit var dataBinding: ActivityUserBinding
    private lateinit var user: User
    private val viewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityUserBinding.inflate(layoutInflater)
        val view = dataBinding.root
        setContentView(view)

        val userId = intent.getStringExtra(Constants.USER_ID)
        val currentUser = FirebaseAuth.getInstance().currentUser.uid

        viewModel.getData(userId!!)

        if (userId == currentUser) {
            dataBinding.btnAddFriendUserProfile.visibility = View.GONE
        } else {
            viewModel.getFellowInfo(userId!!)
            viewModel.getFriendRequest(userId!!)
        }

        viewModel.userLiveData.observe(this, { result ->
            dataBinding.userData = result

            dataBinding.imageViewUserProfile.downloadFromUrl(
                result.profileImage.toString(),
                CircularProgressDrawable(this)
            )

        })



        dataBinding.btnAddFriendUserProfile.setOnClickListener {
            dataBinding?.progressBarActivityUser!!.visibility = View.VISIBLE
            viewModel.sendFriendRequest(userId)
        }

        dataBinding.btnCancelFriendUserProfile.setOnClickListener {
            dataBinding?.progressBarActivityUser?.visibility = View.VISIBLE
            viewModel.removeFriendRequest(userId)
        }
        dataBinding.btnBackUserProfile.setOnClickListener {
            onBackPressed()
        }


        viewModel.liveDataFriendRequestResult.observe(this,{ result ->
                if (result.data()!!.status) {
                    dataBinding!!.btnAddFriendUserProfile.visibility = View.INVISIBLE
                } else {
                    dataBinding!!.btnCancelFriendUserProfile.visibility = View.VISIBLE

            }
        })
        viewModel.liveDataFellowResult.observe(this,{ result ->
                if (!result.data()!!.status) {
                    dataBinding!!.btnAddFriendUserProfile.visibility = View.INVISIBLE
                } else {
                    dataBinding!!.btnAddFriendUserProfile.visibility = View.VISIBLE

            }

        })

        viewModel.liveDataFriendRequestRemoveResult.observe(this,{ result ->
            if (result.success()) {
                dataBinding!!.progressBarActivityUser.visibility = View.INVISIBLE
                Animation.revealAnim(dataBinding!!.btnAddFriendUserProfile)
                Animation.hideAnim(dataBinding!!.btnCancelFriendUserProfile)
            }

        })

        viewModel.liveDataFriendRequestSendResult.observe(this,{ result ->
            if (result.success()) {
                dataBinding?.progressBarActivityUser?.visibility = View.INVISIBLE
                Animation.hideAnim(dataBinding!!.btnAddFriendUserProfile)
                Animation.revealAnim(dataBinding!!.btnCancelFriendUserProfile)
            }
        })

        viewModel.liveDataFellowResult.observe(this,{ result ->

        })


    }
}