package com.mobgen.votingswipe.di

import android.content.Context
import com.mobgen.presentation.LoginActivity
import com.mobgen.votingswipe.VotingSwipeApplication
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainModule {
    @ContributesAndroidInjector
    abstract fun get(): LoginActivity

    @Binds
    abstract fun provideApplicationContext(application: VotingSwipeApplication): Context
}