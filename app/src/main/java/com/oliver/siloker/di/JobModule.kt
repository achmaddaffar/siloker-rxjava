package com.oliver.siloker.di

import com.oliver.siloker.data.network.service.JobService
import com.oliver.siloker.data.pref.SiLokerPreference
import com.oliver.siloker.data.repository.JobRepositoryImpl
import com.oliver.siloker.domain.repository.JobRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object JobModule {

    @Provides
    fun providesJobService(
        retrofit: Retrofit
    ): JobService = retrofit.create(JobService::class.java)

    @Provides
    fun providesJobRepository(
        jobService: JobService,
        preference: SiLokerPreference
    ): JobRepository = JobRepositoryImpl(jobService, preference)
}