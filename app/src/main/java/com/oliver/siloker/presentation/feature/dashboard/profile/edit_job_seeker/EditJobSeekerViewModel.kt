package com.oliver.siloker.presentation.feature.dashboard.profile.edit_job_seeker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oliver.siloker.domain.model.request.UpdateJobSeekerRequest
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
class EditJobSeekerViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EditJobSeekerState())
    val state = _state
        .onStart { getProfile() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(30000L),
            EditJobSeekerState()
        )

    private val _event = MutableSharedFlow<EditJobSeekerEvent>()
    val event = _event.asSharedFlow()

    fun setResumeUrl(value: String) {
        _state.update { it.copy(resumeUrl = value) }
    }

    fun setSkill(index: Int, value: String) {
        _state.update {
            val updatedSkills = it.skills.toMutableList().also { it[index] = value }
            it.copy(skills = updatedSkills)
        }
    }

    fun addSkill() {
        _state.update { it.copy(skills = it.skills + "") }
    }

    fun deleteSkill(index: Int) {
        _state.update {
            val updatedSkills = it.skills.toMutableList().also { it.removeAt(index) }
            it.copy(skills = updatedSkills)
        }
    }

    fun setExperience(index: Int, value: String) {
        _state.update {
            val updatedExperiences = it.experiences.toMutableList().also { it[index] = value }
            it.copy(experiences = updatedExperiences)
        }
    }

    fun addExperience() {
        _state.update { it.copy(experiences = it.experiences + "") }
    }

    fun deleteExperience(index: Int) {
        _state.update {
            val updatedExperiences = it.experiences.toMutableList().also { it.removeAt(index) }
            it.copy(experiences = updatedExperiences)
        }
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
                                resumeUrl = response.jobSeeker?.resumeUrl ?: "",
                                skills = response.jobSeeker?.skills ?: emptyList(),
                                experiences = response.jobSeeker?.experiences ?: emptyList()
                            )
                        }
                    }
                    .onError { _event.emit(EditJobSeekerEvent.Error(it)) }
            }
            .onCompletion { _state.update { it.copy(isLoading = false) } }
            .launchIn(viewModelScope)
    }

    fun registerJobSeeker() {
        val skills = _state.value.skills.toMutableList()
            .also { it.removeIf { it.isEmpty() || it.isBlank() } }
        val experiences =
            _state.value.experiences.toMutableList()
                .also { it.removeIf { it.isEmpty() || it.isBlank() } }

        val request = UpdateJobSeekerRequest(
            resumeUrl = _state.value.resumeUrl.trim().toString(),
            skills = skills,
            experiences = experiences
        )

        userRepository
            .updateJobSeeker(request)
            .onStart { _state.update { it.copy(isLoading = true) } }
            .onEach { result ->
                result
                    .onSuccess { _event.emit(EditJobSeekerEvent.Success) }
                    .onError { _event.emit(EditJobSeekerEvent.Error(it)) }
            }
            .onCompletion { _state.update { it.copy(isLoading = false) } }
            .launchIn(viewModelScope)
    }
}