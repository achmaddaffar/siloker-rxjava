package com.oliver.siloker.domain.model.response

data class JobAdResponseItem(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String,
    val postedAt: String
)
