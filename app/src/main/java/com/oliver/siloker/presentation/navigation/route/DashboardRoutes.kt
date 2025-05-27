package com.oliver.siloker.presentation.navigation.route

import kotlinx.serialization.Serializable

sealed class DashboardRoutes {

    @Serializable
    data object DashboardScreen
}