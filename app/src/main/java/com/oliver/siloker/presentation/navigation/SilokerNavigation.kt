package com.oliver.siloker.presentation.navigation

import android.app.Activity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.oliver.siloker.presentation.auth.login.LoginScreen
import com.oliver.siloker.presentation.auth.register.RegisterScreen
import com.oliver.siloker.presentation.auth.splash.SplashScreen
import com.oliver.siloker.presentation.navigation.route.AuthRoutes
import com.oliver.siloker.presentation.navigation.route.HomeRoutes

@Composable
fun SilokerNavigation(
    activity: Activity?,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = AuthRoutes.SplashScreen,
        modifier = modifier
    ) {
        composable<AuthRoutes.SplashScreen> {
            SplashScreen(
                modifier = modifier,
                onLoginNavigate = {
                    navController.navigate(AuthRoutes.LoginScreen) {
                        popUpTo<AuthRoutes.SplashScreen> {
                            inclusive = true
                        }
                    }
                },
                onHomeNavigate = {
                    navController.navigate(HomeRoutes.HomeScreen) {
                        popUpTo<AuthRoutes.SplashScreen> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<AuthRoutes.LoginScreen> {
            LoginScreen(
                modifier = modifier,
                onHomeNavigate = {
                    navController.navigate(HomeRoutes.HomeScreen) {
                        popUpTo<AuthRoutes.LoginScreen> {
                            inclusive = true
                        }
                    }
                },
                onRegisterNavigate = dropUnlessResumed {
                    navController.navigate(AuthRoutes.RegisterScreen)
                },
                onBackNavigate = {
                    activity?.finish()
                }
            )
        }

        composable<AuthRoutes.RegisterScreen> {
            RegisterScreen(
                modifier = modifier,
                onLoginNavigate = {

                }
            )
        }

        composable<HomeRoutes.HomeScreen> {
            Text("HOME")
        }
    }
}