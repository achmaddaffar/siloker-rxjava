package com.oliver.siloker.presentation.feature.dashboard.profile.edit_employer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oliver.siloker.domain.model.request.UpdateEmployerRequest
import com.oliver.siloker.domain.repository.UserRepository
import com.oliver.siloker.domain.util.onError
import com.oliver.siloker.domain.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class EditEmployerViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _state = MutableStateFlow(EditEmployerState())
    val state = _state
        .onStart { getProfile() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(30000L),
            EditEmployerState()
        )

    private val _event = MutableSharedFlow<EditEmployerEvent>()
    val event = _event.asSharedFlow()

    fun setCompanyName(value: String) {
        _state.update { it.copy(companyName = value) }
    }

    fun setCompanyWebsite(value: String) {
        _state.update { it.copy(companyWebsite = value) }
    }

    fun setPosition(value: String) {
        _state.update { it.copy(position = value) }
    }

    private fun getProfile() {
        userRepository
            .getProfile()
            .onStart { _state.update { it.copy(isLoading = true) } }
            .onEach { result ->
                result
                    .onSuccess { response ->
                        _state.update {
                            it.copy(
                                companyName = response.employer?.companyName ?: "",
                                companyWebsite = response.employer?.companyWebsite ?: "",
                                position = response.employer?.position ?: ""
                            )
                        }
                    }
                    .onError { _event.emit(EditEmployerEvent.Error(it)) }
            }
            .onCompletion { _state.update { it.copy(isLoading = false) } }
            .launchIn(viewModelScope)
    }

    fun registerEmployer() {
        val request = UpdateEmployerRequest(
            companyName = _state.value.companyName.trim(),
            position = _state.value.position.trim(),
            companyWebsite = _state.value.companyWebsite.trim()
        )

        userRepository
            .updateEmployer(request)
            .onStart { _state.update { it.copy(isLoading = true) } }
            .onEach { result ->
                result
                    .onSuccess { _event.emit(EditEmployerEvent.Success) }
                    .onError { _event.emit(EditEmployerEvent.Error(it)) }
            }
            .onCompletion { _state.update { it.copy(isLoading = false) } }
            .launchIn(viewModelScope)
    }
}