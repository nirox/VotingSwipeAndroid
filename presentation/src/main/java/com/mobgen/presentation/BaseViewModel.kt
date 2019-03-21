package com.mobgen.presentation

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel<T : BaseViewModel.Data> : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    val data: MutableLiveData<T> = MutableLiveData()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun executeUseCase(useCase: () -> Disposable) {
        compositeDisposable.add(useCase())
    }

    enum class Status {
        LOADING,
        SUCCESS,
        ERROR
    }

    interface Data {
        var status: Status?
    }

}