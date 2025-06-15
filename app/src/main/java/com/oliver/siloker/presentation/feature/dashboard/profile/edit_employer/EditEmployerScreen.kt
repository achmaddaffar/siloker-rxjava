package com.oliver.siloker.presentation.feature.dashboard.profile.edit_employer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.oliver.siloker.presentation.component.LoadingDialog
import com.oliver.siloker.presentation.component.ResultDialog
import com.oliver.siloker.presentation.util.ErrorMessageUtil.parseNetworkError
import com.oliver.siloker.rx.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.launch

@Composable
fun EditEmployerScreen(
    snackbarHostState: SnackbarHostState,
    onBackNavigate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<EditEmployerViewModel>()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    val state by viewModel.state.subscribeAsState(EditEmployerState())
    var isSuccessPopUpVisible by rememberSaveable { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val disposable = viewModel.event
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { event ->
                when (event) {
                    is EditEmployerEvent.Error -> {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = event.error.parseNetworkError(context),
                                duration = SnackbarDuration.Short
                            )
                        }
                    }

                    EditEmployerEvent.Success -> {
                        isSuccessPopUpVisible = true
                    }
                }
            }

        onDispose { disposable.dispose() }
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
            text = stringResource(R.string.edit_employer),
            fontSize = 24.sp
        )
        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = state.companyName,
            onValueChange = viewModel::setCompanyName,
            label = { Text(stringResource(R.string.company_name)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions {
                focusManager.moveFocus(FocusDirection.Down)
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = state.companyWebsite,
            onValueChange = viewModel::setCompanyWebsite,
            label = { Text(stringResource(R.string.company_website)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions {
                focusManager.moveFocus(FocusDirection.Down)
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = state.position,
            onValueChange = viewModel::setPosition,
            label = { Text(stringResource(R.string.position)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions {
                focusManager.clearFocus()
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(20.dp))

        Button(
            onClick = viewModel::registerEmployer,
            enabled = !state.isLoading && state.companyName.isNotEmpty() && state.companyWebsite.isNotEmpty() && state.position.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.edit))
        }
    }
}