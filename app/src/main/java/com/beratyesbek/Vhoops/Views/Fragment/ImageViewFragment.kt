package com.beratyesbek.Vhoops.Views.Fragment

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beratyesbek.Vhoops.R
import com.beratyesbek.Vhoops.databinding.FragmentImageViewBinding
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView


class ImageViewFragment(val uri : Uri) : Fragment() {

    private lateinit var binding: FragmentImageViewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImageViewBinding.inflate(layoutInflater)
        val view = binding.root


        Picasso.get().load(uri).into(binding.imageViewFragment)

        binding.btnCloseImageViewFragment.setOnClickListener {
            activity?.onBackPressed()
        }
        return view
    }

}