package com.beratyesbek.Vhoops.Views.Activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.beratyesbek.Vhoops.Adapter.ChatViewAdapter
import com.beratyesbek.Vhoops.Business.Concrete.ChatManager
import com.beratyesbek.Vhoops.Business.Concrete.UserManager
import com.beratyesbek.Vhoops.Core.Constants.Constants
import com.beratyesbek.Vhoops.Core.Permission.DocumentPermission
import com.beratyesbek.Vhoops.Core.Permission.GalleryPermission
import com.beratyesbek.Vhoops.Core.Permission.RecordAudioPermission
import com.beratyesbek.Vhoops.Core.Utilities.Control.CheckAndroidUriType
import com.beratyesbek.Vhoops.DataAccess.Concrete.ChatDal
import com.beratyesbek.Vhoops.DataAccess.Concrete.UserDal
import com.beratyesbek.Vhoops.Entities.Concrete.Chat
import com.beratyesbek.Vhoops.Entities.Concrete.Dtos.ChatDto
import com.beratyesbek.Vhoops.R
import com.beratyesbek.Vhoops.databinding.ActivityChatBinding
import com.gauravk.audiovisualizer.visualizer.WaveVisualizer
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import java.io.File


class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var mediaRecorder: MediaRecorder
    private lateinit var receiverId: String
    private val chatDtoList: ArrayList<ChatDto> = ArrayList()
    private lateinit var chatViewAdapter: ChatViewAdapter
    private val CREATE_FILE = 1
    private var editTextControl = true
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



        binding.editTextEnterMessage.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.isNullOrEmpty()) {
                    if (editTextControl) {
                        hideAnim(binding.imageButtonMic)
                        revealAnim(binding.imageButtonSend)
                        editTextControl = false

                    }

                } else {

                    hideAnim(binding.imageButtonSend)
                    revealAnim(binding.imageButtonMic)
                    editTextControl = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        binding.imageButtonMic.setOnClickListener {
            audioRecorder()
        }

        getMessages()
        runRecyclerView()


    }

    fun btnMove(motionEvent: MotionEvent, view: View) {

    }


    fun runRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewChatActivity.layoutManager = layoutManager
        layoutManager.setStackFromEnd(true);
        binding.recyclerViewChatActivity.refreshDrawableState();
        chatViewAdapter = ChatViewAdapter(chatDtoList)
        binding.recyclerViewChatActivity.adapter = chatViewAdapter

    }

    fun audioRecorder() {

        val alertDialog = BottomSheetDialog(this, R.style.BottonSheetTheme)
        alertDialog.setContentView(R.layout.recorder_view_dialog)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnMic = alertDialog.findViewById<ImageButton>(R.id.btn_recorderView_dialog)
        val btnListen = alertDialog.findViewById<ImageButton>(R.id.btn_listen_recorder)
        val btnSend = alertDialog.findViewById<ImageButton>(R.id.btn_send_recorder)
        val btnCancel = alertDialog.findViewById<ImageButton>(R.id.btn_cancel_recorder)
        val chronometer = alertDialog.findViewById<Chronometer>(R.id.chronometer_recorder_dialog)
        val textViewInfo = alertDialog.findViewById<TextView>(R.id.textView_info_recorder_dialog)
        textViewInfo!!.setText("Press on the button")

        btnMic?.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {

                when (motionEvent?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        var pauseOffSet = 0
                        textViewInfo!!.setText("Recording...")
                        chronometer!!.base = SystemClock.elapsedRealtime() - pauseOffSet
                        chronometer.start()
                        hideAnim(btnListen!!)
                        hideAnim(btnSend!!)
                        record()
                    }
                    MotionEvent.ACTION_MOVE -> btnMove(motionEvent, view!!)
                    MotionEvent.ACTION_UP -> {
                        textViewInfo!!.setText("Listen or send")
                        chronometer!!.stop()
                        revealAnim(btnListen!!)
                        revealAnim(btnSend!!)
                        stopRecorder()
                    }

                }
                return view?.onTouchEvent(motionEvent) ?: true

            }

        })

        btnListen!!.setOnClickListener {
            val mediaPlayer = MediaPlayer.create(this,Uri.parse(this.getExternalFilesDir("/")!!.absolutePath +"/" + "fileName.3gp"))
            mediaPlayer.start()
        }

        btnCancel!!.setOnClickListener {
            alertDialog.dismiss()
        }

        btnSend!!.setOnClickListener {

        }

        alertDialog.show()


    }

    fun record() {
        val recordPath = this.getExternalFilesDir("/")!!.absolutePath
        val recordFile = "fileName.3gp"
        val result = RecordAudioPermission.checkPermission(this, this)
        if (result) {
            mediaRecorder = MediaRecorder()
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB)
            mediaRecorder.setOutputFile(recordPath + "/" + recordFile)
            mediaRecorder.prepare()



            mediaRecorder.start()

        }
    }

    fun stopRecorder() {
        mediaRecorder.stop()
        mediaRecorder.release()
    }

    fun btnSendMessage(view: View) {


        val message = binding.editTextEnterMessage.text.toString()

        if (message != null) {

            sendMessage(message)
        }
    }

    fun sendMessage(message: Any) {

        val senderId = FirebaseAuth.getInstance().currentUser.uid

        val chatDal = ChatDal()
        val userManager = UserManager(UserDal())
        val chatManager = ChatManager(chatDal, userManager)

        chatManager.add(Chat(senderId, receiverId, message, false, Timestamp.now())) {
            getMessages()
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
            DocumentPermission.selectFile(this, this)
            alertDialog.dismiss()
        }

        btnGallery!!.setOnClickListener {
            GalleryPermission.selectImage(this, this)
            alertDialog.dismiss()

        }

        btnCamera!!.setOnClickListener {
            val intentToCamera = Intent(this, CameraActivity::class.java)
            startActivity(intentToCamera)
        }

        btnLocation!!.setOnClickListener {
            val intentToMaps = Intent(this, MapsActivity::class.java)
            intentToMaps.putExtra("receiverId", receiverId)
            startActivity(intentToMaps)
        }

        btnAudio!!.setOnClickListener {
            DocumentPermission.selectFile(this, this)
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
        } else if (requestCode == 10) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var uri: Uri? = null

        if (resultCode !== RESULT_CANCELED) {
            if (data != null) {
                uri = data.data
                checkUriExtension(uri)

            }
        }

        super.onActivityResult(requestCode, resultCode, data)

    }

    private fun checkUriExtension(uri: Uri?) {

        val type = CheckAndroidUriType.checkUriType(uri, this)
        if (type != null) {
            uploadFile(uri as Any, type)
        }

    }

    fun uploadFile(message: Any, type: String) {
        val senderId = FirebaseAuth.getInstance().currentUser.uid
        val chatManager = ChatManager(ChatDal(), UserManager(UserDal()))
        chatManager.uploadFile(message as Uri, type) { iDataResult ->
            getFiles(iDataResult.data())
        }

    }

    fun getFiles(path: String) {
        val chatManager = ChatManager(ChatDal(), UserManager(UserDal()))
        chatManager.getFile(path) { iDataResult ->
            sendMessage(iDataResult.data())
        }
    }

    private fun zoomInAnim(view: View) {
        val cx = view.width * 5
        val cy = view.height * 5
        val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()
        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius)

        anim.start()
    }

    private fun zoomOutAnim(view: View) {
        val cx = view.width / 5
        val cy = view.height / 5
        val initialRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()
        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0f)
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)


            }
        })
        anim.start()
    }

    private fun revealAnim(view: View) {

        val cx = view.width / 2
        val cy = view.height / 2
        val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()
        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius)
        view.visibility = View.VISIBLE
        anim.start()
    }

    private fun hideAnim(view: View) {
        val cx = view.width / 2
        val cy = view.height / 2
        val initialRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()
        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0f)
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                view.visibility = View.INVISIBLE
            }
        })
        anim.start()
    }

}