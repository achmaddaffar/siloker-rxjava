package com.oliver.siloker.data.network.model.response

import com.google.gson.annotations.SerializedName

data class JobAdResponseDto(

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
	val content: List<JobAdResponseItemDto?>? = null
)

data class JobAdResponseItemDto(

	@field:SerializedName("updated_at")
	val updatedAt: Any? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("employer_id")
	val employerId: Int? = null
)
