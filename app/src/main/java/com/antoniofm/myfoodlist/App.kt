package com.antoniofm.myfoodlist

import android.app.Application
import com.antoniofm.myfoodlist.api.APIService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App: Application(){

    companion object {
        @JvmStatic
        var api: APIService? = null
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://nutrifoodlist.herokuapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override fun onCreate() {
        super.onCreate()

        api = getRetrofit().create(APIService::class.java)
    }
}