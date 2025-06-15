package com.oliver.siloker.data.repository

import android.app.Application
import com.oliver.siloker.data.mapper.toLoginDto
import com.oliver.siloker.data.mapper.toRegisterMultipartParts
import com.oliver.siloker.data.network.model.response.BaseResponse
import com.oliver.siloker.data.network.service.AuthService
import com.oliver.siloker.data.pref.SiLokerPreference
import com.oliver.siloker.data.util.getResponseRaw
import com.oliver.siloker.domain.error.NetworkError
import com.oliver.siloker.domain.model.request.LoginRequest
import com.oliver.siloker.domain.model.request.RegisterRequest
import com.oliver.siloker.domain.repository.AuthRepository
import com.oliver.siloker.domain.util.Result
import com.oliver.siloker.domain.util.asEmptyDataResult
import com.oliver.siloker.domain.util.onSuccess
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class AuthRepositoryImpl(
    private val authService: AuthService,
    private val preference: SiLokerPreference,
    private val application: Application
) : AuthRepository {
    override fun login(request: LoginRequest): Single<Result<Unit, NetworkError>> {
        return Single.defer {
            authService.login(request.toLoginDto())
                .map { response ->
                    getResponseRaw(response)
                        .onSuccess {
                            preference.putToken(it.data.token)
                            preference.putEmployerId(it.data.employerId ?: -1)
                            preference.putJobSeekerId(it.data.jobSeekerId ?: -1)
                        }
                        .asEmptyDataResult()
                }
                .subscribeOn(Schedulers.io())
        }
    }

    override fun register(request: RegisterRequest): Single<Result<BaseResponse<Boolean>, NetworkError>> {
        return Single.defer {
            authService.register(request.toRegisterMultipartParts(application))
                .map { response -> getResponseRaw(response) }
                .subscribeOn(Schedulers.io())
        }
    }

    override fun getToken(): String? = preference.getToken()

    override fun logout() {
        preference.putToken(null)
        preference.putEmployerId(-1)
        preference.putJobSeekerId(-1)
    }
}