package com.oliver.siloker.presentation.feature.job.ad

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.oliver.siloker.R
import com.oliver.siloker.presentation.component.JobAdCard
import com.oliver.siloker.presentation.ui.theme.AppTypography

@Composable
fun JobAdvertisedListScreen(
    snackbarHostState: SnackbarHostState,
    onJobAdClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<JobAdvertisedViewModel>()
    
    val jobs = viewModel.jobs.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.pagingError.collect { error ->
            snackbarHostState.showSnackbar(
                message = error.message.toString(),
                duration = SnackbarDuration.Short
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.advertised_job),
            style = AppTypography.headlineMedium
        )
        Spacer(Modifier.height(12.dp))
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (jobs.loadState.refresh is LoadState.Loading) {
                item {
                    CircularProgressIndicator()
                }
            }
            items(jobs.itemCount) {
                val job = jobs[it]
                JobAdCard(
                    image = job?.imageUrl,
                    title = job?.title.toString(),
                    description = job?.description.toString(),
                    onClick = {
                        onJobAdClick(job?.id ?: -1)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (jobs.loadState.append is LoadState.Loading) {
                item {
                    CircularProgressIndicator()
                }
            }
        }
    }
}