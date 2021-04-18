package com.beratyesbek.Vhoops.Views.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.beratyesbek.Vhoops.Adapter.NotificationViewAdapter
import com.beratyesbek.Vhoops.Business.Concrete.FriendRequestManager
import com.beratyesbek.Vhoops.Business.Concrete.UserManager
import com.beratyesbek.Vhoops.DataAccess.Concrete.FriendRequestDal
import com.beratyesbek.Vhoops.DataAccess.Concrete.UserDal
import com.beratyesbek.Vhoops.Entities.Concrete.FriendRequest
import com.beratyesbek.Vhoops.Entities.Concrete.User
import com.beratyesbek.Vhoops.databinding.ActivityNotificationBinding
import com.google.firebase.auth.FirebaseAuth

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding
    private  var friendRequestList : ArrayList<FriendRequest>  = ArrayList<FriendRequest>()
    private var userList : ArrayList<User> = ArrayList<User>()
    private lateinit var  notificationViewAdapter: NotificationViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.includeNotificationActivity.toolbar)
        getSupportActionBar()!!.setDisplayShowTitleEnabled(false);

        runRecyclerView()
        getFriendRequestData()

    }



    fun runRecyclerView(){
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewNotificationActivity.layoutManager = layoutManager
        notificationViewAdapter = NotificationViewAdapter(friendRequestList,userList)
        binding.recyclerViewNotificationActivity.adapter = notificationViewAdapter

    }

    fun getFriendRequestData(){
        val userId = FirebaseAuth.getInstance().currentUser.uid
        val friendRequestDal: FriendRequestDal = FriendRequestDal()
        val friendRequestManager = FriendRequestManager(friendRequestDal)
        friendRequestManager.getById(userId){ iDataResult ->

            if (iDataResult.success()) {
                friendRequestList.addAll(iDataResult.data())
                getUserData()

            }
            else {
                //do something later
            }
        };
    }

    fun  getUserData(){
        val userDal : UserDal = UserDal()
        val userManager = UserManager(userDal)
        userManager.getAll { iDataResult ->
            if (iDataResult.success()) {
                userList.addAll(iDataResult.data())
                setData()
            }
            else{
                //do something later
            }
        }
    }

    fun setData(){
        val tempUserList = ArrayList<User>()
        for (friendRequestItem in friendRequestList){
            for (userItem in userList){
                if(friendRequestItem.senderId == userItem.userID) {
                    tempUserList.add(userItem)
                }
            }
        }
        userList.clear()
        userList.addAll(tempUserList)
        notificationViewAdapter.notifyDataSetChanged()


    }
}