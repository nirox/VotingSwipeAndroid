package com.mobgen.presentation.swipe

import com.mobgen.domain.subscribe
import com.mobgen.domain.useCase.GetUsers
import com.mobgen.domain.useCase.UpdateUser
import com.mobgen.presentation.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers

class SwipeViewModel constructor(
    private val getUsers: GetUsers,
    private val updateUser: UpdateUser,
    private val userBindViewMapper: UserBindViewMapper
) :
    BaseViewModel<SwipeViewModel.SwipeViewData>() {
    private val usersViewData = mutableListOf<UserBindView>()
    private val swipeViewData = SwipeViewData(Status.LOADING, usersViewData)
    private val likes = mutableListOf<String>()

    fun addLike(position: Int) {
        likes.clear()
        likes.add(usersViewData[position].id)
        executeUseCase {
            updateUser.execute(likes).subscribe(
                executor = AndroidSchedulers.mainThread(),
                onComplete = {},
                onError = {
                    data.value = swipeViewData.apply {
                        status = Status.ERROR
                    }
                })
        }
    }

    fun init() {
        data.value = swipeViewData.apply {
            status = Status.LOADING
        }
        if (usersViewData.isEmpty()) {
            executeUseCase {
                getUsers.execute().subscribe(
                    executor = AndroidSchedulers.mainThread(),
                    onSuccess = {
                        usersViewData.addAll(it.map(userBindViewMapper::map))
                        data.postValue(swipeViewData.apply {
                            status = Status.SUCCESS
                        })
                    },
                    onError = {
                        data.postValue(swipeViewData.apply {
                            status = Status.ERROR
                        })
                    }

                )
            }
        } else {
            data.value = swipeViewData.apply {
                status = Status.SUCCESS
            }
        }

    }

    fun getUserBindView(position: Int) = usersViewData[position]

    class SwipeViewData(
        override var status: Status?,
        var usersViewData: List<UserBindView> = listOf()
    ) : Data

    class UserBindView(
        val id: String,
        val name: String,
        val description: String,
        val date: String,
        val photo: List<String>
    )
}
