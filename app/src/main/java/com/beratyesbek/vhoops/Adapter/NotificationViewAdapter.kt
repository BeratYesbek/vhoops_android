package com.beratyesbek.vhoops.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.beratyesbek.vhoops.Business.Concrete.FellowManager
import com.beratyesbek.vhoops.Business.Concrete.FriendRequestManager
import com.beratyesbek.vhoops.DataAccess.Concrete.FellowDal
import com.beratyesbek.vhoops.DataAccess.Concrete.FriendRequestDal
import com.beratyesbek.vhoops.entities.concrete.Fellow
import com.beratyesbek.vhoops.entities.concrete.FriendRequest
import com.beratyesbek.vhoops.entities.concrete.User
import com.beratyesbek.vhoops.R
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NotificationViewAdapter(
    val listFriendRequest: ArrayList<FriendRequest>,
    val listUser: ArrayList<User>
) : RecyclerView.Adapter<NotificationViewAdapter.NotificationViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_notification_item, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val user = listUser.get(position);
        val friendRequest = listFriendRequest.get(position);

        holder.textViewUserName.text = user.userName;
        holder.textViewName.text = (user.firstName + " " + user.lastName).toUpperCase();

        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm")
        val date = sdf.format(Date(friendRequest.timeToSend.toDate().toString()))
        holder.textViewTimestamp.text = date

        if (user.profileImage != null) {
            Picasso.get().load(user.profileImage).into(holder.imageViewProfile);
        }
        if (friendRequest.status){
            holder.btnAcceptRequest.visibility = View.GONE
        }
        holder.btnAcceptRequest.setOnClickListener {
            addFriend(position)
        }

    }

    private fun addFriend(position: Int) {

        val fellowDal : FellowDal = FellowDal()
        val fellowManager : FellowManager = FellowManager(fellowDal)

        val friendRequest = listFriendRequest.get(position);
        fellowManager.add(Fellow(friendRequest.senderId,friendRequest.receiverId,false)){ result ->
            if (result.success()){
                fellowManager.add(Fellow(friendRequest.receiverId,friendRequest.senderId,false)){ result ->
                    if (result.success()){
                        updateRequest(position)
                    }

                }
            }

        }
    }

    private fun updateRequest(position: Int){
        val friendRequestDal = FriendRequestDal()
        val friendRequestManager = FriendRequestManager(friendRequestDal)

        var friendRequest = listFriendRequest.get(position);
        friendRequest.status = true;

        friendRequestManager.update(friendRequest){ result ->

        }

    }

    override fun getItemCount(): Int {
        return listFriendRequest.size;
    }

    inner class NotificationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewName: TextView
        val textViewUserName: TextView
        val textViewTimestamp: TextView
        val imageViewProfile: ImageView
        val btnAcceptRequest: Button

        init {
            textViewName = view.findViewById(R.id.textView_name_notification_item)
            textViewUserName = view.findViewById(R.id.textView_username_notification_item)
            imageViewProfile = view.findViewById(R.id.imageView_notification_item)
            btnAcceptRequest = view.findViewById(R.id.btn_accept_request_notification_item)
            textViewTimestamp = view.findViewById(R.id.textView_timestamp_notification_item)
        }
    }
}