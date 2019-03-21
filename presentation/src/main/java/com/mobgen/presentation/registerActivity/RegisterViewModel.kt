package com.mobgen.presentation.registerActivity

import com.mobgen.presentation.BaseViewModel

class RegisterViewModel () : BaseViewModel<RegisterViewModel.LoginViewData>() {
    private var loginViewData = LoginViewData(null)

    init {
        data.value = loginViewData
    }

    fun registration(name: String, date: String, email: String, password: String, description: String) {
        data.value = loginViewData.apply { status = Status.LOADING }

        /*executeUseCase {
            authenticate.execute().subscribe(
                executor = AndroidSchedulers.mainThread(),
                onComplete = {
                    data.postValue(mainViewData.apply {
                        goTwitterFragment = true
                        status = Status.SUCCESS
                        auth = true
                    })
                },
                onError = {
                    data.postValue(mainViewData.apply { status = Status.ERROR })
                    throw  it
                }
            )
        }*/
    }

    class LoginViewData(
        override var status: Status?
    ) : Data
}