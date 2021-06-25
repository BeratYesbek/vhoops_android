package com.beratyesbek.vhoops.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface ApiService {

    @POST("send")
    fun sendRemoteMessage(
        @HeaderMap headers : HashMap<String,String>,
        @Body remoteBody : String
    ) : Call<String>

}