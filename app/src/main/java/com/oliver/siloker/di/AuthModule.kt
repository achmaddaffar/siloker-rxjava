package com.oliver.siloker.di

import android.app.Application
import com.oliver.siloker.data.network.service.AuthService
import com.oliver.siloker.data.pref.SiLokerPreference
import com.oliver.siloker.data.repository.AuthRepositoryImpl
import com.oliver.siloker.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    fun providesAuthService(
        retrofit: Retrofit
    ): AuthService = retrofit
        .create(AuthService::class.java)

    @Provides
    fun providesAuthRepository(
        authService: AuthService,
        preference: SiLokerPreference,
        application: Application
    ): AuthRepository = AuthRepositoryImpl(authService, preference, application)
}