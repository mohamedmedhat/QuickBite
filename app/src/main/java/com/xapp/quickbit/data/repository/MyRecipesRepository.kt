package com.xapp.quickbit.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.xapp.quickbit.data.source.local.database.AppDatabase
import com.xapp.quickbit.data.source.local.entity.MyRecipesEntity

class MyRecipesRepository(context: Context) {
    private val myRecipeDao = AppDatabase.getInstance(context).myRecipesDao()

    suspend fun createNewRecipe(recipe: MyRecipesEntity) {
        return myRecipeDao.createRecipe(recipe)
    }

    fun getAllMyRecipes(): LiveData<List<MyRecipesEntity>> {
        return myRecipeDao.getAllMyRecipes()
    }

    suspend fun deleteRecipe(recipe: MyRecipesEntity){
        return myRecipeDao.deleteMeal(recipe)
    }
}