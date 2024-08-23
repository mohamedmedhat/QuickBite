package com.xapp.quickbit.data.source.remote.network.restapi

import com.xapp.quickbit.data.source.local.entity.MealEntity
import com.xapp.quickbit.data.source.local.entity.response.MealEntityResponse
import com.xapp.quickbit.data.source.remote.model.Category
import com.xapp.quickbit.data.source.remote.model.MealDetail
import com.xapp.quickbit.data.source.remote.model.response.CategoryResponse
import com.xapp.quickbit.data.source.remote.model.response.MealDetailResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeRecipesService {
    @GET("categories.php")
    suspend fun findAllCategories(): CategoryResponse

    @GET("filter.php?")
    suspend fun findMealsByCategory(@Query("i") category: String): MealEntityResponse

    @GET("random.php")
    suspend fun findRandomMeal(): MealDetailResponse

    @GET("lookup.php?")
    suspend fun findMealById(@Query("i") id: String): MealDetailResponse

    @GET("search.php?")
    suspend fun findMealsByFirstLetter(@Query("f") letter: String): MealDetailResponse

    @GET("search.php?")
    suspend fun findMealByName(@Query("s") s: String): MealDetailResponse
}