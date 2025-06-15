package com.oliver.siloker.presentation.feature.job.applicant_detail

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.oliver.siloker.presentation.component.LoadingDialog
import com.oliver.siloker.presentation.ui.theme.AppTypography
import com.oliver.siloker.presentation.util.ErrorMessageUtil.parseNetworkError
import com.oliver.siloker.rx.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.launch

@Composable
fun JobApplicantDetailScreen(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<JobApplicantDetailViewModel>()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val state by viewModel.state.subscribeAsState(JobApplicantDetailState())

    val bullet = "\u2022"
    val paragraphStyle = ParagraphStyle(textIndent = TextIndent(restLine = 12.sp))

    DisposableEffect(Unit) {
        val disposable = viewModel.event
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { event ->
                when (event) {
                    is JobApplicantDetailEvent.Error -> {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = event.error.parseNetworkError(context),
                                duration = SnackbarDuration.Short
                            )
                        }
                    }

                    JobApplicantDetailEvent.DownloadSuccess -> {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = context.getString(R.string.cv_has_been_downloaded_successfully),
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }
            }

        onDispose { disposable.dispose() }
    }

    if (state.isLoading) LoadingDialog()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubcomposeAsyncImage(
                model = state.applicant.profilePictureUrl,
                contentDescription = stringResource(R.string.profile_picture),
                loading = {
                    Box(
                        modifier = Modifier.size(60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                },
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = state.applicant.fullName,
                style = AppTypography.bodyLarge
            )
        }

        HorizontalDivider(modifier = Modifier.fillMaxWidth())

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.resume),
                    style = AppTypography.bodyLarge
                )

                Text(
                    text = stringResource(R.string.link),
                    style = AppTypography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        val resumeUrl = state.applicant.resumeUrl
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
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.cv),
                    style = AppTypography.bodyLarge
                )

                Text(
                    text = stringResource(R.string.link),
                    style = AppTypography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        viewModel.downloadCv(state.applicant.cvUrl.drop(1))
                    }
                )
            }

            Text(
                text = stringResource(R.string.skills),
                style = AppTypography.bodyLarge
            )
            Text(
                text = buildAnnotatedString {
                    state.applicant.skills.forEach {
                        withStyle(style = paragraphStyle) {
                            append(bullet)
                            append("\t\t")
                            append(it)
                        }
                    }
                },
                style = AppTypography.bodyMedium
            )

            Text(
                text = stringResource(R.string.experiences),
                style = AppTypography.bodyLarge
            )
            Text(
                text = buildAnnotatedString {
                    state.applicant.experiences.forEach {
                        withStyle(style = paragraphStyle) {
                            append(bullet)
                            append("\t\t")
                            append(it)
                        }
                    }
                },
                style = AppTypography.bodyMedium
            )

            when (state.applicant.status) {
                "PENDING" -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = viewModel::rejectApplicant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            Text(stringResource(R.string.reject))
                        }
                        Spacer(Modifier.width(12.dp))
                        Button(
                            onClick = viewModel::acceptApplicant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            Text(stringResource(R.string.accept))
                        }
                    }
                }

                else -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = stringResource(R.string.status_sc),
                            style = AppTypography.bodyMedium
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = state.applicant.status,
                            color = when (state.applicant.status) {
                                "ACCEPTED" -> Color.Green
                                "REJECTED" -> Color.Red
                                else -> Color.White
                            },
                            style = AppTypography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}