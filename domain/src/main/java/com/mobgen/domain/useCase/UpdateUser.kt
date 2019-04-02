package com.mobgen.domain.useCase

import com.mobgen.domain.UserRepository
import io.reactivex.Completable
import javax.inject.Inject

class UpdateUser @Inject constructor(private val userRepository: UserRepository) {
    fun execute(likes: List<String>): Completable = userRepository.updateUser(likes)
}