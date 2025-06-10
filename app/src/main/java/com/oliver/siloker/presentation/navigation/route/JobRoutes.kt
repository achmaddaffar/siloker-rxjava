package com.oliver.siloker.presentation.navigation.route

import kotlinx.serialization.Serializable

sealed interface JobRoutes {

    @Serializable
    data object PostJobScreen : JobRoutes

    @Serializable
    data class JobDetailScreen(val jobId: Long) : JobRoutes

    @Serializable
    data object JobApplicationListScreen : JobRoutes
}