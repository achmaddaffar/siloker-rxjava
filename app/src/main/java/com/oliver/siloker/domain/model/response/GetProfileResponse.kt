package com.oliver.siloker.domain.model.response

data class GetProfileResponse(
    val id: Long,
    val fullName: String,
    val phoneNumber: String,
    val password: String,
    val bio: String,
    val employer: Employer?,
    val jobSeeker: JobSeeker?,
    val profilePictureUrl: String?
)
