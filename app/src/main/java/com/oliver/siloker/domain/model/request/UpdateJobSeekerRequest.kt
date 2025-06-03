package com.oliver.siloker.domain.model.request

data class UpdateJobSeekerRequest(
    val resumeUrl: String,
    val skills: List<String>,
    val experiences: List<String>
)
