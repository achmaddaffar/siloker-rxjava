package com.oliver.siloker.presentation.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.oliver.siloker.presentation.auth.login.LoginScreen
import com.oliver.siloker.presentation.auth.register.RegisterScreen
import com.oliver.siloker.presentation.auth.splash.SplashScreen
import com.oliver.siloker.presentation.dashboard.DashboardScreen
import com.oliver.siloker.presentation.job.post.PostJobScreen
import com.oliver.siloker.presentation.navigation.route.AuthRoutes
import com.oliver.siloker.presentation.navigation.route.DashboardRoutes
import com.oliver.siloker.presentation.navigation.route.JobRoutes

@Composable
fun SiLokerNavigation(
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
                    navController.navigate(DashboardRoutes.DashboardScreen) {
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
                    navController.navigate(DashboardRoutes.DashboardScreen) {
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

        composable<DashboardRoutes.DashboardScreen> {
            DashboardScreen(
                onPostJobNavigate = dropUnlessResumed {
                    navController.navigate(JobRoutes.PostJobScreen)
                },
                modifier = modifier
            )
        }

        composable<JobRoutes.PostJobScreen> {
            PostJobScreen(
                onBackNavigate = dropUnlessResumed {
                    navController.navigateUp()
                },
                modifier = modifier
            )
        }
    }
}