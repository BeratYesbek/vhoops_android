package com.beratyesbek.Vhoops.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.beratyesbek.Vhoops.Entities.Concrete.User
import com.beratyesbek.Vhoops.R


class SearchViewAdapter(var userList: ArrayList<User>) :
    RecyclerView.Adapter<SearchViewAdapter.SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_user_item, parent, false)

        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val user: User = userList.get(position)
        holder.textViewUserName!!.text = user.userName
        holder.textViewName!!.text = (user.firstName + user.lastName)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun filterList(filterUserList: ArrayList<User>) {
        userList = filterUserList
        notifyDataSetChanged()
    }

    class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewUserName: TextView?
        val textViewName: TextView?

        init {
            textViewName = view.findViewById(R.id.textView_search_item_Name)
            textViewUserName = view.findViewById(R.id.textView_search_item_userName)
        }
    }
}