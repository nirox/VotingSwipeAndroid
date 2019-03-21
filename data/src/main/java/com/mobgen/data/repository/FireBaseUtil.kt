package com.mobgen.data.repository

import com.google.firebase.database.DataSnapshot
import com.mobgen.data.entity.UserEntity

class FireBaseUtil {
    companion object {
        fun createUser(dataSnapshot: DataSnapshot): UserEntity {
            return UserEntity(
                dataSnapshot.child(UserEntity.Attribute.NAME.aName).value as String,
                dataSnapshot.child(UserEntity.Attribute.PASSWORD.aName).value as String,
                dataSnapshot.child(UserEntity.Attribute.EMAIL.aName).value as String,
                dataSnapshot.child(UserEntity.Attribute.BIRTHDAY.aName).value as String,
                dataSnapshot.child(UserEntity.Attribute.DESCRIPTION.aName).value as String,
                (dataSnapshot.child(UserEntity.Attribute.LIKES.aName).value
                    ?: mapOf<String, String>()) as Map<String, String>,
                (dataSnapshot.child(UserEntity.Attribute.PHOTOS.aName).value
                    ?: mapOf<String, String>()) as Map<String, String>
            )
        }
    }
}