package com.beratyesbek.Vhoops.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.beratyesbek.Vhoops.Entities.Concrete.Dtos.ChatDto
import com.beratyesbek.Vhoops.Entities.Concrete.User
import com.beratyesbek.Vhoops.R
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class ChatViewAdapter(val chatDtoList: ArrayList<ChatDto>) :
    RecyclerView.Adapter<ChatViewAdapter.ChatViewHolder>() {

    private val MSG_TYPE_LEFT = 1
    private val MSG_TYPE_RIGHT = 0
    private var viewTypeCheck = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)

        if (viewType == MSG_TYPE_LEFT) {

            val view = layoutInflater.inflate(R.layout.chat_left_item, parent, false)
            viewTypeCheck = MSG_TYPE_LEFT

            return ChatViewHolder(view)

        } else {

            val view = layoutInflater.inflate(R.layout.chat_right_item,parent,false)
            viewTypeCheck = MSG_TYPE_RIGHT

            return ChatViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {

        val firebaseUser = FirebaseAuth.getInstance().currentUser

        if(chatDtoList.get(position).senderId.equals(firebaseUser.uid)) {
            return MSG_TYPE_RIGHT

        }
        return MSG_TYPE_LEFT


    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatDto = chatDtoList.get(position)

        holder.textViewMessage.text = chatDto.message
        if(viewTypeCheck == MSG_TYPE_LEFT){
            if(chatDtoList.get(0).userPicture != null){
                Picasso.get().load(chatDto.userPicture).into(holder.imageViewUserProfile)
            }
        }

    }

    override fun getItemCount(): Int {
        return chatDtoList.size
    }

    inner class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val textViewMessage : TextView
        val imageViewUserProfile : ImageView

        init {
            textViewMessage = view.findViewById(R.id.textView_show_messages)
            imageViewUserProfile = view.findViewById(R.id.imageView_show_message_ProfileImage)
        }
    }

}