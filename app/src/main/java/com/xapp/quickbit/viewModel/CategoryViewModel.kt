package com.xapp.quickbit.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xapp.quickbit.data.repository.HomeRecipesRepository
import com.xapp.quickbit.data.source.remote.model.Category
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {

    private val _recipes = MutableLiveData<List<Category>>()
    val recipes: LiveData<List<Category>> get() = _recipes

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val categoryRepository = HomeRecipesRepository()

    init {
        fetchRecipes()
    }

    private fun fetchRecipes() {
        viewModelScope.launch {
            try {
                val recipeList = categoryRepository.getAllCategories()
                _recipes.postValue(recipeList.categories)
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }
}
