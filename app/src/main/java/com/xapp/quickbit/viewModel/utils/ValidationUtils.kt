package com.xapp.quickbit.viewModel.utils

object ValidationUtils {

    fun isUserNameFilled(username: String): Boolean {
        return username.isNotBlank()
    }

    fun isEmailFilled(email: String): Boolean {
        return email.isNotBlank()
    }

    fun isPasswordFilled(password: String): Boolean {
        return password.isNotBlank()
    }

    fun isConfirmPasswordFilled(confirmPassword: String): Boolean {
        return confirmPassword.isNotBlank()
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
