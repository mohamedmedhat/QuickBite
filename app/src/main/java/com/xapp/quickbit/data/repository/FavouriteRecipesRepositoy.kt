package com.xapp.quickbit.data.repository

import androidx.lifecycle.LiveData
import com.xapp.quickbit.data.source.local.dao.FavouriteDao
import com.xapp.quickbit.data.source.local.entity.MealInformationEntity

class FavouriteRecipesRepository(private val mealDao: FavouriteDao) {
    val mealList: LiveData<List<MealInformationEntity>> = mealDao.getAllSavedMeals()

    suspend fun insertFavoriteMeal(meal: MealInformationEntity) {
        mealDao.insertFavorite(meal)
    }

    suspend fun getMealById(mealId: String): MealInformationEntity {
        return mealDao.getMealById(mealId)
    }

    suspend fun deleteMealById(mealId: String) {
        mealDao.deleteMealById(mealId)
    }

    suspend fun deleteMeal(meal: MealInformationEntity) = mealDao.deleteMeal(meal)

}