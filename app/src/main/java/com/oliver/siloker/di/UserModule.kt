package com.oliver.siloker.di

import android.app.Application
import com.oliver.siloker.data.network.service.UserService
import com.oliver.siloker.data.repository.UserRepositoryImpl
import com.oliver.siloker.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    fun providesUserService(
        retrofit: Retrofit
    ): UserService = retrofit.create(UserService::class.java)

    @Provides
    fun providesUserRepository(
        userService: UserService,
        application: Application
    ): UserRepository = UserRepositoryImpl(userService, application)
}