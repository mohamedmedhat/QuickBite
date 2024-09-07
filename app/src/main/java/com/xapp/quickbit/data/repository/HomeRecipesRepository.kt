package com.xapp.quickbit.data.repository

import com.xapp.quickbit.data.source.remote.model.response.MealDetailResponse
import com.xapp.quickbit.data.source.remote.network.restapi.HomeRecipesService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRecipesRepository @Inject constructor(
    private val homeRecipesService: HomeRecipesService
) {

    suspend fun getAllCategories() = homeRecipesService.findAllCategories()

    suspend fun getMealsByCategory(category: String) =
        homeRecipesService.findMealsByCategory(category)

    suspend fun getMealsByArea(area: String) = homeRecipesService.findMealsByArea(area)

    suspend fun getRandomMeal(): MealDetailResponse {
        return homeRecipesService.findRandomMeal()
    }

    suspend fun getAllArea() = homeRecipesService.findAllArea()

    suspend fun getMealsByFirstLetter(firstLetter: String) =
        homeRecipesService.findMealsByFirstLetter(firstLetter)

    suspend fun getMealById(id: String) = homeRecipesService.findMealById(id)

    suspend fun getMealByName(name: String) = homeRecipesService.findMealByName(name)
}
