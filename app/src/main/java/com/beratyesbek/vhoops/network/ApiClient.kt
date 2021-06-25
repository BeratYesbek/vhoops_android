package com.beratyesbek.vhoops.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class ApiClient {
    companion object{
        var retrofit :  Retrofit? = null;

         fun getClient() : Retrofit? {
             if (retrofit == null){
                 retrofit = Retrofit
                     .Builder()
                     .baseUrl("https://fcm.googleapis.com/fcm/")
                     .addConverterFactory(ScalarsConverterFactory.create())
                     .build()
             }
             return retrofit
         }
    }
}