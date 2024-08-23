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
    fun insertFavorite(meal: MealInformationEntity)

    @Update
    fun updateFavorite(meal: MealInformationEntity)

    @Query("SELECT * FROM meal_information order by mealId asc")
    fun getAllSavedMeals(): LiveData<List<MealInformationEntity>>

    @Query("SELECT * FROM meal_information WHERE mealId =:id")
    fun getMealById(id: String): MealInformationEntity

    @Query("DELETE FROM meal_information WHERE mealId =:id")
    fun deleteMealById(id: String)

    @Delete
    fun deleteMeal(meal: MealInformationEntity)
}