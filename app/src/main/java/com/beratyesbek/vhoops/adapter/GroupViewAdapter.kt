package com.beratyesbek.vhoops.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.beratyesbek.vhoops.core.utilities.extensions.downloadFromUrl
import com.beratyesbek.vhoops.entities.concrete.Group
import com.beratyesbek.vhoops.R
import com.beratyesbek.vhoops.viewUtilities.OnItemClickListener

class GroupViewAdapter(val groupList : ArrayList<Group>,val onClickListener: OnItemClickListener) : RecyclerView.Adapter<GroupViewAdapter.GroupViewHolder>() {

    private lateinit var context : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
         context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.custom_group_list_item,parent,false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = groupList.get(position)

        holder.textViewGroupName?.text = group.groupName?.toUpperCase()

        if(group.groupDescription!!.length > 30){
            holder.textViewGroupDescription?.text = (group.groupDescription?.substring(0, 30) + "...")

        }else{
            holder.textViewGroupDescription?.text = (group.groupDescription?.substring(0, group.groupDescription!!.length) + "...")

        }


        holder.imageViewGroupIcon?.downloadFromUrl(
            group.groupImage.toString(), CircularProgressDrawable(context)
        )
    }

    override fun getItemCount(): Int {
       return groupList.size
    }
    inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {
        val imageViewGroupIcon : ImageView?
        val textViewGroupName : TextView?
        val textViewGroupDescription : TextView?
        init {
            imageViewGroupIcon = itemView.findViewById(R.id.imageView_group_item)
            textViewGroupName = itemView.findViewById(R.id.textView_group_item_groupName)
            textViewGroupDescription = itemView.findViewById(R.id.textView_groupDescription)
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
           val position = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                onClickListener.onItemClick(position)
            }
        }
    }

}