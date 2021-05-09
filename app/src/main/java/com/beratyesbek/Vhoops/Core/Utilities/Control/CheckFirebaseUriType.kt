package com.beratyesbek.Vhoops.Core.Utilities.Control

import com.beratyesbek.Vhoops.Core.DataAccess.Constants.ExtensionConstants

class CheckFirebaseUriType {

    companion object{

        // that's can just check firebase uri type
        // that's  my custom algorithm

        fun checkUriType(uri :String) : String? {
            val checkType = uri.substring(uri.lastIndexOf("--."),uri.lastIndexOf("--?alt"))
            val type = checkType.substringAfter(".")

            if(type.equals("video")){
                return ExtensionConstants.VIDEO
            }
            else if(type.equals("image")){
                return ExtensionConstants.IMAGE
            }
            else if(type.equals("document")){
                return ExtensionConstants.DOCUMENT
            }
            return null
        }

    }
}