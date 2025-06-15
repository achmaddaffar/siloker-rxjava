package com.oliver.siloker.presentation.feature.auth.register

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.oliver.siloker.domain.error.NetworkError
import com.oliver.siloker.domain.error.auth.RegisterError
import com.oliver.siloker.domain.model.request.RegisterRequest
import com.oliver.siloker.domain.repository.AuthRepository
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
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = BehaviorSubject.createDefault(RegisterState())
    val state = _state.hide()

    private val _event = PublishSubject.create<RegisterEvent>()
    val event = _event.hide()

    val isRegisterEnabled = _state
        .map {
            it.profilePictureUri != Uri.EMPTY && it.fullName.isNotEmpty() &&
                    it.phoneNumber.isNotEmpty() && it.password.isNotEmpty() &&
                    it.repeatPassword.isNotEmpty() && it.bio.isNotEmpty()
        }
        .share()
        .replay(1)
        .refCount(15000L, TimeUnit.MILLISECONDS)

    val compositeDisposable = CompositeDisposable()

    fun setFullName(value: String) {
        _state.onNext(_state.value!!.copy(fullName = value))
    }

    fun setPhoneNumber(value: String) {
        _state.onNext(_state.value!!.copy(phoneNumber = value))
    }

    fun setPassword(value: String) {
        _state.onNext(_state.value!!.copy(password = value))
    }

    fun setRepeatPassword(value: String) {
        _state.onNext(_state.value!!.copy(repeatPassword = value))
    }

    fun setBio(value: String) {
        _state.onNext(_state.value!!.copy(bio = value))
    }

    fun setProfilePictureUri(value: Uri) {
        _state.onNext(_state.value!!.copy(profilePictureUri = value))
    }

    fun register() {
        if (_state.value?.password != _state.value?.repeatPassword) {
            _event.onNext(RegisterEvent.Error(RegisterError.PASSWORD_NOT_SAME))
            return
        }

        _state.value?.password?.length?.let {
            if ((it < 6) == true) {
                _event.onNext(RegisterEvent.Error(RegisterError.TOO_SHORT))
                return
            }
        } ?: return

        val request = RegisterRequest(
            fullName = _state.value?.fullName ?: "",
            phoneNumber = _state.value?.phoneNumber ?: "",
            password = _state.value?.password ?: "",
            bio = _state.value?.bio ?: "",
            profilePictureFile = _state.value?.profilePictureUri ?: Uri.EMPTY
        )

        _state.onNext(_state.value!!.copy(isLoading = true))

        val disposable = authRepository
            .register(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                _state.onNext(_state.value!!.copy(isLoading = false))
                result
                    .onSuccess { _event.onNext(RegisterEvent.Success) }
                    .onError { _event.onNext(RegisterEvent.Error(it)) }
            }, {
                _state.onNext(_state.value!!.copy(isLoading = false))
                _event.onNext(RegisterEvent.Error(NetworkError.UNKNOWN))
            })

        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}