package com.beratyesbek.vhoops.core.firebaseCloudMessaging

import com.google.firebase.iid.FirebaseInstanceIdService

class FirebaseTokenService : FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        super.onTokenRefresh()
    }
}