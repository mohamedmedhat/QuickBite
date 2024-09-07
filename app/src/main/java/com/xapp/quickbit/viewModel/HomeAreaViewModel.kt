package com.xapp.quickbit.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xapp.quickbit.data.repository.HomeRecipesRepository
import com.xapp.quickbit.data.source.remote.model.Area
import com.xapp.quickbit.data.source.remote.model.Meal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeAreaViewModel @Inject constructor(private val homeRecipesRepository: HomeRecipesRepository) :
    ViewModel() {

    private val _area = MutableLiveData<List<Area>>()
    val area: LiveData<List<Area>> get() = _area

    private val _recipe = MutableLiveData<List<Meal>>()
    val recipe: LiveData<List<Meal>> get() = _recipe

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    init {
        getAllArea()
        getRecipesByArea()
    }

    fun getAllArea() {
        viewModelScope.launch {
            try {
                val response = homeRecipesRepository.getAllArea()
                if (response.meals.isEmpty()) {
                    _error.postValue("No recipes found")
                } else {
                    _area.postValue(response.meals)
                }
            } catch (e: Exception) {
                _error.postValue("Error fetching recipes: ${e.message}")
                Log.e("HomeAreaViewModel", e.message.toString())
            }
        }
    }

    fun getRecipesByArea(area: String = "British") {
        viewModelScope.launch {
            try {
                val recipe = homeRecipesRepository.getMealsByArea(area)
                if (recipe.meals.isEmpty()) {
                    _error.postValue("No recipes found for the area: $area")
                } else {
                    _recipe.postValue(recipe.meals)
                }
            } catch (e: Exception) {
                _error.postValue("Error fetching recipes: ${e.message}")
                Log.e("HomeAreaViewModel", e.message.toString())
            }
        }
    }

}