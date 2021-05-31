package com.beratyesbek.vhoops.Business

import android.content.Context
import android.net.Uri
import com.beratyesbek.vhoops.Business.Concrete.ChatManager
import com.beratyesbek.vhoops.Business.Concrete.UserManager
import com.beratyesbek.vhoops.Core.Utilities.Control.CheckAndroidUriType
import com.beratyesbek.vhoops.DataAccess.Concrete.ChatDal
import com.beratyesbek.vhoops.DataAccess.Concrete.UserDal
import com.beratyesbek.vhoops.entities.concrete.Chat
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth

class ChatFileOperations(val receiverId: String) {
    companion object {

        private lateinit var receiverId: String

        fun checkUriExtension(uri: Uri?, context: Context, receiverId: String) {

            val type = CheckAndroidUriType.checkUriType(uri, context)
            if (type != null) {
                this.receiverId = receiverId

                uploadFile(uri as Any, type)


            }
        }


        private fun uploadFile(message: Any, type: String) {
            val senderId = FirebaseAuth.getInstance().currentUser.uid
            val chatManager = ChatManager(ChatDal(), UserManager(UserDal()))
            chatManager.uploadFile(message as Uri, type) { iDataResult ->
                println(20)

                getFiles(iDataResult.data())
            }

        }

        private fun getFiles(path: String) {
            val chatManager = ChatManager(ChatDal(), UserManager(UserDal()))
            chatManager.getFile(path) { iDataResult ->
                sendMessage(iDataResult.data())
            }
        }

        private fun sendMessage(message: Any) {

            val senderId = FirebaseAuth.getInstance().currentUser.uid

            val chatDal = ChatDal()
            val userManager = UserManager(UserDal())
            val chatManager = ChatManager(chatDal, userManager)

            chatManager.add(Chat(senderId, receiverId!!, message, false, Timestamp.now())) {

            }

        }

    }
}