package com.oliver.siloker.data.network.model.response

import com.google.gson.annotations.SerializedName

data class GetProfileResponseDto(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("full_name")
	val fullName: String? = null,

	@field:SerializedName("job_seeker")
	val jobSeeker: JobSeekerDto? = null,

	@field:SerializedName("profile_picture_url")
	val profilePictureUrl: String? = null,

	@field:SerializedName("bio")
	val bio: String? = null,

	@field:SerializedName("employer")
	val employer: EmployerDto? = null,

	@field:SerializedName("phone_number")
	val phoneNumber: String? = null,

	@field:SerializedName("id")
	val id: Long? = null
)

data class EmployerDto(

	@field:SerializedName("company_name")
	val companyName: String? = null,

	@field:SerializedName("company_website")
	val companyWebsite: String? = null,

	@field:SerializedName("id")
	val id: Long? = null,

	@field:SerializedName("position")
	val position: String? = null
)

data class JobSeekerDto(

	@field:SerializedName("skills")
	val skills: List<SkillsItemDto?>? = null,

	@field:SerializedName("id")
	val id: Long? = null,

	@field:SerializedName("resume_url")
	val resumeUrl: String? = null,

	@field:SerializedName("experiences")
	val experiences: List<ExperiencesItemDto?>? = null
)

data class SkillsItemDto(

	@field:SerializedName("updated_at")
	val updatedAt: Any? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Long? = null
)

data class ExperiencesItemDto(

	@field:SerializedName("updated_at")
	val updatedAt: Any? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Long? = null
)
