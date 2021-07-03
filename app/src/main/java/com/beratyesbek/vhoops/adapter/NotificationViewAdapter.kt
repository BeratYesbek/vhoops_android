package com.beratyesbek.vhoops.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.beratyesbek.vhoops.business.concretes.FellowManager
import com.beratyesbek.vhoops.business.concretes.FriendRequestManager
import com.beratyesbek.vhoops.dataAccess.concretes.FellowDal
import com.beratyesbek.vhoops.dataAccess.concretes.FriendRequestDal
import com.beratyesbek.vhoops.entities.concrete.Fellow
import com.beratyesbek.vhoops.entities.concrete.FriendRequest
import com.beratyesbek.vhoops.R
import com.beratyesbek.vhoops.databinding.CustomNotificationItemBinding
import com.beratyesbek.vhoops.entities.concrete.dtos.FriendRequestDto
import com.google.firebase.auth.FirebaseAuth
import kotlin.collections.ArrayList

class NotificationViewAdapter(
    val listFriendRequest: ArrayList<FriendRequestDto>,
) : RecyclerView.Adapter<NotificationViewAdapter.NotificationViewHolder>() {

    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
         val view = DataBindingUtil.inflate<CustomNotificationItemBinding>(inflater,R.layout.custom_notification_item,parent,false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val friendRequestDto = listFriendRequest[position]
        holder.view.friendRequestDto = friendRequestDto

        if (friendRequestDto.status){
            holder.view.btnAcceptRequestNotificationItem.visibility = View.GONE
            holder.view.textViewInfoNotificationItem.text = "You have accepted"
        }
        if (friendRequestDto.status && currentUserId.equals(friendRequestDto.senderId)){
            holder.view.textViewInfoNotificationItem.text = "Request has been accepted"
        }

        holder.view.btnAcceptRequestNotificationItem.setOnClickListener {
            addFriend(position,holder.view.btnAcceptRequestNotificationItem)
        }

    }

    private fun addFriend(position: Int,view :View) {

        val fellowDal : FellowDal = FellowDal()
        val fellowManager : FellowManager = FellowManager(fellowDal)

        val friendRequest = listFriendRequest.get(position);
        fellowManager.add(Fellow(friendRequest.senderId,friendRequest.receiverId,false)){ result ->
            if (result.success()){
                fellowManager.add(Fellow(friendRequest.receiverId,friendRequest.senderId,false)){ result ->
                    if (result.success()){
                        view.visibility = View.INVISIBLE
                        updateRequest(position)
                    }

                }
            }

        }
    }

    private fun updateRequest(position: Int){
        val friendRequestDal = FriendRequestDal()
        val friendRequestManager = FriendRequestManager(friendRequestDal)

        var friendRequestDto = listFriendRequest[position]
        friendRequestDto.status = true;

        friendRequestManager.update(FriendRequest(friendRequestDto.senderId,
            friendRequestDto.receiverId,
            friendRequestDto.documentId,
            friendRequestDto.status,
            friendRequestDto.timeToSend)){ result ->

        }

    }

    override fun getItemCount(): Int {
        return listFriendRequest.size;
    }

    inner class NotificationViewHolder(var view: CustomNotificationItemBinding) : RecyclerView.ViewHolder(view.root) {

    }
}