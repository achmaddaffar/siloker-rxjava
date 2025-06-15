package com.oliver.siloker.domain.repository

import com.oliver.siloker.data.network.model.response.BaseResponse
import com.oliver.siloker.domain.error.NetworkError
import com.oliver.siloker.domain.model.request.LoginRequest
import com.oliver.siloker.domain.model.request.RegisterRequest
import com.oliver.siloker.domain.util.Result
import io.reactivex.rxjava3.core.Single

interface AuthRepository {

    fun login(
        request: LoginRequest
    ): Single<Result<Unit, NetworkError>>

    fun register(
        request: RegisterRequest
    ): Single<Result<BaseResponse<Boolean>, NetworkError>>

    fun getToken(): String?

    fun logout()
}