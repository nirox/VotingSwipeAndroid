package com.mobgen.votingswipe.di

import com.mobgen.votingswipe.VotingSwipeApplication
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        MainModule::class
    ]
)
interface AppComponent : AndroidInjector<VotingSwipeApplication> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<VotingSwipeApplication>()
}