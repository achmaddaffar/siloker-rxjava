package com.oliver.siloker.presentation.feature.auth.login

import com.oliver.siloker.domain.error.NetworkError

data class LoginState(
    val isLoading: Boolean = false,
    val phoneNumber: String = "",
    val password: String = "",
)
