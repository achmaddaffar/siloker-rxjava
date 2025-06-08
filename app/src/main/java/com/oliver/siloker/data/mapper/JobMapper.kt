package com.oliver.siloker.data.mapper

import com.oliver.siloker.BuildConfig
import com.oliver.siloker.data.network.model.response.JobAdResponseDto
import com.oliver.siloker.data.network.model.response.JobDetailResponseDto
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