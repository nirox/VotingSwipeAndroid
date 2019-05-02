package com.mobgen.domain.useCase

import com.mobgen.domain.UserRepository
import com.mobgen.domain.model.User
import io.reactivex.Single
import javax.inject.Inject

class GetUsers @Inject constructor(private val userRepository: UserRepository) {
    fun execute(): Single<List<User>> = userRepository.getUsers()
}