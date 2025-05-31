package com.oliver.siloker.domain.model.request

import android.net.Uri

data class RegisterRequest(
    val fullName: String,
    val phoneNumber: String,
    val password: String,
    val profilePictureFile: Uri,
    val bio: String? = null
)
