package com.mobgen.data.repository

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
    override fun getUsers(): Single<List<User>> {
        //TODO
        return Single.create { emitter -> emitter.onSuccess(listOf()) }
    }

    companion object {
        private var userCache: User? = null
    }

    override fun auth(email: String, password: String): Single<User> =
        firebaseDataResource.auth(email, password).map { userDataMapper.map(it).also { user -> userCache = user } }

    override fun register(user: User, password: String): Completable =
        firebaseDataResource.register(userDataMapperReverse.apply { this.password = password }.map(user))

    override fun getAuthUser(): Single<User> {
        return Single.create { emitter ->
            userCache.check(
                ifNull = {
                    emitter.onError(Throwable("User not authenticated"))
                },
                ifNotNull = { user ->
                    emitter.onSuccess(user)
                }
            )
        }
    }

    override fun update(user: User): Completable {
        //TODO
        return Completable.create { emitter -> emitter.onComplete() }
    }
}