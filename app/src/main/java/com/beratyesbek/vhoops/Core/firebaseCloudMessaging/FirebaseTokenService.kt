package com.beratyesbek.vhoops.Core.firebaseCloudMessaging

import com.google.firebase.iid.FirebaseInstanceIdService

class FirebaseTokenService : FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        super.onTokenRefresh()
    }
}