package com.beratyesbek.Vhoops.Core.Utilities.Control

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.beratyesbek.Vhoops.Core.DataAccess.Constants.ExtensionConstants


class CheckAndroidUriType {

    companion object{
        fun checkUriType(uri : Uri?,context : Context): String?{
            var realType : String? = null
            var mimeType: String? = null
            mimeType = if (ContentResolver.SCHEME_CONTENT == uri!!.scheme) {
                val cr: ContentResolver = context.getContentResolver()
                cr.getType(uri!!)
            } else {
                val fileExtension = MimeTypeMap.getFileExtensionFromUrl(
                    uri
                        .toString()
                )
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase()
                )
            }
            if(mimeType != null){
                 realType = mimeType.substring(0,mimeType.lastIndexOf("/"))

            }



            if(realType.equals("video")){
                return ExtensionConstants.VIDEO
            }
            else if(realType.equals("image")){
                return ExtensionConstants.IMAGE
            }
            else if(realType.equals("application")){
                return ExtensionConstants.DOCUMENT
            }
            return null
        }
    }
}