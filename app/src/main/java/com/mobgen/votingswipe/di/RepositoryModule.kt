package com.mobgen.votingswipe.di

import com.mobgen.data.repository.UserRepositoryImpl
import com.mobgen.domain.UserRepository
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {
    @Binds
    abstract fun provideUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

}