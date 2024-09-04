package com.xapp.quickbit.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xapp.quickbit.data.repository.HomeRecipesRepository
import com.xapp.quickbit.data.source.remote.model.MealDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeRecipesViewModel @Inject constructor(
    private val categoryRepository: HomeRecipesRepository
) : ViewModel() {

    private val _mealRecipes = MutableLiveData<List<MealDetail>>()
    val mealRecipes: LiveData<List<MealDetail>> get() = _mealRecipes

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error


    init {
        fetchRecipesByFirstLetter()
    }

    fun fetchRecipesByFirstLetter(letter: String = "A") {
        viewModelScope.launch {
            try {
                val data = categoryRepository.getMealsByFirstLetter(letter)
                _mealRecipes.postValue(data.meals)
            } catch (e: Exception) {
                _error.postValue(e.message ?: "An error occurred when fetchRecipesByFirstLetter")
            }
        }
    }
}

