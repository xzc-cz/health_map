package com.example.healthmap.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthmap.model.UserDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val firestore = FirebaseFirestore.getInstance()

    private val _loginSuccess = MutableSharedFlow<Boolean>()
    val loginSuccess: SharedFlow<Boolean> = _loginSuccess

    private val _registerResult = MutableSharedFlow<String?>()
    val registerResult: SharedFlow<String?> = _registerResult

    private val _resetResult = MutableSharedFlow<Boolean>()
    val resetResult: SharedFlow<Boolean> = _resetResult

    fun login(
        email: String,
        password: String,
    ) = viewModelScope.launch {
        val snapshot = firestore.collection("users")
            .whereEqualTo("email", email)
            .whereEqualTo("password", password)
            .get()
            .await()
        val success = snapshot.documents.isNotEmpty()
        _loginSuccess.emit(success)
    }

    fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        gender: String,
    ) = viewModelScope.launch {
        val user = UserDto(
            email,
            password,
            firstName,
            lastName,
            gender
        )

        val exists = firestore.collection("users")
            .whereEqualTo("email", email)
            .get()
            .await()
            .documents
            .isNotEmpty()

        if (exists) {
            _registerResult.emit(null)
        } else {
            val ref = firestore.collection("users")
                .add(user)
                .await()
            _registerResult.emit(ref.id)
        }
    }

    fun resetPassword(email: String, newPassword: String) = viewModelScope.launch {
        val snapshot = firestore.collection("users")
            .whereEqualTo("email", email)
            .get()
            .await()

        if (snapshot.isEmpty) {
            _resetResult.emit(false)
            return@launch
        }

        val doc = snapshot.documents.first()
        val id = doc.id
        val user = doc.toObject(UserDto::class.java)
        if (user == null) {
            _resetResult.emit(false)
            return@launch
        }

        user.password = newPassword
        try {
            firestore.collection("users")
                .document(id)
                .set(user)
                .await()
            _resetResult.emit(true)
        } catch (e: Exception) {
            _resetResult.emit(false)
        }
    }
}