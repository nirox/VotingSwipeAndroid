package com.mobgen.domain.model

data class User(
    val name: String,
    val email: String,
    val birthDay: String,
    val description: String,
    val likes: List<String>,
    val photos: List<String>
)