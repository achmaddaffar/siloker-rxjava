package com.oliver.siloker.data.repository

import android.app.Application
import com.oliver.siloker.data.mapper.toDomain
import com.oliver.siloker.data.network.service.UserService
import com.oliver.siloker.data.util.getResponse
import com.oliver.siloker.domain.error.NetworkError
import com.oliver.siloker.domain.model.response.GetProfileResponse
import com.oliver.siloker.domain.repository.UserRepository
import com.oliver.siloker.domain.util.Result
import com.oliver.siloker.domain.util.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl(
    private val userService: UserService,
    private val application: Application
) : UserRepository {

    override fun getProfile(): Flow<Result<GetProfileResponse, NetworkError>> =
        flow {
            val response = getResponse { userService.getProfile() }
            emit(response.map { it.data.toDomain() })
        }
}