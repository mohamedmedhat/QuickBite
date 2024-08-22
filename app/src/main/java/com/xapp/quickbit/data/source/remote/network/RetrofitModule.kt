package com.xapp.quickbit.data.source.remote.network

import com.xapp.quickbit.data.source.remote.network.restapi.FoodService
import com.xapp.quickbit.data.source.remote.network.restapi.RecipeService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitModule {
    const val END_POINT = ""
    private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val apiRecipe: FoodService = retrofit.create(FoodService::class.java)
}