package com.mobgen.presentation.swipe

import com.mobgen.domain.subscribe
import com.mobgen.domain.useCase.GetUsers
import com.mobgen.presentation.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers

class SwipeViewModel constructor(private val getUsers: GetUsers, private val userBindViewMapper: UserBindViewMapper) :
    BaseViewModel<SwipeViewModel.SwipeViewData>() {
    private val usersViewData = mutableListOf<UserBindView>()
    private val swipeViewData = SwipeViewData(Status.LOADING, usersViewData)

    fun addLike(position: Int) {

    }

    fun saveUser() {

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
                        //todo delete above line
                        usersViewData.addAll(
                            listOf(
                                SwipeViewModel.UserBindView(
                                    "",
                                    "",
                                    "",
                                    listOf("https://www.mainewomensnetwork.com/Resources/Pictures/vicki%20aqua%20headshot-smallmwn.jpg")
                                ),
                                SwipeViewModel.UserBindView(
                                    "",
                                    "",
                                    "",
                                    listOf("https://firebasestorage.googleapis.com/v0/b/votingswipe.appspot.com/o/-LayG7v8_iWdbi088SX8%2Frivers.jpg?alt=media&token=5e552d03-c137-4bc0-9661-0487e795adea")
                                ),
                                SwipeViewModel.UserBindView(
                                    "",
                                    "",
                                    "",
                                    listOf("https://firebasestorage.googleapis.com/v0/b/votingswipe.appspot.com/o/-Layg8IJv6XMslGedfT9%2FuserPhoto.jpg?alt=media&token=829a6e4e-ff19-4283-921c-5e7f7de1cd95")
                                ),
                                SwipeViewModel.UserBindView(
                                    "",
                                    "",
                                    "",
                                    listOf("https://www.mainewomensnetwork.com/Resources/Pictures/vicki%20aqua%20headshot-smallmwn.jpg")
                                ),
                                SwipeViewModel.UserBindView(
                                    "",
                                    "",
                                    "",
                                    listOf("https://firebasestorage.googleapis.com/v0/b/votingswipe.appspot.com/o/-LayG7v8_iWdbi088SX8%2Frivers.jpg?alt=media&token=5e552d03-c137-4bc0-9661-0487e795adea")
                                )
                            )
                        )
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
        val name: String,
        val description: String,
        val date: String,
        val photo: List<String>
    )
}
