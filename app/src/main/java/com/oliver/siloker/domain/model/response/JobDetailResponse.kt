package com.oliver.siloker.domain.model.response

data class JobDetailResponse(
    val id: Long = -1,
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val fullName: String = "",
    val phoneNumber: String = "",
    val bio: String = "",
    val profilePictureUrl: String = "",
    val employerId: Long = -1,
    val position: String = "",
    val companyName: String = "",
    val companyWebsite: String = "",
    val isApplicable: Boolean = false,
    val createdAt: String = ""
)
