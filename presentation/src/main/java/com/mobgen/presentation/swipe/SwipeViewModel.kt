package com.mobgen.presentation.swipe

import com.mobgen.domain.model.User
import com.mobgen.domain.subscribe
import com.mobgen.domain.useCase.GetAuthUser
import com.mobgen.presentation.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers

class SwipeViewModel constructor(private val getAuthUser: GetAuthUser) : BaseViewModel<SwipeViewModel.SwipeViewData>() {
    private var user: User? = null
    private val usersViewData = mutableListOf<UserBindView>()


    fun addLike(position: Int) {

    }

    fun saveUser() {

    }

    fun initUser() {
        executeUseCase {
            getAuthUser.execute().subscribe(
                executor = AndroidSchedulers.mainThread(),
                onSuccess = {
                    user = it
                },
                onError = {
                    throw it
                }

            )
        }
    }

    class SwipeViewData(
        override var status: Status?,
        val usersViewData: List<UserBindView> = listOf()
    ) : Data

    class UserBindView(
        val photo: List<String>
    )
}
