package com.oliver.siloker.presentation.feature.job.post

import android.net.Uri

data class PostJobState(
    val selectedImageUri: Uri = Uri.EMPTY,
    val title: String = "",
    val description: String = "",
    val isLoading: Boolean = false
)
