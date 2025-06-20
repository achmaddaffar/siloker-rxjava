package com.oliver.siloker.presentation.feature.dashboard.profile.edit_job_seeker

import androidx.lifecycle.ViewModel
import com.oliver.siloker.domain.error.NetworkError
import com.oliver.siloker.domain.model.request.UpdateJobSeekerRequest
import com.oliver.siloker.domain.repository.UserRepository
import com.oliver.siloker.domain.util.onError
import com.oliver.siloker.domain.util.onSuccess
import com.oliver.siloker.presentation.util.updateState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class EditJobSeekerViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = BehaviorSubject.createDefault(EditJobSeekerState())
    val state = _state
        .doOnSubscribe {
            if (_state.value == EditJobSeekerState()) {
                getProfile()
            }
        }
        .share()
        .replay(1)
        .refCount(15000, TimeUnit.MILLISECONDS)

    private val _event = PublishSubject.create<EditJobSeekerEvent>()
    val event = _event.hide()

    private val compositeDisposable = CompositeDisposable()

    fun setResumeUrl(value: String) {
        _state.updateState { it.copy(resumeUrl = value) }
    }

    fun setSkill(index: Int, value: String) {
        _state.updateState {
            val updatedSkills = it.skills.toMutableList().also { it[index] = value }
            it.copy(skills = updatedSkills)
        }
    }

    fun addSkill() {
        _state.updateState { (it.copy(skills = it.skills + "")) }
    }

    fun deleteSkill(index: Int) {
        val updatedSkills = _state.value?.skills?.toMutableList().also { it?.removeAt(index) }
        _state.onNext(_state.value!!.copy(skills = updatedSkills ?: emptyList()))
    }

    fun setExperience(index: Int, value: String) {
        val updatedExperiences = _state.value?.experiences?.toMutableList().also {
            it?.set(
                index,
                value
            )
        }
        _state.onNext(_state.value!!.copy(experiences = updatedExperiences ?: emptyList()))
    }

    fun addExperience() {
        _state.onNext(
            _state.value!!.copy(
                experiences = (_state.value?.experiences ?: emptyList()) + ""
            )
        )
    }

    fun deleteExperience(index: Int) {
        val updatedExperiences =
            _state.value?.experiences?.toMutableList().also { it?.removeAt(index) }
        _state.onNext(_state.value!!.copy(experiences = updatedExperiences ?: emptyList()))
    }

    private fun getProfile() {
        _state.onNext(_state.value!!.copy(isLoading = true))

        val disposable = userRepository
            .getProfile()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                _state.onNext(_state.value!!.copy(isLoading = false))
                result
                    .onSuccess { response ->
                        _state.onNext(
                            _state.value!!.copy(
                                resumeUrl = response.jobSeeker?.resumeUrl ?: "",
                                skills = response.jobSeeker?.skills ?: emptyList(),
                                experiences = response.jobSeeker?.experiences ?: emptyList()
                            )
                        )
                    }
                    .onError { _event.onNext(EditJobSeekerEvent.Error(it)) }
            }, {
                _state.onNext(_state.value!!.copy(isLoading = false))
                _event.onNext(EditJobSeekerEvent.Error(NetworkError.UNKNOWN))
            })

        compositeDisposable.add(disposable)
    }

    fun registerJobSeeker() {
        val skills = _state.value?.skills?.toMutableList()
            .also { it?.removeIf { it.isEmpty() || it.isBlank() } }
        val experiences =
            _state.value?.experiences?.toMutableList()
                .also { it?.removeIf { it.isEmpty() || it.isBlank() } }

        _state.onNext(_state.value!!.copy(isLoading = true))

        val request = UpdateJobSeekerRequest(
            resumeUrl = _state.value?.resumeUrl?.trim().toString(),
            skills = skills ?: emptyList(),
            experiences = experiences ?: emptyList()
        )

        val disposable = userRepository
            .updateJobSeeker(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                _state.onNext(_state.value!!.copy(isLoading = false))
                result
                    .onSuccess { _event.onNext(EditJobSeekerEvent.Success) }
                    .onError { _event.onNext(EditJobSeekerEvent.Error(it)) }
            }, {
                _state.onNext(_state.value!!.copy(isLoading = false))
                _event.onNext(EditJobSeekerEvent.Error(NetworkError.UNKNOWN))
            })

        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}