package com.oliver.siloker.presentation.dashboard.profile

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oliver.siloker.R
import com.oliver.siloker.presentation.component.LoadingDialog
import com.oliver.siloker.presentation.dashboard.component.ProfileEmployerSection
import com.oliver.siloker.presentation.dashboard.component.ProfileJobSeekerSection
import com.oliver.siloker.presentation.dashboard.component.ProfileTopSection
import com.oliver.siloker.presentation.util.ErrorMessageUtil.parseNetworkError

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    snackbarHostState: SnackbarHostState,
    onLogoutNavigate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<ProfileViewModel>()
    val context = LocalContext.current

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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
            )

            if (state.jobSeeker != null)
                ProfileJobSeekerSection(
                    skills = state.jobSeeker?.skills ?: emptyList(),
                    experiences = state.jobSeeker?.experiences ?: emptyList(),
                    onResumeUrlClick = {

                    },
                    onEditClick = {

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

            if (state.employer != null)
                ProfileEmployerSection(
                    companyName = state.employer?.companyName ?: "",
                    position = state.employer?.position ?: "",
                    onCompanyWebsiteClick = {

                    },
                    onEditClick = {

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