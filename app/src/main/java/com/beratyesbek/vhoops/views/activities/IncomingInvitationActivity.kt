package com.beratyesbek.vhoops.views.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.beratyesbek.vhoops.Core.DataAccess.Constants.MeetingConstants
import com.beratyesbek.vhoops.Core.Utilities.Extension.downloadFromUrl
import com.beratyesbek.vhoops.Core.Utilities.Extension.placeHolderProgressBar
import com.beratyesbek.vhoops.R
import com.beratyesbek.vhoops.databinding.ActivityIncomingInvitationBinding
import com.beratyesbek.vhoops.network.ApiClient
import com.beratyesbek.vhoops.network.ApiService
import com.google.gson.JsonArray
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL

class IncomingInvitationActivity : AppCompatActivity() {


    private lateinit var dataBinding: ActivityIncomingInvitationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityIncomingInvitationBinding.inflate(layoutInflater)
        val view = dataBinding.root
        setContentView(view)

        val userProfileImage = intent.getStringExtra(MeetingConstants.KEY_PROFILE_IMAGE)
        val firstName = intent.getStringExtra(MeetingConstants.KEY_FIRST_NAME)
        val lastName = intent.getStringExtra(MeetingConstants.KEY_LAST_NAME)
        dataBinding.imageUserIncomingInvitation.downloadFromUrl(
            userProfileImage,
            placeHolderProgressBar(this)
        )
        dataBinding.textViewUserNameIncomingActivity.text = "$firstName $lastName".toUpperCase()

        dataBinding.btnRejectInComing.setOnClickListener {
            sendInvitationResponse(
                intent.getStringExtra(MeetingConstants.REMOTE_MSG_INVITER_TOKEN),
                MeetingConstants.REMOTE_MSG_INVITATION_REJECTED

            )
        }
        dataBinding.btnSuccessInComing.setOnClickListener {
            sendInvitationResponse(
                intent.getStringExtra(MeetingConstants.REMOTE_MSG_INVITER_TOKEN),
                MeetingConstants.REMOTE_MSG_INVITATION_ACCEPTED
            )
        }

    }

    private fun sendInvitationResponse(receiverToken : String?,type: String){
        try{

            val tokens = JSONArray()
            tokens.put(receiverToken)

            val body = JSONObject()
            val data = JSONObject()

            data.put(MeetingConstants.REMOTE_MSG_TYPE,MeetingConstants.REMOTE_MSG_INVITATION_RESPONSE)
            data.put(MeetingConstants.REMOTE_MSG_INVITATION_RESPONSE,type)

            body.put(MeetingConstants.REMOTE_MSG_DATA,data)
            body.put(MeetingConstants.REMOTE_MSG_REGISTRATION_IDS,tokens)

            sendRemoteMessage(body.toString(),type)

        }catch (exception :Exception){

        }
    }


    private fun sendRemoteMessage(remoteMessageBody :String,type :String){
        ApiClient.getClient()?.create(ApiService::class.java)?.sendRemoteMessage(
            MeetingConstants.getRemoteMessageHeaders(),remoteMessageBody
        )?.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful){
                    if (type.equals(MeetingConstants.REMOTE_MSG_INVITATION_ACCEPTED)){
                        try{
                            val serverUrl = URL("https://meet.jit.si")
                            val jitsiMeetConferenceOptions = JitsiMeetConferenceOptions
                                .Builder().setServerURL(serverUrl)
                                .setWelcomePageEnabled(false)
                                .setRoom(intent.getStringExtra(MeetingConstants.REMOTE_MSG_MEETING_ROOM))
                                .build()
                            JitsiMeetActivity.launch(this@IncomingInvitationActivity,jitsiMeetConferenceOptions)
                            finish()
                        }catch (exception :Exception){

                        }

                    }else{
                        Toast.makeText(applicationContext,"Invitation rejected", Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(applicationContext,response.message(), Toast.LENGTH_LONG).show()
                }
                finish()

            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(applicationContext,t.message, Toast.LENGTH_LONG).show()
                finish()
            }

        })
    }

    private val invitationBroadcastReceiver : BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val type = intent?.getStringExtra(MeetingConstants.REMOTE_MSG_INVITATION_RESPONSE)
            if (type != null){
                if (type.equals(MeetingConstants.REMOTE_MSG_INVITATION_CANCELED)){
                    Toast.makeText(context,"Invitation accepted",Toast.LENGTH_LONG).show()
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