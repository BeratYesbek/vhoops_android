package com.beratyesbek.vhoops.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.beratyesbek.vhoops.core.utilities.extensions.downloadFromUrl
import com.beratyesbek.vhoops.R
import com.beratyesbek.vhoops.viewUtilities.OnItemClickListener
import com.beratyesbek.vhoops.databinding.CustomGroupInfoItemBinding
import com.beratyesbek.vhoops.entities.concrete.Group
import com.beratyesbek.vhoops.entities.concrete.User
import com.google.firebase.auth.FirebaseAuth

class GroupInfoViewAdapter(val userList : ArrayList<User>,val group : Group, val onClickLister : OnItemClickListener) : RecyclerView.Adapter<GroupInfoViewAdapter.GroupInfoViewHolder>() {

    private var context : Context?  = null
    private val currentUserId = FirebaseAuth.getInstance().currentUser.uid

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupInfoViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = DataBindingUtil.inflate<CustomGroupInfoItemBinding>(inflater, R.layout.custom_group_info_item,parent,false)
        return GroupInfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupInfoViewHolder, position: Int) {
        val user = userList[position]
        holder.view.user = user
        holder.view.imageViewUserGroupInfo.downloadFromUrl(
            user.profileImage.toString(),
            CircularProgressDrawable(context!!)
        )
        println(group?.groupName)
        if (group?.adminId.equals(user.userID)){
            holder.view.btnRemoveUser.visibility = View.INVISIBLE
        }
        if(!currentUserId.equals(group?.adminId) && group != null ){
            holder.view.btnRemoveUser.visibility = View.INVISIBLE

        }
        holder.view.btnRemoveUser.setOnClickListener {
            onClickLister.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class GroupInfoViewHolder(var view: CustomGroupInfoItemBinding) : RecyclerView.ViewHolder(view.root) {

    }
}