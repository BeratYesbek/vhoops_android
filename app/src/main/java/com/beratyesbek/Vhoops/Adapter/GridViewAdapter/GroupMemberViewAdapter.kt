package com.beratyesbek.Vhoops.Adapter.GridViewAdapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.beratyesbek.Vhoops.Entities.Concrete.User
import com.beratyesbek.Vhoops.R
import com.squareup.picasso.Picasso

class GroupMemberViewAdapter(val userList : ArrayList<User>,val context:Context) :BaseAdapter() {
    override fun getCount(): Int {
        return userList.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(p: Int): Long {
        return p.toLong()
    }

    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
       val view : View = View.inflate(context, R.layout.custom_group_dialog_item,null)
        val imageViewUserPicture = view.findViewById<ImageView>(R.id.imageView_groupDialog_item)
        val textViewUserName = view.findViewById<TextView>(R.id.textView_groupDialog_item)

        val user = userList.get(position)

        textViewUserName.text = (user.firstName +" "+ user.lastName).toUpperCase()

        if (user.profileImage != null){
            Picasso.get().load(user.profileImage).into(imageViewUserPicture)
        }
        return view

    }
}