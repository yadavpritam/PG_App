package com.example.pgapp.data.model.pgmodel

data class UserModel(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val profileImage: String = "",
    val savedPgs: List<String> = emptyList()
)