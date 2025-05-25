package com.oliver.siloker.data.network.model.response

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(

    @field:SerializedName("status_code")
    val statusCode: Int,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: T
)
