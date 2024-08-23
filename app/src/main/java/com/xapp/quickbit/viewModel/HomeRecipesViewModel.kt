package com.xapp.quickbit.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xapp.quickbit.data.repository.HomeRecipesRepository
import com.xapp.quickbit.data.source.remote.model.MealDetail
import kotlinx.coroutines.launch

class HomeRecipesViewModel : ViewModel() {

    private val _mealRecipes = MutableLiveData<List<MealDetail>>()
    val mealRecipes: LiveData<List<MealDetail>> get() = _mealRecipes

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val categoryRepository = HomeRecipesRepository()

    init {
        fetchRecipesByFirstLetter()
    }

    fun fetchRecipesByFirstLetter(letter: String = "A") {
        viewModelScope.launch {
            try {
                val data = categoryRepository.getMealsByFirstLetter(letter)
                _mealRecipes.postValue(data.meals ?: emptyList())
            } catch (e: Exception) {
                _error.postValue(e.message ?: "An error occurred when fetchRecipesByFirstLetter")
            }
        }
    }
}

