package com.antoniofm.myfoodlist.api

import com.antoniofm.myfoodlist.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodRepository {

    private var apiService: APIService?=null

    init {
        apiService= App.api
    }

    fun getFoods(callback: RepositoryCallback<List<FoodResponse>?>?) {
        apiService?.getFoodList("food/?format=json")?.enqueue(object : Callback<List<FoodResponse>?> {
            override fun onFailure(call: Call<List<FoodResponse>?>, t: Throwable?) {
                callback?.onError("error happened")
            }

            override fun onResponse(call: Call<List<FoodResponse>?>, response: Response<List<FoodResponse>?>?) {
                if (response != null) {
                    if (response.isSuccessful) {
                        callback?.onSuccess(response.body())
                    } else {
                        callback?.onError(response.errorBody()!!.string())
                    }
                }
            }
        })
    }

    fun postFood(food:FoodResponse, callback: RepositoryCallback<FoodResponse>?) {
        apiService?.postFood("food/?format=json", food)?.enqueue(object : Callback<FoodResponse> {
            override fun onFailure(call: Call<FoodResponse>?, t: Throwable?) {
                callback?.onError("error happened")
            }

            override fun onResponse(call: Call<FoodResponse?>, response: Response<FoodResponse?>?) {
                if (response != null) {
                    if (response.isSuccessful) {
                        callback?.onSuccess(response.body())
                    } else {
                        if (response.code()==400){
                            callback?.onError("Ya existe este alimento")
                        }else{
                            callback?.onError(response.errorBody()!!.string())
                        }
                    }
                }
            }
        })
    }

    interface RepositoryCallback<in T> {
        fun onSuccess(t: T?)
        fun onError(error : String)
    }

}