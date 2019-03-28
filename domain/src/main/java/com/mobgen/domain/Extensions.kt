package com.mobgen.domain

import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

fun <T> T?.check(ifNotNull: (T) -> Unit, ifNull: () -> Unit = {}) {
    if (this == null) {
        ifNull()
    } else {
        ifNotNull(this)
    }
}

fun Completable.subscribe(
    executor: Scheduler,
    onComplete: () -> Unit,
    onError: (exception: Throwable) -> Unit
): Disposable {
    return this.observeOn(executor)
        .subscribeOn(Schedulers.io())
        .subscribe({
            onComplete()
        }, {
            onError(it)
        })
}

fun <T> Single<T>.subscribe(
    executor: Scheduler,
    onSuccess: (result: T) -> Unit,
    onError: (exception: Throwable) -> Unit
): Disposable {
    return this.observeOn(executor)
        .subscribeOn(Schedulers.io())
        .subscribe({
            onSuccess(it)
        }, {
            onError(it)
        })
}

fun String.addSuffix(suffix: String) = "$this$suffix"