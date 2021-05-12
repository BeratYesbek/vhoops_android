package com.beratyesbek.Vhoops.Adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.beratyesbek.Vhoops.Core.Constants.Constants
import com.beratyesbek.Vhoops.Entities.Concrete.Fellow
import com.beratyesbek.Vhoops.Entities.Concrete.User
import com.beratyesbek.Vhoops.R
import com.beratyesbek.Vhoops.ViewUtilities.OnItemClickListener
import com.beratyesbek.Vhoops.Views.Activities.ChatActivity
import com.beratyesbek.Vhoops.Views.Activities.UserActivity
import com.squareup.picasso.Picasso

class FriendViewAdapter(
    val userList: ArrayList<User>,
    val fellowList: ArrayList<Fellow>,
    val clickListener: OnItemClickListener
) : RecyclerView.Adapter<FriendViewAdapter.FriendViewHolder>() {

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_friend_item, parent, false)
        return FriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val user = userList.get(position)

        holder.textViewAbout.text = user.about
        holder.textViewUserName.text = (user.firstName + " " + user.lastName)

        if (user.profileImage != null) {
            Picasso.get().load(user.profileImage).into(holder.imageViewProfile)
        }
        holder.imageViewProfile.setOnClickListener {
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
        return userList.size
    }

    inner class FriendViewHolder(view: View) : RecyclerView.ViewHolder(view),View.OnClickListener {
        val textViewUserName: TextView
        val textViewAbout: TextView
        val imageViewProfile: ImageView

        init {
            textViewUserName = view.findViewById(R.id.textView_friend_item_userName)
            textViewAbout = view.findViewById(R.id.textView_friend_item_about)
            imageViewProfile = view.findViewById(R.id.imageView_friend_item)
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                clickListener.onItemClick(position)
            }
        }

    }
}