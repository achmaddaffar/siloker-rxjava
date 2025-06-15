package com.oliver.siloker.domain.repository

import android.net.Uri
import com.oliver.siloker.data.network.model.response.BaseResponse
import com.oliver.siloker.domain.error.NetworkError
import com.oliver.siloker.domain.model.request.UpdateEmployerRequest
import com.oliver.siloker.domain.model.request.UpdateJobSeekerRequest
import com.oliver.siloker.domain.model.response.GetProfileResponse
import com.oliver.siloker.domain.util.Result
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getProfile(): Single<Result<GetProfileResponse, NetworkError>>

    fun uploadProfilePicture(uri: Uri): Single<Result<BaseResponse<Boolean>, NetworkError>>

    fun updateJobSeeker(
        request: UpdateJobSeekerRequest
    ): Single<Result<BaseResponse<Boolean>, NetworkError>>

    fun updateEmployer(
        request: UpdateEmployerRequest
    ): Single<Result<BaseResponse<Boolean>, NetworkError>>

    fun getEmployerId(): Long

    fun getJobSeekerId(): Long
}