package com.oliver.siloker.data.repository

import android.app.Application
import com.oliver.siloker.data.mapper.toLoginDto
import com.oliver.siloker.data.mapper.toRegisterMultipartParts
import com.oliver.siloker.data.network.model.response.BaseResponse
import com.oliver.siloker.data.network.service.AuthService
import com.oliver.siloker.data.pref.SiLokerPreference
import com.oliver.siloker.data.util.getResponse
import com.oliver.siloker.domain.error.NetworkError
import com.oliver.siloker.domain.model.request.LoginRequest
import com.oliver.siloker.domain.model.request.RegisterRequest
import com.oliver.siloker.domain.repository.AuthRepository
import com.oliver.siloker.domain.util.Result
import com.oliver.siloker.domain.util.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AuthRepositoryImpl(
    private val authService: AuthService,
    private val preference: SiLokerPreference,
    private val application: Application
) : AuthRepository {
    override fun login(request: LoginRequest): Flow<Result<BaseResponse<String>, NetworkError>> =
        flow {
            val result = getResponse { authService.login(request.toLoginDto()) }
            result.onSuccess { preference.putToken(it.data) }
            emit(result)
        }

    override fun register(request: RegisterRequest): Flow<Result<BaseResponse<Boolean>, NetworkError>> =
        flow {
            val result = getResponse {
                authService.register(request.toRegisterMultipartParts(application))
            }
            emit(result)
        }.flowOn(Dispatchers.IO)

    override fun getToken(): String? = preference.getToken()

    override fun logout() {
        preference.putToken(null)
    }
}