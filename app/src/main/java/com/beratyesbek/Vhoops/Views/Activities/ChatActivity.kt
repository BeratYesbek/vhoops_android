package com.beratyesbek.Vhoops.Views.Activities

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.beratyesbek.Vhoops.Adapter.ChatViewAdapter
import com.beratyesbek.Vhoops.Business.Concrete.ChatManager
import com.beratyesbek.Vhoops.Business.Concrete.UserManager
import com.beratyesbek.Vhoops.Core.Constants.Constants
import com.beratyesbek.Vhoops.Core.Permission.DocumentPermission
import com.beratyesbek.Vhoops.Core.Permission.GalleryPermission
import com.beratyesbek.Vhoops.DataAccess.Concrete.ChatDal
import com.beratyesbek.Vhoops.DataAccess.Concrete.UserDal
import com.beratyesbek.Vhoops.Entities.Concrete.Chat
import com.beratyesbek.Vhoops.Entities.Concrete.Dtos.ChatDto
import com.beratyesbek.Vhoops.R
import com.beratyesbek.Vhoops.databinding.ActivityChatBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import java.net.URI

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var receiverId: String
    private val chatDtoList: ArrayList<ChatDto> = ArrayList()
    private lateinit var chatViewAdapter: ChatViewAdapter
    private val CREATE_FILE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        receiverId = intent.getStringExtra(Constants.USER_ID)!!
        val fullName = intent.getStringExtra(Constants.FULL_NAME)
        val profileImage = intent.getStringExtra(Constants.PROFILE_IMAGE)
        val uri: Uri

        val imageView = binding.includeChatActivity.imageViewProfileChatToolbar

        if (profileImage != null) {
            uri = Uri.parse(profileImage)
            Picasso.get().load(uri).into(imageView)
        }
        binding.includeChatActivity.textViewUserFullNameChatActivity.text = fullName!!.toUpperCase()


        getMessages()
        runRecyclerView()


    }

    fun runRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewChatActivity.layoutManager = layoutManager
        layoutManager.setStackFromEnd(true);
        binding.recyclerViewChatActivity.refreshDrawableState();
        chatViewAdapter = ChatViewAdapter(chatDtoList)
        binding.recyclerViewChatActivity.adapter = chatViewAdapter

    }

    fun sendMessage(view: View) {
        val message = binding.editTextEnterMessage.text.toString()
        if (message != null) {

            val senderId = FirebaseAuth.getInstance().currentUser.uid

            val chatDal = ChatDal()
            val userManager = UserManager(UserDal())
            val chatManager = ChatManager(chatDal, userManager)

            chatManager.add(Chat(senderId, receiverId, message, false, Timestamp.now())) {
                getMessages()
            }
        }
    }

    fun getMessages() {

        val chatDal = ChatDal()
        val userManager = UserManager(UserDal())
        val chatManager = ChatManager(chatDal, userManager)

        chatManager.getChatDetail(receiverId) { iDataResult ->
            if (iDataResult.success()) {
                chatDtoList.removeAll(chatDtoList)
                chatDtoList.clear()
                chatDtoList.addAll(iDataResult.data())
                chatViewAdapter.notifyDataSetChanged()
                chatViewAdapter.notifyItemInserted(chatDtoList.size);
                binding.recyclerViewChatActivity.scrollToPosition(chatViewAdapter.getItemCount() - 1);

            }

        }

    }

    fun openTools(view: View) {

        val alertDialog = BottomSheetDialog(this, R.style.BottonSheetTheme)

        alertDialog.setContentView(R.layout.custom_tools)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnDocument = alertDialog.findViewById<ImageButton>(R.id.btn_document)
        val btnGallery = alertDialog.findViewById<ImageButton>(R.id.btn_gallery)
        val btnCamera = alertDialog.findViewById<ImageButton>(R.id.btn_camera)
        val btnLocation = alertDialog.findViewById<ImageButton>(R.id.btn_location)
        val btnAudio = alertDialog.findViewById<ImageButton>(R.id.btn_audio)

        btnDocument!!.setOnClickListener {
            DocumentPermission.selectFile(this,this)
            alertDialog.dismiss()
        }

        btnGallery!!.setOnClickListener {
            GalleryPermission.selectImage(this,this)
            alertDialog.dismiss()

        }

        btnCamera!!.setOnClickListener {
            val intentToCamera = Intent(this,CameraActivity::class.java)
            startActivity(intentToCamera)
        }

        btnLocation!!.setOnClickListener {
            val intentToMaps = Intent(this,MapsActivity::class.java)
            startActivity(intentToMaps)
        }

        btnAudio!!.setOnClickListener {
            DocumentPermission.selectFile(this,this)
            alertDialog.dismiss()

        }

        alertDialog.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 2)
            }
        } else if (requestCode == 3) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.setType("*/*")
                startActivityForResult(intent, 4)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var uri: Uri? = null

        if (resultCode !== RESULT_CANCELED) {
            if (data != null) {
                uri = data.data

            }
        }

        super.onActivityResult(requestCode, resultCode, data)

    }

}