package com.oliver.siloker.presentation.feature.dashboard.profile.edit_job_seeker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oliver.siloker.R
import com.oliver.siloker.presentation.component.LoadingDialog
import com.oliver.siloker.presentation.component.ResultDialog
import com.oliver.siloker.presentation.feature.dashboard.component.ExperienceListSection
import com.oliver.siloker.presentation.feature.dashboard.component.SkillListSection
import com.oliver.siloker.presentation.util.ErrorMessageUtil.parseNetworkError

@Composable
fun EditJobSeekerScreen(
    snackbarHostState: SnackbarHostState,
    onBackNavigate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<EditJobSeekerViewModel>()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val state by viewModel.state.collectAsStateWithLifecycle()
    var isSuccessPopUpVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is EditJobSeekerEvent.Error -> {
                    snackbarHostState.showSnackbar(
                        message = event.error.parseNetworkError(context),
                        duration = SnackbarDuration.Short
                    )
                }

                EditJobSeekerEvent.Success -> {
                    isSuccessPopUpVisible = true
                }
            }
        }
    }

    if (state.isLoading) LoadingDialog()
    if (isSuccessPopUpVisible)
        ResultDialog(
            title = stringResource(R.string.success),
            image = R.drawable.illustration_success,
            description = stringResource(R.string.job_seeker_data_has_been_updated_successfully),
            onDismissRequest = { isSuccessPopUpVisible = false },
            primaryButtonText = stringResource(R.string.back),
            onPrimaryButtonClick = onBackNavigate,
            secondaryButtonText = stringResource(R.string.close),
            onSecondaryButtonClick = { isSuccessPopUpVisible = false }
        )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.edit_job_seeker),
            fontSize = 24.sp
        )
        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = state.resumeUrl,
            onValueChange = viewModel::setResumeUrl,
            label = { Text(stringResource(R.string.resume_url)) },
            keyboardActions = KeyboardActions {
                focusManager.clearFocus()
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        SkillListSection(
            skills = state.skills,
            onValueChange = viewModel::setSkill,
            onDeleteSkill = viewModel::deleteSkill,
            onAddSkill = viewModel::addSkill,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        ExperienceListSection(
            experiences = state.experiences,
            onValueChange = viewModel::setExperience,
            onDeleteExperience = viewModel::deleteExperience,
            onAddExperience = viewModel::addExperience,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(20.dp))

        Button(
            onClick = viewModel::registerJobSeeker,
            enabled = !state.isLoading && state.resumeUrl.isNotEmpty() && state.skills.isNotEmpty() && state.experiences.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.edit))
        }
    }
}