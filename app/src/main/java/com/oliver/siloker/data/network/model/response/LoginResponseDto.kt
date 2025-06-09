package com.oliver.siloker.data.network.model.response

import com.google.gson.annotations.SerializedName

data class LoginResponseDto(

    @field:SerializedName("token")
    val token: String? = null,

    @field:SerializedName("employer_id")
    val employerId: Long? = null,

    @field:SerializedName("job_seeker_id")
    val jobSeekerId: Long? = null,
)
