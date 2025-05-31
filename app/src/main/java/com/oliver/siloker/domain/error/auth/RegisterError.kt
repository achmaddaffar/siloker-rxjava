package com.oliver.siloker.domain.error.auth

import com.oliver.siloker.domain.error.BaseError
import com.oliver.siloker.domain.error.NetworkError

enum class RegisterError : BaseError {
    PASSWORD_NOT_SAME,
    TOO_SHORT
}