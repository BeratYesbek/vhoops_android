package com.beratyesbek.vhoops.views.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Dialog
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
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.beratyesbek.vhoops.adapter.ChatViewAdapter
import com.beratyesbek.vhoops.business.ChatFileOperations
import com.beratyesbek.vhoops.business.concretes.ChatManager
import com.beratyesbek.vhoops.business.concretes.UserManager
import com.beratyesbek.vhoops.core.constants.Constants
import com.beratyesbek.vhoops.core.dataAccess.constants.ExtensionConstants
import com.beratyesbek.vhoops.core.permission.DocumentPermission
import com.beratyesbek.vhoops.core.permission.GalleryPermission
import com.beratyesbek.vhoops.core.permission.RecordAudioPermission
import com.beratyesbek.vhoops.core.utilities.animations.Animation
import com.beratyesbek.vhoops.dataAccess.concretes.ChatDal
import com.beratyesbek.vhoops.dataAccess.concretes.UserDal
import com.beratyesbek.vhoops.listener.JitsimeetListener
import com.beratyesbek.vhoops.entities.concrete.Chat
import com.beratyesbek.vhoops.entities.concrete.dtos.ChatDto
import com.beratyesbek.vhoops.R
import com.beratyesbek.vhoops.viewUtilities.OnItemClickListener
import com.beratyesbek.vhoops.views.fragment.CameraFragment
import com.beratyesbek.vhoops.databinding.ActivityChatBinding
import com.beratyesbek.vhoops.entities.concrete.User
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import java.io.File


class ChatActivity : AppCompatActivity(),OnItemClickListener,JitsimeetListener {

    private lateinit var mediaRecorder: MediaRecorder
    private lateinit var receiverId: String
    private val chatDtoList: ArrayList<ChatDto> = ArrayList()
    private lateinit var chatViewAdapter: ChatViewAdapter
    private lateinit var transaction: FragmentTransaction;
    private var editTextControl = true
    private var toolbarAnimControl = false
    private lateinit var positionArray: ArrayList<Int>
    private lateinit var selectMessageList : ArrayList<Chat>
    private lateinit var deleteAlertDialog: Dialog

    private lateinit var dataBinding: ActivityChatBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataBinding = DataBindingUtil.setContentView(this,R.layout.activity_chat)


        setSupportActionBar(dataBinding.includeChatActivity.toolbarChat)

        positionArray = ArrayList()
        selectMessageList = ArrayList()

        receiverId = intent.getStringExtra(Constants.USER_ID)!!
        val fullName = intent.getStringExtra(Constants.FULL_NAME)
        val profileImage = intent.getStringExtra(Constants.PROFILE_IMAGE)
        val token = intent.getStringExtra(Constants.TOKEN);
        val firstName = intent.getStringExtra(Constants.FIRST_NAME);
        val lastName = intent.getStringExtra(Constants.LAST_NAME);

        var uri: Uri? = null

        val imageView = dataBinding.includeChatActivity.imageViewProfileChatToolbar

        if (profileImage != null) {
            uri = Uri.parse(profileImage)
            Picasso.get().load(uri).into(imageView)
        }
        dataBinding.includeChatActivity.textViewUserFullNameChatActivity.text = fullName!!.toUpperCase()

        dataBinding.includeChatActivity.imageButtonBackChatToolbar.setOnClickListener {
            onBackPressed()
        }

        dataBinding.includeChatActivity.btnDeleteChatToolbar.setOnClickListener {
            displayAlertDialogForDelete({ multipleDeleteMessage() })


        }
        dataBinding.includeChatActivity.btnVideoMeetingChatToolbar.setOnClickListener {

            initiateVideoMeeting(User(firstName!!,lastName!!,"","","",receiverId,token!!,uri,"",""))
        }

