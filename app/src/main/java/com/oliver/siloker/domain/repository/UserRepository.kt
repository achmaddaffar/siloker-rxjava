package com.oliver.siloker.domain.repository

import com.oliver.siloker.domain.error.NetworkError
import com.oliver.siloker.domain.model.response.GetProfileResponse
import com.oliver.siloker.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getProfile(): Flow<Result<GetProfileResponse, NetworkError>>
}