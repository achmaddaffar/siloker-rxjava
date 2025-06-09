package com.oliver.siloker.domain.model.response

data class GetLatestApplicationResponse(
    val totalItem: Int,
    val content: List<ApplicantsResponseItem>
)
