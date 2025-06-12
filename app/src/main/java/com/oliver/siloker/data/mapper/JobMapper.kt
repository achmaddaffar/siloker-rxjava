package com.oliver.siloker.data.mapper

import com.oliver.siloker.BuildConfig
import com.oliver.siloker.data.network.model.response.GetApplicantsResponseDto
import com.oliver.siloker.data.network.model.response.GetApplicantsResponseItemDto
import com.oliver.siloker.data.network.model.response.JobAdResponseDto
import com.oliver.siloker.data.network.model.response.JobDetailResponseDto
import com.oliver.siloker.domain.model.response.ApplicantsResponseItem
import com.oliver.siloker.domain.model.response.GetLatestApplicationResponse
import com.oliver.siloker.domain.model.response.GetLatestJobResponse
import com.oliver.siloker.domain.model.response.JobAdResponseItem
import com.oliver.siloker.domain.model.response.JobDetailResponse

fun JobAdResponseDto.toJobAdDomain() = this.content?.map {
    JobAdResponseItem(
        id = it?.id ?: -1,
        title = it?.title.toString(),
        description = it?.description.toString(),
        imageUrl = "${BuildConfig.BASE_URL}${it?.imageUrl?.drop(1)}",
        postedAt = it?.createdAt.toString()
    )
}

fun JobAdResponseDto.toJobLatestDomain() = GetLatestJobResponse(
    totalItem = this.totalItem ?: -1,
    content = this.toJobAdDomain() ?: emptyList()
)

fun JobDetailResponseDto.toJobDetailDomain() = JobDetailResponse(
    id = this.id ?: -1,
    title = this.title ?: "",
    description = this.description ?: "",
    imageUrl = "${BuildConfig.BASE_URL}${this.imageUrl?.drop(1)}",
    fullName = this.fullName ?: "",
    phoneNumber = this.phoneNumber ?: "",
    bio = this.bio ?: "",
    profilePictureUrl = "${BuildConfig.BASE_URL}${this.profilePictureUrl?.drop(1)}",
    employerId = this.employer?.id ?: -1,
    position = this.employer?.position ?: "",
    companyName = this.employer?.companyName ?: "",
    companyWebsite = this.employer?.companyWebsite ?: "",
    isApplicable = this.isApplicable == true,
    createdAt = this.createdAt ?: ""
)

fun GetApplicantsResponseDto.toApplicantsDomain() = this.content?.map {
    ApplicantsResponseItem(
        id = it?.id ?: -1,
        resumeUrl = it?.resumeUrl ?: "",
        fullName = it?.fullName ?: "",
        phoneNumber = it?.phoneNumber ?: "",
        profilePictureUrl = "${BuildConfig.BASE_URL}${it?.profilePictureUrl?.drop(1)}",
        skills = it?.skills?.map { skill -> skill?.name ?: "" } ?: emptyList(),
        experiences = it?.experiences?.map { exp -> exp?.name ?: "" } ?: emptyList(),
        bio = it?.bio ?: "",
        jobSeekerId = it?.jobSeekerId ?: -1,
        employerPhoneNumber = it?.employerPhoneNumber ?: "",
        cvUrl = it?.cvUrl ?: "",
        status = it?.status ?: "",
        jobId = it?.jobId ?: -1,
        title = it?.title ?: "",
        description = it?.description ?: "",
        imageUrl = "${BuildConfig.BASE_URL}${it?.imageUrl?.drop(1)}",
        updatedAt = it?.updatedAt ?: "",
        createdAt = it?.createdAt ?: ""
    )
}

fun GetApplicantsResponseItemDto.toDomain() = ApplicantsResponseItem(
    id = this.id ?: -1,
    resumeUrl = this.resumeUrl ?: "",
    fullName = this.fullName ?: "",
    phoneNumber = this.phoneNumber ?: "",
    profilePictureUrl = "${BuildConfig.BASE_URL}${this.profilePictureUrl?.drop(1)}",
    skills = this.skills?.map { skill -> skill?.name ?: "" } ?: emptyList(),
    experiences = this.experiences?.map { exp -> exp?.name ?: "" } ?: emptyList(),
    bio = this.bio ?: "",
    jobSeekerId = this.jobSeekerId ?: -1,
    employerPhoneNumber = this.employerPhoneNumber ?: "",
    cvUrl = this.cvUrl ?: "",
    status = this.status ?: "",
    jobId = this.jobId ?: -1,
    title = this.title ?: "",
    description = this.description ?: "",
    imageUrl = "${BuildConfig.BASE_URL}${this.imageUrl?.drop(1)}",
    updatedAt = this.updatedAt ?: "",
    createdAt = this.createdAt ?: ""
)

fun GetApplicantsResponseDto.toApplicantsLatestDomain() = GetLatestApplicationResponse(
    totalItem = this.totalItem ?: -1,
    content = this.toApplicantsDomain() ?: emptyList()
)