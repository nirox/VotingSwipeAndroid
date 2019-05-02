package com.mobgen.presentation.register

import com.mobgen.domain.check
import com.mobgen.domain.model.User
import com.mobgen.domain.subscribe
import com.mobgen.domain.useCase.Register
import com.mobgen.presentation.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers

class RegisterViewModel(private val register: Register) : BaseViewModel<RegisterViewModel.RegisterViewData>() {
    private var registerViewData = RegisterViewData(null, null)

    init {
        data.value = registerViewData
    }

    companion object {
        const val NOT_REGISTERED = "can not be registered"
        const val USER_EXISTS = "Email already exists"
        const val NOT_KEY = "push key for users"
        const val FIRST_SLASH = 2
        const val SECOND_SLASH = 5
    }


    fun registration(
        name: String,
        date: String,
        email: String,
        password: String,
        description: String,
        photos: List<String>
    ) {

        checkRegistrationFields(name, date, email, password, description, photos).check(
            ifNotNull = { errorRegister ->
                data.postValue(registerViewData.apply {
                    status = Status.ERROR
                    error = errorRegister
                })
            },
            ifNull = {
                data.value = registerViewData.apply { status = Status.LOADING }

                executeUseCase {
                    register.execute(
                        User(
                            name = name,
                            email = email,
                            birthDay = date,
                            description = description,
                            photos = photos
                        ), password
                    ).subscribe(
                        executor = AndroidSchedulers.mainThread(),
                        onComplete = {
                            data.postValue(registerViewData.apply { status = Status.SUCCESS })
                        },
                        onError = {
                            data.postValue(registerViewData.apply {
                                status = Status.ERROR
                                it.message?.let { errorMessage ->
                                    this.error = when {
                                        errorMessage.contains(NOT_REGISTERED) -> ErrorRegister.NOT_REGISTERED
                                        errorMessage.contains(USER_EXISTS) -> ErrorRegister.USER_EXISTS
                                        errorMessage.contains(NOT_KEY) -> ErrorRegister.NOT_KEY
                                        else -> null
                                    }
                                }
                            })
                        }
                    )
                }
            }
        )
    }

    private fun checkRegistrationFields(
        name: String,
        date: String,
        email: String,
        password: String,
        description: String,
        photos: List<String>
    ): ErrorRegister? {
        when {
            name.isBlank() -> {
                return ErrorRegister.NAME
            }
            date.isBlank() || date[FIRST_SLASH] != '/' || date[SECOND_SLASH] != '/' -> {
                return ErrorRegister.DATE
            }
            !email.contains("@") || !email.contains(".") -> {
                return ErrorRegister.EMAIL
            }
            password.length < RegisterActivity.MIN_CHARS -> {
                return ErrorRegister.PASSWORD
            }
            description.isBlank() -> {
                return ErrorRegister.DESCRIPTION
            }
            photos.isEmpty() -> {
                return ErrorRegister.NOT_PHOTO
            }
            else -> {
                return null
            }
        }
    }

    class RegisterViewData(
        override var status: Status?,
        var error: ErrorRegister?
    ) : Data

    enum class ErrorRegister {
        NAME,
        DATE,
        EMAIL,
        PASSWORD,
        DESCRIPTION,
        NOT_REGISTERED,
        USER_EXISTS,
        NOT_KEY,
        NOT_PHOTO
    }
}


