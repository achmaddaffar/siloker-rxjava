package com.oliver.siloker.presentation.feature.job.application

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.oliver.siloker.presentation.component.JobApplicationCard
import com.oliver.siloker.presentation.ui.theme.AppTypography
import com.oliver.siloker.rx.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.launch

@Composable
fun JobApplicationListScreen(
    snackbarHostState: SnackbarHostState,
    onJobAdNavigate: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel = hiltViewModel<JobApplicationListViewModel>()
    val scope = rememberCoroutineScope()

    val applicantItems = viewModel.applicants.collectAsLazyPagingItems()

    DisposableEffect(Unit) {
        val disposable = viewModel.pagingError
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = it.message.toString(),
                        duration = SnackbarDuration.Short
                    )
                }
            }

        onDispose { disposable.dispose() }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.job_application),
            style = AppTypography.headlineMedium
        )
        Spacer(Modifier.height(12.dp))
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (applicantItems.loadState.refresh is LoadState.Loading) {
                item {
                    CircularProgressIndicator()
                }
            }
            items(applicantItems.itemCount) {
                val applicant = applicantItems[it]
                JobApplicationCard(
                    image = applicant?.imageUrl,
                    title = applicant?.title.toString(),
                    description = applicant?.description.toString(),
                    status = applicant?.status.toString(),
                    onClick = {
                        onJobAdNavigate(applicant?.jobId ?: -1)
                    },
                    onContactEmployerClick = {
                        val formattedPhoneNumber =
                            if (applicant?.employerPhoneNumber?.startsWith("0") == true)
                                "62${applicant.employerPhoneNumber.drop(1)}"
                            else applicant?.employerPhoneNumber
                        val url = "https://wa.me/$formattedPhoneNumber"
                        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (applicantItems.loadState.append is LoadState.Loading) {
                item {
                    CircularProgressIndicator()
                }
            }
        }
    }
}