package com.mobgen.presentation

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.mobgen.domain.useCase.Authenticate
import com.mobgen.domain.useCase.GetUsers
import com.mobgen.domain.useCase.Register
import com.mobgen.domain.useCase.UpdateUser
import com.mobgen.presentation.login.LoginViewModel
import com.mobgen.presentation.register.RegisterViewModel
import javax.inject.Inject


class ViewModelFactory @Inject constructor(
    private val authenticate: Authenticate,
    private val getUsers: GetUsers,
    private val register: Register,
    private val updateUser: UpdateUser
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(
                authenticate, getUsers, updateUser
            )
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(register)
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        } as T
    }
}