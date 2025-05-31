package com.oliver.siloker.presentation.dashboard

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.dropUnlessResumed
import com.oliver.siloker.R
import com.oliver.siloker.presentation.dashboard.component.SiLokerBottomNavBar
import com.oliver.siloker.presentation.dashboard.history.HistoryContent
import com.oliver.siloker.presentation.dashboard.home.HomeContent
import com.oliver.siloker.presentation.dashboard.profile.ProfileContent

@Composable
fun DashboardScreen(
    onPostJobNavigate: () -> Unit,
    onLogoutNavigate: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedContentIndex by rememberSaveable { mutableIntStateOf(0) }
    var selectedContentIndexBeforeTransition by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            SiLokerBottomNavBar(
                selectedContentIndex = selectedContentIndex
            ) {
                selectedContentIndex = it
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onPostJobNavigate
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.post_job_ad)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        AnimatedContent(
            targetState = selectedContentIndex,
            transitionSpec = if (selectedContentIndexBeforeTransition < selectedContentIndex) {
                selectedContentIndexBeforeTransition = selectedContentIndex
                {
                    ContentTransform(
                        targetContentEnter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
                        initialContentExit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
                    ).using(SizeTransform(clip = false))
                }
            } else {
                selectedContentIndexBeforeTransition = selectedContentIndex
                {
                    ContentTransform(
                        targetContentEnter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(),
                        initialContentExit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
                    ).using(SizeTransform(clip = false))
                }
            },
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) { tabIndex ->
            when (tabIndex) {
                0 -> HomeContent(
                    modifier = Modifier.fillMaxSize()
                )

                1 -> HistoryContent(
                    modifier = Modifier.fillMaxSize()
                )

                2 -> ProfileContent(
                    onLogoutNavigate = onLogoutNavigate,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}