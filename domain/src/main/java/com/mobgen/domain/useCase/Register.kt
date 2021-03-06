package com.mobgen.domain.useCase

import com.mobgen.domain.UserRepository
import com.mobgen.domain.model.User
import io.reactivex.Completable
import javax.inject.Inject

class Register @Inject constructor(private val userRepository: UserRepository) {
    fun execute(user: User, password: String): Completable = userRepository.register(user, password)
}