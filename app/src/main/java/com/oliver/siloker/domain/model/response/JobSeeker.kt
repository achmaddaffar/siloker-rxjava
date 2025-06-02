package com.oliver.siloker.domain.model.response

data class JobSeeker(
    val resumeUrl: String,
    val skills: List<String>,
    val experiences: List<String>
)
