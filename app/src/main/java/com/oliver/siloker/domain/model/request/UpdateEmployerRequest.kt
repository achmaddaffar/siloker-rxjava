package com.oliver.siloker.domain.model.request

data class UpdateEmployerRequest(
    val companyName: String,
    val position: String,
    val companyWebsite: String
)
