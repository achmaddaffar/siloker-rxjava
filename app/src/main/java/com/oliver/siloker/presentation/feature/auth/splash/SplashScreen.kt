package com.oliver.siloker.presentation.feature.auth.splash

import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.oliver.siloker.presentation.util.LockScreenOrientation
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onLoginNavigate: () -> Unit,
    onHomeNavigate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<SplashViewModel>()
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    LaunchedEffect(Unit) {
        delay(500L)
        if (viewModel.hasLogin()) onHomeNavigate()
        else onLoginNavigate()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "SiLoker",
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}