package com.oliver.siloker.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oliver.siloker.domain.model.request.LoginRequest
import com.oliver.siloker.domain.repository.AuthRepository
import com.oliver.siloker.domain.util.onError
import com.oliver.siloker.domain.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _event = Channel<LoginEvent>()
    val event = _event.receiveAsFlow()

    fun setPhoneNumber(
        value: String
    ) {
        _state.value = _state.value.copy(
            phoneNumber = value
        )
    }

    fun setPassword(
        value: String
    ) {
        _state.value = _state.value.copy(
            password = value
        )
    }

    fun login() {
        val request = LoginRequest(
            phoneNumber = _state.value.phoneNumber,
            password = _state.value.password
        )

        authRepository
            .login(request)
            .onStart { _state.update { it.copy(isLoading = true) } }
            .onEach { result ->
                result
                    .onSuccess { _event.send(LoginEvent.Success) }
                    .onError { _event.send(LoginEvent.Error(it)) }
            }
            .onCompletion { _state.update { it.copy(isLoading = false) } }
            .launchIn(viewModelScope)
    }
}