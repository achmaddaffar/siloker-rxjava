package com.oliver.siloker.domain.repository

import android.net.Uri
import com.oliver.siloker.data.network.model.response.BaseResponse
import com.oliver.siloker.domain.error.NetworkError
import com.oliver.siloker.domain.model.request.UpdateEmployerRequest
import com.oliver.siloker.domain.model.request.UpdateJobSeekerRequest
import com.oliver.siloker.domain.model.response.GetProfileResponse
import com.oliver.siloker.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getProfile(): Flow<Result<GetProfileResponse, NetworkError>>

    fun uploadProfilePicture(uri: Uri): Flow<Result<BaseResponse<Boolean>, NetworkError>>

    fun updateJobSeeker(
        request: UpdateJobSeekerRequest
    ): Flow<Result<BaseResponse<Boolean>, NetworkError>>

    fun updateEmployer(
        request: UpdateEmployerRequest
    ): Flow<Result<BaseResponse<Boolean>, NetworkError>>

    fun getEmployerId(): Long

    fun getJobSeekerId(): Long
}