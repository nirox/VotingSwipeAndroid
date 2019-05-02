package com.mobgen.data.repository

import com.google.firebase.database.DataSnapshot
import com.mobgen.data.entity.UserEntity

class FireBaseUtil {
    companion object {
        fun createUser(dataSnapshot: DataSnapshot): UserEntity {
            val likes =
                if (dataSnapshot.child(UserEntity.Attribute.LIKES.aName).value != null) (dataSnapshot.child(UserEntity.Attribute.LIKES.aName).value as ArrayList<String>)
                    .mapIndexed { index: Int, string: String -> index.toString() to string }.toMap() else mapOf()
            val photos =
                if (dataSnapshot.child(UserEntity.Attribute.PHOTOS.aName).value != null)
                    (dataSnapshot.child(UserEntity.Attribute.PHOTOS.aName).value as ArrayList<String>)
                        .mapIndexed { index: Int, string: String -> index.toString() to string }.toMap()
                else
                    mapOf()
            return UserEntity(
                dataSnapshot.key ?: "",
                dataSnapshot.child(UserEntity.Attribute.NAME.aName).value as String,
                dataSnapshot.child(UserEntity.Attribute.PASSWORD.aName).value as String,
                dataSnapshot.child(UserEntity.Attribute.EMAIL.aName).value as String,
                dataSnapshot.child(UserEntity.Attribute.BIRTHDAY.aName).value as String,
                dataSnapshot.child(UserEntity.Attribute.DESCRIPTION.aName).value as String,
                likes,
                photos
            )
        }
    }
}