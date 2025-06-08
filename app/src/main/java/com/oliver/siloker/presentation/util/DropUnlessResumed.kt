package com.oliver.siloker.presentation.util

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController

fun <T> dropUnlessResumedWithParam(navController: NavController, block: (T) -> Unit): (T) -> Unit {
    return { param: T ->
        if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
            block(param)
        }
    }
}
