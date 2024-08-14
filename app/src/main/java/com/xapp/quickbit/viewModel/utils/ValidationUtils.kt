package com.xapp.quickbit.viewModel.utils

object ValidationUtils {

    fun validateInputs(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        return username.isNotBlank() && email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()
    }

    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }

    fun isPasswordMatching(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

    fun isNameValid(name: String): Boolean {
        return name.length <= 25
    }
}
