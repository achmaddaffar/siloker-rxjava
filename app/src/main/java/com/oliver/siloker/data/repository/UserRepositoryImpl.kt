package com.oliver.siloker.data.repository

import android.app.Application
import com.oliver.siloker.data.mapper.toEmployerDto
import com.oliver.siloker.data.mapper.toGetProfileDomain
import com.oliver.siloker.data.mapper.toJobSeekerDto
import com.oliver.siloker.data.network.model.response.BaseResponse
import com.oliver.siloker.data.network.service.UserService
import com.oliver.siloker.data.util.getResponse
import com.oliver.siloker.domain.error.NetworkError
import com.oliver.siloker.domain.model.request.UpdateEmployerRequest
import com.oliver.siloker.domain.model.request.UpdateJobSeekerRequest
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
            emit(response.map { it.data.toGetProfileDomain() })
        }

    override fun updateJobSeeker(request: UpdateJobSeekerRequest): Flow<Result<BaseResponse<Boolean>, NetworkError>> =
        flow {
            val response = getResponse { userService.updateJobSeeker(request.toJobSeekerDto()) }
            emit(response)
        }

    override fun updateEmployer(request: UpdateEmployerRequest): Flow<Result<BaseResponse<Boolean>, NetworkError>> =
        flow {
            val response = getResponse { userService.updateEmployer(request.toEmployerDto()) }
            emit(response)
        }
}