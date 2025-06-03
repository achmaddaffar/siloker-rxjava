package com.oliver.siloker.presentation.navigation.route

import kotlinx.serialization.Serializable

sealed interface DashboardRoutes {

    @Serializable
    data object DashboardScreen : DashboardRoutes

    @Serializable
    data object EditJobSeekerScreen : DashboardRoutes

    @Serializable
    data object EditEmployerScreen : DashboardRoutes
}