package com.beratyesbek.vhoops.views.activities

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
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.beratyesbek.vhoops.adapter.GroupChatViewAdapter
import com.beratyesbek.vhoops.business.GroupChatFileOperations
import com.beratyesbek.vhoops.core.constants.Constants
import com.beratyesbek.vhoops.core.dataAccess.constants.ExtensionConstants
import com.beratyesbek.vhoops.core.permission.DocumentPermission
import com.beratyesbek.vhoops.core.permission.GalleryPermission
import com.beratyesbek.vhoops.core.permission.RecordAudioPermission
import com.beratyesbek.vhoops.core.utilities.animations.Animation
import com.beratyesbek.vhoops.core.utilities.extensions.downloadFromUrl
import com.beratyesbek.vhoops.core.utilities.extensions.placeHolderProgressBar
import com.beratyesbek.vhoops.mvvm.GroupChatViewModel
import com.beratyesbek.vhoops.R
import com.beratyesbek.vhoops.viewUtilities.OnItemClickListener
import com.beratyesbek.vhoops.databinding.ActivityGroupChatBinding
import com.beratyesbek.vhoops.entities.concrete.Group
import com.beratyesbek.vhoops.entities.concrete.GroupChat
import com.beratyesbek.vhoops.entities.concrete.dtos.GroupChatDto
import com.beratyesbek.vhoops.views.fragment.CameraFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class GroupChatActivity : AppCompatActivity(), OnItemClickListener {


    private lateinit var dataBinding: ActivityGroupChatBinding
    private lateinit var mediaRecorder: MediaRecorder
    private var editTextControl = true
    private val viewModel: GroupChatViewModel by viewModels()
    private var groupId: String? = null
    private lateinit var transaction: FragmentTransaction;
    private lateinit var group :Group
    private val groupChatList: ArrayList<GroupChatDto> = ArrayList()

    private lateinit var groupChatViewAdapter: GroupChatViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataBinding = ActivityGroupChatBinding.inflate(layoutInflater)
        val view = dataBinding.root
        setContentView(view)

        val toolbar = dataBinding.includeGroupChatActivity.toolbarGroupChat
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)


        val groupIcon = intent.getStringExtra("GroupIcon")
        groupId = intent.getStringExtra("GroupId")
        val groupName = intent.getStringExtra("GroupName")
        val adminId = intent.getStringExtra("AdminId")
        val createdDate = intent.getStringExtra("CreatedDate")
        viewModel.groupId = groupId!!

        viewModel.getGroupData(groupId!!)

        viewModel.group.observe(this,{
            group = it!!.get(0)
        })

        dataBinding.includeGroupChatActivity.textViewGroupNameChatActivity.text = groupName

        if (groupIcon != null) {
            val imageView = dataBinding.includeGroupChatActivity.imageViewProfileGroupChatToolbar
            this.let {
                imageView.downloadFromUrl(groupIcon.toString(), placeHolderProgressBar(it))
            }
        }
        dataBinding.includeGroupChatActivity.imageButtonBackGroupChatToolbar.setOnClickListener {
            onBackPressed()
        }
        dataBinding.imageButtonAttachFileGroupChat.setOnClickListener {
            toolsDialog()
        }

        dataBinding.imageButtonMicGroupChat.setOnClickListener {
            audioRecorder()
        }
        dataBinding.includeGroupChatActivity.imageViewProfileGroupChatToolbar.setOnClickListener {
            val intentToGroupInfo = Intent(this,GroupInfoActivity::class.java)
            intentToGroupInfo.putExtra(Constants.GROUP_ID,groupId)
            startActivity(intentToGroupInfo)
        }

        dataBinding.editTextEnterMessageGroupChat.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.isNullOrEmpty()) {
                    if (editTextControl) {
                          Animation.hideAnim(dataBinding.imageButtonMicGroupChat)
                          Animation.revealAnim(dataBinding.imageButtonSendGroupChat)
                        editTextControl = false

                    }

                } else {

                      Animation.hideAnim(dataBinding.imageButtonSendGroupChat)
                      Animation.revealAnim(dataBinding.imageButtonMicGroupChat)
                    editTextControl = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        runRecyclerView()

        viewModel.getData(groupId!!)

        viewModel.messageList.observe(this, androidx.lifecycle.Observer { groupChatDtoList ->
            dataBinding.progressBarGroupChatActivity.visibility = View.INVISIBLE
            groupChatList.clear()
            groupChatList.addAll(groupChatDtoList)
            groupChatViewAdapter.notifyDataSetChanged()
            dataBinding.recyclerViewChatActivity.scrollToPosition(groupChatViewAdapter.itemCount - 1)
            dataBinding.recyclerViewChatActivity.scheduleLayoutAnimation()
        })
    }


    private fun runRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        dataBinding.recyclerViewChatActivity.layoutManager = layoutManager
        layoutManager.stackFromEnd = true;
        dataBinding.recyclerViewChatActivity.refreshDrawableState();
        groupChatViewAdapter = GroupChatViewAdapter(groupChatList, this)
        dataBinding.recyclerViewChatActivity.adapter = groupChatViewAdapter
        dataBinding.recyclerViewChatActivity.refreshDrawableState()
        dataBinding.recyclerViewChatActivity.setItemAnimator(DefaultItemAnimator())
        Animation.listItemAnimation(dataBinding.recyclerViewChatActivity)
    }



    fun btnSendMessage(view: View) {
        val message = dataBinding.editTextEnterMessageGroupChat.text.toString()
        if (message.isNotEmpty()) {
            val userId = FirebaseAuth.getInstance().currentUser.uid
            viewModel.sendMessage(GroupChat(userId, groupId!!, message, false, Timestamp.now()))
            dataBinding.editTextEnterMessageGroupChat.text = null
        }
    }

    private fun toolsDialog() {
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
            intentToCamera.putExtra("type",Constants.GROUP_CHAT_ACTIVITY)
            intentToCamera.putExtra("groupId",groupId)
            startActivity(intentToCamera)
        }

        btnLocation!!.setOnClickListener {
            val intentToMaps = Intent(this, MapsActivity::class.java)
            intentToMaps.putExtra("groupId", groupId)
            intentToMaps.putExtra("type",Constants.GROUP_CHAT_ACTIVITY)
            startActivity(intentToMaps)
        }

        btnAudio!!.setOnClickListener {
            DocumentPermission.selectFile(this, this)
            alertDialog.dismiss()

        }


        alertDialog.show()
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
                        Animation.revealAnim(btnListen!!)
                        Animation.hideAnim(btnSend!!)
                        startRecorder()
                    }
                    MotionEvent.ACTION_UP -> {
                        textViewInfo!!.setText("Listen or send")
                        chronometer!!.stop()
                        Animation.hideAnim(btnListen!!)
                        Animation.revealAnim(btnSend!!)
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

            viewModel.uploadFile(file.toUri(), ExtensionConstants.AUDIO)
        }

        alertDialog.show()


    }

    fun startRecorder() {
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

    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentById(R.id.frameLayout_groupChat) != null) {
            removeFragment()
        } else {
            super.onBackPressed()

        }
    }

    private fun runImageFragment(uri: Uri) {

        transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.fade_in_anim, R.anim.slide_out_anim)
        transaction.replace(R.id.frameLayout_groupChat, CameraFragment(uri, null,groupId,null,
            Constants.CHAT_ACTIVITY)
        )
        transaction.commit()
    }

    private fun removeFragment() {

        val fragment: Fragment? = supportFragmentManager.findFragmentById(R.id.frameLayout_groupChat)
        transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.fade_in_anim, R.anim.slide_out_anim)
        transaction.remove(fragment!!)
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.group_chat_menu,menu)

        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.groupInfo -> {
                val intentToGroupInfo = Intent(this,GroupInfoActivity::class.java)
                intentToGroupInfo.putExtra(Constants.GROUP_ID,groupId)
                startActivity(intentToGroupInfo)
            }
            R.id.leaveGroup -> {
                leaveGroup()
            }
            else -> return super.onOptionsItemSelected(item);
        }
        return true
    }

    private fun leaveGroup(){
        val currentUserId = FirebaseAuth.getInstance().currentUser.uid
        group.memberIdList!!.remove(currentUserId)
        viewModel.leaveGroup(group)

        viewModel.leaveGroupResult.observe(this,{
            if (it){
                val intent = Intent(this,NavigationBottomActivity::class.java)
                startActivity(intent)
                finish()

            }else{
                Toast.makeText(this,"Failed",Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun runChatFileOperations(uri: Uri) {
        val type = GroupChatFileOperations.checkUriExtension(uri, this)

        if (type != null) {
            viewModel.uploadFile(uri, type)
        }
    }


    override fun onItemClick(position: Int) {
    }

    override fun onItemLongClick(position: Int) {
        TODO("Not yet implemented")
    }
}