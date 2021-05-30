package com.beratyesbek.vhoops.Core.Permission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class DocumentPermission {
    companion object{
        fun selectFile(context: Context, activity: Activity)  {

            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),3)
            } else {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.setType("*/*")
                activity.startActivityForResult(intent,4)
            }
        }
    }
}