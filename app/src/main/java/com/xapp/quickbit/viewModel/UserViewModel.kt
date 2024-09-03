package com.xapp.quickbit.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xapp.quickbit.data.repository.UserRepository
import com.xapp.quickbit.data.source.local.entity.UserEntity
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository(application)

    private val _userData = MutableLiveData<UserEntity?>()
    val userData: LiveData<UserEntity?> get() = _userData

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun getUserData(email: String) {
        viewModelScope.launch {
            try {
                val data = userRepository.getUserDataByEmail(email)
                _userData.postValue(data)
            } catch (e: Exception) {
                _error.postValue(e.message)
                e.message?.let { Log.e("userViewModel", it) }
            }
        }
    }

    fun addUserData(user: UserEntity) {
        viewModelScope.launch {
            try {
                userRepository.createUser(user)
            } catch (e: Exception) {
                _error.postValue(e.message)
                e.message?.let { Log.e("userViewModel", it) }
            }
        }
    }

    fun updateUserData(user: UserEntity) {
        viewModelScope.launch {
            try {
                userRepository.updateUser(user)
            } catch (e: Exception) {
                _error.postValue(e.message)
                e.message?.let { Log.e("userViewModel", it) }
            }
        }
    }
}
