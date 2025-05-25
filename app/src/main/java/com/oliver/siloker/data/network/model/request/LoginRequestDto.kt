package com.oliver.siloker.data.network.model.request

import com.google.gson.annotations.SerializedName

data class LoginRequestDto(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("phone_number")
	val phoneNumber: String? = null
)
