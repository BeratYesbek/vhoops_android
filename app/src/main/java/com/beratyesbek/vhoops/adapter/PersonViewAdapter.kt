package com.beratyesbek.vhoops.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.beratyesbek.vhoops.core.constants.Constants
import com.beratyesbek.vhoops.core.utilities.extensions.downloadFromUrl
import com.beratyesbek.vhoops.entities.concrete.User
import com.beratyesbek.vhoops.R
import com.beratyesbek.vhoops.viewUtilities.OnItemClickListener
import com.beratyesbek.vhoops.databinding.CustomFriendItemBinding
import com.beratyesbek.vhoops.views.activities.ChatActivity
import com.beratyesbek.vhoops.views.activities.UserActivity
import com.squareup.picasso.Picasso

class PersonViewAdapter(
    val userList: ArrayList<User>,
    val clickListener: OnItemClickListener
) : RecyclerView.Adapter<PersonViewAdapter.FriendViewHolder>() {

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = DataBindingUtil.inflate<CustomFriendItemBinding>(inflater,R.layout.custom_friend_item,parent,false)
        return FriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val user = userList[position]
        holder.view.user = user

        holder.view.imageViewFriendItem.downloadFromUrl(user.profileImage.toString(),
            CircularProgressDrawable(context)
        )

        holder.view.imageViewFriendItem.setOnClickListener {
            customDialog(position)
        }
    }

    fun customDialog(position: Int) {

        val user = userList.get(position)

        val dialog = Dialog(context)
        dialog.setContentView(R.layout.custom_user_detail_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val imageView = dialog.findViewById<ImageView>(R.id.imageView_user_detail_dialog)
        val btnChatActivity = dialog.findViewById<ImageButton>(R.id.btn_userDetail_chatActivity)
        val btnInfoActivity = dialog.findViewById<ImageButton>(R.id.btn_userDetail_infoActivity)

        btnInfoActivity.setOnClickListener {
            val intentToUserActivity = Intent(context,UserActivity::class.java)
            intentToUserActivity.putExtra(Constants.USER_ID,user.userID)
            context.startActivity(intentToUserActivity)
        }
        btnChatActivity.setOnClickListener {
            println(user.profileImage)
            val intentToChatActivity = Intent(context,ChatActivity::class.java)
            intentToChatActivity.putExtra(Constants.USER_ID,user.userID)
            intentToChatActivity.putExtra(Constants.FULL_NAME,user.firstName + " " + user.lastName)
            intentToChatActivity.putExtra(Constants.PROFILE_IMAGE,user.profileImage.toString())
            context.startActivity(intentToChatActivity)
        }
        if (userList.get(position).profileImage != null) {
            Picasso.get().load(userList.get(position).profileImage).into(imageView)
        }
        dialog.show()
    }

    override fun getItemCount(): Int {
        println("user list size " + userList.size)
        return userList.size
    }

    inner class FriendViewHolder(var view: CustomFriendItemBinding) : RecyclerView.ViewHolder(view.root),View.OnClickListener,View.OnLongClickListener {
        val textViewUserName: TextView
        val textViewAbout: TextView
        val imageViewProfile: ImageView
        val relativeLayout : RelativeLayout

        init {
            textViewUserName = view.textViewFriendItemUserName
            textViewAbout = view.textViewFriendItemAbout
            imageViewProfile = view.imageViewFriendItem
            relativeLayout = view.relativeLayoutCustomPersonItem

            relativeLayout.setOnClickListener(this)
            relativeLayout.setOnLongClickListener(this)
            itemView.setOnLongClickListener(this)
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            relativeLayout?.setBackgroundResource(R.drawable.custom_transparent_background)
            if (position != RecyclerView.NO_POSITION) {
                clickListener.onItemClick(position)
            }
        }

        override fun onLongClick(p0: View?): Boolean {

            val position = adapterPosition
            relativeLayout?.setBackgroundResource(R.drawable.oppocity_background)

            if (position != RecyclerView.NO_POSITION) {

                clickListener.onItemLongClick(position)
                return true
            }

            return false
        }


    }
}