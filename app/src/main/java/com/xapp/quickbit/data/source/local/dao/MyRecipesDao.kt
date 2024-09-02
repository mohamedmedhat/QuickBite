package com.xapp.quickbit.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.xapp.quickbit.data.source.local.entity.MyRecipesEntity


@Dao
interface MyRecipesDao {

    @Insert
    suspend fun createRecipe(recipe: MyRecipesEntity)

    @Delete
    suspend fun deleteMeal(meal: MyRecipesEntity)

    @Query("SELECT * FROM my_recipes ORDER BY mealId ASC")
    fun getAllMyRecipes(): LiveData<List<MyRecipesEntity>>
}