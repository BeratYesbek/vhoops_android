package com.beratyesbek.vhoops.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.beratyesbek.vhoops.Core.DataAccess.Constants.ExtensionConstants
import com.beratyesbek.vhoops.Core.Utilities.Control.CheckFirebaseUriType
import com.beratyesbek.vhoops.R
import com.beratyesbek.vhoops.ViewUtilities.OnItemClickListener
import com.beratyesbek.vhoops.databinding.CustomHomeListBinding
import com.beratyesbek.vhoops.entities.concrete.dtos.ChatDto
import com.beratyesbek.vhoops.entities.concrete.dtos.ChatListDto

class HomeViewAdapter(val chatList: ArrayList<ChatListDto>,val onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<HomeViewAdapter.HomeViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = DataBindingUtil.inflate<CustomHomeListBinding>(inflater,R.layout.custom_home_list, parent, false);
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.view.chatListDto = chatList[position]
        val type = checkMessageType(position)

        when(type){
            ExtensionConstants.AUDIO -> holder.view.textViewFriendItemAbout.text = "Audio"
            ExtensionConstants.VIDEO -> holder.view.textViewFriendItemAbout.text = "Video"
            ExtensionConstants.DOCUMENT -> holder.view.textViewFriendItemAbout.text = "Document"
            ExtensionConstants.MAP -> holder.view.textViewFriendItemAbout.text = "Map"
            ExtensionConstants.IMAGE -> holder.view.textViewFriendItemAbout.text = "Image"
            else -> holder.view.textViewFriendItemAbout.text = chatList[position].lastMessage.toString()
        }

    }

    fun checkMessageType(position: Int): String {
        try {
            val message = chatList.get(position).lastMessage.toString()
            if (chatList.get(position).lastMessage is Map<*, *>) {
                return ExtensionConstants.MAP
            } else {
                val type = CheckFirebaseUriType.checkUriType(message)
                if (type == null) {
                    return ExtensionConstants.TEXT
                }
                return type

            }
        } catch (e: Exception) {
            return e.toString()
        }
    }


    override fun getItemCount(): Int {
        return chatList.size
    }

    inner class HomeViewHolder(var view: CustomHomeListBinding) : RecyclerView.ViewHolder(view.root),View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                onItemClickListener.onItemClick(position)
            }
        }

    }
}