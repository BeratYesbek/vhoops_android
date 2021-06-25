package com.beratyesbek.vhoops.Core.firebaseCloudMessaging

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.beratyesbek.vhoops.Core.DataAccess.Constants.MeetingConstants
import com.beratyesbek.vhoops.views.activities.IncomingInvitationActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

open class FirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val type = remoteMessage.data[MeetingConstants.REMOTE_MSG_TYPE]
        if (type != null){
            if (type.equals(MeetingConstants.REMOTE_MSG_INVITATION)){
                val intent = Intent(applicationContext,IncomingInvitationActivity::class.java)

                intent.putExtra(MeetingConstants.KEY_FIRST_NAME,remoteMessage.data[MeetingConstants.KEY_FIRST_NAME])
                intent.putExtra(MeetingConstants.KEY_LAST_NAME,remoteMessage.data[MeetingConstants.KEY_LAST_NAME])
                intent.putExtra(MeetingConstants.KEY_PROFILE_IMAGE,remoteMessage.data[MeetingConstants.KEY_PROFILE_IMAGE])
                intent.putExtra(MeetingConstants.KEY_USER_ID,remoteMessage.data[MeetingConstants.KEY_USER_ID])
                intent.putExtra(MeetingConstants.REMOTE_MSG_MEETING_TYPE,remoteMessage.data[MeetingConstants.REMOTE_MSG_MEETING_TYPE])
                intent.putExtra(MeetingConstants.REMOTE_MSG_INVITER_TOKEN,remoteMessage.data[MeetingConstants.REMOTE_MSG_INVITER_TOKEN])
                intent.putExtra(MeetingConstants.REMOTE_MSG_MEETING_ROOM,remoteMessage.data[MeetingConstants.REMOTE_MSG_MEETING_ROOM])

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                startActivity(intent)

            }else if(type.equals(MeetingConstants.REMOTE_MSG_INVITATION_RESPONSE)){
                val intent = Intent(MeetingConstants.REMOTE_MSG_INVITATION_RESPONSE)
                intent.putExtra(
                    MeetingConstants.REMOTE_MSG_INVITATION_RESPONSE,
                    remoteMessage.data[MeetingConstants.REMOTE_MSG_INVITATION_RESPONSE]
                )
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
            }
        }
    }

}