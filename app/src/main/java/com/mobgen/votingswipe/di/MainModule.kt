package com.mobgen.votingswipe.di

import android.content.Context
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

    @Binds
    abstract fun provideApplicationContext(application: VotingSwipeApplication): Context
}