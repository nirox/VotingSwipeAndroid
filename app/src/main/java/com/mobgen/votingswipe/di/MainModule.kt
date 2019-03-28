package com.mobgen.votingswipe.di

import android.content.Context
import com.mobgen.presentation.LoginActivity
import com.mobgen.presentation.registerActivity.RegisterActivity
import com.mobgen.presentation.swipe.SwipeActivity
import com.mobgen.votingswipe.VotingSwipeApplication
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainModule {
    @ContributesAndroidInjector
    abstract fun get(): LoginActivity

    @ContributesAndroidInjector
    abstract fun getRegisterActivity(): RegisterActivity

    @ContributesAndroidInjector
    abstract fun getSwipeActivity(): SwipeActivity

    @Binds
    abstract fun provideApplicationContext(application: VotingSwipeApplication): Context
}