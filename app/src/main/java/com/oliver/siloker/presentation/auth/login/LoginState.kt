package com.oliver.siloker.presentation.auth.login

import com.oliver.siloker.domain.error.NetworkError

data class LoginState(
    val isLoading: Boolean = false,
    val phoneNumber: String = "",
    val password: String = "",
)
