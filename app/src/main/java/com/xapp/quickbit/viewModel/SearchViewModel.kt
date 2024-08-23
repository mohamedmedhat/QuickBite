package com.xapp.quickbit.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xapp.quickbit.data.repository.HomeRecipesRepository
import com.xapp.quickbit.data.source.remote.model.MealDetail
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val _searchRecipes = MutableLiveData<List<MealDetail>>()
    val searchRecipes: LiveData<List<MealDetail>> get() = _searchRecipes

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val categoryRepository = HomeRecipesRepository()

    fun fetchRecipesByName(name: String) {
        viewModelScope.launch {
            try {
                val response = categoryRepository.getMealByName(name)
                _searchRecipes.postValue(response.meals)
            } catch (e: Exception) {
                _error.postValue(e.message ?: "An error occurred while fetching recipes")
            }
        }
    }
}