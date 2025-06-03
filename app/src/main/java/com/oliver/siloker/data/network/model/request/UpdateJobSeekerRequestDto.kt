package com.oliver.siloker.data.network.model.request

import com.google.gson.annotations.SerializedName

data class UpdateJobSeekerRequestDto(

	@field:SerializedName("skills")
	val skills: List<String>,

	@field:SerializedName("resume_url")
	val resumeUrl: String,

	@field:SerializedName("experiences")
	val experiences: List<String>
)
