package com.beratyesbek.vhoops.views.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.beratyesbek.vhoops.business.ChatFileOperations
import com.beratyesbek.vhoops.business.concretes.UserManager
import com.beratyesbek.vhoops.business.GroupChatFileOperations
import com.beratyesbek.vhoops.core.constants.Constants
import com.beratyesbek.vhoops.core.utilities.extensions.downloadFromUrl
import com.beratyesbek.vhoops.core.utilities.extensions.placeHolderProgressBar
import com.beratyesbek.vhoops.dataAccess.concretes.UserDal
import com.beratyesbek.vhoops.databinding.FragmentCameraBinding
import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso


class CameraFragment(
    var uri: Uri,
    val receiverId: String?,
    val groupId: String?,
    val documentId: String?,
    val type: Int
) : Fragment() {

    private lateinit var dataBinding: FragmentCameraBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentCameraBinding.inflate(layoutInflater)
        val view = dataBinding.root

        dataBinding.btnCancelCameraFragment.setOnClickListener {
            activity!!.onBackPressed()
        }


        dataBinding.btnApproveCameraFragment.setOnClickListener {
            when (type) {
                Constants.CHAT_ACTIVITY -> sendPhotoToChat()
                Constants.CAMERA_ACTIVITY -> updateUserPofile()
                Constants.PROFILE_ACTIVITY -> updateUserPofile()
                Constants.GROUP_CHAT_ACTIVITY -> sendImageToGroupChat()
            }
        }
        dataBinding.btnCameraFragmentEdit.setOnClickListener {
            runEditor()
        }

        this?.let {
            dataBinding.imageViewCameraFragment.downloadFromUrl(uri.toString(),
                placeHolderProgressBar(context!!))
        }

        return view;
    }


    private fun runEditor() {
        val dsPhotoEditorIntent = Intent(context, DsPhotoEditorActivity::class.java)

        dsPhotoEditorIntent.putExtra(
            DsPhotoEditorConstants.DS_MAIN_BACKGROUND_COLOR,
            Color.parseColor("#AEAEAE")
        )
        dsPhotoEditorIntent.putExtra(
            DsPhotoEditorConstants.DS_TOOL_BAR_BACKGROUND_COLOR,
            Color.parseColor("#7289DA")
        )

        dsPhotoEditorIntent.setData(uri);
        dsPhotoEditorIntent.putExtra(
            DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY,
            "YOUR_OUTPUT_IMAGE_FOLDER"
        );

        val toolsToHide =
            intArrayOf(DsPhotoEditorActivity.TOOL_ORIENTATION, DsPhotoEditorActivity.TOOL_CROP)

        dsPhotoEditorIntent.putExtra(
            DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE,
            toolsToHide
        )

        startActivityForResult(dsPhotoEditorIntent, 200);

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (data != null) {
            when (requestCode) {
                200 -> {
                    val result = data.data
                    if (resultCode == Activity.RESULT_OK) {
                        if (result != null) {
                            setImage(result)
                        }
                    }
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data)

    }

    fun setImage(uri: Uri) {
        this.uri = uri
        Picasso.get().load(uri).into(dataBinding.imageViewCameraFragment)
    }

    private fun sendPhotoToChat() {
        if (receiverId != null) {
            ChatFileOperations.checkUriExtension(uri, context!!, receiverId)
            activity?.onBackPressed()
        }
    }

    private fun updateUserPofile() {

        val userId = FirebaseAuth.getInstance().currentUser.uid
        val userDal: UserDal = UserDal()
        val userManager = UserManager(userDal)
        if (uri != null) {
            userManager.uploadPhoto(uri) {
                if (it.success()) {
                    userManager.getPhoto(userId) {
                        if (it.success()) {

                            userManager.updateUserProfileImage(it.data(), documentId!!) { result ->
                                if (result.success()) {
                                    Toast.makeText(
                                        this.context,
                                        result.message(),
                                        Toast.LENGTH_LONG
                                    ).show()
                                    requireActivity().onBackPressed()
                                } else {
                                    Toast.makeText(
                                        this.context,
                                        result.message(),
                                        Toast.LENGTH_LONG
                                    ).show()

                                }
                            }
                        } else {
                            Toast.makeText(context, it.message(), Toast.LENGTH_LONG).show()
                        }

                    }
                } else {
                    Toast.makeText(context, it.message(), Toast.LENGTH_LONG).show()

                }
            }

        }
    }

    private fun sendImageToGroupChat() {
        if (groupId != null) {
            GroupChatFileOperations.groupId = groupId
            val type = GroupChatFileOperations.checkUriExtension(uri, context!!)
            if (type != null) {
                GroupChatFileOperations.uploadFile(uri, type)
            }
            activity?.onBackPressed()
        }
    }


}