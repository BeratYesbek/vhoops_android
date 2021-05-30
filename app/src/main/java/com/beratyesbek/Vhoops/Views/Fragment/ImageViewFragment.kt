package com.beratyesbek.vhoops.views.fragment

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beratyesbek.vhoops.databinding.FragmentImageViewBinding
import com.squareup.picasso.Picasso


class ImageViewFragment(val uri : Uri) : Fragment() {

    private lateinit var dataBinding: FragmentImageViewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentImageViewBinding.inflate(layoutInflater)
        val view = dataBinding.root


        Picasso.get().load(uri).into(dataBinding.imageViewFragment)

        dataBinding.btnCloseImageViewFragment.setOnClickListener {
            activity?.onBackPressed()
        }
        return view
    }

}