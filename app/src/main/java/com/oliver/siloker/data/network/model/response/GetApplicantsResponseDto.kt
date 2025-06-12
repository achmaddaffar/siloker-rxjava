package com.oliver.siloker.data.network.model.response

import com.google.gson.annotations.SerializedName

data class GetApplicantsResponseDto(

    @field:SerializedName("next_page")
    val nextPage: Int? = null,

    @field:SerializedName("total_item")
    val totalItem: Int? = null,

    @field:SerializedName("total_page")
    val totalPage: Int? = null,

    @field:SerializedName("prev_page")
    val prevPage: Int? = null,

    @field:SerializedName("current_page")
    val currentPage: Int? = null,

    @field:SerializedName("content")
    val content: List<GetApplicantsResponseItemDto?>? = null
)

data class SkillsItem(

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Long? = null
)

data class ExperiencesItem(

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Long? = null
)

data class GetApplicantsResponseItemDto(

    @field:SerializedName("skills")
    val skills: List<SkillsItem?>? = null,

    @field:SerializedName("full_name")
    val fullName: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("cv_url")
    val cvUrl: String? = null,

    @field:SerializedName("bio")
    val bio: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("job_seeker_id")
    val jobSeekerId: Long? = null,

    @field:SerializedName("phone_number")
    val phoneNumber: String? = null,

    @field:SerializedName("profile_picture_url")
    val profilePictureUrl: String? = null,

    @field:SerializedName("employer_phone_number")
    val employerPhoneNumber: String? = null,

    @field:SerializedName("id")
    val id: Long? = null,

    @field:SerializedName("resume_url")
    val resumeUrl: String? = null,

    @field:SerializedName("experiences")
    val experiences: List<ExperiencesItem?>? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("job_id")
    val jobId: Long? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("image_url")
    val imageUrl: String? = null
)
