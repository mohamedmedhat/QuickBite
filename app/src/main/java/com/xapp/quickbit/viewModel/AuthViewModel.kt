package com.xapp.quickbit.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.xapp.quickbit.viewModel.utils.ValidationUtils

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val _user = MutableLiveData<FirebaseUser?>()
    private val _registrationState = MutableLiveData<Boolean>()
    private val _loginState = MutableLiveData<Boolean>()
    val registerState: LiveData<Boolean> get() = _registrationState
    val loginState: LiveData<Boolean> get() = _loginState
    val user: LiveData<FirebaseUser?> get() = _user

    val loginError = MutableLiveData<Map<String, String?>>()
    val registrationResult = MutableLiveData<Map<String, String?>>()

    private val loginWithGoogleError = MutableLiveData<String>()
    private var hasError = false

    fun handleRegisterAction(
        userName: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        val errors = mutableMapOf<String, String?>()

        if (!ValidationUtils.isUserNameFilled(userName)) {
            errors["userName"] = "Username is required"
            hasError = true
        }
        if (!ValidationUtils.isEmailFilled(email)) {
            errors["email"] = "Email is required"
            hasError = true
        }
        if (!ValidationUtils.isPasswordFilled(password)) {
            errors["password"] = "Password is required"
            hasError = true
        }
        if (!ValidationUtils.isConfirmPasswordFilled(confirmPassword)) {
            errors["confirmPassword"] = "Confirm password is required"
            hasError = true
        }
        if (!ValidationUtils.isEmailValid(email)) {
            errors["email"] = "Invalid email format"
            hasError = true
        }
        if (!ValidationUtils.isPasswordValid(password)) {
            errors["password"] = "Password must be at least 6 characters"
            hasError = true
        }
        if (!ValidationUtils.isPasswordMatching(password, confirmPassword)) {
            errors["confirmPassword"] = "Passwords do not match"
            hasError = true
        }
        if (!ValidationUtils.isNameValid(userName)) {
            errors["userName"] = "Name must be less than 25 characters"
            hasError = true
        }

        registrationResult.postValue(errors)

        if (!hasError) {
            registerUser(userName, email, password)
        }
    }

    fun handleLoginAction(email: String, password: String) {
        val errors = mutableMapOf<String, String?>()

        if (!ValidationUtils.isEmailFilled(email)) {
            errors["email"] = "Email is required"
            hasError = true
        }
        if (!ValidationUtils.isPasswordFilled(password)) {
            errors["password"] = "Password is required"
            hasError = true
        }
        if (!ValidationUtils.isEmailValid(email)) {
            errors["email"] = "Invalid email format"
            hasError = true
        }
        if (!ValidationUtils.isPasswordValid(password)) {
            errors["password"] = "Password must be at least 6 characters"
            hasError = true
        }
        loginError.postValue(errors)
        if (!hasError) {
            loginUser(email, password)
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { updateTask ->
                            _registrationState.value = updateTask.isSuccessful
                        }
                } else {
                    _registrationState.value = false
                }
            }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _loginState.value = task.isSuccessful
                if (task.isSuccessful) {
                    _user.value = auth.currentUser
                }
            }
    }


    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _user.value = auth.currentUser
                }
            }
    }

    fun checkIfLoggedIn(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("user_id", null) != null
    }

    fun saveUserToPreferences(user: FirebaseUser?, context: Context) {
        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("user_id", user?.uid)
            putString("email", user?.email)
            apply()
        }
    }


    fun signOut() {
        auth.signOut()
        _user.value = null
    }
}
