package com.beratyesbek.vhoops.views.fragment

import android.os.Bundle
import android.telecom.Call
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.beratyesbek.vhoops.Adapter.CallViewAdapter
import com.beratyesbek.vhoops.MVMM.CallFragmentViewModel
import com.beratyesbek.vhoops.R
import com.beratyesbek.vhoops.databinding.FragmentCallBinding
import com.beratyesbek.vhoops.entities.concrete.dtos.InvitationDto
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CallFragment : Fragment() {

    private lateinit var dataBinding: FragmentCallBinding

    private  val viewModel: CallFragmentViewModel by viewModels()

    private val invitationListDto = ArrayList<InvitationDto>()

    private lateinit var callViewAdapter: CallViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentCallBinding.inflate(layoutInflater)
        val view = dataBinding.root

        runRecyclerView()
        viewModel.getInvitationData()

        viewModel.invitationList.observe(viewLifecycleOwner,{ dataList ->
            invitationListDto.clear()
            invitationListDto.addAll(dataList)
            callViewAdapter.notifyDataSetChanged()
        })

        return view;
    }

    private fun runRecyclerView(){
        val recyclerView = dataBinding.recyclerViewCallFragment
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        callViewAdapter = CallViewAdapter(invitationListDto)
        recyclerView.adapter = callViewAdapter
    }


}