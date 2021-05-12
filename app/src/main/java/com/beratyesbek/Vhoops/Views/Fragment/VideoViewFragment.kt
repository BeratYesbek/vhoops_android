package com.beratyesbek.Vhoops.Views.Fragment

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import com.beratyesbek.Vhoops.R
import com.beratyesbek.Vhoops.databinding.FragmentVideoViewBinding
import kotlinx.android.synthetic.main.chat_video_item.*


class VideoViewFragment(val uri : Uri) : Fragment() {


    private lateinit var binding: FragmentVideoViewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentVideoViewBinding.inflate(layoutInflater)
        val view = binding.root

        val mediaController = MediaController(context)
        mediaController.setAnchorView(videoView)

        val videView = binding.videoViewFragment
        videView.setMediaController(mediaController)
        videView.setVideoURI(uri)
        videView.requestFocus()
        videView.start()

        binding.btnBackVideoViewFragment.setOnClickListener {
            activity?.onBackPressed()
        }

        return view
    }

}