package com.oliver.siloker.domain.model.request

data class LoginRequest(
    val phoneNumber: String,
    val password: String
)
