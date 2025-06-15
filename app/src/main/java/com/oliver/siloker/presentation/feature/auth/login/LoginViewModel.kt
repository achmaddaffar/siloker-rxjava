package com.oliver.siloker.presentation.feature.auth.login

import androidx.lifecycle.ViewModel
import com.oliver.siloker.domain.error.NetworkError
import com.oliver.siloker.domain.model.request.LoginRequest
import com.oliver.siloker.domain.repository.AuthRepository
import com.oliver.siloker.domain.util.onError
import com.oliver.siloker.domain.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = BehaviorSubject.createDefault(LoginState())
    val state = _state.hide()

    private val _event = PublishSubject.create<LoginEvent>()
    val event = _event.hide()

    private val compositeDisposable = CompositeDisposable()

    fun setPhoneNumber(
        value: String
    ) {
        _state.onNext(_state.value!!.copy(phoneNumber = value))
    }

    fun setPassword(
        value: String
    ) {
        _state.onNext(_state.value!!.copy(password = value))
    }

    fun login() {
        _state.onNext(_state.value!!.copy(isLoading = true))
        val request = LoginRequest(
            phoneNumber = _state.value?.phoneNumber ?: "",
            password = _state.value?.password ?: ""
        )

        val disposable = authRepository
            .login(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                _state.onNext(_state.value!!.copy(isLoading = false))
                result
                    .onSuccess { _event.onNext(LoginEvent.Success) }
                    .onError { _event.onNext(LoginEvent.Error(it)) }
            }, {
                _state.onNext(_state.value!!.copy(isLoading = false))
                _event.onNext(LoginEvent.Error(NetworkError.UNKNOWN))
            })

        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}