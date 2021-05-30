package com.beratyesbek.vhoops.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.beratyesbek.vhoops.Adapter.NotificationViewAdapter
import com.beratyesbek.vhoops.Business.Concrete.FriendRequestManager
import com.beratyesbek.vhoops.Business.Concrete.UserManager
import com.beratyesbek.vhoops.DataAccess.Concrete.FriendRequestDal
import com.beratyesbek.vhoops.DataAccess.Concrete.UserDal
import com.beratyesbek.vhoops.entities.concrete.FriendRequest
import com.beratyesbek.vhoops.entities.concrete.User
import com.beratyesbek.vhoops.databinding.ActivityNotificationBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.toolbar.view.*

class NotificationActivity : AppCompatActivity() {

    private lateinit var dataBinding: ActivityNotificationBinding
    private  var friendRequestList : ArrayList<FriendRequest>  = ArrayList<FriendRequest>()
    private var userList : ArrayList<User> = ArrayList<User>()
    private lateinit var  notificationViewAdapter: NotificationViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityNotificationBinding.inflate(layoutInflater)
        val view = dataBinding.root
        setContentView(view)

        setSupportActionBar(dataBinding.includeNotificationActivity.toolbar)

        runRecyclerView()
        getFriendRequestData()

    }



    fun runRecyclerView(){
        val layoutManager = LinearLayoutManager(this)
        dataBinding.recyclerViewNotificationActivity.layoutManager = layoutManager
        notificationViewAdapter = NotificationViewAdapter(friendRequestList,userList)
        dataBinding.recyclerViewNotificationActivity.adapter = notificationViewAdapter

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