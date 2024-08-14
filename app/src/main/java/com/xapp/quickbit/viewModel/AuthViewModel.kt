package com.xapp.quickbit.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xapp.quickbit.data.source.local.dao.UserDao
import com.xapp.quickbit.data.source.local.database.AppDatabase
import com.xapp.quickbit.data.source.local.entity.User
import com.xapp.quickbit.viewModel.utils.ValidationUtils
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val authDao: UserDao = AppDatabase.getInstance(application).userDao()

    val registrationResult = MutableLiveData<String>()

    private fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            val newUser = User(name = name, email = email, password = password)
            authDao.addUser(newUser)
            registrationResult.postValue("You have signed up successfully")
        }
    }

    fun handleRegisterAction(
        userName: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        if (!ValidationUtils.validateInputs(userName, email, password, confirmPassword)) {
            registrationResult.postValue("All fields must be filled")
            return
        }
        if (!ValidationUtils.isEmailValid(email)) {
            registrationResult.postValue("Invalid email format")
            return
        }
        if (!ValidationUtils.isPasswordValid(password)) {
            registrationResult.postValue("Password must be at least 6 characters")
            return
        }
        if (!ValidationUtils.isPasswordMatching(password, confirmPassword)) {
            registrationResult.postValue("Passwords do not match")
            return
        }
        if (!ValidationUtils.isNameValid(userName)) {
            registrationResult.postValue("Name must be less than 25 characters")
            return
        }

        signUp(userName, email, password)
    }
}
