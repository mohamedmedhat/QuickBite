package com.xapp.quickbit.data.repository

import androidx.lifecycle.LiveData

import com.xapp.quickbit.data.source.local.dao.MealsDao
import com.xapp.quickbit.data.source.local.entity.MealDB

class FavoriteRepositoriy(private val mealDao: MealsDao) {


    val mealList: LiveData<List<MealDB>> = mealDao.getAllSavedMeals()

    suspend fun insertFavoriteMeal(meal: MealDB) {
        mealDao.insertFavorite(meal)
    }

    suspend fun getMealById(mealId: String): MealDB {
        return mealDao.getMealById(mealId)
    }

    suspend fun deleteMealById(mealId: String) {
        mealDao.deleteMealById(mealId)
    }

    suspend fun deleteMeal(meal: MealDB) = mealDao.deleteMeal(meal)


}