package com.beratyesbek.Vhoops.Fragment

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beratyesbek.Vhoops.R
import com.beratyesbek.Vhoops.databinding.FragmentCameraBinding


class CameraFragment(val bitmap: Bitmap) : Fragment() {

    private lateinit var binding: FragmentCameraBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCameraBinding.inflate(layoutInflater)
        val view = binding.root
        binding.imageViewCameraFragment.setImageBitmap(bitmap);

        binding.btnCancelCameraFragment.setOnClickListener {
            activity!!.onBackPressed()
        }
        return view;




    }


}