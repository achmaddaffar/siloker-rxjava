package com.oliver.siloker.di

import android.content.Context
import com.oliver.siloker.data.network.interceptor.AuthorizationInterceptor
import com.oliver.siloker.data.pref.SiLokerPreference
import com.oliver.siloker.rx.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun providesSiLokerSharedPreferences(
        @ApplicationContext context: Context
    ): SiLokerPreference =
        SiLokerPreference(context.getSharedPreferences("siloker_pref", Context.MODE_PRIVATE))

    @Provides
    fun providesAuthorizationInterceptor(
        preference: SiLokerPreference
    ): AuthorizationInterceptor = AuthorizationInterceptor(preference)

    @Singleton
    @Provides
    fun providesOkHttpClient(
        authorizationInterceptor: AuthorizationInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authorizationInterceptor)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .callTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    fun providesRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .baseUrl(BuildConfig.BASE_URL + "api/v1/")
        .build()
}