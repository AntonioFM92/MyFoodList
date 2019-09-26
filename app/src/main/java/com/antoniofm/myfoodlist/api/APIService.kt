package com.antoniofm.myfoodlist.api

import retrofit2.Call
import retrofit2.http.*

interface APIService {

    @GET
    fun getFoodList(@Url url:String): Call<List<FoodResponse>>

    @POST
    fun postFood(@Url url:String, @Body food:FoodResponse): Call<FoodResponse>

}