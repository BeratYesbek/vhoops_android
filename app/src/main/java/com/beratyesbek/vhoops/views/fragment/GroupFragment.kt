package com.beratyesbek.vhoops.views.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.beratyesbek.vhoops.Adapter.GroupViewAdapter
import com.beratyesbek.vhoops.Business.Concrete.GroupManager
import com.beratyesbek.vhoops.DataAccess.Concrete.GroupDal
import com.beratyesbek.vhoops.entities.concrete.Group
import com.beratyesbek.vhoops.ViewUtilities.OnItemClickListener
import com.beratyesbek.vhoops.views.activities.GroupChatActivity
import com.beratyesbek.vhoops.databinding.FragmentGroupBinding


class GroupFragment : Fragment() ,OnItemClickListener{
    private lateinit var dataBinding: FragmentGroupBinding
    private lateinit var groupViewAdapter : GroupViewAdapter
    private val groupList : ArrayList<Group> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = FragmentGroupBinding.inflate(layoutInflater)
        getGroups()
        runRecyclerView()
        return dataBinding.root
    }
    private fun runRecyclerView(){
        val layoutManager =  LinearLayoutManager(context)
        dataBinding.recyclerViewGroupFragment.layoutManager = layoutManager
        groupViewAdapter = GroupViewAdapter(groupList,this)
        dataBinding.recyclerViewGroupFragment.adapter = groupViewAdapter
    }
    private fun getGroups(){
        val groupManager = GroupManager(GroupDal())
        groupManager.getAll { iDataResult ->
            if (iDataResult.success()) {
                groupList.clear()
                groupList.addAll(iDataResult.data())
                groupViewAdapter.notifyDataSetChanged()
            }
        }

    }

    override fun onItemClick(position: Int) {

        val group = groupList[position]


       val intentToGroupChat = Intent(context,GroupChatActivity::class.java)
        intentToGroupChat.putExtra("GroupIcon",group.groupImage.toString())
        intentToGroupChat.putExtra("GroupId",group.groupId)
        intentToGroupChat.putExtra("GroupName",group.groupName)
        intentToGroupChat.putExtra("AdminId",group.adminId)
        intentToGroupChat.putExtra("CreatedDate",group.createdDate)
        startActivity(intentToGroupChat)
    }

    override fun onItemLongClick(position: Int) {
        TODO("Not yet implemented")
    }
}