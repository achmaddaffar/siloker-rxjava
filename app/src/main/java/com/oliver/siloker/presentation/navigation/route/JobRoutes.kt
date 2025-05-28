package com.oliver.siloker.presentation.navigation.route

import kotlinx.serialization.Serializable

sealed interface JobRoutes {

    @Serializable
    data object PostJobScreen
}