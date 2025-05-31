package com.oliver.siloker.presentation.auth.register

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oliver.siloker.domain.error.auth.RegisterError
import com.oliver.siloker.domain.model.request.RegisterRequest
import com.oliver.siloker.domain.repository.AuthRepository
import com.oliver.siloker.domain.util.onError
import com.oliver.siloker.domain.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<RegisterEvent>()
    val event = _event.asSharedFlow()

    val isRegisterEnabled = _state
        .map {
            it.profilePictureUri != Uri.EMPTY && it.fullName.isNotEmpty() &&
                    it.phoneNumber.isNotEmpty() && it.password.isNotEmpty() &&
                    it.repeatPassword.isNotEmpty() && it.bio.isNotEmpty()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(15000L),
            false
        )

    fun setFullName(value: String) {
        _state.update { it.copy(fullName = value) }
    }

    fun setPhoneNumber(value: String) {
        _state.update { it.copy(phoneNumber = value) }
    }

    fun setPassword(value: String) {
        _state.update { it.copy(password = value) }
    }

    fun setRepeatPassword(value: String) {
        _state.update { it.copy(repeatPassword = value) }
    }

    fun setBio(value: String) {
        _state.update { it.copy(bio = value) }
    }

    fun setProfilePictureUri(value: Uri) {
        _state.update { it.copy(profilePictureUri = value) }
    }

    fun register() {
        viewModelScope.launch {
            if (_state.value.password != _state.value.repeatPassword) {
                _event.emit(RegisterEvent.Error(RegisterError.PASSWORD_NOT_SAME))
                return@launch
            }

            if (_state.value.password.length < 6) {
                _event.emit(RegisterEvent.Error(RegisterError.TOO_SHORT))
                return@launch
            }

            val request = RegisterRequest(
                fullName = _state.value.fullName,
                phoneNumber = _state.value.phoneNumber,
                password = _state.value.password,
                bio = _state.value.bio,
                profilePictureFile = _state.value.profilePictureUri
            )

            authRepository
                .register(request)
                .onStart { _state.update { it.copy(isLoading = true) } }
                .onEach { result ->
                    result
                        .onSuccess { _event.emit(RegisterEvent.Success) }
                        .onError { _event.emit(RegisterEvent.Error(it)) }
                }
                .onCompletion { _state.update { it.copy(isLoading = false) } }
                .launchIn(viewModelScope)
        }
    }
}