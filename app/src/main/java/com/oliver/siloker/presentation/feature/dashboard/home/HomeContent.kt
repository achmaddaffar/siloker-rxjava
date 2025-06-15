package com.oliver.siloker.presentation.feature.dashboard.home

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.oliver.siloker.presentation.component.JobAdCard
import com.oliver.siloker.presentation.util.rememberRxState
import com.oliver.siloker.rx.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    snackbarHostState: SnackbarHostState,
    onJobAdClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<HomeViewModel>()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val state by rememberRxState(viewModel.state, HomeState())
    val jobAdItems = viewModel.jobs.collectAsLazyPagingItems()

    DisposableEffect(Unit) {
        val disposable = viewModel.pagingError
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Toast.makeText(
                    context,
                    it.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }

        onDispose { disposable.dispose() }
    }

    Column(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        OutlinedTextField(
            value = state.query,
            onValueChange = {
                viewModel.setQuery(it)
            },
            placeholder = { Text(stringResource(R.string.search_jobs_)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search_icon)
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions {
                focusManager.clearFocus()
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        PullToRefreshBox(
            isRefreshing = false,
            onRefresh = {
                jobAdItems.refresh()
            }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (jobAdItems.loadState.refresh is LoadState.Loading) {
                    item {
                        CircularProgressIndicator()
                    }
                }
                items(jobAdItems.itemCount) {
                    val jobAd = jobAdItems[it]
                    JobAdCard(
                        image = jobAd?.imageUrl,
                        title = jobAd?.title.toString(),
                        description = jobAd?.description.toString(),
                        onClick = {
                            jobAd?.id?.let { onJobAdClick(it) }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                if (jobAdItems.loadState.append is LoadState.Loading) {
                    item {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}