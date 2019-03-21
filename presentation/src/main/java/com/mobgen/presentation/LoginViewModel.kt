package com.mobgen.presentation

class LoginViewModel () : BaseViewModel<LoginViewModel.LoginViewData>() {
    private var loginViewData = LoginViewData(null)

    init {
        data.value = loginViewData
    }

    fun authenticate(email: String, password: String) {
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