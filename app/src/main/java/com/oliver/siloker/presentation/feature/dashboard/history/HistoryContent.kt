package com.oliver.siloker.presentation.feature.dashboard.history

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oliver.siloker.R
import com.oliver.siloker.presentation.component.LoadingDialog
import com.oliver.siloker.presentation.feature.dashboard.component.JobAdCard
import com.oliver.siloker.presentation.ui.theme.AppTypography
import com.oliver.siloker.presentation.util.ErrorMessageUtil.parseNetworkError

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryContent(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<HistoryViewModel>()
    val context = LocalContext.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is HistoryEvent.Error -> {
                    snackbarHostState.showSnackbar(
                        message = event.error.parseNetworkError(context),
                        duration = SnackbarDuration.Short
                    )
                }

                HistoryEvent.Success -> Unit
            }
        }
    }

    if (state.isLoading) LoadingDialog()

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = {
            viewModel.setIsRefreshing(true)
            viewModel.getLatestApplication()
            viewModel.getLatestJobs()
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
                        .padding(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.job_application),
                        style = AppTypography.bodyLarge
                    )
                    Text(
                        text = state.jobApplicationCount.toString(),
                        style = AppTypography.bodyMedium
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
                        .padding(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.advertised_job),
                        style = AppTypography.bodyLarge
                    )
                    Text(
                        text = state.jobAdvertisedCount.toString(),
                        style = AppTypography.bodyMedium
                    )
                }
            }
            Spacer(Modifier.height(12.dp))

            if (state.jobSeekerId > 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.latest_job_application),
                        style = AppTypography.bodyLarge
                    )
                    TextButton(
                        onClick = {

                        }
                    ) {
                        Text(stringResource(R.string.more))
                    }
                }
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {}
                    items(state.latestApplications.size) {
                        val application = state.latestApplications[it]
                        JobAdCard(
                            image = application.imageUrl,
                            title = application.title,
                            description = application.description,
                            onClick = {

                            },
                            modifier = Modifier
                                .width(250.dp)
                                .height(IntrinsicSize.Max)
                        )
                    }
                    item {}
                }
            } else {
                Box(
                    modifier = Modifier
                        .aspectRatio(16f / 9f)
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.you_haven_t_filled_job_seeker_role),
                        style = AppTypography.bodyLarge
                    )
                }
            }
            Spacer(Modifier.height(12.dp))

            if (state.employerId > 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.latest_job_advertised),
                        style = AppTypography.bodyLarge
                    )
                    TextButton(
                        onClick = {

                        }
                    ) {
                        Text(stringResource(R.string.more))
                    }
                }
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {  }
                    items(state.latestJobs.size) {
                        val job = state.latestJobs[it]
                        JobAdCard(
                            image = job.imageUrl,
                            title = job.title,
                            description = job.description,
                            onClick = {

                            },
                            modifier = Modifier
                                .width(250.dp)
                                .height(IntrinsicSize.Max)
                        )
                    }
                    item {  }
                }
            } else {
                Box(
                    modifier = Modifier
                        .aspectRatio(16f / 9f)
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.you_haven_t_filled_employer_role),
                        style = AppTypography.bodyLarge
                    )
                }
            }
        }
    }
}