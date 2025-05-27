package com.oliver.siloker.data.network.interceptor

import com.oliver.siloker.data.pref.SiLokerPreference
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(
    private val preference: SiLokerPreference
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .apply {
                header("Authorization", "Bearer ${preference.getToken()}")
            }
            .build()

        return chain.proceed(request)
    }
}