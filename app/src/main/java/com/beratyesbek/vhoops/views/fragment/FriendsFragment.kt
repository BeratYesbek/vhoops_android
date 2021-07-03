package com.beratyesbek.vhoops.views.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.beratyesbek.vhoops.adapter.PersonViewAdapter
import com.beratyesbek.vhoops.business.concretes.FellowManager
import com.beratyesbek.vhoops.business.concretes.UserManager
import com.beratyesbek.vhoops.core.constants.Constants
import com.beratyesbek.vhoops.dataAccess.concretes.FellowDal
import com.beratyesbek.vhoops.dataAccess.concretes.UserDal
import com.beratyesbek.vhoops.entities.concrete.Fellow
import com.beratyesbek.vhoops.entities.concrete.User
import com.beratyesbek.vhoops.viewUtilities.OnItemClickListener
import com.beratyesbek.vhoops.views.activities.ChatActivity
import com.beratyesbek.vhoops.databinding.FragmentFriendsBinding
import com.google.firebase.auth.FirebaseAuth


class FriendsFragment : Fragment(), OnItemClickListener {

    private lateinit var dataBinding: FragmentFriendsBinding

    private val userList : ArrayList<User> = ArrayList()
    private val fellowList : ArrayList<Fellow> = ArrayList()

    private lateinit var personViewAdapter : PersonViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentFriendsBinding.inflate(layoutInflater)
        val view = dataBinding.root

        fellowList.clear()
        userList.clear()

        runRecyclerView()
        getFriendData()
        return view
    }

    fun runRecyclerView(){
        val layoutManager =  LinearLayoutManager(context)
        dataBinding.recyclerViewFriendFragment.layoutManager = layoutManager
        personViewAdapter = PersonViewAdapter(userList,this)
        dataBinding.recyclerViewFriendFragment.adapter = personViewAdapter
    }


    fun getFriendData() {
        val userId = FirebaseAuth.getInstance().currentUser.uid
        val fellowDal = FellowDal()
        val fellowManager = FellowManager(fellowDal)
        fellowManager.getById(userId) { iDataResult ->
            if(iDataResult.success()){

                fellowList.addAll(iDataResult.data()!!)
                getUserData()
            }
        }
    }


    fun getUserData(){
        val userDal = UserDal()
        val userManager = UserManager(userDal)
        userManager.getAll { iDataResult ->

            if(iDataResult.success()){
                userList.addAll(iDataResult.data()!!)
                setAllDataByFriend()
            }
        }
    }

    fun setAllDataByFriend(){
        val tempUserList = ArrayList<User>()
        for (friend in fellowList){

            for (user in userList){
                if(friend.userId == user.userID){
                    tempUserList.add(user)
                }
            }
        }
        userList.clear()
        userList.addAll(tempUserList)
        personViewAdapter.notifyDataSetChanged()

    }

    override fun onItemClick(position: Int) {

        val user = userList.get(position)

        val intentToChatActivity = Intent(context,ChatActivity::class.java)
        intentToChatActivity.putExtra(Constants.FULL_NAME,(user.firstName + " " + user.lastName))
        intentToChatActivity.putExtra(Constants.USER_ID,user.userID)
        intentToChatActivity.putExtra(Constants.PROFILE_IMAGE,user.profileImage.toString())

        startActivity(intentToChatActivity)

    }

    override fun onItemLongClick(position: Int) {
        TODO("Not yet implemented")
    }


}