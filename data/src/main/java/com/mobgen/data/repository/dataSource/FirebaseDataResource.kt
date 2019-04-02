package com.mobgen.data.repository.dataSource

import android.net.Uri
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.mobgen.data.entity.UserEntity
import com.mobgen.data.repository.FireBaseUtil
import com.mobgen.domain.check
import io.reactivex.Completable
import io.reactivex.Single
import java.io.File
import javax.inject.Inject

class FirebaseDataResource @Inject constructor() {
    private val database = FirebaseDatabase.getInstance().reference
    private val storage = FirebaseStorage.getInstance().reference

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
                    emitter.onError(Throwable("Email already exists"))
                },
                onError = {
                    database.child(USER_CHILD).push().key.check(
                        ifNotNull = { key ->
                            if (user.photos.values.isNotEmpty()) {
                                uploadFile(
                                    user.photos.values.first(),
                                    key,
                                    onSuccess = {
                                        user.photos =
                                            user.photos.toMutableMap().apply { this["0"] = it }

                                        database.child(USER_CHILD).updateChildren(HashMap<String, Any>().apply {
                                            this["/$key/"] = user.toMap()
                                        }).addOnSuccessListener {
                                            emitter.onComplete()

                                        }.addOnFailureListener {
                                            emitter.onError(Throwable("User can not be registered"))
                                        }
                                    },
                                    onError = {
                                        emitter.onError(it)
                                    }
                                )
                            } else {
                                emitter.onError(Throwable("You should choose a photo"))
                            }
                        },
                        ifNull = {
                            emitter.onError(Throwable("Couldn't get push key for users"))
                        })

                })
        }
    }

    fun getUsers(emailAuthenticatedUser: String?, likes: Map<String, String>): Single<List<UserEntity>> {
        return Single.create { emitter ->
            emailAuthenticatedUser.check(
                ifNotNull = {
                    database.child(USER_CHILD)
                        .orderByChild(UserEntity.Attribute.EMAIL.aName)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(databaseError: DatabaseError) {
                                emitter.onError(databaseError.toException())
                            }

                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.childrenCount > 0L) {
                                    val users = dataSnapshot.children.map { FireBaseUtil.createUser(it) }
                                    emitter.onSuccess(users.filter {
                                        it.email != emailAuthenticatedUser && !likes.values.contains(it.id)
                                    })
                                } else {
                                    emitter.onError(Throwable("Wrong email or password"))
                                }
                            }

                        })
                },
                ifNull = {
                    emitter.onError(Throwable("User did not authenticate"))
                }
            )

        }
    }

    fun updateUser(user: UserEntity?): Completable {

        return Completable.create { emitter ->
            user.check(
                ifNull = {
                    emitter.onError(Throwable("User did not authenticate"))
                },
                ifNotNull = {
                    database.child(USER_CHILD).updateChildren(HashMap<String, Any>().apply {
                        this["/${it.id}/"] = it.toMap()
                    }).addOnSuccessListener {
                        emitter.onComplete()

                    }.addOnFailureListener {
                        emitter.onError(Throwable("User can not be updated"))
                    }
                }
            )

        }
    }

    private fun uploadFile(path: String, key: String, onSuccess: (String) -> Unit, onError: (Throwable) -> Unit) {
        val file = File(path)
        val uri = Uri.fromFile(file)
        val child = "$key/${file.name.substring(file.name.lastIndexOf("/") + 1)}"
        val storageReference =
            storage.child(child)
        storageReference.putFile(uri)
            .addOnSuccessListener {
                getUrlFile(
                    child,
                    onSuccess = {
                        onSuccess(it)
                    },
                    onError = {
                        onError(it)
                    }
                )
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    private fun getUrlFile(path: String, onSuccess: (String) -> Unit, onError: (Throwable) -> Unit) {
        val storageReference =
            storage.child(path)

        storageReference.downloadUrl
            .addOnSuccessListener {
                onSuccess(it.toString())
            }
            .addOnFailureListener {
                onError(it)
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