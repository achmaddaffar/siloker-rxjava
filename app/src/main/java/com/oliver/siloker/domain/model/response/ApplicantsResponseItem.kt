package com.oliver.siloker.domain.model.response

data class ApplicantsResponseItem(
    val id: Long = -1,
    val resumeUrl: String = "",
    val fullName: String = "",
    val phoneNumber: String = "",
    val employerPhoneNumber: String = "",
    val profilePictureUrl: String = "",
    val skills: List<String> = emptyList(),
    val experiences: List<String> = emptyList(),
    val bio: String = "",
    val jobSeekerId: Long = -1,
    val cvUrl: String = "",
    val status: String = "",
    val jobId: Long = -1,
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val updatedAt: String = "",
    val createdAt: String = ""
)
