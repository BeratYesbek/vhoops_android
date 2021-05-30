package com.beratyesbek.vhoops.Business

import android.content.Context
import android.net.Uri
import com.beratyesbek.vhoops.Business.Abstract.IGroupChatService
import com.beratyesbek.vhoops.Business.Concrete.ChatManager
import com.beratyesbek.vhoops.Business.Concrete.GroupChatManager
import com.beratyesbek.vhoops.Business.Concrete.UserManager
import com.beratyesbek.vhoops.Core.Utilities.Control.CheckAndroidUriType
import com.beratyesbek.vhoops.DataAccess.Concrete.ChatDal
import com.beratyesbek.vhoops.DataAccess.Concrete.UserDal
import com.beratyesbek.vhoops.entities.concrete.Chat
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Singleton


class ChatGroupFileOperations {

    companion object {


        fun checkUriExtension(uri: Uri?, context: Context) : String? {

            val type = CheckAndroidUriType.checkUriType(uri, context)
            if (type != null) {

                return type


            }
            return null
        }



    }
}