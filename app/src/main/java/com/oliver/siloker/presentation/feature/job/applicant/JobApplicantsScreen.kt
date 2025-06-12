package com.oliver.siloker.presentation.feature.job.applicant

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import com.oliver.siloker.R
import com.oliver.siloker.domain.util.DateUtil
import com.oliver.siloker.presentation.component.JobApplicantCard
import com.oliver.siloker.presentation.component.LoadingDialog
import com.oliver.siloker.presentation.ui.theme.AppTypography
import com.oliver.siloker.presentation.util.ErrorMessageUtil.parseNetworkError

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobApplicantScreen(
    snackbarHostState: SnackbarHostState,
    onApplicantDetailNavigate: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel = hiltViewModel<JobApplicantsViewModel>()

    val state by viewModel.state.collectAsStateWithLifecycle()
    val applicants = viewModel.applicants.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                JobApplicantsEvent.DownloadSuccess -> {
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.cv_has_been_downloaded_successfully),
                        duration = SnackbarDuration.Short
                    )
                }

                is JobApplicantsEvent.Error -> {
                    snackbarHostState.showSnackbar(
                        message = event.error.parseNetworkError(context),
                        duration = SnackbarDuration.Short
                    )
                }

                is JobApplicantsEvent.PagingError -> {
                    snackbarHostState.showSnackbar(
                        message = event.throwable.message.toString(),
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    if (state.isLoading) LoadingDialog()

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = {
            viewModel.setIsRefreshing(true)
            applicants.refresh()
            viewModel.getJobDetail()
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            SubcomposeAsyncImage(
                model = state.jobDetail.imageUrl,
                contentDescription = stringResource(R.string.job_picture),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(16f / 9f)
                    .fillMaxWidth()
            )
            Spacer(Modifier.height(20.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = state.jobDetail.title,
                    style = AppTypography.headlineSmall
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.description) + ":",
                    style = AppTypography.bodySmall
                )
                Text(
                    text = state.jobDetail.description,
                    style = AppTypography.bodyMedium
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(
                        R.string.posted_on_s,
                        DateUtil.formatIsoToString(state.jobDetail.createdAt)
                    ),
                    style = AppTypography.labelMedium
                )
                Spacer(Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.applicants),
                    style = AppTypography.bodyLarge
                )
                Spacer(Modifier.height(4.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (applicants.loadState.refresh is LoadState.Loading)
                        item { CircularProgressIndicator() }

                    items(applicants.itemCount) {
                        val applicant = applicants[it]

                        JobApplicantCard(
                            fullName = applicant?.fullName.toString(),
                            profilePictureUrl = applicant?.profilePictureUrl.toString(),
                            onClick = {
                                onApplicantDetailNavigate(applicant?.id ?: -1)
                            },
                            skills = applicant?.skills ?: emptyList(),
                            experiences = applicant?.experiences ?: emptyList(),
                            onCvUrlClick = {
                                viewModel.downloadCv(applicant?.cvUrl?.drop(1).toString())
                            },
                            onResumeUrlClick = {
                                val resumeUrl = applicant?.resumeUrl ?: return@JobApplicantCard
                                val formattedUrl =
                                    if (resumeUrl.startsWith("http://") || resumeUrl.startsWith("https://")) {
                                        resumeUrl
                                    } else {
                                        "http://$resumeUrl"
                                    }
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    formattedUrl.toUri()
                                )
                                context.startActivity(intent)
                            },
                            status = applicant?.status.toString(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    if (applicants.loadState.append is LoadState.Loading)
                        item { CircularProgressIndicator() }
                }
            }
        }
    }
}