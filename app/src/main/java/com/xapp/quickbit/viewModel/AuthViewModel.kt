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
import com.xapp.quickbit.data.source.local.dao.UserDao
import com.xapp.quickbit.data.source.local.database.AppDatabase
import com.xapp.quickbit.viewModel.utils.ValidationUtils

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val _user = MutableLiveData<FirebaseUser?>()
    private val _loginError = MutableLiveData<String?>()
    val registrationResult = MutableLiveData<String>()

    private val _registrationState = MutableLiveData<Boolean>()
    val registrationState: LiveData<Boolean> get() = _registrationState

    private val _loginState = MutableLiveData<Boolean>()
    val loginState: LiveData<Boolean> get() = _loginState

    val user: LiveData<FirebaseUser?> get() = _user
    val loginError: LiveData<String?> get() = _loginError

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

        registerUser(userName, email, password)
    }

    fun handleLoginAction(email: String, password: String) {
        if (!ValidationUtils.validateLoginInputs(email, password)) {
            _loginError.value = "Email and password are required"
            return
        }
        if (!ValidationUtils.isEmailValid(email)) {
            _loginError.value = "Invalid email format"
            return
        }
        if (!ValidationUtils.isPasswordValid(password)) {
            _loginError.value = "Password must be at least 6 characters"
            return
        }
        loginUser(email, password)
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
                } else {
                    _loginError.value = task.exception?.message
                }
            }
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _user.value = auth.currentUser
                } else {
                    _loginError.value = task.exception?.message
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
