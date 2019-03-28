package com.mobgen.domain

import com.mobgen.domain.model.User
import io.reactivex.Completable
import io.reactivex.Single

interface UserRepository {
    fun auth(email: String, password: String): Single<User>
    fun register(user: User, password: String): Completable
    fun getAuthUser(): Single<User>
    fun getUsers() : Single<List<User>>
}