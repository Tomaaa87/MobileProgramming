package com.example.cookingtimer.di

import com.example.cookingtimer.di.repository.AuthRepositoryImpl
import com.example.cookingtimer.di.repository.ProfileRepositoryImpl
import com.example.cookingtimer.di.repository.TimerRepositoryImpl
import com.example.cookingtimer.di.repository.AuthRepository
import com.example.cookingtimer.di.repository.ProfileRepository
import com.example.cookingtimer.di.repository.TimerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        profileRepositoryImpl: ProfileRepositoryImpl
    ): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindTimerRepository(
        timerRepositoryImpl: TimerRepositoryImpl
    ): TimerRepository
}
