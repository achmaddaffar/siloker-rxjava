package com.oliver.siloker.domain.repository

import com.oliver.siloker.data.network.model.response.BaseResponse
import com.oliver.siloker.domain.error.NetworkError
import com.oliver.siloker.domain.model.request.LoginRequest
import com.oliver.siloker.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun login(
        request: LoginRequest
    ): Flow<Result<BaseResponse<String>, NetworkError>>

    fun getToken(): String?

    fun logout()
}