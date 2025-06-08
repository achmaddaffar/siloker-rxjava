package com.oliver.siloker.data.network.model.response

import com.google.gson.annotations.SerializedName

data class JobDetailResponseDto(

	@field:SerializedName("full_name")
	val fullName: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("bio")
	val bio: String? = null,

	@field:SerializedName("employer")
	val employer: JobEmployerDto? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("phone_number")
	val phoneNumber: String? = null,

	@field:SerializedName("id")
	val id: Long? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("is_applicable")
	val isApplicable: Boolean? = null,

	@field:SerializedName("profile_picture_url")
	val profilePictureUrl: String? = null
)

data class JobEmployerDto(

	@field:SerializedName("company_name")
	val companyName: String? = null,

	@field:SerializedName("company_website")
	val companyWebsite: String? = null,

	@field:SerializedName("id")
	val id: Long? = null,

	@field:SerializedName("position")
	val position: String? = null
)
