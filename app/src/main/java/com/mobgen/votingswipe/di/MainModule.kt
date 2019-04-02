package com.mobgen.votingswipe.di

import android.content.Context
import com.mobgen.presentation.detail.DetailActivity
import com.mobgen.presentation.swipe.SwipeActivity
import com.mobgen.presentation.login.LoginActivity
import com.mobgen.presentation.register.RegisterActivity
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

    @ContributesAndroidInjector
    abstract fun getDetailActivity(): DetailActivity

    @Binds
    abstract fun provideApplicationContext(application: VotingSwipeApplication): Context
}