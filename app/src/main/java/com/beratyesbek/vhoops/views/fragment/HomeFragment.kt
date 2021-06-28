package com.beratyesbek.vhoops.views.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.beratyesbek.vhoops.Adapter.HomeViewAdapter
import com.beratyesbek.vhoops.Core.Constants.Constants
import com.beratyesbek.vhoops.MVMM.HomeFragmentViewModel
import com.beratyesbek.vhoops.R
import com.beratyesbek.vhoops.ViewUtilities.OnItemClickListener
import com.beratyesbek.vhoops.databinding.FragmentHomeBinding
import com.beratyesbek.vhoops.entities.concrete.dtos.ChatListDto
import com.beratyesbek.vhoops.views.activities.ChatActivity
import dagger.Component
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped

@AndroidEntryPoint
class HomeFragment : Fragment(), OnItemClickListener {

    private val viewModel: HomeFragmentViewModel by viewModels()

    private val chatList: ArrayList<ChatListDto> = ArrayList()

    private lateinit var dataBinding: FragmentHomeBinding

    private lateinit var homeViewAdapter: HomeViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        dataBinding = FragmentHomeBinding.inflate(layoutInflater)
        val view = dataBinding.root

        runRecyclerView()
        viewModel.getChatData()


        viewModel.chatList.observe(viewLifecycleOwner, { dataList ->
            chatList.clear()
            chatList.addAll(dataList)
            homeViewAdapter.notifyDataSetChanged()
        })


        return view
    }

    private fun runRecyclerView() {
        val recyclerView = dataBinding.recyclerViewHomeFragment
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        homeViewAdapter = HomeViewAdapter(chatList, this)
        recyclerView.adapter = homeViewAdapter

    }

    override fun onItemClick(position: Int) {
        val chatList = chatList[position]
        val intentToChatActivity = Intent(context, ChatActivity::class.java)
        intentToChatActivity.putExtra(Constants.USER_ID, chatList.userId)
        intentToChatActivity.putExtra(Constants.PROFILE_IMAGE, chatList.userPicture.toString())
        intentToChatActivity.putExtra(
            Constants.FULL_NAME,
            chatList.userFirstName + " " + chatList.userLastName
        )
        intentToChatActivity.putExtra(Constants.FIRST_NAME, chatList.userFirstName)
        intentToChatActivity.putExtra(Constants.LAST_NAME, chatList.userLastName)
        intentToChatActivity.putExtra(Constants.TOKEN, chatList.userToken)
        intentToChatActivity.putExtra(Constants.MEETING_TYPE, "video")
        startActivity(intentToChatActivity)

    }

    override fun onItemLongClick(position: Int) {
        TODO("Not yet implemented")
    }

}