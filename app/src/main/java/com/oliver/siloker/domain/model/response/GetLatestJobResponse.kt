package com.oliver.siloker.domain.model.response

data class GetLatestJobResponse(
    val totalItem: Int,
    val content: List<JobAdResponseItem>
)
