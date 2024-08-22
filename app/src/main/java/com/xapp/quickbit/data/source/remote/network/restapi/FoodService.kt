package com.xapp.quickbit.data.source.remote.network.restapi

import com.xapp.quickbit.data.source.remote.model.MealsResponse
import com.xapp.quickbit.data.source.remote.model.RandomMealResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodService {


    @GET("filter.php?")
    fun getMealsByCategory(@Query("i") category:String): Call<MealsResponse>

    @GET("random.php")
    fun getRandomMeal(): Call<RandomMealResponse>

    @GET("lookup.php?")
    fun getMealById(@Query("i") id:String): Call<RandomMealResponse>

    @GET("search.php?")
    fun getMealByName(@Query("s") s:String): Call<RandomMealResponse>
}
