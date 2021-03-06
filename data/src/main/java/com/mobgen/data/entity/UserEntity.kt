package com.mobgen.data.entity

import com.google.firebase.database.Exclude


data class UserEntity(
    val id: String,
    val name: String,
    val password: String? = null,
    val email: String,
    val birthDay: String,
    val description: String,
    var likes: Map<String, String>,
    var photos: Map<String, String>
) {
    @Exclude
    fun toMap(): Map<String, Any> = HashMap<String, Any>().apply {
        this[Attribute.NAME.aName] = name
        password?.let { this[Attribute.PASSWORD.aName] = it }
        this[Attribute.EMAIL.aName] = email
        this[Attribute.BIRTHDAY.aName] = birthDay
        this[Attribute.DESCRIPTION.aName] = description
        this[Attribute.LIKES.aName] = likes
        this[Attribute.PHOTOS.aName] = photos
    }

    enum class Attribute(val aName: String) {
        NAME("name"),
        PASSWORD("password"),
        EMAIL("email"),
        BIRTHDAY("birthDate"),
        DESCRIPTION("description"),
        LIKES("likes"),
        PHOTOS("photos")
    }
}