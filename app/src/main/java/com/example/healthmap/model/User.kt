package com.example.healthmap.model

data class UserDto(
    val email: String = "",
    var password: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val gender: String = ""
)