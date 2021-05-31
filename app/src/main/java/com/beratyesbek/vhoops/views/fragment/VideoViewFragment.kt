package com.beratyesbek.vhoops.views.fragment

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import com.beratyesbek.vhoops.databinding.FragmentVideoViewBinding
import kotlinx.android.synthetic.main.chat_video_item.*


class VideoViewFragment(val uri : Uri) : Fragment() {


    private lateinit var dataBinding : FragmentVideoViewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        dataBinding = FragmentVideoViewBinding.inflate(layoutInflater)
        val view = dataBinding.root

        val mediaController = MediaController(context)
        mediaController.setAnchorView(videoView)

        val videView = dataBinding.videoViewFragment
        videView.setMediaController(mediaController)
        videView.setVideoURI(uri)
        videView.requestFocus()
        videView.start()

        dataBinding.btnBackVideoViewFragment.setOnClickListener {
            activity?.onBackPressed()
        }

        return view
    }

}