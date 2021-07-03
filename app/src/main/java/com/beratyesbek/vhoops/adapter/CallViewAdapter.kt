package com.beratyesbek.vhoops.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.beratyesbek.vhoops.R
import com.beratyesbek.vhoops.databinding.CustomInvitationItemBinding
import com.beratyesbek.vhoops.entities.concrete.dtos.InvitationDto

class CallViewAdapter(val invitationDtoList : ArrayList<InvitationDto>) : RecyclerView.Adapter<CallViewAdapter.CallViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<CustomInvitationItemBinding>(inflater, R.layout.custom_invitation_item,parent,false)
        return CallViewHolder(view)
    }

    override fun onBindViewHolder(holder: CallViewHolder, position: Int) {
       holder.view.invitationDto = invitationDtoList[position]
    }

    override fun getItemCount(): Int {
        return  invitationDtoList.size
    }

    class CallViewHolder(var view : CustomInvitationItemBinding) : RecyclerView.ViewHolder(view.root) {

    }

}