package com.xapp.quickbit.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.xapp.quickbit.data.repository.FavouriteRecipesRepository
import com.xapp.quickbit.data.source.local.entity.MealInformationEntity
import kotlinx.coroutines.launch

class FavouriteRecipesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FavouriteRecipesRepository = FavouriteRecipesRepository(application)

    val allFavoriteMeals: LiveData<List<MealInformationEntity>> = repository.getAllMeals()

    fun insertFavoriteMeal(meal: MealInformationEntity) {
        viewModelScope.launch {
            try {
                repository.insertFavoriteMeal(meal)
            } catch (e: Exception) {
                Log.e("Insert To Favourite failed", "$e")
            }
        }
    }

    fun getMealById(mealId: String, callback: (MealInformationEntity?) -> Unit) {
        viewModelScope.launch {
            try {
                val meal = repository.getMealById(mealId)
                callback(meal)
            } catch (e: Exception) {
                Log.e("getMealById from Favourite failed", "$e")
            }
        }
    }

    fun deleteMealById(mealId: String) {
        viewModelScope.launch {
            try {
                repository.deleteMealById(mealId)
            } catch (e: Exception) {
                Log.e("deleteMealById from Favourite failed", "$e")
            }
        }
    }

    fun deleteMeal(meal: MealInformationEntity) {
        viewModelScope.launch {
            try {
                repository.deleteMeal(meal)
            } catch (e: Exception) {
                Log.e("deleteMeal from Favourite failed", "$e")
            }
        }
    }
}

