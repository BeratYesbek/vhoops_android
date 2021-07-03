package com.beratyesbek.vhoops.business

import android.content.Context
import android.net.Uri
import com.beratyesbek.vhoops.business.concretes.GroupChatManager
import com.beratyesbek.vhoops.business.concretes.UserManager
import com.beratyesbek.vhoops.core.utilities.control.CheckAndroidUriType
import com.beratyesbek.vhoops.dataAccess.concretes.GroupChatDal
import com.beratyesbek.vhoops.dataAccess.concretes.UserDal
import com.beratyesbek.vhoops.entities.concrete.GroupChat
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth


class GroupChatFileOperations {

    companion object {

        var groupId = ""

        fun checkUriExtension(uri: Uri?, context: Context): String? {

            val type = CheckAndroidUriType.checkUriType(uri, context)
            if (type != null) {

                return type


            }
            return null
        }


         fun uploadFile(message: Any, type: String) {
            val groupChatManager = GroupChatManager(GroupChatDal(), UserManager(UserDal()))
            groupChatManager.uploadFile(message as Uri, type) { iDataResult ->
                if (iDataResult.success()) {
                    getFiles(iDataResult.data()!!)

                }
            }

        }

        private fun getFiles(path: String) {
            val groupChatManager = GroupChatManager(GroupChatDal(), UserManager(UserDal()))
            groupChatManager.getFile(path) { iDataResult ->
                sendMessage(iDataResult.data()!!)
            }
        }

        private fun sendMessage(message: Any) {

            val senderId = FirebaseAuth.getInstance().currentUser?.uid

            val groupChatManager = GroupChatManager(GroupChatDal(), UserManager(UserDal()))

            groupChatManager.add(GroupChat(senderId!!, groupId,message,false, Timestamp.now())){

            }

        }


    }
}