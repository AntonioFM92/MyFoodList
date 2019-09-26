package com.antoniofm.myfoodlist.api

import com.google.gson.annotations.SerializedName

data class FoodResponse (
    @SerializedName("foodId") val userId : Long,
    @SerializedName("name") val title : String,
    @SerializedName("brand") val body : String
)