package com.beratyesbek.vhoops.Core.DataAccess.Constants

import com.beratyesbek.vhoops.Core.Constants.Constants

class MeetingConstants {

    companion object {
        var KEY_COLLECTION_USERS = "users"
        var KEY_FIRST_NAME = "firstName"
        var KEY_LAST_NAME = "lastName"
        var KEY_USER_ID = "userId"
        var KEY_FCM_TOKEN = "token"
        var KEY_PROFILE_IMAGE = "profileImage"

        var KEY_PREFERENCE_NAME = "videoMeetingPreference"
        var KEY_IS_SIGNED_IN = "isSignedIn"

        var REMOTE_MSG_AUTHORIZATION = "Authorization"
        var REMOTE_MSG_CONTENT_TYPE = "Content-Type"

        var REMOTE_MSG_TYPE = "type"
        var REMOTE_MSG_INVITATION = "invitation"
        var REMOTE_MSG_MEETING_TYPE = "meetingType"
        var REMOTE_MSG_INVITER_TOKEN = "inviterToken"
        var REMOTE_MSG_DATA = "data"
        var REMOTE_MSG_REGISTRATION_IDS = "registration_ids"

        var REMOTE_MSG_INVITATION_RESPONSE = "invitationResponse"
        var REMOTE_MSG_INVITATION_ACCEPTED = "accepted"
        var REMOTE_MSG_INVITATION_REJECTED = "rejected"
        var REMOTE_MSG_INVITATION_CANCELED = "cancelled"

        var REMOTE_MSG_MEETING_ROOM = "meetingRoom"


        fun getRemoteMessageHeaders(): HashMap<String, String> {
            val headers = HashMap<String, String>()
            headers.put(
                MeetingConstants.REMOTE_MSG_AUTHORIZATION,
                "key=AAAAMfeklBU:APA91bHZoRDBcwRsBjOptQjwerKpcJcbZy7pOIo6QYHr-1uM_jX4j9vr_nKYgFfZ3B53UkBdbERcSF23LEhj4DpxLa033ropBXVllAyYxssD3_1gUoGJ7F25it0mnL-UQXD4hQkMZF8V",
            )

            headers.put(MeetingConstants.REMOTE_MSG_CONTENT_TYPE, "application/json")

            return headers
        }
    }
}