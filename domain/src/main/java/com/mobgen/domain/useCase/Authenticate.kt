package com.mobgen.domain.useCase

import com.mobgen.domain.UserRepository
import com.mobgen.domain.model.User
import io.reactivex.Single
import javax.inject.Inject

class Authenticate @Inject constructor(private val userRepository: UserRepository) {
    fun execute(email: String, password: String): Single<User> = userRepository.auth(email, password)
}