package com.mobgen.presentation

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.mobgen.presentation.registerActivity.RegisterViewModel
import javax.inject.Inject


class ViewModelFactory @Inject constructor(
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel()
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel()
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        } as T
    }


}