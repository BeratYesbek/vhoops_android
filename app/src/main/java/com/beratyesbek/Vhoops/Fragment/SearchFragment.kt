package com.beratyesbek.Vhoops.Fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.beratyesbek.Vhoops.Adapter.SearchViewAdapter
import com.beratyesbek.Vhoops.Business.Concrete.UserManager
import com.beratyesbek.Vhoops.DataAccess.UserDal
import com.beratyesbek.Vhoops.Entities.Concrete.User
import com.beratyesbek.Vhoops.R
import com.beratyesbek.Vhoops.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private  var userList = ArrayList<User>()
    private lateinit var adapter : SearchViewAdapter
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = FragmentSearchBinding.inflate(layoutInflater)
        var view = binding.root
        runRecyclerView()
        searchData()

        binding.btnFragmentSearchBack.setOnClickListener {
            activity!!.onBackPressed()
        }

        binding.editTextSearchFragment.addTextChangedListener(object : TextWatcher{
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
        adapter.filterList(filterUserList)

    }
    fun searchData () {
        var search = binding.editTextSearchFragment.text.toString()
        val userDal: UserDal = UserDal()
        val userManager = UserManager(userDal)
        userManager.getAll { result ->
           userList.addAll(result.data())
        }
    }
    fun runRecyclerView(){
        val layoutManager = LinearLayoutManager(context)
        binding.recyclerViewSearchFragment.layoutManager = layoutManager;
        adapter = SearchViewAdapter(userList)
        binding.recyclerViewSearchFragment.adapter = adapter


    }

}