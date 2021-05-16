package com.beratyesbek.Vhoops.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.beratyesbek.Vhoops.Entities.Concrete.Group
import com.beratyesbek.Vhoops.R
import com.squareup.picasso.Picasso

class GroupViewAdapter(val groupList : ArrayList<Group>) : RecyclerView.Adapter<GroupViewAdapter.GroupViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.custom_group_list_item,parent,false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = groupList.get(position)

        holder.textViewGroupName?.text = group.groupName

        if(group.groupImage != null){
            Picasso.get().load(group.groupImage).into(holder.imageViewGroupIcon)
        }
    }

    override fun getItemCount(): Int {
       return groupList.size
    }
    inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewGroupIcon : ImageView?
        val textViewGroupName : TextView?
        init {
            imageViewGroupIcon = itemView.findViewById(R.id.imageView_group_item)
            textViewGroupName = itemView.findViewById(R.id.textView_group_item_groupName)
        }
    }

}