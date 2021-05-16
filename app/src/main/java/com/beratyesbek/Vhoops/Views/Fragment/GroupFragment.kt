package com.beratyesbek.Vhoops.Views.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.beratyesbek.Vhoops.Adapter.GroupViewAdapter
import com.beratyesbek.Vhoops.Business.Concrete.GroupManager
import com.beratyesbek.Vhoops.DataAccess.Concrete.GroupDal
import com.beratyesbek.Vhoops.Entities.Concrete.Group
import com.beratyesbek.Vhoops.R
import com.beratyesbek.Vhoops.databinding.FragmentGroupBinding


class GroupFragment : Fragment() {
    private lateinit var binding: FragmentGroupBinding
    private lateinit var groupViewAdapter : GroupViewAdapter
    private val groupList : ArrayList<Group> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupBinding.inflate(layoutInflater)
        getGroups()
        runRecyclerView()
        return binding.root
    }
    private fun runRecyclerView(){
        val layoutManager =  LinearLayoutManager(context)
        binding.recyclerViewGroupFragment.layoutManager = layoutManager
        groupViewAdapter = GroupViewAdapter(groupList)
        binding.recyclerViewGroupFragment.adapter = groupViewAdapter
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
}