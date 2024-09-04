package com.xapp.quickbit.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xapp.quickbit.data.repository.HomeRecipesRepository
import com.xapp.quickbit.data.source.remote.model.Meal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryDetailsViewModel @Inject constructor(private val categoryRepository: HomeRecipesRepository) :
    ViewModel() {

    private val _categoryDetails = MutableLiveData<List<Meal>>()
    val categoryDetails: LiveData<List<Meal>> get() = _categoryDetails

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error


    fun getRecipesByCategory(category: String) {
        viewModelScope.launch {
            try {
                val details = categoryRepository.getMealsByCategory(category)
                _categoryDetails.postValue(details.meals)
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }
}


