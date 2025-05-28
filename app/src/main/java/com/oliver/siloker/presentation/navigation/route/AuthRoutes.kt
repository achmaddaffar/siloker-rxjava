package com.oliver.siloker.presentation.navigation.route

import kotlinx.serialization.Serializable

sealed interface AuthRoutes {

    @Serializable
    data object SplashScreen

    @Serializable
    data object LoginScreen

    @Serializable
    data object RegisterScreen
}