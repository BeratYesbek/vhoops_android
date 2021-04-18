package com.beratyesbek.Vhoops.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.beratyesbek.Vhoops.Entities.Concrete.User
import com.beratyesbek.Vhoops.ViewUtilities.OnItemClickListener
import com.beratyesbek.Vhoops.R
import com.squareup.picasso.Picasso


class SearchViewAdapter(var userList: ArrayList<User>,val clickListener: OnItemClickListener) :
    RecyclerView.Adapter<SearchViewAdapter.SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_user_item, parent, false)

        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val user: User = userList.get(position)
        holder.textViewUserName!!.text = user.userName
        holder.textViewName!!.text = (user.firstName + user.lastName).toUpperCase()
        if (user.profileImage != null){
            Picasso.get().load(user.profileImage).into(holder.imageViewProfile)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun filterList(filterUserList: ArrayList<User>) {
        userList = filterUserList
        notifyDataSetChanged()
    }

    inner class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view),View.OnClickListener {
        val textViewUserName: TextView?
        val textViewName: TextView?
        val imageViewProfile : ImageView?

        init {
            textViewName = view.findViewById(R.id.textView_search_item_Name)
            textViewUserName = view.findViewById(R.id.textView_search_item_userName)
            imageViewProfile = view.findViewById(R.id.imageView_search_item_profile)
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
