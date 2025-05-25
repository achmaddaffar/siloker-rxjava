package com.oliver.siloker.presentation.navigation.route

import kotlinx.serialization.Serializable

sealed class AuthRoutes {

    @Serializable
    data object SplashScreen

    @Serializable
    data object LoginScreen

    @Serializable
    data object RegisterScreen
}