package com.mobgen.domain.model

data class User(
    val name: String,
    val email: String,
    val birthDay: String,
    val description: String,
    val likes: List<String> = listOf(),
    val photos: List<String> = listOf()
)