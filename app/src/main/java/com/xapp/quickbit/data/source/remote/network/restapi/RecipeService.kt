package com.xapp.quickbit.data.source.remote.network.restapi

import com.xapp.quickbit.data.source.remote.model.Recipe
import com.xapp.quickbit.data.source.remote.network.RetrofitModule
import retrofit2.http.GET

interface RecipeService {
    @GET(RetrofitModule.END_POINT)
    suspend fun getAll(): ArrayList<Recipe>
}