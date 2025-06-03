package com.oliver.siloker.presentation.feature.auth.register

import android.net.Uri

data class RegisterState(
    val fullName: String = "",
    val phoneNumber: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val bio: String = "",
    val profilePictureUri: Uri = Uri.EMPTY,
    val isLoading: Boolean = false
)
