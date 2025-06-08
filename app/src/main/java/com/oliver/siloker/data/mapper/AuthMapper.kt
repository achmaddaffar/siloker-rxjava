package com.oliver.siloker.data.mapper

import android.app.Application
import com.oliver.siloker.data.network.model.request.LoginRequestDto
import com.oliver.siloker.domain.model.request.LoginRequest
import com.oliver.siloker.domain.model.request.RegisterRequest
import com.oliver.siloker.domain.util.FileUtil
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

fun LoginRequest.toLoginDto() = LoginRequestDto(
    password = this.password,
    phoneNumber = this.phoneNumber
)

fun RegisterRequest.toRegisterMultipartParts(application: Application): List<MultipartBody.Part> {
    val file = FileUtil.uriToFile(this.profilePictureFile, application)
    return listOf(
        MultipartBody.Part.createFormData("full_name", this.fullName),
        MultipartBody.Part.createFormData("phone_number", this.phoneNumber),
        MultipartBody.Part.createFormData("password", this.password),
        MultipartBody.Part.createFormData("bio", this.bio ?: ""),
        MultipartBody.Part.createFormData(
            "profile_picture",
            file.name,
            file.asRequestBody("image/*".toMediaTypeOrNull())
        )
    )
}
