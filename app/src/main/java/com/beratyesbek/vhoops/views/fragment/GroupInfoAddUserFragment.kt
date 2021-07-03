package com.beratyesbek.vhoops.views.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.beratyesbek.vhoops.adapter.GroupInfoMemberViewAdapter
import com.beratyesbek.vhoops.core.utilities.animations.Animation
import com.beratyesbek.vhoops.mvvm.GroupInfoAddMemberViewModel
import com.beratyesbek.vhoops.viewUtilities.OnItemClickListener
import com.beratyesbek.vhoops.databinding.FragmentGroupInfoAddUserBinding
import com.beratyesbek.vhoops.entities.concrete.Group
import com.beratyesbek.vhoops.entities.concrete.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.view.*

@AndroidEntryPoint
class GroupInfoAddUserFragment(val group : Group) : Fragment(),OnItemClickListener {

    private lateinit var dataBinding : FragmentGroupInfoAddUserBinding
    private lateinit var groupInfoAddMemberViewAdapter: GroupInfoMemberViewAdapter
    private val viewModel : GroupInfoAddMemberViewModel by viewModels()
    private val userList = ArrayList<User>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentGroupInfoAddUserBinding.inflate(layoutInflater)
        val view = dataBinding.root


        runRecyclerView()

        viewModel.getFriendData()

        viewModel.friendList.observe(viewLifecycleOwner,{ users ->
            userList.clear()
            userList.addAll(users)
            groupInfoAddMemberViewAdapter.notifyDataSetChanged()
            dataBinding.recyclerViewGroupInfoAddUserFragment.scheduleLayoutAnimation()
        })

        viewModel.result.observe(viewLifecycleOwner,{
            Toast.makeText(context,"Member was added",Toast.LENGTH_LONG).show()
        })

        dataBinding.includeFragmentGroupInfoAddUser.btn_back_toolbar.setOnClickListener {
            activity?.onBackPressed()
        }


        return view
    }


    private fun runRecyclerView() {
        val recyclerView = dataBinding.recyclerViewGroupInfoAddUserFragment
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        groupInfoAddMemberViewAdapter = GroupInfoMemberViewAdapter(userList,group,this)
        recyclerView.adapter = groupInfoAddMemberViewAdapter
        recyclerView.refreshDrawableState()
        recyclerView.setItemAnimator(DefaultItemAnimator())
        Animation.listItemAnimationSecond(recyclerView)

    }

    override fun onItemClick(position: Int) {
        val user = userList[position]
        group.memberIdList?.add(user.userID)
        viewModel.updateGroupMember(group)


    }

    override fun onItemLongClick(position: Int) {

    }


}