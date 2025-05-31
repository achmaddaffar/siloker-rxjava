package com.oliver.siloker.presentation.dashboard.profile

import androidx.lifecycle.ViewModel
import com.oliver.siloker.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    fun logout() {
        authRepository.logout()
    }
}