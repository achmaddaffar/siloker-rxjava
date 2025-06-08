package com.oliver.siloker.presentation.navigation

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.oliver.siloker.presentation.feature.auth.login.LoginScreen
import com.oliver.siloker.presentation.feature.auth.register.RegisterScreen
import com.oliver.siloker.presentation.feature.auth.splash.SplashScreen
import com.oliver.siloker.presentation.feature.dashboard.DashboardScreen
import com.oliver.siloker.presentation.feature.dashboard.profile.edit_employer.EditEmployerScreen
import com.oliver.siloker.presentation.feature.dashboard.profile.edit_job_seeker.EditJobSeekerScreen
import com.oliver.siloker.presentation.feature.job.detail.JobDetailScreen
import com.oliver.siloker.presentation.feature.job.post.PostJobScreen
import com.oliver.siloker.presentation.navigation.route.AuthRoutes
import com.oliver.siloker.presentation.navigation.route.DashboardRoutes
import com.oliver.siloker.presentation.navigation.route.JobRoutes
import com.oliver.siloker.presentation.util.dropUnlessResumedWithParam

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SiLokerNavigation(
    activity: Activity?,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier
    ) { _ ->
        NavHost(
            navController = navController,
            startDestination = AuthRoutes.SplashScreen
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
                    snackbarHostState = snackbarHostState,
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
                    snackbarHostState = snackbarHostState,
                    modifier = modifier,
                    onLoginNavigate = dropUnlessResumed {
                        navController.navigate(AuthRoutes.LoginScreen) {
                            popUpTo<AuthRoutes.LoginScreen> {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable<DashboardRoutes.DashboardScreen> {
                DashboardScreen(
                    snackbarHostState = snackbarHostState,
                    onJobAdNavigate = dropUnlessResumedWithParam(
                        navController
                    ) {
                        navController.navigate(JobRoutes.JobDetailScreen(it))
                    },
                    onPostJobNavigate = dropUnlessResumed {
                        navController.navigate(JobRoutes.PostJobScreen)
                    },
                    onEditJobSeekerNavigate = dropUnlessResumed {
                        navController.navigate(DashboardRoutes.EditJobSeekerScreen)
                    },
                    onEditEmployerNavigate = dropUnlessResumed {
                        navController.navigate(DashboardRoutes.EditEmployerScreen)
                    },
                    onLogoutNavigate = dropUnlessResumed {
                        navController.navigate(AuthRoutes.LoginScreen) {
                            popUpTo<AuthRoutes.LoginScreen> {
                                inclusive = true
                            }
                        }
                    },
                    modifier = modifier
                )
            }

            composable<DashboardRoutes.EditJobSeekerScreen> {
                EditJobSeekerScreen(
                    snackbarHostState = snackbarHostState,
                    onBackNavigate = dropUnlessResumed {
                        navController.navigateUp()
                    },
                    modifier = modifier
                )
            }

            composable<DashboardRoutes.EditEmployerScreen> {
                EditEmployerScreen(
                    snackbarHostState = snackbarHostState,
                    onBackNavigate = dropUnlessResumed {
                        navController.navigateUp()
                    },
                    modifier = modifier
                )
            }

            composable<JobRoutes.PostJobScreen> {
                PostJobScreen(
                    snackbarHostState = snackbarHostState,
                    onBackNavigate = dropUnlessResumed {
                        navController.navigateUp()
                    },
                    modifier = modifier
                )
            }

            composable<JobRoutes.JobDetailScreen> {
                JobDetailScreen(
                    snackbarHostState = snackbarHostState,
                    modifier = modifier
                )
            }
        }
    }
}