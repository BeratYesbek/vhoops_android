package com.beratyesbek.Vhoops.Core.Permission

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class RecordAudioPermission {
    companion object{
        fun checkPermission(context :Context , activity: Activity) : Boolean{
            if(ActivityCompat.checkSelfPermission(context,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE),10)
                return false
            }else{
                return true
            }
        }
    }
}