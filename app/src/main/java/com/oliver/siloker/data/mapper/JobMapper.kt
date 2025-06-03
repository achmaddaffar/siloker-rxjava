package com.oliver.siloker.data.mapper

import com.oliver.siloker.BuildConfig
import com.oliver.siloker.data.network.model.response.JobAdResponseDto
import com.oliver.siloker.domain.model.response.JobAdResponseItem

fun JobAdResponseDto.toJobAdDomain() = this.content?.map {
    JobAdResponseItem(
        id = it?.id ?: -1,
        title = it?.title.toString(),
        description = it?.description.toString(),
        imageUrl = "${BuildConfig.BASE_URL}${it?.imageUrl?.drop(1)}",
        postedAt = it?.createdAt.toString()
    )
}