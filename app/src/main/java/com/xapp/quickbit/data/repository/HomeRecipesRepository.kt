package com.xapp.quickbit.data.repository

import com.xapp.quickbit.data.source.remote.network.RetrofitModule

class HomeRecipesRepository {
    private val homeApi = RetrofitModule.homeRecipeApi

    suspend fun getAllCategories() = homeApi.findAllCategories()

    suspend fun getMealsByCategory(category: String) = homeApi.findMealsByCategory(category)

    suspend fun getRandomMeal() = homeApi.findRandomMeal()

    suspend fun getMealsByFirstLetter(firstLetter: String) = homeApi.findMealsByFirstLetter(firstLetter)

    suspend fun getMealById(id: String) = homeApi.findMealById(id)

    suspend fun getMealByName(name: String) = homeApi.findMealByName(name)
}
