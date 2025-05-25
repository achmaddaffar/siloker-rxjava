package com.oliver.siloker.presentation.navigation.route

import kotlinx.serialization.Serializable

sealed class HomeRoutes {

    @Serializable
    data object HomeScreen

    @Serializable
    data object ProfileScreen
}