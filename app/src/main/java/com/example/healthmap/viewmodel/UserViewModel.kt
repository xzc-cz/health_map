package com.example.healthmap.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthmap.model.UserDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val firestore = FirebaseFirestore.getInstance()

    private val _loginSuccess = MutableSharedFlow<Boolean>()
    val loginSuccess: SharedFlow<Boolean> = _loginSuccess

    private val _registerResult = MutableStateFlow<String?>(null)
    val registerResult: StateFlow<String?> = _registerResult

    private val _resetResult = MutableSharedFlow<Boolean>()
    val resetResult: SharedFlow<Boolean> = _resetResult

    //private val _registerResult = MutableStateFlow<String?>(null)

    fun login(
        email: String,
        password: String,
    ) = viewModelScope.launch {
        try {
            // 使用 Firebase Authentication 登录
            com.google.firebase.auth.FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .await()

            // 登录成功，发送 true
            _loginSuccess.emit(true)

        } catch (e: Exception) {
            // 登录失败，记录错误并发送 false
            Log.e("Login", "FirebaseAuth login error: ${e.message}")
            _loginSuccess.emit(false)
        }
    }

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

    fun resetRegisterResult() {
        _registerResult.value = null
    }

    fun resetPassword(email: String) = viewModelScope.launch {
        try {
            com.google.firebase.auth.FirebaseAuth.getInstance()
                .sendPasswordResetEmail(email)
                .await()
            _resetResult.emit(true)
        } catch (e: Exception) {
            Log.e("ResetPassword", "Error: ${e.message}")
            _resetResult.emit(false)
        }
    }

}