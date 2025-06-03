package com.oliver.siloker.presentation.feature.dashboard.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.oliver.siloker.domain.repository.JobRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel @Inject constructor(
    private val jobRepository: JobRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    // TODO("Create Error Events")

    val jobs = state
        .map { it.query }
        .distinctUntilChanged()
        .flatMapLatest { query ->
            delay(500)
            jobRepository.getJobs(query).cachedIn(viewModelScope)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(15000L),
            PagingData.empty()
        )

    fun setQuery(value: String) {
        _state.update { it.copy(query = value) }
    }
}