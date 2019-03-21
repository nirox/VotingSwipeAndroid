package com.mobgen.domain.useCase

import com.mobgen.domain.UserRepository
import com.mobgen.domain.model.User
import io.reactivex.Single
import javax.inject.Inject

class GetAuthUser @Inject constructor(private val userRepository: UserRepository) {
    fun execute(): Single<User> = userRepository.getAuthUser()
}