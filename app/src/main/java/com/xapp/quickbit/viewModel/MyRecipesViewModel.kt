package com.xapp.quickbit.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xapp.quickbit.data.repository.MyRecipesRepository
import com.xapp.quickbit.data.source.local.entity.MyRecipesEntity
import kotlinx.coroutines.launch

class MyRecipesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MyRecipesRepository = MyRecipesRepository(application)

    val allMyCreatedRecipes: LiveData<List<MyRecipesEntity>> = repository.getAllMyRecipes()

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun insertRecipe(meal: MyRecipesEntity) {
        viewModelScope.launch {
            try {
                repository.createNewRecipe(meal)
            } catch (e: Exception) {
                Log.e("Insert To My Recipes failed", "$e")
                _error.postValue(e.message)
            }
        }
    }

    fun deleteRecipe(recipe: MyRecipesEntity) {
        viewModelScope.launch {
            try {
                repository.deleteRecipe(recipe)
            } catch (e: Exception) {
                Log.e("Delete My Recipes failed", "$e")
                _error.postValue(e.message)
            }
        }
    }
}