        dataBinding.editTextEnterMessage.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.isNullOrEmpty()) {
                    if (editTextControl) {
                        hideAnim(dataBinding.imageButtonMic)
                        revealAnim(dataBinding.imageButtonSend)
                        editTextControl = false

                    }

                } else {

                    hideAnim(dataBinding.imageButtonSend)
                    revealAnim(dataBinding.imageButtonMic)
                    editTextControl = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        dataBinding.imageButtonMic.setOnClickListener {
            audioRecorder()
        }

        getMessages()
        runRecyclerView()


    }


    fun runRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        dataBinding.recyclerViewChatActivity.layoutManager = layoutManager
        layoutManager.setStackFromEnd(true);
        dataBinding.recyclerViewChatActivity.refreshDrawableState();
        chatViewAdapter = ChatViewAdapter(chatDtoList,this)
        dataBinding.recyclerViewChatActivity.adapter = chatViewAdapter
        dataBinding.recyclerViewChatActivity.refreshDrawableState()
        dataBinding.recyclerViewChatActivity.setItemAnimator(DefaultItemAnimator())
        Animation.listItemAnimation(dataBinding.recyclerViewChatActivity)

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
            val file = File(this.getExternalFilesDir("/")!!.absolutePath + "/" + "fileName.3gp")
            val mediaPlayer = MediaPlayer.create(
                this,
                file.toUri()
            )

            mediaPlayer.start()
        }

        btnCancel!!.setOnClickListener {
            alertDialog.dismiss()
        }

        btnSend!!.setOnClickListener {

            val file = File(this.getExternalFilesDir("/")!!.absolutePath + "/" + "fileName.3gp")

            uploadFile(file.toUri(), ExtensionConstants.AUDIO)
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


        val message = dataBinding.editTextEnterMessage.text.toString()

        if (message != null) {

            sendMessage(message)
        }
    }

    private fun sendMessage(message: Any) {

        val senderId = FirebaseAuth.getInstance().currentUser.uid

        val chatDal = ChatDal()
        val userManager = UserManager(UserDal())
        val chatManager = ChatManager(chatDal, userManager)

        chatManager.add(Chat(senderId, receiverId, message, false, Timestamp.now())) {
            chatDtoList.clear()
            getMessages()

        }
        dataBinding.editTextEnterMessage.text = null

    }

    private fun getMessages() {

        val chatDal = ChatDal()
        val userManager = UserManager(UserDal())
        val chatManager = ChatManager(chatDal, userManager)

        chatManager.getChatDetail(receiverId) { iDataResult ->
            if (iDataResult.success()) {
                dataBinding.progressBarChatActivity.visibility = View.INVISIBLE
                chatDtoList.clear()

                chatDtoList.addAll(iDataResult.data()!!)
                chatViewAdapter.notifyDataSetChanged()
                chatViewAdapter.notifyItemInserted(chatDtoList.size)
                dataBinding.recyclerViewChatActivity.scrollToPosition(chatViewAdapter.getItemCount() - 1)
                dataBinding.recyclerViewChatActivity.scheduleLayoutAnimation()

            }else{

                chatDtoList.clear()
                chatViewAdapter.notifyDataSetChanged()
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
            intentToCamera.putExtra("type",Constants.CHAT_ACTIVITY)
            intentToCamera.putExtra("receiverId",receiverId)
            startActivity(intentToCamera)
        }

        btnLocation!!.setOnClickListener {
            val intentToMaps = Intent(this, MapsActivity::class.java)
            intentToMaps.putExtra("receiverId", receiverId)
            intentToMaps.putExtra("type",Constants.CHAT_ACTIVITY)
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
        grantResults: IntArray) {

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
        super.onActivityResult(requestCode, resultCode, data)


        if (resultCode !== RESULT_CANCELED ) {
            if (data != null) {
                val uri = data.data
                if(requestCode == 2){
                        runImageFragment(uri!!)
                }else{
                    runChatFileOperations(uri!!)
                }

            }
        }


    }
    private fun runChatFileOperations(uri : Uri){
        ChatFileOperations.checkUriExtension(uri,this,receiverId)
    }

    fun uploadFile(message: Any, type: String) {
        val senderId = FirebaseAuth.getInstance().currentUser.uid
        chatDtoList.clear()
        val chatManager = ChatManager(ChatDal(), UserManager(UserDal()))
        chatManager.uploadFile(message as Uri, type) { iDataResult ->
            getFiles(iDataResult.data()!!)
        }

    }

    private fun getFiles(path: String) {
        val chatManager = ChatManager(ChatDal(), UserManager(UserDal()))
        chatManager.getFile(path) { iDataResult ->
            sendMessage(iDataResult.data()!!)
        }
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

    override fun onBackPressed() {

        if (supportFragmentManager.findFragmentById(R.id.frameLayout_chat) != null) {
            removeFragment()
        } else {
            super.onBackPressed()

        }
    }

    private fun runImageFragment(uri: Uri) {

        transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.fade_in_anim, R.anim.slide_out_anim)
        transaction.replace(R.id.frameLayout_chat, CameraFragment(uri, receiverId,null,null,Constants.CHAT_ACTIVITY))
        transaction.commit()
    }

    private fun removeFragment() {

        val fragment: Fragment? = supportFragmentManager.findFragmentById(R.id.frameLayout_chat)
        transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.fade_in_anim, R.anim.slide_out_anim)
        transaction.remove(fragment!!)
        transaction.commit()
    }


    private fun multipleDeleteMessage(){

        if (selectMessageList.size >0){
            val chatManager = ChatManager(ChatDal(),UserManager(UserDal()))
            chatManager.deleteMulti(selectMessageList){
                selectMessageList.clear()
                clearToolbar()
                deleteAlertDialog.dismiss()
            }
        }
    }



    private fun displayAlertDialogForDelete(function:() -> Unit){
        deleteAlertDialog= Dialog(this)
        deleteAlertDialog.setCancelable(false)
        deleteAlertDialog.setContentView(R.layout.custom_alert_delete_dialog)
        deleteAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val textView = deleteAlertDialog.findViewById<TextView>(R.id.textView_info_alertDeleteDialog)
        val btnDelete = deleteAlertDialog.findViewById<Button>(R.id.btn_accept_alertDeleteDialog)
        val btnCancel = deleteAlertDialog.findViewById<Button>(R.id.btn_cancel_alertDeleteDialog)
        val progressBar = deleteAlertDialog.findViewById<ProgressBar>(R.id.progressBar_alertDialogDelete)

        btnDelete.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            btnDelete.visibility = View.INVISIBLE
            btnCancel.visibility = View.INVISIBLE
            textView.text = "Please wait, deleting..."

            function()

        }
        btnCancel.setOnClickListener {
            deleteAlertDialog.dismiss()
        }
        deleteAlertDialog.show()


    }
    private fun deleteChat(){
        selectMessageList.clear()
        chatDtoList.forEachIndexed { index, chatDto ->
            selectMessageList.add(Chat(chatDto.senderId,chatDto.receiverId,chatDto.message,chatDto.documentId,chatDto.isSeen,chatDto.timeToSend))

        }
        multipleDeleteMessage()
        chatDtoList.clear()
        chatViewAdapter.notifyDataSetChanged()
    }

    private fun clearToolbar(){
        Animation.hideAnim(dataBinding.includeChatActivity.linearLayoutToolbarChatDeleteView)
        Animation.revealAnim(dataBinding.includeChatActivity.btnVideoMeetingChatToolbar)
        toolbarAnimControl = false
    }

    override fun onItemClick(position: Int) {
        if (selectMessageList.size > 0){

            positionArray.forEachIndexed { index, i ->
                if (i == position){
                    println(position)
                    selectMessageList.removeAt(index)
                    dataBinding.includeChatActivity.textViewDeleteItemChatToolbar.text = selectMessageList.size.toString()
                }
            }

            if (selectMessageList.size <= 0){
                Animation.hideAnim(dataBinding.includeChatActivity.linearLayoutToolbarChatDeleteView)
                Animation.revealAnim(dataBinding.includeChatActivity.btnVideoMeetingChatToolbar)
                toolbarAnimControl = false
            }

        }else{
            if (!toolbarAnimControl && selectMessageList.size <= 0 ){
                Animation.hideAnim(dataBinding.includeChatActivity.linearLayoutToolbarChatDeleteView)
                Animation.revealAnim(dataBinding.includeChatActivity.btnVideoMeetingChatToolbar)
                toolbarAnimControl = false
            }
        }

    }

    override fun onItemLongClick(position: Int) {
        positionArray.add(position)
        val chatDto = chatDtoList.get(position)
        val chat : Chat = Chat(chatDto.senderId,chatDto.receiverId,chatDto.message,chatDto.documentId,chatDto.isSeen,chatDto.timeToSend)
        selectMessageList.add(chat)
        dataBinding.includeChatActivity.textViewDeleteItemChatToolbar.text = selectMessageList.size.toString()
        if (selectMessageList.size > 0 && !toolbarAnimControl){
            Animation.revealAnim(dataBinding.includeChatActivity.linearLayoutToolbarChatDeleteView)
            Animation.hideAnim(dataBinding.includeChatActivity.btnVideoMeetingChatToolbar)
            toolbarAnimControl = true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.deleteChat -> displayAlertDialogForDelete({ deleteChat() })
        }
        return true
    }

    override fun initiateVideoMeeting(user: User) {
       val intentToOutGoingInvitation = Intent(this,OutgoingInvitationActivity::class.java)
        intentToOutGoingInvitation.putExtra(Constants.FIRST_NAME,user.firstName)
        intentToOutGoingInvitation.putExtra(Constants.LAST_NAME,user.lastName)
        intentToOutGoingInvitation.putExtra(Constants.PROFILE_IMAGE,user.profileImage.toString())
        intentToOutGoingInvitation.putExtra(Constants.TOKEN,user.token)
        intentToOutGoingInvitation.putExtra(Constants.USER_ID,user.userID)
        startActivity(intentToOutGoingInvitation)


    }

    override fun initiateAudioMeeting(user: User) {
        TODO("Not yet implemented")
    }


}