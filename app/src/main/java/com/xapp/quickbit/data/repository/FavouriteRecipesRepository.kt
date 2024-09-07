package com.xapp.quickbit.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.xapp.quickbit.data.source.local.database.AppDatabase
import com.xapp.quickbit.data.source.local.entity.MealInformationEntity

class FavouriteRecipesRepository(context: Context) {
    private val favouriteDao = AppDatabase.getInstance(context).favouriteDao()

    suspend fun insertFavoriteMeal(meal: MealInformationEntity) {
        favouriteDao.insertFavorite(meal)
    }

    fun getAllMeals(): LiveData<List<MealInformationEntity>> {
        return favouriteDao.getAllSavedMeals()
    }

    suspend fun getMealById(mealId: String): MealInformationEntity {
        return favouriteDao.getMealById(mealId)
    }

    suspend fun deleteMealById(mealId: String) {
        favouriteDao.deleteMealById(mealId)
    }

    suspend fun deleteMeal(meal: MealInformationEntity) {
        favouriteDao.deleteMeal(meal)
    }
}

