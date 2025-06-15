package com.oliver.siloker.presentation.feature.dashboard.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.oliver.siloker.domain.error.NetworkError
import com.oliver.siloker.domain.repository.AuthRepository
import com.oliver.siloker.domain.repository.UserRepository
import com.oliver.siloker.domain.util.onError
import com.oliver.siloker.domain.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = BehaviorSubject.createDefault(ProfileState())
    val state = _state
        .doOnSubscribe {
            if (_state.value == ProfileState()) {
                getProfile()
            }
        }
        .share()
        .replay(1)
        .refCount(15000L, TimeUnit.MILLISECONDS)

    private val _event = PublishSubject.create<ProfileEvent>()
    val event = _event.hide()

    private val compositeDisposable = CompositeDisposable()

    fun setIsRefreshing(value: Boolean) {
        _state.onNext(_state.value!!.copy(isRefreshing = value))
    }

    fun getProfile() {
        _state.onNext(_state.value!!.copy(isLoading = true))

        val disposable = userRepository
            .getProfile()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                _state.onNext(_state.value!!.copy(isLoading = false, isRefreshing = false))
                result
                    .onSuccess { response ->
                        _state.onNext(
                            _state.value!!.copy(
                                fullName = response.fullName,
                                bio = response.bio,
                                profilePictureUrl = response.profilePictureUrl,
                                jobSeeker = response.jobSeeker,
                                employer = response.employer
                            )
                        )
                    }
                    .onError { _event.onNext(ProfileEvent.Error(it)) }
            }, {
                _state.onNext(_state.value!!.copy(isLoading = false, isRefreshing = false))
                _event.onNext(ProfileEvent.Error(NetworkError.UNKNOWN))
            })

        compositeDisposable.add(disposable)
    }

    fun uploadProfilePicture(uri: Uri) {
        _state.onNext(_state.value!!.copy(isLoading = true))

        val disposable = userRepository
            .uploadProfilePicture(uri)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                _state.onNext(_state.value!!.copy(isLoading = false, isRefreshing = false))
                result
                    .onSuccess { getProfile() }
                    .onError { _event.onNext(ProfileEvent.Error(it)) }
            }, {
                _state.onNext(_state.value!!.copy(isLoading = false, isRefreshing = false))
                _event.onNext(ProfileEvent.Error(NetworkError.UNKNOWN))

            })

        compositeDisposable.add(disposable)
    }

    fun logout() {
        authRepository.logout()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}