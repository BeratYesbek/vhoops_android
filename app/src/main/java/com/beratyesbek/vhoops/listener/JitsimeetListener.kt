package com.beratyesbek.vhoops.listener

import com.beratyesbek.vhoops.entities.concrete.User

interface JitsimeetListener {

    fun initiateVideoMeeting(user :User)

    fun initiateAudioMeeting(user :User)

}