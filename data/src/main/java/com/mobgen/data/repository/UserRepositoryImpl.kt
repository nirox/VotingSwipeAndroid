package com.mobgen.data.repository

import com.mobgen.data.entity.UserEntity
import com.mobgen.data.mapper.UserDataMapper
import com.mobgen.data.mapper.UserDataMapperReverse
import com.mobgen.data.repository.dataSource.FirebaseDataResource
import com.mobgen.domain.UserRepository
import com.mobgen.domain.check
import com.mobgen.domain.model.User
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataMapperReverse: UserDataMapperReverse,
    private val userDataMapper: UserDataMapper,
    private val firebaseDataResource: FirebaseDataResource
) : UserRepository {

    companion object {
        private var userCache: UserEntity? = null
    }

    override fun auth(email: String, password: String): Single<User> =
        firebaseDataResource.auth(email, password).map {
            it.let { entity ->
                userCache = entity
                userDataMapper.map(entity)
            }
        }

    override fun register(user: User, password: String): Completable =
        firebaseDataResource.register(userDataMapperReverse.apply { this.password = password }.map(user))

    override fun getAuthUser(): Single<User> {
        return Single.create { emitter ->
            userCache.check(
                ifNull = {
                    emitter.onError(Throwable("User not authenticated"))
                },
                ifNotNull = { user ->
                    emitter.onSuccess(userDataMapper.map(user))
                }
            )
        }
    }
    override fun getUsers(): Single<List<User>> =
        firebaseDataResource.getUsers(userCache?.email, userCache?.likes ?: mapOf()).map { userDataMapper.map(it) }

    override fun updateUser(likes: List<String>): Completable =
        firebaseDataResource.updateUser(userCache?.apply {
            this.likes = likes.mapIndexed { index: Int, string: String -> index.toString() to string }.toMap()
        })

}