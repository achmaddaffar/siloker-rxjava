package com.oliver.siloker.data.mapper

import com.oliver.siloker.data.network.model.request.LoginRequestDto
import com.oliver.siloker.domain.model.request.LoginRequest

fun LoginRequest.toDto() = LoginRequestDto(
    password = this.password,
    phoneNumber = this.phoneNumber
)