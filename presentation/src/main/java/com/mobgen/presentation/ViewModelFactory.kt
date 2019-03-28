package com.mobgen.presentation

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.mobgen.domain.useCase.GetAuthUser
import com.mobgen.presentation.registerActivity.RegisterViewModel
import com.mobgen.presentation.swipe.SwipeViewModel
import javax.inject.Inject


class ViewModelFactory @Inject constructor(
    private val getAuthUser: GetAuthUser
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel()
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel()
            modelClass.isAssignableFrom(SwipeViewModel::class.java) -> SwipeViewModel(getAuthUser)
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        } as T
    }


}