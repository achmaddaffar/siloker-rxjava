package com.oliver.siloker.data.mapper

import com.oliver.siloker.BuildConfig
import com.oliver.siloker.data.network.model.response.EmployerDto
import com.oliver.siloker.data.network.model.response.GetProfileResponseDto
import com.oliver.siloker.data.network.model.response.JobSeekerDto
import com.oliver.siloker.domain.model.response.Employer
import com.oliver.siloker.domain.model.response.GetProfileResponse
import com.oliver.siloker.domain.model.response.JobSeeker

fun JobSeekerDto.toDomain() = JobSeeker(
    resumeUrl = this.resumeUrl ?: "",
    skills = this.skills?.map { it?.name ?: "" } ?: emptyList(),
    experiences = this.experiences?.map { it?.name ?: "" } ?: emptyList()
)

fun EmployerDto.toDomain() = Employer(
    companyWebsite = this.companyWebsite ?: "",
    companyName = this.companyName ?: "",
    position = this.position ?: ""
)

fun GetProfileResponseDto.toDomain() = GetProfileResponse(
    id = this.id ?: -1,
    fullName = this.fullName ?: "",
    phoneNumber = this.phoneNumber ?: "",
    password = this.password ?: "",
    bio = this.bio ?: "",
    employer = this.employer?.toDomain(),
    jobSeeker = this.jobSeeker?.toDomain(),
    profilePictureUrl = "${BuildConfig.BASE_URL}${this.profilePictureUrl?.drop(1)}"
)