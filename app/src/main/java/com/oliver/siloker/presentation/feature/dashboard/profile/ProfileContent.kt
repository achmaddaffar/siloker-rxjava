package com.oliver.siloker.presentation.feature.dashboard.profile

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oliver.siloker.R
import com.oliver.siloker.presentation.component.LoadingDialog
import com.oliver.siloker.presentation.feature.dashboard.component.ProfileEmployerSection
import com.oliver.siloker.presentation.feature.dashboard.component.ProfileJobSeekerSection
import com.oliver.siloker.presentation.feature.dashboard.component.ProfileTopSection
import com.oliver.siloker.presentation.util.ErrorMessageUtil.parseNetworkError

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    snackbarHostState: SnackbarHostState,
    onEditJobSeekerNavigate: () -> Unit,
    onEditEmployerNavigate: () -> Unit,
    onLogoutNavigate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<ProfileViewModel>()
    val context = LocalContext.current
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        it?.let {
            viewModel.uploadProfilePicture(it)
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is ProfileEvent.Error -> {
                    snackbarHostState.showSnackbar(
                        message = event.error.parseNetworkError(context),
                        duration = SnackbarDuration.Short
                    )
                }

                ProfileEvent.Success -> Unit
            }
        }
    }

    if (state.isLoading) LoadingDialog()

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = {
            viewModel.setIsRefreshing(true)
            viewModel.getProfile()
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileTopSection(
                profilePictureUrl = state.profilePictureUrl,
                fullName = state.fullName,
                bio = state.bio,
                onProfileClick = {
                    galleryLauncher.launch("image/*")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
            )

            if (state.jobSeeker != null)
                ProfileJobSeekerSection(
                    skills = state.jobSeeker?.skills ?: emptyList(),
                    experiences = state.jobSeeker?.experiences ?: emptyList(),
                    onResumeUrlClick = {
                        val resumeUrl = state.jobSeeker?.resumeUrl ?: return@ProfileJobSeekerSection
                        val formattedUrl = if (resumeUrl.startsWith("http://") || resumeUrl.startsWith("https://")) {
                            resumeUrl
                        } else {
                            "http://$resumeUrl" // add scheme if missing
                        }
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            formattedUrl.toUri()
                        )
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            else if (!state.isLoading)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(R.string.job_seeker_has_not_been_set))
                }
            OutlinedButton(
                onClick = onEditJobSeekerNavigate,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.edit_job_seeker))
            }

            if (state.employer != null)
                ProfileEmployerSection(
                    companyName = state.employer?.companyName ?: "",
                    position = state.employer?.position ?: "",
                    onCompanyWebsiteClick = {
                        val resumeUrl = state.employer?.companyWebsite ?: return@ProfileEmployerSection
                        val formattedUrl = if (resumeUrl.startsWith("http://") || resumeUrl.startsWith("https://")) {
                            resumeUrl
                        } else {
                            "http://$resumeUrl" // add scheme if missing
                        }
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            formattedUrl.toUri()
                        )
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            else if (!state.isLoading)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(R.string.job_seeker_has_not_been_set))
                }
            OutlinedButton(
                onClick = onEditEmployerNavigate,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.edit_employer))
            }

            HorizontalDivider(modifier = Modifier.fillMaxWidth())

            Button(
                onClick = {
                    viewModel.logout()
                    onLogoutNavigate()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.logout))
            }
        }
    }
}