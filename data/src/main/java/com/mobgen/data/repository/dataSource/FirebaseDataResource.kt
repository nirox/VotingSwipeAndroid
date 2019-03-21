package com.mobgen.data.repository.dataSource

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobgen.data.entity.UserEntity
import com.mobgen.data.repository.FireBaseUtil
import com.mobgen.domain.check
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class FirebaseDataResource @Inject constructor() {
    private val database = FirebaseDatabase.getInstance().reference

    companion object {
        const val USER_CHILD = "users"
    }

    fun auth(email: String, password: String): Single<UserEntity> {
        return Single.create { emitter ->
            getUser(
                email,
                onSuccess = { dataSnapshot ->
                    if (dataSnapshot.child(UserEntity.Attribute.PASSWORD.aName).value as String == password) {
                        emitter.onSuccess(FireBaseUtil.createUser(dataSnapshot))
                    } else {
                        emitter.onError(Throwable("Wrong email or password"))
                    }
                },
                onError = {
                    emitter.onError(it)
                })
        }
    }


    fun register(user: UserEntity): Completable {
        return Completable.create { emitter ->
            getUser(
                user.email,
                onSuccess = {
                    emitter.onError(Throwable("User already exists"))
                },
                onError = {
                    database.child(USER_CHILD).push().key.check(
                        ifNotNull = { key ->
                            database.child(USER_CHILD).updateChildren(HashMap<String, Any>().apply {
                                this["/$key/"] = user.toMap()
                            })
                        },
                        ifNull = {
                            emitter.onError(Throwable("Couldn't get push key for users"))
                        })
                })
        }

    }

    private fun getUser(
        email: String,
        onSuccess: (data: DataSnapshot) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        database.child(USER_CHILD)
            .orderByChild(UserEntity.Attribute.EMAIL.aName)
            .equalTo(email)
            .limitToFirst(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    onError(databaseError.toException())
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.childrenCount == 1L) {
                        onSuccess(dataSnapshot.children.first())
                    } else {
                        onError(Throwable("Wrong email or password"))
                    }
                }

            })
    }

}