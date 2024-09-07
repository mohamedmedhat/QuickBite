package com.xapp.quickbit.viewModel

import android.util.Log
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
class GameViewModel @Inject constructor(private val homeRecipesRepository: HomeRecipesRepository) :
    ViewModel() {

    private val _randomMeal = MutableLiveData<List<MealDetail>>()
    val randomMeal: LiveData<List<MealDetail>> get() = _randomMeal

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun getRandomMeal() {
        viewModelScope.launch {
            try {
                val mealDetail = homeRecipesRepository.getRandomMeal()
                _randomMeal.postValue(mealDetail.meals)
            } catch (e: Exception) {
                _error.postValue(e.message)
                e.message?.let { Log.e(ERROR_TAG, it) }
            }
        }
    }

    companion object {
        private const val ERROR_TAG = "GameViewModel"
    }
}
