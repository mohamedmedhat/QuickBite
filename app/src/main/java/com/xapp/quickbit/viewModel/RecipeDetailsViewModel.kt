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
class RecipeDetailsViewModel @Inject constructor(private val homeRecipesRepository: HomeRecipesRepository) :
    ViewModel() {

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun getMealDetailsById(id: String, callback: (MealDetail?) -> Unit) {
        viewModelScope.launch {
            try {
                val details = homeRecipesRepository.getMealById(id)
                if (details.meals.isNotEmpty()) {
                    callback(details.meals[0])
                } else {
                    _error.postValue("This meal: $id is empty")
                    callback(null)
                }
            } catch (e: Exception) {
                _error.postValue(e.message)
                callback(null)
            }
        }
    }
}
