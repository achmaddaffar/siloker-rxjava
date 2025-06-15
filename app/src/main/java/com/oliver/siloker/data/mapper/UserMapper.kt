package com.oliver.siloker.data.mapper

import com.oliver.siloker.data.network.model.request.UpdateEmployerRequestDto
import com.oliver.siloker.data.network.model.request.UpdateJobSeekerRequestDto
import com.oliver.siloker.data.network.model.response.EmployerDto
import com.oliver.siloker.data.network.model.response.GetProfileResponseDto
import com.oliver.siloker.data.network.model.response.JobSeekerDto
import com.oliver.siloker.domain.model.request.UpdateEmployerRequest
import com.oliver.siloker.domain.model.request.UpdateJobSeekerRequest
import com.oliver.siloker.domain.model.response.Employer
import com.oliver.siloker.domain.model.response.GetProfileResponse
import com.oliver.siloker.domain.model.response.JobSeeker
import com.oliver.siloker.rx.BuildConfig

fun JobSeekerDto.toJobSeekerDomain() = JobSeeker(
    resumeUrl = this.resumeUrl ?: "",
    skills = this.skills?.map { it?.name ?: "" } ?: emptyList(),
    experiences = this.experiences?.map { it?.name ?: "" } ?: emptyList()
)

fun EmployerDto.toEmployerDomain() = Employer(
    companyWebsite = this.companyWebsite ?: "",
    companyName = this.companyName ?: "",
    position = this.position ?: ""
)

fun GetProfileResponseDto.toGetProfileDomain() = GetProfileResponse(
    id = this.id ?: -1,
    fullName = this.fullName ?: "",
    phoneNumber = this.phoneNumber ?: "",
    password = this.password ?: "",
    bio = this.bio ?: "",
    employer = this.employer?.toEmployerDomain(),
    jobSeeker = this.jobSeeker?.toJobSeekerDomain(),
    profilePictureUrl = "${BuildConfig.BASE_URL}${this.profilePictureUrl?.drop(1)}"
)

fun UpdateJobSeekerRequest.toJobSeekerDto() = UpdateJobSeekerRequestDto(
    skills = this.skills,
    resumeUrl = this.resumeUrl,
    experiences = this.experiences
)

fun UpdateEmployerRequest.toEmployerDto() = UpdateEmployerRequestDto(
    companyName = this.companyName,
    companyWebsite = this.companyWebsite,
    position = this.position
)
