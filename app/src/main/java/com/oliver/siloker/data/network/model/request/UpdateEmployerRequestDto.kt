package com.oliver.siloker.data.network.model.request

import com.google.gson.annotations.SerializedName

data class UpdateEmployerRequestDto(

	@field:SerializedName("company_name")
	val companyName: String,

	@field:SerializedName("company_website")
	val companyWebsite: String,

	@field:SerializedName("position")
	val position: String
)
