package com.xapp.quickbit.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.xapp.quickbit.data.source.local.entity.MealInformationEntity

@Dao
interface FavouriteDao {
    @Insert
    suspend fun insertFavorite(meal: MealInformationEntity)

    @Update
    suspend fun updateFavorite(meal: MealInformationEntity)

    @Query("SELECT * FROM meal_information ORDER BY mealId ASC")
    fun getAllSavedMeals(): LiveData<List<MealInformationEntity>>

    @Query("SELECT * FROM meal_information WHERE mealId = :id")
    suspend fun getMealById(id: String): MealInformationEntity

    @Query("DELETE FROM meal_information WHERE mealId = :id")
    suspend fun deleteMealById(id: String)

    @Delete
    suspend fun deleteMeal(meal: MealInformationEntity)
}
