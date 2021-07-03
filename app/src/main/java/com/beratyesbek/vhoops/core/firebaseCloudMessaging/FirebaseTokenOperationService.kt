package com.beratyesbek.vhoops.core.firebaseCloudMessaging

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

class FirebaseTokenOperationService {


    fun getToken(context: Context) {
        FirebaseApp.initializeApp(context)
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        val firebaseMessaging = FirebaseMessaging.getInstance()
        firebaseMessaging.token.addOnSuccessListener {token ->
            updateToken(context,token)

        }.addOnFailureListener {

        }

    }

    fun updateToken(context: Context, token: String) {
        val firebaseFirestore = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser.uid;

        firebaseFirestore.collection("Users").whereEqualTo("UserID", userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val document = documentSnapshot.documents[0]
                val hashMap = hashMapOf<String, Any>(
                    "Token" to token
                )
                firebaseFirestore.collection("Users").document(document.id).update(hashMap)
                    .addOnSuccessListener {

                    }
            }.addOnFailureListener {

            }
    }

}