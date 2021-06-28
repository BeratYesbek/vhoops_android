package com.beratyesbek.vhoops.views.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.beratyesbek.vhoops.Business.Abstract.IInvitationService
import com.beratyesbek.vhoops.Business.Abstract.IUserService
import com.beratyesbek.vhoops.Business.Concrete.InvitationManager
import com.beratyesbek.vhoops.Core.Constants.Constants
import com.beratyesbek.vhoops.Core.DataAccess.Constants.MeetingConstants
import com.beratyesbek.vhoops.Core.Utilities.Extension.downloadFromUrl
import com.beratyesbek.vhoops.Core.Utilities.Extension.placeHolderProgressBar
import com.beratyesbek.vhoops.databinding.ActivityOutgoingInvitationBinding
import com.beratyesbek.vhoops.entities.concrete.Invitation
import com.beratyesbek.vhoops.entities.concrete.User
import com.beratyesbek.vhoops.network.ApiClient
import com.beratyesbek.vhoops.network.ApiService
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class OutgoingInvitationActivity : AppCompatActivity() {

    private lateinit var dataBinding: ActivityOutgoingInvitationBinding
    private var inviterToken : String? = null
    private var receiverId :String? =null
    private var meetingRoom = ""
    @Inject
    protected lateinit var userService  :IUserService;
    @Inject
    protected lateinit var invitationService: IInvitationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityOutgoingInvitationBinding.inflate(layoutInflater)
        val view = dataBinding.root
        setContentView(view)


        val firstName = intent.getStringExtra(Constants.FIRST_NAME)
        val lastName = intent.getStringExtra(Constants.LAST_NAME)
        val token = intent.getStringExtra(Constants.TOKEN)
        val profileImage = intent.getStringExtra(Constants.PROFILE_IMAGE)
        val meetingType = intent.getStringExtra(Constants.MEETING_TYPE)
        receiverId = intent.getStringExtra(Constants.USER_ID)
        getInviterToken(meetingType,token)

        dataBinding.textViewUserNameOutgoingActivity.text = "$firstName $lastName".toUpperCase()

        var uri : Uri? = null
        if (profileImage != null){
            dataBinding.imageUserOutgoingInvitation.downloadFromUrl(profileImage, placeHolderProgressBar(this)
            )
        }

        dataBinding.btnReject.setOnClickListener {

            cancelInvitation(token)
        }


    }

    private fun addIncomingInvitationDate(){
        val senderId = FirebaseAuth.getInstance().currentUser.uid

        invitationService.add(Invitation(senderId,receiverId, Timestamp.now(),"outGoing")){

        }
    }

    private fun getUserData(meetingType: String?,token: String?){
        val id = FirebaseAuth.getInstance().currentUser.uid
        userService.getById(id){iDataResult ->
            if (iDataResult.success()){
                val user = iDataResult.data()[0]
                initiateMeeting(meetingType,user,token)
            }
        }
    }

    private fun getInviterToken(meetingType: String?,token: String?){
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            if (task.isSuccessful && task.result != null) {
                inviterToken = task.result?.token
                getUserData(meetingType,token)
            }
        }
    }


    private fun initiateMeeting(meetingType : String?,
                                user :User,
                                receiverToken : String?){
        try{
            val tokens = JSONArray()
            tokens.put(receiverToken)

            val body = JSONObject()
            val data = JSONObject()

            data.put(MeetingConstants.REMOTE_MSG_TYPE,MeetingConstants.REMOTE_MSG_INVITATION)
            data.put(MeetingConstants.REMOTE_MSG_MEETING_TYPE,meetingType)
            data.put(MeetingConstants.KEY_FIRST_NAME,user.firstName)
            data.put(MeetingConstants.KEY_LAST_NAME,user.lastName)
            data.put(MeetingConstants.KEY_USER_ID,user.userID)
            data.put(MeetingConstants.KEY_PROFILE_IMAGE,user.profileImage.toString())
            data.put(MeetingConstants.KEY_FCM_TOKEN,receiverToken)
            data.put(MeetingConstants.REMOTE_MSG_INVITER_TOKEN,inviterToken)

            meetingRoom = user.userID + "_" +  UUID.randomUUID().toString().substring(0,5)
            data.put(MeetingConstants.REMOTE_MSG_MEETING_ROOM,meetingRoom)

            body.put(MeetingConstants.REMOTE_MSG_DATA,data)
            body.put(MeetingConstants.REMOTE_MSG_REGISTRATION_IDS,tokens)

            sendRemoteMessage(body.toString(),MeetingConstants.REMOTE_MSG_INVITATION)


        }catch (exception : Exception){
            Toast.makeText(this,exception.message + "exception a",Toast.LENGTH_LONG).show();
        }
    }

    private fun sendRemoteMessage(remoteMessageBody :String,type :String){
        ApiClient.getClient()?.create(ApiService::class.java)?.sendRemoteMessage(
            MeetingConstants.getRemoteMessageHeaders(),remoteMessageBody
        )?.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful){
                    if (type.equals(MeetingConstants.REMOTE_MSG_INVITATION)){
                        addIncomingInvitationDate()
                        Toast.makeText(applicationContext,"Invitation send successfully",Toast.LENGTH_LONG).show()
                    }else if(type.equals(MeetingConstants.REMOTE_MSG_INVITATION_RESPONSE)){
                        Toast.makeText(applicationContext,"Invitation Cancelled",Toast.LENGTH_LONG).show()
                        finish()

                    }
                }else{
                    Toast.makeText(applicationContext,response.message(),Toast.LENGTH_LONG).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(applicationContext,t.message,Toast.LENGTH_LONG).show()
                finish()
            }

        })
    }

    private fun cancelInvitation(receiverToken : String?){
        try{

            val tokens = JSONArray()
            tokens.put(receiverToken)

            val body = JSONObject()
            val data = JSONObject()

            data.put(MeetingConstants.REMOTE_MSG_TYPE,MeetingConstants.REMOTE_MSG_INVITATION_RESPONSE)
            data.put(MeetingConstants.REMOTE_MSG_INVITATION_RESPONSE,MeetingConstants.REMOTE_MSG_INVITATION_CANCELED)

            body.put(MeetingConstants.REMOTE_MSG_DATA,data)
            body.put(MeetingConstants.REMOTE_MSG_REGISTRATION_IDS,tokens)

            sendRemoteMessage(body.toString(),MeetingConstants.REMOTE_MSG_INVITATION_RESPONSE)

        }catch (exception :Exception){

        }
    }

    private val invitationBroadcastReceiver : BroadcastReceiver = object :BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val type = intent?.getStringExtra(MeetingConstants.REMOTE_MSG_INVITATION_RESPONSE)
            if (type != null){
                if (type.equals(MeetingConstants.REMOTE_MSG_INVITATION_ACCEPTED)){
                    val serverUrl = URL("https://meet.jit.si")
                    val jitsiMeetConferenceOptions = JitsiMeetConferenceOptions
                        .Builder().setServerURL(serverUrl)
                        .setWelcomePageEnabled(false)
                        .setRoom(meetingRoom)
                        .build()
                    JitsiMeetActivity.launch(this@OutgoingInvitationActivity,jitsiMeetConferenceOptions)
                    finish()
                }else{
                    Toast.makeText(context,"Invitation rejected",Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(applicationContext).registerReceiver(
            invitationBroadcastReceiver,
            IntentFilter(MeetingConstants.REMOTE_MSG_INVITATION_RESPONSE)
        )
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(applicationContext).unregisterReceiver(
            invitationBroadcastReceiver
        )
    }



}