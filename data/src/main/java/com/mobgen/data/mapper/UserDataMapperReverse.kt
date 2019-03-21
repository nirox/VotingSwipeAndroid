package com.mobgen.data.mapper

import com.mobgen.data.entity.UserEntity
import com.mobgen.domain.model.User
import javax.inject.Inject

class UserDataMapperReverse @Inject constructor() : DataMapper<User, UserEntity> {
    var password: String = ""
    override fun map(value: User): UserEntity {
        return UserEntity(
            value.name,
            password,
            value.email,
            value.birthDay,
            value.description,
            value.likes.mapIndexed { index: Int, string: String -> index.toString() to string }.toMap(),
            value.photos.mapIndexed { index: Int, string: String -> index.toString() to string }.toMap()
        )
    }
}