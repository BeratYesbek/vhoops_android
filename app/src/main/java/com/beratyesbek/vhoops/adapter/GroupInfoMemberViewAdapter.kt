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
import com.beratyesbek.vhoops.databinding.CustomGroupInfoAddMemberItemBinding
import com.beratyesbek.vhoops.entities.concrete.Group
import com.beratyesbek.vhoops.entities.concrete.User

class GroupInfoMemberViewAdapter(val userList : ArrayList<User>,val group : Group,val onItemClickListener : OnItemClickListener) :
    RecyclerView.Adapter<GroupInfoMemberViewAdapter.GroupInfoMemberViewHolder>() {

    private lateinit var context : Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupInfoMemberViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<CustomGroupInfoAddMemberItemBinding>(inflater,R.layout.custom_group_info_add_member_item,parent,false)
        return GroupInfoMemberViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupInfoMemberViewHolder, position: Int) {
        holder.view.user = userList[position]
        group.memberIdList?.forEach { userId ->
            if (userId == userList[position].userID){
                holder.view.btnAddMember.visibility = View.INVISIBLE
            }
        }

        holder.view.imageViewUserGroupInfoAddMember.downloadFromUrl(
            userList[position].profileImage.toString(),
            CircularProgressDrawable(context)
        )
        holder.view.btnAddMember.setOnClickListener {
            holder.view.btnAddMember.visibility = View.INVISIBLE
            onItemClickListener.onItemClick(position)
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class GroupInfoMemberViewHolder(var view : CustomGroupInfoAddMemberItemBinding) : RecyclerView.ViewHolder(view.root) {

    }

}