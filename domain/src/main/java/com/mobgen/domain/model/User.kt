package com.mobgen.domain.model

data class User(
    val id: String = "",
    val name: String,
    val email: String,
    val birthDay: String,
    val description: String,
    var likes: List<String> = listOf(),
    var photos: List<String> = listOf()
)