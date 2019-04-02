package com.mobgen.data.mapper

import com.mobgen.data.entity.UserEntity
import com.mobgen.domain.model.User
import javax.inject.Inject

class UserDataMapper @Inject constructor() : DataMapper<UserEntity, User> {
    override fun map(value: UserEntity): User =
        User(
            value.id,
            value.name,
            value.email,
            value.birthDay,
            value.description,
            value.likes.values.toList(),
            value.photos.values.toList()
        )
}