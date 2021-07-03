package com.beratyesbek.vhoops.core

import com.google.firebase.auth.FirebaseAuth

class EmailVerification {


    companion object{
        private val firebaseAuth  = FirebaseAuth.getInstance()

        fun sendEmail(){

            firebaseAuth.currentUser?.sendEmailVerification()?.addOnSuccessListener {

            }?.addOnFailureListener {

            }
        }
        fun resetPassword(email :String){
            firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener {

            }.addOnFailureListener {

            }
        }
    }

}