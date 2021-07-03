package com.beratyesbek.vhoops.views.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.beratyesbek.vhoops.views.activities.UserActivity
import com.beratyesbek.vhoops.adapter.SearchViewAdapter
import com.beratyesbek.vhoops.business.concretes.UserManager
import com.beratyesbek.vhoops.core.constants.Constants
import com.beratyesbek.vhoops.core.utilities.animations.Animation
import com.beratyesbek.vhoops.dataAccess.concretes.UserDal
import com.beratyesbek.vhoops.entities.concrete.User
import com.beratyesbek.vhoops.viewUtilities.OnItemClickListener
import com.beratyesbek.vhoops.databinding.FragmentSearchBinding


class SearchFragment : OnItemClickListener, Fragment() {

    private lateinit var dataBinding: FragmentSearchBinding
    private  var userList = ArrayList<User>()
    private lateinit var adapter : SearchViewAdapter
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        dataBinding = FragmentSearchBinding.inflate(layoutInflater)
        val view = dataBinding.root
        runRecyclerView()
        searchData()

        dataBinding.btnFragmentSearchBack.setOnClickListener {
            activity?.onBackPressed()
        }

        dataBinding.editTextSearchFragment.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                filter(p0.toString())
            }

        })




        return view
    }


    fun filter(filter : String){
       val filterUserList = ArrayList<User>()
        println(filter)
       for (item in userList){
           if(item.userName!!.contains(filter)){
             filterUserList.add(item);
           }
       }
        adapter.filterList(filterUserList,dataBinding.recyclerViewSearchFragment)

    }

    fun searchData () {
        var search = dataBinding.editTextSearchFragment.text.toString()
        val userDal: UserDal = UserDal()
        val userManager = UserManager(userDal)
        userManager.getAll { result ->
           userList.addAll(result.data()!!)
        }
    }

    fun runRecyclerView(){
        val layoutManager = LinearLayoutManager(context)
        dataBinding.recyclerViewSearchFragment.layoutManager = layoutManager;
        adapter = SearchViewAdapter(userList,this)
        dataBinding.recyclerViewSearchFragment.adapter = adapter
        dataBinding.recyclerViewSearchFragment.refreshDrawableState()
        dataBinding.recyclerViewSearchFragment.setItemAnimator(DefaultItemAnimator())
        Animation.listItemAnimation(dataBinding.recyclerViewSearchFragment)

    }

    override fun onItemClick(position: Int) {
        val user : User = userList.get(position);
        val intentToUserProfile = Intent(context,UserActivity::class.java)
        intentToUserProfile.putExtra(Constants.USER_ID,user.userID)
        startActivity(intentToUserProfile)

    }

    override fun onItemLongClick(position: Int) {
        TODO("Not yet implemented")
    }


}

