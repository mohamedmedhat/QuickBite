package com.xapp.quickbit.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.xapp.quickbit.data.source.local.entity.MealDB
@Dao
interface MealsDao {


    @Insert
    fun insertFavorite(meal: MealDB)

    @Update
    fun updateFavorite(meal: MealDB)

    @Query("SELECT * FROM meal_information order by mealId asc")
    fun getAllSavedMeals(): LiveData<List<MealDB>>

    @Query("SELECT * FROM meal_information WHERE mealId =:id")
    fun getMealById(id: String): MealDB

    @Query("DELETE FROM meal_information WHERE mealId =:id")
    fun deleteMealById(id: String)

    @Delete
    fun deleteMeal(meal: MealDB)


}
