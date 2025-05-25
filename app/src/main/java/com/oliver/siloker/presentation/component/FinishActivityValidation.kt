package com.oliver.siloker.presentation.component

import androidx.activity.compose.BackHandler
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.oliver.siloker.R
import kotlinx.coroutines.launch

@Composable
fun FinishActivityValidation(
    snackbarHostState: SnackbarHostState,
    enabled: Boolean = true,
    onFinish: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var lastBackPressTime by rememberSaveable { mutableLongStateOf(0L) }
    val exitTimeThreshold = 2000L
    BackHandler(enabled = enabled) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastBackPressTime < exitTimeThreshold) {
            onFinish()
        } else {
            lastBackPressTime = currentTime
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.press_back_again_to_exit),
                    duration = SnackbarDuration.Short
                )
            }
        }
    }
}