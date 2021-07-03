package com.beratyesbek.vhoops.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.beratyesbek.vhoops.adapter.NotificationViewAdapter
import com.beratyesbek.vhoops.mvvm.NotificationViewModel
import com.beratyesbek.vhoops.databinding.ActivityNotificationBinding
import com.beratyesbek.vhoops.entities.concrete.dtos.FriendRequestDto
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.view.*
@AndroidEntryPoint
class NotificationActivity : AppCompatActivity() {

    private lateinit var dataBinding: ActivityNotificationBinding
    private  val friendRequestList = ArrayList<FriendRequestDto>()
    private lateinit var  notificationViewAdapter: NotificationViewAdapter

    private val viewModel : NotificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityNotificationBinding.inflate(layoutInflater)
        val view = dataBinding.root
        setContentView(view)

        setSupportActionBar(dataBinding.includeNotificationActivity.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        runRecyclerView()

        viewModel.getFriendRequestDetails()

        viewModel.friendRequestList.observe(this,{  arrayList ->
            friendRequestList.addAll(arrayList)
            notificationViewAdapter.notifyDataSetChanged()
        })
        dataBinding.includeNotificationActivity.btn_back_toolbar.setOnClickListener {
            onBackPressed()
        }
    }



    fun runRecyclerView(){
        val layoutManager = LinearLayoutManager(this)
        dataBinding.recyclerViewNotificationActivity.layoutManager = layoutManager
        notificationViewAdapter = NotificationViewAdapter(friendRequestList)
        dataBinding.recyclerViewNotificationActivity.adapter = notificationViewAdapter

    }

}