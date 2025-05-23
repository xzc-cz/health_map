package com.example.healthmap.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthmap.model.UserDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserViewModel(application: Application) : AndroidViewModel(application) {
    // Firebase Firestore instance
    private val firestore = FirebaseFirestore.getInstance()

    // SharedFlow for login success state
    private val _loginSuccess = MutableSharedFlow<Boolean>()
    val loginSuccess: SharedFlow<Boolean> = _loginSuccess

    // StateFlow for registration result: SUCCESS, EXISTS, FAIL
    private val _registerResult = MutableStateFlow<String?>(null)
    val registerResult: StateFlow<String?> = _registerResult

    // SharedFlow to emit password reset result
    private val _resetResult = MutableSharedFlow<Boolean>()
    val resetResult: SharedFlow<Boolean> = _resetResult

    // StateFlow to hold current logged-in user data
    private val _currentUser = MutableStateFlow<UserDto?>(null)
    val currentUser: StateFlow<UserDto?> = _currentUser

    // SharedFlow for login error messages
    private val _loginError = MutableSharedFlow<String?>()
    val loginError: SharedFlow<String?> = _loginError

    // Function to log in user using FirebaseAuth
    fun login(email: String, password: String) = viewModelScope.launch {
        try {
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .await()

            _loginSuccess.emit(true)
            _loginError.emit(null) // Clear previous error
        } catch (e: Exception) {
            val errorMessage = e.localizedMessage ?: "Login failed due to an unknown error."
            Log.e("Login", "Login failed: $errorMessage")
            _loginSuccess.emit(false)
            _loginError.emit("Login failed: $errorMessage")
        }
    }

    // Function to register a new user using FirebaseAuth + Firestore
    fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        gender: String,
    ) = viewModelScope.launch {
        try {
            val authResult = FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .await()

            val userId = authResult.user?.uid ?: ""
            val user = UserDto(
                email = email,
                firstName = firstName,
                lastName = lastName,
                gender = gender
            )

            firestore.collection("users")
                .document(userId)
                .set(user)
                .await()

            _registerResult.value = "SUCCESS"

        } catch (e: Exception) {
            Log.e("Register", "Error: ${e.message}")
            if (e.message?.contains("email address is already in use") == true) {
                _registerResult.value = "EXISTS"
            } else {
                _registerResult.value = "FAIL"
            }
        }
    }

    // Reset registration result back to null
    fun resetRegisterResult() {
        _registerResult.value = null
    }

    // Trigger password reset email via FirebaseAuth
    fun resetPassword(email: String) = viewModelScope.launch {
        try {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).await()
            _resetResult.emit(true)
        } catch (e: Exception) {
            Log.e("ResetPassword", "Error: ${e.message}")
            _resetResult.emit(false)
        }
    }

    // Load current user profile from Firestore
    fun loadCurrentUserFromFirebase() = viewModelScope.launch {
        try {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            val snapshot = firestore.collection("users").document(userId).get().await()
            val user = snapshot.toObject(UserDto::class.java)
            _currentUser.value = user
        } catch (e: Exception) {
            Log.e("Profile", "Failed to load user: ${e.message}")
        }
    }

    // Update user profile fields in Firestore
    fun updateProfile(firstName: String, lastName: String, gender: String) = viewModelScope.launch {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch

        try {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .update(
                    mapOf(
                        "firstName" to firstName,
                        "lastName" to lastName,
                        "gender" to gender
                    )
                )
                .await()

            loadCurrentUserFromFirebase()

        } catch (e: Exception) {
            Log.e("EditProfile", "Update failed", e)
        }
    }
}