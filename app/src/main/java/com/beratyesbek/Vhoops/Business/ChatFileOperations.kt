package com.beratyesbek.Vhoops.Business

import android.content.Context
import android.net.Uri
import com.beratyesbek.Vhoops.Business.Concrete.ChatManager
import com.beratyesbek.Vhoops.Business.Concrete.UserManager
import com.beratyesbek.Vhoops.Core.Utilities.Control.CheckAndroidUriType
import com.beratyesbek.Vhoops.DataAccess.Concrete.ChatDal
import com.beratyesbek.Vhoops.DataAccess.Concrete.UserDal
import com.beratyesbek.Vhoops.Entities.Concrete.Chat
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth

class ChatFileOperations (val receiverId : String){
    companion object{

        private lateinit var receiverId : String

        fun checkUriExtension(uri: Uri?,context: Context,receiverId: String) {

            val type = CheckAndroidUriType.checkUriType(uri, context)
            if (type != null) {
                this.receiverId = receiverId
                println(10)

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
                println(30)
                sendMessage(iDataResult.data())
            }
        }

        private fun sendMessage(message: Any) {
            println(40)

            val senderId = FirebaseAuth.getInstance().currentUser.uid

            val chatDal = ChatDal()
            val userManager = UserManager(UserDal())
            val chatManager = ChatManager(chatDal, userManager)

            chatManager.add(Chat(senderId, receiverId, message, false, Timestamp.now())) {

            }

        }

    }
}