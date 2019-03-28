package com.mobgen.presentation.login

import com.mobgen.domain.subscribe
import com.mobgen.domain.useCase.Authenticate
import com.mobgen.presentation.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers

class LoginViewModel(private val authenticate: Authenticate) : BaseViewModel<LoginViewModel.LoginViewData>() {
    private var loginViewData = LoginViewData(null, "")

    init {
        data.value = loginViewData
    }

    fun authenticate(email: String, password: String) {
        data.value = loginViewData.apply { status = Status.LOADING }

        executeUseCase {
            authenticate.execute(email, password).subscribe(
                executor = AndroidSchedulers.mainThread(),
                onSuccess = {
                    data.postValue(loginViewData.apply { status = Status.SUCCESS })
                },
                onError = {
                    data.postValue(loginViewData.apply {
                        status = Status.ERROR
                        errorMessage = it.message ?: ""
                    })
                }
            )
        }
    }

    class LoginViewData(
        override var status: Status?,
        var errorMessage: String
    ) : Data
}